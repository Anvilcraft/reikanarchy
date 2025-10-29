package Reika.DragonAPI.Instantiable.Data.BlockStruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class DepthFirstSearch extends AbstractSearch {
    private final LinkedList<Coordinate> currentPath = new LinkedList();
    private Comparator stepValue;
    private boolean isDone;

    public DepthFirstSearch(
        int x, int y, int z, PropagationCondition p, TerminationCondition t
    ) {
        super(x, y, z, p, t);
        currentPath.add(root);
    }

    @Override
    public boolean tick(World world) {
        if (isDone)
            return true;
        if (stepValue == null && termination instanceof FixedPositionTarget)
            stepValue = new Coordinate.DistanceComparator(
                ((FixedPositionTarget) termination).getTarget(), false
            );
        Coordinate c = currentPath.getLast();
        ArrayList<Coordinate> li = this.getNextSearchCoordsFor(world, c);
        if (stepValue != null)
            Collections.sort(li, stepValue);
        for (Coordinate c2 : li) {
            if (c2.yCoord < 0 || c2.yCoord >= 256)
                continue;
            if (searchedCoords.contains(c2))
                continue;
            if (currentPath.size() > depthLimit
                || !limit.isBlockInside(c.xCoord, c.yCoord, c.zCoord))
                continue;
            if (!this.isValidLocation(
                    world, c2.xCoord, c2.yCoord, c2.zCoord, currentPath.getLast()
                ))
                continue;
            currentPath.add(c2);
            if (termination.isValidTerminus(world, c2.xCoord, c2.yCoord, c2.zCoord)) {
                result.addAll(currentPath);
                isDone = true;
                return true;
            } else {
                searchedCoords.add(c2);
                return false;
            }
        }
        currentPath.removeLast();
        if (currentPath.isEmpty()) {
            isDone = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void clear() {
        searchedCoords.clear();
        currentPath.clear();
        result.clear();
        System.gc();
    }

    public static LinkedList<Coordinate> getPath(
        World world,
        double x,
        double y,
        double z,
        TerminationCondition t,
        PropagationCondition c
    ) {
        DepthFirstSearch s = new DepthFirstSearch(
            MathHelper.floor_double(x),
            MathHelper.floor_double(y),
            MathHelper.floor_double(z),
            c,
            t
        );
        while (!s.tick(world)) {}
        return s.result.isEmpty() ? null : s.getResult().getPath();
    }
}
