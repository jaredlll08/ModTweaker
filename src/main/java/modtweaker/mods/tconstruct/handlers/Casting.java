package modtweaker.mods.tconstruct.handlers;

import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import mezz.jei.api.recipe.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.mc1102.item.MCItemStack;
import modtweaker.JEIAddonPlugin;
import modtweaker.mods.tconstruct.TConstructHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.*;
import slimeknights.tconstruct.plugin.jei.CastingRecipeWrapper;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.tconstruct.Casting")
public class Casting {
    
    protected static final String name = "TConstruct Casting";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addBasinRecipe(IItemStack output, ILiquidStack liquid, @Optional IItemStack cast, @Optional boolean consumeCast, @Optional int timeInTicks) {
        if(liquid == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        RecipeMatch match = null;
        if(cast != null) {
            match = RecipeMatch.of(toStack(cast));
        }
        FluidStack fluid = toFluid(liquid);
        if(timeInTicks == 0) {
            timeInTicks = CastingRecipe.calcCooldownTime(fluid.getFluid(), fluid.amount);
        }
        CastingRecipe rec = new CastingRecipe(toStack(output), match, fluid, timeInTicks, consumeCast, false);
        MineTweakerAPI.apply(new Add(rec, (LinkedList<ICastingRecipe>) TConstructHelper.basinCasting, false));
    }
    
    @ZenMethod
    public static void addTableRecipe(IItemStack output, ILiquidStack liquid, @Optional IItemStack cast, @Optional boolean consumeCast, @Optional int timeInTicks) {
        if(liquid == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        RecipeMatch match = null;
        if(cast != null) {
            match = RecipeMatch.of(toStack(cast));
        }
        FluidStack fluid = toFluid(liquid);
        if(timeInTicks == 0) {
            timeInTicks = CastingRecipe.calcCooldownTime(fluid.getFluid(), fluid.amount);
        }
        CastingRecipe rec = new CastingRecipe(toStack(output), match, fluid, timeInTicks, consumeCast, false);
        MineTweakerAPI.apply(new Add(rec, (LinkedList<ICastingRecipe>) TConstructHelper.tableCasting, true));
    }
    
    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition<ICastingRecipe> {
        
        final boolean table;
        
        public Add(CastingRecipe recipe, LinkedList<ICastingRecipe> list, boolean table) {
            super(Casting.name, list);
            this.recipes.add(recipe);
            this.table = table;
        }
        
        @Override
        protected String getRecipeInfo(ICastingRecipe recipe) {
            return LogHelper.getStackDescription(((CastingRecipe) recipe).getResult());
        }
        
        @Override
        public IRecipeWrapper wrapRecipe(ICastingRecipe recipe) {
            if(table) {
                return new CastingRecipeWrapper((CastingRecipe) recipe, JEIAddonPlugin.castingTable);
            } else {
                return new CastingRecipeWrapper((CastingRecipe) recipe, JEIAddonPlugin.castingBasin);
            }
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void removeTableRecipe(IIngredient output, @Optional IIngredient liquid, @Optional IIngredient cast) {
        removeRecipe(output, liquid, cast, TConstructHelper.tableCasting, true);
    }
    
    @ZenMethod
    public static void removeBasinRecipe(IIngredient output, @Optional IIngredient liquid, @Optional IIngredient cast) {
        removeRecipe(output, liquid, cast, TConstructHelper.basinCasting, false);
    }
    
    public static void removeRecipe(IIngredient output, IIngredient liquid, IIngredient cast, List<ICastingRecipe> list, boolean table) {
        if(output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        if(liquid == null) {
            liquid = IngredientAny.INSTANCE;
        }
        
        List<ICastingRecipe> recipes = new LinkedList<>();
        
        for(ICastingRecipe recipe : list) {
            if(recipe instanceof CastingRecipe) {
                if(((CastingRecipe) recipe).getResult() == null) {
                    continue;
                }
                if(!matches(output, toIItemStack(((CastingRecipe) recipe).getResult()))) {
                    continue;
                }
                
                if(!matches(liquid, toILiquidStack(((CastingRecipe) recipe).getFluid()))) {
                    
                    continue;
                }
                if((((CastingRecipe) recipe).cast != null && cast != null) && (((CastingRecipe) recipe).cast.matches(toStacks(cast.getItems().toArray(new IItemStack[0]))) == null)) {
                    continue;
                }
                
                recipes.add((CastingRecipe) recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(list, recipes, table));
        } else
        
        {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s, material %s and cast %s. Command ignored!", Casting.name, output.toString(), liquid.toString(), cast != null ? cast.toString() : null));
        }
    }
    
    // Removes all matching recipes, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval<ICastingRecipe> {
        
        final boolean table;
        
        public Remove(List<ICastingRecipe> list, List<ICastingRecipe> recipes, boolean table) {
            super(Casting.name, list, recipes);
            this.table = table;
        }
        
        @Override
        protected String getRecipeInfo(ICastingRecipe recipe) {
            return LogHelper.getStackDescription(((CastingRecipe) recipe).getResult());
        }
        
        @Override
        public IRecipeWrapper wrapRecipe(ICastingRecipe recipe) {
            IFocus<ItemStack> focus = JEIAddonPlugin.recipeRegistry.createFocus(IFocus.Mode.OUTPUT, ((CastingRecipe) recipe).getResult());
            List<IRecipeCategory> categories = JEIAddonPlugin.recipeRegistry.getRecipeCategories(focus);
            Iterator var4 = categories.iterator();
            outer:
            while(true) {
                IRecipeCategory category;
                do {
                    if(!var4.hasNext()) {
                        if(table) {
                            return new CastingRecipeWrapper((CastingRecipe) recipe, JEIAddonPlugin.castingTable);
                        } else {
                            return new CastingRecipeWrapper((CastingRecipe) recipe, JEIAddonPlugin.castingBasin);
                        }
                    }
                    
                    category = (IRecipeCategory) var4.next();
                } while(!category.getUid().equals("tconstruct.casting_table"));
                
                List<IRecipeWrapper> wrappers = JEIAddonPlugin.recipeRegistry.getRecipeWrappers(category, focus);
                Iterator var7 = wrappers.iterator();
                
                while(var7.hasNext()) {
                    CastingRecipeWrapper wrapper = (CastingRecipeWrapper) var7.next();
                    if(StackHelper.matches(new MCItemStack(wrapper.lazyInitOutput().get(0)), new MCItemStack(((CastingRecipe) recipe).getResult()))) {
                        return wrapper;
                    }
                    
                }
            }
        }
    }
}
