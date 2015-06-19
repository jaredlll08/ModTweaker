package modtweaker2.utils;

import java.util.ArrayList;

import net.minecraftforge.common.util.ForgeDirection;

public class ArrayUtils {


	public static <E> ArrayList<E> toArrayList(E[] array) {
		ArrayList<E> list = new ArrayList<E>();
		for (E e : array) {
			list.add(e);
		}
		return list;
	}


}
