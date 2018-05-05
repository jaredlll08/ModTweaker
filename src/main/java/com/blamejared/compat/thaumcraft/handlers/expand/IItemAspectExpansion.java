package com.blamejared.compat.thaumcraft.handlers.expand;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thaumcraft.handlers.aspects.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.*;

import java.util.*;

@ZenExpansion("crafttweaker.item.IItemStack")
@ModOnly("thaumcraft")
@ZenRegister
public class IItemAspectExpansion {
    
    private static Map<IItemStack, CTAspectStack[]> backupAspects = new HashMap<>();
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
    
    @ZenMethod
    public static CTAspectStack[] getAspects(IItemStack stack) {
        if(backupAspects.containsKey(stack)){
            return backupAspects.get(stack);
        }
        AspectList list = new AspectList(InputHelper.toStack(stack));
        CTAspectStack[] arr = new CTAspectStack[list.size()];
        int count = 0;
        for(Map.Entry<Aspect, Integer> entry : list.aspects.entrySet()) {
            arr[count++] = new CTAspectStack(new CTAspect(entry.getKey()), entry.getValue());
        }
        return arr;
    }
    
}
