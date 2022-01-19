
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import static io.restassured.RestAssured.given;

    public class GetAllBeers {

        @Test(groups = "smoke")
        public void getAllBeers() {
            Response response = given()
                    .get("https://api.punkapi.com/v2/beers");
            System.out.println("Status Code is " + response.getStatusCode());
            Assert.assertEquals(response.getStatusCode(), 200);
        }

        @Test(groups = "smoke")
        public void getResponseBodyParameters() {
            Response response = given()
                    .get("https://api.punkapi.com/v2/beers");
            String responseBody = response.getBody().asString();
            System.out.println("Response Body is " + responseBody);

            List<Object> list;
            list = response.jsonPath().getList("$");
            System.out.println(response.jsonPath().getList("$"));
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

//        System.out.println("Response Body ID is "+ response.jsonPath().get("id"));
//        Assert.assertEquals(responseBody.contains("id"),true);
//        System.out.println("Response Body name is "+ response.jsonPath().get("name"));
//        Assert.assertEquals(responseBody.contains("name"),true);
//        System.out.println("Response Body description is "+ response.jsonPath().get("description"));
//        Assert.assertEquals(responseBody.contains("description"),true);
//        System.out.println("Response Body abv is "+ response.jsonPath().get("abv"));
//        Assert.assertEquals(responseBody.contains("abv"),true);


            }
        }

        @Test(groups = "regression")
        public void getAllBearWithConditional_abv(){
            int conditional_abv = 6;
            Response response = given().queryParam("abv_gt", conditional_abv)
                    .contentType("application/json")
                    .get("https://api.punkapi.com/v2/beers")
                    .then().assertThat().statusCode(200).extract().response();
            List<Float> list = response.jsonPath().getList("abv");
            System.out.println(list.size());
            System.out.println(list);
            for (int i=0;i<list.size();i++){
                float actual_abv =  Float.parseFloat(String.valueOf(list.get(i)));
                System.out.println(actual_abv);
                Assert.assertTrue(actual_abv>conditional_abv);
            }
        }

        @Test(groups = "regression")
        public void getAllBearWithPagination(){
            int givenpage = 2;
            int givenperpage = 5;
            Response response = given().queryParam("page", givenpage)
                    .queryParam("per_page",givenperpage)
                    .contentType("application/json")
                    .get("https://api.punkapi.com/v2/beers")
                    .then().assertThat().statusCode(200).extract().response();
            List<Integer> list = response.jsonPath().getList("id");
            System.out.println(list.size());
            System.out.println(list);
            Assert.assertEquals(list.size(),givenperpage);
        }
    }
