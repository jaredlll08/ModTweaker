package modtweaker2.mods.tconstruct.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.CastingRecipe;

@ZenClass("mods.tconstruct.Casting")
public class Casting {
    //Adding a TConstruct Casting recipe
    @ZenMethod
    public static void addBasinRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast, @Optional boolean consume, int delay) {
        MineTweakerAPI.apply(new Add(new CastingRecipe(toStack(output), toFluid(metal), toStack(cast), consume, delay, null), TConstructHelper.basinCasting));
    }

    @ZenMethod
    public static void addTableRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast, @Optional boolean consume, int delay) {
        MineTweakerAPI.apply(new Add(new CastingRecipe(toStack(output), toFluid(metal), toStack(cast), consume, delay, null), TConstructHelper.tableCasting));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(CastingRecipe recipe, ArrayList list) {
            super("TConstruct Casting", list, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((CastingRecipe) recipe).output.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a TConstruct Casting recipe
    @ZenMethod
    public static void removeBasinRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove((toStack(output)), TConstructHelper.basinCasting, RecipeComponent.Output));
    }

    @ZenMethod
    public static void removeTableRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove((toStack(output)), TConstructHelper.tableCasting, RecipeComponent.Output));
    }
    
    @ZenMethod
    public static void removeBasinLiquid(ILiquidStack fluid) {
        MineTweakerAPI.apply(new Remove((toFluid(fluid)), TConstructHelper.basinCasting));
    }

    @ZenMethod
    public static void removeTableLiquid(ILiquidStack fluid) {
        MineTweakerAPI.apply(new Remove((toFluid(fluid)), TConstructHelper.tableCasting));
    }

    @ZenMethod
    public static void removeCastRecipes(IItemStack cast) {
        MineTweakerAPI.apply(new Remove((toStack(cast)), TConstructHelper.tableCasting, RecipeComponent.Cast));
    }

    // Removes all matching recipes, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        protected final LinkedList<CastingRecipe> removedRecipes;
        protected final RecipeComponent component;
        
        public Remove(ItemStack output, ArrayList list, RecipeComponent component) {
            super("TConstruct Casting", list, output);
            this.removedRecipes = new LinkedList<CastingRecipe>();
            this.component = component;
        }
        
        public Remove(FluidStack output, ArrayList list) {
        	super("TConstruct Casting", list, output);
        	this.removedRecipes = new LinkedList<CastingRecipe>();
        	this.component = RecipeComponent.Material;
        }

        // Loops through the registry, to find all items that matches, then removes them
        @Override
        public void apply() {
            for (Iterator<CastingRecipe> iterator = ((ArrayList<CastingRecipe>)list).iterator(); iterator.hasNext();) {
                CastingRecipe r = iterator.next();
                boolean removeRecipie = false;
                
                switch(component)
                {
                	case Cast:
                		if (r.cast != null && areEqual(r.cast, stack)) {
                			removeRecipie = true;
                        }

                		break;
                		
                	case Material:
                        if (r.castingMetal != null && r.castingMetal.isFluidEqual(fluid)) {
                        	removeRecipie = true;
                        }

                		break;
                		
                	case Output:
                		if (r.output != null && areEqual(r.output, stack)) {
                			removeRecipie = true;
                        }

                		break;
                }

                if(removeRecipie) {
                	iterator.remove();
                	removedRecipes.add(r);
                }
            }
        }
        
        @Override
        public void undo() {
            for(CastingRecipe recipe : removedRecipes) {
                this.list.add(recipe);
            }
            removedRecipes.clear();
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    public enum RecipeComponent {
    	Output,
    	Cast,
    	Material
    }
}
