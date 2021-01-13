package com.blamejared.compat.thaumcraft.handlers.expand;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspectStack;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;

@ZenExpansion("crafttweaker.item.IItemStack")
@ModOnly("thaumcraft")
@ZenRegister
public class IItemAspectExpansion {
    
    @SuppressWarnings("deprecation")
    @ZenMethod
    public static void setAspects(IItemStack stack, CTAspectStack... aspects) {
        ModTweaker.LATE_ADDITIONS.add(new BaseAction("Aspects") {
            @Override
            public void apply() {
                AspectList list = new AspectList();
                for(CTAspectStack aspect : aspects) {
                    list.add(aspect.getInternal().getInternal(), aspect.getAmount());
                }
                ThaumcraftApi.registerObjectTag(InputHelper.toStack(stack), list);
            }
            
            @Override
            public String describe() {
                return "Setting aspects on item: " + LogHelper.getStackDescription(stack) + " to: " + getAspects();
            }
            
            private String getAspects() {
                StringBuilder builder = new StringBuilder();
                for(CTAspectStack aspect : aspects) {
                    builder.append(aspect.getInternal().getInternal().getName()).append(", ");
                }
                return builder.reverse().deleteCharAt(0).deleteCharAt(0).reverse().toString();
            }
            
        });
        
    }
    
    @SuppressWarnings("deprecation")
    @ZenMethod
    public static void removeAspects(IItemStack stack, CTAspectStack... aspects) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction("Aspects") {
            
            @Override
            public void apply() {
                AspectList list = new AspectList(InputHelper.toStack(stack));
                for(CTAspectStack aspect : aspects) {
                    list.remove(aspect.getInternal().getInternal());
                }
                ThaumcraftApi.registerObjectTag(InputHelper.toStack(stack), list);
            }
    
            @Override
            public String describe() {
                return "Removing aspects on item: " + LogHelper.getStackDescription(stack) + "," + getAspects();
            }
            
            private String getAspects() {
                StringBuilder builder = new StringBuilder();
                for(CTAspectStack aspect : aspects) {
                    builder.append(aspect.getInternal().getInternal().getName()).append(", ");
                }
                return builder.reverse().deleteCharAt(0).deleteCharAt(0).reverse().toString();
            }
        });
        
    }
}
