import java.util.ArrayList;

public class ServerMain {
    
    public static void main(String[] args) throws InterruptedException {
    
        Users users=new Users();
        users.add(new User("admin", "admin","u1"));
        users.add(new User("jim", "jim","u2"));
        users.add(new User("dio", "69","u3"));
        users.add(new User("eren","tatakae","u3" ));
        int port = 8188;
        Server server=new Server(port,users);
        server.start();
    }

    
}
