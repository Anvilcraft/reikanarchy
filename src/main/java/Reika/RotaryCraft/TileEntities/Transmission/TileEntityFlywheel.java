/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Transmission;

import java.util.Collection;

import Reika.ChromatiCraft.API.Interfaces.WorldRift;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Libraries.MathSci.ReikaEngLibrary;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.MathSci.ReikaPhysicsHelper;
import Reika.RotaryCraft.API.Event.FlywheelFailureEvent;
import Reika.RotaryCraft.API.Interfaces.ComplexIO;
import Reika.RotaryCraft.API.Power.PowerGenerator;
import Reika.RotaryCraft.API.Power.PowerTracker;
import Reika.RotaryCraft.API.Power.ShaftMerger;
import Reika.RotaryCraft.Auxiliary.Interfaces.PowerSourceTracker;
import Reika.RotaryCraft.Auxiliary.Interfaces.SimpleProvider;
import Reika.RotaryCraft.Auxiliary.PowerSourceList;
import Reika.RotaryCraft.Auxiliary.RotaryAux;
import Reika.RotaryCraft.Auxiliary.ShaftPowerEmitter;
import Reika.RotaryCraft.Auxiliary.TorqueUsage;
import Reika.RotaryCraft.Base.TileEntity.TileEntityTransmissionMachine;
import Reika.RotaryCraft.Registry.ConfigRegistry;
import Reika.RotaryCraft.Registry.Flywheels;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.Registry.SoundRegistry;
import Reika.RotaryCraft.RotaryConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityFlywheel extends TileEntityTransmissionMachine
    implements SimpleProvider, PowerGenerator, ShaftMerger {
    public static final int MINTORQUERATIO = 4;

    private int decayTime;
    private int maxtorque;

    public boolean failed = false;
    private int soundtick = 0;

    private int oppTorque = 0;
    private int updateticks = 0;

    private Flywheels type = Flywheels.WOOD;

    public int getOppTorque(TileEntityFlywheel reading) {
        if (reading.getTypeOrdinal().ordinal() < this.getTypeOrdinal().ordinal())
            return this.getMinTorque() * MINTORQUERATIO;
        return omega < omegain - omegain / 20 ? torque + oppTorque : oppTorque;
    }

    public int getMinTorque() {
        return this.getTypeOrdinal().getMinTorque();
    }

    public void testFailure() {
        double factor = 0.25 * Math.sqrt(omega);
        switch (this.getTypeOrdinal().ordinal()) {
            case 1:
                factor /= 2.5;
            case 3:
                factor *= 1.25;
        }
        factor *= ReikaMathLibrary.doubpow(omega / 65536D, 1.5); //to reduce damage
        double energy = ReikaEngLibrary.rotenergy(this.getDensity(), 0.25, omega, 0.75);
        //ReikaJavaLibrary.pConsole(ReikaPhysicsHelper.getExplosionFromEnergy(energy/factor)+"
        //fails: "+ReikaEngLibrary.mat_rotfailure(this.getDensity(), 0.75, omega,
        //100*this.getStrength()));
        if (ReikaEngLibrary.mat_rotfailure(
                this.getDensity(), 0.75, omega, 100 * this.getStrength()
            ))
            this.fail(worldObj, xCoord, yCoord, zCoord, energy / factor);
    }

    private void fail(World world, int x, int y, int z, double e) {
        failed = true;
        float f = ReikaPhysicsHelper.getExplosionFromEnergy(e);
        if (!world.isRemote)
            world.createExplosion(
                null, x + 0.5, y + 0.5, z + 0.5, f, ConfigRegistry.BLOCKDAMAGE.getState()
            );
        MinecraftForge.EVENT_BUS.post(new FlywheelFailureEvent(this, f));
    }

    private double getDensity() {
        return this.getTypeOrdinal().density;
    }

    public Flywheels getTypeOrdinal() {
        return type;
    }

    private double getStrength() {
        return this.getTypeOrdinal().tensileStrength;
    }

    public int frictionLosses(int speed) {
        int fric = RotaryConfig.friction;
        return (speed * fric);
    }

    @Override
    public void updateEntity(World world, int x, int y, int z, int meta) {
        super.updateTileEntity();
        this.loadType();
        this.getIOSides(world, x, y, z, meta);
        if (failed) {
            omega = 0;
            torque = 0;
            power = 0;
            return;
        }
        if (read != null && write != null)
            this.process(world, x, y, z);
        power = (long) omega * (long) torque;
        this.testFailure();
        this.playSounds();

        this.basicPowerReceiver();
    }

    private void playSounds() {
        if (omega == 0) {
            soundtick = 20000;
            return;
        }
        float pitch = (float) Math.sqrt(omega / 1024F);
        if (pitch == 0)
            pitch = 0.01F;
        soundtick++;
        if (soundtick < -3F / (pitch * pitch) + 69 / (pitch))
            return;
        soundtick = 0;
        SoundRegistry.FLYWHEEL.playSoundAtBlock(
            worldObj, xCoord, yCoord, zCoord, RotaryAux.isMuffled(this) ? 0.3F : 1, pitch
        );
    }

    private void loadType() {
        maxtorque = this.getTypeOrdinal().maxTorque;
        decayTime = this.getTypeOrdinal().decayTime;
    }

    public void getIOSides(World world, int x, int y, int z, int metadata) {
        switch (metadata % 4) {
            case 0:
                read = ForgeDirection.WEST;
                break;
            case 1:
                read = ForgeDirection.EAST;
                break;
            case 2:
                read = ForgeDirection.NORTH;
                break;
            case 3:
                read = ForgeDirection.SOUTH;
                break;
        }
        write = read.getOpposite();
    }

    public void process(World world, int x, int y, int z) {
        omegain = 0;
        tickcount++;
        boolean isCentered = x == xCoord && y == yCoord && z == zCoord;
        int dx = x + read.offsetX;
        int dy = y + read.offsetY;
        int dz = z + read.offsetZ;
        MachineRegistry m = isCentered ? this.getMachine(read)
                                       : MachineRegistry.getMachine(world, dx, dy, dz);
        TileEntity te = isCentered ? this.getAdjacentTileEntity(read)
                                   : world.getTileEntity(dx, dy, dz);
        if (this.isProvider(te)) {
            if (m == MachineRegistry.SHAFT) {
                TileEntityShaft devicein = (TileEntityShaft) te;
                if (devicein.isCross()) {
                    omegain = this.readFromCross(devicein, false);
                    torquein = this.readFromCross(devicein, true);
                } else if (devicein.isWritingTo(this)) {
                    omegain = devicein.omega;
                    torquein = devicein.torque;
                }
            }
            if (te instanceof SimpleProvider) {
                this.copyStandardPower(te);
            }
            if (te instanceof ComplexIO) {
                ComplexIO pwr = (ComplexIO) te;
                ForgeDirection dir = this.getInputForgeDirection().getOpposite();
                omegain = pwr.getSpeedToSide(dir);
                torquein = pwr.getTorqueToSide(dir);
            }
            if (te instanceof ShaftPowerEmitter) {
                ShaftPowerEmitter sp = (ShaftPowerEmitter) te;
                if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                    torquein = sp.getTorque();
                    omegain = sp.getOmega();
                }
            }
            if (m == MachineRegistry.SPLITTER) {
                TileEntitySplitter devicein = (TileEntitySplitter) te;
                if (devicein.isSplitting()) {
                    int om = omega;
                    int tq = torque;
                    this.readFromSplitter(world, x, y, z, devicein);
                    omegain = omega;
                    torquein = torque;
                    omega = om;
                    torque = tq;
                    return;
                } else if (devicein.isWritingTo(this)) {
                    omegain = devicein.omega;
                    torquein = devicein.torque;
                }
            }
        } else if (te instanceof WorldRift) {
            WorldRift sr = (WorldRift) te;
            WorldLocation loc = sr.getLinkTarget();
            if (loc != null)
                this.process(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord);
        } else {
            //omega = 0;
            //torque = 0;
            //power = 0;
            //return;
            //why was this here?!
        }
        double r = 0.75; //this calculates the flywheel datas. You already assumed r=0.75
                         //in previous formulas, so I used that. I set t=0.4 from the
                         //model in-game
        double t = 0.25; //thickness is closer to 0.25m for the actual main disk
        double mass = (t * r * r * Math.PI) * this.getDensity();
        double iner = mass * r * r / 2; //standard inertial moment formula for a cylinder
                                        //with its rotor on the central axis
        updateticks = 0;
        if (torquein >= this.getMinTorque()) {
            oppTorque = TorqueUsage.getTorque(this);
            if (oppTorque <= torquein) {
                if (omega <= omegain) {
                    if ((torquein - oppTorque) / iner < 1
                        && (torquein - oppTorque)
                            > 0) { //making up for the fact that numbers are integers
                        int i = 1;
                        while ((((double) torquein - oppTorque) / iner * i) < 1) {
                            i++;
                        }
                        updateticks = i;
                        if (tickcount % updateticks == 0) {
                            omega++;
                            tickcount = 0;
                        }
                    } else {
                        omega += (torquein - oppTorque)
                            / iner; //increasing omega, following the formula
                                    //torque=inertia*ang.acceleration
                    }
                    if (omega > omegain)
                        omega = omegain; //to prevent oscillations once reached the input
                                         //value
                } else {
                    if ((torquein + oppTorque) / iner
                        < 1) { //same as before, but to reduce omega if it's greater than
                               //the input omega
                        int i = 1;
                        while ((((double) torquein + oppTorque) / iner * i) < 1) {
                            i++;
                        }
                        updateticks = i;
                        if (tickcount % updateticks == 0) {
                            omega--;
                            tickcount = 0;
                        }
                    } else
                        omega = (int) Math.max(0, omega - (torquein + oppTorque) / iner);
                }
                torque = Math.min(torquein, maxtorque);
            } else {
                if ((oppTorque - torquein) / iner
                    < 1) { //this applies the same formula to reduce omega in the case the
                           //input is smaller than the required output
                    int i = 1;
                    while (((oppTorque - torquein) / iner * i) < 1) {
                        i++;
                    }
                    updateticks = i;
                    if (tickcount % updateticks == 0) {
                        omega--;
                        tickcount = 0;
                    }
                } else
                    omega = (int) Math.max(0, omega - (oppTorque - torquein) / iner);
                if (omega < 0)
                    omega = 0;
            }
        } else {
            if (omega == 0) {
                torque = 0;
                tickcount = 0;
            } else { //same as before, but without input
                oppTorque = TorqueUsage.getTorque(this);
                if (oppTorque / iner < 1 && oppTorque > 0) {
                    int i = 1;
                    while ((oppTorque / iner * i) < 1) {
                        i++;
                    }
                    updateticks = i;
                    if (tickcount % updateticks == 0) {
                        omega--;
                    }
                } else
                    omega = (int) Math.max(0, omega - oppTorque / iner);
                if (omega < 0)
                    omega = 0;
            }
        }
        if (omega <= 0)
            torque = 0;
    }

    private void decrSpeed() {
        if (omega > 0 && tickcount >= decayTime) {
            omega--;
            tickcount = 0;
        }
    }

    public int readFromCross(TileEntityShaft cross, boolean isTorque) {
        if (cross.isWritingTo(this)) {
            if (isTorque)
                return cross.readtorque[0];
            return cross.readomega[0];
        } else if (cross.isWritingTo2(this)) {
            if (isTorque)
                return cross.readtorque[1];
            return cross.readomega[1];
        } else
            return 0; //not its output
    }

    @Override
    protected void writeSyncTag(NBTTagCompound NBT) {
        super.writeSyncTag(NBT);
        NBT.setBoolean("failed", failed);

        NBT.setInteger("typeIdx", type.ordinal());
    }

    @Override
    protected void readSyncTag(NBTTagCompound NBT) {
        super.readSyncTag(NBT);
        failed = NBT.getBoolean("failed");

        type = Flywheels.list[NBT.getInteger("typeIdx")];
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(World world, int x, int y, int z) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getTile() {
        return MachineRegistry.FLYWHEEL;
    }

    @Override
    public int getRedstoneOverride() {
        return 15 * omega / this.getTypeOrdinal().maxSpeed;
    }

    @Override
    public void onEMP() {}

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        /*return new PowerSourceList().addSource(this);*/
        PowerSourceList pwr = new PowerSourceList();
        pwr.addAll(pwr.getAllFrom(
            worldObj,
            read,
            xCoord + read.offsetX,
            yCoord + read.offsetY,
            zCoord + read.offsetZ,
            this,
            caller
        ));
        if (pwr.isEmpty())
            pwr.addSource(this);
        return pwr;
    }

    @Override
    public long getMaxPower() {
        return maxtorque * omega;
    }

    @Override
    public long getCurrentPower() {
        return power;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int getEmittingX() {
        return xCoord + write.offsetX;
    }

    @Override
    public int getEmittingY() {
        return yCoord + write.offsetY;
    }

    @Override
    public int getEmittingZ() {
        return zCoord + write.offsetZ;
    }

    @Override
    public void onPowerLooped(PowerTracker pwr) {
        omega = torque = 0;
        power = 0;
    }

    @Override
    public void getAllOutputs(Collection<TileEntity> c, ForgeDirection dir) {
        if (dir == read)
            c.add(this.getAdjacentTileEntity(write));
    }

    @Override
    public void fail() {
        this.fail(
            worldObj, xCoord, yCoord, zCoord, ReikaPhysicsHelper.getEnergyFromExplosion(4)
        );
    }

    public void setMaterialFromItem(ItemStack is) {
        type = Flywheels.getMaterialFromFlywheelItem(is);
        if (worldObj != null)
            this.syncAllData(true);
    }
}
