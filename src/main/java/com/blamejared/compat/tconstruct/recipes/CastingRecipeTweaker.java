package com.blamejared.compat.tconstruct.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

public class CastingRecipeTweaker extends CastingRecipe {
    
    public CastingRecipeTweaker(ItemStack output, RecipeMatch cast, Fluid fluid, int amount) {
        super(output, cast, fluid, amount);
    }
    
    public CastingRecipeTweaker(ItemStack output, RecipeMatch cast, Fluid fluid, int amount, int time) {
        super(output, cast, fluid, amount, time);
    }
    
    public CastingRecipeTweaker(ItemStack output, Fluid fluid, int amount, int time) {
        super(output, fluid, amount, time);
    }
    
    public CastingRecipeTweaker(ItemStack output, RecipeMatch cast, Fluid fluid, int amount, boolean consumesCast, boolean switchOutputs) {
        super(output, cast, fluid, amount, consumesCast, switchOutputs);
    }
    
    public CastingRecipeTweaker(ItemStack output, RecipeMatch cast, FluidStack fluid, boolean consumesCast, boolean switchOutputs) {
        super(output, cast, fluid, consumesCast, switchOutputs);
    }
    
    public CastingRecipeTweaker(ItemStack output, RecipeMatch cast, FluidStack fluid, int time, boolean consumesCast, boolean switchOutputs) {
        super(output, cast, fluid, time, consumesCast, switchOutputs);
    }
}
