package allolikatest;
import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.deca.Deca100M;
import com.example.decathlon.deca.Deca110MHurdles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class NegativInputValusTest {

    Deca100M event = new Deca100M();



    @Test
    void testDeca100mValidPoint() {
        int actual = event.calculateResult(-12);
        assertEquals(0, actual);

    }

    @Test
    void testDeca110mHurdlesValidPoint() {
        int actual = event.calculateResult(0);
        assertEquals(0,actual);

//        assertThrows(IllegalArgumentException.class, () -> {
//            calc.calculateTrack(25.4347, 18, 1.81, -12);



//        Deca100M event = new Deca100M();int actual = event.calculateResult(-12);assertEquals(0, actual);

    }


}
