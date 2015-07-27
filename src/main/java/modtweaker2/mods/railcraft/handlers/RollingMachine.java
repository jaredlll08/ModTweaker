package modtweaker2.mods.railcraft.handlers;

import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.InputHelper.toShapedObjects;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseCraftingAddition;
import modtweaker2.utils.BaseCraftingRemoval;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.Rolling")
public class RollingMachine {
    
    public static final String name = "Railcraft Rolling Machine";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] ingredients) {
        MineTweakerAPI.apply(new Add(new ShapedOreRecipe(toStack(output), toShapedObjects(ingredients))));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
        MineTweakerAPI.apply(new Add(new ShapelessOreRecipe(toStack(output), toObjects(ingredients))));
    }

    private static class Add extends BaseCraftingAddition {
        public Add(IRecipe recipe) {
            super(RollingMachine.name, RailcraftHelper.rolling);
            recipes.add(recipe);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient ingredient) {
        List<IRecipe> recipes = BaseCraftingRemoval.getRecipes(RailcraftHelper.rolling, ingredient);

        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipes found for %s. Command ignored!", RollingMachine.name, ingredient.toString()));
        }
    }

    private static class Remove extends BaseCraftingRemoval {
        public Remove(List<IRecipe> recipes) {
            super(RollingMachine.name, RailcraftHelper.rolling, recipes);
        }
    }
}
