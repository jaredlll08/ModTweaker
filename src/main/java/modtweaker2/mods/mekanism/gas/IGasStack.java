package modtweaker2.mods.mekanism.gas;

import minetweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

@ZenClass("modtweaker.gas.IGasStack")
public interface IGasStack extends IIngredient {
    @ZenGetter("definition")
    public IGasDefinition getDefinition();

    @ZenGetter("name")
    public String getName();

    @ZenGetter("displayName")
    public String getDisplayName();

    @ZenGetter("amount")
    public int getAmount();

    @ZenOperator(OperatorType.MUL)
    @ZenMethod
    public IGasStack withAmount(int amount);

    public Object getInternal();
}