package web.sockets.mult.pocketServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;


public class PageHttpHandler implements HttpHandler {

    private final String htmlPage;

    public PageHttpHandler(String htmlPage) {
        this.htmlPage = htmlPage;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, htmlPage.length());
        OutputStream os = exchange.getResponseBody();
        os.write(htmlPage.getBytes());
        os.close();
    }
}
