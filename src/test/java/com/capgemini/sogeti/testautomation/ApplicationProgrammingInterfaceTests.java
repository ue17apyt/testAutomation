package com.capgemini.sogeti.testautomation;

import com.jayway.jsonpath.JsonPath;
import groovy.lang.Tuple3;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
public class ApplicationProgrammingInterfaceTests {

    private static final String URL_ZIPPOPOTAM_STUTTGART = "http://api.zippopotam.us/de/bw/stuttgart";
    private static final String URL_ZIPPOPOTAM_MAIN = "http://api.zippopotam.us";
    private static final String COUNTRY_NAME = "Germany";
    private static final String STATE_NAME = "Baden-Württemberg";
    private static final String DISTRICT_NAME = "Stuttgart Degerloch";
    private static final String PREDICATE_OF_POST_CODE = "$.places[?(@.['post code']=='70597')]";
    private static final long RESPONSE_TIME_THRESHOLD_IN_MILLISECONDS = 1300L;

    private final Logger logger = getLogger(ApplicationProgrammingInterfaceTests.class);

    /**
     * Test Case 1
     */
    @Test
    public void zippopotamStuttgartTest() {

        Response response = given().when().get(URL_ZIPPOPOTAM_STUTTGART);

        response.then().assertThat()
                // Verify the response status code is OK
                .statusCode(OK.value())
                // Verify the response content type is JSON
                .contentType(JSON)
                // Verify the response time is below the threshold
                .time(lessThan(RESPONSE_TIME_THRESHOLD_IN_MILLISECONDS), MILLISECONDS)
                // Verify the country is "Germany"
                .body("country", equalTo(COUNTRY_NAME))
                // Verify the state is "Baden-Württemberg"
                .body("state", equalTo(STATE_NAME));

        this.logger.info("The request to visit {} has succeeded", URL_ZIPPOPOTAM_STUTTGART);
        this.logger.info("The content type is categorized as \"{}\"", response.getContentType());
        this.logger.info("The response has taken {} milliseconds", response.getTimeIn(MILLISECONDS));
        this.logger.info("The country is {} and the state is {}", COUNTRY_NAME, STATE_NAME);

        // Convert the response body to JSON string
        String jsonString = response.getBody().asString();

        // Search the tuples of maps containing the specified post code from the JSON string
        List<Map<String, String>> targetList = JsonPath.parse(jsonString).read(PREDICATE_OF_POST_CODE);

        // Collect all the same place names from the filtered tuples in a string list
        List<String> placeNameList = targetList.stream()
                .filter(target -> target.get("place name").equals(DISTRICT_NAME))
                .map(target -> target.get("place name"))
                .collect(toList());

        // Verify the specified place name corresponding to the specified post code exists
        assertFalse(placeNameList.isEmpty());
        this.logger.info(
                "The district \"{}\" corresponds to the post code \"{}\"",
                DISTRICT_NAME, targetList.get(0).get("post code")
        );

    }

    /**
     * Test Case 2
     */
    @Test
    public void zippopotamMultiplePlacesTest() {

        // Initialize the test data in a list of pairs
        List<Tuple3<String, String, String>> countryPostCodeTuples = new ArrayList<>();
        countryPostCodeTuples.add(new Tuple3<>("us", "90210", "Beverly Hills"));
        countryPostCodeTuples.add(new Tuple3<>("us", "12345", "Schenectady"));
        countryPostCodeTuples.add(new Tuple3<>("ca", "B2R", "Waverley"));

        RestAssured.baseURI = URL_ZIPPOPOTAM_MAIN;
        // Iterate the test data to finish the test tasks
        for (Tuple3<String, String, String> countryPostCodeTuple : countryPostCodeTuples) {

            Response response = given().when().get(
                    "/" + countryPostCodeTuple.getV1() + "/" + countryPostCodeTuple.getV2()
            );

            response.then().assertThat()
                    // Verify the response status code is OK
                    .statusCode(OK.value())
                    // Verify the response content type is JSON
                    .contentType(JSON)
                    // Verify the response time is below the threshold
                    .time(lessThan(RESPONSE_TIME_THRESHOLD_IN_MILLISECONDS), MILLISECONDS);

            this.logger.info("The request to visit {}/{}/{} has succeeded",
                    RestAssured.baseURI, countryPostCodeTuple.getV1(), countryPostCodeTuple.getV2()
            );
            this.logger.info("The content type is categorized as \"{}\"", response.getContentType());
            this.logger.info("The response has taken {} milliseconds", response.getTimeIn(MILLISECONDS));

            // Convert the response body to JSON string
            String jsonString = response.getBody().asString();

            // Search the tuples of maps containing the specified place name from the JSON string
            String predicateOfPlaceName = "$.places[?(@.['place name']=='" + countryPostCodeTuple.getV3() + "')]";
            List<Map<String, String>> targetList = JsonPath.parse(jsonString).read(predicateOfPlaceName);

            // Verify that such tuple exists
            assertFalse(targetList.isEmpty());
            this.logger.info(
                    "The district \"{}\" corresponds to the country \"{}\" and the post code \"{}\"",
                    countryPostCodeTuple.getV3(), countryPostCodeTuple.getV1(), countryPostCodeTuple.getV2())
            ;

        }

    }

}