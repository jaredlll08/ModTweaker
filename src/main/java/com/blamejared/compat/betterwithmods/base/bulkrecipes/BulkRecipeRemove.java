package com.blamejared.compat.betterwithmods.base.bulkrecipes;


import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.google.common.collect.Lists;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class BulkRecipeRemove<T extends BulkRecipe> extends BaseAction {

    private final List<ItemStack> outputs;
    private CraftingManagerBulk<T> manager;

    public BulkRecipeRemove(String name, Supplier<CraftingManagerBulk<T>> manager, IItemStack[] outputs) {
        this(name, manager.get(), Lists.newArrayList(InputHelper.toStacks(outputs)));
    }

    private BulkRecipeRemove(String name, CraftingManagerBulk<T> manager, List<ItemStack> outputs) {
        super(name);
        this.manager = manager;
        this.outputs = outputs;
    }

    @Override
    public void apply() {
        if (!manager.remove(outputs)) {
            LogHelper.logWarning(String.format("No recipes were removed for output %s", getRecipeInfo()));
        } else {
            LogHelper.logInfo(String.format("Successfully removed all recipes for %s", getRecipeInfo()));
        }
    }

    @Override
    protected String getRecipeInfo() {
        return String.format("%s - %s", name, outputs.stream().map(ItemStack::getDisplayName).collect(Collectors.joining(",")));
    }
}