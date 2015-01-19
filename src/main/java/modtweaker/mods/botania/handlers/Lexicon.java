package modtweaker.mods.botania.handlers;

import java.util.ArrayList;
import java.util.List;

import static modtweaker.helpers.InputHelper.toObject;
import static modtweaker.helpers.InputHelper.toObjects;
import static modtweaker.helpers.InputHelper.toShapedObjects;
import static modtweaker.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.recipes.ShapedRecipe;
import minetweaker.mc1710.recipes.RecipeConverter;
import modtweaker.mods.botania.BotaniaHelper;
import modtweaker.mods.botania.lexicon.AddCategory;
import modtweaker.mods.botania.lexicon.AddEntry;
import modtweaker.mods.botania.lexicon.AddPage;
import modtweaker.mods.botania.lexicon.RemoveCategory;
import modtweaker.mods.botania.lexicon.RemoveEntry;
import modtweaker.mods.botania.lexicon.RemovePage;
import modtweaker.mods.botania.lexicon.SetCategoryIcon;
import modtweaker.mods.botania.lexicon.SetCategoryPriority;
import modtweaker.mods.botania.lexicon.SetEntryKnowledgeType;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
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
public class Lexicon {

    @ZenMethod
    public static void addBrewPage(String name, String entry, int page_number, String brew, IIngredient[] recipe, String bottomText) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	RecipeBrew page_recipe=new RecipeBrew(BotaniaAPI.getBrewFromKey(brew),(Object[])recipe);
    	LexiconPage page=new PageBrew(page_recipe,name,bottomText);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addCraftingPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][][] inputs) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(outputs.length!=inputs.length)
    		throw new IllegalArgumentException("Length of input and output must match");
    	List<IRecipe> recipes=new ArrayList<IRecipe>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		recipes.add(RecipeConverter.convert(new ShapedRecipe(outputs[i],inputs[i],null,false)));
    	}
    	LexiconPage page=new PageCraftingRecipe(name, recipes);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }
    
    @ZenMethod
    public static void addElvenPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(outputs.length!=inputs.length)
    		throw new IllegalArgumentException("Length of input and output must match");
    	List<RecipeElvenTrade> recipes=new ArrayList<RecipeElvenTrade>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		recipes.add(new RecipeElvenTrade(toStack(outputs[i]),toObjects(inputs[i])));
    	}
    	LexiconPage page=new PageElvenRecipe(name,recipes);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }
    
    @ZenMethod
    public static void addEntityPage(String name, String entry, int page_number, String entity, int size) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	LexiconPage page=new PageEntity(entity, entity, size);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }
    
    @ZenMethod
    public static void addImagePage(String name, String entry, int page_number, String resource) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	LexiconPage page=new PageImage(name, resource);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }
    
    @ZenMethod
    public static void addLorePage(String name, String entry, int page_number) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	LexiconPage page=new PageLoreText(name);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addInfusionPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(outputs.length!=inputs.length || outputs.length!=mana.length)
    		throw new IllegalArgumentException("Length of input and output must match");
    	List<RecipeManaInfusion> recipes=new ArrayList<RecipeManaInfusion>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		recipes.add(new RecipeManaInfusion(toStack(outputs[i]),toObject(inputs[i]),mana[i]));
    	}
    	LexiconPage page=new PageManaInfusionRecipe(name,recipes);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addAlchemyPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(outputs.length!=inputs.length || outputs.length!=mana.length)
    		throw new IllegalArgumentException("Length of input and output must match");
    	List<RecipeManaInfusion> recipes=new ArrayList<RecipeManaInfusion>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		RecipeManaInfusion current_recipe=new RecipeManaInfusion(toStack(outputs[i]),toObject(inputs[i]),mana[i]);
    		current_recipe.setAlchemy(true);
    		recipes.add(current_recipe);
    	}
    	LexiconPage page=new PageManaInfusionRecipe(name,recipes);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addConjurationPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(outputs.length!=inputs.length || outputs.length!=mana.length)
    		throw new IllegalArgumentException("Length of input and output must match");
    	List<RecipeManaInfusion> recipes=new ArrayList<RecipeManaInfusion>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		RecipeManaInfusion current_recipe=new RecipeManaInfusion(toStack(outputs[i]),toObject(inputs[i]),mana[i]);
    		current_recipe.setConjuration(true);
    		recipes.add(current_recipe);
    	}
    	LexiconPage page=new PageManaInfusionRecipe(name,recipes);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addPetalPage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(outputs.length!=inputs.length)
    		throw new IllegalArgumentException("Length of input and output must match");
    	List<RecipePetals> recipes=new ArrayList<RecipePetals>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		recipes.add(new RecipePetals(toStack(outputs[i]),toObjects(inputs[i])));
    	}
    	LexiconPage page=new PagePetalRecipe(name,recipes);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addRunePage(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs, int[] mana) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	if(outputs.length!=inputs.length || outputs.length!=mana.length)
    		throw new IllegalArgumentException("Length of input and output must match");
    	List<RecipeRuneAltar> recipes=new ArrayList<RecipeRuneAltar>();
    	for(int i=0;i<outputs.length;i++)
    	{
    		recipes.add(new RecipeRuneAltar(toStack(outputs[i]),mana[i],toObjects(inputs[i])));
    	}
    	LexiconPage page=new PageRuneRecipe(name,recipes);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void addTextPage(String name, String entry, int page_number) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	LexiconPage page=new PageText(name);
        MineTweakerAPI.apply(new AddPage(name,lexiconEntry,page,page_number));
    }

    @ZenMethod
    public static void removePage(String entry, int page_number) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
        MineTweakerAPI.apply(new RemovePage(lexiconEntry,page_number));
    }

    @ZenMethod
    public static void addEntry(String entry, String catagory) {
    	LexiconCategory lexiconCatagory=BotaniaHelper.findCatagory(catagory);
    	LexiconEntry lexiconEntry=new LexiconEntry(entry,lexiconCatagory);
        MineTweakerAPI.apply(new AddEntry(lexiconEntry));
    }

    @ZenMethod
    public static void removeEntry(String entry) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
        MineTweakerAPI.apply(new RemoveEntry(lexiconEntry));
    }
    
    @ZenMethod
    public static void setEntryKnowledgeType(String entry, String knowledgeType) {
    	LexiconEntry lexiconEntry=BotaniaHelper.findEntry(entry);
    	KnowledgeType type=BotaniaHelper.findKnowledgeType(knowledgeType);
        MineTweakerAPI.apply(new SetEntryKnowledgeType(lexiconEntry,type));
    }
    
    @ZenMethod
    public static void addCategory(String name) {
    	LexiconCategory lexiconCategory=new LexiconCategory(name);
        MineTweakerAPI.apply(new AddCategory(lexiconCategory));
    }
    
    @ZenMethod
    public static void removeCategory(String name) {
    	LexiconCategory lexiconCatagory=BotaniaHelper.findCatagory(name);
        MineTweakerAPI.apply(new RemoveCategory(lexiconCatagory));
    }

    @ZenMethod
    public static void setCategoryPriority(String name, int priority) {
    	LexiconCategory lexiconCatagory=BotaniaHelper.findCatagory(name);
        MineTweakerAPI.apply(new SetCategoryPriority(lexiconCatagory,priority));
    }
    
    @ZenMethod
    public static void setCategoryIcon(String name, String icon) {
    	LexiconCategory lexiconCatagory=BotaniaHelper.findCatagory(name);
        MineTweakerAPI.apply(new SetCategoryIcon(lexiconCatagory,icon));
    }

}
