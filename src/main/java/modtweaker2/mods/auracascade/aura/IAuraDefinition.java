package modtweaker2.mods.auracascade.aura;

import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenOperator;

@ZenClass("modtweaker.auraCascade.IAuraDefinition")
public interface IAuraDefinition {
	
    @ZenOperator(OperatorType.MUL)
    public IAuraStack asAura(int amount);

    @ZenGetter("name")
    public String getName();

    @ZenGetter("displayName")
    public String getDisplayName();
}