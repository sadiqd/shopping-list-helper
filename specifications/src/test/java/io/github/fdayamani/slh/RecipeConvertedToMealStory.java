package io.github.fdayamani.slh;

import org.apache.commons.io.FileUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static io.github.fdayamani.slh.LightweightTestEmbedder.aLightweightTestRunnerWithStepsFrom;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.assertThat;

public class RecipeConvertedToMealStory {

    public static final String MEALPLANNER_PATH = "src/test/resources/mealplanner/";
    private String mealName;
    private List<String> shoppingList;

    FileMealPlannerCreator mealplanner = new FileMealPlannerCreator(MEALPLANNER_PATH);
    DestinationSpy destination = new DestinationSpy();

    ShoppingListGenerator generator = new ShoppingListGenerator(mealplanner, destination);

    @Test
    @Ignore
    public void verifyShoppingListHelperStory() throws Exception {
        aLightweightTestRunnerWithStepsFrom(this)
                .withStory("stories/recipe_converted_to_meal.story")
                .run();
    }

    @Given("the ingredients for $meal are $ingredients")
    public void createRecipeFileWith(String meal, String ingredients) throws IOException {
        List<String> lines = Arrays.asList("Ingredients", ingredients);
        mealName = meal.replaceAll(" ", "_") + ".txt";
        File file = new File(MEALPLANNER_PATH + "/" +  mealName);
        write(Paths.get(file.toURI()), lines, StandardCharsets.UTF_8);
    }

    @When("the shopping list is generated")
    public void generateShoppingList() {
        shoppingList = generator.generate();
    }

    @Then("the final shopping list contains $ingredients")
    public void assertThatShoppingListContains(String ingredients) {
        assertThat(shoppingList).isEqualTo(Arrays.asList(ingredients.split((", "))));
    }

    @After
    public void clearDown() throws IOException {
        FileUtils.cleanDirectory(new File(MEALPLANNER_PATH));
    }

}