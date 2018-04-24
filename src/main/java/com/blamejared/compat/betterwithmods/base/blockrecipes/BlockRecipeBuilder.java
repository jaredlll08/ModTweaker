package com.blamejared.compat.betterwithmods.base.blockrecipes;

import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.BlockDropIngredient;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.block.recipe.BlockRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.base.RemoveAll;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;
import java.util.function.Supplier;

public abstract class BlockRecipeBuilder<T extends BlockRecipe> {
    protected BlockIngredient input;
    protected List<ItemStack> outputs;
    private Supplier<CraftingManagerBlock<T>> registry;
    protected String name;

    public BlockRecipeBuilder(Supplier<CraftingManagerBlock<T>> registry, String name) {
        this.registry = registry;
        this.name = name;
    }

    protected void addRecipe(T recipe) {
        ModTweaker.LATE_ADDITIONS.add(new BlockRecipeAdd<>(name, registry.get(), recipe));
    }

    @ZenMethod
    public abstract void build();

    public void removeRecipe(IItemStack[] output) {
        ModTweaker.LATE_REMOVALS.add(new BlockRecipeRemove<>(name, registry.get(), output));
    }

    public void removeRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new BlockRecipeRemoveInput<>(name, registry.get(), input));
    }

    public void _buildRecipe(IIngredient input, IItemStack[] outputs) {
        this.input = new BlockIngredient(CraftTweakerMC.getIngredient(input));
        this.outputs = InputHelper.toNonNullList(CraftTweakerMC.getItemStacks(outputs));
    }

    @ZenMethod
    public BlockRecipeBuilder setInputBlockDrop(IItemStack input) {
        this.input = new BlockDropIngredient(CraftTweakerMC.getItemStack(input));
        return this;
    }

    @ZenMethod
    public BlockRecipeBuilder setInputBlockDrop(IItemStack[] inputs) {
        this.input = new BlockDropIngredient(CraftTweakerMC.getItemStacks(inputs));
        return this;
    }

    @ZenMethod
    public void removeAll() {
        ModTweaker.LATE_REMOVALS.add(new RemoveAll<>(name, registry.get().getRecipes()));
    }

    public String getName() {
        return name;
    }
}
