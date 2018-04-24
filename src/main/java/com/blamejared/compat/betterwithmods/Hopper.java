package com.blamejared.compat.betterwithmods;

import betterwithmods.api.tile.IHopperFilter;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.registry.HopperFilter;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.module.gameplay.HopperRecipes;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.google.common.collect.Lists;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ZenClass("mods.betterwithmods.FilteredHopper")
@ModOnly("betterwithmods")
@ZenRegister
public class Hopper {
    @ZenMethod
    public static void addFilter(String name, IIngredient filter) {
        ModTweaker.LATE_ADDITIONS.add(new AddFilter(name,CraftTweakerMC.getIngredient(filter)));
    }

    @ZenMethod
    public static void addFilteredItem(String name, IIngredient item) {
        addFilteredItem(name, new IIngredient[]{item});
    }

    @ZenMethod
    public static void addFilteredItem(String name, IIngredient[] items) {
        ModTweaker.LATE_ADDITIONS.add(new AddFilterItem(name,Arrays.stream(items).map(CraftTweakerMC::getIngredient).collect(Collectors.toList())));
    }

    @ZenMethod
    public static void addFilterRecipe(String name, IIngredient input, IItemStack[] outputs, IItemStack[] secondary) {
        ModTweaker.LATE_ADDITIONS.add(new AddHopperRecipe(name,
                CraftTweakerMC.getIngredient(input),
                Lists.newArrayList(CraftTweakerMC.getItemStacks(outputs)),
                Lists.newArrayList(CraftTweakerMC.getItemStacks(secondary))));
    }

    @ZenMethod
    public static void addSoulUrnRecipe(IIngredient input, IItemStack[] outputs, IItemStack[] secondary) {
        ModTweaker.LATE_ADDITIONS.add(new AddSoulUrnRecipe(CraftTweakerMC.getIngredient(input),
                Lists.newArrayList(CraftTweakerMC.getItemStacks(outputs)),
                Lists.newArrayList(CraftTweakerMC.getItemStacks(secondary))));
    }

    @ZenMethod
    public static void clearFilter(String name) {
        ModTweaker.LATE_REMOVALS.add(new ClearFilter(name));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack[] outputs, IItemStack[] secondary) {
        ModTweaker.LATE_REMOVALS.add(new RemoveHopperRecipe(Lists.newArrayList(CraftTweakerMC.getItemStacks(outputs)),
                Lists.newArrayList(CraftTweakerMC.getItemStacks(secondary))));
    }

