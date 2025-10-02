package myStepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MyStepdefs {
    @Given("I have a discipline {string} and a result {double}")
    public void iHaveADisciplineAndAResult(String arg0, double arg1) {
        System.out.println("I have a discipline and a result");
    }

    @When("I calculate the score")
    public void iCalculateTheScore() {
        System.out.println("I calculate the score");
    }

    @Then("the score will be {int}")
    public void theScoreWillBe(int arg0) {
        System.out.println("I get a score");
    }
}
