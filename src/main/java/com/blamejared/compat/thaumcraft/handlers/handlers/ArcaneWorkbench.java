package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thaumcraft.handlers.ThaumCraft;
import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspectStack;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import com.blamejared.reference.Reference;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.*;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.*;

import java.util.*;

@ZenClass("mods.thaumcraft.ArcaneWorkbench")
@ZenRegister
@ModOnly("thaumcraft")
public class ArcaneWorkbench {
    
    @ZenMethod
    public static void registerShapedRecipe(String name, String research, int vis, CTAspectStack[] aspectList, IItemStack output, IIngredient[][] input) {
        ModTweaker.LATE_ADDITIONS.add(new AddShaped(new ResourceLocation("thaumcraft", name), research, vis, ThaumCraft.getAspects(aspectList), InputHelper.toStack(output), InputHelper.toShapedObjects(input)));
    }
    
    @ZenMethod
    public static void registerShapelessRecipe(String name, String research, int vis, CTAspectStack[] aspectList, IItemStack output, IIngredient[] input) {
        NonNullList<Ingredient> list = NonNullList.withSize(input.length, Ingredient.EMPTY);
        for(int i = 0; i < input.length; i++) {
            list.set(i, CraftTweakerMC.getIngredient(input[i]));
        }
        ModTweaker.LATE_ADDITIONS.add(new AddShapeless(new ResourceLocation("thaumcraft", name), research, vis, ThaumCraft.getAspects(aspectList), InputHelper.toStack(output), list));
    }
    
    @ZenMethod
    public static void removeRecipe(String name) {
        ModTweaker.LATE_REMOVALS.add(new Remove(name));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }
    
    
    public static class Remove extends BaseAction {
        
        private String name;
        private IItemStack output;
        
        public Remove(String name) {
            super("ArcaneWorkbench");
            this.name = name;
        }
        
        public Remove(IItemStack output) {
            super("ArcaneWorkbench");
            this.output = output;
        }
        
        @Override
        public void apply() {
            List<ResourceLocation> removed = new ArrayList<>();
            
            for(Map.Entry<ResourceLocation, IRecipe> entry : ForgeRegistries.RECIPES.getEntries()) {
                if(entry.getValue() instanceof IArcaneRecipe) {
                    if(name != null && !name.isEmpty()) {
                        if(entry.getKey().toString().equals(name)) {
                            removed.add(entry.getKey());
                        }
                    }
                    if(output != null) {
                        if(output.matches(InputHelper.toIItemStack(entry.getValue().getRecipeOutput()))) {
                            removed.add(entry.getKey());
                        }
                    }
                    
                }
            }
            
            removed.forEach(RegistryManager.ACTIVE.getRegistry(GameData.RECIPES)::remove);
        }
    }
    
    public static class AddShaped extends BaseAction {
        
        private ResourceLocation location;
        private String research;
        private int vis;
        private AspectList list;
        private ItemStack output;
        private Object[] shaped;
        
        public AddShaped(ResourceLocation location, String research, int vis, AspectList list, ItemStack output, Object[] shaped) {
            super("ArcaneWorkbench");
            this.location = location;
            this.research = research;
            this.vis = vis;
            this.list = list;
            this.output = output;
            this.shaped = shaped;
        }
        
        @Override
        public void apply() {
            ThaumcraftApi.addArcaneCraftingRecipe(location, new ShapedArcaneRecipe(location, research, vis, list, output, shaped));
        }
        
        @Override
        public String describe() {
            return "Adding shaped recipe for ArcaneWorkbench with name: " + location + " with output: " + LogHelper.getStackDescription(output);
        }
    }
    
    
    public static class AddShapeless extends BaseAction {
        
        private ResourceLocation location;
        private String research;
        private int vis;
        private AspectList list;
        private ItemStack output;
        private NonNullList<Ingredient> shapeless;
        
        public AddShapeless(ResourceLocation location, String research, int vis, AspectList list, ItemStack output, NonNullList<Ingredient> shapeless) {
            super("ArcaneWorkbench");
            this.location = location;
            this.research = research;
            this.vis = vis;
            this.list = list;
            this.output = output;
            this.shapeless = shapeless;
        }
        
        @Override
        public void apply() {
            ThaumcraftApi.addArcaneCraftingRecipe(location, new ShapelessArcaneRecipe(location, research, vis, list, shapeless, output));
        }
        
        @Override
        public String describe() {
            return "Adding shapeless recipe for ArcaneWorkbench with name: " + location + " with output: " + LogHelper.getStackDescription(output);
        }
    }
}
