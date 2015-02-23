package modtweaker2.mods.auracascade.aura;

import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

@ZenClass("modtweaker.auraCascade.IAuraStack")
public interface IAuraStack {
    @ZenGetter("definition")
    public IAuraDefinition getDefinition();

    @ZenGetter("name")
    public String getName();

    @ZenGetter("displayName")
    public String getDisplayName();

    @ZenGetter("amount")
    public int getAmount();

    @ZenOperator(OperatorType.MUL)
    @ZenMethod
    public IAuraStack withAmount(int amount);

    public Object getInternal();
}