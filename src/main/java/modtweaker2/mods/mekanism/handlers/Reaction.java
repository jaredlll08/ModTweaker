//package modtweaker2.mods.mekanism.handlers;
//
//import static modtweaker2.helpers.InputHelper.toFluid;
//import static modtweaker2.helpers.InputHelper.toStack;
//import static modtweaker2.mods.mekanism.MekanismHelper.toGas;
//import mekanism.common.recipe.RecipeHandler.Recipe;
//import mekanism.common.recipe.inputs.PressurizedInput;
//import mekanism.common.recipe.machines.PressurizedRecipe;
//import mekanism.common.recipe.outputs.PressurizedOutput;
//import minetweaker.MineTweakerAPI;
//import minetweaker.api.item.IItemStack;
//import minetweaker.api.liquid.ILiquidStack;
//import modtweaker2.mods.mekanism.Mekanism;
//import modtweaker2.mods.mekanism.gas.IGasStack;
//import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
//import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//
//@ZenClass("mods.mekanism.Reaction")
//public class Reaction {
//    @ZenMethod
//    public static void addRecipe(IItemStack solid, ILiquidStack liquid, IGasStack gas, IItemStack outItem, IGasStack outGas, double energy, int ticks) {
//        if (Mekanism.v7)
//        {
//            PressurizedInput input = new PressurizedInput(toStack(solid), toFluid(liquid), toGas(gas));
//            PressurizedRecipe recipe = new PressurizedRecipe(input, new PressurizedOutput(toStack(outItem), toGas(outGas)), energy, ticks);
//            MineTweakerAPI.apply(new AddMekanismRecipe("PRESSURIZED_REACTION_CHAMBER", Recipe.PRESSURIZED_REACTION_CHAMBER.get(), input, recipe));
//        }
//        else {
//            mekanism.common.recipe.machines.PressurizedRecipe recipe = new mekanism.common.recipe.machines.PressurizedRecipe(toStack(solid), toFluid(liquid), toGas(gas), toStack(outItem), toGas(outGas), energy, ticks);
//            MineTweakerAPI.apply(new AddMekanismRecipe("PRESSURIZED_REACTION_CHAMBER", Recipe.PRESSURIZED_REACTION_CHAMBER.get(), recipe.getInput(), recipe));
//        }
//    }
//
//    @ZenMethod
//    public static void removeRecipe(IItemStack outItem, IGasStack outGas) {
//        if (!Mekanism.v7) throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
//        PressurizedOutput output = new PressurizedOutput(toStack(outItem), toGas(outGas));
//        MineTweakerAPI.apply(new RemoveMekanismRecipe("PRESSURIZED_REACTION_CHAMBER", Recipe.PRESSURIZED_REACTION_CHAMBER.get(), output));
//    }
//}
