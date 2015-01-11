package modtweaker.util;

import static modtweaker.helpers.StackHelper.areEqual;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class BaseCraftingRemoval extends BaseListRemoval {
    public BaseCraftingRemoval(String name, List list, ItemStack stack) {
        super(name, list, stack);
    }

    @Override
    public void apply() {
        for (IRecipe r : (List<IRecipe>) list) {
            if (r.getRecipeOutput() != null && r.getRecipeOutput() instanceof ItemStack && areEqual(r.getRecipeOutput(), stack)) {
                recipe = r;
                break;
            }
        }

        list.remove(recipe);
    }

    @Override
    public String getRecipeInfo() {
        return stack.getDisplayName();
    }
}
