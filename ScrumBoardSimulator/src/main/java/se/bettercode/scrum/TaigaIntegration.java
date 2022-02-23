package se.bettercode.scrum;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;

import org.json.JSONObject;

public class TaigaIntegration {

	static String authResponse;
	static String projResponse;
	
	public static String getTaigaInfo() {
		// Login and get authorization token

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.taiga.io/api/v1/auth"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"password\":\"T2021blue\", \"type\":\"normal\",\"username\":\"dbiegan\"}"))
				.build();
		client.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(res -> {
					authResponse = res;
				})
				.join();
		
		JSONObject responseJSON = new JSONObject(authResponse);
		String authToken = responseJSON.getString("auth_token");

		// Get project info
		client = HttpClient.newHttpClient();
		request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.taiga.io/api/v1/projects/by_slug?slug=gianniconsiglio-sbs-consiglio-project1"))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + authToken)
				.build();
		client.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(res -> {
					projResponse = res;
				})
				.join();
		
		return projResponse;
	}
}
