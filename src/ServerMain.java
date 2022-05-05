public class ServerMain {
    
    public static void main(String[] args) throws InterruptedException {
    
        Users users=new Users();
        users.add(new User("admin", "admin","u1"));
        users.add(new User("jim", "jim","u2"));
        users.add(new User("dio", "69","u3"));
        users.add(new User("eren","tatakae","u3" ));
        
        Groups groups = new Groups();
        groups.add(new Group("theFirstGang", "g1")); 
        groups.add(new Group("ItsukiIsTheBest", "g2"));
        groups.add(new Group("Hori'sFanClub", "g3"));
        
        int port = 8188;
        Server server=new Server(port,users,groups);
        server.start();
    }

    
}
