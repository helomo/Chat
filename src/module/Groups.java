package module;
import java.util.ArrayList;

public class Groups {
    ArrayList<Group> groups= new ArrayList<Group>();
    public Groups(){

    }
    public void add(Group group){
        this.groups.add(group);
    }
    public ArrayList<Group> getGroups(){
        return this.groups;
    }
    public boolean checkGroups(String groupName)  {
        boolean tmp=false;
        for (Group group:groups){
            tmp= tmp || group.getGroupName().equalsIgnoreCase(groupName);
        }
        return tmp;
    }
    public boolean checkGroupsbyId(String groupId)  {
        boolean tmp=false;
        for (Group group:groups){
            tmp= tmp || group.getGroupId().equalsIgnoreCase(groupId);
        }
        return tmp;
    }
    public Group getGroup(String groupName){
        for(Group group:groups){
            if(group.getGroupName().equalsIgnoreCase(groupName)){
                return group;
            }
        }
        return null;

    }
}
