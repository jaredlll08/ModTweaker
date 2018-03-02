package com.blamejared.compat.bloodmagic;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.bloodmagic.AlchemyArray")
@ZenRegister
@ModOnly("bloodmagic")
public class AlchemyArray {
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, IItemStack catalyst, @Optional String textureLocation) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(input), InputHelper.toStack(catalyst), InputHelper.toStack(output), textureLocation !=null ? new ResourceLocation(textureLocation) : null));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input, IItemStack catalyst) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input), InputHelper.toStack(catalyst)));
    }
    
    
    private static class Add extends BaseAction {
        
        private ItemStack input, catalyst, output;
        private ResourceLocation location;
        
        public Add(ItemStack input, ItemStack catalyst, ItemStack output, ResourceLocation location) {
            super("AlchemyArray");
            this.input = input;
            this.catalyst = catalyst;
            this.output = output;
            this.location = location;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().addAlchemyArray(input, catalyst, output, location);
        }
        
        @Override
        public String describe() {
            return "Adding AlchemyArray Recipe input=" + input + ", catalyst=" + catalyst + ", output=" + output + ", location=" + location;
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack input, catalyst;
        
        public Remove(ItemStack input, ItemStack catalyst) {
            super("AlchemyArray");
            this.input = input;
            this.catalyst = catalyst;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeAlchemyArray(input, catalyst);
        }
        
        @Override
        public String describe() {
            return "Removing AlchemyArray recipe for: " + input + " and " + catalyst;
        }
    }
}
