package com.blamejared.compat.thaumcraft.handlers.aspects;

import crafttweaker.annotations.*;
import stanhebben.zenscript.annotations.*;

@ZenRegister
@ModOnly("thaumcraft")
@ZenClass("thaumcraft.aspect.CTAspectStack")
public class CTAspectStack {
    
    private final CTAspect internal;
    private final int amount;
    
    public CTAspectStack(CTAspect internal, int amount) {
        this.internal = internal;
        this.amount = amount;
    }
    
    @ZenOperator(OperatorType.MUL)
    @ZenMethod
    public CTAspectStack setAmount(int amount) {
        return new CTAspectStack(internal, amount);
    }
    
    @ZenGetter("internal")
    public CTAspect getInternal() {
        return internal;
    }
    
    @ZenGetter("amount")
    public int getAmount() {
        return amount;
    }
    
}
