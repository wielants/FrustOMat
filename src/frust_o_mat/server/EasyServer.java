package frust_o_mat.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import frust_o_mat.service.FrustOMat;
import frust_o_mat.service.FrustOMatPersistentImpl;

public class EasyServer {

	private static FrustOMat fom = new FrustOMatPersistentImpl();
	
	public static class MyHandler implements HttpHandler {

		private ResponseBuilder respondeBuilder = new ResponseBuilder();

		public void handle(HttpExchange t) throws IOException {
			String method = t.getRequestMethod();
			URI uri = t.getRequestURI();
			Headers headers = t.getRequestHeaders();
			String requestBody = MyUtils.fromStream(t.getRequestBody());

			System.out.println(method + " " + uri);

			String relPath = uri.getPath().substring(context().length() + 1);
			File file = new File(relPath);
			if (file.exists()) {
				String response = getFileContent(file);
				MyUtils.writeResponse(t, response);
			} else {
				String response = respondeBuilder.buildResponse(method, uri,
						headers, requestBody);
				MyUtils.writeResponse(t, response);
			}
		}

		private String getFileContent(File file) throws IOException {

			byte[] encoded = Files.readAllBytes(Paths.get(file
					.getAbsolutePath()));
			return new String(encoded, "UTF-8");
		}
	}
	
	static String dec ( String s )
	  {
	      try {
			return URLDecoder.decode( s, "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException (e);
		}
	  }

	public static class ResponseBuilder {
		public String buildResponse(String method, URI uri,
				Headers requestHeaders, String requestBody) {
			
			if (method.equals("POST"))
			{
				// FIXME: Parsen verschoenern! \n am Schluss entfernen!, etc.
				String topic = dec(requestBody.substring("topic=".length()).replaceAll("\n", ""));
				fom.addTopic(topic);
			}
			else
			{
			String query = uri.getQuery();
			if (query != null && !query.isEmpty())
			{
				int index = Integer.valueOf(query.substring(2));
				fom.voteForTopic(fom.getTopics().get(index - 1));
			}
			}
			return htmlHTML(fom.getTopics());
		}

		private String htmlHTML(List<String> topics) {
			String head = getHeadHTML("Frust-O-Mat");
			String body = getBodyHTML(getTopicsHTML(topics));
			return "<!doctype html><html lang=\"de\">" + head + body
					+ "</html>";
		}

		private String getBodyHTML(String topics) {
			return "<body><style type=\"text/css\">@import \"FrustOMat.css\";</style>"
					+ "<h1>Frust-O-Mat - Lassen Sie hier Ihren Frust raus!</h1>"
					+ topics
					+ "<div class=\"new_topic\"><h3>Alles blöd, nix passt!</h3>"
					+ "<form name=\"input\" action=\"" + actionTarget() + "\" method=\"post\">"
					+ "Mein Anliegen ist folgendes: "+ "<input type=\"text\" name=\"topic\">"
					+ "<input type=\"submit\" value=\"Submit\">"
					+ "</form>"
					+ "</div>"
					+ "</body>";
		}

		private String getHeadHTML(String title) {
			String head = "<head><meta charset=\"utf-8\">" + "<title>"
					+ StringEscapeUtils.escapeHtml4(title) + "</title></head>";
			return head;
		}

		private String getTopicsHTML(List<String> topics) {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < topics.size(); i++) {
				result.append(getTopicHTML(i + 1, topics.get(i)));
			}
			return result.toString();
		}

		private String getTopicHTML(int index, String topic) {
			String topicHTML = "<form action=\"" + actionTarget() + " method=\"get\">" + "<input type=\"hidden\" name=\"a\" value=\"" + Integer.toString(index) + "\" />" + "<div class=\"topic\" onClick=\"javascript:this.parentNode.submit();\">" + StringEscapeUtils.escapeHtml4(topic) + "</div></form>";
			return topicHTML;
		}

		private String actionTarget() {
			String actionTarget = "http://"
								+ host()
								+ ":"
								+ port()
								+ context()
								+ "/index.html\"";
			return actionTarget;
		}
	}

	/**
	 * Auswertung der Votes per: cat votes.txt | sort | uniq -c | sort -rn
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		fillTopics();
		
		InetSocketAddress listenPort = new InetSocketAddress(host(), port());
		HttpServer server = HttpServer.create(listenPort, 100);
		server.createContext(context(), new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("Server listening on " + listenPort.toString());
	}

	private static void fillTopics() {
		if (fom.getTopics().isEmpty())
		{
			fom.addTopic("Keiner will für nichts zuständig sein!");
			fom.addTopic("Der Build kriecht wieder!");
			fom.addTopic("Unser neues unternehmensweites Kommunikations-Programm ist als Jabber-Client nicht zu gebrauchen!");
			fom.addTopic("Mir kann wieder niemand weiterhelfen!");
			fom.addTopic("Das Betriebssystem spinnt wieder!");
			fom.addTopic("Ich habe keine Tools, mit denen ich gescheit arbeiten kann!");
			fom.addTopic("Einmal mit Profis arbeiten!");
			fom.addTopic("Unser Email-Programm ist echt ein Rotz!");
			fom.addTopic("Ich kann's nich leiden, wenn ich ständig vom Arbeiten abgehalten werde!");
			fom.addTopic("Ich find's toll!");
		}
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
