package com.blamejared.compat.tconstruct;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tconstruct.recipes.*;
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
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.annotations.Optional;

import java.util.*;

@ZenClass("mods.tconstruct.Casting")
@ZenRegister
@ModOnly("tconstruct")
public class Casting {
    
    public static final List<IItemStack> REMOVED_RECIPES_TABLE = new LinkedList<>();
    public static final List<IItemStack> REMOVED_RECIPES_BASIN = new LinkedList<>();
    
    private static boolean init = false;
    
    private static void init() {
        if(!init) {
            MinecraftForge.EVENT_BUS.register(new Casting());
            init = true;
        }
    }
    
    @ZenMethod
    public static void addTableRecipe(IItemStack output, IItemStack cast, ILiquidStack fluid, int amount, @Optional boolean consumeCast) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(cast), InputHelper.toFluid(fluid), amount, true, consumeCast));
    }
    
    @ZenMethod
    public static void addBasinRecipe(IItemStack output, IItemStack cast, ILiquidStack fluid, int amount, @Optional boolean consumeCast) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(cast), InputHelper.toFluid(fluid), amount, false, consumeCast));
    }
    
    @ZenMethod
    public static void removeTableRecipe(IItemStack output) {
        init();
        CraftTweakerAPI.apply(new Remove(output, true));
    }
    
    @ZenMethod
    public static void removeBasinRecipe(IItemStack output) {
        init();
        CraftTweakerAPI.apply(new Remove(output, false));
    }
    
    private static class Remove extends BaseUndoable {
        
        private IItemStack output;
        private boolean table;
        
        protected Remove(IItemStack output, boolean table) {
            super("Casting");
            this.output = output;
            this.table = table;
        }
        
        @Override
        public void apply() {
            if(table)
                REMOVED_RECIPES_TABLE.add(output);
            else
                REMOVED_RECIPES_BASIN.add(output);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    @SubscribeEvent
    public void onTinkerRegister(TinkerRegisterEvent.TableCastingRegisterEvent event) {
        if(event.getRecipe() instanceof CastingRecipeTweaker || !(event.getRecipe() instanceof CastingRecipe)) {
            return;
        }
        for(IItemStack stack : REMOVED_RECIPES_TABLE) {
            if(stack == null || ((CastingRecipe) event.getRecipe()).getResult() == null) {
                continue;
            }
            if(stack.matches(InputHelper.toIItemStack(((CastingRecipe) event.getRecipe()).getResult()))) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onTinkerRegister(TinkerRegisterEvent.BasinCastingRegisterEvent event) {
        if(event.getRecipe() instanceof CastingRecipeTweaker || !(event.getRecipe() instanceof CastingRecipe)) {
            return;
        }
        for(IItemStack stack : REMOVED_RECIPES_BASIN) {
            if(stack == null || ((CastingRecipe) event.getRecipe()).getResult() == null) {
                continue;
            }
            if(stack.matches(InputHelper.toIItemStack(((CastingRecipe) event.getRecipe()).getResult()))) {
                event.setCanceled(true);
            }
        }
    }
    
    private static class Add extends BaseUndoable {
        
        private ItemStack output, cast;
        private FluidStack fluid;
        private int amount;
        private boolean table;
        private boolean consumeCast;
        
        public Add(ItemStack output, ItemStack cast, FluidStack fluid, int amount, boolean table, boolean consumeCast) {
            super("Casting");
            this.output = output;
            this.cast = cast;
            this.fluid = fluid;
            this.amount = amount;
            this.table = table;
            this.consumeCast = consumeCast;
        }
        
        @Override
        public void apply() {
            RecipeMatch rm = null;
            if(cast != ItemStack.EMPTY) {
                rm = RecipeMatch.ofNBT(cast);
            }
            if(table)
                TinkerRegistry.registerTableCasting(new CastingRecipeTweaker(output, rm, fluid.getFluid(), amount, consumeCast, false));
            else
                TinkerRegistry.registerBasinCasting(new CastingRecipeTweaker(output, rm, fluid.getFluid(), amount, consumeCast, false));
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
}
