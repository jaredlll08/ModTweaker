package modtweaker.mods.embers.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;
import teamroots.embers.compat.jei.MixingRecipeWrapper;
import teamroots.embers.recipe.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.toFluid;

@ZenClass("mods.embers.Mixer")
public class Mixer {
    
    public static String name = "Embers Mixer";
    
    @ZenMethod
    public static void addRecipe(ILiquidStack input1, ILiquidStack input2, ILiquidStack input3, ILiquidStack input4, ILiquidStack output) {
        ArrayList<FluidStack> fluids = new ArrayList<>();
        if(input1 != null) {
            fluids.add(toFluid(input1));
        }
        if(input2 != null) {
            fluids.add(toFluid(input2));
        }
        if(input3 != null) {
            fluids.add(toFluid(input3));
        }
        if(input4 != null) {
            fluids.add(toFluid(input4));
        }
        MineTweakerAPI.apply(new Add(new FluidMixingRecipe(fluids.toArray(new FluidStack[fluids.size()]), toFluid(output))));
    }
    
    
    @ZenMethod
    public static void remove(ILiquidStack output) {
        List<FluidMixingRecipe> recipes = new ArrayList<>();
        
        for(int i = 0; i < RecipeRegistry.mixingRecipes.size(); i++) {
            if(RecipeRegistry.mixingRecipes.get(i).output.getFluid().getName().equals(toFluid(output).getFluid().getName())) {
                recipes.add(RecipeRegistry.mixingRecipes.get(i));
            }
        }
        
        if(!recipes.isEmpty()) {
            for(int i = 0; i < recipes.size(); i++) {
                MineTweakerAPI.apply(new Remove(recipes.get(i)));
            }
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", Mixer.name, output.toString()));
        }
        
    }
    
    private static class Add extends BaseListAddition<FluidMixingRecipe> {
        
        public Add(FluidMixingRecipe recipe) {
            super(Mixer.name, RecipeRegistry.mixingRecipes);
            this.recipes.add(recipe);
        }
    
        @Override
        public void apply() {
            super.apply();
            successful.forEach(rec ->{
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new MixingRecipeWrapper(rec));
            });
        }
    
        @Override
        public void undo() {
            super.undo();
            successful.forEach(rec ->{
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new MixingRecipeWrapper(rec));
            });
        }
    
        @Override
        protected String getRecipeInfo(FluidMixingRecipe arg0) {
            return LogHelper.getStackDescription(arg0.output);
        }
    }
    
    private static class Remove extends BaseListRemoval<FluidMixingRecipe> {
        
        public Remove(FluidMixingRecipe recipe) {
            super(Mixer.name, RecipeRegistry.mixingRecipes);
            this.recipes.remove(recipe);
        }
        @Override
        public void apply() {
            super.apply();
            successful.forEach(rec ->{
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new MixingRecipeWrapper(rec));
            });
        }
    
        @Override
        public void undo() {
            super.undo();
            successful.forEach(rec ->{
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new MixingRecipeWrapper(rec));
            });
        }
        @Override
        protected String getRecipeInfo(FluidMixingRecipe arg0) {
            return LogHelper.getStackDescription(arg0.output);
        }
    }
}
