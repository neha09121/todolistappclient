package com.example.list.client.todolistappclient;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ToDoListClient {

	public static final String BASE_URI = "http://secret-coast-2427.herokuapp.com/jaxrs/";
	public static final String PATH_GETLIST = "/todolist/";
	private static ClientConfig config = new DefaultClientConfig();
	private static Client client = Client.create(config);
	private static WebResource nameResource;

	public static void main(String[] args) throws JSONException {
		getAll();
		testAdd("title1", "body1", false);
		getOneItem();
		getNonExistingItem();
		testAdd("title 2", "body 2", true);
		testAdd("title 3", "body 3", true);
		testAdd("title 4", "body 4", true);
		testAdd("Unknown title", "Unknown body", false);
		getAll();
		getOneItem();
		delete("title 2", "body 2", true);
		getAll();
		update("title1","body1", "false", "titleN","bodyN");
		toggleToDoItemDoneStatus("titleN","bodyN", false);
		

	}

	private static void toggleToDoItemDoneStatus(String title, String body, boolean done) {
		// TODO Auto-generated method stub
		System.out.println("*********markDoneUndone***********");
		ClientResponse response = client
					.resource(BASE_URI)
					.path(PATH_GETLIST).path(title).path(body).path(""+done)
					.type(MediaType.TEXT_PLAIN)
					.put(ClientResponse.class);
		

		System.out.println("Response with json  : " + response.toString());
		System.out.println("********************");
	}

	private static void update(String oldtitle, String oldbody, String done, String newTitle,
			String newBody) throws JSONException {

		// creating todolist item
		System.out.println("*********testUpdate***********");
		Map<String, String> todoListJson = new 	LinkedHashMap<String, String>();
		todoListJson.put("title", oldtitle);
		todoListJson.put("body", oldbody);
		todoListJson.put("done", done);
		todoListJson.put("newTitle", newTitle);
		todoListJson.put("newBody", newBody);
		
		ObjectMapper mapper = new ObjectMapper();
		ClientResponse response = null;

		try {
			response = client
					.resource(BASE_URI)
					.path(PATH_GETLIST)
					.type(MediaType.APPLICATION_JSON_TYPE)
					.put(ClientResponse.class,
							mapper.writeValueAsString(todoListJson));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Response with json : " + response.toString());
		System.out.println("********************");
	}

	private static void delete(String title, String body, boolean done) {
		System.out.println("**********delete**********");
		// getting the list
		WebResource resource = client.resource(BASE_URI).path(PATH_GETLIST).path(title).path(body).path(""+done);
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
		System.out.println("Response \n" + response.toString() + "\n\n");
		System.out.println("********************");
		
	}

	public static void testAdd(String title, String body, boolean done) {
		// creating todolist item
		System.out.println("*********testAdd***********");
		ListItem todoListItem = new ListItem(title, body, done);

		ObjectMapper mapper = new ObjectMapper();
		ClientResponse response = null;

		try {
			response = client
					.resource(BASE_URI)
					.path(PATH_GETLIST)
					.type(MediaType.APPLICATION_JSON_TYPE)
					.post(ClientResponse.class,
							mapper.writeValueAsString(todoListItem));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Response with json : " + response.toString());
		System.out.println("********************");
	}

	public static void getAll() {
		System.out.println("**********getAll**********");
		// getting the list
		nameResource = client.resource(BASE_URI).path(PATH_GETLIST);
		System.out.println("Client Response \n"
				+ getClientResponse(nameResource));
		System.out.println("Response \n" + getResponse(nameResource) + "\n\n");
		System.out.println("********************");

	}

	public static void getOneItem() {
		System.out.println("**********getOneItem**********");
		// getting the particular item
		nameResource = client.resource(BASE_URI).path(PATH_GETLIST).path("1");
		System.out.println("Client Response for 1 \n"
				+ getClientResponse(nameResource));
		System.out.println("Response for 1 \n" + getResponse(nameResource)
				+ "\n\n");
		System.out.println("********************");

	}

	public static void getNonExistingItem() {
		System.out.println("**********getNonExistingItem**********");
		// getting out of bound item
		nameResource = client.resource(BASE_URI).path(PATH_GETLIST)
				.path("5387");
		System.out.println("Client Response for 5 \n"
				+ getClientResponse(nameResource));
		System.out.println("Response for 5387 \n" + getResponse(nameResource)
				+ "\n\n");
		System.out.println("********************");
	}

	/**
	 * Returns client response. e.g : GET
	 * http://localhost:8080/todolist/{id}
	 * returned a response status of 200 OK
	 * 
	 * @param service
	 * @return
	 */
	private static String getClientResponse(WebResource resource) {
		return resource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class)
				.toString();
	}

	/**
	 * Returns the response as XML e.g : <Userx><Name>Pavithra</Name></User>
	 * 
	 * @param service
	 * @return
	 */
	private static String getResponse(WebResource resource) {
		return resource.accept(MediaType.TEXT_PLAIN).get(String.class);
	}
}