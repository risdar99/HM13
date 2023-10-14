import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.exercise3.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final int USER_ID = 1;

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();

        try {
            getOpenTasksForUser(USER_ID, gson);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void getOpenTasksForUser(int userId, Gson gson) throws URISyntaxException, IOException, InterruptedException {
        String userTasksUrl = BASE_URL + "/users/" + userId + "/todos";
        HttpRequest getUserTasksRequest = HttpRequest.newBuilder(new URI(userTasksUrl))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(getUserTasksRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            User[] users = gson.fromJson(response.body(), User[].class);

            System.out.println("Open tasks for user " + userId + ":");
            for (User user : users) {
                if (!user.isCompleted()) {
                    System.out.println("Task: " + user.getTitle());
                }
            }
        } else {
            System.err.println("Failed to retrieve tasks. Status code: " + response.statusCode());
        }
    }
}