package web.sockets.mult.pocketServer;

import com.sun.net.httpserver.*;
import com.sun.net.httpserver.spi.HttpServerProvider;
import sun.net.httpserver.DefaultHttpServerProvider;
import com.sun.net.httpserver.HttpContext;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 *  Pocket multithreading socket server.
 *  "localhost/" - return general page with simple two links.
 *  "localhost/first.do" - with link to "localhost/second.do" and
 *  "localhost/second.do" with link to "localhost/first.do".
 */

public class Server {

    public static void main(String[] args) throws IOException {

        final int backlog = 64;
        final InetSocketAddress serverAddress = new InetSocketAddress(8080);

        HttpServerProvider provider = DefaultHttpServerProvider.provider();
        HttpServer server = provider.createHttpServer(serverAddress, backlog);
        server.setExecutor(Executors.newCachedThreadPool());

        HttpContext baseContext = server.createContext("/");
        baseContext.setHandler(new PageHttpHandler("" +
                "<html>" +
                "   <body>" +
                "       <h3>Hello! Which page you need?</h3>" +
                "       <p><a href=\"/first.do\">first</a></p>" +
                "       <p><a href=\"/second.do\">second</a></p>" +
                "   </body>" +
                "</html>"
        ));

        HttpContext aContext = server.createContext("/first.do");
        aContext.setHandler(new PageHttpHandler("" +
                "<html>" +
                "   <body>" +
                "       <p><a href=\"/second.do\">go to the second</a></p>" +
                "   </body>" +
                "</html>"
        ));

        HttpContext bContext = server.createContext("/second.do");
        bContext.setHandler(new PageHttpHandler("" +
                "<html>" +
                "   <body>" +
                "       <p><a href=\"/first.do\">go to the first</a></p>" +
                "   </body>" +
                "</html>"
        ));

        server.start();
    }
}
