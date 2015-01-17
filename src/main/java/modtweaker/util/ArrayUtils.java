package modtweaker.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class ArrayUtils {

	
	public static ArrayList<ItemStack> toArrayList(ItemStack[] array){
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for(ItemStack stack : array){
			stacks.add(stack);
		}
		return stacks;
	}
}
