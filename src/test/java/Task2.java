import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

/*
Используя метод REST API для получения информации о статьях, получить информацию о статьях с категорией «Тестовая страница»,
чтобы в результате вернулись статьи https://loadtestweb.wordpress.com/category/тестовая-страница/
Выбрать случайным образом из полученных статей и их атрибутов ссылку (URL) на одну из них.
Открыть статью по полученной ссылке.
 */
public class Task2 {
    private static final String URL_API = "https://public-api.wordpress.com/rest/v1.1/sites/";
    private static final String URL_WEBSITE = "loadtestweb.wordpress.com/posts/";
    private static final String URL_PROPERTY = "?category=";
    private static final String CATEGORY = "тестовая-страница";


    @Test
    public void Test2() {
        RestAssured.baseURI = URL_API;
        //check status code 200 and content-type "JSON"
        given()
                .param("category", CATEGORY)
                .when().get(URL_WEBSITE)
                .then().assertThat()
                .statusCode(200)
                .and().contentType(ContentType.JSON);

        // take URLs
        Response response = get(URL_API + URL_WEBSITE + URL_PROPERTY + CATEGORY);
        String json = response.asString();
        JsonPath jp = new JsonPath(json);
        jp.setRoot("posts");
        List<String> urls = jp.getList("URL");

        // take random URL
        Random random = new Random();
        int n = random.nextInt(urls.size());
        String randomURL = urls.get(n);

        // open random URL
        given().post(randomURL).then().assertThat().statusCode(200);
    }
}