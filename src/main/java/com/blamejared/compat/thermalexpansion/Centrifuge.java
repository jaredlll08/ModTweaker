package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.CentrifugeManager;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

import cofh.core.init.CoreProps;
import cofh.core.util.helpers.ItemHelper;
import cofh.thermalexpansion.item.ItemMorb;
import cofh.thermalfoundation.init.TFFluids;

import java.util.Arrays;
import java.util.ArrayList;

@ZenClass("mods.thermalexpansion.Centrifuge")
@ModOnly("thermalexpansion")
@ZenRegister
public class Centrifuge {
    
    @ZenMethod
    public static void addRecipe(WeightedItemStack[] outputs, IItemStack input, ILiquidStack fluid, int energy) {
        IItemStack[] items = new IItemStack[outputs.length];
        Integer[] chances = new Integer[outputs.length];
        for(int i = 0; i < outputs.length; i++) {
            items[i] = outputs[i].getStack();
            chances[i] = (int) outputs[i].getPercent();
        }
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStacks(items), chances, InputHelper.toStack(input), energy, InputHelper.toFluid(fluid)));
    }

    @ZenMethod
    public static void addRecipeMob(String entityId, WeightedItemStack[] outputs, ILiquidStack fluid, int energy, int xp) {
        IItemStack[] items = new IItemStack[outputs.length];
        Integer[] chances = new Integer[outputs.length];

        if (energy<=0) { 
            energy=CentrifugeManager.DEFAULT_ENERGY*2;
        }

        for(int i = 0; i < outputs.length; i++) {
            items[i] = outputs[i].getStack();
            chances[i] = (int) outputs[i].getPercent();
        }

        ModTweaker.LATE_ADDITIONS.add(new AddMob(InputHelper.toStacks(items), chances, entityId, energy, InputHelper.toFluid(fluid), xp));
    }

    @ZenMethod
    public static void addRecipeMob(IEntityDefinition entity, WeightedItemStack[] outputs, ILiquidStack fluid, int energy, int xp) {
        addRecipeMob(entity.getId(), outputs, fluid, energy, xp);
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }

    @ZenMethod
    public static void removeRecipeMob(String entityId) {
        ModTweaker.LATE_REMOVALS.add(new RemoveMob(entityId));
    }

    @ZenMethod
    public static void removeRecipeMob(IEntityDefinition entity) {
        ModTweaker.LATE_REMOVALS.add(new RemoveMob(entity.getId()));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack[] outputs;
        private Integer[] chances;
        private ItemStack input;
        private int energy;
        private FluidStack fluid;
        
        public Add(ItemStack[] outputs, Integer[] chances, ItemStack input, int energy, FluidStack fluid) {
            super("Centrifuge");
            this.outputs = outputs;
            this.chances = chances;
            this.input = input;
            this.energy = energy;
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            CentrifugeManager.addRecipe(energy, input, Arrays.asList(outputs), Arrays.asList(chances), fluid);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }

    private static class AddMob extends BaseAction {
        private ItemStack[] outputs;
        private Integer[] chances;
        private String entityId;
        private int energy;
        private FluidStack fluid;
        private int xp;

        public AddMob(ItemStack[] outputs, Integer[] chances, String entityId, int energy, FluidStack fluid, int xp) {
            super("Centrifuge");
            this.outputs = outputs;
            this.chances = chances;
            this.entityId = entityId;
            this.energy = energy;
            this.fluid = fluid;
            this.xp = xp;
        }

        @Override
        public void apply() {
            if (!ItemMorb.validMobs.contains(entityId)) {
                CraftTweakerAPI.logError("Not a valid morb entity: " + entityId);
                return;
            }

            if (fluid==null) { 
                this.fluid=new FluidStack(TFFluids.fluidExperience, xp * CoreProps.MB_PER_XP);
            }
           
            // This is copied from CoFH CentrifugeManager.java:255 onwards
            // It simplifies the need to create recipes for standard and
            // reusable morbs. Modified slightly.
            ArrayList<ItemStack> outputStandard = new ArrayList<ItemStack>(Arrays.asList(outputs));
            ArrayList<ItemStack> outputReusable = new ArrayList<ItemStack>(Arrays.asList(outputs));

            ArrayList<Integer> chanceStandard = new ArrayList<Integer>(Arrays.asList(chances));
            ArrayList<Integer> chanceReusable = new ArrayList<Integer>(Arrays.asList(chances));

            outputStandard.add(ItemHelper.cloneStack(ItemMorb.morbStandard));
            outputReusable.add(ItemHelper.cloneStack(ItemMorb.morbReusable));

            chanceStandard.add(ItemMorb.REUSE_CHANCE);
            chanceReusable.add(100);

            CentrifugeManager.addRecipeMob(energy, ItemMorb.setTag(ItemHelper.cloneStack(ItemMorb.morbStandard), this.entityId, false), outputStandard, chanceStandard, fluid);
            CentrifugeManager.addRecipeMob(energy, ItemMorb.setTag(ItemHelper.cloneStack(ItemMorb.morbReusable), this.entityId, false), outputReusable, chanceReusable, fluid);
        }

        @Override
        protected String getRecipeInfo() {
            return entityId;
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("Centrifuge");
            this.input = input;
        }
        
        @Override
        public void apply() {
            if(!CentrifugeManager.recipeExists(input)) {
                CraftTweakerAPI.logError("No Centrifuge recipe exists for: " + input);
                return;
            }
            CentrifugeManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }

    private static class RemoveMob extends BaseAction {
        private String entityId;

        public RemoveMob(String entityId) {
            super("Centrifuge");
            this.entityId = entityId;
        }

        @Override
        public void apply() {
            ItemStack standard = ItemMorb.setTag(ItemHelper.cloneStack(ItemMorb.morbStandard), entityId, false);
            ItemStack reusable =  ItemMorb.setTag(ItemHelper.cloneStack(ItemMorb.morbReusable), entityId, false);

            if (!CentrifugeManager.recipeExistsMob(standard) && !CentrifugeManager.recipeExistsMob(reusable)) {
                CraftTweakerAPI.logError("No Centrifuge mob recipe exists for entity: " + entityId);
                return;
            }

            // I think it's technically possible for there to be unmatched morbs
            if (CentrifugeManager.recipeExistsMob(standard)) {
                CentrifugeManager.removeRecipeMob(standard);
            }

            if (CentrifugeManager.recipeExistsMob(reusable)) {
                CentrifugeManager.removeRecipeMob(reusable);
            }
        }

        @Override
        protected String getRecipeInfo() {
            return entityId;
        }
    }
}
