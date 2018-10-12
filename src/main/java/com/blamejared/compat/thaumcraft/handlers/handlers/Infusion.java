package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thaumcraft.handlers.ThaumCraft;
import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspectStack;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.*;
import thaumcraft.api.internal.CommonInternals;

import java.util.*;

@ZenClass("mods.thaumcraft.Infusion")
@ZenRegister
@ModOnly("thaumcraft")
public class Infusion {
    
    @ZenMethod
    public static void registerRecipe(String name, String research, IItemStack output, int instability, CTAspectStack[] aspects, IIngredient centralItem, IIngredient[] recipe) {
        InfusionRecipe infRecipe = new InfusionRecipe(research, InputHelper.toStack(output), instability, ThaumCraft.getAspects(aspects), InputHelper.toObject(centralItem), InputHelper.toObjects(recipe));
        ModTweaker.LATE_ADDITIONS.add(new Add(new ResourceLocation("thaumcraft", name), infRecipe));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    @ZenMethod
    public static void removeRecipe(String recipeName) {
        ModTweaker.LATE_REMOVALS.add(new Remove(recipeName));
    }
    
    private static class Add extends BaseAction {
        
        private ResourceLocation resourceLocation;
        private InfusionRecipe recipe;
        
        public Add(ResourceLocation resourceLocation, InfusionRecipe recipe) {
            super("Infusion");
            this.resourceLocation = resourceLocation;
            this.recipe = recipe;
        }
        
        @Override
        public void apply() {
            ThaumcraftApi.addInfusionCraftingRecipe(resourceLocation, recipe);
        }
        
        
        @Override
        public String describe() {
            return "Adding infusion recipe for: " + recipe.recipeOutput;
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(recipe.getRecipeOutput());
        }
    }
    
    private static class Remove extends BaseAction {
        
        private IItemStack output;
        private String recipeName;
        
        public Remove(IItemStack output) {
            super("Infusion");
            this.output = output;
        }
        
        public Remove(String recipeName) {
            super("Infusion");
            this.recipeName = recipeName;
        }
        
        @Override
        public void apply() {
            List<ResourceLocation> remove = new ArrayList<>();
            
            for(Map.Entry<ResourceLocation, IThaumcraftRecipe> entry : CommonInternals.craftingRecipeCatalog.entrySet()) {
                if(entry.getValue() instanceof InfusionRecipe) {
                    InfusionRecipe recipe = (InfusionRecipe) entry.getValue();
                    if(output != null) {
                        if(recipe.getRecipeOutput() instanceof ItemStack) {
                            IItemStack recOutput = InputHelper.toIItemStack((ItemStack) recipe.getRecipeOutput());
                            if(output.matches(recOutput)) {
                                remove.add(entry.getKey());
                            }
                        }
                    }else if(recipeName !=null){
                        if(entry.getKey().toString().equals(recipeName)){
                            remove.add(entry.getKey());
                        }
                    }
                }
            }
            remove.forEach(CommonInternals.craftingRecipeCatalog::remove);
        }
        
        
        @Override
        public String describe() {
            return "Removing Infusion recipe for:" + getRecipeInfo();
        }
        
        @Override
        protected String getRecipeInfo() {
            if(output == null) {
                return recipeName;
            }
            return LogHelper.getStackDescription(output);
        }
    }
    
}
