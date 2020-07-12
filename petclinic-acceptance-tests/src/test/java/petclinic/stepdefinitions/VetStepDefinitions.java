package petclinic.stepdefinitions;

import com.google.common.base.Splitter;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import org.assertj.core.api.SoftAssertions;
import petclinic.actions.navigation.Navigate;
import petclinic.actions.vets.Vet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.*;
import static petclinic.actions.vets.VetQuestions.specialitiesFor;
import static petclinic.actions.vets.VetQuestions.vetNamesDisplayed;

public class VetStepDefinitions {
    @Given("{actor} is a Pet Clinic owner")
    public void aPetClinicOwner(Actor actor) {
        actor.attemptsTo(
                Navigate.toTheApplicationHomePage()
        );
    }

    @When("{actor} views the vets in his clinic")
    public void viewsTheVetsInHisClinic(Actor actor) {
        actor.attemptsTo(
                Navigate.toTheVeterinarianList()
        );
    }

    @Then("{actor} should see the name of each vet in the clinic")
    public void shouldSeeTheNameAndSpecialitiesOfEachVet(Actor actor) {
        actor.should(
                seeThat("the list of vet names", vetNamesDisplayed(), is(not(empty())))
        );
    }

    @Then("{actor} should see each of the following vet specialities")
    public void shouldSeeVetSpecialities(Actor actor, List<Vet> vetSpecialities) {

        SoftAssertions softly = new SoftAssertions();
        vetSpecialities.forEach(
                vet -> {
                    List<String> displayedSpecialities = specialitiesFor(vet.getName()).answeredBy(actor);
                    softly.assertThat(displayedSpecialities).containsExactlyElementsOf(vet.getSpecialities());
                }
        );
        softly.assertAll();
    }

    @DataTableType
    public Vet vetEntry(Map<String, String> entry) {
        return new Vet(entry.get("Name"),specialitiesIn(entry.get("Specialities")));
    }

    private List<String> specialitiesIn(String specialityList) {
        if (specialityList == null) { return new ArrayList<>(); }
        return Splitter.on(",").trimResults().splitToList(specialityList);
    }

}