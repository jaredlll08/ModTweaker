package com.blamejared.compat.refinedstorage;

import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import com.blamejared.reference.Reference;
import com.raoulvdberge.refinedstorage.api.solderer.ISoldererRecipe;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import stanhebben.zenscript.annotations.*;

import javax.annotation.Nonnull;
import java.util.*;

@ZenClass("mods.refinedstorage.Solderer")
@ModOnly("refinedstorage")
@ZenRegister
public class Solderer {
    
    
    @ZenMethod
    public static void addRecipe(String name, IItemStack output, int time, IItemStack[] rows) {
        if(rows.length != 3) {
            CraftTweakerAPI.logError("Invalid array length! There have to have 3 items in the array! Use null where applicable!", new ArrayIndexOutOfBoundsException());
            return;
        }
        NonNullList<ItemStack> list = NonNullList.from(ItemStack.EMPTY, InputHelper.toStacks(rows));
        CraftTweakerAPI.apply(new Add(Collections.singletonList(new ISoldererRecipe() {
            @Override
            public ResourceLocation getName() {
                return new ResourceLocation(Reference.MODID, "name");
            }
            
            @Nonnull
            @Override
            public NonNullList<ItemStack> getRow(int row) {
                return NonNullList.withSize(1, list.get(row));
            }
            
            @Nonnull
            @Override
            public ItemStack getResult() {
                return InputHelper.toStack(output);
            }
            
            @Override
            public int getDuration() {
                return time;
            }
            
            @Override
            public boolean isProjectERecipe() {
                return false;
            }
        })));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }
    
    private static class Add extends BaseListAddition<ISoldererRecipe> {
        
        
        protected Add(List<ISoldererRecipe> recipes) {
            super("Solderer", API.instance().getSoldererRegistry().getRecipes(), recipes);
        }
        
        @Override
        protected String getRecipeInfo(ISoldererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }
        
    }
    
    private static class Remove extends BaseListRemoval<ISoldererRecipe> {
        
        private IItemStack output;
        
        protected Remove(IItemStack output) {
            super("Solderer", API.instance().getSoldererRegistry().getRecipes(), Collections.emptyList());
            this.output = output;
        }
        
        @Override
        public void apply() {
            for(ISoldererRecipe recipe : API.instance().getSoldererRegistry().getRecipes()) {
                if(output.matches(InputHelper.toIItemStack(recipe.getResult()))) {
                    recipes.add(recipe);
                }
            }
            super.apply();
        }
        
        @Override
        protected String getRecipeInfo(ISoldererRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }
        
    }
}