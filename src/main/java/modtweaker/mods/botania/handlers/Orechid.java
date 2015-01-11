package modtweaker.mods.botania.handlers;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;

@ZenClass("mods.botania.Orechid")
public class Orechid {
    @ZenMethod
    public static void addOre(IOreDictEntry oreDict, int weight) {
        MineTweakerAPI.apply(new Add(oreDict.getName(), weight));
    }

    @ZenMethod
    public static void addOre(String oreDict, int weight) {
        MineTweakerAPI.apply(new Add(oreDict, weight));
    }

    private static class Add implements IUndoableAction {
        String oreDict;
        int weight;

        public Add(String ore, int prop) {
            oreDict = ore;
            weight = prop;
        }

        @Override
        public void apply() {

            BotaniaAPI.addOreWeight(oreDict, weight);
        }

        @Override
        public String describe() {
            return "Adding Orechid Ore Weight: " + oreDict + ":" + weight;
        }

        @Override
        public boolean canUndo() {
            return oreDict != null;
        }

        @Override
        public void undo() {
            BotaniaAPI.oreWeights.remove(oreDict);
        }

        @Override
        public String describeUndo() {
            return "Removing Orechid Ore: " + oreDict;
        }

        @Override
        public String getOverrideKey() {
            return null;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeOre(IOreDictEntry oreDict) {
        MineTweakerAPI.apply(new Remove(oreDict.getName()));
    }

    @ZenMethod
    public static void removeOre(String oreDict) {
        MineTweakerAPI.apply(new Remove(oreDict));
    }

    private static class Remove implements IUndoableAction {
        String oreDict;
        int weight;

        public Remove(String ore) {
            oreDict = ore;
        }

        @Override
        public void apply() {
            weight = BotaniaAPI.getOreWeight(oreDict);
            BotaniaAPI.oreWeights.remove(oreDict);
        }

        @Override
        public String describe() {
            return "Removing Orechid Ore: " + oreDict;
        }

        @Override
        public boolean canUndo() {
            return weight > 0;
        }

        @Override
        public void undo() {
            BotaniaAPI.addOreWeight(oreDict, weight);
        }

        @Override
        public String describeUndo() {
            return "Restoring Orechid Ore Weight: " + oreDict + ":" + weight;
        }

        @Override
        public String getOverrideKey() {
            return null;
        }
    }
}
