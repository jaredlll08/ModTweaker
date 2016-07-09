package modtweaker2.brackets.util;

import slimeknights.tconstruct.library.materials.Material;

/**
 * Created by Jared on 6/16/2016.
 */

public class MCMaterialDefinition implements IMaterialDefinition {
    private final Material material;

    public MCMaterialDefinition(Material material) {
        this.material = material;
    }

    @Override
    public IMaterial asMaterial() {
        return new MCMaterial(material);
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
