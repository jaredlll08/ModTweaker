package com.blamejared.compat.tconstruct;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tconstruct.recipes.DryingRecipeTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

@ZenClass("mods.tconstruct.Drying")
@ZenRegister
@ModOnly("tconstruct")
public class Drying {
    
    public static final List<IItemStack> DRYING_RECIPES = new LinkedList<>();
    private static boolean init = false;
    
    private static void init() {
        if(!init) {
            MinecraftForge.EVENT_BUS.register(new Drying());
            init = true;
        }
    }
    
    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack input, int time) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input), time));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        init();
        CraftTweakerAPI.apply(new Remove(output));
    }
    
    private static class Add extends BaseUndoable {
        
        private ItemStack output, input;
        private int time;
        
        public Add(ItemStack output, ItemStack input, int time) {
            super("Drying");
            this.output = output;
            this.input = input;
            this.time = time;
        }
        
        @Override
        public void apply() {
            TinkerRegistry.addDryingRecipe(new DryingRecipeTweaker(new RecipeMatch.Item(input, 1), output, time));
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseUndoable {
        
        private IItemStack output;
        
        protected Remove(IItemStack output) {
            super("Drying");
            this.output = output;
        }
        
        @Override
        public void apply() {
            DRYING_RECIPES.add(output);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    @SubscribeEvent
    public void onTinkerRegister(TinkerRegisterEvent.DryingRackRegisterEvent event) {
        if(event.getRecipe() instanceof DryingRecipeTweaker) {
            return;
        }
        for(IItemStack stack : DRYING_RECIPES) {
            if(stack.matches(InputHelper.toIItemStack(event.getRecipe().getResult()))) {
                event.setCanceled(true);
            }
        }
    }
}
