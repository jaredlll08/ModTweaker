package com.blamejared.compat.tconstruct.materials;

import slimeknights.tconstruct.library.materials.Material;

public class TICMaterialDefinition implements ITICMaterialDefinition {
    
    private final Material material;
    
    public TICMaterialDefinition(Material material) {
        this.material = material;
    }
    
    @Override
    public ITICMaterial asMaterial() {
        return new TICMaterial(material);
    }
    
    @Override
    public String getName() {
        return material.getLocalizedName();
    }
    
    @Override
    public String getDisplayName() {
        return material.getLocalizedName();
    }
}