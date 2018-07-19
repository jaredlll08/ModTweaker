package com.blamejared.compat.botania.handlers;


import com.blamejared.ModTweaker;
import com.blamejared.compat.botania.lexicon.*;

import com.blamejared.compat.botania.lexicon.pages.*;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.botania.Lexicon")
@ModOnly("botania")
@ZenRegister
public class Lexicon {
    
    @ZenMethod
    public static void addBrewPage(String name, String entry, int page_number, String brew, IIngredient[] recipe, String bottomText) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageBrew(name, entry, page_number, brew, recipe, bottomText));
    }
    
    @ZenMethod
    public static void addCraftingPage(String name, String entry, int page_number, String... recipeNames) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageCrafting(name, entry, page_number, recipeNames));
    }
    
    @ZenMethod
    public static void addElvenPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageElven(name, entry, page_number, outputs, inputs));
    }
    
    @ZenMethod
    public static void addEntityPage(String name, String entry, int page_number, String entity, int size) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageEntity(name, entry, page_number, entity, size));
    }
    
    @ZenMethod
    public static void addImagePage(String name, String entry, int page_number, String resource) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageImage(name, entry, page_number, resource));
    }
    
    @ZenMethod
    public static void addLorePage(String name, String entry, int page_number) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageLore(name, entry, page_number));
    }
    
    @ZenMethod
    public static void addInfusionPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageInfusion(name, entry, page_number, outputs, inputs, mana));
    }
    
    @ZenMethod
    public static void addAlchemyPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageAlchemy(name, entry, page_number, outputs, inputs, mana));
    }
    
    @ZenMethod
    public static void addConjurationPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageConjuration(name, entry, page_number, outputs, inputs, mana));
    }
    
    @ZenMethod
    public static void addPetalPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs) {
        ModTweaker.LATE_ADDITIONS.add(new AddPagePetal(name, entry, page_number, outputs, inputs));
    }
    
    @ZenMethod
    public static void addRunePage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs, int[] mana) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageRune(name, entry, page_number, outputs, inputs, mana));
    }
    
    @ZenMethod
    public static void addTextPage(String name, String entry, int page_number) {
        ModTweaker.LATE_ADDITIONS.add(new AddPageText(name, entry, page_number));
    }
    
    @ZenMethod
    public static void removePage(String entry, int page_number) {
        ModTweaker.LATE_REMOVALS.add(new RemovePage(entry, page_number));
    }
    
    @ZenMethod
    public static void addEntry(String entry, String category, IItemStack stack) {
        ModTweaker.LATE_ADDITIONS.add(new AddEntry(entry, category, stack));
    }
    
    @ZenMethod
    public static void removeEntry(String entry) {
        ModTweaker.LATE_REMOVALS.add(new RemoveEntry(entry));
    }
    
    @ZenMethod
    public static void setEntryKnowledgeType(String entry, String knowledgeType) {
        ModTweaker.LATE_ADDITIONS.add(new SetEntryKnowledgeType(entry, knowledgeType));
    }
    
    @ZenMethod
    public static void addCategory(String name) {
        ModTweaker.LATE_ADDITIONS.add(new AddCategory(name));
    }
    
    @ZenMethod
    public static void removeCategory(String name) {
        ModTweaker.LATE_REMOVALS.add(new RemoveCategory(name));
    }
    
    @ZenMethod
    public static void setCategoryPriority(String name, int priority) {
        ModTweaker.LATE_ADDITIONS.add(new SetCategoryPriority(name, priority));
    }
    
    @ZenMethod
    public static void setCategoryIcon(String name, String icon) {
        ModTweaker.LATE_ADDITIONS.add(new SetCategoryIcon(name, icon));
    }
    
    @ZenMethod
    public static void addRecipeMapping(IItemStack stack, String entry, int page) {
        ModTweaker.LATE_ADDITIONS.add(new AddRecipeMapping(stack, entry, page));
    }
    
    @ZenMethod
    public static void removeRecipeMapping(IItemStack stack) {
        ModTweaker.LATE_REMOVALS.add(new RemoveRecipeMapping(stack));
        
    }
}
