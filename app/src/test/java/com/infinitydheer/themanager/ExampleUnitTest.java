package com.infinitydheer.themanager;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void Long_ValueOf_isWhatIWant() {
        assertEquals(-1001,(long)Long.valueOf("-1001"));
        assertEquals(1001,(long)Long.valueOf("1001"));
        assertEquals(10958,(long)Long.valueOf("10958"));
        assertEquals(-10958,(long)Long.valueOf("-10958"));
    }
}