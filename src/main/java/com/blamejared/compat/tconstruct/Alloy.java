package com.blamejared.compat.tconstruct;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tconstruct.recipes.AlloyRecipeTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

@ZenClass("mods.tconstruct.Alloy")
@ZenRegister
@ModOnly("tconstruct")
public class Alloy {
    
    public static final List<ILiquidStack> REMOVED_RECIPES = new LinkedList<>();
    private static boolean init = false;
    
    private static void init() {
        if(!init) {
            MinecraftForge.EVENT_BUS.register(new Alloy());
            init = true;
        }
    }
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, ILiquidStack[] inputs) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Alloy.Add(InputHelper.toFluid(output), InputHelper.toFluids(inputs)));
    }
    
    @ZenMethod
    public static void removeRecipe(ILiquidStack output) {
        init();
        CraftTweakerAPI.apply(new Alloy.Remove(output));
    }
    
    private static class Add extends BaseUndoable {
        
        private FluidStack output;
        private FluidStack[] input;
        
        public Add(FluidStack output, FluidStack[] input) {
            super("Alloy");
            this.output = output;
            this.input = input;
        }
        
        @Override
        public void apply() {
            TinkerRegistry.registerAlloy(new AlloyRecipeTweaker(output, input));
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private ILiquidStack output;
        
        protected Remove(ILiquidStack output) {
            super("Alloy");
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
    public void onTinkerRegister(TinkerRegisterEvent.AlloyRegisterEvent event) {
        if(event.getRecipe() instanceof AlloyRecipeTweaker) {
            return;
        }
        for(ILiquidStack stack : REMOVED_RECIPES) {
            if(event.getRecipe().getResult().isFluidEqual(((FluidStack)stack.getInternal()))) {
                event.setCanceled(true);
            }
        }
    }
}
