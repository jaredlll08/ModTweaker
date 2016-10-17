package modtweaker.mods.bloodmagic;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import modtweaker.helpers.ReflectionHelper;

import java.util.List;

public class BloodMagicHelper
{
    public static List<TartaricForgeRecipe> soulForgeList = null;


    static {
        try {
            soulForgeList = ReflectionHelper.getStaticObject(TartaricForgeRecipeRegistry.class, "recipeList");
        }
        catch (Exception e)
        {
        }
    }
    private BloodMagicHelper(){}
}
