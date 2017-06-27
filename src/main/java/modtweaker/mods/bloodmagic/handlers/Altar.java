package modtweaker.mods.bloodmagic.handlers;

import WayofTime.bloodmagic.api.*;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry.AltarRecipe;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeJEI;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import modtweaker.JEIAddonPlugin;
import modtweaker.mods.bloodmagic.BloodMagicHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import stanhebben.zenscript.annotations.*;

import java.util.*;
import java.util.Map.Entry;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.bloodmagic.Altar")
public class Altar
{
    protected static final String name = "Blood Magic Altar";

    private static final EnumAltarTier[] altarTiers = EnumAltarTier.values();

    @ZenMethod
    public static void addRecipe(IItemStack output, int minTier, int syphon, int consumeRate, int drainRate, IItemStack[] input)
    {
        if (output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        else if(minTier <= 0 || minTier > altarTiers.length)
        {
            LogHelper.logWarning(String.format("Invalid altar tier (%d) required for %s Recipe", minTier, Altar.name));
            return;
        }
        else if(syphon < 0)
        {
            LogHelper.logWarning(String.format("Syphon can't be below 0 (%d) for %s Recipe", syphon, Altar.name));
            return;
        }
        else if(consumeRate < 0)
        {
            LogHelper.logWarning(String.format("Consume rate can't be below 0 (%d) for %s Recipe", consumeRate, Altar.name));
            return;
        }
        else if(drainRate < 0)
        {
            LogHelper.logWarning(String.format("Drain rate can't be below 0 (%d) for %s Recipe", drainRate, Altar.name));
            return;
        }

        List<ItemStack> inputs = Arrays.asList(toStacks(input));
        AltarRecipeRegistry.AltarRecipe temp = new AltarRecipeRegistry.AltarRecipe(inputs, toStack(output), altarTiers[minTier-1], syphon, consumeRate, drainRate);
        MineTweakerAPI.apply(new Add(temp.getInput(), temp, BloodMagicHelper.altarBiMap));
    }

    private static class Add extends BaseMapAddition<List<ItemStackWrapper>, AltarRecipe>
    {
        public Add(List<ItemStackWrapper> inputs, AltarRecipe altarRecipe, Map<List<ItemStackWrapper>, AltarRecipe> map)
        {
            super(Altar.name, map);
            this.recipes.put(inputs, altarRecipe);
        }


        @Override
        public void apply() {
            if(recipes.isEmpty())
                return;

            for(Entry<List<ItemStackWrapper>, AltarRecipe> entry : recipes.entrySet()) {
                List<ItemStackWrapper> key = entry.getKey();
                AltarRecipe value = entry.getValue();
                AltarRecipe oldValue = map.put(key, value);

                if(oldValue != null) {
                    LogHelper.logWarning(String.format("Overwritten %s Recipe for %s", name, getRecipeInfo( new AbstractMap.SimpleEntry<List<ItemStackWrapper>, AltarRecipe>(entry.getKey(), value))));
                    overwritten.put(key, oldValue);
                }

                successful.put(key, value);
                List input = ItemStackWrapper.toStackList(value.getInput());
                ItemStack output = (value).getOutput();
                int requiredTier = (value).getMinTier().toInt();
                int requiredLP = (value).getSyphon();
                int consumptionRate = (value).getConsumeRate();
                int drainRate = (value).getDrainRate();
                if(output.getItem() == ForgeModContainer.getInstance().universalBucket && requiredLP == 1000) {
                    output = BloodMagicAPI.getLifeEssenceBucket();
                }

                AltarRecipeJEI recipe = new AltarRecipeJEI(input, output, requiredTier, requiredLP, consumptionRate, drainRate);
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(recipe);
            }
        }

        @Override
        public void undo() {
            if(successful.isEmpty() && overwritten.isEmpty())
                return;

            for(Entry<List<ItemStackWrapper>, AltarRecipe> entry : successful.entrySet()) {
                List<ItemStackWrapper> key = entry.getKey();
                AltarRecipe value = map.remove(key);

                if(value == null) {
                    LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
                }else {
                    successful.put(key, value);
                    if (JEIAddonPlugin.recipeRegistry != null) {

                        List<AltarRecipeJEI> list = JEIAddonPlugin.recipeRegistry.getRecipeWrappers(JEIAddonPlugin.recipeRegistry.getRecipeCategories(Arrays.asList("BloodMagic:altar")).get(0));
                        final AltarRecipeJEI[] recipe = {null};
                        list.forEach(rec -> {
                            ItemStack input = ((List<ItemStack>) ReflectionHelper.getFinalObject(rec, "input")).get(0);
                            ItemStack output = ReflectionHelper.getFinalObject(rec, "output");
                            if (input.isItemEqual(value.getInput().get(0).toStack()) && output.isItemEqual(value.getOutput())) {
                                recipe[0] = rec;
                            }
                        });
                        if (recipe[0] != null)
                            MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(recipe[0]);
                    }
                }
            }

            for(Entry<List<ItemStackWrapper>, AltarRecipe> entry : overwritten.entrySet()) {
                List<ItemStackWrapper> key = entry.getKey();
                AltarRecipe value = entry.getValue();
                AltarRecipe oldValue = map.put(key, value);

                if(oldValue != null) {
                    LogHelper.logWarning(String.format("Overwritten %s Recipe which should not exist for %s", name, getRecipeInfo(new AbstractMap.SimpleEntry<List<ItemStackWrapper>, AltarRecipe>(entry.getKey(), value))));
                }
            }
        }
        @Override
        public String getRecipeInfo(Entry<List<ItemStackWrapper>, AltarRecipe> recipe)
        {
            ItemStack output = ReflectionHelper.getFinalObject(recipe.getValue(), "output");
            return LogHelper.getStackDescription(output);
        }
    }

    @ZenMethod
    public static void removeRecipe(IIngredient output)
    {
        remove(output, BloodMagicHelper.altarBiMap);
    }

    public static void remove(IIngredient output, Map<List<ItemStackWrapper>, AltarRecipe> map)
    {
        if (output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }

        Map<List<ItemStackWrapper>, AltarRecipe> recipes = new HashMap<>();

        for(AltarRecipe altarRecipe : map.values())
        {
            ItemStack recipeOutput = ReflectionHelper.getFinalObject(altarRecipe, "output");
            if(matches(output, toIItemStack(recipeOutput))) {
                recipes.put(altarRecipe.getInput(), altarRecipe);
            }
        }

        if(!recipes.isEmpty())
        {
            MineTweakerAPI.apply(new Remove(map, recipes));
        }
        else
        {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", Altar.name, output.toString()));
        }
    }

    private static class Remove extends BaseMapRemoval<List<ItemStackWrapper>, AltarRecipe>
    {
        public Remove(Map<List<ItemStackWrapper>, AltarRecipe> map, Map<List<ItemStackWrapper>, AltarRecipe> inputs)
        {
            super(Altar.name, map, inputs);
        }


        @Override
        public void apply() {
            if(recipes.isEmpty())
                return;

            for(List<ItemStackWrapper> key : recipes.keySet()) {
                AltarRecipe oldValue = map.remove(key);
                if(oldValue != null) {
                    successful.put(key, oldValue);
                    if (JEIAddonPlugin.recipeRegistry != null) {
                        List<AltarRecipeJEI> list = JEIAddonPlugin.recipeRegistry.getRecipeWrappers(JEIAddonPlugin.recipeRegistry.getRecipeCategories(Arrays.asList("BloodMagic:altar")).get(0));
                        final AltarRecipeJEI[] recipe = {null};

                        list.forEach(rec -> {
                            ItemStack input = ((List<ItemStack>) ReflectionHelper.getFinalObject(rec, "input")).get(0);
                            ItemStack output = ReflectionHelper.getFinalObject(rec, "output");
                            if (input.isItemEqual(oldValue.getInput().get(0).toStack()) && output.isItemEqual(oldValue.getOutput())) {
                                recipe[0] = rec;
                            }
                        });
                        if (recipe[0] != null)
                            MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(recipe[0]);
                }



                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe : null object", name));
                }
            }
        }

        @Override
        public void undo() {
            if(successful.isEmpty())
                return;

            for(Entry<List<ItemStackWrapper>, AltarRecipe> entry : successful.entrySet()) {
                if(entry != null) {
                    AltarRecipe oldValue = map.put(entry.getKey(), entry.getValue());
                    if(oldValue != null) {
                        LogHelper.logWarning(String.format("Overwritten %s Recipe for %s while restoring.", name, getRecipeInfo(entry)));
                    }else{
                        List input = ItemStackWrapper.toStackList(entry.getValue().getInput());
                        ItemStack output = (entry).getValue().getOutput();
                        int requiredTier = (entry).getValue().getMinTier().toInt();
                        int requiredLP = (entry).getValue().getSyphon();
                        int consumptionRate = (entry).getValue().getConsumeRate();
                        int drainRate = (entry).getValue().getDrainRate();
                        if(output.getItem() == ForgeModContainer.getInstance().universalBucket && requiredLP == 1000) {
                            output = BloodMagicAPI.getLifeEssenceBucket();
                        }

                        AltarRecipeJEI recipe = new AltarRecipeJEI(input, output, requiredTier, requiredLP, consumptionRate, drainRate);
                        MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(recipe);
                    }
                }
            }
        }

        @Override
        public String getRecipeInfo(Entry<List<ItemStackWrapper>, AltarRecipe> recipe)
        {
            ItemStack output = ReflectionHelper.getFinalObject(recipe.getValue(), "output");
            return LogHelper.getStackDescription(output);
        }
    }
}
