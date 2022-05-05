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

}