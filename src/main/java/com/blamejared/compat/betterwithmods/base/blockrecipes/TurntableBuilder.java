package com.blamejared.compat.betterwithmods.base.blockrecipes;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.BlockRecipe;
import betterwithmods.common.registry.block.recipe.TurntableRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;

public class TurntableBuilder extends BlockRecipeBuilder<TurntableRecipe> {
    private int rotations = 8;
    private IBlockState productState = Blocks.AIR.getDefaultState();

    public TurntableBuilder(Supplier<CraftingManagerBlock<TurntableRecipe>> registry, String name) {
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

    @Override
    public void build() {
        addRecipe(new TurntableRecipe(input, outputs, productState, rotations));
    }

    public void removeRecipe(IBlockState productState) {
        ModTweaker.LATE_REMOVALS.add(new TurntableRemoveProduct(name, productState));
    }

    public class TurntableRemoveProduct extends BaseAction {

        private final IBlockState productState;

        private TurntableRemoveProduct(String name, IBlockState productState) {
            super(name);
            this.productState = productState;
        }

        @Override
        public void apply() {
            if (!BWRegistry.TURNTABLE.remove(productState)) {
                LogHelper.logWarning(String.format("No recipes were removed for input %s", getRecipeInfo(productState)));
            } else {
                LogHelper.logInfo(String.format("Succesfully removed all recipes with %s as input", getRecipeInfo(productState)));
            }
        }

        private String getRecipeInfo(IBlockState productState) {
            return String.format("%s - %s", name, productState.getBlock().toString()+"@"+productState.getBlock().getMetaFromState(productState));
        }
    }
}
