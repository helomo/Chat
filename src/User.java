public class User {
    private String username;
    private String password;
    private String id;
    
    public User(String username,String password,String id){
        this.username=username;
        this.password=password;
        this.id=id;
    }
    public String getUsername() {
        return this.username;
    }
    public Boolean check(String username, String password){
        return username.equals(this.username) && password.equals(this.password);
    }
    
}
