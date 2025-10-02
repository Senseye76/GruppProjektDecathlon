package myStepDefinitions;

import com.example.decathlon.deca.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class EwasStepdefs {
    String discipline="";
    double result=0.00;
    int score=0;

    @Given("I have a discipline {} and a result {}")
    public void iHaveADisciplineAndAResult(String dis, double res) {
        discipline=dis;
        result=res;
    }
    @When("I calculate the score")
    public void iCalculateTheScore() {
        switch (discipline) {
            case "100m":
                Deca100M deca100M = new Deca100M();
                score = deca100M.calculateResult(result);
                break;
            case "400m":
                Deca400M deca400M = new Deca400M();
                score = deca400M.calculateResult(result);
                break;
            case "1500m":
                Deca1500M deca1500M = new Deca1500M();
                score = deca1500M.calculateResult(result);
                break;
            case "110m Hurdles":
                Deca110MHurdles deca110MHurdles = new Deca110MHurdles();
                score = deca110MHurdles.calculateResult(result);
                break;
            case "Long Jump":
                DecaLongJump decaLongJump = new DecaLongJump();
                score = decaLongJump.calculateResult(result);
                break;
            case "High Jump":
                DecaHighJump decaHighJump = new DecaHighJump();
                score = decaHighJump.calculateResult(result);
                break;
            case "Pole Vault":
                DecaPoleVault decaPoleVault = new DecaPoleVault();
                score = decaPoleVault.calculateResult(result);
                break;
            case "Discus Throw":
                DecaDiscusThrow decaDiscusThrow = new DecaDiscusThrow();
                score = decaDiscusThrow.calculateResult(result);
                break;
            case "Javelin Throw":
                DecaJavelinThrow decaJavelinThrow = new DecaJavelinThrow();
                score = decaJavelinThrow.calculateResult(result);
                break;
            case "Shot Put":
                DecaShotPut decaShotPut = new DecaShotPut();
                score = decaShotPut.calculateResult(result);
                break;
        }

    }

    @Then("the score will be {int}")
    public void theScoreWillBe(int expectedScore) {

        assertEquals(expectedScore, score);
    }
}
