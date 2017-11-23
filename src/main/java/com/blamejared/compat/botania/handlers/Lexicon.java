package com.blamejared.compat.botania.handlers;

import static com.blamejared.mtlib.helpers.InputHelper.toObject;
import static com.blamejared.mtlib.helpers.InputHelper.toObjects;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;
import static com.blamejared.mtlib.helpers.InputHelper.toStacks;

import java.util.ArrayList;
import java.util.List;

import com.blamejared.compat.botania.BotaniaHelper;
import com.blamejared.compat.botania.lexicon.AddCategory;
import com.blamejared.compat.botania.lexicon.AddEntry;
import com.blamejared.compat.botania.lexicon.AddPage;
import com.blamejared.compat.botania.lexicon.AddRecipeMapping;
import com.blamejared.compat.botania.lexicon.RemoveCategory;
import com.blamejared.compat.botania.lexicon.RemoveEntry;
import com.blamejared.compat.botania.lexicon.RemovePage;
import com.blamejared.compat.botania.lexicon.RemoveRecipeMapping;
import com.blamejared.compat.botania.lexicon.SetCategoryIcon;
import com.blamejared.compat.botania.lexicon.SetCategoryPriority;
import com.blamejared.compat.botania.lexicon.SetEntryKnowledgeType;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.lexicon.page.PageBrew;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageEntity;
import vazkii.botania.common.lexicon.page.PageImage;
import vazkii.botania.common.lexicon.page.PageLoreText;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;

@ZenClass("mods.botania.Lexicon")
@ModOnly("botania")
@ZenRegister
public class Lexicon {

    @ZenMethod
    public static void addBrewPage(String name, String entry, int page_number, String brew, IIngredient[] recipe, String bottomText) {
    	LexiconEntry lexiconEntry= BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	if(BotaniaAPI.getBrewFromKey(brew)==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find brew "+brew);
    		return;
    	}
    	RecipeBrew page_recipe=new RecipeBrew(BotaniaAPI.getBrewFromKey(brew),toObjects(recipe));
    	LexiconPage page=new PageBrew(page_recipe,name,bottomText);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addCraftingPage(String name, String entry, int page_number, String... recipeNames) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	List<ResourceLocation> recipes = new ArrayList<>();
    	for(String s : recipeNames)
    	{
    		recipes.add(new ResourceLocation(s));
    	}
    	LexiconPage page=new PageCraftingRecipe(name, recipes);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }
    
