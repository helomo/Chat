import java.util.ArrayList;

public class Users{
    ArrayList<User> users=new ArrayList<User>();
    public Users(){

    }
    public void add (User user){
        users.add(user);
    }
    public boolean checkUser(String username,String password){
        boolean tmp=false;
        for(User user:users){
            tmp=tmp || user.check(username, password);
        }
        return tmp;
    }
    public User getUser(String username){
        for(User user:users){
            if(user.getUsername().equalsIgnoreCase(username)){
                return user;
            }
        }
        return null;

    }

}