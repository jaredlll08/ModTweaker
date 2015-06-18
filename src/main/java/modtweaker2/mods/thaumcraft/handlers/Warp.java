package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.utils.TweakerPlugin;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;

@ZenClass("mods.thaumcraft.Warp")
public class Warp {

	@ZenMethod
	public static void addToResearch(String key, int warp) {
			MineTweakerAPI.apply(new Add(key, warp));
	}

	@ZenMethod
	public static void addToItem(IItemStack stack, int warp) {
			MineTweakerAPI.apply(new Add(toStack(stack), warp));
	}

	private static class Add implements IUndoableAction {
		Object target;
		int warp;

		public Add(Object targ, int amount) {
			target = targ;
			warp = amount;
		}

		public void apply() {
			if (target instanceof String)
				ThaumcraftApi.addWarpToResearch((String) target, warp);
			else if (target instanceof ItemStack)
				ThaumcraftApi.addWarpToItem((ItemStack) target, warp);
		}

		public boolean canUndo() {
			return ThaumcraftApi.getWarp(target) > 0;
		}

		public String describe() {
			String desc = "Adding " + warp + " warp to ";
			if (target instanceof String)
				desc += (String) target;
			else if (target instanceof ItemStack)
				desc += ((ItemStack) target).getDisplayName();
			return desc;
		}

		public void undo() {
			if (target instanceof String)
				ThaumcraftHelper.warpList.remove(target);
			else if (target instanceof ItemStack)
				ThaumcraftHelper.warpList.remove(Arrays.asList(((ItemStack) target).getItem(), ((ItemStack) target).getItemDamage()));
		}

		public String describeUndo() {
			String desc = "Removing warp from ";
			if (target instanceof String)
				desc += (String) target;
			else if (target instanceof ItemStack)
				desc += ((ItemStack) target).getDisplayName();
			return desc;
		}

		public Object getOverrideKey() {
			return null;
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeFromResearch(String key) {
			MineTweakerAPI.apply(new Remove(key));
	}

	@ZenMethod
	public static void removeFromItem(IItemStack stack) {
			MineTweakerAPI.apply(new Remove(toStack(stack)));
	}

	@ZenMethod
	public static void removeAll() {
			MineTweakerAPI.apply(new MassRemove(RemoveType.BOTH));
	}

	@ZenMethod
	public static void removeAllResearch() {
			MineTweakerAPI.apply(new MassRemove(RemoveType.RESEARCH));
	}

	@ZenMethod
	public static void removeAllItems() {
			MineTweakerAPI.apply(new MassRemove(RemoveType.ITEMS));
	}

	private static class Remove implements IUndoableAction {
		Object target;
		int warp;

		public Remove(Object targ) {
			target = targ;
		}

		public void apply() {
			if (target instanceof String)
				warp = ThaumcraftHelper.warpList.remove(target);
			else if (target instanceof ItemStack)
				warp = ThaumcraftHelper.warpList.remove(Arrays.asList(((ItemStack) target).getItem(), ((ItemStack) target).getItemDamage()));
		}

		public boolean canUndo() {
			return warp > 0;
		}

		public String describe() {
			String desc = "Removing warp from ";
			if (target instanceof String)
				desc += (String) target;
			else if (target instanceof ItemStack)
				desc += ((ItemStack) target).getDisplayName();
			return desc;
		}

		public void undo() {
			if (target instanceof String)
				ThaumcraftApi.addWarpToResearch((String) target, warp);
			else if (target instanceof ItemStack)
				ThaumcraftApi.addWarpToItem((ItemStack) target, warp);
		}

		public String describeUndo() {
			String desc = "Restoring " + warp + " warp to ";
			if (target instanceof String)
				desc += (String) target;
			else if (target instanceof ItemStack)
				desc += ((ItemStack) target).getDisplayName();
			return desc;
		}

		public Object getOverrideKey() {
			return null;
		}

	}

	public static enum RemoveType {
		RESEARCH, ITEMS, BOTH
	}

	private static class MassRemove implements IUndoableAction {
		HashMap<Object, Integer> oldMap = new HashMap<Object, Integer>();
		RemoveType type;

		public MassRemove(RemoveType typ) {
			type = typ;
		}

		public void apply() {
			if (type == RemoveType.BOTH) {
				for (Object key : ThaumcraftHelper.warpList.keySet()) {
					oldMap.put(key, ThaumcraftHelper.warpList.get(key));
				}
				ThaumcraftHelper.warpList.clear();
			} else if (type == RemoveType.ITEMS) {
				for (Object key : ThaumcraftHelper.warpList.keySet()) {
					if (key instanceof List) {
						oldMap.put(key, ThaumcraftHelper.warpList.get(key));
					}
				}
				for (Object key : oldMap.keySet()) {
					ThaumcraftHelper.warpList.remove(key);
				}
			} else if (type == RemoveType.RESEARCH) {
				for (Object key : ThaumcraftHelper.warpList.keySet()) {
					if (key instanceof String) {
						oldMap.put(key, ThaumcraftHelper.warpList.get(key));
					}
				}
				for (Object key : oldMap.keySet()) {
					ThaumcraftHelper.warpList.remove(key);
				}
			}
		}

		public boolean canUndo() {
			return oldMap.size() > 0;
		}

		public String describe() {
			if (type == RemoveType.RESEARCH)
				return "Clearing All Research Warp";
			else if (type == RemoveType.ITEMS)
				return "Clearing All Item Warp";
			else
				return "Clearing All Warp";
		}

		public void undo() {
			for (Object key : oldMap.keySet()) {
				ThaumcraftHelper.warpList.put(key, oldMap.get(key));
			}
		}

		public String describeUndo() {
			if (type == RemoveType.RESEARCH)
				return "Restoring All Research Warp";
			else if (type == RemoveType.ITEMS)
				return "Restoring All Item Warp";
			else
				return "Restoring All Warp";
		}

		public Object getOverrideKey() {
			return null;
		}

	}
}