    @ZenMethod
    public static void addElvenPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	if(outputs.length!=inputs.length)
    	{
    		CraftTweakerAPI.getLogger().logError("Length of input and output must match");
    		return;
    	}
    	List<RecipeElvenTrade> recipes=new ArrayList<RecipeElvenTrade>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		//TODO test
			recipes.add(new RecipeElvenTrade(toStacks(outputs),toObjects(inputs[i])));
    	}


		LexiconPage page=new PageElvenRecipe(name,recipes);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }
    
    @ZenMethod
    public static void addEntityPage(String name, String entry, int page_number, String entity, int size) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	if(!EntityList.isRegistered(new ResourceLocation(entity)))
    	{
    		CraftTweakerAPI.getLogger().logError("No such entity "+entity);
    		return;
    	}
    	LexiconPage page=new PageEntity(entity, entity, size);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }
    
    @ZenMethod
    public static void addImagePage(String name, String entry, int page_number, String resource) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	LexiconPage page=new PageImage(name, resource);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }
    
    @ZenMethod
    public static void addLorePage(String name, String entry, int page_number) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	LexiconPage page=new PageLoreText(name);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addInfusionPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	if(outputs.length!=inputs.length || outputs.length!=mana.length)
    	{
    		CraftTweakerAPI.getLogger().logError("Length of input and output must match");
    		return;
    	}
    	List<RecipeManaInfusion> recipes=new ArrayList<RecipeManaInfusion>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		recipes.add(new RecipeManaInfusion(toStack(outputs[i]),toObject(inputs[i]),mana[i]));
    	}
    	LexiconPage page=new PageManaInfusionRecipe(name,recipes);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addAlchemyPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	if(outputs.length!=inputs.length || outputs.length!=mana.length)
    	{
    		CraftTweakerAPI.getLogger().logError("Length of input and output must match");
    		return;
    	}
    	List<RecipeManaInfusion> recipes=new ArrayList<RecipeManaInfusion>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		RecipeManaInfusion current_recipe=new RecipeManaInfusion(toStack(outputs[i]),toObject(inputs[i]),mana[i]);
    		current_recipe.setCatalyst(RecipeManaInfusion.alchemyState);
    		recipes.add(current_recipe);
    	}
    	LexiconPage page=new PageManaInfusionRecipe(name,recipes);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addConjurationPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	if(outputs.length!=inputs.length || outputs.length!=mana.length)
    	{
    		CraftTweakerAPI.getLogger().logError("Length of input and output must match");
    		return;
    	}
    	List<RecipeManaInfusion> recipes=new ArrayList<RecipeManaInfusion>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		RecipeManaInfusion current_recipe=new RecipeManaInfusion(toStack(outputs[i]),toObject(inputs[i]),mana[i]);
    		current_recipe.setCatalyst(RecipeManaInfusion.conjurationState);
    		recipes.add(current_recipe);
    	}
    	LexiconPage page=new PageManaInfusionRecipe(name,recipes);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addPetalPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	if(outputs.length!=inputs.length)
    	{
    		CraftTweakerAPI.getLogger().logError("Length of input and output must match");
    		return;
    	}
    	List<RecipePetals> recipes=new ArrayList<RecipePetals>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		recipes.add(new RecipePetals(toStack(outputs[i]),toObjects(inputs[i])));
    	}
    	LexiconPage page=new PagePetalRecipe<>(name,recipes);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addRunePage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs, int[] mana) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	if(outputs.length!=inputs.length || outputs.length!=mana.length)
    	{
    		CraftTweakerAPI.getLogger().logError("Length of input and output must match");
    		return;
    	}
    	List<RecipeRuneAltar> recipes=new ArrayList<RecipeRuneAltar>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		recipes.add(new RecipeRuneAltar(toStack(outputs[i]),mana[i],toObjects(inputs[i])));
    	}
    	LexiconPage page=new PageRuneRecipe(name,recipes);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addTextPage(String name, String entry, int page_number) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
    	LexiconPage page=new PageText(name);
        CraftTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void removePage(String entry, int page_number) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(page_number>=lexiconEntry.pages.size())
    	{
    		CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for "+entry);
    		return;
    	}
        CraftTweakerAPI.apply(new RemovePage(lexiconEntry,page_number));
    }

    @ZenMethod
    public static void addEntry(String entry, String catagory, IItemStack stack) {
    	LexiconCategory lexiconCategory=BotaniaHelper.findCatagory(catagory);
    	if(lexiconCategory==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon category "+catagory);
    		return;
    	}
    	LexiconEntry lexiconEntry=new LexiconEntry(entry,lexiconCategory);
    	lexiconEntry.setIcon(toStack(stack));
        CraftTweakerAPI.apply(new AddEntry(lexiconEntry));
    }
    @ZenMethod
    public static void removeEntry(String entry) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
        CraftTweakerAPI.apply(new RemoveEntry(lexiconEntry));
    }
    
    @ZenMethod
    public static void setEntryKnowledgeType(String entry, String knowledgeType) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	KnowledgeType type=BotaniaHelper.findKnowledgeType(knowledgeType);
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+entry);
    		return;
    	}
    	if(type==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find knowledge type "+knowledgeType);
    		return;
    	}
        CraftTweakerAPI.apply(new SetEntryKnowledgeType(lexiconEntry,type));
    }
    
    @ZenMethod
    public static void addCategory(String name) {
    	LexiconCategory lexiconCategory=new LexiconCategory(name);
        CraftTweakerAPI.apply(new AddCategory(lexiconCategory));
    }
    
    @ZenMethod
    public static void removeCategory(String name) {
    	LexiconCategory lexiconCategory=BotaniaHelper.findCatagory(name);
    	if(lexiconCategory==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon category "+name);
    		return;
    	}
        CraftTweakerAPI.apply(new RemoveCategory(lexiconCategory));
    }

    @ZenMethod
    public static void setCategoryPriority(String name, int priority) {
    	LexiconCategory lexiconCategory=BotaniaHelper.findCatagory(name);
    	if(lexiconCategory==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon category "+name);
    		return;
    	}
        CraftTweakerAPI.apply(new SetCategoryPriority(lexiconCategory,priority));
    }
    
    @ZenMethod
    public static void setCategoryIcon(String name, String icon) {
    	LexiconCategory lexiconCategory=BotaniaHelper.findCatagory(name);
    	if(lexiconCategory==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon category "+name);
    		return;
    	}
        CraftTweakerAPI.apply(new SetCategoryIcon(lexiconCategory,icon));
    }

    @ZenMethod
    public static void addRecipeMapping(IItemStack stack, String Entry, int page) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(Entry);
    	if(LexiconRecipeMappings.getDataForStack(toStack(stack))!=null)
    	{
    		CraftTweakerAPI.getLogger().logError("There is already a recipe mapping for "+stack);
    		return;
    	}
    	if(lexiconEntry==null)
    	{
    		CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry "+Entry);
    		return;
    	}
    	if(lexiconEntry.pages.size()<page)
    	{
    		CraftTweakerAPI.getLogger().logError("Not enough pages in "+Entry);
    		return;
    	}
        CraftTweakerAPI.apply(new AddRecipeMapping(toStack(stack),lexiconEntry,page));
    }

    @ZenMethod
    public static void removeRecipeMapping(IItemStack stack) {
    	if(LexiconRecipeMappings.getDataForStack(toStack(stack))==null)
    	{
    		CraftTweakerAPI.getLogger().logError("There isn't a recipe mapping for "+stack);
    		return;
    	}
        CraftTweakerAPI.apply(new RemoveRecipeMapping(toStack(stack)));
    }
}
