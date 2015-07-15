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
    
    public static String wildcardToRegex(String expression) {
        if(expression == null) return null;
        
        StringBuilder sb = new StringBuilder();
        sb.append('^');
        
        for(int i = 0; i < expression.length(); i ++) {
            char c = expression.charAt(i);
            
            switch(c) {
                case '*':
                    sb.append(".*");
                    break;
                case '?':
                    sb.append('.');
                    break;
                    
                case '(': 
                case ')':
                case '[':
                case ']':
                case '$':
                case '^':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    sb.append('\\').append(c);
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        
        sb.append('$');
        return sb.toString();
    }
}
