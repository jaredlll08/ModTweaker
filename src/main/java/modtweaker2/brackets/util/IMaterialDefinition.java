package modtweaker2.brackets.util;

import stanhebben.zenscript.annotations.*;

/**
 * Created by Jared on 6/16/2016.
 */
@ZenClass("modtweaker.material.IMaterialDefinition")
public interface IMaterialDefinition {

    @ZenOperator(OperatorType.MUL)
    public IMaterial asMaterial();

    @ZenGetter("name")
    public String getName();

    @ZenGetter("displayName")
    public String getDisplayName();

}
