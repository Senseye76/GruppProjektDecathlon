package testing;
import com.example.decathlon.common.Competitor;
import com.example.decathlon.common.InputResult;
import com.example.decathlon.common.SelectDiscipline;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTestClass {

    @Test
    public void testDTInputFieldName(){

        String expected = "Maja";
        Competitor comp = new Competitor(expected);
        String actual = comp.getName();
        String test = "";

        assertEquals(expected, actual);
    }
    @Test
    public void testDTDiscipline(){

        String expected="";
        String[] disciples = new String[10];
        disciples[0]="100m";
        disciples[1]="110m Hurdles";
        disciples[2]="400m";
        disciples[3]="1500m";
        disciples[4]="Discus Throw";
        disciples[5]="High Jump";
        disciples[6]="Javelin Throw";
        disciples[7]="Long Jump";
        disciples[8]="Pole Vault";
        disciples[9]="Shot Put";
        String actual ="";

        for (int i=0;i<10;i++) {
            expected = disciples[i];
            SelectDiscipline discipline = new SelectDiscipline(expected);
            actual= discipline.getCurDiscipline();
            assertEquals(expected, actual);
        }
    }
    @Test
    public void testDTScore(){

        double expected=11;
        InputResult result = new InputResult(expected);
        double actual =result.getInputResult();
        assertEquals(expected, actual);
    }

}
