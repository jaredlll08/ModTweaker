package com.blamejared.compat.tconstruct.recipes;

import net.minecraftforge.fluids.*;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

public class MeltingRecipeTweaker extends MeltingRecipe {
    
    public MeltingRecipeTweaker(RecipeMatch input, Fluid output) {
        super(input, output);
    }
    
    public MeltingRecipeTweaker(RecipeMatch input, FluidStack output) {
        super(input, output);
    }
    
    public MeltingRecipeTweaker(RecipeMatch input, Fluid output, int temperature) {
        super(input, output, temperature);
    }
    
    public MeltingRecipeTweaker(RecipeMatch input, FluidStack output, int temperature) {
        super(input, output, temperature);
    }
}
