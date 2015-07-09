package modtweaker2.mods.fsp.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.MutablePair;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import flaxbeard.steamcraft.api.SteamcraftRegistry;

@ZenClass("mods.fsp.Furnace")
public class Furnace {
    
    public static final String name = "FSP Furnace (Steamed Foods)";
    
    @ZenMethod
    public static void addSteamFood(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new AddSteamFood(toStack(input), MutablePair.of(toStack(input).getItem(), toStack(input).getItemDamage()), MutablePair.of(toStack(output).getItem(), toStack(output).getItemDamage())));
    }

    private static class AddSteamFood extends BaseMapAddition<MutablePair<Item, Integer>, MutablePair<Item, Integer>> {
        public AddSteamFood(ItemStack stack, MutablePair<Item, Integer> key, MutablePair<Item, Integer> recipe) {
            super(Furnace.name, SteamcraftRegistry.steamedFoods);
            recipes.put(key, recipe);
        }

        @Override
        public String getRecipeInfo(Entry<MutablePair<Item, Integer>, MutablePair<Item, Integer>> recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getKey().left, 1, recipe.getKey().right));
        }
    }

    @ZenMethod
    public static void removeSteamFood(IIngredient input) {
        Map<MutablePair<Item, Integer>, MutablePair<Item, Integer>> recipes = new HashMap<MutablePair<Item, Integer>, MutablePair<Item, Integer>>();
        
        for(Entry<MutablePair<Item, Integer>, MutablePair<Item, Integer>> recipe : SteamcraftRegistry.steamedFoods.entrySet()) {
            if(matches(input, toIItemStack(new ItemStack(recipe.getKey().left, 1, recipe.getKey().right)))) {
                recipes.put(recipe.getKey(), recipe.getValue());
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveSteamFood(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Furnace.name, input.toString()));
        }
    }

    private static class RemoveSteamFood extends BaseMapRemoval<MutablePair<Item, Integer>, MutablePair<Item, Integer>> {
        public RemoveSteamFood(Map<MutablePair<Item, Integer>, MutablePair<Item, Integer>> recipes) {
            super(Furnace.name, SteamcraftRegistry.steamedFoods, recipes);
        }

        @Override
        public String getRecipeInfo(Entry<MutablePair<Item, Integer>, MutablePair<Item, Integer>> recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getKey().left, 1, recipe.getKey().right));
        }
    }
}
