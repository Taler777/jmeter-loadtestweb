import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

/*
Внизу стартовой страницы сайта есть ссылки на RSS.
Для демонстрации работы с XML нужно извлечь ссылки на статьи из RSS и открыть одну из них.
 */
public class Task1 {

    private static final String URL_WEBSITE = "https://loadtestweb.wordpress.com/feed/";

    @Test
    public void Test2() {
        RestAssured.baseURI = URL_WEBSITE;
        //check status code 200 and content-type "XML"
        given()
                .when().get(URL_WEBSITE)
                .then().assertThat()
                .statusCode(200)
                .and().contentType(ContentType.XML);

        // take URLs
        Response response = get(URL_WEBSITE);
        String xml = response.asString();
        XmlPath xmlPath = response.xmlPath();
        xmlPath.setRoot("rss.channel.item");
        List<String> urls = xmlPath.getList("link");

        // take random URL
        Random random = new Random();
        int n = random.nextInt(urls.size());
        String randomURL = urls.get(n);

        // open random URL
        given().post(randomURL).then().assertThat().statusCode(200);
    }
}
