package com.blamejared.compat.thaumcraft.handlers.aspects;

import crafttweaker.annotations.*;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.aspects.Aspect;

@ZenRegister
@ModOnly("thaumcraft")
@ZenClass("thaumcraft.aspect.CTAspect")
public class CTAspect {
    
    private final Aspect internal;
    
    public CTAspect(Aspect internal) {
        this.internal = internal;
    }
    
    @ZenGetter("name")
    public String getName(){
        return internal.getName();
    }
    
    @ZenSetter("chatColour")
    public void setChatColour(String chatColour) {
        internal.setChatcolor(chatColour);
    }
    
    @ZenGetter("chatColour")
    public String getChatColour() {
        return internal.getChatcolor();
    }
    
    
    public Aspect getInternal() {
        return internal;
    }
}
