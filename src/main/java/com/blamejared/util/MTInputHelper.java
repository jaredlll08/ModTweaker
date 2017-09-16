package com.blamejared.util;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IIngredient;
import gnu.trove.set.TCharSet;
import gnu.trove.set.hash.TCharHashSet;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Collections;

public class MTInputHelper {
    public static Object[] toShapedObjects(IIngredient[][] ingredients) {
        if(ingredients == null)
            return null;
        else {
            ArrayList<Object> prep = new ArrayList<>();
            TCharSet usedCharSet = new TCharHashSet();

            prep.add("abc");
            prep.add("def");
            prep.add("ghi");
            char[][] map = new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}};
            for(int x = 0; x < ingredients.length; x++) {
                if(ingredients[x] != null) {
                    for(int y = 0; y < ingredients[x].length; y++) {
                        if(ingredients[x][y] != null && x < map.length && y < map[x].length) {
                            prep.add(map[x][y]);
                            usedCharSet.add(map[x][y]);
                            prep.add(InputHelper.toObject(ingredients[x][y]));
                        }
                    }
                }
            }

            for (int i = 0; i < 3; i++) {
                StringBuilder sb = new StringBuilder();
                if (prep.get(i) instanceof String){
                    String s = (String) prep.get(i);
                    for (int j = 0; j < 3; j++) {
                        char c = s.charAt(j);
                        if (usedCharSet.contains(c)){
                            sb.append(c);
                        }else {
                            sb.append(" ");
                        }
                    }

                    prep.set(i, sb.toString());
                }
            }


            return prep.toArray();
        }
    }

    public static <R> NonNullList<R> toNonNullList(R[] items){
        NonNullList<R> nonNullList = NonNullList.create();
        Collections.addAll(nonNullList, items);

        return nonNullList;
    }

}
