package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.lexicon.LexiconCategory;

public class SetCategoryIcon implements IAction {
	
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
	public String describe() {
        return "Setting Lexicon Category icon: " + category.getUnlocalizedName();
	}

}
