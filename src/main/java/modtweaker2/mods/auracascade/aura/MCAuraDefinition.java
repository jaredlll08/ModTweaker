package modtweaker2.mods.auracascade.aura;

import pixlepix.auracascade.data.AuraQuantity;


public class MCAuraDefinition implements IAuraDefinition {
    private final AuraQuantity aura;

    public MCAuraDefinition(AuraQuantity aura) {
        this.aura = aura;
    }

    @Override
    public String getName() {
        return aura.getType().name;
    }

    @Override
    public String getDisplayName() {
        return aura.getType().name;
    }

	@Override
	public IAuraStack asAura(int amount) {
		return new MCAuraStack(new AuraQuantity(aura.getType(), aura.getNum()));
	}
}