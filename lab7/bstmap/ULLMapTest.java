package bstmap;

import static org.junit.Assert.*;

import org.junit.Test;


public class ULLMapTest {

    @Test
    public void putTest() {
        ULLMap<Integer,Integer> u = new ULLMap<>();
        u.put(1,3);
        u.put(2,3);
        assertEquals(2, u.size());
        u.put(1,4);
        assertEquals(2, u.size());
        u.put(2,3);
        assertEquals(2, u.size());
        u.put(4,4);
        assertEquals(3, u.size());
    }
}
