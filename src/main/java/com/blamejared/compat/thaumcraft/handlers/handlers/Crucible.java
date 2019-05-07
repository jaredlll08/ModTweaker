package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thaumcraft.handlers.ThaumCraft;
import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspectStack;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.blamejared.reference.Reference;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.*;
import thaumcraft.api.internal.CommonInternals;

import java.util.*;

@ZenClass("mods.thaumcraft.Crucible")
@ZenRegister
@ModOnly("thaumcraft")
public class Crucible {
    
    @ZenMethod
    public static void registerRecipe(String name, String researchKey, IItemStack output, IIngredient input, CTAspectStack[] aspects) {
        ModTweaker.LATE_ADDITIONS.add(new Add(new ResourceLocation("thaumcraft", name), researchKey, InputHelper.toStack(output), InputHelper.toObject(input), ThaumCraft.getAspects(aspects)));
    }
    
    @ZenMethod
    public static void removeRecipe(String name) {
        ModTweaker.LATE_REMOVALS.add(new Remove(name));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    public static class Add extends BaseAction {
        
        private ResourceLocation name;
        private String research;
        private ItemStack output;
        private Object input;
        private AspectList aspects;
        
        public Add(ResourceLocation name1, String research, ItemStack output, Object input, AspectList aspects) {
            super("Crucible");
            this.name = name1;
            this.research = research;
            this.output = output;
            this.input = input;
            this.aspects = aspects;
        }
        
        @Override
        public void apply() {
            ThaumcraftApi.addCrucibleRecipe(name, new CrucibleRecipe(research, output, input, aspects));
        }
    }
    
    public static class Remove extends BaseAction {
        
        private String name;
        private IItemStack output;
        
        protected Remove(String name) {
            super("Crucible");
            this.name = name;
        }
        
        protected Remove(IItemStack output) {
            super("Crucible");
            this.output = output;
        }
        
        
        @Override
        public void apply() {
            List<ResourceLocation> toRemove = new ArrayList<>();
            for(Map.Entry<ResourceLocation, IThaumcraftRecipe> entry : CommonInternals.craftingRecipeCatalog.entrySet()) {
                if(entry.getValue() instanceof CrucibleRecipe) {
                    if(name != null && !name.isEmpty()) {
                        if(entry.getKey().toString().equals(name)) {
                            toRemove.add(entry.getKey());
                        }
                    }
                    if(output != null && !output.isEmpty()) {
                        if(output.matches(InputHelper.toIItemStack(((CrucibleRecipe) entry.getValue()).getRecipeOutput()))) {
                            toRemove.add(entry.getKey());
                        }
                    }
                }
            }
            toRemove.forEach(CommonInternals.craftingRecipeCatalog::remove);
            
        }
    }
}
