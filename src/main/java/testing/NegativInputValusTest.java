package testing;
import com.example.decathlon.deca.*;
import com.example.decathlon.heptathlon.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class NegativInputValusTest {

    Deca100M deca100M = new Deca100M();
    Deca110MHurdles deca110MHurdles = new Deca110MHurdles();
    Deca400M deca400M = new Deca400M();
    Deca1500M deca1500M = new Deca1500M();
    DecaDiscusThrow decaDiscusThrow = new DecaDiscusThrow();
    DecaHighJump decaHighJump = new DecaHighJump();
    DecaJavelinThrow decaJavelinThrow = new DecaJavelinThrow();
    DecaLongJump decaLongJump = new DecaLongJump();
    DecaPoleVault decaPoleVault = new DecaPoleVault();
    DecaShotPut decaShotPut = new DecaShotPut();
    Hep100MHurdles hep100MHurdles = new Hep100MHurdles();
    Hep200M hep200M = new Hep200M();
    Hep800M hep800M = new Hep800M();
    HeptHightJump heptHightJump = new HeptHightJump();
    HeptJavelinThrow heptJavelinThrow = new HeptJavelinThrow();
    HeptLongJump heptLongJump = new HeptLongJump();
    HeptShotPut heptShotPut = new HeptShotPut();



    @Test
    void testDeca100mNegetivePoint() {
        int actual = deca100M.calculateResult(-12);
        assertEquals(-1, actual);
    }
    @Test
    void testDeca110mHurdlesNegetivePoint() {
        int actual = deca110MHurdles.calculateResult(0);
        assertEquals(-1,actual);
    }
    @Test
    void testDeca400mNegetivePoint() {
        int actual = deca400M.calculateResult(-9);
        assertEquals(-1, actual);
    }
    @Test
    void testDeca1500mNegetivePoint() {
        int actual = deca1500M.calculateResult(-6);
        assertEquals(-1,actual);
    }
    @Test
    void testDecaDiscusThrowNegetivePoint() {
        int actual = decaDiscusThrow.calculateResult(-8);
        assertEquals(-1, actual);
    }
    @Test
    void testDecaHighJumpNegetivePoint() {
        int actual = decaHighJump.calculateResult(-40);
        assertEquals(-1,actual);
    }
    @Test
    void testDecaJavalinThrowNegetivePoint() {
        int actual = decaJavelinThrow.calculateResult(-90);
        assertEquals(-1,actual);
    }
    @Test
    void testDecaLongJumpNegetivePoint() {
        int actual = decaLongJump.calculateResult(-249);
        assertEquals(-1,actual);
    }
    @Test
    void testDecaPoleVaultNegetivePoint() {
        int actual = decaPoleVault.calculateResult(-900);
        assertEquals(-1,actual);
    }
    @Test
    void testDecaShotPutNegetivePoint() {
        int actual = decaShotPut.calculateResult(-11);
        assertEquals(-1,actual);
    }
    @Test
    void testHep100HurdleNegetivePoint() {
        int actual = hep100MHurdles.calculateResult(-6);
        assertEquals(0,actual);
    }
    @Test
    void testHep200mNegetivePoint() {
        int actual = hep200M.calculateResult(-41);
        assertEquals(0,actual);
    }
    @Test
    void testHep800mPoint() {
        int actual = hep800M.calculateResult(-71);
        assertEquals(0,actual);
    }
    @Test
    void testHeptHightJumpNegetivePoint() {
        int actual = heptHightJump.calculateResult(-40);
        assertEquals(0,actual);
    }
    @Test
    void testHeptJavalinThrowNegetivePoint() {
        int actual = heptJavelinThrow.calculateResult(-60);
        assertEquals(0,actual);
    }
    @Test
    void testHeptLongJumpNegetivePoint() {
        int actual = heptLongJump.calculateResult(-265);
        assertEquals(0,actual);
    }
    @Test
    void testHeptShotPutNegetivePoint() {
        int actual = heptShotPut.calculateResult(-25);
        assertEquals(0,actual);

//        assertThrows(IllegalArgumentException.class, () -> {
//            calc.calculateTrack(25.4347, 18, 1.81, -12);



//        Deca100M event = new Deca100M();int actual = event.calculateResult(-12);assertEquals(0, actual);

    }


}
