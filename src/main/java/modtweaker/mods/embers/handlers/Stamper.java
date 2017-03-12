package modtweaker.mods.embers.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;
import teamroots.embers.compat.jei.StampingRecipeWrapper;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.recipe.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;

@ZenClass("mods.embers.Stamper")
public class Stamper {
    
    public static String name = "Embers Stamper";
    
    
    @ZenMethod
    public static void addRecipe(IItemStack itemInput, ILiquidStack fluidInput, String stampType, IItemStack result, boolean matchMeta, boolean matchNBT) {
        MineTweakerAPI.apply(new Add(new ItemStampingRecipe(toStack(itemInput), toFluid(fluidInput), getStampFromString(stampType), toStack(result), matchMeta, matchNBT)));
    }
    
    @ZenMethod
    public static void addRecipe(IOreDictEntry key, ILiquidStack fluidInput, String stampType, IItemStack result) {
        MineTweakerAPI.apply(new AddOre(new ItemStampingOreRecipe(key.getName(), toFluid(fluidInput), getStampFromString(stampType), toStack(result), false, false)));
    }
    
    @ZenMethod
    public static void remove(IItemStack item) {
        List<ItemStampingRecipe> recipes = new ArrayList<ItemStampingRecipe>();
        List<ItemStampingOreRecipe> oreRecipes = new ArrayList<ItemStampingOreRecipe>();
        
        if(item == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        for(int i = 0; i < RecipeRegistry.stampingRecipes.size(); i++) {
            if(ItemStack.areItemsEqual(RecipeRegistry.stampingRecipes.get(i).result, toStack(item))) {
                recipes.add(RecipeRegistry.stampingRecipes.get(i));
            }
        }
        
        for(int i = 0; i < RecipeRegistry.stampingOreRecipes.size(); i++) {
            if(ItemStack.areItemsEqual(RecipeRegistry.stampingOreRecipes.get(i).result, toStack(item))) {
                oreRecipes.add(RecipeRegistry.stampingOreRecipes.get(i));
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else if(!oreRecipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveOre(oreRecipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", Stamper.name, item.toString()));
        }
        
    }
    
    private static class AddOre extends BaseListAddition<ItemStampingOreRecipe> {
        
        public AddOre(ItemStampingOreRecipe recipe) {
            super(Stamper.name, RecipeRegistry.stampingOreRecipes);
            
            this.recipes.add(recipe);
        }
        
        @Override
        public void apply() {
            super.apply();
            successful.forEach(rec -> {
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new StampingRecipeWrapper(rec));
            });
        }
        
        @Override
        public void undo() {
            super.undo();
            successful.forEach(rec -> {
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new StampingRecipeWrapper(rec));
            });
        }
        
        @Override
        protected String getRecipeInfo(ItemStampingOreRecipe arg0) {
            return arg0.getOre();
        }
    }
    
    
    private static class Add extends BaseListAddition<ItemStampingRecipe> {
        
        public Add(ItemStampingRecipe recipe) {
            super(Stamper.name, RecipeRegistry.stampingRecipes);
            
            this.recipes.add(recipe);
        }
        
        @Override
        public void apply() {
            super.apply();
            successful.forEach(rec -> {
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new StampingRecipeWrapper(rec));
            });
        }
        
        @Override
        public void undo() {
            super.undo();
            successful.forEach(rec -> {
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new StampingRecipeWrapper(rec));
            });
        }
        
        @Override
        protected String getRecipeInfo(ItemStampingRecipe arg0) {
            return LogHelper.getStackDescription(arg0.getStack());
        }
    }
    
    private static class Remove extends BaseListRemoval<ItemStampingRecipe> {
        
        public Remove(List<ItemStampingRecipe> recipes) {
            super(Stamper.name, RecipeRegistry.stampingRecipes, recipes);
        }
        
        @Override
        public void apply() {
            super.apply();
            successful.forEach(rec -> {
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new StampingRecipeWrapper(rec));
            });
        }
        
        @Override
        public void undo() {
            super.undo();
            successful.forEach(rec -> {
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new StampingRecipeWrapper(rec));
            });
        }
        
        @Override
        protected String getRecipeInfo(ItemStampingRecipe arg0) {
            return LogHelper.getStackDescription(arg0.getStack());
        }
    }
    
    private static class RemoveOre extends BaseListRemoval<ItemStampingOreRecipe> {
        
        public RemoveOre(List<ItemStampingOreRecipe> recipes) {
            super(Stamper.name, RecipeRegistry.stampingOreRecipes, recipes);
        }
        
        @Override
        public void apply() {
            super.apply();
            successful.forEach(rec -> {
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new StampingRecipeWrapper(rec));
            });
        }
        
        @Override
        public void undo() {
            super.undo();
            successful.forEach(rec -> {
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new StampingRecipeWrapper(rec));
            });
        }
        
        @Override
        protected String getRecipeInfo(ItemStampingOreRecipe arg0) {
            return arg0.getOre();
        }
    }
    
    
    public static EnumStampType getStampFromString(String string) {
        switch(string) {
            default:
                return EnumStampType.TYPE_NULL;
            case "flat":
                return EnumStampType.TYPE_FLAT;
            case "bar":
                return EnumStampType.TYPE_BAR;
            case "plate":
                return EnumStampType.TYPE_PLATE;
            case "null":
                return EnumStampType.TYPE_NULL;
        }
    }
}
