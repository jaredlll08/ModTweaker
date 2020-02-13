package com.blamejared.compat.immersivetech;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import ferro2000.immersivetech.api.craftings.DistillerRecipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.definitions.zenclasses.ParsedZenClassMethod;

@ZenClass("mods.immersivetech.Distiller")
@ZenRegister
@ModOnly("immersivetech")
public class Distiller {

    @ZenMethod
    public static void addRecipe(ILiquidStack output, IIngredient outputStack, ILiquidStack input, int energy, int time, double chance) {
        if( input.getAmount() > 1000 ) {
            throw new IllegalArgumentException("Cannot have a fluid input amount greater than 1000 mb, or else the Distiller won't accept the liquid!");
        }

        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toFluid(output), InputHelper.toFluid(input), energy, time, (float) chance, outputStack));
    }

    @ZenMethod
    public static void removeRecipe(ILiquidStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toFluid(input)));
    }

    private static class Add extends BaseAction {

        private FluidStack output;
        private FluidStack input;
        private IIngredient outputStack;
        private int energy;
        private int time;
        private float chance;

        public Add(FluidStack output, FluidStack input, int energy, int time, float chance, IIngredient outputStack) {
            super("Distiller");
            this.output = output;
            this.input = input;
            this.energy = energy;
            this.time = time;
            this.chance = chance;
            this.outputStack = outputStack;
        }

        @Override
        public void apply() {
            IItemStack[] stacks = outputStack != null ? outputStack.getItemArray() : new IItemStack[0];
            ItemStack os = ItemStack.EMPTY;

            if( stacks.length > 0 ) {
                os = InputHelper.toStack(stacks[0]);
                os.setCount(outputStack.getAmount());
            }

            DistillerRecipes.addRecipe(output, input, os, energy, time, chance);
        }

        @Override
        public String describe() {
            return "Adding Distiller recipe for: " + output + " (with: " + outputStack + ") from: " + input + " energy: " + energy + ", time: " + time + ", chance: " + chance;
        }
    }

    private static class Remove extends BaseAction {

        private FluidStack input;

        public Remove(FluidStack input) {
            super("Distiller");
            this.input = input.copy();
            this.input.amount = Integer.MAX_VALUE;
        }

        @Override
        public void apply() {
            DistillerRecipes.recipeList.remove(DistillerRecipes.findRecipe(this.input));
        }

        @Override
        public String describe() {
            return "Removing Distiller recipe for: " + input;
        }
    }
}
