package modtweaker2.mods.thaumcraft.recipe;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

public class MTInfusionRecipe extends InfusionRecipe {
    private final boolean fuzzyCentre;
    private final boolean[] fuzzyRecipe;

    public MTInfusionRecipe(String research, Object output, int inst, AspectList aspects2, ItemStack input, ItemStack[] recipe, boolean fuzzyCentre, boolean[] fuzzyRecipe) {
        super(research, output, inst, aspects2, input, recipe);
        this.fuzzyCentre = fuzzyCentre;
        this.fuzzyRecipe = fuzzyRecipe;
    }

    @Override
    public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
        if (getRecipeInput() == null) return false;

        if (research.length() > 0 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research)) {
            return false;
        }

        ItemStack i2 = central.copy();
        if (getRecipeInput().getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
        }

        if (!areItemStacksEqual(i2, getRecipeInput(), fuzzyCentre)) return false;

        ArrayList<ItemStack> ii = new ArrayList<ItemStack>();
        for (ItemStack is : input) {
            ii.add(is.copy());
        }

        for (int j = 0; j < getComponents().length; j++) {
            ItemStack comp = getComponents()[j];
            boolean b = false;
            for (int a = 0; a < ii.size(); a++) {
                i2 = ii.get(a).copy();
                if (comp.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
                }

                if (areItemStacksEqual(i2, comp, fuzzyRecipe[j])) {
                    ii.remove(a);
                    b = true;
                    break;
                }
            }
            if (!b) return false;
        }

        return ii.size() == 0 ? true : false;
    }
}
