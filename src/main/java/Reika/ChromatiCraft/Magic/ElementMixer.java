/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Magic;

import java.util.ArrayList;
import java.util.Collection;

import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.DragonAPI.Instantiable.Data.Maps.MixMap;
import Reika.DragonAPI.Instantiable.Data.Maps.PairMap;

public class ElementMixer {
    public static final ElementMixer instance = new ElementMixer();

    private final MixMap<CrystalElement, CrystalElement> data = new MixMap();
    private final PairMap<CrystalElement> locks = new PairMap();

    private ElementMixer() {
        this.addMix(CrystalElement.BLACK, CrystalElement.WHITE, CrystalElement.GRAY);
        this.addMix(CrystalElement.RED, CrystalElement.WHITE, CrystalElement.PINK);
        this.addMix(CrystalElement.GREEN, CrystalElement.WHITE, CrystalElement.LIME);
        this.addMix(CrystalElement.BLUE, CrystalElement.WHITE, CrystalElement.LIGHTBLUE);
        this.addMix(CrystalElement.BLUE, CrystalElement.GREEN, CrystalElement.CYAN);
        this.addMix(CrystalElement.RED, CrystalElement.YELLOW, CrystalElement.ORANGE);
        this.addMix(CrystalElement.RED, CrystalElement.BLUE, CrystalElement.PURPLE);

        this.addMix(CrystalElement.GRAY, CrystalElement.WHITE, CrystalElement.LIGHTGRAY);
        this.addMix(CrystalElement.PURPLE, CrystalElement.PINK, CrystalElement.MAGENTA);

        this.addIncompatibility(CrystalElement.CYAN, CrystalElement.RED);
        this.addIncompatibility(CrystalElement.LIGHTGRAY, CrystalElement.BLUE);
        this.addIncompatibility(CrystalElement.PINK, CrystalElement.WHITE);
    }

    private void addMix(CrystalElement e1, CrystalElement e2, CrystalElement out) {
        data.addMix(e1, e2, out);
    }

    public CrystalElement getMix(CrystalElement e1, CrystalElement e2) {
        return data.getMix(e1, e2);
    }

    public boolean isMixable(CrystalElement e1, CrystalElement e2) {
        return this.getMix(e1, e2) != null;
    }

    public CrystalElement subtract(CrystalElement mix, CrystalElement e) {
        return data.getOtherEntry(mix, e);
    }

    private void addIncompatibility(CrystalElement e1, CrystalElement e2) {
        locks.add(e1, e2);
    }

    public boolean isCompatible(CrystalElement e1, CrystalElement e2) {
        return !locks.contains(e1, e2);
    }

    public Collection<CrystalElement> getMixablesWith(CrystalElement color) {
        return data.getMixablesWith(color);
    }

    public Collection<CrystalElement> getChildrenOf(CrystalElement color) {
        return data.getChildrenOf(color);
    }

    public Collection<CrystalElement> getMixParents(CrystalElement color) {
        return data.getMixParents(color);
    }

    public boolean related(CrystalElement e1, CrystalElement e2) {
        return this.getMixParents(e1).contains(e2) || this.getMixParents(e2).contains(e1);
    }

    public boolean hasMixes(CrystalElement color) {
        //ReikaJavaLibrary.pConsole(color+" > "+this.getChildrenOf(color)+" &
        //"+this.getMixParents(color));
        return !this.getRelatedColors(color).isEmpty(
        ); //this.getChildrenOf(color) != null || !this.getMixParents(color).isEmpty();
    }

    public Collection<CrystalElement> getRelatedColors(CrystalElement color) {
        Collection<CrystalElement> c = new ArrayList(this.getMixParents(color));
        Collection<CrystalElement> c2 = this.getChildrenOf(color);
        if (c2 != null)
            c.addAll(c2);
        return c;
    }
}
