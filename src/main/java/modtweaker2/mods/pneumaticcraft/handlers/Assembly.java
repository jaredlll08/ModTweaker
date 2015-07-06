package modtweaker2.mods.pneumaticcraft.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import pneumaticCraft.api.recipe.AssemblyRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.pneumaticcraft.Assembly")
public class Assembly {
    
    public static final String name = "PneumaticCraft Assembly";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addDrillRecipe(IItemStack input, IItemStack output) {
        addRecipe(input, output, AssemblyRecipe.drillRecipes);
    }

    @ZenMethod
    public static void addLaserRecipe(IItemStack input, IItemStack output) {
        addRecipe(input, output, AssemblyRecipe.laserRecipes);
    }

    @ZenMethod
    public static void addLaserDrillRecipe(IItemStack input, IItemStack output) {
        addRecipe(input, output, AssemblyRecipe.drillLaserRecipes);
    }
    
    public static void addRecipe(IItemStack input, IItemStack output, List<AssemblyRecipe> list) {
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        ItemStack in = toStack(input);
        
        for(AssemblyRecipe recipe : list) {
            if(areEqual(recipe.getInput(), in)) {
                LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", name, InputHelper.getStackDescription(in)));
                return;
            }
        }
        
        MineTweakerAPI.apply(new Add(new AssemblyRecipe(toStack(input), toStack(output)), list));
    }

    private static class Add extends BaseListAddition<AssemblyRecipe> {
        public Add(AssemblyRecipe recipe, List<AssemblyRecipe> list) {
            super(Assembly.name, list);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(AssemblyRecipe recipe) {
            return InputHelper.getStackDescription(recipe.getOutput());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeDrillRecipe(IIngredient output) {
        removeRecipe(AssemblyRecipe.drillRecipes, output);
    }

    @ZenMethod
    public static void removeLaserRecipe(IIngredient output) {
        removeRecipe(AssemblyRecipe.laserRecipes, output);
    }

    @ZenMethod
    public static void removeLaserDrillRecipe(IIngredient output) {
        removeRecipe(AssemblyRecipe.drillLaserRecipes, output);
    }
    
    public static void removeRecipe(List<AssemblyRecipe> list, IIngredient output) {
        List<AssemblyRecipe> recipes = new LinkedList<AssemblyRecipe>();
        
        for (AssemblyRecipe r : list) {
            if (r.getOutput() != null && matches(output, toIItemStack(r.getOutput()))) {
                recipes.add(r);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(list, recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", name, output.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<AssemblyRecipe> {
        public Remove(List<AssemblyRecipe> list, List<AssemblyRecipe> recipes) {
            super(Assembly.name, list, recipes);
        }

        @Override
        public String getRecipeInfo(AssemblyRecipe recipe) {
            return InputHelper.getStackDescription(recipe.getOutput());
        }
    }
}
