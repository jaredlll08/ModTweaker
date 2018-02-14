package com.blamejared.compat.extrautils2;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.rwtema.extrautils2.api.machine.IMachineRecipe;
import com.rwtema.extrautils2.api.machine.MachineSlotFluid;
import com.rwtema.extrautils2.api.machine.MachineSlotItem;
import com.rwtema.extrautils2.api.machine.XUMachineCrusher;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ZenClass("mods.extrautils2.Crusher")
@ModOnly("extrautils2")
@ZenRegister
public class Crusher {
    
    @ZenMethod
    public static void add(IItemStack output, IItemStack input, @Optional IItemStack secondaryOutput, @Optional float secondaryChance) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), InputHelper.toStack(secondaryOutput), secondaryChance));
    }
    
    @ZenMethod
    public static void remove(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(input));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack output, input, secondaryOutput;
        private float secondaryChance;
        
        public Add(ItemStack output, ItemStack input, ItemStack secondaryOutput, float secondaryChance) {
            super("Crusher");
            this.output = output;
            this.input = input;
            this.secondaryOutput = secondaryOutput;
            this.secondaryChance = secondaryChance;
            if(!secondaryOutput.isEmpty() && secondaryChance <= 0) {
                this.secondaryChance = 100;
            }
        }
        
        @Override
        public void apply() {
            XUMachineCrusher.addRecipe(input, output, secondaryOutput, secondaryChance);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private IItemStack input;
        
        public Remove(IItemStack input) {
            super("Crusher");
            this.input = input;
        }
        
        @Override
        public void apply() {
            List<IMachineRecipe> toRemove = new ArrayList<>();
            
            for(IMachineRecipe recipe : XUMachineCrusher.INSTANCE.recipes_registry) {
                for(Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>> mapMapPair : recipe.getJEIInputItemExamples()) {
                    for(ItemStack stack : mapMapPair.getKey().get(XUMachineCrusher.INPUT)) {
                        if(StackHelper.matches(input, InputHelper.toIItemStack(stack))) {
                            toRemove.add(recipe);
                        }
                    }
                }
            }
            
            for(IMachineRecipe iMachineRecipe : toRemove) {
                XUMachineCrusher.INSTANCE.recipes_registry.removeRecipe(iMachineRecipe);
            }
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
    
}
