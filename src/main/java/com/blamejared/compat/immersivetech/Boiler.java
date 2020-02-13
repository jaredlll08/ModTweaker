package com.blamejared.compat.immersivetech;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import ferro2000.immersivetech.api.craftings.BoilerRecipes;
import ferro2000.immersivetech.api.craftings.SolarTowerRecipes;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.immersivetech.Boiler")
@ZenRegister
@ModOnly("immersivetech")
public class Boiler {

    @ZenMethod
    public static void addRecipe(ILiquidStack output, ILiquidStack input, int time) {
        if( input.getAmount() > 1000 ) {
            throw new IllegalArgumentException("Cannot have a fluid input amount greater than 1000 mb, or else the Boiler won't accept the liquid!");
        }

        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(output), InputHelper.toFluid(input), time));
    }

    @ZenMethod
    public static void removeRecipe(ILiquidStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toFluid(input)));
    }

    private static class Add extends BaseAction {

        private FluidStack output;
        private FluidStack input;
        private int time;

        public Add(FluidStack output, FluidStack input, int time) {
            super("Boiler");
            this.output = output;
            this.input = input;
            this.time = time;
        }

        @Override
        public void apply() {
            BoilerRecipes.addRecipe(output, input, time);
        }

        @Override
        public String describe() {
            return "Adding Boiler recipe for: " + output + " from: " + input + " time: " + time;
        }
    }

    private static class Remove extends BaseAction {

        private FluidStack input;

        public Remove(FluidStack input) {
            super("Boiler");
            this.input = input.copy();
            this.input.amount = Integer.MAX_VALUE;
        }

        @Override
        public void apply() {
            BoilerRecipes.recipeList.remove(BoilerRecipes.findRecipe(this.input));
        }

        @Override
        public String describe() {
            return "Removing Boiler recipe for: " + input;
        }
    }
}
