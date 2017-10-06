package com.blamejared.compat.tconstruct;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tconstruct.recipes.MeltingRecipeTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.annotations.Optional;

import java.util.*;

@ZenClass("mods.tconstruct.Melting")
@ZenRegister
@ModOnly("tconstruct")
public class Melting {
    
    public static final List<ILiquidStack> REMOVED_RECIPES = new LinkedList<>();
    private static boolean init = false;
    
    private static void init() {
        if(!init) {
            MinecraftForge.EVENT_BUS.register(new Melting());
            init = true;
        }
    }
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, IItemStack input, @Optional int temp) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Melting.Add(InputHelper.toFluid(output), InputHelper.toStack(input), temp));
    }
    
    @ZenMethod
    public static void removeRecipe(ILiquidStack output) {
        init();
        CraftTweakerAPI.apply(new Melting.Remove(output));
    }
    
    private static class Add extends BaseUndoable {
        
        private FluidStack output;
        private ItemStack input;
        private int temp;
        
        public Add(FluidStack output, ItemStack input, int temp) {
            super("Melting");
            this.output = output;
            this.input = input;
            this.temp = temp;
        }
        
        @Override
        public void apply() {
            if(temp != 0)
                TinkerRegistry.registerMelting(new MeltingRecipeTweaker(RecipeMatch.of(input, output.amount), output, temp));
            else
                TinkerRegistry.registerMelting(new MeltingRecipeTweaker(RecipeMatch.of(input, output.amount), output));
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private ILiquidStack output;
        
        protected Remove(ILiquidStack output) {
            super("Melting");
            this.output = output;
        }
        
        @Override
        public void apply() {
            REMOVED_RECIPES.add(output);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    @SubscribeEvent
    public void onTinkerRegister(TinkerRegisterEvent.MeltingRegisterEvent event) {
        if(event.getRecipe() instanceof MeltingRecipeTweaker) {
            return;
        }
        for(ILiquidStack stack : REMOVED_RECIPES) {
            if(event.getRecipe().getResult().isFluidEqual(((FluidStack) stack.getInternal()))) {
                event.setCanceled(true);
            }
        }
    }
}
