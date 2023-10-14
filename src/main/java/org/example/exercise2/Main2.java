import com.google.gson.*;
import org.example.exercise2.Comment;
import org.example.exercise2.Post;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main2 {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        int userId = 1;

        try {
            Post lastPost = getLatestPost(userId);

            Comment[] comments = getPostComments(lastPost.getId());

            String fileName = "user-" + userId + "-post-" + lastPost.getId() + "-comments.json";
            writeCommentsToFile(fileName, comments);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static Post getLatestPost(int userId) throws IOException, InterruptedException, URISyntaxException {
        String url = BASE_URL + "/users/" + userId + "/posts";
        HttpRequest request = HttpRequest.newBuilder(new URI(url)).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Post[] posts = gson.fromJson(response.body(), Post[].class);

        if (posts.length == 0) {
            throw new IllegalArgumentException("No posts found for user " + userId);
        }

        Post latestPost = posts[0];
        for (Post post : posts) {
            if (post.getId() > latestPost.getId()) {
                latestPost = post;
            }
        }

        return latestPost;
    }

    private static Comment[] getPostComments(int postId) throws URISyntaxException, IOException, InterruptedException {
        String postCommentsUrl = BASE_URL + "/posts/" + postId + "/comments";

        HttpRequest postCommentsRequest = HttpRequest.newBuilder(new URI(postCommentsUrl))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(postCommentsRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), Comment[].class);
        } else {
            System.err.println("Failed to retrieve comments. Status code: " + response.statusCode());
            return new Comment[0];
        }
    }

    private static void writeCommentsToFile(String fileName, Comment[] comments) throws IOException {
        Path filePath = Paths.get(fileName);
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(comments, writer);
        }
        System.out.println("Comments saved to file: " + filePath.toAbsolutePath());
    }
}