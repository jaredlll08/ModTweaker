package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.Arrays;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.utils.BaseUndoable;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;

@ZenClass("mods.thaumcraft.Aspects")
public class Aspects {
	/** Add/Remove/Set Aspects for items **/
	@ZenMethod
	public static void add(IItemStack stack, String aspects) {
		
			MineTweakerAPI.apply(new Add(toStack(stack), aspects, false));
	}

	@ZenMethod
	public static void set(IItemStack stack, String aspects) {
		
			MineTweakerAPI.apply(new Add(toStack(stack), aspects, true));
	}

	// Adds or sets Aspects
	private static class Add extends BaseUndoable {
		private final ItemStack stack;
		private final String aspects;
		private final boolean replace;
		private AspectList oldList;

		public Add(ItemStack stack, String aspects, boolean replace) {
			super("Aspects");
			this.stack = stack;
			this.aspects = aspects;
			this.replace = replace;
		}

		@Override
		public void apply() {
			AspectList newList;
			if (replace) {
				newList = ThaumcraftHelper.parseAspects(aspects);
			} else {
				oldList = ThaumcraftApiHelper.getObjectAspects(stack);
				newList = ThaumcraftHelper.parseAspects(oldList, aspects);
			}

			ThaumcraftApi.objectTags.put(Arrays.asList(stack.getItem(), stack.getItemDamage()), newList);
			
			success = true;
		}

		@Override
		public boolean canUndo() {
			return !replace;
		}

		@Override
		public void undo() {
			if (replace)
				return;
			if (oldList == null) {
				ThaumcraftApi.objectTags.remove(Arrays.asList(stack.getItem(), stack.getItemDamage()));
			} else
				ThaumcraftApi.objectTags.put(Arrays.asList(stack.getItem(), stack.getItemDamage()), oldList);
		}

		@Override
		public String getRecipeInfo() {
			return stack.getDisplayName();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void remove(IItemStack stack, String aspects) {
		
			MineTweakerAPI.apply(new Remove(toStack(stack), aspects));
	}

	private static class Remove extends BaseUndoable {
		private final ItemStack stack;
		private final String aspects;
		private AspectList oldList;
		private AspectList newList;

		public Remove(ItemStack stack, String aspects) {
			super("Aspects");
			this.stack = stack;
			this.aspects = aspects;
		}

		@Override
		public void apply() {
			oldList = ThaumcraftApiHelper.getObjectAspects(stack);
			if (oldList != null) {
				newList = ThaumcraftHelper.removeAspects(oldList, aspects);
				ThaumcraftApi.objectTags.put(Arrays.asList(stack.getItem(), stack.getItemDamage()), newList);
			}
			
			success = true;
		}

		@Override
		public boolean canUndo() {
			return oldList != null;
		}

		@Override
		public void undo() {
			ThaumcraftApi.objectTags.put(Arrays.asList(stack.getItem(), stack.getItemDamage()), oldList);
		}

		@Override
		public String getRecipeInfo() {
			return stack.getDisplayName();
		}
	}

	/** Add/Remove/Set Aspects for entities **/
	@ZenMethod
	public static void addEntity(String entityName, String aspects) {
		if (!EntityList.stringToClassMapping.containsKey(entityName)) {
			MineTweakerAPI.getLogger().logError("No such entity " + entityName);
			return;
		}
		
			MineTweakerAPI.apply(new AddEntity(entityName, aspects, false));
	}

	@ZenMethod
	public static void setEntity(String entityName, String aspects) {
		if (!EntityList.stringToClassMapping.containsKey(entityName)) {
			MineTweakerAPI.getLogger().logError("No such entity " + entityName);
			return;
		}
		
			MineTweakerAPI.apply(new AddEntity(entityName, aspects, true));
	}

	// Adds or sets Aspects
	private static class AddEntity extends BaseUndoable {
		private final String entityName;
		private final String aspects;
		private final boolean replace;
		private AspectList oldList;
		private AspectList newList;

		public AddEntity(String entityName, String aspects, boolean replace) {
			super("Aspects");
			this.entityName = entityName;
			this.aspects = aspects;
			this.replace = replace;
		}

		@Override
		public void apply() {
			oldList = ThaumcraftHelper.getEntityAspects(entityName);
			if (!replace)
				newList = ThaumcraftHelper.parseAspects(oldList, aspects);
			else
				newList = ThaumcraftHelper.parseAspects(aspects);
			ThaumcraftHelper.removeEntityAspects(entityName);
			ThaumcraftApi.registerEntityTag(entityName, newList);
			
			success = true;
		}

		@Override
		public void undo() {
			ThaumcraftHelper.removeEntityAspects(entityName);
			if (oldList != null)
				ThaumcraftApi.registerEntityTag(entityName, oldList);
		}

		@Override
		public String getRecipeInfo() {
			return entityName;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeEntity(String entityName, String aspects) {
		if (!EntityList.stringToClassMapping.containsKey(entityName)) {
			MineTweakerAPI.getLogger().logError("No such entity " + entityName);
			return;
		}
		
			MineTweakerAPI.apply(new RemoveEntity(entityName, aspects));
	}

	private static class RemoveEntity extends BaseUndoable {
		private final String entityName;
		private final String aspects;
		private AspectList oldList;
		private AspectList newList;

		public RemoveEntity(String entityName, String aspects) {
			super("Aspects");
			this.entityName = entityName;
			this.aspects = aspects;
		}

		@Override
		public void apply() {
			oldList = ThaumcraftHelper.getEntityAspects(entityName);
			if (oldList != null) {
				newList = ThaumcraftHelper.removeAspects(oldList, aspects);
				ThaumcraftHelper.removeEntityAspects(entityName);
				ThaumcraftApi.registerEntityTag(entityName, newList);
			}
			
			success = true;
		}

		@Override
		public boolean canUndo() {
			return oldList != null;
		}

		@Override
		public void undo() {
			ThaumcraftHelper.removeEntityAspects(entityName);
			ThaumcraftApi.registerEntityTag(entityName, oldList);
		}

		@Override
		public String getRecipeInfo() {
			return entityName;
		}
	}

}
