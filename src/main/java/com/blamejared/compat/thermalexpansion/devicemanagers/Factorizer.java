package com.blamejared.compat.thermalexpansion.devicemanagers;

import cofh.thermalexpansion.util.managers.device.FactorizerManager;
import com.blamejared.ModTweaker;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenClass("mods.thermalexpansion.Factorizer")
@ModOnly("thermalexpansion")
@ZenRegister
public class Factorizer {
    
    @ZenMethod
    public static void addRecipeSplit(IItemStack in, IItemStack out) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddFactorizer(ActionAddFactorizer.Type.Split, in, out));
    }
    
    @ZenMethod
    public static void addRecipeCombine(IItemStack in, IItemStack out) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddFactorizer(ActionAddFactorizer.Type.Combine, in, out));
    }
    
    @ZenMethod
    public static void addRecipeBoth(IItemStack in, IItemStack out) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddFactorizer(ActionAddFactorizer.Type.Both, in, out));
    }
    
    @ZenMethod
    public static void removeRecipeCombine(IItemStack in) {
        ModTweaker.LATE_REMOVALS.add(new ActionRemoveFactorizer(in, false));
    }
    
    @ZenMethod
    public static void removeRecipeSplit(IItemStack in) {
        ModTweaker.LATE_REMOVALS.add(new ActionRemoveFactorizer(in, true));
    }
    
    
    private static final class ActionAddFactorizer implements IAction {
        
        private final Type type;
        private final IItemStack input;
        private final IItemStack output;
        
        private ActionAddFactorizer(Type type, IItemStack input, IItemStack output) {
            this.type = type;
            this.input = input;
            this.output = output;
        }
        
        @Override
        public void apply() {
            final ItemStack inputStack = CraftTweakerMC.getItemStack(input);
            final ItemStack outputStack = CraftTweakerMC.getItemStack(output);
            switch(type) {
                case Split:
                    FactorizerManager.addRecipe(inputStack, outputStack, true);
                    break;
                case Combine:
                    FactorizerManager.addRecipe(inputStack, outputStack, false);
                    break;
                case Both:
                    FactorizerManager.addRecipe(inputStack, outputStack, true);
                    FactorizerManager.addRecipe(inputStack, outputStack, false);
                    break;
            }
        }
        
        @Override
        public String describe() {
            switch(type) {
                case Split:
                    return String.format("Adding Factorizer Split recipe for %s -> %s", input.toCommandString(), output.toCommandString());
                case Combine:
                    return String.format("Adding Factorizer Combine recipe for %s -> %s", input.toCommandString(), output
                            .toCommandString());
                case Both:
                    return String.format("Adding Factorizer Two-Way recipe for %s <-> %s", input.toCommandString(), output
                            .toCommandString());
            }
            return "Error, no valid Factorizer type!";
        }
        
        private enum Type {Split, Combine, Both}
    }
    
    
    private static final class ActionRemoveFactorizer implements IAction {
        
        private final IItemStack input;
        private final boolean isSplit;
        
        private ActionRemoveFactorizer(IItemStack input, boolean isSplit) {
            this.input = input;
            this.isSplit = isSplit;
        }
        
        @Override
        public void apply() {
            FactorizerManager.removeRecipe(CraftTweakerMC.getItemStack(input), isSplit);
        }
        
        @Override
        public String describe() {
            return "Removing Factorizer for " + input.toCommandString();
        }
    }
}
