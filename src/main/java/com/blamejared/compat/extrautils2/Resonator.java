package com.blamejared.compat.extrautils2;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.rwtema.extrautils2.crafting.ResonatorRecipe;
import com.rwtema.extrautils2.tile.TileResonator;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

@ZenClass("mods.extrautils2.Resonator")
@ModOnly("extrautils2")
@ZenRegister
public class Resonator {
    
    /**
     * Adds recipes to the resonator
     *
     * @param output      output item
     * @param input       input item
     * @param energy      energy -> 1 GP are 100 here
     * @param addOwnerTag (no idea)
     */
    @ZenMethod
    public static void add(IItemStack output, IItemStack input, int energy, @Optional boolean addOwnerTag) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), energy, addOwnerTag));
    }
    
    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack output, input;
        private int energy;
        private boolean ownerTag;
        
        public Add(ItemStack output, ItemStack input, int energy, boolean ownerTag) {
            super("Resonator");
            this.output = output;
            this.input = input;
            this.energy = energy;
            this.ownerTag = ownerTag;
        }
        
        @Override
        public void apply() {
            TileResonator.register(input, output, energy, ownerTag);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private IItemStack output;
        
        public Remove(IItemStack output) {
            super("Resonator");
            this.output = output;
        }
        
        @Override
        public void apply() {
            List<ResonatorRecipe> list = new ArrayList<>();
            for(ResonatorRecipe recipe : TileResonator.resonatorRecipes) {
                if(StackHelper.matches(output, InputHelper.toIItemStack(recipe.output))){
                 list.add(recipe);
                }
            }
            TileResonator.resonatorRecipes.removeAll(list);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
}
