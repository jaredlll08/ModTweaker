package modtweaker2.mods.thaumcraft.aspect;

import java.util.List;

import minetweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

@ZenClass("modtweaker.aspect.IAspectStack")
public interface IAspectStack extends IIngredient {
    
    @ZenGetter("definition")
    public abstract IAspectDefinition getDefinition();
    
    @ZenGetter("name")
    public abstract String getName();
    
    @ZenGetter("displayName")
    public abstract String getDisplayName();
    
    @ZenOperator(OperatorType.MUL)
    @ZenMethod
    public abstract IAspectStack amount(int amount);
    
    @ZenMethod
    public abstract IAspectStack withAmount(int amount);
    
    public abstract List<IAspectStack> getAspects();
}
