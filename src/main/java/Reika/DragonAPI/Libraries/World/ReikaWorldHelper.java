/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Libraries.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import Reika.DragonAPI.APIPacketHandler;
import Reika.DragonAPI.APIPacketHandler.PacketIDs;
import Reika.DragonAPI.Auxiliary.Trackers.SpecialDayTracker;
import Reika.DragonAPI.Base.BlockTieredResource;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.DragonAPIInit;
import Reika.DragonAPI.Exception.MisuseException;
import Reika.DragonAPI.Extras.BlockProperties;
import Reika.DragonAPI.IO.ReikaFileReader;
import Reika.DragonAPI.Instantiable.Data.Collections.RelativePositionList;
import Reika.DragonAPI.Instantiable.Data.Collections.TimedSet;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldChunk;
import Reika.DragonAPI.Instantiable.Event.IceFreezeEvent;
import Reika.DragonAPI.Instantiable.Event.MobTargetingEvent;
import Reika.DragonAPI.Instantiable.Math.Noise.Simplex3DGenerator;
import Reika.DragonAPI.Instantiable.ResettableRandom;
import Reika.DragonAPI.Instantiable.TemperatureEffect;
import Reika.DragonAPI.Instantiable.TemperatureEffect.TemperatureCallback;
import Reika.DragonAPI.Interfaces.Callbacks.PositionCallable;
import Reika.DragonAPI.Interfaces.CustomTemperatureBiome;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.MathSci.ReikaPhysicsHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaVectorHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaCropHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaPlantHelper;
import Reika.DragonAPI.Libraries.ReikaEntityHelper;
import Reika.DragonAPI.Libraries.ReikaFluidHelper;
import Reika.DragonAPI.Libraries.ReikaNBTHelper.NBTTypes;
import Reika.DragonAPI.Libraries.ReikaSpawnerHelper;
import Reika.DragonAPI.ModInteract.AtmosphereHandler;
import Reika.DragonAPI.ModInteract.DeepInteract.EnderIOFacadeHandler;
import Reika.DragonAPI.ModInteract.DeepInteract.PlanetDimensionHandler;
import Reika.DragonAPI.ModInteract.DeepInteract.ReikaMystcraftHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.NaturaBlockHandler;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.InterfaceCache;
import Reika.DragonAPI.ModRegistry.ModCropList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import org.apache.commons.lang3.tuple.ImmutablePair;

public final class ReikaWorldHelper extends DragonAPICore {
    private static final TimedSet<WorldChunk> forcingChunkSet = new TimedSet();
    private static Field moddedGeneratorList;
    private static Method computeModdedGeneratorList;
    private static final Random moddedGenRand_Calcer = new Random();
    private static final ResettableRandom moddedGenRand = new ResettableRandom();

    private static final HashMap<Material, TemperatureEffect> temperatureBlockEffects
        = new HashMap();
    private static final HashMap<String, WorldID> worldIDMap = new HashMap();
    private static final HashMap<Integer, String> worldKeys = new HashMap();

    private static final HashMap<Class, Boolean> fakeWorldTypes = new HashMap();

    private static final HashMap<ImmutablePair<Integer, Long>, Simplex3DGenerator>
        tempNoise = new HashMap();

    private static final double TEMP_NOISE_BASE = 10;

    public static WorldIDBase clientWorldID;

    static {
        try {
            moddedGeneratorList
                = GameRegistry.class.getDeclaredField("sortedGeneratorList");
            moddedGeneratorList.setAccessible(true);

            computeModdedGeneratorList
                = GameRegistry.class.getDeclaredMethod("computeSortedGeneratorList");
            computeModdedGeneratorList.setAccessible(true);

            temperatureBlockEffects.put(Material.rock, TemperatureEffect.rockMelting);
            temperatureBlockEffects.put(Material.iron, TemperatureEffect.rockMelting);
            temperatureBlockEffects.put(Material.ice, TemperatureEffect.iceMelting);
            temperatureBlockEffects.put(
                Material.snow, TemperatureEffect.snowVaporization
            );
            temperatureBlockEffects.put(
                Material.craftedSnow, TemperatureEffect.snowVaporization
            );
            temperatureBlockEffects.put(Material.cloth, TemperatureEffect.woolIgnition);
            temperatureBlockEffects.put(Material.wood, TemperatureEffect.woodIgnition);
            temperatureBlockEffects.put(Material.grass, TemperatureEffect.groundGlassing);
            temperatureBlockEffects.put(Material.sand, TemperatureEffect.groundGlassing);
            temperatureBlockEffects.put(
                Material.ground, TemperatureEffect.groundGlassing
            );
            temperatureBlockEffects.put(Material.leaves, TemperatureEffect.plantIgnition);
            temperatureBlockEffects.put(Material.plants, TemperatureEffect.plantIgnition);
            temperatureBlockEffects.put(Material.vine, TemperatureEffect.plantIgnition);
            temperatureBlockEffects.put(Material.web, TemperatureEffect.plantIgnition);
            temperatureBlockEffects.put(Material.tnt, TemperatureEffect.tntIgnition);
        } catch (Exception e) {
            throw new RuntimeException(
                "Could not find GameRegistry IWorldGenerator data!", e
            );
        }
    }

    private static Simplex3DGenerator getOrCreateTemperatureNoise(World world) {
        ImmutablePair<Integer, Long> pair
            = new ImmutablePair(world.provider.dimensionId, world.getSeed());
        Simplex3DGenerator gen = tempNoise.get(pair);
        if (gen == null | true) {
            gen = new Simplex3DGenerator(world.getSeed());
            gen.setFrequency(1 / 20D);
            //gen.addOctave(3.7, 0.17, 117.6);
            tempNoise.put(pair, gen);
        }
        return gen;
    }

    public static boolean softBlocks(IBlockAccess world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (b == Blocks.air)
            return true;
        if (b == Blocks.piston_extension)
            return false;
        if (ReikaBlockHelper.isLiquid(b))
            return true;
        if (b.isReplaceable(world, x, y, z))
            return true;
        if (b.isAir(world, x, y, z))
            return true;
        if (b == Blocks.vine)
            return true;
        return (BlockProperties.isSoft(b));
    }

    public static boolean softBlocks(Block id) {
        if (id == Blocks.air || id.getMaterial() == Material.air)
            return true;
        return BlockProperties.isSoft(id);
    }

    public static boolean flammable(IBlockAccess world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (b == Blocks.air)
            return false;
        if (b == Blocks.trapdoor || b == Blocks.chest)
            return false;
        if (b.getFlammability(world, x, y, z, ForgeDirection.UP) > 0)
            return true;
        return BlockProperties.isFlammable(b);
    }

    public static boolean flammable(Block id) {
        if (id == Blocks.air)
            return false;
        return BlockProperties.isFlammable(id);
    }

    public static boolean nonSolidBlocks(IBlockAccess world, int x, int y, int z) {
        return BlockProperties.isNonSolid(world.getBlock(x, y, z));
    }

    /**
     *Caps the metadata at a certain value (eg, for leaves, metas are from 0-11, but
     *there are only 4 types, and each type has 3 metas). Args: Initial metadata, cap (#
     *of types)
    */
    public static int capMetadata(int meta, int cap) {
        while (meta >= cap)
            meta -= cap;
        return meta;
    }

    public static Material getMaterial(IBlockAccess world, int x, int y, int z) {
        return (!(world instanceof World)
                || ((World) world).checkChunksExist(x, y, z, x, y, z))
            ? world.getBlock(x, y, z).getMaterial()
            : Material.air;
    }

