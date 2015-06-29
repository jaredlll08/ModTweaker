package modtweaker2.mods.mariculture.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mariculture.api.fishery.Loot;
import mariculture.api.fishery.Loot.Rarity;
import mariculture.api.fishery.RodType;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.mods.mariculture.MaricultureHelper;
import modtweaker2.utils.BaseUndoable;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cpw.mods.fml.common.Loader;

@ZenClass("mods.mariculture.Fishing")
public class Fishing {
    //HashMap Helper for converting strings to rodtypes, (will personally add a registry to mariculture eventually...)
    public static HashMap<String, RodType> rodTypes = new HashMap();

    static {
        rodTypes.put("net", RodType.NET);
        rodTypes.put("old", RodType.OLD);
        rodTypes.put("good", RodType.GOOD);
        rodTypes.put("dire", RodType.DIRE);
        rodTypes.put("super", RodType.SUPER);
        rodTypes.put("flux", RodType.FLUX);
        if (Loader.isModLoaded("AWWayofTime")) {
            try {
                rodTypes.put("blood", (RodType) ReflectionHelper.getStaticObject(Class.forName("mariculture.plugins.PluginBloodMagic"), "BLOOD"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //Adding Fishing Loot
    @ZenMethod
    public static void addJunk(IItemStack loot, double chance, @Optional String type, @Optional boolean exact, @Optional int[] dimension) {
        addLoot(toStack(loot), chance, type, exact, dimension, Rarity.JUNK);
    }

    @ZenMethod
    public static void addGood(IItemStack loot, double chance, @Optional String type, @Optional boolean exact, @Optional int[] dimension) {
        addLoot(toStack(loot), chance, type, exact, dimension, Rarity.GOOD);
    }

    @ZenMethod
    public static void addRare(IItemStack loot, double chance, @Optional String type, @Optional boolean exact, @Optional int[] dimension) {
        addLoot(toStack(loot), chance, type, exact, dimension, Rarity.RARE);
    }

    private static void addLoot(ItemStack stack, double chance, String type, boolean exact, int[] dimension, Rarity rarity) {
        if (dimension == null || dimension.length == 0) dimension = new int[] { Short.MAX_VALUE };
        if (type == null) type = "dire";
        for (int dim : dimension) {
            MineTweakerAPI.apply(new AddLoot(new Loot(stack, chance, rarity, dim, rodTypes.get(type), exact), rarity.name()));
        }
    }

    private static class AddLoot extends BaseUndoable {
        private final Loot loot;

        public AddLoot(Loot loot, String description) {
            super("Fishing Loot - " + description);
            this.loot = loot;
        }

        @Override
        public void apply() {
            ArrayList<Loot> list = MaricultureHelper.loot.get(loot.rarity);
            list.add(loot);
            MaricultureHelper.loot.put(loot.rarity, list);
            success = true;
        }

        @Override
        public void undo() {
            ArrayList<Loot> list = MaricultureHelper.loot.get(loot.rarity);
            list.remove(loot);
            MaricultureHelper.loot.put(loot.rarity, list);
        }

        @Override
        public String getRecipeInfo() {
            return loot.loot.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing Fishing Loot, will remove it from every single list
    @ZenMethod
    public static void removeLoot(IItemStack loot) {
        MineTweakerAPI.apply(new RemoveLoot(toStack(loot)));
    }

    private static class RemoveLoot extends BaseUndoable {
        private HashMap<Rarity, Loot> loot;
        private final ItemStack stack;

        public RemoveLoot(ItemStack stack) {
            super("Fishing Loot");
            this.loot = new HashMap();
            this.stack = stack;
        }

        @Override
        public void apply() {
            loot.clear();
            apply(Rarity.JUNK);
            apply(Rarity.GOOD);
            apply(Rarity.RARE);
            success = true;
        }

        //Performs the apply function on all rarity types
        public void apply(Rarity rarity) {
            ArrayList<Loot> list = MaricultureHelper.loot.get(rarity);
            int preSize = list.size();
            for (Loot l : list) {
                if (areEqual(l.loot, stack)) {
                    loot.put(rarity, l);
                    break;
                }
            }

            list.remove(loot);

            if (list.size() != preSize) MaricultureHelper.loot.put(rarity, list);
        }

        @Override
        public void undo() {
            for (Map.Entry<Rarity, Loot> entry : loot.entrySet()) {
                undo(entry.getKey(), entry.getValue());
            }
        }

        //Undoes the action on the applicable rarities
        public void undo(Rarity rarity, Loot l) {
            ArrayList<Loot> list = MaricultureHelper.loot.get(rarity);
            list.add(l);
            MaricultureHelper.loot.put(rarity, list);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
