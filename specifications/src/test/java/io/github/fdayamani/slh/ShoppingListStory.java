package io.github.fdayamani.slh;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Test;

import java.util.*;

import static io.github.fdayamani.slh.LightweightTestEmbedder.aLightweightTestRunnerWithStepsFrom;
import static io.github.fdayamani.slh.Meal.Builder.aMeal;
import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingListStory {

    private MealPlanner mealPlanner = new MealPlanner();
    private ShoppingList shoppingList = new ShoppingList();
    private Set<String> groceryList = new HashSet<>();

    @Test
    public void verifyShoppingListStory() throws Exception {
        aLightweightTestRunnerWithStepsFrom(this)
                .withStory("stories/shopping_list.story")
                .run();
    }

    @Given("that I want to make $meal")
    public void mealPlannerContains(String meal) {
        Set<String> chickenSteakIngredients = new HashSet<>(Arrays.asList(
                "chicken steaks",
                "marinade"
                ));
        mealPlanner.add(
                aMeal()
                    .withIngredients(chickenSteakIngredients)
                    .withInstructions("marinate overnight then cook on griddle pan. Use extra marinade to baste")
                    .build());
    }

    @When("I invoke the shopping list helper")
    public void invokeShoppingListHelper() {
        groceryList = shoppingList.retrieveShoppingListFor(mealPlanner);
    }

    @Then("the final list is $ingredients")
    public void assertListIsExpectedListOfIngredients(String ingredients) {
        Set<String> expectedShoppingList = new HashSet<>();
        for (String ingredient : ingredients.split(", ")) {
            expectedShoppingList.add(ingredient);
        }

        assertThat(groceryList).isEqualTo(expectedShoppingList);
    }
}
