package com.blamejared.compat.tconstruct;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.tconstruct.Fuel")
@ZenRegister
@ModOnly("tconstruct")
public class Fuel {
    
    
    @ZenMethod
    public static void registerFuel(ILiquidStack fluid, int duration) {
        ModTweaker.LATE_ADDITIONS.add(new Fuel.Add(InputHelper.toFluid(fluid),  duration));
    }
    
    private static class Add extends BaseUndoable {
        
        private FluidStack fuel;
        private int duration;
        
        public Add(FluidStack fuel, int temp) {
            super("Fuel");
            this.fuel = fuel;
            this.duration = temp;
        }
        
        @Override
        public void apply() {
            TinkerRegistry.registerSmelteryFuel(fuel, duration);
            
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(fuel);
        }
    }
    
}
