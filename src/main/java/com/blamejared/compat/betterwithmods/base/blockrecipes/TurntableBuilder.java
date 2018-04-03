package com.blamejared.compat.betterwithmods.base.blockrecipes;

import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.TurntableRecipe;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import stanhebben.zenscript.annotations.ZenMethod;

public class TurntableBuilder extends BlockRecipeBuilder<TurntableRecipe> {
    private int rotations = 8;
    private IBlockState productState = Blocks.AIR.getDefaultState();

    public TurntableBuilder(CraftingManagerBlock<TurntableRecipe> registry, String name) {
        super(registry, name);
    }

    @ZenMethod
    public TurntableBuilder setRotations(int rotations) {
        this.rotations = rotations;
        return this;
    }

    @ZenMethod
    public TurntableBuilder setProductState(IItemStack productState) {
        if (InputHelper.isABlock(productState)) {
            Block block = CraftTweakerMC.getBlock(productState);
            this.productState = block.getStateFromMeta(productState.getMetadata());
        } else {
            LogHelper.logError(String.format("%s Product State must create a valid BlockState", productState.getDisplayName()), new IllegalArgumentException(String.format("%s Product State must create a valid BlockState", productState.getDisplayName())));
        }
        return this;
    }

    @ZenMethod
    public TurntableBuilder buildRecipe(IIngredient input, IItemStack[] outputs) {
        _buildRecipe(input,outputs);
        return this;
    }

    @ZenMethod
    @Override
    public void build() {
        addRecipe(new TurntableRecipe(input, outputs, productState, rotations));
    }
}
