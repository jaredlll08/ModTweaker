package modtweaker.mods.thaumcraft.handlers;

import static modtweaker.helpers.InputHelper.toStack;

import java.util.Arrays;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.thaumcraft.ThaumcraftHelper;
import modtweaker.util.BaseDescriptionAddition;
import modtweaker.util.BaseDescriptionRemoval;
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

    //Adds or sets Aspects
    private static class Add extends BaseDescriptionAddition {
        private final ItemStack stack;
        private final String aspects;
        private final boolean replace;
        private AspectList oldList;
        private AspectList newList;

        public Add(ItemStack stack, String aspects, boolean replace) {
            super("Aspects");
            this.stack = stack;
            this.aspects = aspects;
            this.replace = replace;
        }

        @Override
        public void apply() {
            oldList = ThaumcraftApiHelper.getObjectAspects(stack);
            if (!replace) newList = ThaumcraftHelper.parseAspects(oldList, aspects);
            else newList = ThaumcraftHelper.parseAspects(aspects);
            ThaumcraftApi.objectTags.put(Arrays.asList(stack.getItem(), stack.getItemDamage()), newList);
        }

        @Override
        public void undo() {
            if (oldList == null) {
                ThaumcraftApi.objectTags.remove(Arrays.asList(stack.getItem(), stack.getItemDamage()));
            } else ThaumcraftApi.objectTags.put(Arrays.asList(stack.getItem(), stack.getItemDamage()), oldList);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void remove(IItemStack stack, String aspects) {
        MineTweakerAPI.apply(new Remove(toStack(stack), aspects));
    }

    private static class Remove extends BaseDescriptionRemoval {
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
    public static void addForEntity(String entityName, String aspects) {
    	if(!EntityList.stringToClassMapping.containsKey(entityName))
    	{
    		MineTweakerAPI.getLogger().logError("No such entity "+entityName);
    		return;
    	}
        MineTweakerAPI.apply(new AddForEntity(entityName, aspects, false));
    }

    @ZenMethod
    public static void setForEntity(String entityName, String aspects) {
    	if(!EntityList.stringToClassMapping.containsKey(entityName))
    	{
    		MineTweakerAPI.getLogger().logError("No such entity "+entityName);
    		return;
    	}
        MineTweakerAPI.apply(new AddForEntity(entityName, aspects, true));
    }

    //Adds or sets Aspects
    private static class AddForEntity extends BaseDescriptionAddition {
        private final String entityName;
        private final String aspects;
        private final boolean replace;
        private AspectList oldList;
        private AspectList newList;

        public AddForEntity(String entityName, String aspects, boolean replace) {
            super("Aspects");
            this.entityName = entityName;
            this.aspects = aspects;
            this.replace = replace;
        }

        @Override
        public void apply() {
            oldList = ThaumcraftHelper.getEntityAspects(entityName);
            if (!replace) newList = ThaumcraftHelper.parseAspects(oldList, aspects);
            else newList = ThaumcraftHelper.parseAspects(aspects);
            ThaumcraftHelper.removeEntityAspects(entityName);
            ThaumcraftApi.registerEntityTag(entityName, newList);
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

    //////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeForEntity(String entityName, String aspects) {
    	if(!EntityList.stringToClassMapping.containsKey(entityName))
    	{
    		MineTweakerAPI.getLogger().logError("No such entity "+entityName);
    		return;
    	}
        MineTweakerAPI.apply(new RemoveForEntity(entityName, aspects));
    }

    private static class RemoveForEntity extends BaseDescriptionRemoval {
        private final String entityName;
        private final String aspects;
        private AspectList oldList;
        private AspectList newList;

        public RemoveForEntity(String entityName, String aspects) {
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
