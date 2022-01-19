import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
public class GetBrewedBeers {


    @Test
    public void getBeerBrewedBeforeSpecificDate() {
        String givenBrewedBeforeDate = "02/2011";
        RestAssured.baseURI = "https://api.punkapi.com";
        Response response = given().queryParam("brewed_before", givenBrewedBeforeDate)
                .when().get("/v2/beers")
                .then().extract().response();
        List list;
        list = response.jsonPath().getList("first_brewed");
        int brewedBeforeSpecificDate = list.size();

        int givenBrewedBeforeMonth = Integer.parseInt(givenBrewedBeforeDate.split("/")[0]);
        int givenBrewedBeforeYear = Integer.parseInt(givenBrewedBeforeDate.split("/")[1]);

        for (int i = 0; i < brewedBeforeSpecificDate; i++) {
            String actualBrewedBeforeDate = list.get(i).toString();
            int actualBrewedBeforeMonth = Integer.parseInt(actualBrewedBeforeDate.split("/")[0]);
            int actualBrewedBeforeYear = Integer.parseInt(actualBrewedBeforeDate.split("/")[1]);


            if (givenBrewedBeforeYear >= actualBrewedBeforeYear) {
                if (actualBrewedBeforeYear == givenBrewedBeforeYear) {
                    if (actualBrewedBeforeMonth > givenBrewedBeforeMonth) {
                        System.out.println("Mismatched Found");
                    }
                }
            }
            else {
                System.out.println("Mismatched Found!!!!!!!!");
            }

            }

        list = response.jsonPath().getList("$");
        int responseObjectsSize = list.size();
        System.out.println("JsonResponse Size: " + responseObjectsSize + "\n");

        String[] params = {"id", "name", "description", "abv"};
        for (String param : params) {
            list = response.jsonPath().getList(param);
            int checkParamInListSize = list.size();
            if (checkParamInListSize == responseObjectsSize) {
                System.out.println("Size of " + param + " in JSON: " + checkParamInListSize
                        + " is equal to JsonResponse Size: " + responseObjectsSize + ". Validated successfully!\n");
            } else {
                System.out.println("Size of " + param + " in JSON: " + checkParamInListSize
                        + " is not equal to JsonResponse Size: " + responseObjectsSize + ". Validation failed!\n");
            }

        }
    }
}
