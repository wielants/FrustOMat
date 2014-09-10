package frust_o_mat.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class EasyServer {

	public static class MyHandler implements HttpHandler {

		private ResponseBuilder respondeBuilder = new ResponseBuilder();

		public void handle(HttpExchange t) throws IOException {
			String method = t.getRequestMethod();
			URI uri = t.getRequestURI();
			Headers headers = t.getRequestHeaders();
			String requestBody = MyUtils.fromStream(t.getRequestBody());

			System.out.println(method + " " + uri);

			String relPath = uri.getPath().substring(context().length() + 1);
			File file = new File (relPath);
			if (file.exists())
			{
				String response = getFileContent (file);
				MyUtils.writeResponse(t, response);			
			}
			else
			{
				String response = respondeBuilder.buildResponse(method, uri,
					headers, requestBody);
				MyUtils.writeResponse(t, response);
			}
		}

		private String getFileContent(File file) throws IOException {
					
					  byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
					  return new String(encoded);
		}
	}

	public static class ResponseBuilder {
		public String buildResponse(String method, URI uri,
				Headers requestHeaders, String requestBody) {
			return htmlHTML(getTopics());
		}

		private String htmlHTML(List<String> topics) {
			String head = getHeadHTML("aussagekräftiger Titel der Seite");
			String body = getBodyHTML(getTopicsHTML(topics));
			return "<!doctype html><html lang=\"de\">" + head + body
					+ "</html>";
		}

		private String getBodyHTML(String topics) {
			return "<body><style type=\"text/css\">@import \"FrustOMat.css\";</style>" + topics + "</body>";
		}

		private String getHeadHTML(String title) {
			String head = "<head><meta charset=\"utf-8\">"
					+ "<title>" + StringEscapeUtils.escapeHtml4(title)
					+ "</title></head>";
			return head;
		}

		private String getTopicsHTML(List<String> topics) {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < topics.size(); i++) {
				result.append(getTopicHTML(i + 1, topics.get(i)));
			}
			return result.toString();
		}

		private List<String> getTopics() {
			List<String> result = new ArrayList<String>();
			result.add("Das gefällt mir nicht!");
			return result;
		}

		private String getTopicHTML(int index, String topic) {
			String topicHTML = 
			"<form action=\"http://"+ host() + ":" + port() + context() + "/index.html\" method=\"get\">"
		    + "<input type=\"hidden\" name=\"a\" value=\"" + Integer.toString(index) + "\" />"
	    	+ "<div class=\"topic\" onClick=\"javascript:this.parentNode.submit();\">"
	    	+ StringEscapeUtils.escapeHtml4(topic)
	    	+ "</div></form>";
			return topicHTML;
		}
	}

	public static void main(String[] args) throws IOException {
		InetSocketAddress listenPort = new InetSocketAddress(host(), port());
		HttpServer server = HttpServer.create(listenPort, 100);
		server.createContext(context(), new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("Server listening on " + listenPort.toString());
	}

	private static String context() {
		return "/FrustOMat";
	}

	private static int port() {
		return 8080;
	}

	private static String host() {
		return "localhost";
	}
}
