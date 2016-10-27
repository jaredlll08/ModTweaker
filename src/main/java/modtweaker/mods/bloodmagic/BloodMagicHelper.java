package modtweaker.mods.bloodmagic;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import com.google.common.collect.BiMap;
import com.blamejared.mtlib.helpers.ReflectionHelper;

import java.util.List;

public class BloodMagicHelper
{
    public static List<TartaricForgeRecipe> soulForgeList = null;
    public static List<AlchemyTableRecipe> alchemyTableList = null;
    public static BiMap<List<ItemStackWrapper>, AltarRecipeRegistry.AltarRecipe> altarBiMap = null;


    static {
        try {
            soulForgeList = ReflectionHelper.getStaticObject(TartaricForgeRecipeRegistry.class, "recipeList");
            alchemyTableList = ReflectionHelper.getStaticObject(AlchemyTableRecipeRegistry.class, "recipeList");
            altarBiMap = ReflectionHelper.getStaticObject(AltarRecipeRegistry.class, "recipes");
        }
        catch (Exception e)
        {
        }
    }
    private BloodMagicHelper(){}
}
