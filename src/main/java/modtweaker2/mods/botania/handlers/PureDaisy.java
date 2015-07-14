package modtweaker2.mods.botania.handlers;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;

@ZenClass("mods.botania.PureDaisy")
public class PureDaisy {
    
    public static final String name = "Botania PureDaisy";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IIngredient blockInput, IItemStack blockOutput) {
        if(blockInput == null || blockOutput == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        Object input = InputHelper.toObject(blockInput);
        if(input instanceof ItemStack) input = Block.getBlockFromItem(((ItemStack)input).getItem());
        ItemStack output = InputHelper.toStack(blockOutput);
        
        RecipePureDaisy recipe = new RecipePureDaisy(input, Block.getBlockFromItem(output.getItem()), output.getItemDamage());
        
        MineTweakerAPI.apply(new Add(recipe));
    }
    
    private static class Add extends BaseListAddition<RecipePureDaisy> {
        public Add(RecipePureDaisy recipe) {
            super(PureDaisy.name, BotaniaAPI.pureDaisyRecipes);
            recipes.add(recipe);
        }
        
        @Override
        protected String getRecipeInfo(RecipePureDaisy recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getOutput(), 1, recipe.getOutputMeta()));
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void removeRecipe(IIngredient input, @Optional IIngredient output) {
        List<RecipePureDaisy> recipes = new LinkedList<RecipePureDaisy>();
        
        for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
            recipes.add(recipe);
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            
        }
    }
    
    private static class Remove extends BaseListRemoval<RecipePureDaisy> {
        public Remove(List<RecipePureDaisy> recipes) {
            super(PureDaisy.name, BotaniaAPI.pureDaisyRecipes, recipes);
        }
        
        @Override
        protected String getRecipeInfo(RecipePureDaisy recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getOutput(), 1, recipe.getOutputMeta()));
        }
    }
}
