package modtweaker2.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

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
