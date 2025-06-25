package util;

import com.google.gson.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BookAPI {
    public static String[] getBookDetailsByISBN(String isbn) {
        try {
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner sc = new Scanner(url.openStream());
            StringBuilder json = new StringBuilder();
            while (sc.hasNext()) {
                json.append(sc.nextLine());
            }
            sc.close();

            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();
            JsonArray items = jsonObject.getAsJsonArray("items");
            if (items != null && items.size() > 0) {
                JsonObject volumeInfo = items.get(0).getAsJsonObject().getAsJsonObject("volumeInfo");
                String title = volumeInfo.get("title").getAsString();
                String author = volumeInfo.getAsJsonArray("authors").get(0).getAsString();
                return new String[]{title, author};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
