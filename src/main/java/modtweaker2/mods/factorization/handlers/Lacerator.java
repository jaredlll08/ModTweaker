package modtweaker2.mods.factorization.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toObject;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.mods.factorization.FactorizationHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.factorization.Lacerator")
public class Lacerator {
    
    public static final String name = "Factorization Lacerator";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, double probability) {
        Object recipe = FactorizationHelper.getLaceratorRecipe(toObject(input), toStack(output), (float) probability);
        MineTweakerAPI.apply(new Add(recipe));
    }

    private static class Add extends BaseListAddition<Object> {
        @SuppressWarnings("unchecked")
        public Add(Object recipe) {
            super(Lacerator.name, FactorizationHelper.lacerator);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(Object recipe) {
            return LogHelper.getStackDescription((ItemStack) ReflectionHelper.getObject(recipe, "output"));
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        List<Object> recipes = new LinkedList<Object>();
        for (Object r : FactorizationHelper.lacerator) {
            if(r != null) {
                ItemStack out = (ItemStack) ReflectionHelper.getObject(r, "output");
                if (output != null && matches(output, toIItemStack(out))) {
                    recipes.add(r);
                }
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored", Lacerator.name, output.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<Object> {
        @SuppressWarnings("unchecked")
        public Remove(List<Object> recipes) {
            super(Lacerator.name, FactorizationHelper.lacerator, recipes);
        }

        @Override
        public String getRecipeInfo(Object recipe) {
            return LogHelper.getStackDescription((ItemStack) ReflectionHelper.getObject(recipe, "output"));
        }
    }
}
