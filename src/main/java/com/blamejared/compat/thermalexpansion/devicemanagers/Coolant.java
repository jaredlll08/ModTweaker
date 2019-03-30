package com.blamejared.compat.thermalexpansion.devicemanagers;

import cofh.thermalexpansion.util.managers.device.CoolantManager;
import com.blamejared.ModTweaker;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.thermalexpansion.Coolant")
@ModOnly("thermalexpansion")
@ZenRegister
public class Coolant {
    
    @ZenMethod
    public static void addCoolant(ILiquidStack fluid, int coolantRf, int coolantFactor) {
        if(coolantRf < 0 || coolantFactor < 1 || coolantFactor > 100) {
            CraftTweakerAPI.logWarning(String.format("Invalid parameters for Coolant.addCoolant(%d, %d)", coolantRf, coolantFactor));
            return;
        }
        ModTweaker.LATE_ADDITIONS.add(new ActionAddCoolant(fluid, coolantRf, coolantFactor));
    }
    
    @ZenMethod
    public static void removeCoolant(ILiquidStack fluid) {
        ModTweaker.LATE_REMOVALS.add(new ActionRemoveCoolant(fluid));
    }
    
    private static final class ActionAddCoolant implements IAction {
        
        private final ILiquidStack fluid;
        
        //Water: 250.000, Cryotheum: 3.000.000
        private final int coolantRf;
        
        //Water: 20, Cryotheum: 60
        private final int coolantFactor;
        
        private ActionAddCoolant(ILiquidStack fluid, int coolantRf, int coolantFactor) {
            this.fluid = fluid;
            this.coolantRf = coolantRf;
            this.coolantFactor = coolantFactor;
        }
        
        @Override
        public void apply() {
            if(!CoolantManager.addCoolant(fluid.getDefinition().getName(), coolantRf, coolantFactor))
                CraftTweakerAPI.logError(String.format("Could not add %s to the TE coolant manager, used value: coolantRF: %d, coolantFactor: %d", fluid
                        .toCommandString(), coolantRf, coolantFactor));
            
        }
        
        @Override
        public String describe() {
            return String.format("Registering %s to the ThermalExpansion coolant manager", fluid.toCommandString());
        }
    }
    
    
    private static final class ActionRemoveCoolant implements IAction {
        
        private final ILiquidStack fluid;
        
        private ActionRemoveCoolant(ILiquidStack fluid) {
            this.fluid = fluid;
        }
        
        @Override
        public void apply() {
            CoolantManager.removeCoolant(fluid.getDefinition().getName());
        }
        
        @Override
        public String describe() {
            return String.format("De-Registering %s from the ThermalExpansion coolant manager", fluid.toCommandString());
        }
    }
}
