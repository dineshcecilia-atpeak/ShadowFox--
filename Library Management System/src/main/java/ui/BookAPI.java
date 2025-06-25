package ui;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookAPI {
    public static JSONObject fetchBookByISBN(String isbn) {
        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String input;
            StringBuilder content = new StringBuilder();
            while ((input = in.readLine()) != null) content.append(input);
            in.close();

            return new JSONObject(content.toString())
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .getJSONObject("volumeInfo");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
