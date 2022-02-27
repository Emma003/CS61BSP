package flik;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {

    @Test
    public void isSameNumber() {
        int i = 6;
        int j = 6;
        assertTrue("These should be unequal", Flik.isSameNumber(i,j));
    }

    @Test
    public void isSameNumber2() {
        int i = -6;
        int j = -6;
        assertTrue("These should be equal", Flik.isSameNumber(i,j));
    }

    @Test
    public void isSameNumber3() {
        int i = 0;
        int j = 0;
        assertTrue("These should be equal", Flik.isSameNumber(i,j));
    }

    @Test
    public void isSameNumber4() {
        int i = 128;
        int j = 128;
        assertTrue("These should be equal", Flik.isSameNumber(i,j));
    }

}
