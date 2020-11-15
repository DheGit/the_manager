package com.infinitydheer.themanager.domain.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A Utility class which encapsulates all methods which are common for small processing
 */
public class CommonUtils {

    private CommonUtils(){}

    /**
     * Utility method to convert any given {@link Integer} to it's binary format in {@link String}
     * @param x The number to be converted
     * @return The equivalent binary representation of the passed integer, as String
     */
    public static String toBinString(int x){
        int counter=0;
        StringBuilder builder=new StringBuilder();

        while(x>0){
            ++counter;
            if(counter!=1&&counter%8==1) builder.append(" ");
            builder.append(x&1);
            x>>=1;
        }

        if(builder.length()>8){
            int rem=8-builder.length()%9;
            if(rem%8!=0){
                while(rem>0){
                    builder.append("0");
                    --rem;
                }
            }
        }

        return builder.reverse().toString();
    }

    /**
     * Removes all the newline('\n') characters of a {@link String}
     * @param inp The input string
     * @return The input string with all newline characters converted to a space(' ')
     */
    public static String removeNewLines(String inp){
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<inp.length();i++){
            char curChar=inp.charAt(i);
            if(curChar=='\n') builder.append(' ');
            else builder.append(curChar);
        }
        return builder.toString();
    }

    /**
     * Removes all the trailing or starting newline('\n') or space(' ') characters in the entered text
     * @param inp The text to be processed
     * @return The text after processing
     */
    public static String removeUselessExtremes(String inp){
        int i=0;
        while(i<inp.length()){
            if(!(inp.charAt(i)==' '||inp.charAt(i)=='\n')) break;
            ++i;
        }
        String oup=inp.substring(i);

        i=oup.length()-1;
        while(i>=0) {
            if (!(oup.charAt(i) == ' ' || oup.charAt(i) == '\n')) break;
            --i;
        }
        oup=oup.substring(0,i+1);

        return oup;
    }

    /**
     * Reverses any {@link List}
     * @param originalList The original list
     * @param <T> Datatype of the elements of the list
     * @return The reversed list
     */
    public static <T> List<T> reverseList(List<T> originalList){
        if(originalList==null) return null;
        List<T> reversedList=new ArrayList<>();
        int size=originalList.size();
        for(int i=size-1;i>=0;i--){
            reversedList.add(originalList.get(i));
        }
        return reversedList;
    }
}
