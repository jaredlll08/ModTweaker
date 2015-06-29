//package modtweaker2.mods.mekanism.handlers;
//
//import static modtweaker2.helpers.InputHelper.toFluid;
//import static modtweaker2.mods.mekanism.MekanismHelper.toGas;
//
//import java.util.Map;
//
//import mekanism.common.recipe.RecipeHandler.Recipe;
//import mekanism.common.recipe.inputs.ChemicalPairInput;
//import mekanism.common.recipe.machines.SeparatorRecipe;
//import mekanism.common.recipe.outputs.ChemicalPairOutput;
//import minetweaker.MineTweakerAPI;
//import minetweaker.api.liquid.ILiquidStack;
//import modtweaker2.mods.mekanism.Mekanism;
//import modtweaker2.mods.mekanism.gas.IGasStack;
//import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
//import modtweaker2.utils.BaseMapRemoval;
//import net.minecraftforge.fluids.FluidStack;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//
//@ZenClass("mods.mekanism.Separator")
//public class Separator {
//    @ZenMethod
//    public static void addRecipe(ILiquidStack input, IGasStack gas1, IGasStack gas2) {
//        if (Mekanism.v7)
//        {
//            ChemicalPairInput pair = new ChemicalPairInput(toGas(gas1), toGas(gas2));
//            MineTweakerAPI.apply(new AddMekanismRecipe("ELECTROLYTIC_SEPARATOR", Recipe.ELECTROLYTIC_SEPARATOR.get(), toFluid(input), pair));
//        } else
//        {
//            throw new UnsupportedOperationException("Syntax for v8 is: Fluid, Energy, Gas, Gas");
//        }
//    }
//
//    @ZenMethod
//    public static void addRecipe(ILiquidStack input, double energy, IGasStack gas1, IGasStack gas2) {
//        if (Mekanism.v7)
//        {
//            throw new UnsupportedOperationException("Syntax for v7 is: Fluid, Gas, Gas");
//        } else
//        {
//            SeparatorRecipe recipe = new SeparatorRecipe(toFluid(input), energy, toGas(gas1), toGas(gas2));
//            MineTweakerAPI.apply(new AddMekanismRecipe("ELECTROLYTIC_SEPARATOR", Recipe.ELECTROLYTIC_SEPARATOR.get(), recipe.getInput(), recipe));
//        }
//    }
//
//    @ZenMethod
//    public static void removeRecipe(ILiquidStack input) {
//        if (!Mekanism.v7) throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
//        MineTweakerAPI.apply(new Remove(toFluid(input)));
//    }
//
//    private static class Remove extends BaseMapRemoval {
//        public Remove(FluidStack stack) {
//            super("Electrolytic Separator", Recipe.ELECTROLYTIC_SEPARATOR.get(), stack);
//        }
//
//        //We must search through the recipe entries so that we can assign the correct key for removal
//        @Override
//        public void apply() {
//            for (Map.Entry<FluidStack, ChemicalPairOutput> entry : ((Map<FluidStack, ChemicalPairOutput>) map).entrySet()) {
//                if (entry.getKey() != null && entry.getKey().isFluidEqual((FluidStack) stack)) {
//                    key = entry.getKey();
//                    break;
//                }
//            }
//
//            super.apply();
//        }
//    }
//}
