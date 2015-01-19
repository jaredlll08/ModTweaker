package modtweaker.mods.botania.lexicon;

import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import minetweaker.IUndoableAction;

public class SetCategoryIcon implements IUndoableAction {
	
	LexiconCategory category;
	ResourceLocation oldIcon;
	ResourceLocation newIcon;

    public SetCategoryIcon(LexiconCategory category, String icon) {
        this.category=category;
        this.newIcon=new ResourceLocation(icon);
    }

    @Override
	public void apply() {
    	oldIcon=category.getIcon();
    	category.setIcon(newIcon);
	}

	@Override
	public boolean canUndo() {
        return category != null;
	}
	
	@Override
	public String describe() {
        return "Setting Lexicon Category priority: " + category.getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Unsetting Lexicon Category priority: " + category.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
    	category.setIcon(oldIcon);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}
