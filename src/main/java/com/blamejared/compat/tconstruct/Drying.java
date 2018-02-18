package com.blamejared.compat.tconstruct;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tconstruct.recipes.DryingRecipeTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;
import java.util.stream.Collectors;

@ZenClass("mods.tconstruct.Drying")
@ZenRegister
@ModOnly("tconstruct")
public class Drying {
    
    public static final Map<IItemStack, IItemStack> DRYING_RECIPES = new LinkedHashMap<>();
    private static boolean init = false;
    
    private static void init() {
        if(!init) {
            MinecraftForge.EVENT_BUS.register(new Drying());
            init = true;
        }
    }
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input, int time) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), input, time));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output, @Optional IItemStack input) {
        init();
        CraftTweakerAPI.apply(new Remove(output, input));
    }
    
    private static class Add extends BaseAction {
        
        private final ItemStack output;
        private final IIngredient ingredient;
        private final int time;
        
        public Add(ItemStack output, IIngredient input, int time) {
            super("Drying");
            this.output = output;
            this.ingredient = input;
            this.time = time;
        }
        
        @Override
        public void apply() {
            List<ItemStack> validIngredients = ingredient.getItems().stream().map(CraftTweakerMC::getItemStack).collect(Collectors.toList());
            if(validIngredients.isEmpty())
                CraftTweakerAPI.logInfo("Could not find matching items for " + ingredient.toString() + ". Ignoring " + name + " recipe for " + output.getDisplayName());
            else
                TinkerRegistry.addDryingRecipe(new DryingRecipeTweaker(RecipeMatch.of(validIngredients), output, time));
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private IItemStack output;
        private IItemStack input;
        
        
        protected Remove(IItemStack output, IItemStack input) {
            super("Drying");
            this.output = output;
            this.input = input;
        }
        
        @Override
        public void apply() {
            DRYING_RECIPES.put(output, input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    @SubscribeEvent
    public void onTinkerRegister(TinkerRegisterEvent.DryingRackRegisterEvent event) {
        if(event.getRecipe() instanceof DryingRecipeTweaker) {
            return;
        }
        for(Map.Entry<IItemStack, IItemStack> ent : DRYING_RECIPES.entrySet()) {
            if(ent.getKey().matches(InputHelper.toIItemStack(event.getRecipe().getResult()))) {
                if(ent.getValue() != null) {
                    if(event.getRecipe().input.matches(NonNullList.withSize(1, (ItemStack) ent.getValue().getInternal())).isPresent()) {
                        event.setCanceled(true);
                    }
                } else
                    event.setCanceled(true);
            }
        }
    }
}