    @ZenMethod
    public static void removeRecipeByInput(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new RemoveHopperRecipeByInput(CraftTweakerMC.getItemStack(input)));
    }

    @ZenMethod
    public static void removeAll() {
        ModTweaker.LATE_REMOVALS.add(new RemoveAll());
    }


    public static class AddFilter extends BaseAction {
        Ingredient ingredient;
        String filterName;

        protected AddFilter(String filterName, Ingredient ingredient) {
            super("Filtered Hopper");
            this.filterName = filterName;
            this.ingredient = ingredient;
        }

        @Override
        protected String getRecipeInfo() {
            return filterName+" ("+Arrays.toString(ingredient.getMatchingStacks())+")";
        }

        @Override
        public void apply() {
            if(BWRegistry.HOPPER_FILTERS.getFilter(filterName) != HopperFilter.NONE)
                LogHelper.logWarning(String.format("Hopper Filter %s couldn't be added: Filter already exists", getRecipeInfo()));
            else {
                BWRegistry.HOPPER_FILTERS.addFilter(new HopperFilter(filterName, ingredient, Lists.newArrayList()));
                LogHelper.logInfo(String.format("Successfully added Hopper Filter %s", getRecipeInfo()));
            }
        }
    }

    public static class ClearFilter extends BaseAction {
        String filterName;

        protected ClearFilter(String filterName) {
            super("Filtered Hopper");
            this.filterName = filterName;
        }

        @Override
        protected String getRecipeInfo() {
            return filterName;
        }

        @Override
        public void apply() {
            IHopperFilter filter = BWRegistry.HOPPER_FILTERS.getFilter(filterName);
            if(filter == HopperFilter.NONE || !(filter instanceof HopperFilter))
                LogHelper.logWarning(String.format("Hopper Filter %s couldn't be cleared: Filter doesn't exist or is not a modifiable filter.", getRecipeInfo()));
            else {
                ((HopperFilter) filter).getFiltered().clear();
                LogHelper.logInfo(String.format("Successfully cleared Hopper Filter %s", getRecipeInfo()));
            }
        }
    }

    public static class AddFilterItem extends BaseAction {
        String filterName;
        List<Ingredient> items;

        protected AddFilterItem(String filterName, List<Ingredient> item) {
            super("Filtered Hopper");
            this.filterName = filterName;
            this.items = item;
        }

        @Override
        protected String getRecipeInfo() {
            return String.format("%s - %s", items.stream().map(ingredient -> Arrays.toString(ingredient.getMatchingStacks())).collect(Collectors.joining(",")), filterName);
        }

        @Override
        public void apply() {
            IHopperFilter filter = BWRegistry.HOPPER_FILTERS.getFilter(filterName);
            if(filter == HopperFilter.NONE || !(filter instanceof HopperFilter))
                LogHelper.logWarning(String.format("Filtered items %s couldn't be added: Filter doesn't exist or is not a modifiable filter.", getRecipeInfo()));
            else {
                ((HopperFilter) filter).getFiltered().addAll(items);
                LogHelper.logInfo(String.format("Successfully added items %s", getRecipeInfo()));
            }
        }
    }

    public static class AddHopperRecipe extends BaseAction {
        String filterName;
        Ingredient input;
        List<ItemStack> outputs;
        List<ItemStack> secondary;

        public AddHopperRecipe(String filterName, Ingredient input, List<ItemStack> outputs, List<ItemStack> secondary) {
            super("Filtered Hopper");
            this.filterName = filterName;
            this.input = input;
            this.outputs = outputs;
            this.secondary = secondary;
        }

        @Override
        protected String getRecipeInfo() {
            return String.format("%s -> %s,%s in %s",
                    Arrays.toString(input.getMatchingStacks()),
                    outputs.stream().map(ItemStack::getDisplayName).collect(Collectors.joining(",")),
                    secondary.stream().map(ItemStack::getDisplayName).collect(Collectors.joining(",")),
                    filterName);
        }

        @Override
        public void apply() {
            HopperInteractions.addHopperRecipe(new HopperInteractions.HopperRecipe(filterName,input,outputs,secondary));
        }
    }

    public static class AddSoulUrnRecipe extends BaseAction {
        Ingredient input;
        List<ItemStack> outputs;
        List<ItemStack> secondary;

        public AddSoulUrnRecipe(Ingredient input, List<ItemStack> outputs, List<ItemStack> secondary) {
            super("Filtered Hopper");
            this.input = input;
            this.outputs = outputs;
            this.secondary = secondary;
        }

        @Override
        protected String getRecipeInfo() {
            return String.format("%s -> %s,%s (soul urn)",
                    Arrays.toString(input.getMatchingStacks()),
                    outputs.stream().map(ItemStack::getDisplayName).collect(Collectors.joining(",")),
                    secondary.stream().map(ItemStack::getDisplayName).collect(Collectors.joining(",")));
        }

        @Override
        public void apply() {
            HopperInteractions.addHopperRecipe(new HopperInteractions.SoulUrnRecipe(input,outputs,secondary));
        }
    }

    public static class RemoveHopperRecipe extends BaseAction {
        List<ItemStack> outputs;
        List<ItemStack> secondary;

        public RemoveHopperRecipe(List<ItemStack> outputs, List<ItemStack> secondary) {
            super("Filtered Hopper");
            this.outputs = outputs;
            this.secondary = secondary;
        }

        @Override
        protected String getRecipeInfo() {
            return String.format("%s,%s",
                    outputs.stream().map(ItemStack::getDisplayName).collect(Collectors.joining(",")),
                    secondary.stream().map(ItemStack::getDisplayName).collect(Collectors.joining(",")));
        }

        @Override
        public void apply() {
            if (!HopperInteractions.remove(outputs,secondary)) {
                LogHelper.logWarning(String.format("No recipes were removed for outputs %s", getRecipeInfo()));
            } else {
                LogHelper.logInfo(String.format("Successfully removed all recipes for %s", getRecipeInfo()));
            }
        }
    }

    public static class RemoveHopperRecipeByInput extends BaseAction {
        ItemStack input;

        public RemoveHopperRecipeByInput(ItemStack input) {
            super("Filtered Hopper");
            this.input = input;
        }

        @Override
        protected String getRecipeInfo() {
            return input.getDisplayName();
        }

        @Override
        public void apply() {
            if (!HopperInteractions.removeByInput(input)) {
                LogHelper.logWarning(String.format("No recipes were removed for input %s", getRecipeInfo()));
            } else {
                LogHelper.logInfo(String.format("Successfully removed all recipes for %s", getRecipeInfo()));
            }
        }
    }

    public static class RemoveAll extends BaseAction {
        public RemoveAll() {
            super("Filtered Hopper");
        }

        @Override
        public void apply() {
            HopperInteractions.RECIPES.clear();
        }
    }

}
