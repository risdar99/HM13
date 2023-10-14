package org.example.exercise1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    private static final String CLIENT_URL = "https://jsonplaceholder.typicode.com/users";
    private static final int userId = 1;
    private static final String username =  "Bret";

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Gson gson = new GsonBuilder().create();
        User userInfo = User.builder()
                .name("Dasha Risukhina")
                .username("RisDar")
                .email("darya.yefimchuk@gmail.com")
                .address(new Address("Malokytaivska", "73", "Kyiv", "03083", new Geo("50.3943369", "30.53")))
                .phone("0991413444")
                .website("hider.com")
                .company(new Company("Rehau", "polymer products", "engineering"))
                .build();

        post(userInfo, gson);
        get(gson);
        delete(gson);
        getInfoUserForId(userId, gson);
        getInfoUserForUsername(username, gson);
    }

    private static void post(User userInfo, Gson gson) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest postHttpRequest = HttpRequest.newBuilder(new URI(CLIENT_URL))
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(userInfo)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(postHttpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println();
        System.out.println("Push response.statusCode() = " + response.statusCode());
        System.out.println("Push response.body = " + response.body());
        System.out.println();
        System.out.println();
    }

    private static void get(Gson gson) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getHttpRequest = HttpRequest.newBuilder(new URI(CLIENT_URL))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(getHttpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Get response.statusCode() = " + response.statusCode());
        System.out.println("Get response.body = " + response.body());
        System.out.println();
        System.out.println();

    }

    private static void delete(Gson gson) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest deleteHttpRequest = HttpRequest.newBuilder(new URI("https://jsonplaceholder.typicode.com/users/2"))
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> send = httpClient.send(deleteHttpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Send response.statusCode after delete = " + send.statusCode());
        System.out.println();
        System.out.println();
    }

    private static void getInfoUserForId(int userId, Gson gson) throws URISyntaxException, IOException, InterruptedException {
        String userUrl = CLIENT_URL + "/" + userId;

        HttpRequest getUserRequest = HttpRequest.newBuilder(new URI(userUrl))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(getUserRequest, HttpResponse.BodyHandlers.ofString());

        User user = gson.fromJson(response.body(), User.class);
        System.out.println("User by id " + userId + ": " + user);
        System.out.println();
        System.out.println();
    }

    private static void getInfoUserForUsername(String username, Gson gson) throws URISyntaxException, IOException, InterruptedException {
        String userUrl = CLIENT_URL + "?username=" + username;

        HttpRequest getUserRequest = HttpRequest.newBuilder(new URI(userUrl))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient() ;
            HttpResponse<String> response = httpClient.send(getUserRequest, HttpResponse.BodyHandlers.ofString());

            User[] users = gson.fromJson(response.body(), User[].class);


            for (User user : users) {
                System.out.println("User by username " + username + ": " + user);
                System.out.println();
                System.out.println();
            }
        }
}