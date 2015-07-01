package modtweaker2.helpers;

import java.util.List;
import java.util.ListIterator;

public class StringHelper {
    public static List<String> toLowerCase(List<String> stringList) {
        ListIterator<String> iterator = stringList.listIterator();
        
        while(iterator.hasNext()) {
            iterator.set(iterator.next().toLowerCase());
        }
        
        return stringList;
    }
    
    public static String join(List<String> list, String conjunction) {
        StringBuilder sb = new StringBuilder();
        
        if(conjunction == null) {
            conjunction = ", ";
        }
        
        if(list != null && !list.isEmpty()) {
            for(String string : list) {
                sb.append(string).append(conjunction);
            }
            
            sb.setLength(sb.length() - conjunction.length());
        }
        
        return sb.toString();
    }
}
