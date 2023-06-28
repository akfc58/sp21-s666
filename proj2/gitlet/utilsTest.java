package gitlet;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class utilsTest {

    @Test
    public void sha1Test() {
        String[] l = {"t", "a"};
        System.out.println(Utils.sha1("test"));
    }
}
