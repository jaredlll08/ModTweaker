//package modtweaker2.mods.mekanism.handlers;
//
//import static modtweaker2.mods.mekanism.MekanismHelper.toGas;
//
//import java.util.Iterator;
//import java.util.Map;
//
//import mekanism.api.gas.GasStack;
//import mekanism.common.recipe.RecipeHandler.Recipe;
//import mekanism.common.recipe.machines.WasherRecipe;
//import minetweaker.MineTweakerAPI;
//import modtweaker2.mods.mekanism.Mekanism;
//import modtweaker2.mods.mekanism.gas.IGasStack;
//import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
//import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//
//@ZenClass("mods.mekanism.chemical.Washer")
//public class ChemicalWasher {
//    @ZenMethod
//    public static void addRecipe(IGasStack input, IGasStack output) {
//        if (Mekanism.v7)
//        {
//            MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_WASHER", Recipe.CHEMICAL_WASHER.get(), toGas(input), toGas(output)));
//        }
//        else
//        {
//            WasherRecipe recipe = new WasherRecipe(toGas(input), toGas(output));
//            MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_WASHER", Recipe.CHEMICAL_WASHER.get(), recipe.getInput(), recipe));
//        }
//    }
//
//    @ZenMethod
//    public static void removeRecipe(IGasStack output) {
//        if (!Mekanism.v7) throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
//        MineTweakerAPI.apply(new Remove("CHEMICAL_WASHER", Recipe.CHEMICAL_WASHER.get(), toGas(output)));
//    }
//
//    private static class Remove extends RemoveMekanismRecipe {
//        public Remove(String string, Map map, Object key) {
//            super(string, map, key);
//        }
//
//        @Override
//        public void apply() {
//            Iterator it = map.entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry pairs = (Map.Entry) it.next();
//                GasStack key = (GasStack) pairs.getKey();
//                GasStack value = (GasStack) pairs.getValue();
//                if (key != null) {
//                    if (this.key instanceof GasStack && value.isGasEqual((GasStack) this.key)) {
//                        this.key = key;
//                        break;
//                    }
//                }
//
//            }
//
//            recipe = map.get(key);
//            map.remove(key);
//        }
//    }
//}
