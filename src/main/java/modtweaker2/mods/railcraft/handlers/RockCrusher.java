package modtweaker2.mods.railcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import mods.railcraft.api.crafting.IRockCrusherRecipe;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.RockCrusher")
public class RockCrusher {
    
    public static final String name = "Railcraft Rock Crusher";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, IItemStack[] output, double[] chances) {
        IRockCrusherRecipe recipe = RailcraftHelper.getRockCrusherRecipe(toStack(input), matchDamage, matchNBT);
        
        for (int i = 0; i < output.length; i++) {
            recipe.addOutput(toStack(output[i]), (float) chances[i]);
        }
        
        MineTweakerAPI.apply(new Add(recipe));
    }

    private static class Add extends BaseListAddition<IRockCrusherRecipe> {
        
        @SuppressWarnings("unchecked")
        public Add(IRockCrusherRecipe recipe) {
            super(RockCrusher.name, (List<IRockCrusherRecipe>)RailcraftHelper.crusher);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(IRockCrusherRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getInput());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient input) {
        List<IRockCrusherRecipe> recipes = new LinkedList<IRockCrusherRecipe>();
        
        for (IRockCrusherRecipe r : RailcraftHelper.crusher) {
            if (r.getInput() != null && matches(input, toIItemStack(r.getInput()))) {
                recipes.add(r);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", RockCrusher.name, input.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<IRockCrusherRecipe> {
        
        @SuppressWarnings("unchecked")
        public Remove(List<IRockCrusherRecipe> recipes) {
            super("Rock Crusher", (List<IRockCrusherRecipe>)RailcraftHelper.crusher, recipes);
        }

        @Override
        public String getRecipeInfo(IRockCrusherRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getInput());
        }
    }
}
