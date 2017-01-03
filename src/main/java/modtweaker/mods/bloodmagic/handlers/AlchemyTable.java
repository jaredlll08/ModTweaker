package modtweaker.mods.bloodmagic.handlers;

import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.compat.jei.alchemyTable.AlchemyTableRecipeJEI;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import modtweaker.mods.bloodmagic.BloodMagicHelper;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.bloodmagic.AlchemyTable")
public class AlchemyTable {
	
	protected static final String name = "Blood Magic Alchemy Table";
	
	@ZenMethod
	public static void addRecipe(IItemStack output, int lpDrained, int ticksRequired, int tierRequired, IIngredient[] input) {
		if(output == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		} else if(lpDrained < 0) {
			LogHelper.logWarning(String.format("LP drained can't be below 0 (%d) for %s Recipe", lpDrained, name));
			return;
		} else if(ticksRequired < 0) {
			LogHelper.logWarning(String.format("Ticks required can't be below 0 (%d) for %s Recipe", ticksRequired, name));
			return;
		} else if(tierRequired < 1) {
			LogHelper.logWarning(String.format("Tier required can't be below 1 (%d) for %s Recipe", tierRequired, name));
			return;
		}
		
		MineTweakerAPI.apply(new Add(new AlchemyTableRecipe(toStack(output), lpDrained, ticksRequired, tierRequired, toObjects(input)), BloodMagicHelper.alchemyTableList));
	}
	
	private static class Add extends BaseListAddition<AlchemyTableRecipe> {
		
		public Add(AlchemyTableRecipe recipe, List<AlchemyTableRecipe> list) {
			super(AlchemyTable.name, list);
			this.recipes.add(recipe);
		}
		
		@Override
		public void apply() {
			if(recipes.isEmpty()) {
				return;
			}
			
			for(AlchemyTableRecipe recipe : recipes) {
				if(recipe != null) {
					if(list.add(recipe)) {
						successful.add(recipe);
						MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new AlchemyTableRecipeJEI(recipe));
					} else {
						LogHelper.logError(String.format("Error adding %s Recipe for %s", name, getRecipeInfo(recipe)));
					}
				} else {
					LogHelper.logError(String.format("Error adding %s Recipe: null object", name));
				}
			}
		}
		
		@Override
		public void undo() {
			if(this.successful.isEmpty()) {
				return;
			}
			
			for(AlchemyTableRecipe recipe : successful) {
				if(recipe != null) {
					if(!list.remove(recipe)) {
						LogHelper.logError(String.format("Error removing %s Recipe for %s", name, this.getRecipeInfo(recipe)));
					} else {
						MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new AlchemyTableRecipeJEI(recipe));
					}
				} else {
					LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
				}
			}
		}
		
		@Override
		public String getRecipeInfo(AlchemyTableRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getRecipeOutput(null));
		}
	}
	
	@ZenMethod
	public static void removeRecipe(IIngredient output) {
		remove(output, BloodMagicHelper.alchemyTableList);
	}
	
	public static void remove(IIngredient output, List<AlchemyTableRecipe> list) {
		if(output == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}
		
		List<AlchemyTableRecipe> recipes = new LinkedList<>();
		
		for(AlchemyTableRecipe recipe : list) {
			if(matches(output, toIItemStack(recipe.getRecipeOutput(null))))
				recipes.add(recipe);
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(list, recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", AlchemyTable.name, output.toString()));
		}
		
	}
	
	private static class Remove extends BaseListRemoval<AlchemyTableRecipe> {
		
		public Remove(List<AlchemyTableRecipe> list, List<AlchemyTableRecipe> recipes) {
			super(AlchemyTable.name, list, recipes);
		}
		
		@Override
		public void apply() {
			if (recipes.isEmpty()) {
				return;
			}
			for (AlchemyTableRecipe recipe : this.recipes) {
				if (recipe != null) {
					if (this.list.remove(recipe)) {
						
						successful.add(recipe);
						MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new AlchemyTableRecipeJEI(recipe));
					} else {
						LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
					}
				} else {
					LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
				}
			}
		}
		
		@Override
		public void undo() {
			if (successful.isEmpty()) {
				return;
			}
			for (AlchemyTableRecipe recipe : successful) {
				if (recipe != null) {
					if (!list.add(recipe)) {
						LogHelper.logError(String.format("Error restoring %s Recipe for %s", name, getRecipeInfo(recipe)));
					}else{
						MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new AlchemyTableRecipeJEI(recipe));
					}
				} else {
					LogHelper.logError(String.format("Error restoring %s Recipe: null object", name));
				}
			}
		}
		
		@Override
		protected String getRecipeInfo(AlchemyTableRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getRecipeOutput(null));
		}
	}
}
