package modtweaker.mods.tconstruct.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.ReflectionHelper;
import modtweaker.mods.tconstruct.TConstructHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import modtweaker.util.BaseSetVar;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.crafting.PatternBuilder.ItemKey;
import tconstruct.library.tools.ToolCore;
import tconstruct.tools.TinkerTools;

@ZenClass("mods.tconstruct.Tweaks")
public class TiCTweaks {
	// Set the maximum RF
	@ZenMethod
	public static void setRFCapacity(String tool, int capacity) {
		MineTweakerAPI.apply(new AdjustRF(tool, capacity));
	}

	private static class AdjustRF extends BaseSetVar {
		public AdjustRF(String tool, int newValue) {
			super("RF Maximum for Tinkers Tools", ToolCore.class, ReflectionHelper.getStaticObject(TinkerTools.class, tool), "capacity", 400000, newValue);
		}
	}

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
		MineTweakerAPI.apply(new Add(TConstructHelper.getItemKey(input.getItem(), input.getItemDamage(), value, material)));
	}

	// Tweaks for setting repair materials
	private static class Add extends BaseListAddition {
		public Add(ItemKey recipe) {
			super("Repair Material", PatternBuilder.instance.materials, recipe);
		}

		@Override
		public String getRecipeInfo() {
			return new ItemStack(((ItemKey) recipe).item, 1, ((ItemKey) recipe).damage).getDisplayName();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@ZenMethod
	public static void removeRepairMaterial(IItemStack output, @Optional String material) {
		MineTweakerAPI.apply(new Remove(toStack(output), material));
	}

	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class Remove extends BaseListRemoval {
		private final String material;

		public Remove(ItemStack stack, String material) {
			super("Repair Material", PatternBuilder.instance.materials, stack);
			this.material = material;
		}

		// Loops through the registry, to find the item that matches, saves that
		// recipe then removes it
		@Override
		public void apply() {
			for (ItemKey r : PatternBuilder.instance.materials) {
				ItemStack clone = new ItemStack(r.item, 1, r.damage);
				if ((material != null && material.equalsIgnoreCase(r.key)) || (material == null)) {
					if (clone.isItemEqual(stack)) {
						recipe = r;
						break;
					}
				}
			}

			PatternBuilder.instance.materials.remove(recipe);
		}

		@Override
		public String getRecipeInfo() {
			return stack.getDisplayName();
		}
	}
}
