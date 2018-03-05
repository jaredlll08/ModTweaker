package com.blamejared.compat.betterwithmods;

import betterwithmods.common.registry.blockmeta.managers.TurntableManager;
import betterwithmods.common.registry.blockmeta.recipe.TurntableRecipe;
import betterwithmods.module.compat.jei.category.TurntableRecipeCategory;
import com.blamejared.ModTweaker;
import com.blamejared.compat.betterwithmods.util.BMAdd;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.collect.Lists;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.compiler.ITypeRegistry;

import java.util.*;


@ZenClass("mods.betterwithmods.Turntable")
@ModOnly("betterwithmods")
@ZenRegister
public class Turntable {
    
    @ZenMethod
    public static void add(IItemStack inputBlock, IItemStack outputBlock, IItemStack[] additionalOutput) {
        if(!InputHelper.isABlock(inputBlock))
            LogHelper.logError("Input must be a block", new IllegalArgumentException());
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(inputBlock), InputHelper.toStack(outputBlock), Lists.newArrayList(InputHelper.toStacks(additionalOutput))));
    }
    
    public static class Add extends BMAdd {
        
        public Add(ItemStack input, ItemStack output, List<ItemStack> scraps) {
            super(TurntableRecipeCategory.UID, TurntableManager.INSTANCE, Lists.newArrayList(new TurntableRecipe(Block.getBlockFromItem(input.getItem()), input.getMetadata(), output.isEmpty() ? null : Block.getBlockFromItem(output.getItem()), output.isEmpty() ? 0 : output.getMetadata(), scraps)));
        }
    }
    
    @ZenMethod
    public static void remove(IItemStack inputBlock) {
        if(!InputHelper.isABlock(inputBlock))
            LogHelper.logError("Input must be a block", new IllegalArgumentException());
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(inputBlock)));
    }
    
    public static class Remove extends BaseListRemoval<TurntableRecipe> {
        
        private ItemStack input;
        
        protected Remove(ItemStack input) {
            super("Remove Turntable Recipe", Collections.emptyList());
            this.input = input;
        }
        
        
        @Override
        public void apply() {
            for(TurntableRecipe recipe : TurntableManager.INSTANCE.getRecipes()) {
                if(InputHelper.toIItemStack(input).matches(InputHelper.toIItemStack(recipe.getStack()))) {
                    successful.add(recipe);
                }
            }
            System.out.println(">>>" + successful);
            for(TurntableRecipe recipe : successful) {
                System.out.println(">>>" + TurntableManager.INSTANCE.getRecipes().remove(recipe));
            }
        }
        
        @Override
        public String describe() {
            return String.format("Removing %d %s Recipe(s) for %s", TurntableManager.INSTANCE.removeTurntableRecipe(input).size(), this.name, this.getRecipeInfo());
        }
        
        @Override
        protected String getRecipeInfo(TurntableRecipe recipe) {
            return recipe.getStack().getDisplayName();
        }
    }
    
    
}
