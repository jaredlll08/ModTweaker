//package modtweaker2.mods.mekanism.handlers;
//
//import static modtweaker2.helpers.InputHelper.toStack;
//import mekanism.api.gas.GasRegistry;
//import mekanism.common.recipe.RecipeHandler.Recipe;
//import mekanism.common.recipe.inputs.AdvancedMachineInput;
//import mekanism.common.recipe.machines.OsmiumCompressorRecipe;
//import minetweaker.MineTweakerAPI;
//import minetweaker.api.item.IItemStack;
//import modtweaker2.mods.mekanism.Mekanism;
//import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
//import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//
//@ZenClass("mods.mekanism.Compressor")
//public class Compressor {
//    @ZenMethod
//    public static void addRecipe(IItemStack input, IItemStack output) {
//        AdvancedMachineInput aInput = new AdvancedMachineInput(toStack(input), GasRegistry.getGas("liquidOsmium"));
//        if (Mekanism.v7)
//            MineTweakerAPI.apply(new AddMekanismRecipe("OSMIUM_COMPRESSOR", Recipe.OSMIUM_COMPRESSOR.get(), aInput, toStack(output)));
//        else
//        {
//            OsmiumCompressorRecipe recipe = new OsmiumCompressorRecipe(toStack(input), toStack(output));
//            MineTweakerAPI.apply(new AddMekanismRecipe("OSMIUM_COMPRESSOR", Recipe.OSMIUM_COMPRESSOR.get(), recipe.getInput(), recipe));
//        }
//    }
//
//    @ZenMethod
//    public static void removeRecipe(IItemStack output) {
//        if (!Mekanism.v7) throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
//        MineTweakerAPI.apply(new RemoveMekanismRecipe("OSMIUM_COMPRESSOR", Recipe.OSMIUM_COMPRESSOR.get(), toStack(output)));
//    }
//}
