package com.blamejared.compat.betterwithmods;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.blockmeta.managers.KilnManager;
import betterwithmods.common.registry.blockmeta.recipe.KilnRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.util.BMAdd;
import com.blamejared.compat.betterwithmods.util.BMRemove;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import com.google.common.collect.Lists;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenClass("mods.betterwithmods.Kiln")
@ModOnly("betterwithmods")
@ZenRegister
public class Kiln {
    
    @ZenMethod
    public static void add(IItemStack[] output, @NotNull IItemStack input) {
        ItemStack stack = InputHelper.toStack(input);
        if(InputHelper.isABlock(stack)) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            ItemStack[] outputs = InputHelper.toStacks(output);
            KilnRecipe r = new KilnRecipe(block, stack.getMetadata(), Arrays.asList(outputs));
            ModTweaker.LATE_ADDITIONS.add(new BMAdd("Set Kiln Recipe", KilnManager.INSTANCE, Lists.newArrayList(r)));
        }
    }
    
    @ZenMethod
    public static void remove(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new BMRemove("Set Kiln Recipe", KilnManager.INSTANCE, InputHelper.toStack(input)));
    }
    
    
    @ZenMethod
    public static void registerBlock(IItemStack block) {
        ModTweaker.LATE_ADDITIONS.add(new KilnBlock(BWMRecipes.getStateFromStack(InputHelper.toStack(block))));
    }
    
    
    public static class KilnBlock extends BaseUndoable {
        
        private IBlockState state;
        
        protected KilnBlock(IBlockState state) {
            super("Set Kiln Structure Block");
            this.state = state;
        }
        
        @Override
        public void apply() {
            KilnStructureManager.registerKilnBlock(state);
        }
    }
}
