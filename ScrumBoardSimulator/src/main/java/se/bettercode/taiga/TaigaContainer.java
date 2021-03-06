package se.bettercode.taiga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class TaigaContainer {
	
	String url;
	String baseURL = "https://api.taiga.io/api/v1/";
	String authorize_token = "";
	String projectId = "";

	// Json Objects:
	JSONObject currentProject = new JSONObject();
	JSONArray epics = new JSONArray();
	JSONArray stories = new JSONArray();
	JSONArray tasks = new JSONArray();
	JSONArray people = new JSONArray();
	
	
	public TaigaContainer()
	{
		JSONArray ar = new JSONArray();
	}
	
	
	// Get Token
	public void getData() throws IOException
	{
		getEpics();
		getStories();
		getTask();
		getTeamMembers();
	}
	
	/**
	 * This method is to log into a taiga account and return a token
	 * @param userName
	 * @param password
	 */
	public void login(String userName, String password) throws IOException
	{
		URL url = new URL(baseURL + "auth");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);
		String jsonInputString = "{\"username\": \"" + userName + "\" , \"password\": \"" + password + "\""
				+ ", \"type\": \"normal\"}";


		connection.setRequestMethod("POST");

		try(OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		if(connection.getResponseCode() == 200)
		{
			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				JSONObject loginObject = new JSONObject(response.toString());
				authorize_token = loginObject.getString("auth_token");



			}
		}
	}

	/**
	 * This method au
	 * @throws IOException
	 */
	public void authorize() throws IOException
	{

	}

	/**
	 * This method is to set the project for the user
	 */
	public void setProject(String project) throws IOException {
		URL url = new URL(baseURL + "projects/by_slug?slug=" + project);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Authorization", "Bearer " + authorize_token);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		if(connection.getResponseCode() == 200)
		{
			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				currentProject = new JSONObject(response.toString());
				projectId = String.valueOf(currentProject.getInt("id"));
			}
		}
	}

	private void getEpics() throws IOException {
		System.out.println("EPICS");
		URL url = new URL(baseURL + "epics?project=" + projectId);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Authorization", "Bearer " + authorize_token);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		if(connection.getResponseCode() == 200)
		{
			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
				epics = new JSONArray(response.toString());
			}
		}
	}

	private void getStories() throws IOException {
		System.out.println("Stories");

		URL url = new URL(baseURL + "userstories?project=" + projectId);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Authorization", "Bearer " + authorize_token);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		if(connection.getResponseCode() == 200)
		{
			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
				stories = new JSONArray(response.toString());
			}
		}
	}

	private void getTask() throws IOException {
		System.out.println("Tasks");

		URL url = new URL(baseURL + "tasks?project=" + projectId);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Authorization", "Bearer " + authorize_token);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		if(connection.getResponseCode() == 200)
		{
			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
				tasks = new JSONArray(response.toString());
			}
		}
	}

	private void getTeamMembers() throws IOException {
		System.out.println("Team members");

		URL url = new URL(baseURL + "users?project=" + projectId);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Authorization", "Bearer " + authorize_token);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		if(connection.getResponseCode() == 200)
		{
			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
				people = new JSONArray(response.toString());
			}
		}
	}
	
	

}
