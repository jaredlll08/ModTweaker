package modtweaker.mods.botania.handlers;

import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.mc1102.item.MCItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.client.integration.jei.elventrade.ElvenTradeRecipeWrapper;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.toObjects;
import static com.blamejared.mtlib.helpers.InputHelper.toStacks;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.botania.ElvenTrade")
public class ElvenTrade {
    
    protected static final String name = "Botania Eleven Trade";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IItemStack[] outputs, IIngredient[] input) {
        MineTweakerAPI.apply(new Add(new RecipeElvenTrade(InputHelper.toStacks(outputs), toObjects(input))));
    }

    private static class Add extends BaseListAddition<RecipeElvenTrade> {
        public Add(RecipeElvenTrade recipe) {
            super(ElvenTrade.name, BotaniaAPI.elvenTradeRecipes);
            recipes.add(recipe);
        }
    
        @Override
        public void apply() {
            super.apply();
            successful.forEach(rec ->{
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new ElvenTradeRecipeWrapper(rec));
            });
        }
        
        @Override
        public void undo() {
            super.apply();
            successful.forEach(rec ->{
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new ElvenTradeRecipeWrapper(rec));
            });
        }
    
        @Override
        public String getRecipeInfo(RecipeElvenTrade recipe) {
            return LogHelper.getStackDescription(recipe.getOutputs());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        // Get list of existing recipes, matching with parameter
        LinkedList<RecipeElvenTrade> recipes = new LinkedList<RecipeElvenTrade>();
        
        for(RecipeElvenTrade entry : BotaniaAPI.elvenTradeRecipes) {
            if(entry != null && entry.getOutputs() != null && matches(output, toStacks(entry.getOutputs().toArray(new ItemStack[entry.getOutputs().size()])))) {
                recipes.add(entry);
            }
        }
        
        // Check if we found the recipes and apply the action
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ElvenTrade.name, output.toString()));
        }
        
    }
    public static IItemStack[] toStacks(ItemStack[] iIngredient) {
        ArrayList<IItemStack> stacks = new ArrayList<>();
    
        for(ItemStack stack : iIngredient) {
            stacks.add(new MCItemStack(stack));
        }
        
        return stacks.toArray(new IItemStack[stacks.size()]);
    }

    private static class Remove extends BaseListRemoval<RecipeElvenTrade> {
        public Remove(List<RecipeElvenTrade> recipes) {
            super(ElvenTrade.name, BotaniaAPI.elvenTradeRecipes, recipes);
        }
    
        @Override
        public void apply() {
            super.apply();
            successful.forEach(rec ->{
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new ElvenTradeRecipeWrapper(rec));
            });
        }
    
        @Override
        public void undo() {
            super.apply();
            successful.forEach(rec ->{
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new ElvenTradeRecipeWrapper(rec));
            });
        }
    
        @Override
        public String getRecipeInfo(RecipeElvenTrade recipe) {
            return LogHelper.getStackDescription(recipe.getOutputs());
        }
    }
}
