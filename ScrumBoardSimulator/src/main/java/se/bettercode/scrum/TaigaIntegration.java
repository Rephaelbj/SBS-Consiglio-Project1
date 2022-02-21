package se.bettercode.scrum;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;

public class TaigaIntegration {

	public static void getTaigaInfo() {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.taiga.io/api/v1/auth"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"password\":\"T2021blue\", \"type\":\"normal\",\"username\":\"dbiegan\"}"))
				.build();
		client.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(System.out::println)
				.join();
	}
}
