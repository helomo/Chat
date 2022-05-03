import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    private int serverPort;

    public Server(int serverPort) {
        this.serverPort=serverPort;
    }
    @Override
    public void run() {
        try  { 
            ServerSocket serverSocket = new ServerSocket(this.serverPort);
            while (true){
                System.out.println("connecting.....");
                Socket Csocket= serverSocket.accept();
                System.out.println("connected client : "+ Csocket);
                serverWorker sWorker= new serverWorker(Csocket);
                sWorker.start();
                
                
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
