package com.blamejared.compat.tconstruct.materials;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.*;

@ZenClass("modtweaker.material.IMaterialDefinition")
@ZenRegister
public interface IMaterialDefinition {

    @ZenOperator(OperatorType.MUL)
    IMaterial asMaterial();

    @ZenGetter("name")
    String getName();

    @ZenGetter("displayName")
    String getDisplayName();

}