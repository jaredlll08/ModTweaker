package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.TransposerManager;
import cofh.thermalexpansion.util.crafting.TransposerManager.RecipeTransposer;

@ZenClass("mods.thermalexpansion.Transposer")
public class Transposer {
    
    public static final String nameFill = "Thermal Expansion Transposer (Fill)";
    public static final String nameExtract = "Thermal Expansion Transposer (Extract)";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addFillRecipe(int energy, IItemStack input, IItemStack output, ILiquidStack liquid) {
        if(input == null || output == null || liquid == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameFill));
            return;
        }
	    
        if(TransposerManager.fillRecipeExists(toStack(input), toFluid(liquid))) {
            LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s and %s. Command ignored!", Transposer.nameFill, InputHelper.getStackDescription(toStack(input)), InputHelper.getStackDescription(toFluid(liquid))));
            return;
        }
        
        RecipeTransposer recipe = ReflectionHelper.getInstance(ThermalHelper.transposerRecipe, toStack(input), toStack(output), toFluid(liquid), energy, 100);
        
        if(recipe != null) {
            MineTweakerAPI.apply(new Add(recipe, RecipeType.Fill));
        } else {
            LogHelper.logError(String.format("Error while creating instance for %s recipe.", nameFill));
        }
	}

	@ZenMethod
	public static void addExtractRecipe(int energy, IItemStack input, IItemStack output, ILiquidStack liquid, int chance) {
        if(input == null || output == null || liquid == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameExtract));
            return;
        }
        
        if(TransposerManager.extractionRecipeExists(toStack(input), toFluid(liquid))) {
            LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s and %s. Command ignored!", Transposer.nameExtract, InputHelper.getStackDescription(toStack(input)), InputHelper.getStackDescription(toFluid(liquid))));
            return;
        }
        
        RecipeTransposer recipe = ReflectionHelper.getInstance(ThermalHelper.transposerRecipe, toStack(input), toStack(output), toFluid(liquid), energy, 100);
        
        if(recipe != null) {
            MineTweakerAPI.apply(new Add(recipe, RecipeType.Extract));
        } else {
            LogHelper.logError(String.format("Error while creating instance for %s recipe.", nameExtract));
        }
	}
	
	private static class Add extends BaseListAddition<RecipeTransposer> {
	    private RecipeType type;

        protected Add(RecipeTransposer recipe, RecipeType type) {
            super(type == RecipeType.Fill ? Transposer.nameFill : Transposer.nameExtract, null);
            this.type = type;
            recipes.add(recipe);
        }

        @Override
        public void apply() {
            for(RecipeTransposer recipe : recipes) {
                boolean applied = false;
                switch(type) {
                    case Fill:
                        applied = TransposerManager.addFillRecipe(
                                recipe.getEnergy(),
                                recipe.getInput(),
                                recipe.getOutput(),
                                recipe.getFluid(),
                                false);
                        break;
                        
                    case Extract:
                        applied = TransposerManager.addExtractionRecipe(
                                recipe.getEnergy(),
                                recipe.getInput(), 
                                recipe.getOutput(),
                                recipe.getFluid(),
                                recipe.getChance(),
                                false);
                        break;
                }

                if(applied) {
                    successful.add(recipe);
                }
            }
        }
        
        @Override
        public void undo() {
            for(RecipeTransposer recipe : successful) {
                switch(type)
                {
                    case Fill:
                        TransposerManager.removeFillRecipe(recipe.getInput(), recipe.getFluid());
                        break;
                    case Extract:
                        TransposerManager.removeExtractionRecipe(recipe.getInput());
                        break;
                }
            }
        }
        
        @Override
        protected boolean equals(RecipeTransposer recipe, RecipeTransposer otherRecipe) {
            return ThermalHelper.equals(recipe, otherRecipe);
        }
        
        @Override
        protected String getRecipeInfo(RecipeTransposer recipe) {
            return InputHelper.getStackDescription(recipe.getInput());
        }
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
	public static void removeFillRecipe(IIngredient input, IIngredient liquid) {
	    removeRecipe(input, liquid, RecipeType.Fill);
	}

	@ZenMethod
	public static void removeExtractRecipe(IIngredient input) {
	    removeRecipe(input, IngredientAny.INSTANCE, RecipeType.Extract);
	}

	public static void removeRecipe(IIngredient input, IIngredient liquid, RecipeType type) {
        List<RecipeTransposer> recipes = new LinkedList<RecipeTransposer>();
        
        for(RecipeTransposer recipe : type == RecipeType.Fill ? TransposerManager.getFillRecipeList() : TransposerManager.getExtractionRecipeList()) {
            if(recipe != null && matches(input, toIItemStack(recipe.getInput())) && matches(liquid, toILiquidStack(recipe.getFluid()))) {
                recipes.add(recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes, type));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s and %s.", type == RecipeType.Fill ? Transposer.nameFill : Transposer.nameExtract, input.toString(), liquid.toString()));
        }
	}

	private static class Remove extends BaseListRemoval<RecipeTransposer> {
	    private RecipeType type;
	    
        protected Remove(List<RecipeTransposer> recipes, RecipeType type) {
            super(type == RecipeType.Fill ? Transposer.nameFill : Transposer.nameExtract, null, recipes);
            this.type = type;
        }
        
        @Override
        public void apply() {
            for(RecipeTransposer recipe : recipes) {
                boolean removed = false;
                switch(type) {
                    case Fill:
                        removed = TransposerManager.removeFillRecipe(recipe.getInput(), recipe.getFluid());
                        break;
                    case Extract:
                        removed = TransposerManager.removeExtractionRecipe(recipe.getInput());
                        break;
                }
                
                if(removed) {
                    successful.add(recipe);
                }
            }
        }
        
        @Override
        public void undo() {
            for(RecipeTransposer recipe : successful) {
                switch(type) {
                case Fill:
                    TransposerManager.addFillRecipe(
                            recipe.getEnergy(),
                            recipe.getInput(),
                            recipe.getOutput(),
                            recipe.getFluid(),
                            false);
                    break;
                    
                case Extract:
                    TransposerManager.addExtractionRecipe(
                            recipe.getEnergy(),
                            recipe.getInput(), 
                            recipe.getOutput(),
                            recipe.getFluid(),
                            recipe.getChance(),
                            false);
                    break;
                }
            }
        }

        @Override
        protected boolean equals(RecipeTransposer recipe, RecipeTransposer otherRecipe) {
            return ThermalHelper.equals(recipe, otherRecipe);
        }
        
        @Override
        protected String getRecipeInfo(RecipeTransposer recipe) {
            return InputHelper.getStackDescription(recipe.getOutput());
        }
	}
    
    protected enum RecipeType {
        Fill,
        Extract
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   

    @ZenMethod
    public static void refreshRecipes() {
        MineTweakerAPI.apply(new Refresh());
    }

    private static class Refresh implements IUndoableAction {

        public void apply() {
            TransposerManager.refreshRecipes();
        }

        public boolean canUndo() {
            return true;
        }

        public String describe() {
            return "Refreshing " + Transposer.nameFill + " & " + Transposer.nameExtract + " recipes";
        }

        public void undo() {
        }

        public String describeUndo() {
            return "Ignoring undo of " + Transposer.nameFill + " & " + Transposer.nameExtract + " recipe refresh";
        }

        public Object getOverrideKey() {
            return null;
        }
    }
}
