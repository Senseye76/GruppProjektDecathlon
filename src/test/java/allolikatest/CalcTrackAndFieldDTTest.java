package allolikatest;
import com.example.decathlon.common.CalcTrackAndField;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CalcTrackAndFieldDTTest {

    CalcTrackAndField calc = new CalcTrackAndField();

    @Test
    void testDeca100mValidPoint() {
        int points = calc.calculateTrack(25.4347, 18, 1.81, -12);
        assertEquals(651, points, "100m på 12sek ska ge resultatet 651 poäng");

    }

    @Test
    void testDeca1500mValidPoint() {
        int points = calc.calculateTrack(0.03768, 480, 1.85, 3);
        assertEquals(3399, points, "1500m på 3sek ska ge resultatet 3399 poäng");
    }

    @Test
    void testDeca400mValidPoint() {
        int points = calc.calculateTrack(1.53775, 82, 1.81, 35);
        assertEquals(1634, points);
    }

    @Test
    void testDeca110HurdlesValidPoint() {
        int points = calc.calculateTrack(5.74352, 28.5, 1.92, 15);
        assertEquals(850, points);
    }
    //    @Test
    //    void testHep100HurdlesValidPoint() {
    //        int points = calc.calculateTrack(9.23076,26.7,1.835 ,10 );
    //        assertEquals(1617, points);
    //    }
    //    @Test
    //    void testHep200mValidPoint() {
    //        int points = calc.calculateTrack(4.99087,42.5,1.81 ,20 );
    //        assertEquals(1398, points);
    //}
    //    @Test.
    //    void testHep800mValidPoint() {
    //        int points = calc.calculateTrack(0.11193,254,1.88 ,103 );
    //        assertEquals(1397, points);
    //    }

    @Test
    void testDecaDiscusThrowValidPoint() {
        int points = calc.calculateField(12.91, 4, 1.1, 45);
        assertEquals(767, points);
    }

    @Test
    void testDecaHighJumpValidPoint() {
        int points = calc.calculateField(0.8465, 75, 1.42, 99);
        assertEquals(77, points);
    }

    @Test
    void testDecaJavalinThrowValidPoint() {
        int points = calc.calculateField(10.14, 7, 1.08, 105);
        assertEquals(1434, points);

    }

    @Test
    void testDecaLongJumpValidPoint() {
        int points = calc.calculateField(0.14354, 100, 1.4, 895);
        assertEquals(1312, points);
    }

    @Test
    void testDecaPoleVaultValidPoint() {
        int points = calc.calculateField(0.2797, 100, 1.35, 560);
        assertEquals(1100, points);
    }

    @Test
    void testDecaShotPutValidPoint() {
        int points = calc.calculateField(51.39, 1.5, 1.05, 21);
        assertEquals(1162, points);


//    Deca100M event = new Deca100M();int actual = event.calculateResult(-12);assertEquals(0, actual);
    }
}