package allolikatest;
import com.example.decathlon.deca.Deca100M;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class NegativInputValusTest {

    Deca100M event = new Deca100M();



    @Test
    void testDeca100mNegetivePoint() {
        int actual = event.calculateResult(-12);
        assertEquals(-1, actual);

    }

    @Test
    void testDeca110mHurdlesNegetivePoint() {
        int actual = event.calculateResult(0);
        assertEquals(-1,actual);
    }
    @Test
    void testDeca400mNegetivePoint() {
        int actual = event.calculateResult(-9);
        assertEquals(-1,actual);
//        assertThrows(IllegalArgumentException.class, () -> {
//            calc.calculateTrack(25.4347, 18, 1.81, -12);



//        Deca100M event = new Deca100M();int actual = event.calculateResult(-12);assertEquals(0, actual);

    }


}
