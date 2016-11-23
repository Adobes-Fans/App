import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by crcrcry on 2016/11/21.
 */
public class Server {

    public static void main(String args[]) {
        Control c = new Control();

        try {
            InetAddress addr = InetAddress.getLocalHost();
            ServerSocket server = new ServerSocket(11111);
            System.out.println("Wait for connection! My ip is " + addr);
            Socket socket = server.accept();
            System.out.println("Success connection!");

            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            while (true) {

                String nowLine = is.readLine();
                System.out.println("Client:" + nowLine);

                c.dealStr(nowLine);

                if (socket.isClosed()) {
                    break;
                }

                os.println("Server has received!");
                os.flush();
            }
            server.close();
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }
}
