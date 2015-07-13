package modtweaker2.mods.extendedworkbench.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;

import java.util.Arrays;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseCraftingAddition;
import modtweaker2.utils.BaseCraftingRemoval;
import naruto1310.extendedWorkbench.crafting.ExtendedCraftingManager;
import naruto1310.extendedWorkbench.crafting.ExtendedShapedRecipes;
import naruto1310.extendedWorkbench.crafting.ExtendedShapelessRecipes;
import naruto1310.extendedWorkbench.crafting.IExtendedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.extendedworkbench")
public class ExtendedCrafting {
    
    public static final String name = "Extended Workbench";
    
    @ZenMethod
    public static void addShaped(IItemStack output, IItemStack[][] ingredients) {
        MineTweakerAPI.apply(new Add(getShapedRecipe(output, ingredients)));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IItemStack[] ingredients) {
        MineTweakerAPI.apply(new Add(new ExtendedShapelessRecipes(toStack(output), Arrays.asList(toStacks(ingredients)))));
    }

    private static class Add extends BaseCraftingAddition {
        public Add(IExtendedRecipe recipe) {
            super(ExtendedCrafting.name, ExtendedCraftingManager.getInstance().getRecipeList());
            recipes.add(recipe);
        }
    }

    private static IExtendedRecipe getShapedRecipe(IItemStack out, IItemStack[][] ingredients) {

        int width = 0;
        int height = ingredients.length;
        ItemStack[] recipe;

        for (int x = 0; x < ingredients.length; x++) {
            if (ingredients[x] != null && ingredients[x].length > width) width = ingredients[x].length;
        }

        recipe = new ItemStack[width * height];
        int counter = 0;
        for (int x = 0; x < ingredients.length; x++) {
            if (ingredients[x] != null) {
                for (int y = 0; y < ingredients[x].length; y++) {
                    recipe[counter++] = toStack(ingredients[x][y]);
                }
            }
        }

        return new ExtendedShapedRecipes(width, height, recipe, toStack(out));

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        List<IRecipe> recipes = BaseCraftingRemoval.getRecipes(ExtendedCraftingManager.getInstance().getRecipeList(), output);
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new BaseCraftingRemoval(ExtendedCrafting.name, ExtendedCraftingManager.getInstance().getRecipeList(), recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ExtendedCrafting.name, output));
        }
    }
}
