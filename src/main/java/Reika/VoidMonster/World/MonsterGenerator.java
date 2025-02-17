/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.VoidMonster.World;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import Reika.DragonAPI.Auxiliary.Trackers.SpecialDayTracker;
import Reika.DragonAPI.Auxiliary.Trackers.TickRegistry.TickHandler;
import Reika.DragonAPI.Auxiliary.Trackers.TickRegistry.TickType;
import Reika.DragonAPI.DragonOptions;
import Reika.DragonAPI.Instantiable.IO.SimpleConfig;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.DragonAPI.ModInteract.DeepInteract.ReikaMystcraftHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.ExtraUtilsHandler;
import Reika.DragonAPI.ModList;
import Reika.VoidMonster.Entity.EntityVoidMonster;
import Reika.VoidMonster.ModInterface.VoidMystPages;
import Reika.VoidMonster.VoidMonster;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;

public class MonsterGenerator implements TickHandler {
    public static final MonsterGenerator instance = new MonsterGenerator();

    private final Random rand = new Random();

    private final HashMap<Integer, Boolean> APIRules = new HashMap();
    private final HashSet<Integer> configDimensionList = new HashSet();
    private final HashMap<Integer, Integer> monsterCooldown = new HashMap();

    private boolean whitelist;

    private boolean cachedLoadState = false;
    private long cachedLoadTime = -1;

    private MonsterGenerator() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void spawn(World world, EntityPlayer ep) {
        if (world.isRemote)
            return;
        EntityVoidMonster ev = new EntityVoidMonster(world);
        if (world.provider.isHellWorld)
            ev.setNether();
        ev.forceSpawn = true;
        ev.setLocationAndAngles(ep.posX, ev.isNetherVoid() ? 260 : -10, ep.posZ, 0, 0);
        if (this.isHalloween())
            ev.setGhost();
        world.spawnEntityInWorld(ev);
    }

    public boolean isHalloween() {
        if (ReikaObfuscationHelper.isDeObfEnvironment())
            return false;
        if (!DragonOptions.APRIL.getState())
            return false;
        long time = System.currentTimeMillis();
        if (time - cachedLoadTime > 60000) {
            Calendar c = Calendar.getInstance();
            boolean flag = SpecialDayTracker.instance.isHalloween();
            cachedLoadTime = time;
            cachedLoadState = flag;
        }
        return cachedLoadState && rand.nextInt(4) == 0;
    }

    @Override
    public void tick(TickType type, Object... tickData) {
        World world = (World) tickData[0];
        if (world != null) {
            if (this.canSpawnIn(world)) {
                EntityPlayer ep = this.getRandomPlayer(world);
                if (ep != null) {
                    this.spawn(world, ep);
                }
            }
        }
    }

    private EntityPlayer getRandomPlayer(World world) {
        ArrayList<EntityPlayer> li = new ArrayList(world.playerEntities);
        int idx = rand.nextInt(li.size());
        EntityPlayer ep = li.get(idx);
        while (ReikaPlayerAPI.isFake(ep) || ep.ticksExisted < 5) {
            li.remove(idx);
            if (li.isEmpty())
                return null;
            idx = rand.nextInt(li.size());
            ep = li.get(idx);
        }
        return ep;
    }

    private boolean canSpawnIn(World world) {
        if (world.playerEntities.isEmpty())
            return false;
        if (world.provider.dimensionId != -1
            && world.getWorldInfo().getTerrainType() == WorldType.FLAT)
            return false;
        long time = world.getTotalWorldTime();
        if (time % 8 != 0)
            return false;
        if (monsterCooldown.containsKey(world)
            && time < monsterCooldown.get(world.provider.dimensionId))
            return false;
        Collection<EntityVoidMonster> li = VoidMonster.getCurrentMonsterList(world);
        if (li != null && !li.isEmpty())
            return false;
        if (ModList.MYSTCRAFT.isLoaded() && ReikaMystcraftHelper.isMystAge(world))
            return !VoidMystPages.instance.existsInWorld(world);
        return this.isHardcodedAllowed(world.provider.dimensionId)
            || this.isDimensionAllowed(world);
    }

    public boolean isDimensionAllowed(World world) {
        int id = world.provider.dimensionId;
        Boolean get = APIRules.get(id);
        if (get != null)
            return get.booleanValue();
        return configDimensionList.contains(id) == whitelist;
    }

    public void addCooldown(EntityLiving e, int delay) {
        Integer get = monsterCooldown.get(e.worldObj.provider.dimensionId);
        int put = Math.max(get != null ? get.intValue() : 0, delay);
        monsterCooldown.put(e.worldObj.provider.dimensionId, put);
    }

    @Override
    public EnumSet<TickType> getType() {
        return EnumSet.of(TickType.WORLD);
    }

    @Override
    public boolean canFire(Phase p) {
        return p == Phase.START;
    }

    @Override
    public String getLabel() {
        return "Void Monster";
    }

    public void setDimensions(Collection<Integer> dimensions) {
        for (int id : dimensions) {
            configDimensionList.add(id);
        }
    }

    public void setDimensionRuleAPI(int id, boolean allow) {
        APIRules.put(id, allow);
    }

    private boolean isHardcodedAllowed(int id) {
        if (id == 0)
            return true;
        if (id == -1)
            return true;
        if (ModList.EXTRAUTILS.isLoaded() && id == ExtraUtilsHandler.getInstance().darkID)
            return true;
        return false;
    }

    public void loadConfig(SimpleConfig config) {
        configDimensionList.clear();
        ArrayList<Integer> dimensions
            = config.getIntList("Control Setup", "Banned Dimensions", 1, -112);
        whitelist = config.getBoolean(
            "Control Setup", "Dimension list is actually whitelist", false
        );
        this.setDimensions(dimensions);
    }
}
