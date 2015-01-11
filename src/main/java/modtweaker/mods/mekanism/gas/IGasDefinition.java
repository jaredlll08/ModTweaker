package modtweaker.mods.mekanism.gas;

import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenOperator;

@ZenClass("modtweaker.gas.IGasDefinition")
public interface IGasDefinition {
    @ZenOperator(OperatorType.MUL)
    public IGasStack asStack(int millibuckets);

    @ZenGetter("name")
    public String getName();

    @ZenGetter("displayName")
    public String getDisplayName();
}