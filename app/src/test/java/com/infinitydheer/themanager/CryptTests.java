package com.infinitydheer.themanager;

import com.infinitydheer.themanager.domain.crypt.AES;
import com.infinitydheer.themanager.domain.utils.CommonUtils;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class CryptTests {
    @Test
    public void boxTest(){
        char[] S_BOX= AES.getsBox();
        char[] INV_S_BOX=AES.getInvSBox();

        for(int i=0;i<256;i++){
            assertEquals(i,INV_S_BOX[S_BOX[i]]);
        }
    }

    @Test
    public void emojiTest(){
        String textWithEm="\uD83D\uDE03";
        System.out.println(textWithEm+" with length: "+textWithEm.length());
        for(int i=0;i<textWithEm.length();i++) {
            System.out.println("#"+(i+1)+": "+ CommonUtils.toBinString(textWithEm.codePointAt(i)));
        }
    }

    @Test
    public void stringToByteTest(){
        String booyah="The time I had in Gurgaon was so awesome!\uD83D\uDE03\nI wish I could have it again";
        byte[] bobytes=booyah.getBytes(StandardCharsets.UTF_16);

        System.out.println(bobytes.length+" is the length of the byte output, while the length of the string input is "+booyah.length());

        String outString=new String(bobytes,StandardCharsets.UTF_16);
        System.out.println("\nThe original string as converted from the bytecode is: \n"+outString);

        if(booyah.equals(outString)) System.out.println("Match success");
        else System.out.println("Match failed");

        System.out.println("Now checking the contents of the byte array...\n{");
        for(int i=0;i<bobytes.length;i++){
            String toPrint=bobytes[i]+"";

            if(i<bobytes.length-1) toPrint+=", ";
            else toPrint+="}";

            System.out.println(toPrint);
        }
    }
}
