package com.blamejared.compat.botania.lexicon;

import vazkii.botania.api.lexicon.*;

public class UsefulEntry extends LexiconEntry {
    
    public UsefulEntry(String unlocalizedName, LexiconCategory category) {
        super(unlocalizedName, category);
    }
    
    @Override
    public String getTagline() {
        return "botania.tagline." + super.getUnlocalizedName();
    }
}