    public static boolean isAirBlock(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).isAir(world, x, y, z);
    }

    /*
    public static void setBlock(World world, int x, int y, int z, ItemStack is) {
        setBlock(world, x, y, z, is, 3);
    }

    public static void setBlock(World world, int x, int y, int z, ItemStack is, int flag)
    { world.setBlock(x, y, z, Block.getBlockFromItem(is.getItem()), is.getItemDamage(),
    flag);
    }
     */
    public static int findFluidSurface(World world, int x, int y, int z) {
        return findFluidSurface(world, x, y, z, null);
    }

    /**
     * Finds the top edge of the top fluid block in the column. Returns -1 if not a fluid
     * block even at the search start.
     */
    public static int findFluidSurface(World world, int x, int y, int z, Fluid look) {
        Block b = world.getBlock(x, y, z);

        if (b == Blocks.flowing_water)
            b = Blocks.water;
        else if (b == Blocks.flowing_lava)
            b = Blocks.lava;

        Fluid f = ReikaFluidHelper.lookupFluidForBlock(b);
        if (f == null || (look != null && f != look))
            return -1;

        Block b2 = b;
        int dy = y;

        while ((b2 == b) && dy < 256) {
            dy++;
            b2 = world.getBlock(x, dy, z);
            if (b2 == Blocks.flowing_water)
                b2 = Blocks.water;
            else if (b2 == Blocks.flowing_lava)
                b2 = Blocks.lava;
        }
        return dy;
    }

    /**
     *Search for a specific block in a range. Returns true if found. Cannot identify if
     *found more than one, or where the found one(s) is/are. May be CPU-intensive. Args:
     *World, this.x,y,z, search range, target id
    */
    public static boolean
    findNearBlock(World world, int x, int y, int z, int range, Block id) {
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                for (int k = -range; k <= range; k++) {
                    if (world.getBlock(x + i, y + j, z + k) == id)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     *Search for a specific block in a range. Returns true if found. Cannot identify if
     *found more than one. May be CPU-intensive. Args: World, this.x,y,z, search range,
     *target id, meta
    */
    public static Coordinate
    findNearBlock(World world, int x, int y, int z, int range, Block id, int meta) {
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                for (int k = -range; k <= range; k++) {
                    if (world.getBlock(x + i, y + j, z + k) == id
                        && world.getBlockMetadata(x + i, y + j, z + k) == meta)
                        return new Coordinate(x + i, y + j, z + k);
                }
            }
        }
        return null;
    }

    /**
     *Search for a specific block in a range. Returns number found. Cannot identify
     *where they are. May be CPU-intensive. Args: World, this.x,y,z, search range, target
     *id
    */
    public static int
    findNearBlocks(World world, int x, int y, int z, int range, Block id) {
        int count = 0;
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                for (int k = -range; k <= range; k++) {
                    if (world.getBlock(x + i, y + j, z + k) == id)
                        count++;
                }
            }
        }
        return count;
    }

    /**
     *Tests for if a block of a certain id is in the "sights" of a directional block (eg
     *dispenser). Returns the number of blocks away it is. If not found, returns 0 (an
     *impossibility). Args: World, this.x,y,z, search range, target id, direction "f"
    */
    public static int
    isLookingAt(World world, int x, int y, int z, int range, Block id, int f) {
        Block idfound = Blocks.air;

        switch (f) {
            case 0: //facing north (-z);
                for (int i = 0; i < range; i++) {
                    idfound = world.getBlock(x, y, z - i);
                    if (idfound == id)
                        return i;
                }
                break;
            case 1: //facing east (-x);
                for (int i = 0; i < range; i++) {
                    idfound = world.getBlock(x - i, y, z);
                    if (idfound == id)
                        return i;
                }
                break;
            case 2: //facing south (+z);
                for (int i = 0; i < range; i++) {
                    idfound = world.getBlock(x, y, z + i);
                    if (idfound == id)
                        return i;
                }
                break;
            case 3: //facing west (+x);
                for (int i = 0; i < range; i++) {
                    idfound = world.getBlock(x + i, y, z);
                    if (idfound == id)
                        return i;
                }
                break;
        }
        return 0;
    }

    public static ForgeDirection checkForAdjNonCube(World world, int x, int y, int z) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            if (world.checkChunksExist(dx, dy, dz, dx, dy, dz)) {
                Block id2 = world.getBlock(dx, dy, dz);
                if (!id2.isOpaqueCube())
                    return dir;
            }
        }
        return null;
    }

    /**
     *Returns the direction in which a block of the specified ID was found.
     *Returns null if not found. Args: World, x,y,z, id to search.
    */
    public static ForgeDirection
    checkForAdjBlock(World world, int x, int y, int z, Block id) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            if (world.checkChunksExist(dx, dy, dz, dx, dy, dz)) {
                Block id2 = world.getBlock(dx, dy, dz);
                if (id == id2)
                    return dir;
            }
        }
        return null;
    }

    /**
     *Returns the direction in which a block of the specified ID was found.
     *Returns null if not found. Args: World, x,y,z, id to search.
    */
    public static Coordinate
    checkForAdjBlockWithCorners(World world, int x, int y, int z, Block id) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    int dx = x + i;
                    int dy = y + j;
                    int dz = z + k;
                    if (world.checkChunksExist(dx, dy, dz, dx, dy, dz)) {
                        Block id2 = world.getBlock(dx, dy, dz);
                        if (id == id2)
                            return new Coordinate(i, j, k);
                    }
                }
            }
        }
        return null;
    }

    /**
     *Returns the direction in which a block of the specified ID was found.
     *Returns -1 if not found. Args: World, x,y,z, id to search.
    */
    public static ForgeDirection
    checkForAdjBlock(World world, int x, int y, int z, Block id, int meta) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            if (world.checkChunksExist(dx, dy, dz, dx, dy, dz)) {
                Block id2 = world.getBlock(dx, dy, dz);
                int meta2 = world.getBlockMetadata(dx, dy, dz);
                if (id == id2 && (meta2 == meta || meta == -1))
                    return dir;
            }
        }
        return null;
    }

    /**
     *Returns the direction in which a block of the specified material was found.
     *Returns -1 if not found. Args: World, x,y,z, material to search.
    */
    public static ForgeDirection
    checkForAdjMaterial(World world, int x, int y, int z, Material mat) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            if (world.checkChunksExist(dx, dy, dz, dx, dy, dz)) {
                Material mat2 = getMaterial(world, dx, dy, dz);
                if (ReikaBlockHelper.matchMaterialsLoosely(mat, mat2))
                    return dir;
            }
        }
        return null;
    }

    /**
     *Returns the direction in which a source block of the specified liquid was found.
     *Returns -1 if not found. Args: World, x,y,z, material (water/lava) to search.
    */
    public static ForgeDirection
    checkForAdjSourceBlock(World world, int x, int y, int z, Material mat) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            if (world.checkChunksExist(dx, dy, dz, dx, dy, dz)) {
                Material mat2 = getMaterial(world, dx, dy, dz);
                if (mat == mat2 && world.getBlockMetadata(dx, dy, dz) == 0)
                    return dir;
            }
        }
        return null;
    }

    public static ForgeDirection checkForAdjTile(
        World world, int x, int y, int z, Class<? extends TileEntity> c, boolean inherit
    ) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            if (world.checkChunksExist(dx, dy, dz, dx, dy, dz)) {
                TileEntity te = world.getTileEntity(dx, dy, dz);
                if (te != null) {
                    if (te.getClass() == c
                        || (inherit && te.getClass().isAssignableFrom(c)))
                        return dir;
                }
            }
        }
        return null;
    }

    /**
     *Edits a block adjacent to the passed arguments, on the specified side.
     *Args: World, x, y, z, side, id to change to, metadata to change to
    */
    public static void changeAdjBlock(
        World world, int x, int y, int z, ForgeDirection side, Block id, int meta
    ) {
        int dx = x + side.offsetX;
        int dy = y + side.offsetY;
        int dz = z + side.offsetZ;
        if (world.checkChunksExist(dx, dy, dz, dx, dy, dz)) {
            world.setBlock(dx, dy, dz, id, meta, 3);
        }
    }

    /**
     * Applies temperature effects to the environment. Args: World, x, y, z, temperature
     */
    public static void
    temperatureEnvironment(World world, int x, int y, int z, int temperature) {
        temperatureEnvironment(world, x, y, z, temperature, null);
    }

    /**
     * Applies temperature effects to the environment. Args: World, x, y, z, temperature
     */
    public static void temperatureEnvironment(
        World world, int x, int y, int z, int temperature, TemperatureCallback callback
    ) {
        temperatureEnvironment(world, x, y, z, temperature, false, callback);
    }

    /**
     * Applies temperature effects to the environment. Args: World, x, y, z, temperature
     */
    public static void temperatureEnvironment(
        World world,
        int x,
        int y,
        int z,
        int temperature,
        boolean corners,
        TemperatureCallback callback
    ) {
        if (temperature < 0) {
            for (int i = 0; i < 6; i++) {
                ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[i];
                int dx = x + side.offsetX;
                int dy = y + side.offsetY;
                int dz = z + side.offsetZ;
                if (getFluid(world, dx, dy, dz) == FluidRegistry.WATER
                    && !InterfaceCache.STREAM.instanceOf(world.getBlock(dx, dy, dz))) {
                    if (IceFreezeEvent.fire_IgnoreVanilla(world, dx, dy, dz))
                        changeAdjBlock(world, x, y, z, side, Blocks.ice, 0);
                }
            }
        }
        if (corners) {
            for (int i = -4; i < 4; i++) {
                for (int k = -4; k < 4; k++) {
                    if (Math.abs(i) + Math.abs(k) <= 4) {
                        int dx = x + i;
                        int dz = z + k;
                        Material mat = getMaterial(world, dx, y, dz);
                        TemperatureEffect eff = temperatureBlockEffects.get(mat);
                        if (eff != null && temperature >= eff.minimumTemperature) {
                            eff.apply(world, dx, y, dz, temperature, callback);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
                for (int d = 1; d < 4; d++) {
                    int dx = x + dir.offsetX * d;
                    int dy = y + dir.offsetY * d;
                    int dz = z + dir.offsetZ * d;
                    Material mat = getMaterial(world, dx, dy, dz);
                    TemperatureEffect eff = temperatureBlockEffects.get(mat);
                    if (eff != null && temperature >= eff.minimumTemperature) {
                        eff.apply(world, dx, dy, dz, temperature, callback);
                    }
                }
            }
        }
    }

    public static boolean isMeltable(World world, int x, int y, int z, int temperature) {
        Block b = world.getBlock(x, y, z);
        if (b == Blocks.air || b == Blocks.bedrock)
            return false;
        if (b == Blocks.obsidian) {
            return temperature >= 1800;
        }
        Material m = b.getMaterial();
        if (m == Material.rock) {
            return temperature >= 1500;
        }
        if (m == Material.iron) {
            return temperature >= 2000;
        }
        return false;
    }

    public static void ignite(World world, int x, int y, int z) {
        ignite(world, x, y, z, 0);
    }

    /** Surrounds the block with fire. Args: World, x, y, z */
    public static void ignite(World world, int x, int y, int z, int meta) {
        if (world.getBlock(x, y, z) == Blocks.air)
            return;
        if (world.getBlock(x - 1, y, z) == Blocks.air)
            world.setBlock(x - 1, y, z, Blocks.fire, meta, 3);
        if (world.getBlock(x + 1, y, z) == Blocks.air)
            world.setBlock(x + 1, y, z, Blocks.fire, meta, 3);
        if (world.getBlock(x, y - 1, z) == Blocks.air)
            world.setBlock(x, y - 1, z, Blocks.fire, meta, 3);
        if (world.getBlock(x, y + 1, z) == Blocks.air)
            world.setBlock(x, y + 1, z, Blocks.fire, meta, 3);
        if (world.getBlock(x, y, z - 1) == Blocks.air)
            world.setBlock(x, y, z - 1, Blocks.fire, meta, 3);
        if (world.getBlock(x, y, z + 1) == Blocks.air)
            world.setBlock(x, y, z + 1, Blocks.fire, meta, 3);
    }

    /**
     *Returns the number of flui blocks directly and continuously above the passed
     *coordinates. Returns -1 if invalid liquid specified. Args: World, x, y, z
    */
    public static int getDepthFromBelow(World world, int x, int y, int z, Fluid f) {
        int i = 0;
        while (ReikaFluidHelper.lookupFluidForBlock(world.getBlock(x, y + 1 + i, z)) == f
        ) {
            i++;
        }
        return i;
    }

    /**
     *Returns true if the block ID is one associated with caves, like air, cobwebs,
     *spawners, mushrooms, etc. Args: Block ID
    */
    public static boolean caveBlock(Block id) {
        if (id == Blocks.air || id == Blocks.flowing_water || id == Blocks.water
            || id == Blocks.flowing_lava || id == Blocks.lava || id == Blocks.web
            || id == Blocks.mob_spawner || id == Blocks.red_mushroom
            || id == Blocks.brown_mushroom)
            return true;
        return false;
    }

    /**
     *Performs machine overheat effects (primarily intended for RotaryCraft).
     *Args: World, x, y, z, item drop id, item drop metadata, min drops, max drops,
     *spark particles yes/no, number-of-sparks multiplier (default 20-40),
     *flaming explosion yes/no, smoking explosion yes/no, explosion force (0 for none)
    */
    public static void overheat(
        World world,
        int x,
        int y,
        int z,
        ItemStack drop,
        int mindrops,
        int maxdrops,
        boolean sparks,
        float sparkmultiplier,
        boolean flaming,
        boolean smoke,
        float force
    ) {
        world.setBlock(x, y, z, Blocks.air);
        if (force > 0 && !world.isRemote) {
            if (flaming)
                world.newExplosion(null, x, y, z, force, true, smoke);
            else
                world.createExplosion(null, x, y, z, force, smoke);
        }
        int numsparks = rand.nextInt(20) + 20;
        numsparks *= sparkmultiplier;
        if (sparks)
            for (int i = 0; i < numsparks; i++)
                world.spawnParticle(
                    "lava", x + rand.nextFloat(), y + 1, z + rand.nextFloat(), 0, 0, 0
                );
        if (drop != null) {
            ItemStack scrap = drop.copy();
            int numdrops = rand.nextInt(1 + maxdrops - mindrops) + mindrops;
            if (!world.isRemote) {
                for (int i = 0; i < numdrops; i++) {
                    EntityItem ent = new EntityItem(
                        world, x + rand.nextFloat(), y + 0.5, z + rand.nextFloat(), scrap
                    );
                    ent.motionX = -0.2 + 0.4 * rand.nextFloat();
                    ent.motionY = 0.5 * rand.nextFloat();
                    ent.motionZ = -0.2 + 0.4 * rand.nextFloat();
                    world.spawnEntityInWorld(ent);
                    ent.velocityChanged = true;
                }
            }
        }
    }

    /**
     *Takes a specified amount of XP and splits it randomly among a bunch of orbs.
     *Args: World, x, y, z, amount
    */
    public static void
    splitAndSpawnXP(World world, double x, double y, double z, int xp) {
        splitAndSpawnXP(world, x, y, z, xp, 6000);
    }

    /**
     *Takes a specified amount of XP and splits it randomly among a bunch of orbs.
     *Args: World, x, y, z, amount, life
    */
    public static void
    splitAndSpawnXP(World world, double x, double y, double z, int xp, int life) {
        int max = xp / 5 + 1;

        while (xp > 0) {
            int value = rand.nextInt(max) + 1;
            while (value > xp)
                value = rand.nextInt(max) + 1;
            xp -= value;
            EntityXPOrb orb = new EntityXPOrb(world, x, y, z, value);
            orb.motionX = -0.2 + 0.4 * rand.nextFloat();
            orb.motionY = 0.3 * rand.nextFloat();
            orb.motionZ = -0.2 + 0.4 * rand.nextFloat();
            orb.xpOrbAge = 6000 - life;
            if (!world.isRemote) {
                orb.velocityChanged = true;
                world.spawnEntityInWorld(orb);
            }
        }
    }

    /**
     *Returns true if the coordinate specified is a lava source block and would be
     *recreated according to the lava-duplication rules that existed for a short time in
     *Beta 1.9. Args: World, x, y, z
    */
    public static boolean is1p9InfiniteLava(World world, int x, int y, int z) {
        if (getMaterial(world, x, y, z) != Material.lava
            || world.getBlockMetadata(x, y, z) != 0)
            return false;
        if (getMaterial(world, x + 1, y, z) != Material.lava
            || world.getBlockMetadata(x + 1, y, z) != 0)
            return false;
        if (getMaterial(world, x, y, z + 1) != Material.lava
            || world.getBlockMetadata(x, y, z + 1) != 0)
            return false;
        if (getMaterial(world, x - 1, y, z) != Material.lava
            || world.getBlockMetadata(x - 1, y, z) != 0)
            return false;
        if (getMaterial(world, x, y, z - 1) != Material.lava
            || world.getBlockMetadata(x, y, z - 1) != 0)
            return false;
        return true;
    }

    /**
     *Returns the y-coordinate of the top non-air block at the given xz coordinates, at
     *or below the specified y-coordinate. Returns -1 if none. Args: World, x, y, z
    */
    public static int findTopBlockBelowY(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        while (b == Blocks.air && y >= 0) {
            y--;
            b = world.getBlock(x, y, z);
        }
        return y;
    }

    /** Returns true if the coordinate is a liquid source Blocks. Args: World, x, y, z */
    public static boolean isLiquidSourceBlock(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (b == Blocks.air)
            return false;
        int meta = world.getBlockMetadata(x, y, z);
        if (b instanceof BlockFluidFinite)
            return meta == 7;
        if (meta != 0)
            return false;
        return b instanceof BlockLiquid || b instanceof BlockFluidBase;
    }

    /**
     *Breaks a contiguous area of blocks recursively (akin to a fill tool in image
     *editors). Args: World, start x, start y, start z, id, metadata (-1 for any)
    */
    public static void
    recursiveBreak(World world, int x, int y, int z, Block id, int meta, int fortune) {
        if (id == Blocks.air)
            return;
        if (world.getBlock(x, y, z) != id)
            return;
        if (meta != world.getBlockMetadata(x, y, z) && meta != -1)
            return;
        int metad = world.getBlockMetadata(x, y, z);
        ReikaItemHelper.dropItems(
            world, x, y, z, id.getDrops(world, x, y, z, metad, fortune)
        );
        ReikaSoundHelper.playBreakSound(world, x, y, z, id);
        world.setBlockToAir(x, y, z);
        world.markBlockForUpdate(x, y, z);
        recursiveBreak(world, x + 1, y, z, id, meta, fortune);
        recursiveBreak(world, x - 1, y, z, id, meta, fortune);
        recursiveBreak(world, x, y + 1, z, id, meta, fortune);
        recursiveBreak(world, x, y - 1, z, id, meta, fortune);
        recursiveBreak(world, x, y, z + 1, id, meta, fortune);
        recursiveBreak(world, x, y, z - 1, id, meta, fortune);
    }

    public static void recursiveBreakWithinSphere(
        World world,
        int x,
        int y,
        int z,
        Block id,
        int meta,
        int x0,
        int y0,
        int z0,
        double r
    ) {
        recursiveBreakWithinSphere(world, x, y, z, id, meta, x0, y0, z0, r, 0);
    }

    /**
     *Like the ordinary recursive break but with a spherical bounded volume. Args:
     *World, x, y, z, id to replace, metadata to replace (-1 for any), origin x,y,z, max
     *radius
    */
    public static void recursiveBreakWithinSphere(
        World world,
        int x,
        int y,
        int z,
        Block id,
        int meta,
        int x0,
        int y0,
        int z0,
        double r,
        int fortune
    ) {
        if (id == Blocks.air)
            return;
        if (world.getBlock(x, y, z) != id)
            return;
        if (meta != world.getBlockMetadata(x, y, z) && meta != -1)
            return;
        if (ReikaMathLibrary.py3d(x - x0, y - y0, z - z0) > r)
            return;
        int metad = world.getBlockMetadata(x, y, z);
        ReikaItemHelper.dropItems(
            world, x, y, z, id.getDrops(world, x, y, z, metad, fortune)
        );
        ReikaSoundHelper.playBreakSound(world, x, y, z, id);
        world.setBlockToAir(x, y, z);
        world.markBlockForUpdate(x, y, z);
        recursiveBreakWithinSphere(world, x + 1, y, z, id, meta, x0, y0, z0, r, fortune);
        recursiveBreakWithinSphere(world, x - 1, y, z, id, meta, x0, y0, z0, r, fortune);
        recursiveBreakWithinSphere(world, x, y + 1, z, id, meta, x0, y0, z0, r, fortune);
        recursiveBreakWithinSphere(world, x, y - 1, z, id, meta, x0, y0, z0, r, fortune);
        recursiveBreakWithinSphere(world, x, y, z + 1, id, meta, x0, y0, z0, r, fortune);
        recursiveBreakWithinSphere(world, x, y, z - 1, id, meta, x0, y0, z0, r, fortune);
    }

    /**
     *Like the ordinary recursive break but with a bounded volume. Args: World, x, y, z,
     *id to replace, metadata to replace (-1 for any), min x,y,z, max x,y,z
    */
    public static void recursiveBreakWithBounds(
        World world,
        int x,
        int y,
        int z,
        Block id,
        int meta,
        int x1,
        int y1,
        int z1,
        int x2,
        int y2,
        int z2,
        int fortune
    ) {
        if (id == Blocks.air)
            return;
        if (x < x1 || y < y1 || z < z1 || x > x2 || y > y2 || z > z2)
            return;
        if (world.getBlock(x, y, z) != id)
            return;
        if (meta != world.getBlockMetadata(x, y, z) && meta != -1)
            return;
        int metad = world.getBlockMetadata(x, y, z);
        ReikaItemHelper.dropItems(
            world, x, y, z, id.getDrops(world, x, y, z, metad, fortune)
        );
        ReikaSoundHelper.playBreakSound(world, x, y, z, id);
        world.setBlockToAir(x, y, z);
        world.markBlockForUpdate(x, y, z);
        recursiveBreakWithBounds(
            world, x + 1, y, z, id, meta, x1, y1, z1, x2, y2, z2, fortune
        );
        recursiveBreakWithBounds(
            world, x - 1, y, z, id, meta, x1, y1, z1, x2, y2, z2, fortune
        );
        recursiveBreakWithBounds(
            world, x, y + 1, z, id, meta, x1, y1, z1, x2, y2, z2, fortune
        );
        recursiveBreakWithBounds(
            world, x, y - 1, z, id, meta, x1, y1, z1, x2, y2, z2, fortune
        );
        recursiveBreakWithBounds(
            world, x, y, z + 1, id, meta, x1, y1, z1, x2, y2, z2, fortune
        );
        recursiveBreakWithBounds(
            world, x, y, z - 1, id, meta, x1, y1, z1, x2, y2, z2, fortune
        );
    }

    /**
     *Recursively fills a contiguous area of one block type with another, akin to a fill
     *tool. Args: World, start x, start y, start z, id to replace, id to fill with,
     *metadata to replace (-1 for any), metadata to fill with
    */
    public static void recursiveFill(
        World world, int x, int y, int z, Block id, Block idto, int meta, int metato
    ) {
        if (world.getBlock(x, y, z) != id)
            return;
        if (meta != world.getBlockMetadata(x, y, z) && meta != -1)
            return;
        int metad = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, idto, metato, 3);
        world.markBlockForUpdate(x, y, z);
        recursiveFill(world, x + 1, y, z, id, idto, meta, metato);
        recursiveFill(world, x - 1, y, z, id, idto, meta, metato);
        recursiveFill(world, x, y + 1, z, id, idto, meta, metato);
        recursiveFill(world, x, y - 1, z, id, idto, meta, metato);
        recursiveFill(world, x, y, z + 1, id, idto, meta, metato);
        recursiveFill(world, x, y, z - 1, id, idto, meta, metato);
    }

    /**
     *Like the ordinary recursive fill but with a bounded volume. Args: World, x, y, z,
     *id to replace, id to fill with, metadata to replace (-1 for any),
     *metadata to fill with, min x,y,z, max x,y,z
    */
    public static void recursiveFillWithBounds(
        World world,
        int x,
        int y,
        int z,
        Block id,
        Block idto,
        int meta,
        int metato,
        int x1,
        int y1,
        int z1,
        int x2,
        int y2,
        int z2
    ) {
        if (x < x1 || y < y1 || z < z1 || x > x2 || y > y2 || z > z2)
            return;
        if (world.getBlock(x, y, z) != id)
            return;
        if (meta != world.getBlockMetadata(x, y, z) && meta != -1)
            return;
        int metad = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, idto, metato, 3);
        world.markBlockForUpdate(x, y, z);
        recursiveFillWithBounds(
            world, x + 1, y, z, id, idto, meta, metato, x1, y1, z1, x2, y2, z2
        );
        recursiveFillWithBounds(
            world, x - 1, y, z, id, idto, meta, metato, x1, y1, z1, x2, y2, z2
        );
        recursiveFillWithBounds(
            world, x, y + 1, z, id, idto, meta, metato, x1, y1, z1, x2, y2, z2
        );
        recursiveFillWithBounds(
            world, x, y - 1, z, id, idto, meta, metato, x1, y1, z1, x2, y2, z2
        );
        recursiveFillWithBounds(
            world, x, y, z + 1, id, idto, meta, metato, x1, y1, z1, x2, y2, z2
        );
        recursiveFillWithBounds(
            world, x, y, z - 1, id, idto, meta, metato, x1, y1, z1, x2, y2, z2
        );
    }

    /**
     *Like the ordinary recursive fill but with a spherical bounded volume. Args: World,
     *x, y, z, id to replace, id to fill with, metadata to replace (-1 for any), metadata
     *to fill with, origin x,y,z, max radius
    */
    public static void recursiveFillWithinSphere(
        World world,
        int x,
        int y,
        int z,
        Block id,
        Block idto,
        int meta,
        int metato,
        int x0,
        int y0,
        int z0,
        double r
    ) {
        /*DragonAPICore.log(world.getBlock(x, y, z)+" & "+id+" @ "+x0+", "+y0+", "+z0);
        DragonAPICore.log(world.getBlockMetadata(x, y, z)+" & "+meta+" @ "+x0+", "+y0+",
        "+z0); DragonAPICore.log(ReikaMathLibrary.py3d(x-x0, y-y0, z-z0)+" & "+r+" @
        "+x0+", "+y0+", "+z0);*/
        if (world.getBlock(x, y, z) != id)
            return;
        if (meta != world.getBlockMetadata(x, y, z) && meta != -1)
            return;
        if (ReikaMathLibrary.py3d(x - x0, y - y0, z - z0) > r)
            return;
        int metad = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, idto, metato, 3);
        world.markBlockForUpdate(x, y, z);
        recursiveFillWithinSphere(
            world, x + 1, y, z, id, idto, meta, metato, x0, y0, z0, r
        );
        recursiveFillWithinSphere(
            world, x - 1, y, z, id, idto, meta, metato, x0, y0, z0, r
        );
        recursiveFillWithinSphere(
            world, x, y + 1, z, id, idto, meta, metato, x0, y0, z0, r
        );
        recursiveFillWithinSphere(
            world, x, y - 1, z, id, idto, meta, metato, x0, y0, z0, r
        );
        recursiveFillWithinSphere(
            world, x, y, z + 1, id, idto, meta, metato, x0, y0, z0, r
        );
        recursiveFillWithinSphere(
            world, x, y, z - 1, id, idto, meta, metato, x0, y0, z0, r
        );
    }

    /**
     *Returns true if there is a clear line of sight between two points. Args: World,
     *Start x,y,z, End x,y,z NOTE: If one point is a block, use canBlockSee instead, as
     *this method will always return false.
    */
    public static boolean lineOfSight(
        World world, double x1, double y1, double z1, double x2, double y2, double z2
    ) {
        if (world.isRemote)
            ; //return false;
        Vec3 v1 = Vec3.createVectorHelper(x1, y1, z1);
        Vec3 v2 = Vec3.createVectorHelper(x2, y2, z2);
        return (world.rayTraceBlocks(v1, v2) == null);
    }

    /**
     * Returns true if there is a clear line of sight between two entites. Args: World,
     * Entity 1, Entity 2
     */
    public static boolean lineOfSight(World world, Entity e1, Entity e2) {
        Vec3 v1 = Vec3.createVectorHelper(e1.posX, e1.posY + e1.getEyeHeight(), e1.posZ);
        Vec3 v2 = Vec3.createVectorHelper(e2.posX, e2.posY + e2.getEyeHeight(), e2.posZ);
        return (world.rayTraceBlocks(v1, v2) == null);
    }

    /**
     * Returns true if a block can see an point. Args: World, block x,y,z, Point x,y,z,
     * Max Range
     */
    public static boolean canBlockSee(
        World world, int x, int y, int z, double x0, double y0, double z0, double range
    ) {
        Block locid = world.getBlock(x, y, z);
        range += 2;
        for (int k = 0; k < 10; k++) {
            float a = 0;
            float b = 0;
            float c = 0;
            switch (k) {
                case 1:
                    a = 1;
                    break;
                case 2:
                    b = 1;
                    break;
                case 3:
                    a = 1;
                    b = 1;
                    break;
                case 4:
                    c = 1;
                    break;
                case 5:
                    a = 1;
                    c = 1;
                    break;
                case 6:
                    b = 1;
                    c = 1;
                    break;
                case 7:
                    a = 1;
                    b = 1;
                    c = 1;
                    break;
                case 8:
                    a = 0.5F;
                    b = 0.5F;
                    c = 0.5F;
                    break;
                case 9:
                    b = 0.5F;
                    break;
            }
            for (float i = 0; i <= range; i += 0.25) {
                Vec3 vec2 = ReikaVectorHelper.getVec2Pt(x0, y0, z0, x + a, y + b, z + c)
                                .normalize();
                vec2 = ReikaVectorHelper.scaleVector(vec2, i);
                vec2.xCoord += x0;
                vec2.yCoord += y0;
                vec2.zCoord += z0;
                //ReikaColorAPI.write(String.format("%f -->  %.3f,  %.3f, %.3f", i,
                //vec2.xCoord, vec2.yCoord, vec2.zCoord));
                int dx = MathHelper.floor_double(vec2.xCoord);
                int dy = MathHelper.floor_double(vec2.yCoord);
                int dz = MathHelper.floor_double(vec2.zCoord);
                Block id = world.getBlock(dx, dy, dz);
                if (dx == x && dy == y && dz == z) {
                    //ReikaColorAPI.writeCoords(world, (int)vec2.xCoord, (int)vec2.yCoord,
                    //(int)vec2.zCoord);
                    return true;
                } else if (id != locid && ReikaBlockHelper.isCollideable(world, dx, dy, dz) && !softBlocks(world, dx, dy, dz)) {
                    i = (float) (range + 1); //Hard loop break
                }
            }
        }
        return false;
    }

    /** a, b, c, are the "internal offset" of the vector origins */
    public static boolean rayTraceTwoBlocks(
        World world,
        int x1,
        int y1,
        int z1,
        int x2,
        int y2,
        int z2,
        float a,
        float b,
        float c
    ) {
        Vec3 vec1 = Vec3.createVectorHelper(x1 + a, y1 + b, z1 + c);
        Vec3 vec2 = Vec3.createVectorHelper(x2 + a, y2 + b, z2 + c);
        Vec3 ray = ReikaVectorHelper.subtract(vec1, vec2);
        double dx = vec2.xCoord - vec1.xCoord;
        double dy = vec2.yCoord - vec1.yCoord;
        double dz = vec2.zCoord - vec1.zCoord;
        double dd = ReikaMathLibrary.py3d(dx, dy, dz);
        for (double d = 0.25; d <= dd; d += 0.5) {
            Vec3 vec0 = ReikaVectorHelper.scaleVector(ray, d);
            Vec3 vec = ReikaVectorHelper.scaleVector(ray, d - 0.25);
            vec0.xCoord += vec1.xCoord;
            vec0.yCoord += vec1.yCoord;
            vec0.zCoord += vec1.zCoord;
            vec.xCoord += vec1.xCoord;
            vec.yCoord += vec1.yCoord;
            vec.zCoord += vec1.zCoord;
            MovingObjectPosition mov = world.rayTraceBlocks(vec, vec0);
            if (mov != null) {
                if (mov.typeOfHit == MovingObjectType.BLOCK) {
                    int bx = mov.blockX;
                    int by = mov.blockY;
                    int bz = mov.blockZ;
                    if (bx == x1 && by == y1 && bz == z1) {
                    } else if (bx == x2 && by == y2 && bz == z2) {
                    } else {
                        if (!softBlocks(world, bx, by, bz)
                            && ReikaBlockHelper.isCollideable(world, bx, by, bz))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean
    rayTraceTwoBlocks(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        return rayTraceTwoBlocks(world, x1, y1, z1, x2, y2, z2, 0.5F, 0.5F, 0.5F);
    }

    /**
     *Returns true if the entity can see a block, or if it could be moved to a position
     *where it could see the block. Args: World, Block x,y,z, Entity, Max Move Distance DO
     *NOT USE THIS-CPU INTENSIVE TO ALL HELL!
    */
    public static boolean
    canSeeOrMoveToSeeBlock(World world, int x, int y, int z, Entity ent, double r) {
        double d = 4; //+ReikaMathLibrary.py3d(x-ent.posX, y-ent.posY, z-ent.posZ);
        if (canBlockSee(world, x, y, z, ent.posX, ent.posY, ent.posZ, d))
            return true;
        double xmin;
        double ymin;
        double zmin;
        double xmax;
        double ymax;
        double zmax;
        double[] pos = new double[3];
        boolean[] signs = new boolean[3];
        boolean[] signs2 = new boolean[3];
        signs[0] = (ReikaMathLibrary.isSameSign(ent.posX, x));
        signs[1] = (ReikaMathLibrary.isSameSign(ent.posY, y));
        signs[2] = (ReikaMathLibrary.isSameSign(ent.posZ, z));
        for (double i = ent.posX - r; i <= ent.posX + r; i += 0.5) {
            for (double j = ent.posY - r; j <= ent.posY + r; j += 0.5) {
                for (double k = ent.posZ - r; k <= ent.posZ + r; k += 0.5) {
                    if (canBlockSee(
                            world, x, y, z, ent.posX + i, ent.posY + j, ent.posZ + k, d
                        ))
                        return true;
                }
            }
        }
        /*
        for (double i = ent.posX; i > ent.posX-r; i -= 0.5) {
            Block b = world.getBlock((int)i, (int)ent.posY, (int)ent.posZ);
            if (isCollideable(world, (int)i, (int)ent.posY, (int)ent.posZ)) {
                xmin = i+Blocks.blocksList[id].getBlockBoundsMaxX();
            }
        }
        for (double i = ent.posX; i < ent.posX+r; i += 0.5) {
            Block b = world.getBlock((int)i, (int)ent.posY, (int)ent.posZ);
            if (isCollideable(world, (int)i, (int)ent.posY, (int)ent.posZ)) {
                xmax = i+Blocks.blocksList[id].getBlockBoundsMinX();
            }
        }
        for (double i = ent.posY; i > ent.posY-r; i -= 0.5) {
            Block b = world.getBlock((int)ent.posX, (int)i, (int)ent.posZ);
            if (isCollideable(world, (int)ent.posX, (int)i, (int)ent.posZ)) {
                ymin = i+Blocks.blocksList[id].getBlockBoundsMaxX();
            }
        }
        for (double i = ent.posY; i < ent.posY+r; i += 0.5) {
            Block b = world.getBlock((int)ent.posX, (int)i, (int)ent.posZ);
            if (isCollideable(world, (int)ent.posX, (int)i, (int)ent.posZ)) {
                ymax = i+Blocks.blocksList[id].getBlockBoundsMinX();
            }
        }
        for (double i = ent.posZ; i > ent.posZ-r; i -= 0.5) {
            Block b = world.getBlock((int)ent.posX, (int)ent.posY, (int)i);
            if (isCollideable(world, (int)ent.posX, (int)ent.posY, (int)i)) {
                zmin = i+Blocks.blocksList[id].getBlockBoundsMaxX();
            }
        }
        for (double i = ent.posZ; i < ent.posZ+r; i += 0.5) {
            Block b = world.getBlock((int)ent.posX, (int)ent.posY, (int)i);
            if (isCollideable(world, (int)ent.posX, (int)ent.posY, (int)i)) {
                zmax = i+Blocks.blocksList[id].getBlockBoundsMinX();
            }
        }*/
        signs2[0] = (ReikaMathLibrary.isSameSign(pos[0], x));
        signs2[1] = (ReikaMathLibrary.isSameSign(pos[1], y));
        signs2[2] = (ReikaMathLibrary.isSameSign(pos[2], z));
        if (signs[0] != signs2[0] || signs[1] != signs2[1]
            || signs[2]
                != signs2[2]) //Cannot pull the item "Across" (so that it moves away)
            return false;
        return false;
    }

    /*
    public static boolean lenientSeeThrough(World world, double x, double y, double z,
    double x0, double y0, double z0) { MovingObjectPosition pos; Vec3 par1Vec3 =
    Vec3.createVectorHelper(x, y, z); Vec3 par2Vec3 = Vec3.createVectorHelper(x0, y0, z0);
        if (!Double.isNaN(par1Vec3.xCoord) && !Double.isNaN(par1Vec3.yCoord) &&
    !Double.isNaN(par1Vec3.zCoord)) { if (!Double.isNaN(par2Vec3.xCoord) &&
    !Double.isNaN(par2Vec3.yCoord) && !Double.isNaN(par2Vec3.zCoord)) { int var5 =
    MathHelper.floor_double(par2Vec3.xCoord); int var6 =
    MathHelper.floor_double(par2Vec3.yCoord); int var7 =
    MathHelper.floor_double(par2Vec3.zCoord); int var8 =
    MathHelper.floor_double(par1Vec3.xCoord); int var9 =
    MathHelper.floor_double(par1Vec3.yCoord); int var10 =
    MathHelper.floor_double(par1Vec3.zCoord); Block var11 = world.getBlock(var8, var9,
    var10); int var12 = world.getBlockMetadata(var8, var9, var10); Block var13 = var11;
                //ReikaColorAPI.write(var11);
                if (var13 != null && (var11 != Blocks.air && !softBlocks(var11) && (var11
    != Blocks.leaves) && (var11 != Blocks.web)) && var13.canCollideCheck(var12, false)) {
                    MovingObjectPosition var14 = var13.collisionRayTrace(world, var8,
    var9, var10, par1Vec3, par2Vec3); if (var14 != null) pos = var14;
                }
                var11 = 200;
                while (var11-- >= 0) {
                    if (Double.isNaN(par1Vec3.xCoord) || Double.isNaN(par1Vec3.yCoord) ||
    Double.isNaN(par1Vec3.zCoord)) pos = null; if (var8 == var5 && var9 == var6 && var10
    == var7) pos = null; boolean var39 = true; boolean var40 = true; boolean var41 = true;
                    double var15 = 999.0D;
                    double var17 = 999.0D;
                    double var19 = 999.0D;
                    if (var5 > var8)
                        var15 = var8+1.0D;
                    else if (var5 < var8)
                        var15 = var8+0.0D;
                    else
                        var39 = false;
                    if (var6 > var9)
                        var17 = var9+1.0D;
                    else if (var6 < var9)
                        var17 = var9+0.0D;
                    else
                        var40 = false;
                    if (var7 > var10)
                        var19 = var10+1.0D;
                    else if (var7 < var10)
                        var19 = var10+0.0D;
                    else
                        var41 = false;
                    double var21 = 999.0D;
                    double var23 = 999.0D;
                    double var25 = 999.0D;
                    double var27 = par2Vec3.xCoord-par1Vec3.xCoord;
                    double var29 = par2Vec3.yCoord-par1Vec3.yCoord;
                    double var31 = par2Vec3.zCoord-par1Vec3.zCoord;
                    if (var39)
                        var21 = (var15-par1Vec3.xCoord) / var27;
                    if (var40)
                        var23 = (var17-par1Vec3.yCoord) / var29;
                    if (var41)
                        var25 = (var19-par1Vec3.zCoord) / var31;
                    boolean var33 = false;
                    byte var42;
                    if (var21 < var23 && var21 < var25) {
                        if (var5 > var8)
                            var42 = 4;
                        else
                            var42 = 5;
                        par1Vec3.xCoord = var15;
                        par1Vec3.yCoord += var29*var21;
                        par1Vec3.zCoord += var31*var21;
                    }
                    else if (var23 < var25) {
                        if (var6 > var9)
                            var42 = 0;
                        else
                            var42 = 1;
                        par1Vec3.xCoord += var27*var23;
                        par1Vec3.yCoord = var17;
                        par1Vec3.zCoord += var31*var23;
                    }
                    else {
                        if (var7 > var10)
                            var42 = 2;
                        else
                            var42 = 3;

                        par1Vec3.xCoord += var27*var25;
                        par1Vec3.yCoord += var29*var25;
                        par1Vec3.zCoord = var19;
                    }
                    Vec3 var34 = Vec3.createVectorHelper(par1Vec3.xCoord, par1Vec3.yCoord,
    par1Vec3.zCoord); var8 = (int)(var34.xCoord =
    MathHelper.floor_double(par1Vec3.xCoord)); if (var42 == 5) {
                        --var8;
                        ++var34.xCoord;
                    }
                    var9 = (int)(var34.yCoord = MathHelper.floor_double(par1Vec3.yCoord));
                    if (var42 == 1) {
                        --var9;
                        ++var34.yCoord;
                    }
                    var10 = (int)(var34.zCoord =
    MathHelper.floor_double(par1Vec3.zCoord)); if (var42 == 3) {
                        --var10;
                        ++var34.zCoord;
                    }
                    Block var35 = world.getBlock(var8, var9, var10);
                    int var36 = world.getBlockMetadata(var8, var9, var10);
                    Block var37 = var35;
                    if (var35 != Blocks.air && var37.canCollideCheck(var36, false)) {
                        MovingObjectPosition var38 = var37.collisionRayTrace(world, var8,
    var9, var10, par1Vec3, par2Vec3); if (var38 != null) pos = var38;
                    }
                }
                pos = null;
            }
            else
                pos = null;
        }
        else
            pos = null;
        return (pos == null);
    }*/

    /**
     *Returns true if the specified corner has at least one air block adjacent to it,
     *but is not surrounded by air on all sides or in the void. Args: World, x, y, z
    */
    public static boolean cornerHasAirAdjacent(World world, int x, int y, int z) {
        if (y <= 0)
            return false;
        int airs = 0;
        if (world.getBlock(x, y, z) == Blocks.air)
            airs++;
        if (world.getBlock(x - 1, y, z) == Blocks.air)
            airs++;
        if (world.getBlock(x, y, z - 1) == Blocks.air)
            airs++;
        if (world.getBlock(x - 1, y, z - 1) == Blocks.air)
            airs++;
        if (world.getBlock(x, y - 1, z) == Blocks.air)
            airs++;
        if (world.getBlock(x - 1, y - 1, z) == Blocks.air)
            airs++;
        if (world.getBlock(x, y - 1, z - 1) == Blocks.air)
            airs++;
        if (world.getBlock(x - 1, y - 1, z - 1) == Blocks.air)
            airs++;
        return (airs > 0 && airs != 8);
    }

    /**
     *Returns true if the specified corner has at least one nonopaque block adjacent to
     *it, but is not surrounded by air on all sides or in the void. Args: World, x, y, z
    */
    public static boolean cornerHasTransAdjacent(World world, int x, int y, int z) {
        if (y <= 0)
            return false;
        Block id;
        int airs = 0;
        boolean nonopq = false;
        id = world.getBlock(x, y, z);
        if (id == Blocks.air)
            airs++;
        else if (!id.isOpaqueCube())
            nonopq = true;
        id = world.getBlock(x - 1, y, z);
        if (id == Blocks.air)
            airs++;
        else if (!id.isOpaqueCube())
            nonopq = true;
        id = world.getBlock(x, y, z - 1);
        if (id == Blocks.air)
            airs++;
        else if (!id.isOpaqueCube())
            nonopq = true;
        id = world.getBlock(x - 1, y, z - 1);
        if (id == Blocks.air)
            airs++;
        else if (!id.isOpaqueCube())
            nonopq = true;
        id = world.getBlock(x, y - 1, z);
        if (id == Blocks.air)
            airs++;
        else if (!id.isOpaqueCube())
            nonopq = true;
        id = world.getBlock(x - 1, y - 1, z);
        if (id == Blocks.air)
            airs++;
        else if (!id.isOpaqueCube())
            nonopq = true;
        id = world.getBlock(x, y - 1, z - 1);
        if (id == Blocks.air)
            airs++;
        else if (!id.isOpaqueCube())
            nonopq = true;
        id = world.getBlock(x - 1, y - 1, z - 1);
        if (id == Blocks.air)
            airs++;
        else if (!id.isOpaqueCube())
            nonopq = true;
        return (airs != 8 && nonopq);
    }

    /**
     * Spawns a line of particles between two points. Args: World, start x,y,z, end
     * x,y,z, particle type, particle speed x,y,z, number of particles
     */
    public static void spawnParticleLine(
        World world,
        double x1,
        double y1,
        double z1,
        double x2,
        double y2,
        double z2,
        String name,
        double vx,
        double vy,
        double vz,
        int spacing
    ) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        double sx = dx / spacing;
        double sy = dy / spacing;
        double sz = dy / spacing;
        double[][] parts = new double[spacing + 1][3];
        for (int i = 0; i <= spacing; i++) {
            parts[i][0] = i * sx + x1;
            parts[i][1] = i * sy + y1;
            parts[i][2] = i * sz + z1;
        }
        for (int i = 0; i < parts.length; i++) {
            world.spawnParticle(name, parts[i][0], parts[i][1], parts[i][2], vx, vy, vz);
        }
    }

    /**
     *Checks if a liquid block is part of a column (has same liquid above and below and
     *none of them are source blocks). Args: World, x, y, z
    */
    public static boolean isLiquidAColumn(World world, int x, int y, int z) {
        Fluid f = getFluid(world, x, y, z);
        if (f == null)
            return false;
        if (isLiquidSourceBlock(world, x, y, z))
            return false;
        if (getFluid(world, x, y + 1, z) != f)
            return false;
        if (isLiquidSourceBlock(world, x, y + 1, z))
            return false;
        if (getFluid(world, x, y - 1, z) != f)
            return false;
        if (isLiquidSourceBlock(world, x, y - 1, z))
            return false;
        return true;
    }

    public static Fluid getFluid(IBlockAccess world, int x, int y, int z) {
        return ReikaFluidHelper.lookupFluidForBlock(world.getBlock(x, y, z));
    }

    /** Updates all blocks adjacent to the coordinate given. Args: World, x, y, z */
    public static void causeAdjacentUpdates(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        world.notifyBlocksOfNeighborChange(x, y, z, b);
    }

    /**
     * Updates all blocks adjacent to the coordinate given, provided they meet a
     * criterion. Args: World, x, y, z
     */
    public static void causeAdjacentUpdatesIf(
        World world, int x, int y, int z, PositionCallable<Boolean> criteria
    ) {
        Block b = world.getBlock(x, y, z);
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            if (criteria.call(world, dx, dy, dz)) {
                world.notifyBlockOfNeighborChange(dx, dy, dz, b);
            }
        }
    }

    public static ArrayList<ItemStack>
    getDropsAt(World world, int x, int y, int z, int fortune, EntityPlayer ep) {
        Block b = world.getBlock(x, y, z);
        if (b == Blocks.air)
            return new ArrayList();
        int meta = world.getBlockMetadata(x, y, z);
        ThreadLocal harvesters
            = (ThreadLocal) ReikaObfuscationHelper.get("harvesters", b);
        harvesters.set(ep);
        ArrayList<ItemStack> li = b.getDrops(world, x, y, z, meta, fortune);
        if (ep != null) {
            if (b instanceof BlockTieredResource) {
                BlockTieredResource bt = (BlockTieredResource) b;
                li = new ArrayList(
                    bt.isPlayerSufficientTier(world, x, y, z, ep)
                        ? bt.getHarvestResources(world, x, y, z, fortune, ep)
                        : bt.getNoHarvestResources(world, x, y, z, fortune, ep)
                );
            }
            HarvestDropsEvent evt = new HarvestDropsEvent(
                x, y, z, world, b, meta, fortune, 1F, li, ep, false
            );
            MinecraftForge.EVENT_BUS.post(evt);
            li = evt.drops;
        }
        harvesters.set(null);
        return li;
    }

    /** Drops all items from a given Blocks. Args: World, x, y, z, fortune level */
    public static ArrayList<ItemStack>
    dropBlockAt(World world, int x, int y, int z, int fortune, EntityPlayer ep) {
        ArrayList<ItemStack> li = getDropsAt(world, x, y, z, fortune, ep);
        ReikaItemHelper.dropItems(world, x + 0.5, y + 0.5, z + 0.5, li);
        return li;
    }

    /** Drops all items from a given block with no fortune effect. Args: World, x, y, z */
    public static ArrayList<ItemStack>
    dropBlockAt(World world, int x, int y, int z, EntityPlayer ep) {
        return dropBlockAt(world, x, y, z, 0, ep);
    }

    /** Sets the biome type at an xz column. Args: World, x, z, biome */
    public static void setBiomeForXZ(
        World world, int x, int z, BiomeGenBase biome, boolean applyEnvironment
    ) {
        Chunk ch = world.getChunkFromBlockCoords(x, z);

        int ax = x - ch.xPosition * 16;
        int az = z - ch.zPosition * 16;

        byte[] biomes = ch.getBiomeArray();
        int index = az * 16 + ax;
        if (index < 0 || index >= biomes.length) {
            DragonAPICore.logError(
                "BIOME CHANGE ERROR: " + x + "&" + z + " @ " + ch.xPosition + "&"
                    + ch.zPosition + ": " + ax + "%" + az + " -> " + index,
                Side.SERVER
            );
            return;
        }
        biomes[index] = (byte) biome.biomeID;
        ch.setBiomeArray(biomes);
        ch.setChunkModified();
        if (applyEnvironment) {
            for (int i = 0; i < 256; i++)
                temperatureEnvironment(
                    world, x, i, z, ReikaBiomeHelper.getBiomeTemp(world, biome)
                );
        }

        if (!world.isRemote) {
            int packet = APIPacketHandler.PacketIDs.BIOMECHANGE.ordinal();
            ReikaPacketHelper.sendDataPacketWithRadius(
                DragonAPIInit.packetChannel, packet, world, x, 0, z, 1024, biome.biomeID
            );
        }
    }

    public static BiomeGenBase getNaturalGennedBiomeAt(World world, int x, int z) {
        BiomeGenBase[] biomes
            = world.getWorldChunkManager().loadBlockGeneratorData(null, x, z, 1, 1);
        BiomeGenBase natural = biomes != null && biomes.length > 0 ? biomes[0] : null;
        return natural;
    }

    /**
     * Sets the biome type at an xz column and mimics its generation. Args: World, x, z,
     * biome
     */
    public static void setBiomeAndBlocksForXZ(
        World world, int x, int z, BiomeGenBase biome, boolean applyEnvironment
    ) {
        Chunk ch = world.getChunkFromBlockCoords(x, z);

        int ax = x - ch.xPosition * 16;
        int az = z - ch.zPosition * 16;

        byte[] biomes = ch.getBiomeArray();
        int index = az * 16 + ax;
        if (index < 0 || index >= biomes.length) {
            DragonAPICore.logError(
                "BIOME CHANGE ERROR: " + x + "&" + z + " @ " + ch.xPosition + "&"
                    + ch.zPosition + ": " + ax + "%" + az + " -> " + index,
                Side.SERVER
            );
            return;
        }

        BiomeGenBase from = BiomeGenBase.biomeList[biomes[index]];

        biomes[index] = (byte) biome.biomeID;
        ch.setBiomeArray(biomes);
        if (applyEnvironment) {
            for (int i = 0; i < 256; i++)
                temperatureEnvironment(
                    world, x, i, z, ReikaBiomeHelper.getBiomeTemp(world, biome)
                );
        }

        if (!world.isRemote) {
            int packet = APIPacketHandler.PacketIDs.BIOMECHANGE.ordinal();
            ReikaPacketHelper.sendDataPacketWithRadius(
                DragonAPIInit.packetChannel, packet, world, x, 0, z, 1024, biome.biomeID
            );
        }

        Block fillerID = from.fillerBlock;
        Block topID = from.topBlock;

        for (int y = 30; y < world.provider.getHeight(); y++) {
            Block id = world.getBlock(x, y, z);
            if (id == fillerID) {
                world.setBlock(x, y, z, biome.fillerBlock);
            }
            if (id == topID && y == world.getTopSolidOrLiquidBlock(x, z) - 1) {
                world.setBlock(x, y, z, biome.topBlock);
            }

            if (biome.getEnableSnow()) {
                if (world.canBlockFreeze(x, y, z, false))
                    world.setBlock(x, y, z, Blocks.ice);
                else if (world.canBlockSeeTheSky(x, y + 1, z) && world.isAirBlock(x, y + 1, z))
                    world.setBlock(x, y + 1, z, Blocks.snow);
            } else {
                if (id == Blocks.snow)
                    world.setBlockToAir(x, y, z);
                if (id == Blocks.ice)
                    world.setBlock(x, y, z, Blocks.flowing_water);
            }
        }

        if (world.isRemote)
            return;

        BiomeDecorator dec = biome.theBiomeDecorator;

        int trees = dec.treesPerChunk;
        int grass = dec.grassPerChunk;
        int flowers = dec.flowersPerChunk;
        int cactus = dec.cactiPerChunk;
        int bushes = dec.deadBushPerChunk;
        int sugar = dec.reedsPerChunk;
        int mushrooms = dec.mushroomsPerChunk;
        int lily = dec.waterlilyPerChunk;
        int bigmush = dec.bigMushroomsPerChunk;

        double fac = 1 / 3D;
        int top = world.getTopSolidOrLiquidBlock(x, z);

        if (ReikaRandomHelper.doWithChance(fac * trees / 96D)) {
            WorldGenerator gen = biome.func_150567_a(rand);
            if (ReikaPlantHelper.SAPLING.canPlantAt(world, x, top, z)) {
                if (softBlocks(world, x, top, z))
                    world.setBlockToAir(x, top, z);
                gen.generate(world, rand, x, top, z);
            }
        }

        if (ReikaRandomHelper.doWithChance(fac * grass / 64D)) {
            WorldGenerator gen = biome.getRandomWorldGenForGrass(rand);
            if (softBlocks(world, x, top, z))
                world.setBlockToAir(x, top, z);
            gen.generate(world, rand, x, top, z);
        }

        if (ReikaRandomHelper.doWithChance(fac * bigmush / 96D)) {
            if (softBlocks(world, x, top, z))
                world.setBlockToAir(x, top, z);
            biome.theBiomeDecorator.bigMushroomGen.generate(world, rand, x, top, z);
        }

        if (ReikaRandomHelper.doWithChance(fac * cactus / 256D)) {
            int y = world.getTopSolidOrLiquidBlock(x, z);
            if (ReikaPlantHelper.CACTUS.canPlantAt(world, x, y, z)) {
                int h = 1 + rand.nextInt(3);
                for (int i = 0; i < h; i++)
                    world.setBlock(x, y + i, z, Blocks.cactus);
            }
        }

        if (ReikaRandomHelper.doWithChance(fac * sugar / 64D)) {
            int y = world.getTopSolidOrLiquidBlock(x, z);
            if (ReikaPlantHelper.SUGARCANE.canPlantAt(world, x, y, z)) {
                int h = 1 + rand.nextInt(3);
                for (int i = 0; i < h; i++)
                    world.setBlock(x, y + i, z, Blocks.reeds);
            }
        }

        if (ReikaRandomHelper.doWithChance(fac * bushes / 64D)) {
            int y = world.getTopSolidOrLiquidBlock(x, z);
            if (ReikaPlantHelper.BUSH.canPlantAt(world, x, y, z)) {
                world.setBlock(x, y, z, Blocks.deadbush);
            }
        }

        if (ReikaRandomHelper.doWithChance(fac * lily / 64D)) {
            int y = world.getTopSolidOrLiquidBlock(x, z);
            if (ReikaPlantHelper.LILYPAD.canPlantAt(world, x, y, z)) {
                world.setBlock(x, y, z, Blocks.waterlily);
            }
        }

        if (ReikaRandomHelper.doWithChance(fac * flowers / 256D)) {
            int y = world.getTopSolidOrLiquidBlock(x, z);
            if (ReikaPlantHelper.FLOWER.canPlantAt(world, x, y, z)) {
                if (rand.nextInt(3) == 0)
                    world.setBlock(x, y, z, Blocks.red_flower);
                else
                    world.setBlock(x, y, z, Blocks.yellow_flower);
            }
        }

        if (ReikaRandomHelper.doWithChance(fac * 128 * mushrooms / 256D)) {
            int y = world.getTopSolidOrLiquidBlock(x, z);
            if (ReikaPlantHelper.MUSHROOM.canPlantAt(world, x, y, z)) {
                if (rand.nextInt(4) == 0)
                    world.setBlock(x, y, z, Blocks.red_mushroom);
                else
                    world.setBlock(x, y, z, Blocks.brown_mushroom);
            }
        }

        biome.decorate(world, rand, x, z);

        for (int i = 40; i < 80; i++) {
            world.markBlockForUpdate(x, i, z);
            causeAdjacentUpdates(world, x, i, z);
        }
    }

    /**
     * Get the sun brightness as a fraction from 0 to 1. Args: World, whether to apply
     * weather modulation
     */
    public static float getSunIntensity(World world, boolean weather, float ptick) {
        float ang = world.getCelestialAngle(ptick);
        float base = 1.0F - (MathHelper.cos(ang * (float) Math.PI * 2.0F) * 2.0F + 0.2F);

        if (base < 0.0F)
            base = 0.0F;

        if (base > 1.0F)
            base = 1.0F;

        base = 1.0F - base;
        if (weather) {
            base = (float) (base * (1.0D - world.getRainStrength(ptick) * 5.0F / 16.0D));
            base = (float
            ) (base * (1.0D - world.getWeightedThunderStrength(ptick) * 5.0F / 16.0D));
        }
        return base;
    }

    /** Returns the sun's declination, clamped to 0-90. Args: World */
    public static float getSunAngle(World world) {
        int time = (int) (world.getWorldTime() % 12000);
        float suntheta
            = 0.5F * (float) (90 * Math.sin(Math.toRadians(time * 90D / 6000D)));
        return suntheta;
    }

    /**
     * Tests if a block is nearby, yes/no. Args: World, x, y, z, id to test, meta to
     * test, range
     */
    public static boolean
    testBlockProximity(World world, int x, int y, int z, Block id, int meta, int r) {
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    int rx = x + i;
                    int ry = y + j;
                    int rz = z + k;
                    Block rid = world.getBlock(rx, ry, rz);
                    int rmeta = world.getBlockMetadata(rx, ry, rz);
                    if (rid == id && (meta == -1 || rmeta == meta))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Tests if a liquid is nearby, yes/no. Args: World, x, y, z, liquid material, range
     */
    public static boolean
    testLiquidProximity(World world, int x, int y, int z, Material mat, int r) {
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    int rx = x + i;
                    int ry = y + j;
                    int rz = z + k;
                    Material rmat = getMaterial(world, rx, ry, rz);
                    if (rmat == mat)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * A less intensive but less accurate block proximity test. Args: World, x, y, z,
     * range
     */
    public static boolean
    testBlockProximityLoose(World world, int x, int y, int z, Block id, int meta, int r) {
        int total = r * r * r * 8; //(2r)^3
        int frac = total / 16;
        for (int i = 0; i < frac; i++) {
            int rx = ReikaRandomHelper.getRandomPlusMinus(x, r);
            int ry = ReikaRandomHelper.getRandomPlusMinus(y, r);
            int rz = ReikaRandomHelper.getRandomPlusMinus(z, r);
            Block rid = world.getBlock(rx, ry, rz);
            int rmeta = world.getBlockMetadata(rx, ry, rz);
            if (rid == id && (meta == -1 || rmeta == meta))
                return true;
        }
        return false;
    }

    public static EntityLivingBase
    getClosestLivingEntity(World world, double x, double y, double z, AxisAlignedBB box) {
        List<EntityLivingBase> li
            = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
        double d = Double.MAX_VALUE;
        EntityLivingBase index = null;
        for (EntityLivingBase e : li) {
            if (!e.isDead && e.getHealth() > 0) {
                double dd = ReikaMathLibrary.py3d(e.posX - x, e.posY - y, e.posZ - z);
                if (dd < d) {
                    index = e;
                    d = dd;
                }
            }
        }
        return index;
    }

    public static EntityLivingBase getClosestLivingEntityNoPlayers(
        World world,
        double x,
        double y,
        double z,
        AxisAlignedBB box,
        boolean excludeCreativeOnly
    ) {
        List<EntityLivingBase> li
            = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
        double d = Double.MAX_VALUE;
        EntityLivingBase index = null;
        for (EntityLivingBase e : li) {
            if (!(e instanceof EntityPlayer)
                || (excludeCreativeOnly && !((EntityPlayer) e).capabilities.isCreativeMode
                )) {
                if (!e.isDead && e.getHealth() > 0) {
                    double dd = ReikaMathLibrary.py3d(e.posX - x, e.posY - y, e.posZ - z);
                    if (dd < d) {
                        index = e;
                        d = dd;
                    }
                }
            }
        }
        return index;
    }

    public static EntityLivingBase getClosestHostileEntity(
        World world, double x, double y, double z, AxisAlignedBB box
    ) {
        List<EntityLivingBase> li
            = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
        double d = Double.MAX_VALUE;
        EntityLivingBase index = null;
        for (EntityLivingBase e : li) {
            if (ReikaEntityHelper.isHostile(e)) {
                if (!e.isDead && e.getHealth() > 0) {
                    double dd = ReikaMathLibrary.py3d(e.posX - x, e.posY - y, e.posZ - z);
                    if (dd < d) {
                        index = e;
                        d = dd;
                    }
                }
            }
        }
        return index;
    }

    public static EntityLivingBase getClosestLivingEntityOfClass(
        Class<? extends EntityLivingBase> c,
        World world,
        double x,
        double y,
        double z,
        AxisAlignedBB box
    ) {
        List<EntityLivingBase> li = world.getEntitiesWithinAABB(c, box);
        double d = Double.MAX_VALUE;
        EntityLivingBase index = null;
        for (EntityLivingBase e : li) {
            if (!e.isDead && e.getHealth() > 0) {
                double dd = ReikaMathLibrary.py3d(e.posX - x, e.posY - y, e.posZ - z);
                if (dd < d) {
                    index = e;
                    d = dd;
                }
            }
        }
        return index;
    }

    public static Entity getClosestEntityOfClass(
        Class<? extends Entity> c, World world, double x, double y, double z, double range
    ) {
        AxisAlignedBB box
            = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(range, range, range);
        List<Entity> li = world.getEntitiesWithinAABB(c, box);
        double d = Double.MAX_VALUE;
        Entity index = null;
        for (Entity e : li) {
            if (!e.isDead) {
                double dd = ReikaMathLibrary.py3d(e.posX - x, e.posY - y, e.posZ - z);
                if (dd < d) {
                    index = e;
                    d = dd;
                }
            }
        }
        return index;
    }

    public static EntityLivingBase getClosestLivingEntityOfClass(
        Class<? extends EntityLivingBase> c,
        World world,
        double x,
        double y,
        double z,
        double range
    ) {
        AxisAlignedBB box
            = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(range, range, range);
        return getClosestLivingEntityOfClass(c, world, x, y, z, box);
    }

    public static boolean otherDimensionsExist() {
        return ModList.MYSTCRAFT.isLoaded() || ModList.TWILIGHT.isLoaded()
            || ModList.EXTRAUTILS.isLoaded() || Loader.isModLoaded("Aroma1997sDimension")
            || Loader.isModLoaded("rftools");
    }

    public static int getAmbientTemperatureAt(World world, int x, int y, int z) {
        return getAmbientTemperatureAt(world, x, y, z, 1);
    }

    public static int
    getAmbientTemperatureAt(World world, int x, int y, int z, float varFactor) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        int Tamb = ReikaBiomeHelper.getBiomeTemp(world, biome);
        float temp = Tamb;

        if (SpecialDayTracker.instance.isWinterEnabled()
            && world.provider.dimensionId != -1) {
            temp -= 10;
        }

        if (varFactor > 0) {
            Simplex3DGenerator gen = getOrCreateTemperatureNoise(world);
            //ReikaJavaLibrary.pConsole(new Coordinate(x, y, z)+" > "+gen.getValue(x, y,
            //z));
            if (biome instanceof CustomTemperatureBiome)
                varFactor *= ((CustomTemperatureBiome) biome)
                                 .getNoiseVariationStrength(world, x, y, z, varFactor);
            temp += gen.getValue(x, y, z) * varFactor * TEMP_NOISE_BASE;
        }

        if (world.provider.dimensionId == -1) {
            if (y > 128) {
                temp -= 50;
            }
            if (y < 45) {
                int d = y <= 30 ? 15 : 45 - y;
                temp += 20 * d;
            }
        } else {
            if (!world.provider.hasNoSky) {
                if (world.canBlockSeeTheSky(x, y + 1, z)) {
                    float sun = getSunIntensity(world, true, 0);
                    if (biome instanceof CustomTemperatureBiome)
                        temp += ((CustomTemperatureBiome) biome)
                                    .getSurfaceTemperatureModifier(
                                        world, x, y, z, temp, sun
                                    );
                    else
                        temp += (sun - 0.75F) * (world.isRaining() ? 10 : 20);
                }
                if (!isVoidWorld(world, x, z)) {
                    int h = world.provider.getAverageGroundLevel();
                    int dy = h - y;
                    if (dy > 0) {
                        if (dy < 20) {
                            temp -= dy;
                            temp = Math.max(temp, Tamb - 20);
                        } else if (dy < 25) {
                            temp -= 2 * (25 - dy);
                            temp = Math.max(temp, Tamb - 20);
                        } else {
                            temp += 100 * (dy - 20) / h;
                            temp = Math.min(temp, Tamb + 70);
                        }
                    }
                    if (y > 96) {
                        temp -= (y - 96) / 4;
                        if (biome instanceof CustomTemperatureBiome)
                            temp += ((CustomTemperatureBiome) biome)
                                        .getAltitudeTemperatureModifier(
                                            world, x, y, z, temp, -dy
                                        );
                    }
                }
            }
        }

        return (int) temp;
    }

    /**
     * Returns whether there is a TileEntity at the specified position. Does not call
     * getTileEntity().
     */
    public static boolean tileExistsAt(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (b == Blocks.air)
            return false;
        if (b == null)
            return false;
        int meta = world.getBlockMetadata(x, y, z);
        if (!b.hasTileEntity(meta))
            return false;
        return true;
    }

    public static int
    getFreeDistance(World world, int x, int y, int z, ForgeDirection dir, int maxdist) {
        for (int i = 1; i < maxdist; i++) {
            int dx = x + dir.offsetX * i;
            int dy = y + dir.offsetY * i;
            int dz = z + dir.offsetZ * i;
            if (!softBlocks(world, dx, dy, dz)) {
                return i - 1;
            }
        }
        return maxdist;
    }

    public static boolean isExposedToAir(World world, int x, int y, int z) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetZ;
            int dz = z + dir.offsetY;
            if (!world.checkChunksExist(dx, dy, dz, dx, dy, dz))
                continue;
            if (countsAsAirExposure(world, dx, dy, dz)) {
                return true;
            }
        }
        return false;
    }

    public static boolean countsAsAirExposure(World world, int dx, int dy, int dz) {
        Block b = world.getBlock(dx, dy, dz);
        if (b == null || b == Blocks.air || b.isAir(world, dx, dy, dz))
            return true;
        if (b.getCollisionBoundingBoxFromPool(world, dx, dy, dz) == null)
            return true;
        if (InterfaceCache.BCPIPEBLOCK.instanceOf(b)
            || InterfaceCache.TDDUCTBLOCK.instanceOf(b)
            || InterfaceCache.AECABLEBLOCK.instanceOf(b)) {
            return true;
        }
        if (InterfaceCache.EIOCONDUITBLOCK.instanceOf(b)) {
            TileEntity te = world.getTileEntity(dx, dy, dz);
            Block f = EnderIOFacadeHandler.instance.getFacade(te);
            if (f == null)
                return true;
            int mf = EnderIOFacadeHandler.instance.getFacadeMeta(te);
            b = f;
        }
        Material mat = b.getMaterial();
        if (mat != null) {
            if (mat == Material.circuits || mat == Material.air || mat == Material.cactus
                || mat == Material.fire)
                return true;
            if (mat == Material.plants || mat == Material.portal || mat == Material.vine
                || mat == Material.web)
                return true;
            if (!mat.isSolid())
                return true;
        }
        return false;
    }

    public static boolean
    isExposedToAirWithException(World world, int x, int y, int z, Block ex) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetZ;
            int dz = z + dir.offsetY;
            if (!world.checkChunksExist(dx, dy, dz, dx, dy, dz))
                continue;
            Block b = world.getBlock(dx, dy, dz);
            if (b == ex)
                continue;
            if (countsAsAirExposure(world, dx, dy, dz)) {
                return true;
            }
        }
        return false;
    }

    public static int countAdjacentBlocks(
        IBlockAccess world, int x, int y, int z, Block id, boolean checkCorners
    ) {
        int count = 0;
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetZ;
            int dz = z + dir.offsetY;
            Block id2 = world.getBlock(dx, dy, dz);
            if (id == id2)
                count++;
        }

        if (checkCorners) {
            for (int n = 0; n < RelativePositionList.cornerDirections.getSize(); n++) {
                Coordinate d
                    = RelativePositionList.cornerDirections.getNthPosition(x, y, z, n);
                int dx = d.xCoord;
                int dy = d.yCoord;
                int dz = d.zCoord;
                Block id2 = world.getBlock(dx, dy, dz);
                if (id == id2) {
                    count++;
                }
            }
        }

        return count;
    }

    public static void forceGenAndPopulate(World world, int x, int z) {
        forceGenAndPopulate(world, x, z, 0);
    }

    public static void forceGenAndPopulate(World world, int x, int z, int range) {
        for (int i = -range; i <= range; i++) {
            for (int k = -range; k <= range; k++) {
                int dx = x + i * 16;
                int dz = z + k * 16;

                Chunk ch = world.getChunkFromBlockCoords(dx, dz);
                WorldChunk wc = new WorldChunk(world, ch);
                if (forcingChunkSet.contains(wc))
                    break;
                forcingChunkSet.add(world.getTotalWorldTime(), wc);
                IChunkProvider p = world.getChunkProvider();
                if (!ch.isTerrainPopulated) {
                    try {
                        p.populate(p, dx >> 4, dz >> 4);
                    } catch (ConcurrentModificationException e) {
                        DragonAPICore.logError("Chunk at "+dx+", "+dz+" failed to allow population due to a ConcurrentModificationException! Contact Reika with information on any mods that might be multithreading worldgen!");
                        e.printStackTrace();
                    } catch (Exception e) {
                        DragonAPICore.logError(
                            "Chunk at " + dx + ", " + dz + " failed to allow population!"
                        );
                        e.printStackTrace();
                    }
                }
            }
        }
        forcingChunkSet.clear();
    }

    public static Collection<IInventory>
    getAllInventories(World world, int x, int y, int z, int r) {
        Collection<IInventory> c = new ArrayList();
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    int dx = x + i;
                    int dy = y + j;
                    int dz = z + k;
                    if (tileExistsAt(world, dx, dy, dz)) {
                        TileEntity te = world.getTileEntity(dx, dy, dz);
                        if (te instanceof IInventory)
                            c.add((IInventory) te);
                    }
                }
            }
        }
        return c;
    }

    public static void dropAndDestroyBlockAt(
        World world, int x, int y, int z, EntityPlayer ep, boolean breakAll, boolean FX
    ) {
        Block b = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (b.blockHardness < 0 && !breakAll)
            return;
        dropBlockAt(world, x, y, z, ep);
        if (ep != null)
            b.onBlockDestroyedByPlayer(world, x, y, z, meta);
        if (ep != null)
            b.removedByPlayer(world, ep, x, y, z, true);
        else
            world.setBlock(x, y, z, Blocks.air);
        if (FX) {
            ReikaPacketHelper.sendDataPacketWithRadius(
                DragonAPIInit.packetChannel,
                PacketIDs.BREAKPARTICLES.ordinal(),
                world,
                x,
                y,
                z,
                128,
                Block.getIdFromBlock(b),
                meta
            );
            ReikaSoundHelper.playBreakSound(world, x, y, z, b);
        }
    }

    public static boolean
    matchWithItemStack(World world, int x, int y, int z, ItemStack is) {
        return ReikaItemHelper.matchStackWithBlock(is, world.getBlock(x, y, z))
            && is.getItemDamage() == world.getBlockMetadata(x, y, z);
    }

    public static boolean isSubmerged(IBlockAccess iba, int x, int y, int z) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            Block b = iba.getBlock(dx, dy, dz);
            if (b == Blocks.water || b == Blocks.flowing_water)
                continue;
            if (!b.isOpaqueCube() && !b.renderAsNormalBlock() && b.getRenderType() != 0)
                return false;
        }
        return true;
    }

    /**
     * Returns true if the chunk here (block coords) has been generated, whether or not
     * it is currently loaded.
     */
    public static boolean isChunkGenerated(WorldServer world, int x, int z) {
        return isChunkGeneratedChunkCoords(world, x >> 4, z >> 4);
    }

    /**
     * Returns true if the chunk here has been generated, whether or not it is currently
     * loaded.
     */
    public static boolean isChunkGeneratedChunkCoords(WorldServer world, int x, int z) {
        IChunkLoader loader = world.theChunkProviderServer.currentChunkLoader;
        return loader instanceof AnvilChunkLoader
            && ((AnvilChunkLoader) loader).chunkExists(world, x, z);
    }

    public static int getWaterDepth(IBlockAccess world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        int c = 0;
        while (b == Blocks.water) {
            y--;
            c++;
            b = world.getBlock(x, y, z);
        }
        return c;
    }

    public static boolean isWorldLoaded(int dim) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            throw new MisuseException("This cannot be called clientside!");
        return DimensionManager.getWorld(dim) != null;
    }

    public static int getTopNonAirBlock(World world, int x, int z, boolean skipClouds) {
        Chunk ch = world.getChunkFromBlockCoords(x, z);
        int top = ch.getTopFilledSegment() + 15;
        for (int y = top; y > 0; y--) {
            Block b = ch.getBlock(x & 15, y, z & 15);
            if (!b.isAir(world, x & 15, y, z & 15)
                && (!skipClouds || b != NaturaBlockHandler.getInstance().cloudID)) {
                return y;
            }
        }
        return 0;
    }

    public static World getBasicReferenceWorld() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT
            ? getClientWorld()
            : DimensionManager.getWorld(0);
    }

    @SideOnly(Side.CLIENT)
    private static World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    public static ArrayList<BlockKey> getBlocksAlongVector(
        World world, double x1, double y1, double z1, double x2, double y2, double z2
    ) {
        HashSet<Coordinate> set = new HashSet();
        ArrayList<BlockKey> li = new ArrayList();
        double dd = ReikaMathLibrary.py3d(x2 - x1, y2 - y1, z2 - z1);
        for (double d = 0; d <= dd; d += 0.25) {
            double f = d / dd;
            double dx = x1 + f * (x2 - x1);
            double dy = y1 + f * (y2 - y1);
            double dz = z1 + f * (z2 - z1);
            Coordinate c = new Coordinate(dx, dy, dz);
            if (!set.contains(c)) {
                set.add(c);
                li.add(new BlockKey(c.getBlock(world), c.getBlockMetadata(world)));
            }
        }
        return li;
    }

    public static FluidStack getDrainableFluid(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (b instanceof IFluidBlock) {
            Fluid f = ((IFluidBlock) b).getFluid();
            if (f == null) {
                DragonAPICore.logError(
                    "Found a fluid block " + b + ":" + b.getUnlocalizedName()
                    + " with a null fluid @ " + x + ", " + y + ", " + z + "!"
                );
                return null;
            }
            return ((IFluidBlock) b).drain(world, x, y, z, false);
        } else if (b instanceof BlockLiquid) {
            if (meta != 0)
                return null;
            Fluid f = ReikaFluidHelper.lookupFluidForBlock(b);
            return f != null ? new FluidStack(f, FluidContainerRegistry.BUCKET_VOLUME)
                             : null;
        } else {
            return null;
        }
    }

    /** A surrogate for the method in world, more performant and fires an event. */
    public static EntityPlayer
    getClosestVulnerablePlayer(World world, double x, double y, double z, double r) {
        double dd = Double.POSITIVE_INFINITY;
        double dist = -1;
        EntityPlayer ret = null;

        for (EntityPlayer ep : ((List<EntityPlayer>) world.playerEntities)) {
            boolean flag = false;
            Result res = MobTargetingEvent.firePre(ep, world, x, y, z, r);
            if (res != Result.DENY
                && (!ep.capabilities.disableDamage || res == Result.ALLOW)
                && ep.isEntityAlive()) {
                dist = ep.getDistanceSq(x, y, z);

                if (ep.isSneaking()) {
                    r *= 0.8;
                }

                if (ep.isInvisible()) {
                    float f = ep.getArmorVisibility();

                    if (f < 0.1F) {
                        f = 0.1F;
                    }

                    r *= 0.7F * f;
                }

                flag = (r < 0 || dist < r * r) && dist < dd;
                flag = MobTargetingEvent.fire(ep, world, x, y, z, r, flag);
            }

            if (flag) {
                dd = dist;
                ret = ep;
            }
        }

        EntityPlayer post = MobTargetingEvent.firePost(world, x, y, z, r);
        if (post != null)
            ret = post;

        return ret;
    }

    public static void erodeBlock(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        Material mat = b.getMaterial();
        if (ReikaBlockHelper.isLiquid(b) || mat == Material.plants
            || mat == Material.leaves) {
            world.setBlock(x, y, z, Blocks.air);
        } else if (b == Blocks.cobblestone) {
            world.setBlock(x, y, z, Blocks.gravel);
        } else if (b == Blocks.gravel) {
            world.setBlock(x, y, z, Blocks.sand);
        } else if (b == Blocks.sand) {
            world.setBlock(x, y, z, Blocks.air);
        } else if (mat == Material.rock) {
            world.setBlock(x, y, z, Blocks.cobblestone);
        } else if (b instanceof BlockLog) {
            world.setBlock(x, y, z, Blocks.fire);
        } else if (mat == Material.ground) {
            world.setBlock(x, y, z, Blocks.sand);
        } else if (mat == Material.grass || b == Blocks.grass) {
            world.setBlock(x, y, z, Blocks.dirt);
        }
    }

    public static boolean
    hydrateFarmland(World world, int x, int y, int z, boolean fullHydrate) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta < 7) {
            world.setBlockMetadataWithNotify(x, y, z, fullHydrate ? 7 : meta + 1, 3);
            return true;
        }
        return false;
    }

    public static Coordinate findTreeNear(World world, int x, int y, int z, int r) {
        int[] ddy = r > 2
            ? new int[] { y - r, y - r / 2, y - 1, y, y + 1, y + r / 2, y + r }
            : new int[] { y - 2, y - 1, y, y + 1, y + 2 };
        for (int f = 0; f < ddy.length; f++) {
            int dy = ddy[f];
            for (int i = -r; i <= r; i++) {
                for (int k = -r; k <= r; k++) {
                    if (ReikaBlockHelper.isWood(world, x + i, dy, z + k)) {
                        if (ReikaBlockHelper.isWood(world, x + i, dy + 1, z + k)
                            && ReikaBlockHelper.isWood(world, x + i, dy - 1, z + k)) {
                            return new Coordinate(x + i, dy, z + k);
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isBlockEncased(World world, int x, int y, int z, Block b) {
        return isBlockEncased(world, x, y, z, b, -1);
    }

    public static boolean
    isBlockEncased(World world, int x, int y, int z, Block b, int meta) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i != 0 || j != 0 || k != 0) {
                        int dx = x + i;
                        int dy = y + j;
                        int dz = z + k;
                        Block b2 = world.getBlock(dx, dy, dz);
                        int meta2 = world.getBlockMetadata(dx, dy, dz);
                        if (b != null) {
                            if (b2 != b || (meta != -1 && meta2 != meta)) {
                                return false;
                            }
                        } else {
                            if (b2.isAir(world, dx, dy, dz))
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean
    matchBlock(IBlockAccess world, int x, int y, int z, Block b, int meta) {
        return world.getBlock(x, y, z) == b && world.getBlockMetadata(x, y, z) == meta;
    }

    public static void fertilizeAndHealBlock(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (b == Blocks.dirt && meta == 0) {
            world.setBlock(x, y, z, Blocks.grass);
        } else if (b == Blocks.deadbush || (b == Blocks.tallgrass && meta == 0)) {
            world.setBlock(x, y, z, Blocks.tallgrass, rand.nextInt(6) == 0 ? 2 : 1, 3);
        } else if (b == Blocks.farmland) {
            hydrateFarmland(world, x, y, z, true);
        }
    }

    public static int getTopSolidOrLiquidBlockForDouble(World world, double x, double z) {
        return world.getTopSolidOrLiquidBlock(
            MathHelper.floor_double(x), MathHelper.floor_double(z)
        );
    }

    public static MobSpawnerBaseLogic
    generateSpawner(World world, int x, int y, int z, Class mob) {
        world.setBlock(x, y, z, Blocks.mob_spawner);
        TileEntityMobSpawner te = (TileEntityMobSpawner) world.getTileEntity(x, y, z);
        ReikaSpawnerHelper.setMobSpawnerMob(
            te, (String) EntityList.classToStringMapping.get(mob)
        );
        return te.func_145881_a();
    }

    public static boolean hasAdjacentWater(
        IBlockAccess world, int x, int y, int z, boolean vertical, boolean source
    ) {
        for (int i = vertical ? 0 : 2; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            Block id2 = world.getBlock(dx, dy, dz);
            int meta2 = world.getBlockMetadata(dx, dy, dz);
            if ((id2 == Blocks.water || id2 == Blocks.flowing_water)
                && (meta2 == 0 || !source))
                return true;
        }
        return false;
    }

    public static void triggerFallingBlock(World world, int x, int y, int z, Block b) {
        if (y >= 0 && BlockFalling.func_149831_e(world, x, y - 1, z)) {
            int r = 32;

            if (!BlockFalling.fallInstantly
                && world.checkChunksExist(x - r, y - r, z - r, x + r, y + r, z + r)) {
                if (!world.isRemote) {
                    EntityFallingBlock e = new EntityFallingBlock(
                        world,
                        x + 0.5,
                        y + 0.5,
                        z + 0.5,
                        b,
                        world.getBlockMetadata(x, y, z)
                    );
                    world.spawnEntityInWorld(e);
                }
            } else {
                world.setBlockToAir(x, y, z);

                while (BlockFalling.func_149831_e(world, x, y - 1, z) && y > 0) {
                    y--;
                }
                if (y > 0) {
                    world.setBlock(x, y, z, b);
                }
            }
        }
    }

    public static boolean
    isBlockSurroundedBySolid(World world, int x, int y, int z, boolean vertical) {
        for (int i = vertical ? 0 : 2; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            Block id = world.getBlock(dx, dy, dz);
            if (!id.getMaterial().isSolid())
                return false;
        }
        return true;
    }

    public static boolean checkForAdjSolidBlock(World world, int x, int y, int z) {
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dy = y + dir.offsetY;
            int dz = z + dir.offsetZ;
            Block id = world.getBlock(dx, dy, dz);
            if (id.getMaterial().isSolid())
                return true;
        }
        return false;
    }

    public static boolean isVoidWorld(World world, int x, int z) {
        //if (world.getChunkProvider().provideChunk(x >> 4, z >> 4) instanceof EmptyChunk)
        //want the provider that only returns these 	return true;
        return world.getBlock(x, 0, z) == Blocks.air || world.canBlockSeeTheSky(x, 1, z);
    }

    public static int getSuperflatHeight(World world) {
        if (ModList.MYSTCRAFT.isLoaded() && ReikaMystcraftHelper.isMystAge(world)) {
            return ReikaMystcraftHelper.getFlatWorldThickness(world);
        }
        IChunkProvider icp = world.provider.createChunkGenerator();
        if (!(icp instanceof ChunkProviderFlat))
            return 4;
        ChunkProviderFlat f = (ChunkProviderFlat) icp;
        FlatGeneratorInfo ifo = f.flatWorldGenInfo;
        int sum = 0;
        for (Object o : ifo.getFlatLayers()) {
            FlatLayerInfo fi = (FlatLayerInfo) o;
            sum += fi.getLayerCount();
        }
        return sum;
    }

    public static Coordinate getRandomLoadedCoordinate(World world) {
        ArrayList<ChunkCoordIntPair> li = new ArrayList(world.activeChunkSet);
        if (li.isEmpty())
            return null;
        int idx = rand.nextInt(li.size());
        ChunkCoordIntPair cp = li.get(idx);
        return new Coordinate(
            (cp.chunkXPos << 4) + rand.nextInt(16),
            0,
            (cp.chunkZPos << 4) + rand.nextInt(16)
        );
    }

    /**
     * Returns true if the chunk is loaded by the ChunkProviderServer, which is true if
     * the noiseGen phase has been completed.
     */
    public static boolean isChunkPastNoiseGen(World world, int x, int z) {
        return world instanceof WorldServer
            ? ((ChunkProviderServer) world.getChunkProvider())
                  .loadedChunkHashMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(x, z))
            : true;
    }

    public static boolean
    isChunkPastCompletelyFinishedGenerating(World world, int x, int z) {
        if (isChunkPastNoiseGen(world, x, z)) {
            Chunk c = world.getChunkFromChunkCoords(x, z);
            return c.isTerrainPopulated; // && c.isLightPopulated;
        }
        return false;
    }

    public static List<IWorldGenerator> getModdedGenerators() {
        try {
            List<IWorldGenerator> li
                = (List<IWorldGenerator>) moddedGeneratorList.get(null);
            while (li == null) {
                computeModdedGeneratorList.invoke(null);
                li = (List<IWorldGenerator>) moddedGeneratorList.get(null);
            }
            return li;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Note that you must reset the seed of this random for every generator (so they all
     * get the same seed)!
     */
    public static ResettableRandom
    getModdedGeneratorChunkRand(int cx, int cz, World world) {
        long worldSeed = world.getSeed();
        moddedGenRand_Calcer.setSeed(worldSeed);
        long xSeed = moddedGenRand_Calcer.nextLong() >> 2 + 1L;
        long zSeed = moddedGenRand_Calcer.nextLong() >> 2 + 1L;
        long seed = (xSeed * cx + zSeed * cz) ^ worldSeed;
        moddedGenRand.setSeed(seed);
        return moddedGenRand;
    }

    public static void
    cancelScheduledTick(WorldServer world, int x, int y, int z, Block b) {
        Chunk c = world.getChunkFromBlockCoords(x, z);
        List<NextTickListEntry> li = world.getPendingBlockUpdates(c, true);
        if (li != null) {
            for (NextTickListEntry e : li) {
                if (e.xCoord == x && e.yCoord == y && e.zCoord == z
                    && e.func_151351_a() == b) {
                } else {
                    rescheduleTick(world, e);
                }
            }
        }
    }

    private static void rescheduleTick(WorldServer world, NextTickListEntry e) {
        world.scheduleBlockUpdateWithPriority(
            e.xCoord,
            e.yCoord,
            e.zCoord,
            e.func_151351_a(),
            (int) (e.scheduledTime - world.getTotalWorldTime()),
            e.priority
        );
    }

    public static boolean
    regionContainsBiome(World world, int x0, int x1, int z0, int z1, BiomeGenBase b) {
        return world.getBiomeGenForCoords(x0, z0) == b
            || world.getBiomeGenForCoords(x1, z0) == b
            || world.getBiomeGenForCoords(x0, z1) == b
            || world.getBiomeGenForCoords(x1, z1) == b;
    }

    public static boolean isAdjacentToCrop(World world, int x, int y, int z) {
        for (int i = 2; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            int dx = x + dir.offsetX;
            int dz = z + dir.offsetZ;
            if (world.checkChunksExist(dx, y, dz, dx, y, dz)) {
                Block id = world.getBlock(dx, y, dz);
                if (ReikaCropHelper.getCrop(id) != null
                    || ModCropList.getModCrop(id, world.getBlockMetadata(dx, y, dz))
                        != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPositionEmpty(World world, double dx, double dy, double dz) {
        int x = MathHelper.floor_double(dx);
        int y = MathHelper.floor_double(dy);
        int z = MathHelper.floor_double(dz);
        Block b = world.getBlock(x, y, z);
        return b.isAir(world, x, y, z);
    }

    public static double
    getAmbientPressureAt(World world, int x, int y, int z, boolean checkLiquidColumn) {
        double Pamb = 101.3;
        if (world.provider.dimensionId == -1)
            Pamb = 20000;
        else if (world.provider.dimensionId == 1)
            Pamb = 24; //0.2atm

        if (world.isThundering())
            Pamb -= 5;
        else if (world.isRaining())
            Pamb -= 2.5;

        if (y < 30 && world.getWorldInfo().getTerrainType() != WorldType.FLAT) {
            double f = (30 - y) / 30D;
            Pamb *= 1 + 0.5 * f;
        }
        if (y > 64 && world.getWorldInfo().getTerrainType() == WorldType.FLAT) {
            double f = (y - 64) / (128D - 64D);
            Pamb *= 1 - 0.2 * f;
        }
        if (y > 128) {
            double f = (y - 128) / (192D - 128D);
            Pamb *= 1 - 0.2 * f;
        }
        if (y > 192) {
            double f = (y - 128) / (256D - 192D);
            Pamb *= 1 - 0.4 * f;
        }
        Pamb *= AtmosphereHandler.getAtmoDensity(world); //higher for thin atmo
        if (checkLiquidColumn) {
            double fluid = getFluidColumnPressure(world, x, y + 1, z);
            fluid *= 1 + PlanetDimensionHandler.getExtraGravity(world) - 0.03125;
            Pamb += fluid;
        }
        return Pamb;
    }

    /** In kPa -> rho g h */
    public static double getFluidColumnPressure(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (b == Blocks.flowing_water)
            b = Blocks.water;
        else if (b == Blocks.flowing_lava)
            b = Blocks.lava;
        Fluid f = ReikaFluidHelper.lookupFluidForBlock(b);
        int p = 0;
        while (f != null && y < 256) {
            p += f.getDensity(world, x, y, z) * ReikaPhysicsHelper.g;

            y++;

            b = world.getBlock(x, y, z);
            if (b == Blocks.flowing_water)
                b = Blocks.water;
            else if (b == Blocks.flowing_lava)
                b = Blocks.lava;
            f = ReikaFluidHelper.lookupFluidForBlock(b);
        }
        return p / 1000D;
    }

    /** Radius is in CHUNKS! */
    public static boolean isRadiusLoaded(World world, int x, int z, int r) {
        int x0 = (x >> 4) - r;
        int x1 = (x >> 4) + r;
        int z0 = (z >> 4) - r;
        int z1 = (z >> 4) + r;
        for (int dx = x0; dx <= x1; dx++) {
            for (int dz = z0; dz <= z1; dz++) {
                if (!world.getChunkProvider().chunkExists(dx, dz))
                    return false;
            }
        }
        return true;
    }

    public static boolean isFakeWorld(World world) {
        Boolean fake = fakeWorldTypes.get(world.getClass());
        if (fake == null) {
            fake = calcFakeWorld(world);
            fakeWorldTypes.put(world.getClass(), fake);
        }
        return fake.booleanValue();
    }

    private static boolean calcFakeWorld(World world) {
        String s = world.getClass().getSimpleName();
        if (s != null && s.toLowerCase(Locale.ENGLISH).contains("fake"))
            return true;
        s = world.provider != null ? world.provider.getDimensionName() : "fake";
        if (s != null && s.toLowerCase(Locale.ENGLISH).contains("fake"))
            return true;
        return false;
    }

    public static int getBiomeSize(World world) {
        WorldType type = world.getWorldInfo().getTerrainType();
        return GenLayer.getModdedBiomeSize(
            type, (byte) (type == WorldType.LARGE_BIOMES ? 6 : 4)
        );
    }

    public static void blockRain(World world, int ticks, boolean thunder) {
        //world.provider.resetRainAndThunder();
        world.getWorldInfo().setRaining(false);
        world.getWorldInfo().setThundering(false);
        if (thunder) {
            world.getWorldInfo().setThunderTime(ticks);
        } else {
            world.getWorldInfo().setRainTime(ticks);
        }
    }

    public static void convertBiomeRegionFrom(
        World world,
        int x,
        int z,
        BiomeGenBase from,
        BiomeGenBase to,
        BiomeGenBase same,
        int depthLimit,
        boolean applyEnv
    ) {
        HashSet<Coordinate> done = new HashSet();
        HashSet<Coordinate> next = new HashSet();
        HashSet<Coordinate> next2 = new HashSet();
        next.add(new Coordinate(x, 0, z));
        int n = 0;
        while (!next.isEmpty() && (n < depthLimit || depthLimit == -1)) {
            for (Coordinate c : next) {
                BiomeGenBase put = to == null
                    ? getNaturalGennedBiomeAt(world, c.xCoord, c.zCoord)
                    : to;
                if (put == from && same != null) {
                    put = same;
                }
                setBiomeForXZ(world, c.xCoord, c.zCoord, put, applyEnv);
                for (int i = 2; i < 6; i++) {
                    ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
                    Coordinate c2 = c.offset(dir, 1);
                    BiomeGenBase b = world.getBiomeGenForCoords(c2.xCoord, c2.zCoord);
                    if (b == from) {
                        next2.add(c2);
                    }
                }
            }
            next = next2;
            n++;
        }
    }

    public static WorldID getCurrentWorldID(World world) {
        if (world.isRemote)
            throw new MisuseException("This cannot be called from the client side!");
        String f = getWorldKey(world);
        WorldID get = worldIDMap.get(f);
        if (get == null) {
            get = calculateWorldID(world);
            worldIDMap.put(f, get);
        }
        return get;
    }

    private static String getWorldKey(World world) {
        String key = worldKeys.get(world.provider.dimensionId);
        if (key == null) {
            File f = world.getSaveHandler().getWorldDirectory();
            key = ReikaFileReader.getRealPath(f); //ReikaFileReader.getRelativePath(DragonAPICore.getMinecraftDirectory(),
                                                  //f);
            worldKeys.put(world.provider.dimensionId, key);
        }
        return key;
    }

    public static void clearWorldKeyCache() {
        worldKeys.clear();
    }

    private static WorldID calculateWorldID(World world) {
        File f = new File(getWorldMetadataFolder(world), "worldID.dat");
        if (!f.exists())
            return WorldID.NONEXISTENT;
        return WorldID.readFile(f);
        //String name = ReikaFileReader.getFileNameNoExtension(f);
        //return ReikaJavaLibrary.safeLongParse(name);
    }

    private static File getWorldMetadataFolder(World world) {
        if (world.isRemote)
            throw new MisuseException("This cannot be called from the client side!");
        File ret = new File(world.getSaveHandler().getWorldDirectory(), "DragonAPI_Data");
        ret.mkdirs();
        return ret;
    }

    public static void onWorldCreation(World world) {
        if (world.isRemote)
            throw new MisuseException("This cannot be called from the client side!");
        File folder = getWorldMetadataFolder(world);
        File f = new File(folder, "worldID.dat");
        try {
            f.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        WorldID id = new WorldID(world);
        id.writeToFile(f);
    }

    public static class WorldIDBase {
        public final long worldCreationTime;
        public final long sourceSessionStartTime;
        public final long sessionWorldIndex;
        public final String originalFolder;

        public WorldIDBase(long time, long session, long index, String folder) {
            worldCreationTime = time;
            sourceSessionStartTime = session;
            sessionWorldIndex = index;
            originalFolder = folder;
        }

        public final long getUniqueHash() {
            return worldCreationTime
                ^ ((((long) originalFolder.hashCode()) << 32) | sessionWorldIndex);
        }

        @Override
        public final String toString() {
            return this.getClass().getSimpleName() + " " + worldCreationTime + " in "
                + originalFolder + "#" + sessionWorldIndex;
        }
    }

    public static final class WorldID extends WorldIDBase {
        private static int worldsThisSession = 0;

        private static final WorldID NONEXISTENT
            = new WorldID(0, 0, 0, "[NONEXISTENT]", "[NONEXISTENT]", new HashSet());

        public final String creatingPlayer;

        private final HashSet<String> modList;

        private WorldID(World world) {
            this(
                System.currentTimeMillis(),
                DragonAPICore.getLaunchTime(),
                worldsThisSession,
                ReikaFileReader.getRealPath(world.getSaveHandler().getWorldDirectory()),
                getSessionName(),
                getModList()
            );
            worldsThisSession++;
        }

        private static HashSet<String> getModList() {
            HashSet<String> ret = new HashSet();
            for (ModContainer mc : Loader.instance().getActiveModList()) {
                ret.add(mc.getModId());
            }
            return ret;
        }

        private WorldID(
            long time,
            long session,
            int index,
            String folder,
            String player,
            HashSet<String> modlist
        ) {
            super(time, session, index, folder);
            creatingPlayer = player;
            modList = modlist;
        }

        private static String getSessionName() {
            return DragonAPICore.getLaunchingPlayer().getName();
        }

        public boolean isValid() {
            return worldCreationTime > 0 && !originalFolder.equals("[NONEXISTENT]");
        }

        private void writeToFile(File f) {
            NBTTagCompound data = new NBTTagCompound();
            data.setLong("creationTime", worldCreationTime);
            data.setLong("sourceSession", sourceSessionStartTime);
            data.setLong("sessionIndex", sessionWorldIndex);
            data.setString("originalFolder", originalFolder);
            data.setString("creatingPlayer", creatingPlayer);
            try (FileOutputStream out = new FileOutputStream(f)) {
                CompressedStreamTools.writeCompressed(data, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static WorldID readFile(File f) {
            try (FileInputStream in = new FileInputStream(f)) {
                NBTTagCompound data = CompressedStreamTools.readCompressed(in);
                long c = data.getLong("creationTime");
                long s = data.getLong("sourceSession");
                String folder = data.getString("originalFolder");
                String player = data.getString("creatingPlayer");
                HashSet<String> modlist = new HashSet();
                NBTTagList li = data.getTagList("mods", NBTTypes.STRING.ID);
                for (Object o : li.tagList) {
                    modlist.add((String) o);
                }
                return new WorldID(
                    c, s, data.getInteger("sessionIndex"), folder, player, modlist
                );
            } catch (Exception e) {
                e.printStackTrace();
                return NONEXISTENT;
            }
        }

        public void sendClientPacket(EntityPlayerMP ep) {
            int[] timeInts = ReikaJavaLibrary.splitLong(worldCreationTime);
            int[] sessionInts = ReikaJavaLibrary.splitLong(sourceSessionStartTime);
            int[] indexInts = ReikaJavaLibrary.splitLong(sessionWorldIndex);
            ReikaPacketHelper.sendStringIntPacket(
                DragonAPIInit.packetChannel,
                PacketIDs.WORLDID.ordinal(),
                ep,
                originalFolder,
                timeInts[0],
                timeInts[1],
                sessionInts[0],
                sessionInts[1],
                indexInts[0],
                indexInts[1]
            );
        }
    }
}
