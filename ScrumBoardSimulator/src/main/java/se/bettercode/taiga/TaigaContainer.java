import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaigaContainer {
	
	ArrayList<String> people;
	String url;
	String baseURL = "https://api.taiga.io/api/v1/";
	String authorize_token = "application-tokens/authorize";
	
	
	public TaigaContainer()
	{
		
	}
	
	
	// Get Token
	public void getData() throws IOException
	{
		authorize();
//		URL url = new URL("https://api.taiga.io/api/v1/application-tokens/authorize");
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod("POST");
//		int responseCode = connection.getResponseCode();
		
	}
	
	/**
	 * This method is too authorize the taiga account
	 * @param userName
	 * @param password
	 */
	public void login(String userName, String password)
	{
		
	}
	
	public void authorize() throws IOException
	{
		URL url = new URL(baseURL + "auth");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);
		String jsonInputString = "{\"username\": \"default@asu.edu\" , \"password\": \"default\""
				+ ", \"type\": \"normal\"}";


		connection.setRequestMethod("POST");
		
		try(OutputStream os = connection.getOutputStream()) {
		    byte[] input = jsonInputString.getBytes("utf-8");
		    os.write(input, 0, input.length);			
		}
		
		System.out.println(connection.getResponseCode());
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
				}
		}
	}
	
	public void setApplication()
	{
		
	}
	
	
	

}
