package com.blamejared.compat.actuallyadditions;

import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CompostRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.actuallyadditions.Compost")
@Handler("actuallyadditions")
public class Compost {
    
    @ZenMethod
    @Document({"output", "outputDisplay", "input", "inputDisplay"})
    public static void addRecipe(IItemStack output, IItemStack outputDisplay, IItemStack input, IItemStack inputDisplay) {
        if(!InputHelper.isABlock(outputDisplay) || !InputHelper.isABlock(inputDisplay)) {
            MineTweakerAPI.logError("outputDisplay or InputDisplay is not a block!");
            return;
        }
        MineTweakerAPI.apply(new Add(Collections.singletonList(new CompostRecipe(InputHelper.toStack(input), Block.getBlockFromItem(InputHelper.toStack(inputDisplay).getItem()), InputHelper.toStack(output), Block.getBlockFromItem(InputHelper.toStack(outputDisplay).getItem())))));
    }
    
    @ZenMethod
    @Document({"output"})
    public static void removeRecipe(IItemStack output) {
        List<CompostRecipe> recipes = new ArrayList<>();
        for(CompostRecipe recipe : ActuallyAdditionsAPI.COMPOST_RECIPES) {
            if(output.matches(InputHelper.toIItemStack(recipe.output))) {
                recipes.add(recipe);
            }
        }
        MineTweakerAPI.apply(new Remove(recipes));
    }
    
    private static class Add extends BaseListAddition<CompostRecipe> {
        
        protected Add(List<CompostRecipe> recipes) {
            super("Compost", ActuallyAdditionsAPI.COMPOST_RECIPES, recipes);
        }
        
        @Override
        public String getRecipeInfo(CompostRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
    }
    
    private static class Remove extends BaseListRemoval<CompostRecipe> {
        
        protected Remove(List<CompostRecipe> recipes) {
            super("Compost", ActuallyAdditionsAPI.COMPOST_RECIPES, recipes);
        }
        
        @Override
        public String getRecipeInfo(CompostRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output);
        }
    }
}
