package com.blamejared.compat.betterwithmods;

import betterwithmods.module.gameplay.miniblocks.MiniBlockIngredient;
import com.blamejared.compat.betterwithmods.base.bulkrecipes.BulkRecipeBuilder;
import com.google.common.collect.Lists;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.*;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collections;
import java.util.List;

@ZenClass("mods.betterwithmods.MiniBlocks")
@ModOnly("betterwithmods")
@ZenRegister
public class MiniBlocks {

    @ZenMethod
    public static IIngredient getMiniBlock(String type, IIngredient parent) {
        MiniBlockIngredient mini = new MiniBlockIngredient(type, BulkRecipeBuilder.getIngredient(parent));
        return new IngredientMini(mini, 1);
    }

    public static class IngredientMini implements IIngredient {

        private final MiniBlockIngredient ingredient;
        private final int amount;

        public IngredientMini(MiniBlockIngredient ingredient, int amount) {
            this.ingredient = ingredient;
            this.amount = amount;
        }

        @Override
        public String getMark() {
            return null;
        }

        @Override
        public int getAmount() {
            return amount;
        }

        @Override
        public List<IItemStack> getItems() {
            return Lists.newArrayList(getItemArray());
        }

        @Override
        public IItemStack[] getItemArray() {
            return CraftTweakerMC.getIItemStacks(ingredient.getMatchingStacks());
        }

        @Override
        public List<ILiquidStack> getLiquids() {
            return Collections.emptyList();
        }

        @Override
        public IIngredient amount(int amount) {
            return new IngredientMini(ingredient, amount);
        }

        @Override
        public IIngredient transformNew(IItemTransformerNew transformer) {
            return null;
        }

        @Override
        public IIngredient only(IItemCondition condition) {
            return null;
        }

        @Override
        public IIngredient marked(String mark) {
            return null;
        }

        @Override
        public IIngredient or(IIngredient ingredient) {
            return new IngredientOr(this, ingredient);
        }

        @Override
        public boolean matches(IItemStack item) {
            return ingredient.apply(CraftTweakerMC.getItemStack(item));
        }

        @Override
        public boolean matchesExact(IItemStack item) {
            return matches(item);
        }

        @Override
        public boolean matches(ILiquidStack liquid) {
            return false;
        }

        @Override
        public boolean contains(IIngredient ingredient) {
            return ingredient.getItems().stream().anyMatch(this::matches);
        }

        @Override
        public IItemStack applyTransform(IItemStack item, IPlayer byPlayer) {
            return item;
        }

        @Override
        public IItemStack applyNewTransform(IItemStack item) {
            return item;
        }

        @Override
        public Object getInternal() {
            return null;
        }

        @Override
        public String toCommandString() {
            return ingredient.toString() + " * " + amount;
        }

        @Override
        public boolean hasNewTransformers() {
            return false;
        }

        @Override
        public boolean hasTransformers() {
            return false;
        }

        @Override
        public IIngredient transform(IItemTransformer transformer) {
            return null;
        }

        // #############################
        // ### Object implementation ###
        // #############################

        @Override
        public String toString() {
            return "(Ingredients) " + ingredient.toString();
        }
    }
}
