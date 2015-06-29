package modtweaker2.mods.fsp.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;
import static modtweaker2.mods.fsp.FSPHelper.getLiquid;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.MutablePair;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.Tuple3;

@ZenClass("mods.fsp.Crucible")
public class Crucible {
    
    public static final String nameLiquid = "FSP Crucible (Liquid)";
    public static final String nameMelting = "FSP Crucible (Liquid)";
    public static final String nameDunking = "FSP Crucible (Dunking)";
    
    @ZenMethod
    public static void addLiquid(String name, IItemStack ingot, IItemStack plate, IItemStack nugget, int r, int g, int b) {
        MineTweakerAPI.apply(new AddLiquid(new CrucibleLiquid(name, toStack(ingot), toStack(plate), toStack(nugget), null, r, g, b)));
    }

    @ZenMethod
    public static void addLiquid(String name, IItemStack ingot, IItemStack plate, IItemStack nugget, int r, int g, int b, String l1, int n1, String l2, int n2, int n3) {
        MineTweakerAPI.apply(new AddLiquid(new CrucibleLiquid(name, toStack(ingot), toStack(plate), toStack(nugget), new CrucibleFormula(getLiquid(l1), n1, getLiquid(l2), n2, n3), r, g, b)));
    }

    private static class AddLiquid extends BaseListAddition<CrucibleLiquid> {
        public AddLiquid(CrucibleLiquid recipe) {
            super(nameLiquid, SteamcraftRegistry.liquids);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(CrucibleLiquid recipe) {
            return recipe.name;
        }
    }

    @ZenMethod
    public static void addMelting(IItemStack input, String liquid, int volume) {
        ItemStack stack = toStack(input);
        CrucibleLiquid fluid = getLiquid(liquid);
        if (fluid != null) {
            MineTweakerAPI.apply(new AddMelting(stack, MutablePair.of(stack.getItem(), stack.getItemDamage()), MutablePair.of(fluid, volume)));
        }
    }

    private static class AddMelting extends BaseMapAddition<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> {
        public AddMelting(ItemStack stack, MutablePair<Item, Integer> key, MutablePair<CrucibleLiquid, Integer> recipe) {
            super(nameMelting, SteamcraftRegistry.smeltThings);
            map.put(key, recipe);
        }

        @Override
        protected String getRecipeInfo(Entry<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> recipe) {
            return InputHelper.getStackDescription(new ItemStack(recipe.getKey().left, 1, recipe.getKey().right));
        }
    }

    @ZenMethod
    public static void removeMelting(IIngredient input) {
        Map<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> recipes = new HashMap<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>>();
        
        for(Entry<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> entry : SteamcraftRegistry.smeltThings.entrySet()) {
            if(matches(input, toIItemStack(new ItemStack(entry.getKey().left, 1, entry.getKey().right)))) {
                recipes.put(entry.getKey(), entry.getValue());
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMelting(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipes found for %s", Crucible.nameMelting, input));
        }
    }

    private static class RemoveMelting extends BaseMapRemoval<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> {
        public RemoveMelting(Map<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> recipes) {
            super(nameMelting, SteamcraftRegistry.smeltThings, recipes);
        }

        @Override
        protected String getRecipeInfo(Entry<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> recipe) {
            return InputHelper.getStackDescription(new ItemStack(recipe.getKey().left, 1, recipe.getKey().right));
        }
    }

    @ZenMethod
    public static void addDunking(IItemStack input, String liquid, int volume, IItemStack output) {
        ItemStack stack = toStack(input);
        CrucibleLiquid fluid = getLiquid(liquid);
        if (fluid != null) {
            MineTweakerAPI.apply(new AddDunking(new Tuple3(stack.getItem(), stack.getItemDamage(), fluid), MutablePair.of(volume, toStack(output))));
        }
    }

    private static class AddDunking extends BaseMapAddition<Tuple3, MutablePair<Integer, ItemStack>> {
        public AddDunking(Tuple3 key, MutablePair<Integer, ItemStack> recipe) {
            super(Crucible.nameDunking, SteamcraftRegistry.dunkThings);
            recipes.put(key, recipe);
        }

        @Override
        public String getRecipeInfo(Entry<Tuple3, MutablePair<Integer, ItemStack>> recipe) {
            return InputHelper.getStackDescription(recipe.getValue().right);
        }
    }

    @ZenMethod
    public static void removeDunking(IIngredient input, String liquid) {
        Map<Tuple3, MutablePair<Integer, ItemStack>> recipes = new HashMap<Tuple3, MutablePair<Integer, ItemStack>>();
        
        for(Entry<Tuple3, MutablePair<Integer, ItemStack>> recipe : SteamcraftRegistry.dunkThings.entrySet()) {
            if(recipe.getValue() != null && recipe.getValue().right != null && matches(input, toIItemStack(recipe.getValue().right))) {
                if(((CrucibleLiquid)recipe.getKey().third).equals(getLiquid(liquid))) {
                    recipes.put(recipe.getKey(), recipe.getValue());
                }
            }
                
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveDunking(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s and %s. Command ignored!", Crucible.nameDunking, input.toString(), liquid));
        }
    }

    private static class RemoveDunking extends BaseMapRemoval<Tuple3, MutablePair<Integer, ItemStack>> {
        public RemoveDunking(Map<Tuple3, MutablePair<Integer, ItemStack>> recipes) {
            super(Crucible.nameDunking, SteamcraftRegistry.dunkThings, recipes);
        }

        @Override
        public String getRecipeInfo(Entry<Tuple3, MutablePair<Integer, ItemStack>> recipe) {
            return InputHelper.getStackDescription(recipe.getValue().right);
        }
    }
}
