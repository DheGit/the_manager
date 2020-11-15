package com.infinitydheer.themanager;

import com.infinitydheer.themanager.domain.utils.CommonUtils;

import static org.junit.Assert.*;
import org.junit.Test;

public class AlgoTests {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void byteTest() {
        System.out.println((byte) 255);
        System.out.println((byte) 129);
        System.out.println((byte) 192);
    }

    @Test
    public void toBinString_isCorrect() {
        assertEquals("10", CommonUtils.toBinString(2));
        assertEquals("11", CommonUtils.toBinString(3));
        assertEquals("110", CommonUtils.toBinString(6));
        assertEquals("111", CommonUtils.toBinString(7));
        assertEquals("1111111", CommonUtils.toBinString(127));
        assertEquals("11111111", CommonUtils.toBinString(255));
        assertEquals("00000001 11111111", CommonUtils.toBinString(511));
    }

    @Test
    public void removeUselessTrails_isCorrect() {
        assertEquals("this is a good text", CommonUtils.removeUselessExtremes("     \n\n\n     this is a good text     \n\n\n\n     "));
        assertEquals("", CommonUtils.removeUselessExtremes("     \n\n\n          \n\n\n\n     "));
    }
}
