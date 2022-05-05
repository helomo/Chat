import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{

    private final int serverPort;
    private ArrayList<serverWorker> workersList= new ArrayList<serverWorker>();
    private Users users;
    private Groups groups;
    public List<serverWorker> getWorkersList(){
        return this.workersList;
    }
    
    public Server(int serverPort, Users users, Groups groups) {
        this.serverPort=serverPort;
        this.users=users;
        this.groups=groups;
    }
    @Override
    public void run() {
        try  { 
            ServerSocket serverSocket = new ServerSocket(this.serverPort);
            while (true){
                System.out.println("connecting.....");
                Socket Csocket= serverSocket.accept();
                System.out.println("connected client : "+ Csocket);
                serverWorker sWorker= new serverWorker(this,Csocket,users,groups);
                workersList.add(sWorker);
                sWorker.start();
                
                
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void remove(serverWorker serverWorker) {
        workersList.remove(serverWorker);
    }
}
