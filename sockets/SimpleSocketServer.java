package web.sockets;

/**
 * Simple Socket server. 
 * Read request and print result in console.
 * Response returns the correct full date.
 */
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

public class SimpleSocketServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("waiting for connection...");

        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("got connection!!!");

            try (InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream()){

                // read request
                byte[] request = readRequest(in);
                System.out.println(new String(request, StandardCharsets.US_ASCII));

                // write response
                String responseString = "Full date: " + new Date().toString();
                byte[] response = responseString.getBytes(StandardCharsets.US_ASCII);
                out.write(response);

            } finally {
                socket.close();
            }
        }
    }


    private static byte[] readRequest(InputStream in) throws IOException {

        byte[] buff = new byte[8192];
        int headerLength = 0;

        while(true) {
            int count = in.read(buff, headerLength, (buff.length - headerLength));

            if (count < 0) {
                throw new RuntimeException("Connection closed! Not full HTTP header");

            } else {
                headerLength += count;

                if (isRequestEnd(buff, headerLength)) {
                    return Arrays.copyOfRange(buff, 0, headerLength);
                }

                if (headerLength >= buff.length) {
                    throw new RuntimeException("Too big HTTP header! More than " + buff.length + " bytes");
                }
            }
        }
    }


    private static boolean isRequestEnd(byte[] request, int length) {

        if (length < 4) {
            return false;
        }

        return request[length - 4] == '\r' &&
                request[length - 3] == '\n' &&
                request[length - 2] == '\r' &&
                request[length - 1] == '\n';
    }
}
