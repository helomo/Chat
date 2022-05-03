public class ServerMain {
    
    public static void main(String[] args) throws InterruptedException {
        int port = 8188;
            Server server=new Server(port);
            server.start();
    }

    
}
