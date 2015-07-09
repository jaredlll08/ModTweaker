package modtweaker2.mods.pneumaticcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStacks;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import pneumaticCraft.api.recipe.PressureChamberRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.pneumaticcraft.Pressure")
public class Pressure {
    
    public static final String name = "PneumaticCraft Pressure Chamber";
    
    @ZenMethod
    public static void addRecipe(IItemStack[] input, double pressure, IItemStack[] output, boolean asBlock) {
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        MineTweakerAPI.apply(new Add(new PressureChamberRecipe(toStacks(input), (float) pressure, toStacks(output), asBlock)));
    }

    private static class Add extends BaseListAddition<PressureChamberRecipe> {
        public Add(PressureChamberRecipe recipe) {
            super(Pressure.name, PressureChamberRecipe.chamberRecipes);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(PressureChamberRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output[0]);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient[] output) {
        List<PressureChamberRecipe> recipes = new LinkedList<PressureChamberRecipe>();
        
        for (PressureChamberRecipe r : PressureChamberRecipe.chamberRecipes) {
            if(r != null) {
                boolean matches = true;
                for (IIngredient ingredient : output) {
                    for(ItemStack stack : r.output) {
                        if (!matches(ingredient, toIItemStack(stack))) {
                            matches = false;
                            break;
                        }
                    }
                }

                if (matches) {
                    recipes.add(r);
                }
            }
        }

        MineTweakerAPI.apply(new Remove(recipes));
    }

    private static class Remove extends BaseListRemoval<PressureChamberRecipe> {
        public Remove(List<PressureChamberRecipe> recipes) {
            super(Pressure.name, PressureChamberRecipe.chamberRecipes, recipes);
        }
        
        @Override
        public String getRecipeInfo(PressureChamberRecipe recipe) {
            return LogHelper.getStackDescription(recipe.output[0]);
        }
    }
}
