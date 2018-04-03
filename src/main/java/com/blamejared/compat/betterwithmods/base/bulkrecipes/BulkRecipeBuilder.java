package com.blamejared.compat.betterwithmods.base.bulkrecipes;

import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BulkRecipeBuilder<T extends BulkRecipe> {

    protected List<Ingredient> inputs;
    protected List<ItemStack> outputs;
    protected int priority;
    private CraftingManagerBulk<T> registry;
    private String name;

    public BulkRecipeBuilder(CraftingManagerBulk<T> registry, String name) {
        this.registry = registry;
        this.name = name;
    }

    protected void addRecipe(T recipe) {
        ModTweaker.LATE_ADDITIONS.add(new BulkRecipeAdd<>(name, registry, recipe));
    }

    @ZenMethod
    public abstract void build();

    @ZenMethod
    public BulkRecipeBuilder<T> buildRecipe(IIngredient[] inputs, IItemStack[] outputs) {
        this.inputs = Arrays.stream(inputs).map(CraftTweakerMC::getIngredient).collect(Collectors.toList());
        this.outputs = InputHelper.toNonNullList(CraftTweakerMC.getItemStacks(outputs));
        return this;
    }

    @ZenMethod
    public BulkRecipeBuilder<T> setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public void removeRecipe(IItemStack[] output) {
        ModTweaker.LATE_REMOVALS.add(new BulkRecipeRemove<>(name, registry, output));
    }


}
