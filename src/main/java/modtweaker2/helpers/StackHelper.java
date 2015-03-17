package modtweaker2.helpers;

//import modtweaker2.mods.botania.Botania;
import modtweaker2.mods.botania.Botania;
import modtweaker2.utils.TweakerPlugin;
import net.minecraft.item.ItemStack;

public class StackHelper {
	// Stack is the stack that is part of a recipe, stack2 is the one input
	// trying to match
	public static boolean areEqual(ItemStack stack, ItemStack stack2) {
		if (stack == null || stack2 == null)
			return false;
		else if (TweakerPlugin.isLoaded("Botania") && Botania.isSubtile(stack) && Botania.isSubtile(stack2)) {
			return Botania.subtileMatches(stack, stack2);
		} else
			return stack.isItemEqual(stack2);
	}
}
