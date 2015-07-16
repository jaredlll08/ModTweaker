package modtweaker2.helpers;

import java.util.Arrays;
import java.util.List;

import mekanism.api.gas.GasStack;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.mc1710.item.MCItemStack;
import modtweaker2.mods.mekanism.gas.MCGasStack;
import modtweaker2.utils.TweakerPlugin;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class LogHelper {
    public static void logPrinted(IPlayer player) {
        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }

    public static void log(IPlayer player, String message) {
        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage(message));
        }
    }

    public static void print(String string) {
        System.out.println(string);
        MineTweakerAPI.logCommand(string);
    }
    
    public static void logError(String message) {
        MineTweakerAPI.logError("[ModTweaker2] " + message);
    }
    
    public static void logError(String message, Throwable exception) {
        MineTweakerAPI.logError("[ModTweaker2] " + message, exception);
    }
    
    public static void logWarning(String message) {
        MineTweakerAPI.logWarning("[ModTweaker2] " + message);
    }
    
    public static void logInfo(String message) {
        MineTweakerAPI.logInfo("[ModTweaker2] " + message);
    }

    /**
     * Returns a string representation of the item which can also be used in scripts
     */
    public static String getStackDescription(Object object) {
        if(object instanceof ItemStack) {
            return new MCItemStack((ItemStack)object).toString();
        } else if (object instanceof FluidStack) {
            return getStackDescription((FluidStack)object);
        } else if (object instanceof Block) {
            return new MCItemStack(new ItemStack((Block)object, 1, 0)).toString();
        } else if (TweakerPlugin.isLoaded("Mekanism") && object instanceof GasStack) {
            return new MCGasStack((GasStack)object).toString();
        } else if (object instanceof String) {
            // Check if string specifies an oredict entry
            List<ItemStack> ores = OreDictionary.getOres((String)object);

            if(!ores.isEmpty()) {
                return "<ore:" + (String)object + ">";
            } else {
                return "\"" + (String)object + "\"";
            }
        } else if (object != null) {
            return "\"" + object.toString() + "\"";
        } else {
            return "null";
        }
    }
    
    public static String getStackDescription(FluidStack stack) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<liquid:").append(stack.getFluid().getName()).append('>');
        
        if(stack.amount > 1) {
            sb.append(" * ").append(stack.amount);
        }
        
        return sb.toString();
    }
    
    public static String getListDescription(List<?> objects) {
        StringBuilder sb = new StringBuilder();
        
        if(objects.isEmpty()) {
            sb.append("[]");
        } else {
            sb.append('[');
            for(Object object : objects) {
                if(object instanceof List) {
                    sb.append(getListDescription((List)object)).append(", ");
                } else if(object instanceof Object[]) {
                    sb.append(getListDescription(Arrays.asList((Object[])object))).append(", ");
                }  else {
                    sb.append(getStackDescription(object)).append(", ");
                }
            }
            sb.setLength(sb.length() - 2);
            sb.append(']');
        }
        
        return sb.toString();
    }

    public static String getCraftingDescription(IRecipe recipe) {
        if(recipe instanceof ShapelessOreRecipe)
            return LogHelper.getCraftingDescription((ShapelessOreRecipe)recipe);
        else if(recipe instanceof ShapedOreRecipe)
            return LogHelper.getCraftingDescription((ShapedOreRecipe)recipe);
        else if(recipe instanceof ShapelessRecipes)
            return LogHelper.getCraftingDescription((ShapelessRecipes)recipe);
        else if(recipe instanceof ShapedRecipes)
            return LogHelper.getCraftingDescription((ShapedRecipes)recipe);
        
        return recipe.toString();
    }

    public static String getCraftingDescription(ShapelessOreRecipe recipe) {
        return getListDescription(recipe.getInput());
    }

    public static String getCraftingDescription(ShapelessRecipes recipe) {
        return getListDescription(recipe.recipeItems);
    }

    public static String getCraftingDescription(ShapedOreRecipe recipe) {
        int height = ReflectionHelper.<Integer>getObject(recipe, "width");
        int width = ReflectionHelper.<Integer>getObject(recipe, "height");
        
        Object[][] recipes = InputHelper.getMultiDimensionalArray(Object.class, recipe.getInput(), height, width);
        
        return getListDescription(Arrays.asList(recipes));
    }

    public static String getCraftingDescription(ShapedRecipes recipe) {
        ItemStack[][] recipes = InputHelper.getMultiDimensionalArray(ItemStack.class, recipe.recipeItems, recipe.recipeHeight, recipe.recipeWidth);
        return getListDescription(Arrays.asList(recipes));
    }
}
