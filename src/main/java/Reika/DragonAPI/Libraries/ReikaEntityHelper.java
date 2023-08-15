package Reika.DragonAPI.Libraries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import Reika.DragonAPI.APIPacketHandler.PacketIDs;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.DragonAPIInit;
import Reika.DragonAPI.Instantiable.Data.KeyedItemStack;
import Reika.DragonAPI.Instantiable.DummyTeleporter;
import Reika.DragonAPI.Instantiable.IO.PacketTarget;
import Reika.DragonAPI.Interfaces.ComparableAI;
import Reika.DragonAPI.Interfaces.Entity.CustomProjectile;
import Reika.DragonAPI.Interfaces.Entity.EtherealEntity;
import Reika.DragonAPI.Interfaces.Entity.TameHostile;
import Reika.DragonAPI.Interfaces.Item.UnbreakableArmor;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.MathSci.ReikaPhysicsHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaVectorHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.Rendering.ReikaColorAPI;
import Reika.DragonAPI.ModInteract.Bees.ReikaBeeHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.DartItemHandler;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.InterfaceCache;
import WayofTime.alchemicalWizardry.api.spell.EntitySpellProjectile;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

public final class ReikaEntityHelper extends DragonAPICore {
    public static final IEntitySelector hostileOrPlayerSelector = new IEntitySelector() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityLivingBase
                && (e instanceof EntityPlayer || isHostile((EntityLivingBase) e));
        }
    };

    public static final IEntitySelector playerSelector = new IEntitySelector() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityPlayer;
        }
    };

    public static final IEntitySelector hostileSelector = new IEntitySelector() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityLivingBase && isHostile((EntityLivingBase) e);
        }
    };

    public static final IEntitySelector nonMobSelector = new IEntitySelector() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return !(e instanceof EntityLivingBase) || !isHostile((EntityLivingBase) e);
        }
    };

    public static final IEntitySelector itemSelector = new IEntitySelector() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityItem;
        }
    };

    public static final IEntitySelector itemOrXPSelector = new IEntitySelector() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityItem || e instanceof EntityXPOrb;
        }
    };

    public static final class NameContainsSelector implements IEntitySelector {
        private final String seek;

        public NameContainsSelector(String s) {
            seek = s;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return e.getClass().getName().contains(seek);
        }
    };

    public static final class NotSelfSelector implements IEntitySelector {
        private final Entity reference;

        public NotSelfSelector(Entity e) {
            reference = e;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return e != reference;
        }
    };

    public static final class ClassEntitySelector implements IEntitySelector {
        private final Class classType;
        private final boolean exactMatch;

        public ClassEntitySelector(Class c, boolean exact) {
            classType = c;
            exactMatch = exact;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return exactMatch ? e.getClass() == classType
                              : classType.isAssignableFrom(e.getClass());
        }
    }

    public static final class SpecificItemSelector implements IEntitySelector {
        private final KeyedItemStack item;

        public SpecificItemSelector(ItemStack is) {
            this(new KeyedItemStack(is).setSimpleHash(true));
        }

        public SpecificItemSelector(KeyedItemStack ks) {
            item = ks;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityItem
                && item.match(((EntityItem) e).getEntityItem());
        }
    };

    public static final class MultiItemSelector implements IEntitySelector {
        private final HashSet<KeyedItemStack> items = new HashSet();

        public MultiItemSelector(Collection<KeyedItemStack> c) {
            for (KeyedItemStack ks : c) {
                items.add(ks.copy().setSimpleHash(true));
            }
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            if (e instanceof EntityItem) {
                KeyedItemStack ks = new KeyedItemStack(((EntityItem) e).getEntityItem())
                                        .setSimpleHash(true);
                return items.contains(ks);
            }
            return false;
        }
    };

    public static final class EntityIDSelector implements IEntitySelector {
        private final int entityID;

        public EntityIDSelector(int id) {
            entityID = id;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return EntityList.getEntityID(e) == entityID;
        }
    }

    private static abstract class CombinedEntitySelector implements IEntitySelector {
        protected final Collection<IEntitySelector> callers = new ArrayList();

        private final CombinedEntitySelector addSelector(IEntitySelector e) {
            callers.add(e);
            return this;
        }
    }

    private static final class CombinedEntitySelectorAND extends CombinedEntitySelector {
        @Override
        public boolean isEntityApplicable(Entity e) {
            for (IEntitySelector ie : callers)
                if (!ie.isEntityApplicable(e))
                    return false;
            return true;
        }
    }

    private static final class CombinedEntitySelectorOR extends CombinedEntitySelector {
        @Override
        public boolean isEntityApplicable(Entity e) {
            for (IEntitySelector ie : callers)
                if (ie.isEntityApplicable(e))
                    return true;
            return false;
        }
    }

    public static final Comparator<String> entityByDisplayComparator
        = new Comparator<String>() {
              @Override
              public int compare(String o1, String o2) {
                  return String.CASE_INSENSITIVE_ORDER.compare(
                      getEntityDisplayName(o1), getEntityDisplayName(o2)
                  );
              }
          };

    private static HashMap<Class, Integer> mobColorArray = new HashMap();
    private static HashMap<Class, Boolean> hostilityMap = new HashMap();

    public static boolean tameMobTargeting;

    /**
     * Converts a mob ID to a color, based off the mob's color. Players return bright
     * red. Args: Mob ID
     */
    public static int mobToColor(EntityLivingBase ent) {
        Integer color = mobColorArray.get(ent.getClass());
        if (color == null) {
            color = calcMobColor(ent);
            mobColorArray.put(ent.getClass(), color);
        }
        return color;
    }

    private static int calcMobColor(EntityLivingBase e) {
        Class c = e.getClass();
        Class s = c.getSuperclass();
        while (s != null && s != Entity.class) {
            if (mobColorArray.containsKey(s)) {
                int clr = mobColorArray.get(s);
                clr = ReikaColorAPI.getColorWithBrightnessMultiplier(
                    clr, (float) ReikaRandomHelper.getRandomBetween(0.25, 1)
                );
                return clr;
            }
            c = s;
            s = c.getSuperclass();
        }
        return 0xffffff;
    }

    static {
        mobColorArray.put(EntityCreeper.class, ReikaColorAPI.RGBtoHex(65, 183, 54));
        mobColorArray.put(EntitySkeleton.class, ReikaColorAPI.GStoHex(207)); //Skeleton
        mobColorArray.put(
            EntitySpider.class, ReikaColorAPI.RGBtoHex(90, 71, 43)
        ); //Spider
        mobColorArray.put(
            EntityGiantZombie.class, ReikaColorAPI.RGBtoHex(67, 109, 53)
        ); //Giant
        mobColorArray.put(
            EntityZombie.class, ReikaColorAPI.RGBtoHex(67, 109, 53)
        ); //Zombie
        mobColorArray.put(EntitySlime.class, ReikaColorAPI.RGBtoHex(90, 162, 68)); //Slime
        mobColorArray.put(EntityGhast.class, ReikaColorAPI.GStoHex(240)); //Ghast
        mobColorArray.put(
            EntityPigZombie.class, ReikaColorAPI.RGBtoHex(181, 131, 131)
        ); //PigZombie
        mobColorArray.put(
            EntityEnderman.class, ReikaColorAPI.RGBtoHex(204, 15, 248)
        ); //Enderman
        mobColorArray.put(
            EntityCaveSpider.class, ReikaColorAPI.RGBtoHex(18, 77, 90)
        ); //Cave Spider
        mobColorArray.put(
            EntitySilverfish.class, ReikaColorAPI.GStoHex(140)
        ); //Silverfish
        mobColorArray.put(
            EntityBlaze.class, ReikaColorAPI.RGBtoHex(235, 180, 26)
        ); //Blaze
        mobColorArray.put(
            EntityMagmaCube.class, ReikaColorAPI.RGBtoHex(84, 14, 0)
        ); //LavaSlime
        mobColorArray.put(
            EntityDragon.class, ReikaColorAPI.RGBtoHex(224, 121, 250)
        ); //Dragon
        mobColorArray.put(EntityWither.class, ReikaColorAPI.GStoHex(79)); //Wither
        mobColorArray.put(EntityBat.class, ReikaColorAPI.RGBtoHex(118, 100, 61)); //Bat
        mobColorArray.put(
            EntityWitch.class, ReikaColorAPI.RGBtoHex(163, 148, 131)
        ); //Witch

        mobColorArray.put(EntityPig.class, ReikaColorAPI.RGBtoHex(238, 158, 158)); //Pig
        mobColorArray.put(EntitySheep.class, ReikaColorAPI.GStoHex(214)); //Sheep
        mobColorArray.put(EntityCow.class, ReikaColorAPI.RGBtoHex(67, 53, 37)); //Cow
        mobColorArray.put(
            EntityChicken.class, ReikaColorAPI.RGBtoHex(193, 147, 67)
        ); //Chicken
        mobColorArray.put(
            EntitySquid.class, ReikaColorAPI.RGBtoHex(83, 108, 127)
        ); //Squid
        mobColorArray.put(EntityWolf.class, ReikaColorAPI.RGBtoHex(183, 179, 180)); //Wolf
        mobColorArray.put(
            EntityMooshroom.class, ReikaColorAPI.RGBtoHex(151, 3, 4)
        ); //Mooshroom
        mobColorArray.put(
            EntitySnowman.class, ReikaColorAPI.RGBtoHex(226, 143, 34)
        ); //Snow Golem
        mobColorArray.put(
            EntityOcelot.class, ReikaColorAPI.RGBtoHex(242, 197, 110)
        ); //Ocelot
        mobColorArray.put(
            EntityIronGolem.class, ReikaColorAPI.RGBtoHex(208, 185, 168)
        ); //Iron Golem

        mobColorArray.put(
            EntityVillager.class, ReikaColorAPI.RGBtoHex(178, 122, 98)
        ); //Villager

        mobColorArray.put(EntityPlayer.class, 0x0000ff);
        mobColorArray.put(EntityAnimal.class, 0x00a000);
        mobColorArray.put(EntityGolem.class, 0x7f7f7f);
        mobColorArray.put(EntityMob.class, 0xff0000);
        mobColorArray.put(Entity.class, 0xffffff);
    }

    /** Returns true if the mob is a hostile one. Args: EntityLivingBase mob */
    public static boolean isHostile(EntityLivingBase mob) {
        if (mob instanceof EntityIronGolem) {
            EntityLivingBase tgt = ((EntityIronGolem) mob).getAttackTarget();
            return tgt instanceof EntityPlayer
                || (tgt instanceof EntityTameable && ((EntityTameable) tgt).isTamed())
                || tgt instanceof TameHostile;
        }
        return isHostile(mob.getClass());
    }

    public static boolean isHostile(Class<? extends EntityLivingBase> mob) {
        Boolean ret = hostilityMap.get(mob);
        if (ret == null) {
            ret = calcHostility(mob);
            hostilityMap.put(mob, ret);
        }
        return ret.booleanValue();
    }

    private static boolean calcHostility(Class<? extends EntityLivingBase> mob) {
        if (TameHostile.class.isAssignableFrom(mob))
            return false;
        if (EntityMob.class.isAssignableFrom(mob))
            return true;
        if (EntityGhast.class.isAssignableFrom(mob))
            return true;
        if (EntitySlime.class.isAssignableFrom(mob))
            return true;
        if (EntityWitch.class.isAssignableFrom(mob))
            return true;
        if (EntityDragon.class.isAssignableFrom(mob))
            return true;
        if (EntityWither.class.isAssignableFrom(mob))
            return true;
        String n = mob.getName().toLowerCase(Locale.ENGLISH);
        if (n.contains("wisp"))
            return true;
        if (n.contains("pech"))
            return true;
        if (n.contains("botania") && n.contains("doppleganger"))
            return true;
        if (n.contains("tconstruct") && n.contains("blueslime"))
            return true;
        return false;
    }

    /** Converts a string mobname to its respective id. Args: Name */
    public static int mobNameToID(String name) {
        return (int) EntityList.stringToIDMapping.get(name);
    }

    /** Converts a string mobname to its respective class file. Args: Name */
    public static Class mobNameToClass(String name) {
        return (Class) EntityList.stringToClassMapping.get(name);
    }

    /** Returns the number of mob-type entities in MineCraft. Args: World */
    public static int getNumberMobsInMC(World world) {
        int highestid = 0;
        int numberentities = EntityList.IDtoClassMapping.size();
        Object[] entityKeys = EntityList.IDtoClassMapping.keySet().toArray();
        int[] entityIDs = new int[entityKeys.length];
        for (int i = 0; i < entityKeys.length; i++) {
            entityIDs[i] = Integer.valueOf(String.valueOf(entityKeys[i]));
        }
        for (int i = 0; i < entityIDs.length; i++) {
            if (entityIDs[i] > highestid) {
                highestid = entityIDs[i];
            }
        }
        int numbermobs = 0;
        for (int id = 0; id <= highestid; id++) {
            if (EntityList.IDtoClassMapping.containsKey(id) && id != 48
                && id != 49) { //ID 48,49 is "Mob","Monster" -> EntityLivingBase.class,
                               //EntityMob.class
                Entity ent = EntityList.createEntityByID(id, world);
                if (ent instanceof EntityLivingBase)
                    numbermobs++;
            }
        }
        return numbermobs;
    }

    /** Returns the mass (in kg) of the entity. Args: Entity */
    public static double getEntityMass(Entity ent) {
        if (ent instanceof EntityItem || ent instanceof EntityXPOrb)
            return 0.25;
        if (ent instanceof EntityCreeper)
            return 100; //220 lbs; TNT is heavy
        if (ent instanceof EntitySkeleton)
            return 30; //66 lbs
        if (ent instanceof EntityPigZombie)
            return 90;
        if (ent instanceof EntityZombie || ent instanceof EntityPlayer
            || ent instanceof EntityVillager || ent instanceof EntityWitch)
            return 70; // 180 lbs
        if (ent instanceof EntityCaveSpider)
            return 30;
        if (ent instanceof EntitySpider)
            return 60; //
        if (ent instanceof EntityPig)
            return 100;
        if (ent instanceof EntityCow || ent instanceof EntityMooshroom)
            return 350;
        if (ent instanceof EntityGhast)
            return 20; //spirit creature
        if (ent instanceof EntityBlaze)
            return 300;
        if (ent instanceof EntityMagmaCube) {
            EntityMagmaCube cube = (EntityMagmaCube) ent;
            return 400 * cube.getSlimeSize() * cube.getSlimeSize();
        }
        if (ent instanceof EntitySlime) {
            EntitySlime cube = (EntitySlime) ent;
            return 200 * cube.getSlimeSize() * cube.getSlimeSize();
        }
        if (ent instanceof EntityEnderman)
            return 40;
        if (ent instanceof EntitySilverfish)
            return 1;
        if (ent instanceof EntityChicken)
            return 2;
        if (ent instanceof EntityDragon)
            return 10000; //really conjectural
        if (ent instanceof EntityWither)
            return 3000; //even more conjectural
        if (ent instanceof EntityWolf)
            return 50;
        if (ent instanceof EntityOcelot)
            return 15;
        if (ent instanceof EntityIronGolem)
            return 32000; //iron = 8g/cc, 4m^3 of it
        if (ent instanceof EntityGolem)
            return 100;
        if (ent instanceof EntitySheep)
            return 150;
        if (ent instanceof EntitySquid)
            return 120;
        if (ent instanceof EntityBat)
            return 0.5;
        if (ent instanceof EntityMinecart)
            return 400;
        if (ent instanceof EntityBoat)
            return 70;
        if (ent instanceof EntityTNTPrimed)
            return 2700; //2.7 g/cc
        if (ent instanceof EntityFallingBlock)
            return ReikaPhysicsHelper.getBlockDensity(
                ((EntityFallingBlock) ent).func_145805_f()
            ); //2 g/cc
        return 100;
    }

    /** Returns an itemstack (size 1 item) of the entity's breeding Items. Args: Entity */
    public static ItemStack getBreedItem(EntityAnimal e) {
        if (e instanceof EntitySheep) {
            return new ItemStack(Items.wheat);
        } else if (e instanceof EntityCow) {
            return new ItemStack(Items.wheat);
        } else if (e instanceof EntityPig) {
            return new ItemStack(Items.carrot);
        } else if (e instanceof EntityChicken) {
            return new ItemStack(Items.wheat_seeds);
        } else if (e instanceof EntityWolf) {
            return new ItemStack(Items.porkchop);
        } else if (e instanceof EntityOcelot) {
            return new ItemStack(Items.fish);
        }
        return null;
    }

    /**
     * Returns true if the given pitch falls within the given creature's hearing range.
     */
    public static boolean isHearingRange(long freq, EntityLivingBase ent) {
        if (ent instanceof EntityPlayer || ent instanceof EntityWitch
            || ent instanceof EntityZombie) {
            if (freq < 20)
                return false;
            if (freq > 20000)
                return false;
        }
        if (ent instanceof EntitySlime)
            return false; //deaf
        if (ent instanceof EntityZombie || ent instanceof EntitySkeleton) {
            if (freq < 20)
                return false;
            if (freq > 5000) //high-frequency hearing loss
                return false;
        }
        if (ent instanceof EntitySpider) {
            if (freq < 1000)
                return false;
            if (freq > 100000)
                return false;
        }
        if (ent instanceof EntityCreeper) {
            if (freq < 500)
                return false;
            if (freq > 40000)
                return false;
        }
        if (ent instanceof EntityGhast) {
            if (freq < 200)
                return false;
            if (freq > 10000)
                return false;
        }
        if (ent instanceof EntityPigZombie) { //Overlap of pig and zombie hearing ranges
            if (freq < 64)
                return false;
            if (freq > 5000)
                return false;
        }
        if (ent instanceof EntityEnderman) {
            if (freq < 5)
                return false;
            if (freq > 2000)
                return false;
        }
        if (ent instanceof EntityBlaze) {
            if (freq > 500)
                return false;
        }
        if (ent instanceof EntitySilverfish) {
            if (freq < 1000)
                return false;
            if (freq > 35000)
                return false;
        }
        if (ent instanceof EntityDragon) {
            if (freq < 5)
                return false;
            if (freq > 8000)
                return false;
        }
        if (ent instanceof EntityWither) {
            if (freq < 2)
                return false;
            if (freq > 10000)
                return false;
        }
        if (ent instanceof EntityPig) {
            if (freq < 64)
                return false;
            if (freq > 32000)
                return false;
        }
        if (ent instanceof EntityCow || ent instanceof EntityMooshroom) {
            if (freq < 23)
                return false;
            if (freq > 35000)
                return false;
        }
        if (ent instanceof EntityChicken) {
            if (freq < 125)
                return false;
            if (freq > 2000)
                return false;
        }
        if (ent instanceof EntitySheep) {
            if (freq < 100)
                return false;
            if (freq > 30000)
                return false;
        }
        if (ent instanceof EntityBat) {
            if (freq < 2000)
                return false;
            if (freq > 110000)
                return false;
        }
        if (ent instanceof EntityOcelot) {
            if (freq < 45)
                return false;
            if (freq > 64000)
                return false;
        }
        if (ent instanceof EntityWolf) {
            if (freq < 67)
                return false;
            if (freq > 45000)
                return false;
        }
        if (ent instanceof EntitySquid)
            if (freq > 500)
                return false;
        return true;
    }

    /** Knocks one entity away from another. Args: Attacker, target, power */
    public static void knockbackEntity(Entity a, Entity b, double power) {
        knockbackEntityFromPos(a.posX, a.posY, a.posZ, b, power);
    }

    /** Knocks one entity away from another. Args: Attacker, target, power, ypowerscale */
    public static void knockbackEntity(Entity a, Entity b, double power, double yscale) {
        knockbackEntityFromPos(a.posX, a.posY, a.posZ, b, power, 0, yscale);
    }

    public static void
    knockbackEntityFromPos(double x, double y, double z, Entity ent, double power) {
        knockbackEntityFromPos(x, y, z, ent, power, 0);
    }

    /**
     * Knocks an entity away from a position. Args: x, y, z, entity, power, distance
     * scale exponent
     */
    public static void knockbackEntityFromPos(
        double x, double y, double z, Entity ent, double power, double distanceScale
    ) {
        knockbackEntityFromPos(x, y, z, ent, power, distanceScale, 1);
    }

    /**
     * Knocks an entity away from a position. Args: x, y, z, entity, power, distance
     * scale exponent, y scalar
     */
    public static void knockbackEntityFromPos(
        double x,
        double y,
        double z,
        Entity ent,
        double power,
        double distanceScale,
        double yscale
    ) {
        double dx = x - ent.posX;
        //double dy = y-ent.posY;
        double dz = z - ent.posZ;
        double dd = ReikaMathLibrary.py3d(dx, 0, dz);
        if (distanceScale > 0) {
            power /= Math.pow(dd, distanceScale);
        }
        ent.motionX -= dx / dd / 2 * power;
        ent.motionZ -= dz / dd / 2 * power;
        //ent.motionY -= dy/10;
        if (ent.onGround || ent.posY > y)
            ent.motionY += 0.4 * power;
        //if (!ent.worldObj.isRemote)
        ent.velocityChanged = true;
    }

    /**
     * Returns true if all EntityLivingBase within the list are dead. Args: List
     * [The list MUST be of EntityLivingBase (or subclass) - any other type WILL cause
     * a classcast exception!], test isDead only yes/no
     */
    public static boolean allAreDead(List mobs, boolean isDeadOnly) {
        for (int i = 0; i < mobs.size(); i++) {
            EntityLivingBase ent = (EntityLivingBase) mobs.get(i);
            if ((!ent.isDead && ent.getHealth() > 0) || (!ent.isDead && isDeadOnly))
                return false;
        }
        return true;
    }

    /**
     * Adds a small velocity in a random direction (akin to items' speeds when dropped)
     */
    public static void addRandomDirVelocity(Entity ent, double max) { /*
         ent.motionX = -max+2*max*rand.nextFloat();
         ent.motionZ = -max+2*max*rand.nextFloat();
         ent.motionY = 4*max*rand.nextFloat();*/
    }

    /** Drop an entity's head. Args: EntityLivingBase */
    public static void dropHead(EntityLivingBase e) {
        if (e == null)
            return;
        ItemStack is = null;
        if (e instanceof EntitySkeleton) {
            EntitySkeleton ek = (EntitySkeleton) e;
            if (ek.getSkeletonType() == 1) //Wither Skeleton
                is = new ItemStack(Items.skull, 1, 1);
            else
                is = new ItemStack(Items.skull, 1, 0);
        }
        if (e instanceof EntityZombie) {
            if (!(((EntityZombie) e).isVillager() || e instanceof EntityPigZombie))
                is = new ItemStack(Items.skull, 1, 2);
        }
        if (e instanceof EntityPlayer)
            is = new ItemStack(Items.skull, 1, 3);
        if (e instanceof EntityCreeper)
            is = new ItemStack(Items.skull, 1, 4);
        if (is == null)
            return;
        ReikaItemHelper.dropItem(e.worldObj, e.posX, e.posY + 0.2, e.posZ, is);
    }

    public static ItemStack getFoodItem(EntityLivingBase e) {
        if (e instanceof EntityCow) {
            return new ItemStack(Items.beef);
        } else if (e instanceof EntityPig) {
            return new ItemStack(Items.porkchop);
        } else if (e instanceof EntityChicken) {
            return new ItemStack(Items.chicken);
        } else if (e instanceof EntitySheep) {
            if (ModList.DARTCRAFT.isLoaded()) {
                Item id = DartItemHandler.getInstance().meatID;
                if (id != null)
                    return new ItemStack(id, 1, 0);
            }
        } else if (e instanceof EntityZombie) {
            return new ItemStack(Items.rotten_flesh);
        } else if (e instanceof EntityHorse) {
            return new ItemStack(Items.beef);
        }
        return null;
    }

    /**
     * Spawns a bunch of particles around an entity. Args: Particle Type, Entity, number
     * of particles
     */
    public static void spawnParticlesAround(String part, Entity e, int num) {
        for (int k = 0; k < num; k++)
            e.worldObj.spawnParticle(
                part,
                e.posX - 0.6 + 1.2 * rand.nextDouble(),
                e.posY + e.height / 2 - 0.6 + 1.2 * rand.nextDouble(),
                e.posZ - 0.6 + 1.2 * rand.nextDouble(),
                -0.2 + 0.4 * rand.nextDouble(),
                0.4 * rand.nextDouble(),
                -0.2 + 0.4 * rand.nextDouble()
            );
    }

    /** Returns the Entity ID from entity class. Args: Entity Class */
    public static int getEntityIDByClass(Class cl) {
        String name = (String) EntityList.classToStringMapping.get(cl);
        return mobNameToID(name);
    }

    public static int getEntityID(Entity e) {
        if (e instanceof EntityPlayer) {
            return -1;
        }
        return EntityList.getEntityID(e);
    }

    public static boolean burnsInSun(EntityLivingBase e) {
        return e.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    public static boolean
    isEntityWearingFullSuitOf(EntityLivingBase e, ArmorMaterial type) {
        return isEntityWearingFullSuitOf(
            e,
            (ItemStack is)
                -> is.getItem() instanceof ItemArmor
                && ((ItemArmor) is.getItem()).getArmorMaterial() == type
        );
    }

    public static boolean
    isEntityWearingFullSuitOf(EntityLivingBase e, Function<ItemStack, Boolean> func) {
        for (int i = 1; i <= 4; i++) {
            ItemStack is = e.getEquipmentInSlot(i);
            if (is == null || !func.apply(is))
                return false;
        }
        return true;
    }

    public static Render getEntityRenderer(Class entityClass) {
        return (Render) RenderManager.instance.entityRenderMap.get(entityClass);
    }

    public static void setNoPotionParticles(EntityLivingBase e) {
        e.getDataWatcher().updateObject(7, 0);
    }

    public static void setInvulnerable(Entity e, boolean invuln) {
        NBTTagCompound NBT = new NBTTagCompound();
        e.writeToNBT(NBT);
        NBT.setBoolean("Invulnerable", invuln);
        e.readFromNBT(NBT);
    }

    public static boolean isLivingMob(String mob, boolean allowPlayers) {
        Class c = allowPlayers ? EntityLivingBase.class : EntityLiving.class;
        return c.isAssignableFrom((Class) EntityList.stringToClassMapping.get(mob));
    }

    public static boolean hasID(String mob) {
        return EntityList.stringToIDMapping.containsKey(mob);
    }

    public static boolean hasID(Class<? extends Entity> c) {
        return EntityList.classToIDMapping.containsKey(c);
    }

    public static String getEntityDisplayName(String name) {
        String ret = StatCollector.translateToLocal("entity." + name + ".name");
        ret = ret.startsWith("entity.") ? StatCollector.translateToLocal(name) : ret;
        EntityRegistration reg = EntityRegistry.instance().lookupModSpawn(
            (Class<? extends Entity>) EntityList.stringToClassMapping.get(name), true
        );
        if (reg != null && reg.getContainer() != null) {
            ret = ret.replace(reg.getContainer().getModId() + ".", "");
        }
        return ret;
    }

    public static boolean isTameHostile(String mob) {
        return TameHostile.class.isAssignableFrom((Class
        ) EntityList.stringToClassMapping.get(mob));
    }

    /*
    @Deprecated //Too easy to #%$^ up
    public static void overrideEntity(Class mobClass, String name, int entityID) {
        EntityEggInfo info = (EntityEggInfo)EntityList.entityEggs.get(entityID);
        removeEntityMapping(mobClass, name, entityID);
        EntityList.addMapping(mobClass, name, entityID);
        if (info != null) {
            EntityList.entityEggs.put(entityID, info);
        }
    }

    @Deprecated //Too easy to #%$^ up
    public static void removeEntityFromRegistry(Class c) {
        String s = (String)EntityList.classToStringMapping.remove(c);
        int id = (int)EntityList.stringToIDMapping.remove(s);
        EntityList.stringToClassMapping.remove(s);
        EntityList.IDtoClassMapping.remove(id);
        EntityList.classToIDMapping.remove(c);
    }

    @Deprecated //Too easy to #%$^ up
    public static void overrideEntityAtSameID(Class oldClass, Class newClass) {
        DragonAPICore.debug("Preparing to override Entity class "+oldClass+" with
    "+newClass); String s = (String)EntityList.classToStringMapping.remove(oldClass); int
    id = (int)EntityList.stringToIDMapping.remove(s); DragonAPICore.debug("Found ID String
    '"+s+"' and numerical ID "+id);

        EntityList.stringToClassMapping.remove(s);
        EntityList.IDtoClassMapping.remove(id);
        EntityList.classToIDMapping.remove(oldClass);

        EntityList.addMapping(newClass, s, id);
        DragonAPICore.debug("ID Mapping added. Getters:
    "+EntityList.classToStringMapping.get(newClass)+" &
    "+EntityList.classToIDMapping.get(newClass));
    }

    @Deprecated //Too easy to #%$^ up
    private static void removeEntityMapping(Class mobClass, String name, int entityID) {
        EntityList.stringToClassMapping.remove(name);
        EntityList.entityEggs.remove(entityID);
        EntityList.IDtoClassMapping.remove(entityID);
    }
     */
    private static EntityCreeper dummy;

    public static EntityLivingBase
    getDummyMob(World world, double x, double y, double z) {
        if (dummy == null)
            dummy = new EntityCreeper(world);
        dummy.setLocationAndAngles(x, y, z, 0, 0);
        return dummy;
    }

    public static void
    sortEntityListByDistance(List<Entity> li, double x, double y, double z) {
        Collections.sort(li, new EntityDistanceComparator(x, y, z));
    }

    public static final class EntityDistanceComparator implements Comparator<Entity> {
        public final double posX;
        public final double posY;
        public final double posZ;

        public EntityDistanceComparator(double x, double y, double z) {
            posX = x;
            posY = y;
            posZ = z;
        }

        @Override
        public int compare(Entity e1, Entity e2) {
            double dd1
                = ReikaMathLibrary.py3d(e1.posX - posX, e1.posY - posY, e1.posZ - posZ);
            double dd2
                = ReikaMathLibrary.py3d(e2.posX - posX, e2.posY - posY, e2.posZ - posZ);
            return dd1 > dd2 ? 1 : dd1 < dd2 ? -1 : 0;
        }
    }

    public static boolean isBossMob(EntityLivingBase e) {
        if (e instanceof EntityWither || e instanceof EntityDragon)
            return true;
        String name = e.getClass().getName().toLowerCase(Locale.ENGLISH);
        if (name.contains("voidmonster"))
            return false;
        if (name.startsWith("twilightforest.entity.boss"))
            return true;
        return false;
    }

    public static void transferEntityToDimension(Entity e, int to_dim) {
        transferEntityToDimension(e, to_dim, null);
    }

    public static void transferEntityToDimension(Entity e, int to_dim, Teleporter t) {
        if (!e.isDead) {
            if (e instanceof EntityPlayerMP) {
                transferPlayerToDimension((EntityPlayerMP) e, to_dim, t);
            } else {
                if (!e.worldObj.isRemote) {
                    e.worldObj.theProfiler.startSection("changeDimension");
                    MinecraftServer ms = MinecraftServer.getServer();
                    int from_dim = e.dimension;
                    WorldServer from = ms.worldServerForDimension(from_dim);
                    WorldServer to = ms.worldServerForDimension(to_dim);
                    e.dimension = to_dim;
                    e.worldObj.removeEntity(e);
                    e.isDead = false;
                    e.worldObj.theProfiler.startSection("reposition");
                    ms.getConfigurationManager().transferEntityToWorld(
                        e, from_dim, from, to, t != null ? t : new DummyTeleporter(to)
                    );
                    /*
                    e.worldObj.theProfiler.endStartSection("reloading");
                    Entity copy =
                    EntityList.createEntityByName(EntityList.getEntityString(e), to);

                    if (copy != null) {
                        copy.copyDataFrom(e, true);
                        to.spawnEntityInWorld(copy);
                    }

                    e.isDead = true;
                     */
                    e.worldObj.theProfiler.endSection();
                    from.resetUpdateEntityTick();
                    to.resetUpdateEntityTick();
                    e.worldObj.theProfiler.endSection();
                } else {
                    e.dimension = to_dim;
                }
            }
        }
    }

    private static void
    transferPlayerToDimension(EntityPlayerMP ep, int to_dim, Teleporter t) {
        MinecraftServer mcs = ep.mcServer;
        if (ep.timeUntilPortal > 0)
            ep.timeUntilPortal = 10;
        mcs.getConfigurationManager().transferPlayerToDimension(
            ep,
            to_dim,
            t != null ? t : new DummyTeleporter(mcs.worldServerForDimension(to_dim))
        );
    }

    public static <T extends Entity> T getNearestEntityOfSameType(T e, int r) {
        return (T) e.worldObj.findNearestEntityWithinAABB(
            e.getClass(), ReikaAABBHelper.getEntityCenteredAABB(e, 24), e
        );
    }

    public static void decrEntityItemStack(EntityItem ei, int rem) {
        ItemStack is = ei.getEntityItem();
        is.stackSize -= rem;
        if (is.stackSize <= 0)
            ei.setDead();
    }

    public static void clearEntities(World world, IEntitySelector sel) {
        for (Entity e : ((List<Entity>) world.loadedEntityList)) {
            if (sel == null || sel.isEntityApplicable(e)) {
                e.setDead();
            }
        }
    }

    /** Lower priority value == higher actual priority */
    public static void addAITask(EntityLiving e, EntityAIBase task, int priority) {
        if (!hasAITask(e, task)) {
            e.tasks.addTask(priority, task);
        }
    }

    public static boolean hasAITask(EntityLiving e, EntityAIBase task) {
        for (EntityAITaskEntry in : ((List<EntityAITaskEntry>) e.tasks.taskEntries)) {
            if (in.action == task || in.action.equals(task)) {
                return true;
            }
            if (in instanceof ComparableAI && task instanceof ComparableAI) {
                if (((ComparableAI) in).match((ComparableAI) task))
                    return true;
                if (((ComparableAI) task).match((ComparableAI) in))
                    return true;
            }
        }
        return false;
    }

    public static void seamlessTeleport(
        Entity e,
        int x1,
        int y1,
        int z1,
        int x2,
        int y2,
        int z2,
        ForgeDirection from,
        ForgeDirection to
    ) {
        if (e.worldObj.isRemote)
            return;

        double dx = e.posX - x1 - 0.5;
        double dy = e.posY - y1 - 0.5;
        double dz = e.posZ - z1 - 0.5;
        float yaw = getDYaw(e, from, to);

        Vec3 vec = Vec3.createVectorHelper(dx, dy, dz);
        vec = ReikaVectorHelper.rotateVector(vec, 0, yaw, 0);

        double nx = x2 + vec.xCoord + 0.5;
        double ny = y2 + vec.yCoord + 0.5;
        double nz = z2 + vec.zCoord + 0.5;

        Vec3 vvec = Vec3.createVectorHelper(e.motionX, e.motionY, e.motionZ);
        vvec = ReikaVectorHelper.rotateVector(vvec, 0, yaw, 0);
        e.motionX = vvec.xCoord;
        e.motionY = vvec.yCoord;
        e.motionZ = vvec.zCoord;

        if (e instanceof EntityPlayer) {
            e.rotationYaw += yaw;
            ((EntityPlayer) e).rotationYawHead += yaw;
            ((EntityPlayer) e).prevRotationYawHead += yaw;
            e.prevRotationYaw += yaw;
            ((EntityPlayer) e).setPositionAndUpdate(nx, ny, nz);
            byte a = (byte) (MathHelper.floor_float(
                ((EntityPlayer) e).rotationYawHead * 256.0F / 360.0F
            ));
            ((EntityPlayerMP) e)
                .playerNetServerHandler.sendPacket(new S19PacketEntityHeadLook(e, a));
        } else {
            e.setLocationAndAngles(nx, ny, nz, e.rotationYaw + yaw, e.rotationPitch);
        }
    }

    private static float getDYaw(Entity e, ForgeDirection from, ForgeDirection to) {
        int rel = ReikaDirectionHelper.getRelativeAngle(from, to);
        if (rel > 180)
            rel = rel - 360;
        return rel;
    }

    /**
     * Gets a direction from an entity's look direction. Args: Entity, allow vertical
     * yes/no
     */
    public static ForgeDirection
    getDirectionFromEntityLook(EntityLivingBase e, boolean vertical) {
        if (MathHelper.abs(e.rotationPitch) < 60 || !vertical) {
            int i = MathHelper.floor_double((e.rotationYaw * 4F) / 360F + 0.5D);
            while (i > 3)
                i -= 4;
            while (i < 0)
                i += 4;
            switch (i) {
                case 0:
                    return ForgeDirection.SOUTH;
                case 1:
                    return ForgeDirection.WEST;
                case 2:
                    return ForgeDirection.NORTH;
                case 3:
                    return ForgeDirection.EAST;
            }
        } else { //Looking up/down
            if (e.rotationPitch > 0)
                return ForgeDirection.DOWN; //set to up
            else
                return ForgeDirection.UP; //set to down
        }
        return ForgeDirection.UNKNOWN;
    }

    public static Class<? extends EntityLivingBase>
    getEntityCategoryClass(EntityLivingBase e) {
        if (e instanceof EntityPlayer)
            return EntityPlayer.class;
        //else if (e instanceof IBossDisplayData)
        //	return IBossDisplayData.class;
        else if (e instanceof EntityEnderman || e instanceof EntityPigZombie)
            return e.getClass();
        else if (e instanceof EntitySlime)
            return EntitySlime.class;
        else if (e instanceof EntityMob)
            return EntityMob.class;
        else if (e instanceof EntityAnimal)
            return EntityAnimal.class;
        else if (e instanceof EntityFlying)
            return EntityFlying.class;
        else
            return e.getClass();
    }

    public static class WrappedDamageSource extends DamageSource {
        private final DamageSource wrapped;
        public final EntityPlayer player;

        public WrappedDamageSource(DamageSource src, EntityPlayer ep) {
            super(src.damageType);
            wrapped = src;
            player = ep;
        }

        @Override
        public boolean isProjectile() {
            return wrapped.isProjectile();
        }

        @Override
        public boolean isExplosion() {
            return wrapped.isExplosion();
        }

        @Override
        public boolean isUnblockable() {
            return wrapped.isUnblockable();
        }

        @Override
        public float getHungerDamage() {
            return wrapped.getHungerDamage();
        }

        @Override
        public boolean canHarmInCreative() {
            return wrapped.canHarmInCreative();
        }

        @Override
        public boolean isDamageAbsolute() {
            return wrapped.isDamageAbsolute();
        }

        @Override
        public Entity getEntity() {
            return player;
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase tg) {
            return wrapped.func_151519_b(tg);
        }

        @Override
        public boolean isFireDamage() {
            return wrapped.isFireDamage();
        }

        @Override
        public String getDamageType() {
            return wrapped.damageType;
        }

        @Override
        public boolean isDifficultyScaled() {
            return wrapped.isDifficultyScaled();
        }

        @Override
        public boolean isMagicDamage() {
            return wrapped.isMagicDamage();
        }
    }

    public static Entity getShootingEntity(Entity e) {
        if (e instanceof EntityThrowable) {
            return ((EntityThrowable) e).getThrower();
        } else if (e instanceof EntityFireball) {
            return ((EntityFireball) e).shootingEntity;
        } else if (e instanceof EntityArrow) {
            return ((EntityArrow) e).shootingEntity;
        } else if (e instanceof CustomProjectile) {
            return ((CustomProjectile) e).getFiringEntity();
        } else if (ModList.BLOODMAGIC.isLoaded() && InterfaceCache.SPELLSHOT.instanceOf(e)) {
            return ((EntitySpellProjectile) e).shootingEntity;
        }
        return null;
    }

    public static boolean canEntitySeeTheSky(Entity e) {
        return e.worldObj.canBlockSeeTheSky(
            MathHelper.floor_double(e.posX),
            MathHelper.floor_double(e.posY),
            MathHelper.floor_double(e.posZ)
        );
    }

    public static boolean isNearSkylight(Entity e) {
        return getSkyLightAt(e) > 0;
    }

    public static int getSkyLightAt(Entity e) {
        return e.worldObj.getSavedLightValue(
            EnumSkyBlock.Sky,
            MathHelper.floor_double(e.posX),
            MathHelper.floor_double(e.posY + e.height / 2),
            MathHelper.floor_double(e.posZ)
        );
    }

    public static boolean isEntityWearingPoweredArmor(EntityLivingBase e) {
        for (int i = 1; i <= 4; i++) {
            ItemStack is = e.getEquipmentInSlot(i);
            if (is != null) {
                if (InterfaceCache.GASITEM.instanceOf(is.getItem()))
                    return true;
                if (InterfaceCache.RFENERGYITEM.instanceOf(is.getItem()))
                    return true;
                if (InterfaceCache.ENERGYITEM.instanceOf(is.getItem()))
                    return true;
                if (InterfaceCache.IELECTRICITEM.instanceOf(is.getItem()))
                    return true;
                if (InterfaceCache.MUSEELECTRICITEM.instanceOf(is.getItem()))
                    return true;
            }
        }
        return false;
    }

    public static double getCarriedMass(EntityLivingBase e) {
        double mass = 0;
        for (int i = 0; i <= 4; i++) {
            mass += ReikaItemHelper.getItemMass(e.getEquipmentInSlot(i));
        }
        return mass;
    }

    public static EntityXPOrb mergeXPOrbs(World world, Collection<EntityXPOrb> c) {
        double x = 0;
        double y = 0;
        double z = 0;
        double vx = 0;
        double vy = 0;
        double vz = 0;
        int n = 0;
        int value = 0;
        int age = Integer.MAX_VALUE;
        for (EntityXPOrb e : c) {
            if (e.isDead || e.xpValue <= 0 || e.xpOrbAge < 2)
                continue;
            e.setDead();
            x += e.posX;
            y += e.posY;
            z += e.posZ;
            vx += e.motionX;
            vy += e.motionY;
            vz += e.motionZ;
            n++;
            value += e.xpValue;
            age = Math.min(age, e.xpOrbAge);
            e.xpValue = 0;
        }
        if (n > 0 && value > 0) {
            x /= n;
            y /= n;
            z /= n;
            vx /= n;
            vy /= n;
            vz /= n;
            EntityXPOrb xp = new EntityXPOrb(world, x, y, z, value);
            xp.motionX = vx;
            xp.motionY = vy;
            xp.motionZ = vz;
            xp.xpOrbAge = age;
            xp.velocityChanged = true;
            if (!world.isRemote) {
                //ReikaJavaLibrary.pConsole("Collated "+n+" from "+c.size()+" V="+value);
                world.spawnEntityInWorld(xp);
            }
            return xp;
        }
        return null;
    }

    public static EntityXPOrb mergeXPOrbs(EntityXPOrb e1, EntityXPOrb e2) {
        if (e1.isDead)
            return e2;
        if (e2.isDead)
            return e1;
        e1.setDead();
        e2.setDead();
        double x = (e1.posX + e2.posX) / 2;
        double y = (e1.posY + e2.posY) / 2;
        double z = (e1.posZ + e2.posZ) / 2;
        EntityXPOrb xp = new EntityXPOrb(e1.worldObj, x, y, z, e1.xpValue + e2.xpValue);
        xp.xpOrbAge = Math.min(e1.xpOrbAge, e2.xpOrbAge);
        if (!e1.worldObj.isRemote) {
            e1.worldObj.spawnEntityInWorld(xp);
        }
        return xp;
    }

    public static int damageArmor(EntityLivingBase e, int amt) {
        return damageArmor(e, amt, null);
    }

    public static int damageArmor(
        EntityLivingBase e, int amt, BiFunction<ItemStack, Integer, Integer> handle
    ) {
        return damageArmor(e, amt, (s -> true), handle);
    }

    public static int damageArmor(EntityLivingBase e, int amt, int slot) {
        return damageArmor(e, amt, (s -> s == slot), null);
    }

    public static int damageArmor(
        EntityLivingBase e,
        int amt,
        Function<Integer, Boolean> apply,
        BiFunction<ItemStack, Integer, Integer> handle
    ) {
        int ret = 0;
        for (int i = 1; i <= 4; i++) {
            ret += damageArmorItem(e, i, amt, handle);
        }
        return ret;
    }

    private static int damageArmorItem(
        EntityLivingBase e,
        int slot,
        int amt,
        BiFunction<ItemStack, Integer, Integer> handle
    ) {
        ItemStack arm = e.getEquipmentInSlot(slot);
        if (arm != null && canDamageArmorOf(e)) {
            ItemStack pre = arm.copy();
            int ret = 0;
            if (handle != null) {
                Integer get = handle.apply(arm, amt);
                if (get != null) {
                    ret += get.intValue();
                }
            }
            Item item = arm.getItem();
            if (InterfaceCache.MUSEELECTRICITEM.instanceOf(item)) {
                IMuseElectricItem ms = (IMuseElectricItem) item;
                ret += ms.extractEnergy(arm, amt * 300, false);
            } else if (InterfaceCache.RFENERGYITEM.instanceOf(item)) {
                IEnergyContainerItem ie = (IEnergyContainerItem) item;
                ret += ie.extractEnergy(arm, amt * 300, false);
            } else if (InterfaceCache.IELECTRICITEM.instanceOf(item)) {
                ret += ElectricItem.manager.discharge(
                    arm, amt * 250, Integer.MAX_VALUE, true, false, false
                );
            } else if (InterfaceCache.GASITEM.instanceOf(item)) {
                IGasItem ie = (IGasItem) item;
                GasStack gas = ie.getGas(arm);
                if (gas != null && gas.amount > 0)
                    gas = ie.removeGas(arm, Math.max(amt, gas.amount * amt / 400));
                ret += gas != null ? gas.amount : 0;
            } else if (item instanceof UnbreakableArmor && !((UnbreakableArmor) item).canBeDamaged()) {
                //do nothing
            } else {
                arm.damageItem(amt, e);
                if (arm.getItemDamage() > arm.getMaxDamage() || arm.stackSize <= 0) {
                    arm = null;
                    e.setCurrentItemOrArmor(slot, null);
                }
                e.playSound("random.break", 0.1F, 0.8F);
                ret += amt;
            }
            ItemStack post = e.getEquipmentInSlot(slot);
            return ItemStack.areItemStacksEqual(pre, post) ? 0 : ret;
        }
        return 0;
    }

    private static boolean canDamageArmorOf(EntityLivingBase target) {
        MinecraftServer ms = MinecraftServer.getServer();
        return target instanceof EntityPlayer ? ms != null && ms.isPVPEnabled() : true;
    }

    public static boolean existsAnotherEntityWithin(Entity e, double dist) {
        AxisAlignedBB box = ReikaAABBHelper.getEntityCenteredAABB(e, dist);
        return e.worldObj
                   .selectEntitiesWithinAABB(e.getClass(), box, new NotSelfSelector(e))
                   .size()
            > 0;
    }

    public static boolean
    existsAnotherValidEntityWithin(Entity e, double dist, IEntitySelector check) {
        AxisAlignedBB box = ReikaAABBHelper.getEntityCenteredAABB(e, dist);
        return e.worldObj
                   .selectEntitiesWithinAABB(
                       e.getClass(),
                       box,
                       combineEntitySelectors(true, new NotSelfSelector(e), check)
                   )
                   .size()
            > 0;
    }

    public static IEntitySelector
    combineEntitySelectors(boolean and, IEntitySelector... s) {
        CombinedEntitySelector ret
            = and ? new CombinedEntitySelectorAND() : new CombinedEntitySelectorOR();
        for (int i = 0; i < s.length; i++) {
            ret.addSelector(s[i]);
        }
        return ret;
    }

    public static boolean
    doSetHealthDamage(EntityLivingBase e, DamageSource src, float amt) {
        if (amt >= e.getHealth()) { //kill
            e.setHealth(0.1F);
            e.hurtResistantTime = 0;
            e.attackEntityFrom(
                src, Integer.MAX_VALUE
            ); //some mods stop damage less than this
            return true;
        } else {
            e.setHealth(e.getHealth() - amt);
            return false;
        }
    }

    public static boolean isInWorld(Entity e) {
        return e.worldObj != null && e.worldObj.getEntityByID(e.getEntityId()) == e;
    }

    public static boolean isInRain(Entity e) {
        return e.worldObj.canLightningStrikeAt(
                   MathHelper.floor_double(e.posX),
                   MathHelper.floor_double(e.posY),
                   MathHelper.floor_double(e.posZ)
               )
            || e.worldObj.canLightningStrikeAt(
                MathHelper.floor_double(e.posX),
                MathHelper.floor_double(e.posY + e.height),
                MathHelper.floor_double(e.posZ)
            );
    }

    public static void chargeCreeper(EntityCreeper e) {
        e.getDataWatcher().updateObject(17, (byte) 1);
    }

    @SideOnly(Side.CLIENT)
    public static void verifyClientEntity(Entity e) {
        //ReikaJavaLibrary.pConsole("Verifying existence of "+e+" on side
        //"+FMLCommonHandler.instance().getEffectiveSide());
        int id = e.getEntityId();
        int dim = e.worldObj.provider.dimensionId;
        int cl = e.getClass().getName().hashCode();
        ReikaPacketHelper.sendDataPacket(
            DragonAPIInit.packetChannel,
            PacketIDs.ENTITYVERIFY.ordinal(),
            PacketTarget.server,
            id,
            dim,
            cl
        );
    }

    public static void
    performEntityVerification(EntityPlayerMP ep, int entityID, int dim, int classHash) {
        //ReikaJavaLibrary.pConsole("Verifying existence of "+entityID+" on side
        //"+FMLCommonHandler.instance().getEffectiveSide());
        World world = DimensionManager.getWorld(dim);
        if (world != null) {
            Entity e = world.getEntityByID(entityID);
            if (e != null) {
                if (e.getClass().getName().hashCode() == classHash) {
                    //ReikaJavaLibrary.pConsole("Verified existence of "+e+" on side
                    //"+FMLCommonHandler.instance().getEffectiveSide());
                    return;
                }
            }
        }
        ReikaPacketHelper.sendDataPacket(
            DragonAPIInit.packetChannel,
            PacketIDs.ENTITYVERIFYFAIL.ordinal(),
            ep,
            entityID,
            dim
        );
        //ReikaJavaLibrary.pConsole("Verified NON-existence of "+entityID+" on side
        //"+FMLCommonHandler.instance().getEffectiveSide());
    }

    public static boolean isLookingAt(EntityLivingBase e, Entity tg) {
        return isLookingAt(e, tg.posX, tg.posY + tg.height / 2, tg.posZ);
    }

    public static boolean isLookingAt(EntityLivingBase e, double x, double y, double z) {
        double dx = x - e.posX;
        double dy = y - e.posY;
        double dz = z - e.posZ;

        float yaw = e.rotationYawHead % 360;
        float pitch = e.rotationPitch + 90;
        if (yaw < 0)
            yaw += 360;

        double dl = ReikaMathLibrary.py3d(dx, 0, dz);
        double arel = -Math.toDegrees(Math.atan2(dx, dz));
        double prel = 90 - Math.toDegrees(Math.atan2(dy, dl));
        if (arel < 0)
            arel += 360;

        double phi = arel - yaw;
        double theta = prel - pitch;
        return Math.abs(phi) < 50 && Math.abs(theta) < 35;
    }

    public static boolean isSolidEntity(Entity e) {
        if (e instanceof EtherealEntity)
            return false;
        String name = e.getClass().getSimpleName();
        if (name.equalsIgnoreCase("EntityTFMobileFirefly"))
            return false;
        if (name.equalsIgnoreCase("EntityWisp"))
            return false;
        if (ModList.FORESTRY.isLoaded() && ReikaBeeHelper.isButterfly(e))
            return false;
        return true;
    }

    public static void playAggroSound(EntityCreature ec) {
        if (ec instanceof EntityEnderman) {
            ec.playSound("mob.endermen.scream", 0.6F, 1);
        } else if (ec instanceof EntityPigZombie) {
            ec.playSound("mob.zombiepig.zpigangry", 1, 1);
        } else {
            playHurtSound(ec);
        }
    }

    public static void playHurtSound(EntityLivingBase e) {
        try {
            String s = (String) ReikaObfuscationHelper.invoke("getHurtSound", e);
            e.playSound(s, 1, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getCreeperFuse(EntityCreeper e) {
        NBTTagCompound tag = new NBTTagCompound();
        e.writeEntityToNBT(tag);
        return tag.getInteger("Fuse");
    }

    public static boolean isCreeperCharged(EntityCreeper e) {
        NBTTagCompound tag = new NBTTagCompound();
        e.writeEntityToNBT(tag);
        return tag.getBoolean("powered");
    }

    public static boolean isPigZombieAngry(EntityPigZombie e) {
        NBTTagCompound tag = new NBTTagCompound();
        e.writeEntityToNBT(tag);
        return tag.getInteger("Anger") > 0;
    }

    public static Entity getEntityByUID(World world, UUID uid) {
        for (Entity e : ((List<Entity>) world.loadedEntityList)) {
            if (e.getUniqueID().equals(uid))
                return e;
        }
        return null;
    }

    public static Collection<AttributeModifier>
    getAttributeByName(IAttributeInstance iai, String n) {
        if (iai instanceof ModifiableAttributeInstance) {
            ModifiableAttributeInstance mai = (ModifiableAttributeInstance) iai;
            /*
            for (int i = 0; i <= 2; i++) {
                List<AttributeModifier> li =
            (List<AttributeModifier>)mai.getModifiersByOperation(i); for
            (AttributeModifier am : li) { if (n.equals(am.getName())) { return am;
                    }
                }
            }
             */
            return (Collection<AttributeModifier>) mai.mapByName.get(n);
        }
        return null;
    }

    public static boolean isInBiome(Entity e, BiomeGenBase b) {
        return b != null
            && e.worldObj.getBiomeGenForCoords(
                   MathHelper.floor_double(e.posX), MathHelper.floor_double(e.posZ)
               )
            == b;
    }

    public static int countEntities(World world, IEntitySelector sel) {
        int c = 0;
        for (Entity e : ((List<Entity>) world.loadedEntityList)) {
            if (sel == null || sel.isEntityApplicable(e))
                c++;
        }
        return c;
    }

    public static boolean isTamed(Entity e) {
        return e instanceof TameHostile
            || (e instanceof EntityTameable && ((EntityTameable) e).isTamed());
    }
}
