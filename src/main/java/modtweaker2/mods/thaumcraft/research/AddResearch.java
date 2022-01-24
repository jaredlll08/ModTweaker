package modtweaker2.mods.thaumcraft.research;

import net.minecraft.item.ItemStack;
import minetweaker.IUndoableAction;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class AddResearch implements IUndoableAction {
	String key;
	String tab;
	ResearchItem research;
	ResearchItem oldResearch;
	ItemStack[] itemTriggers;
	String[] entityTriggers;

	public AddResearch(ResearchItem res, ItemStack[] triggers, String[] entTriggers) {
		research = res;
		tab = research.category;
		key = research.key;
		itemTriggers = triggers;
		entityTriggers = entTriggers;
	}

	@Override
	public void apply() {
		oldResearch = ResearchCategories.getResearch(research.key);
		if (itemTriggers != null) {
			research = research.setItemTriggers(itemTriggers);
		}
		if (entityTriggers != null) {
			research = research.setEntityTriggers(entityTriggers);
		}
		research.registerResearchItem();
	}

	@Override
	public String describe() {
		return "Registering Research: " + key;
	}

	@Override
	public boolean canUndo() {
		return tab != null && key != null;
	}

	@Override
	public void undo() {
		if (oldResearch == null)
			ResearchCategories.researchCategories.get(tab).research.remove(key);
		else
			ResearchCategories.researchCategories.get(tab).research.put(key, oldResearch);
	}

	@Override
	public String describeUndo() {
		return "Removing Research: " + key;
	}

	@Override
	public String getOverrideKey() {
		return null;
	}

}