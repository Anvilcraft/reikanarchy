package Reika.DragonAPI.Base;

import java.util.ArrayList;
import java.util.Random;

import Reika.DragonAPI.Instantiable.Data.BlockStruct.AbstractSearch.PropagationCondition;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.AbstractSearch.TerminationCondition;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.BreadthFirstSearch;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockBox;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Event.LeafDecayEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockCustomLeaf extends BlockLeaves {
    /** For fast/fancy graphics */
    protected IIcon[][] icon = new IIcon[16][2];

    protected final Random rand = new Random();

    protected BlockCustomLeaf() {
        this(false);
    }

    protected BlockCustomLeaf(boolean tick) {
        super();
        this.setCreativeTab(this.showInCreative() ? this.getCreativeTab() : null);
        this.setStepSound(soundTypeGrass);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            this.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setTickRandomly(tick || this.decays() || this.shouldRandomTick());
    }

    @Override
    public final boolean getCanBlockGrass() {
        return false;
    }

    /**
     * Overridden to allow conditional disabling of mod leaf control hacks, like the one
     * in RandomThings.
     */
    @Override
    public final void
    onNeighborBlockChange(World world, int x, int y, int z, Block neighborID) {
        this.onBlockUpdate(world, x, y, z);
        if (this.allowModDecayControl()) {
            super.onNeighborBlockChange(world, x, y, z, neighborID);
        } else {
        }
    }

    @Override
    public String[] func_150125_e() {
        return new String[] { this.getUnlocalizedName() };
    }

    protected void onBlockUpdate(World world, int x, int y, int z) {}

    public abstract boolean shouldRandomTick();

    public abstract boolean decays();

    public abstract boolean isNatural();

    public abstract boolean allowModDecayControl();

    public abstract boolean showInCreative();

    public abstract CreativeTabs getCreativeTab();

    @Override
    public IIcon getIcon(int par1, int par2) {
        return icon[par2][this.getOpacityIndex()];
    }

    public final IIcon getIcon(int meta, boolean fancy) {
        return icon[meta][fancy ? 0 : 1];
    }

    protected final int getOpacityIndex() {
        field_150121_P = Minecraft.getMinecraft().gameSettings.fancyGraphics;
        return field_150121_P ? 0 : 1;
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        return 1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int
    getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 30;
    }

    @Override
    public int
    getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 60;
    }

    @Override
    public final void updateTick(World world, int x, int y, int z, Random par5Random) {
        int meta = world.getBlockMetadata(x, y, z);
        //ReikaJavaLibrary.pConsole(Block.getIdFromBlock(this)+" @ "+x+", "+y+", "+z+" :
        //"+this.decays()+"&"+this.shouldTryDecay(world, x, y, z, meta));
        boolean flag = false;
        if (this.decays() && this.shouldTryDecay(world, x, y, z, meta)) {
            if (!LeafDecayEvent.fire(this, world, x, y, z)) {
                flag = this.decay(world, x, y, z, par5Random);
            }
        }
        if (!flag)
            this.onRandomUpdate(world, x, y, z, par5Random);
    }

    protected void onRandomUpdate(World world, int x, int y, int z, Random r) {}

    public abstract boolean shouldTryDecay(World world, int x, int y, int z, int meta);

    protected boolean
    decay(World world, final int x, final int y, final int z, Random par5Random) {
        final int sides = this.getNumberSidesToPropagate(world, x, y, z);

        TerminationCondition t = new TerminationCondition() {
            @Override
            public boolean isValidTerminus(World world, int dx, int dy, int dz) {
                return BlockCustomLeaf.this.isValidLog(world, x, y, z, dx, dy, dz);
            }
        };

        PropagationCondition c = new PropagationCondition() {
            @Override
            public boolean isValidLocation(
                World world, int dx, int dy, int dz, Coordinate from
            ) {
                return BlockCustomLeaf.this.isMatchingLeaf(world, x, y, z, dx, dy, dz)
                    || BlockCustomLeaf.this.isValidLog(world, x, y, z, dx, dy, dz);
            }
        };

        BreadthFirstSearch s = new BreadthFirstSearch(x, y, z, c, t) {
            @Override
            protected ArrayList<Coordinate> getNextSearchCoordsFor(
                World world, Coordinate c
            ) {
                ArrayList<Coordinate> li = new ArrayList();
                switch (sides) {
                    case 0:
                    case 1:
                    case 2:
                        int r = sides == 0 ? 2 : 1;
                        for (int i = -r; i <= r; i++) {
                            for (int j = -r; j <= r; j++) {
                                for (int k = -r; k <= r; k++) {
                                    if (sides != 2 || i == 0 || j == 0 || k == 0)
                                        li.add(c.offset(i, j, k));
                                }
                            }
                        }
                        break;
                    case 3:
                        li.addAll(c.getAdjacentCoordinates());
                        break;
                }
                return li;
            }
        };
        s.limit = BlockBox.block(x, y, z).expand(this.getMaximumLogSearchRadius());
        s.depthLimit = this.getMaximumLogSearchDepth();
        s.complete(world);
        boolean decay = s.getResult().isEmpty();
        if (decay) {
            this.dropBlockAsItemWithChance(
                world, x, y, z, world.getBlockMetadata(x, y, z), 1, 0
            );
            world.setBlockToAir(x, y, z);
            this.onLeafDecay(world, x, y, z);
        }
        return decay;
    }

    /**
     Controls how strict adjacency-for-log-support searching is, namely which blocks
     can be spread to from a given position. 3 = adjacent only, 2 = share one axis, 1 =
     touching on any corner, 0 = can jump one block
   */
    protected int getNumberSidesToPropagate(World world, int x, int y, int z) {
        return 3;
    }

    protected void onLeafDecay(World world, int x, int y, int z) {}

    @Override
    public final void beginLeavesDecay(World world, int x, int y, int z) {
        if (this.decays()) {}
    }

    @Override
    public void registerBlockIcons(IIconRegister ico) {
        for (int i = 0; i < this.getMetaLimit(); i++) {
            icon[i][0] = ico.registerIcon(this.getFancyGraphicsIcon(i));
            icon[i][1] = ico.registerIcon(this.getFastGraphicsIcon(i));
        }
    }

    protected abstract int getMetaLimit();

    @Override
    public final boolean isLeaves(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    public abstract String getFastGraphicsIcon(int meta);
    public abstract String getFancyGraphicsIcon(int meta);

    public abstract boolean isMatchingLeaf(
        IBlockAccess iba, int thisX, int thisY, int thisZ, int lookX, int lookY, int lookZ
    );
    public abstract boolean isValidLog(
        IBlockAccess iba, int thisX, int thisY, int thisZ, int lookX, int lookY, int lookZ
    );

    public abstract int getMaximumLogSearchRadius();
    public abstract int getMaximumLogSearchDepth();
}
