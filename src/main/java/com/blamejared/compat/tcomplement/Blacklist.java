package com.blamejared.compat.tcomplement;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tconstruct.recipes.MeltingRecipeTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import knightminer.tcomplement.library.TCompRegistry;
import knightminer.tcomplement.library.events.TCompRegisterEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.mantle.util.RecipeMatch;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.tcomplement.Blacklist")
@ZenRegister
@ModOnly("tcomplement")
public class Blacklist {
    
    
    public static final List<IItemStack> REMOVED_RECIPES = new LinkedList<>();
    private static boolean init = false;
    
    private static void init() {
        if(!init) {
            MinecraftForge.EVENT_BUS.register(new Blacklist());
            init = true;
        }
    }
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, IItemStack input) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Blacklist.Add(InputHelper.toFluid(output), InputHelper.toStack(input)));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        init();
        CraftTweakerAPI.apply(new Blacklist.Remove(input));
    }
    
    private static class Add extends BaseAction {
        
        private FluidStack output;
        private ItemStack input;
        
        public Add(FluidStack output, ItemStack input) {
            super("Blacklist");
            this.output = output;
            this.input = input;
        }
        
        @Override
        public void apply() {
            TCompRegistry.registerMelterBlacklist(RecipeMatch.of(input, output.amount));
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private IItemStack input;
        
        protected Remove(IItemStack input) {
            super("Blacklist");
            this.input = input;
        }
        
        @Override
        public void apply() {
            REMOVED_RECIPES.add(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
    
    @SubscribeEvent
    public void onTinkerRegister(TCompRegisterEvent.MelterBlackListRegisterEvent event) {
        if(event.getRecipe() instanceof MeltingRecipeTweaker) {
            return;
        }
        for(IItemStack ent : REMOVED_RECIPES) {
            if(event.getRecipe().matches(InputHelper.toStack(ent))) {
                event.setCanceled(true);
            }
        }
    }
}
