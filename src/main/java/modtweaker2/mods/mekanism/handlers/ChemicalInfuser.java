//package modtweaker2.mods.mekanism.handlers;
//
//import static modtweaker2.mods.mekanism.MekanismHelper.toGas;
//import mekanism.common.recipe.RecipeHandler.Recipe;
//import mekanism.common.recipe.inputs.ChemicalPairInput;
//import mekanism.common.recipe.machines.ChemicalInfuserRecipe;
//import minetweaker.MineTweakerAPI;
//import modtweaker2.mods.mekanism.Mekanism;
//import modtweaker2.mods.mekanism.gas.IGasStack;
//import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
//import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//
//@ZenClass("mods.mekanism.chemical.Infuser")
//public class ChemicalInfuser {
//	@ZenMethod
//	public static void addRecipe(IGasStack left, IGasStack right, IGasStack out) {
//		if (Mekanism.v7) {
//			ChemicalPairInput pair = new ChemicalPairInput(toGas(left), toGas(right));
//			MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_INFUSER", Recipe.CHEMICAL_INFUSER.get(), pair, toGas(out)));
//		} else {
//			ChemicalInfuserRecipe recipe = new ChemicalInfuserRecipe(toGas(left), toGas(right), toGas(out));
//			MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_INFUSER", Recipe.CHEMICAL_INFUSER.get(), recipe.getInput(), recipe));
//		}
//	}
//
//	@ZenMethod
//	public static void removeRecipe(IGasStack output) {
//		if (!Mekanism.v7)
//			throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
//		MineTweakerAPI.apply(new RemoveMekanismRecipe("CHEMICAL_INFUSER", Recipe.CHEMICAL_INFUSER.get(), toGas(output)));
//		
//		
//	}
//}
