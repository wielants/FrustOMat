package frust_o_mat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

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
			String requestBody = fromStream (t.getRequestBody());
			

			String response = respondeBuilder.buildResponse(method, uri,
					headers, requestBody);
			writeResponse(t, response);
		}
		public static String fromStream(InputStream in) throws IOException
		{
		    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		    StringBuilder out = new StringBuilder();
		    String newLine = System.getProperty("line.separator");
		    String line;
		    while ((line = reader.readLine()) != null) {
		        out.append(line);
		        out.append(newLine);
		    }
		    return out.toString();
		}

		private void writeResponse(HttpExchange t, String response)
				throws IOException {
			t.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public static class ResponseBuilder {
		public String buildResponse(String method, URI uri,
				Headers requestHeaders, String requestBody) {
//			if (method.equals("GET")) {
//				if (uri.getPath().("hello")) {
//					return "hello!";
//				}
//			}
			System.out.println(uri.getQuery());
			return "<!doctype html>"
+ "<html lang=\"de\">"
+ "  <head>"
+ "    <meta charset=\"utf-8\">"
+ "    <title>aussagekräftiger Titel der Seite</title>"
+ "  </head>"
+ "  <body>"
+ "<form action=\"http://localhost:8080/FrustOMat/index.html?a=1\">"
+ "<input type=\"submit\" value=\"Das gefällt mir nicht!\">"
+ "</form>"
+ "<form action=\"http://localhost:8080/FrustOMat/index.html?2\">"
+ "<input type=\"submit\" value=\"Das gefällt mir nicht!\">"
+ "</form>"
+ "<form action=\"http://localhost:8080/FrustOMat/index.html?3\">"
+ "<input type=\"submit\" value=\"Das gefällt mir nicht!\">"
+ "</form>"
+ "<form action=\"http://localhost:8080/FrustOMat/index.html?4\">"
+ "<input type=\"submit\" value=\"Das gefällt mir nicht!\">"
+ "</form>"
+ "<form action=\"http://localhost:8080/FrustOMat/index.html?5\">"
+ "<input type=\"submit\" value=\"Das gefällt mir nicht!\">"
+ "</form>"
+ "  </body>"
+ "</html>"
+ "</body>"
+ "</html>";
		}
	}

	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 100);
		server.createContext("/FrustOMat/index.html", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}
}
