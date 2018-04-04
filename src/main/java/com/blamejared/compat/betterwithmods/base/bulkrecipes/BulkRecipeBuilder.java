package com.blamejared.compat.betterwithmods.base.bulkrecipes;

import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import betterwithmods.util.StackIngredient;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.base.RemoveAll;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class BulkRecipeBuilder<T extends BulkRecipe> {

    protected List<Ingredient> inputs;
    protected List<ItemStack> outputs;
    protected int priority;
    private Supplier<CraftingManagerBulk<T>> registry;
    private String name;

    public BulkRecipeBuilder(Supplier<CraftingManagerBulk<T>> registry, String name) {
        this.registry = registry;
        this.name = name;
    }

    public static Ingredient getIngredient(IIngredient ingredient) {
        Ingredient i = CraftTweakerMC.getIngredient(ingredient);
        if (ingredient.getAmount() > 1) {
            return StackIngredient.fromIngredient(ingredient.getAmount(), i);
        }
        return i;
    }

    protected void addRecipe(T recipe) {
        ModTweaker.LATE_ADDITIONS.add(new BulkRecipeAdd<>(name, registry, recipe));
    }

    public abstract void build();

    public void _buildRecipe(IIngredient[] inputs, IItemStack[] outputs) {
        this.inputs = Arrays.stream(inputs).map(BulkRecipeBuilder::getIngredient).collect(Collectors.toList());
        this.outputs = InputHelper.toNonNullList(CraftTweakerMC.getItemStacks(outputs));
    }

    public void removeRecipe(IItemStack[] output) {
        ModTweaker.LATE_REMOVALS.add(new BulkRecipeRemove<>(name, registry, output));
    }

    @ZenMethod
    public void removeAll() {
        ModTweaker.LATE_REMOVALS.add(new RemoveAll<>(name, registry.get().getRecipes()));
    }
}
