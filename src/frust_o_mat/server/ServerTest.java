package frust_o_mat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServerTest {

	private String host = "localhost";
	private int port = 8080;
	private HttpServer server;

	@Before
	public void startServer() throws IOException {
		InetSocketAddress listenPort = new InetSocketAddress(host, port);
		server = HttpServer.create(listenPort, 1);
		server.setExecutor(null);
		server.start();
	}

	@After
	public void stopServer() throws IOException {
		server.stop(0);
	}

	@Test
	public void testStartServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket();
		try {
			serverSocket.bind(new InetSocketAddress(host, port));
			Assert.fail();
		} catch (BindException be) {
			// ok
		} finally {
			serverSocket.close();
		}
	}

	private String callbackResult;
	private volatile boolean reacted;
	
	@Test
	public void testSetContext() throws IOException {
		
		callbackResult = "";
		reacted = false;
		
		server.createContext("/test123", new HttpHandler() {
			@Override
			public void handle(HttpExchange http) throws IOException {
				callbackResult = http.getRequestURI().toString();
				reacted = true;
			}
		});
		
		String uri = "/test123/infotext.html?a=1&b=3";
		httpGET(uri);
		
		while (! reacted) {}
		Assert.assertEquals(uri, callbackResult);
	}

	private void httpGET(String uri) throws UnknownHostException, IOException {
		Socket socket = null;
		try
		{
		    socket = new Socket(host, port);
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    
		    out.print("GET " + uri + " HTTP/1.1\r\n");
		    out.print("Host: www.example.net\r\n");
		    out.print("");
		    out.flush();
		}
		finally
		{
			socket.close();
		}
	}
}
