package com.blamejared.compat.thaumcraft.handlers.expand;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspectStack;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.entity.IEntityDefinition;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.internal.CommonInternals;

@ZenExpansion("crafttweaker.entity.IEntityDefinition")
@ModOnly("thaumcraft")
@ZenRegister
public class IEntityAspectExpansion {
    
    @ZenMethod
    public static void setAspects(IEntityDefinition definition, CTAspectStack... aspects) {
        ModTweaker.LATE_ADDITIONS.add(new BaseAction("Aspects") {
            @Override
            public void apply() {
                AspectList list = new AspectList();
                for(CTAspectStack aspect : aspects) {
                    list.add(aspect.getInternal().getInternal(), aspect.getAmount());
                }
                ThaumcraftApi.registerEntityTag(definition.getName(), list);
            }
            
            @Override
            public String describe() {
                return "Setting aspects on entity: " + definition.getId() + " to: " + getAspects();
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
    public static void removeAspects(IEntityDefinition definition, CTAspectStack... aspects) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction("Aspects") {
            
            @Override
            public void apply() {
                ThaumcraftApi.EntityTags tags = getTags();
                AspectList list = tags.aspects;
                for(CTAspectStack aspect : aspects) {
                    list.remove(aspect.getInternal().getInternal());
                }
                ThaumcraftApi.registerEntityTag(definition.getName(), list);
            }
            
            @Override
            public String describe() {
                return "Removing aspects on entity: " + definition.getId() + "," + getAspects();
            }
            
            private String getAspects() {
                StringBuilder builder = new StringBuilder();
                for(CTAspectStack aspect : aspects) {
                    builder.append(aspect.getInternal().getInternal().getName()).append(", ");
                }
                return builder.reverse().deleteCharAt(0).deleteCharAt(0).reverse().toString();
            }
            
            private ThaumcraftApi.EntityTags getTags() {
                for(ThaumcraftApi.EntityTags tags : CommonInternals.scanEntities) {
                    if(tags.entityName.equalsIgnoreCase(definition.getName())) {
                        return tags;
                    }
                }
                return null;
            }
        });
        
    }
    
}
