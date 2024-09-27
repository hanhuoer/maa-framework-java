package io.github.hanhuoer.maa.define.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaaBoolTest {

    @Test
    public void testEquals() {
        MaaBool maaBoolTrue = MaaBool.valueOf(true);
        MaaBool maaBoolFalse = MaaBool.valueOf(false);

        assertEquals(MaaBool.TRUE, maaBoolTrue);
        assertNotEquals(MaaBool.FALSE, maaBoolTrue);

        assertEquals(maaBoolTrue, MaaBool.TRUE);
        assertNotEquals(maaBoolTrue, MaaBool.FALSE);
        assertEquals(maaBoolTrue, Boolean.TRUE);
        assertNotEquals(maaBoolTrue, Boolean.FALSE);

        assertEquals(MaaBool.FALSE, maaBoolFalse);
        assertNotEquals(MaaBool.TRUE, maaBoolFalse);
        assertEquals(maaBoolFalse, Boolean.FALSE);
        assertNotEquals(maaBoolFalse, Boolean.TRUE);
    }

    @Test
    public void testGetValue() {
        assertTrue(MaaBool.TRUE.getValue());
        assertFalse(MaaBool.FALSE.getValue());

        assertTrue(MaaBool.valueOf(true).getValue());
        assertFalse(MaaBool.valueOf(false).getValue());
    }

}