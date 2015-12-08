package modtweaker2.mods.tconstruct.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.crafting.PatternBuilder.ItemKey;

@ZenClass("mods.tconstruct.Tweaks")
public class TiCTweaks {

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Disabling for 0.5, will do this properly for 0.6 **/
	// Tweaks for enabling / disabling Patterns
	/*
	 * @ZenMethod public static void removePattern(IItemStack stack) {
	 * MineTweakerAPI.apply(new DisablePattern(toStack(stack))); }
	 * 
	 * private static class DisablePattern implements IUndoableAction { private
	 * String key; private MaterialSet set; private List list; private ItemStack
	 * stack; private final ItemStack disable;
	 * 
	 * public DisablePattern(ItemStack disable) { this.disable = disable; }
	 * 
	 * //Loops through the pattern mappings to find an entry with the same
	 * material name
	 * 
	 * @Override public void apply() { for (Entry<List, ItemStack> entry :
	 * TConstructRegistry.patternPartMapping.entrySet()) { ItemStack check =
	 * entry.getValue(); if (check.isItemEqual(disable)) { list =
	 * entry.getKey(); stack = entry.getValue(); break; } }
	 * 
	 * TConstructRegistry.patternPartMapping.remove(list); }
	 * 
	 * @Override public boolean canUndo() { return true; }
	 * 
	 * @Override public void undo() {
	 * TConstructRegistry.patternPartMapping.put(list, stack); }
	 * 
	 * @Override public String describe() { return
	 * "Disabling creation of the pattern for: " + disable.getDisplayName(); }
	 * 
	 * @Override public String describeUndo() { return
	 * "Enabling creation of the pattern for: " + disable.getDisplayName(); }
	 * 
	 * @Override public Object getOverrideKey() { return null; } }
	 */

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void addRepairMaterial(IItemStack stack, String material, int value) {
		ItemStack input = toStack(stack);
		MineTweakerAPI.apply(new Add(PatternBuilder.instance.new ItemKey(input.getItem(), input.getItemDamage(), value, material)));
	}

	// Tweaks for setting repair materials
	private static class Add extends BaseListAddition<ItemKey> {
		public Add(ItemKey recipe) {
			super("Repair Material", PatternBuilder.instance.materials);
			recipes.add(recipe);
		}

		@Override
		protected String getRecipeInfo(ItemKey recipe) {
		    return LogHelper.getStackDescription(new ItemStack(recipe.item, 1, recipe.damage));
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
	public static void removeRepairMaterial(IIngredient output, @Optional String material) {
	    
	    List<ItemKey> recipes = new LinkedList<ItemKey>();
	    
        for (ItemKey recipe : PatternBuilder.instance.materials) {
            IItemStack clone = toIItemStack(new ItemStack(recipe.item, 1, recipe.damage));
            if ((material != null && material.equalsIgnoreCase(recipe.key)) || (material == null)) {
                if (output.matches(clone)) {
                    recipes.add(recipe);
                }
            }
        }
	    
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        }
	}

	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class Remove extends BaseListRemoval<ItemKey> {
		public Remove(List<ItemKey> recipes) {
			super("Repair Material", PatternBuilder.instance.materials, recipes);
		}

		@Override
		protected String getRecipeInfo(ItemKey recipe) {
		    return LogHelper.getStackDescription(new ItemStack(recipe.item, 1, recipe.damage));
		}
	}
}
