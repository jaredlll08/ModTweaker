package modtweaker2.mods.auracascade.aura;

import pixlepix.auracascade.data.AuraQuantity;


public class MCAuraStack implements IAuraStack {
    private final AuraQuantity aura;

    public MCAuraStack(AuraQuantity aura) {
        this.aura = aura;
    }

    @Override
    public IAuraDefinition getDefinition() {
        return new MCAuraDefinition(aura);
    }

    @Override
    public String getName() {
        return aura.getType().name();
    }

    @Override
    public String getDisplayName() {
        return aura.getType().name();
    }

    @Override
    public int getAmount() {
        return aura.getNum();
    }

    @Override
    public IAuraStack withAmount(int amount) {
        AuraQuantity result = new AuraQuantity(aura.getType(), amount);
        return new MCAuraStack(result);
    }

    @Override
    public Object getInternal() {
        return aura;
    }
}