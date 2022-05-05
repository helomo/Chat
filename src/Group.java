import java.util.ArrayList;

public class Group {
    private String groupName;
    private String groupId;
    private ArrayList<User> gorupUsers=new ArrayList<User>();
    public Group(String groupName,String groupId){
        this.groupName=groupName;
        this.groupId=groupId;
    }
    public Group(String groupName,String groupId,ArrayList<User> groupUsers){
        this.groupName=groupName;
        this.groupId=groupId;
        this.gorupUsers=groupUsers;
    }

    public void add(User user){
        gorupUsers.add(user);
    }
    public String getGroupName(){
        return this.groupName;
    }
    public String getGroupId(){
        return this.groupId;
    }
    public ArrayList<User> getGroupUsers(){
        return this.gorupUsers;
    }
    public boolean isMember(String username){
        boolean tmp =false;
        for (User user:gorupUsers){
            tmp= tmp || username.equalsIgnoreCase(user.getUsername());
        }
        return tmp;
    }
    public String toString(){
        return this.groupId+": "+this.groupName+" ";
    }
}
