package com.blamejared.compat.bloodmagic;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import WayofTime.bloodmagic.potion.BMPotionUtils;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTablePotionRecipe;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTableRecipe;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.potions.IPotionEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import stanhebben.zenscript.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ZenClass("mods.bloodmagic.AlchemyTable")
@ZenRegister
@ModOnly("bloodmagic")
public class AlchemyTable {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] inputs, int syphon, int ticks, int minTier) {
        if(inputs.length == 0 || inputs.length > 6) {
            CraftTweakerAPI.logError("Invalid Input Array! Maximum size is 6!");
            return;
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), syphon, ticks, minTier, InputHelper.toObjects(inputs)));
    }

    @ZenMethod
    public static void addPotionRecipe(IItemStack[] inputs, IPotionEffect effects, int syphon, int ticks, int minTier) {
        if (inputs.length == 0 || inputs.length > 5) {
            CraftTweakerAPI.logError("Invalid Input Array! Maximum size is 5 (to account for catalysts)!");
            return;
        }

        List<ItemStack> inputsNew = withoutFlask(Arrays.asList(InputHelper.toStacks(inputs)));

        ModTweaker.LATE_ADDITIONS.add(new AddPotion(inputsNew, CraftTweakerMC.getPotionEffect(effects), syphon, ticks, minTier));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack[] inputs) {
        if(inputs.length == 0 || inputs.length > 6) {
            CraftTweakerAPI.logError("Invalid Input Array! Maximum size is 6!");
            return;
        }
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStacks(inputs)));
    }

    public static List<ItemStack> withoutFlask (List<ItemStack> inputs) {
        List<ItemStack> inputsNew = new ArrayList<>();
        for (ItemStack item : inputs) {
            if (item.getItem() == RegistrarBloodMagicItems.POTION_FLASK) {
                continue;
            }

            inputsNew.add(item);
        }

        return inputsNew;
    }

    private static class Add extends BaseAction {
        private ItemStack output;
        private int syphon, ticks, minTier;
        private Object[] inputs;
        
        public Add(ItemStack output, int syphon, int ticks, int minTier, Object[] inputs) {
            super("AlchemyTable");
            this.output = output;
            this.syphon = syphon;
            this.ticks = ticks;
            this.minTier = minTier;
            this.inputs = inputs;
        }
        
        @Override
        public void apply() {
            BloodMagicAPI.INSTANCE.getRecipeRegistrar().addAlchemyTable(output, syphon, ticks, minTier, inputs);
        }
        
        
        @Override
        public String describe() {
            return "Adding AlchemyTable recipe for: " + output + " from: [" + String.join(",", getStringFromStacks(inputs)) + "] syphon: " + syphon + ", ticks: " + ticks + ", minTier: " + minTier;
        }
    }

    private static class AddPotion extends BaseAction {
        private int syphon, ticks, minTier;
        private List<ItemStack> inputs;
        private PotionEffect effect;

        public AddPotion(List<ItemStack> inputs, PotionEffect effect, int syphon, int ticks, int minTier) {
            super("AlchemyTable");
            this.inputs = inputs;
            this.effect = effect;
            this.syphon = syphon;
            this.ticks = ticks;
            this.minTier = minTier;
        }

        @Override
        public void apply() {
            AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTablePotionRecipe(syphon, ticks, minTier, inputs, effect));

            List<ItemStack> lengtheningList = new ArrayList<>();
            lengtheningList.addAll(inputs);

            lengtheningList.add(ComponentTypes.CATALYST_LENGTH_1.getStack());
            AlchemyTableRecipeRegistry.registerRecipe(BMPotionUtils.getLengthAugmentRecipe(syphon, ticks,  minTier, lengtheningList, effect, 1));

            List<ItemStack> powerList = new ArrayList<>();
            powerList.addAll(inputs);
            powerList.add(ComponentTypes.CATALYST_POWER_1.getStack());
            AlchemyTableRecipeRegistry.registerRecipe(BMPotionUtils.getPowerAugmentRecipe(syphon, ticks, minTier, powerList, effect, 1));
        }

        @Override
        public String describe() {
            return "Adding AlchemyTable potion recipe for: [" + effect.getEffectName() + "] with input: [" + String.join(",", getStringFromStacks(inputs.toArray())) + "] syphon: " + syphon + ", ticks: " + ticks + ", minTier: " + minTier;
        }
    }
    
    public static String[] getStringFromStacks(Object[] arr) {
        String[] retArr = new String[arr.length];
        for(int i = 0; i < arr.length; i++) {
            retArr[i] = arr[i].toString();
        }
        return retArr;
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack[] inputs;
        
        public Remove(ItemStack[] inputs) {
            super("AlchemyTable");
            this.inputs = inputs;
        }
        
        @Override
        public void apply() {
            if (!BloodMagicAPI.INSTANCE.getRecipeRegistrar().removeAlchemyTable(inputs)) {
                // potentially this is a potion
                List<ItemStack> inputsList = new ArrayList<>(Arrays.asList(inputs));
                AlchemyTableRecipe recipe = AlchemyTableRecipeRegistry.getMatchingRecipe(inputsList, null, null);
                if (recipe == null) { return; }

                AlchemyTableRecipeRegistry.removeRecipe(recipe);
                // if it's a potion properly added, there's length and power crystal recipes
                List<ItemStack> newInputsList = withoutFlask(inputsList);

                newInputsList.add(recipe.getRecipeOutput(inputsList));
                newInputsList.add(ComponentTypes.CATALYST_POWER_1.getStack());

                recipe = AlchemyTableRecipeRegistry.getMatchingRecipe(newInputsList, null, null);
                if (recipe != null) {
                    AlchemyTableRecipeRegistry.removeRecipe(recipe);
                }

                newInputsList.remove(ComponentTypes.CATALYST_POWER_1.getStack());
                newInputsList.add(ComponentTypes.CATALYST_LENGTH_1.getStack());

                recipe = AlchemyTableRecipeRegistry.getMatchingRecipe(newInputsList, null, null);
                if (recipe != null) {
                    AlchemyTableRecipeRegistry.removeRecipe(recipe);
                }
            }
        }

        @Override
        public String describe() {
            return "Removing AlchemyTable recipe for: [" + String.join(",", getStringFromStacks(inputs)) + "]";
        }
    }
}
