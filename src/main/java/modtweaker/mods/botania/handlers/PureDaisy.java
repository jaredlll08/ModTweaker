package modtweaker.mods.botania.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;

import java.util.LinkedList;
import java.util.List;

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
        
        if(input == null || (input instanceof ItemStack && !InputHelper.isABlock((ItemStack)input))) {
            LogHelper.logError(String.format("Input must be a block or an oredict entry."));
            return;
        }
        
        if(input instanceof ItemStack) input = Block.getBlockFromItem(((ItemStack)input).getItem());
        ItemStack output = InputHelper.toStack(blockOutput);
        
        RecipePureDaisy recipe = new RecipePureDaisy(input, Block.getBlockFromItem(output.getItem()).getDefaultState(), output.getItemDamage());
        
        MineTweakerAPI.apply(new Add(recipe));
    }
    
    private static class Add extends BaseListAddition<RecipePureDaisy> {
        public Add(RecipePureDaisy recipe) {
            super(PureDaisy.name, BotaniaAPI.pureDaisyRecipes);
            recipes.add(recipe);
        }
        
        @Override
        protected String getRecipeInfo(RecipePureDaisy recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getOutputState().getBlock(), 1));
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        List<RecipePureDaisy> recipes = new LinkedList<RecipePureDaisy>();
        
        for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
            IItemStack out = InputHelper.toIItemStack(new ItemStack(recipe.getOutputState().getBlock(), 1));
            
            if(StackHelper.matches(output, out)) {
                recipes.add(recipe);
            }
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
            return LogHelper.getStackDescription(new ItemStack(recipe.getOutputState().getBlock(), 1));
        }
    }
}
