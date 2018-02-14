package com.blamejared.compat.tconstruct;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tconstruct.recipes.AlloyRecipeTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.tconstruct.Alloy")
@ZenRegister
@ModOnly("tconstruct")
public class Alloy {
    
    public static final Map<ILiquidStack, List<ILiquidStack>> REMOVED_RECIPES = new LinkedHashMap<>();
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
    public static void removeRecipe(ILiquidStack output, @Optional ILiquidStack[] input) {
        init();
        List<ILiquidStack> in = new ArrayList<>();
        if(input == null || input.length == 0) {
            in = null;
        } else {
            Collections.addAll(in, input);
        }
        
        CraftTweakerAPI.apply(new Alloy.Remove(output, in));
    }
    
    private static class Add extends BaseAction {
        
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
    
    private static class Remove extends BaseAction {
        
        private ILiquidStack output;
        private List<ILiquidStack> inputs;
        
        protected Remove(ILiquidStack output) {
            super("Alloy");
            this.output = output;
        }
        
        protected Remove(ILiquidStack output, List<ILiquidStack> inputs) {
            super("Alloy");
            this.output = output;
            this.inputs = inputs;
        }
        
        @Override
        public void apply() {
            REMOVED_RECIPES.put(output, inputs);
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
        for(Map.Entry<ILiquidStack, List<ILiquidStack>> entry : REMOVED_RECIPES.entrySet()) {
            
            if(event.getRecipe().getResult().isFluidEqual(((FluidStack) entry.getKey().getInternal()))) {
                if(entry.getValue() != null) {
                    List<ILiquidStack> in = entry.getValue();
                    List<FluidStack> rin = event.getRecipe().getFluids();
                    if(rin.size() == in.size()) {
                        boolean valid = true;
                        for(int i = 0; i < in.size(); i++) {
                            ILiquidStack stack = in.get(i);
                            FluidStack lStack = rin.get(i);
                            if(!lStack.isFluidEqual(((FluidStack) stack.getInternal()))) {
                                valid = false;
                                
                            }
                        }
                        if(valid) {
                            event.setCanceled(true);
                        }
                    }
                } else {
                    event.setCanceled(true);
                }
            }
        }
    }
}
