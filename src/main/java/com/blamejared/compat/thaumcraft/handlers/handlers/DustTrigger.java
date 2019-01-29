package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.common.lib.crafting.DustTriggerOre;
import thaumcraft.common.lib.crafting.DustTriggerSimple;

import java.lang.reflect.Field;
import java.util.Iterator;

@ZenClass("mods.thaumcraft.SalisMundus")
@ZenRegister
@ModOnly("thaumcraft")
public class DustTrigger {
    
    private static final Field simpleTriggerResult;
    private static final Field oredictTriggerResult;
    
    static {
        
        Field A = null;
        Field B = null;
        try {
            A = DustTriggerSimple.class.getDeclaredField("result");
            A.setAccessible(true);
            
            B = DustTriggerOre.class.getDeclaredField("result");
            B.setAccessible(true);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
        
        simpleTriggerResult = A;
        oredictTriggerResult = B;
    }
    
    private DustTrigger() {
    }
    
    @ZenMethod
    public static void addSingleConversion(IBlock in, IItemStack out, @Optional String research) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddTrigger(in, out, research));
    }
    
    @ZenMethod
    public static void addSingleConversion(IOreDictEntry in, IItemStack out, @Optional String research) {
        ModTweaker.LATE_ADDITIONS.add(new ActionAddTrigger(in, out, research));
    }
    
    @ZenMethod
    public static void removeSingleConversion(IIngredient output) {
        ModTweaker.LATE_REMOVALS.add(new ActionRemoveTrigger(output));
    }
    
    static final class ActionAddTrigger implements IAction {
    
        private final IItemStack output;
        private final IDustTrigger trigger;
    
        ActionAddTrigger(IBlock in, IItemStack output, String research) {
            this.output = output;
            this.trigger = new DustTriggerSimple(research, CraftTweakerMC.getBlock(in), CraftTweakerMC.getItemStack(output));
        }
    
        ActionAddTrigger(IOreDictEntry in, IItemStack output, String research) {
            this.output = output;
            this.trigger = new DustTriggerOre(research, in.getName(), CraftTweakerMC.getItemStack(output));
        }
    
        @Override
        public void apply() {
            IDustTrigger.registerDustTrigger(this.trigger);
        }
    
        @Override
        public String describe() {
            return "Adding Salis mundus Conversion with output " + output.toCommandString();
        }
    }
    
    static final class ActionRemoveTrigger implements IAction {
        
        private final IIngredient output;
        
        ActionRemoveTrigger(IIngredient output) {
            this.output = output;
        }
        
        @Override
        public void apply() {
            final Iterator<IDustTrigger> iterator = IDustTrigger.triggers.iterator();
            while(iterator.hasNext()) {
                final IDustTrigger trigger = iterator.next();
                try {
                    final IItemStack item;
                    if(trigger instanceof DustTriggerSimple && simpleTriggerResult != null)
                        item = CraftTweakerMC.getIItemStack((ItemStack) simpleTriggerResult.get(trigger));
                    else if(trigger instanceof DustTriggerOre && oredictTriggerResult != null)
                        item = CraftTweakerMC.getIItemStack((ItemStack) oredictTriggerResult.get(trigger));
                    else
                        continue;
                    
                    if(output.contains(item))
                        iterator.remove();
                } catch(IllegalAccessException e) {
                    CraftTweakerAPI.logError("Error while applying remove Salis mundus action: ", e);
                }
            }
        }
        
        @Override
        public String describe() {
            return "Removing all Single Salis mundus actions that return " + output.toCommandString();
        }
    }
    
    
}
