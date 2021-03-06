package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import module.*;



public class serverWorker extends Thread {

    private final Socket clientSocket;
    private boolean logedIn=false;
    private String login;
    private String password;
    private Server server;
    private OutputStream outputStream;
    private Users users;
    private Groups groups;
    private User user;
   
    

    public serverWorker(Server server, Socket clientSocket, Users users, Groups groups){
        this.server=server;
        this.clientSocket=clientSocket;
        this.users=users;
        this.groups=groups;
    }
    public Boolean isLogedIn(){
        return this.logedIn;
    }
    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException | InterruptedException e) {
           // e.printStackTrace();
        }
        
    }
    private  void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream= clientSocket.getInputStream();
        this.outputStream= clientSocket.getOutputStream();
         
        BufferedReader reader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        while ((line=reader.readLine())!= null){
            String [] token= line.split(" ");
            if (token!=null && token.length>0){
                String cmd= token[0];
                if("quit".equalsIgnoreCase(cmd) ){
                    if(this.isLogedIn())
                        handelLogOut(outputStream);
                    break;
                }
                else if ("getOnline".equalsIgnoreCase(cmd)){
                    if(this.isLogedIn())
                        handelgetOnline();
                    else 
                        send2("you need to login first!!\n");
                    }
                
                else if ( "logout".equalsIgnoreCase(cmd)){
                    if(this.isLogedIn())
                        handelLogOut(outputStream);
                    else 
                        send2("you need to login first!!\n");
                    }
                
                else if ("login".equalsIgnoreCase(cmd)){
                    if(this.isLogedIn())
                        this.send2("you have loged in already!!\n");
                    else
                        handelLogIn(outputStream,token);

                }
                else if("msg".equalsIgnoreCase(cmd)){
                    if(this.isLogedIn())
                        handelMsg(token);
                    else
                        this.send2("login first!!\n");    

                }
                else if ("join".equalsIgnoreCase(cmd)){
                    if(this.isLogedIn())
                        handlJoin(token);
                    else
                        this.send2("login first!!\n");   
                }
                else if ("leave".equalsIgnoreCase(cmd)){
                    if(this.isLogedIn())
                        handlLeave(token);
                    else
                        this.send2("login first!!\n");   
                }
                else if ("GroupMsg".equalsIgnoreCase(cmd)){
                    if(this.isLogedIn())
                        handleGroupMsg(token);
                    else
                        this.send2("login first!!\n");   
                }
                else if ("reg".equalsIgnoreCase(cmd)){
                    if(!this.isLogedIn())
                        handelReg(token);
                    else
                        this.send2("you already hava an acount!!\n");
                }
                else{ 
                    String msg="not recognizable command "+cmd +"\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        
        clientSocket.close();
    }

    private void handelgetOnline() throws IOException {
        List<serverWorker> workersList =server.getWorkersList();
        String msg;
        for (serverWorker worker:workersList){
            if(!this.getLogin().equalsIgnoreCase(worker.getLogin())){
                if (worker.isLogedIn()){
                    msg="Online "+ worker.getLogin()+"\n";
                    this.send2(msg);
                }
            }

        }
        
    }
    private void handelReg(String[] token) throws IOException {
        String userName;
        String password;
        User newUser;
        if(token.length==3){
            userName=token[1];
            password=token[2];
            newUser=users.getUser(userName);
            if(newUser ==null){
                users.add(new User(userName, password, "U"+(users.getLength()+1)));
                send2(userName+" is registard succusfully!!\n");
            }
            else 
                send2("This userName is already used try another one!\n");
        }
        else
            send2("wrong format!!\n");

    }
    private void handlLeave(String[] token) throws IOException {
        if (token.length==2){
            String groupName=token[1];
            Group tmpGroup;
            if(groups.checkGroups(groupName)){
                tmpGroup=groups.getGroup(groupName);
                if( tmpGroup.isMember(this.login)){
                    tmpGroup.remove(user);
                    this.send2("You left "+groupName+"\n");
                }
                else{
                    this.send2("you are not a member of "+groupName+"\n");
                }
            }
            else{
                this.send2(groupName+" does not exist!! \n");

            }
        }
    }
    private void handleGroupMsg(String [] token) throws IOException {
        Group tmpgGroup=groups.getGroup(token[1]);
        if(tmpgGroup!=null){
            if(tmpgGroup.isMember(this.getLogin())){
                String msgBody= msg(token);
                List<serverWorker> workersList =server.getWorkersList();
            
                for (serverWorker worker:workersList){
                    if(tmpgGroup.isMember(worker.getLogin())){
                        String outMsg="Group:"+tmpgGroup.getGroupName()+" "+this.login+" :"+msgBody+"\n";
                        worker.send(outMsg);
                    }
                }
            }
            else{
                this.send2("you are not a member of this group!!\n");
            }
        }
        else 
            this.send2("there is no such group with this name\n");
        
    }
    private void handlJoin(String[] token) throws IOException {
        if (token.length==2){
            String groupName=token[1];
            Group tmpGroup;
            if(groups.checkGroups(groupName)){
                tmpGroup=groups.getGroup(groupName);
                if( !tmpGroup.isMember(this.login)){
                    tmpGroup.add(this.user);
                    this.send2("You joind "+groupName+"\n");
                }
                else{
                    this.send2("you already are a member of "+groupName+"\n");
                }
            }
            else{
                tmpGroup= new Group(groupName, "g"+(groups.getGroups().size()+1));
                groups.add(tmpGroup);
                this.send2(groupName+" has been created in "+ new Date()+"\n");
                tmpGroup.add(this.user);
                this.send2("You joind "+groupName+"\n");

            }
        }
    }
    //"msg" "login(username)" "the message"
    private void handelMsg(String[] token) throws IOException {
        String recever= token[1];

        String msgBody= msg(token);
        List<serverWorker> workersList =server.getWorkersList();
        for (serverWorker worker:workersList){
            if(recever.equalsIgnoreCase(worker.getLogin())){
                String outMsg=this.login+" :"+msgBody+"\n";
                worker.send(outMsg);
            }
        }
        

    }
    private String msg(String[] token) {
        String tmp="";
        if (token.length>2){
            tmp=token[2];
            for (int i=3;i<token.length;i++){
                tmp=tmp+" "+token [i];
            }
        }
        return tmp;
    }
    
    public String getLogin(){
        return this.login;
    }

    private void handelLogOut(OutputStream outputStream) throws IOException {
        server.remove(this);
        if(this.logedIn){
            String msg="loged out!!\n";
            send2(msg);
            this.logedIn=false;
            System.out.println(this.login+" has loged out in "+(new Date()).toString());
            List<serverWorker> workersList =server.getWorkersList();
            
            for (serverWorker worker:workersList){
                if(!this.getLogin().equalsIgnoreCase(worker.getLogin())){
                    worker.send("Sys: offline "+this.getLogin()+"!\n");
                }
            }
            this.login=null;
        }
        else{
            String msg="Can't use this command \n";
            send2(msg);
        }
    }
    private void handelLogIn(OutputStream outputStream, String[] token) throws IOException{
        if (token.length==3){
            this.login=token[1];
            this.password=token[2];
            this.user=users.getUser(this.login);
            if(users.checkUser(this.login, this.password)){
                this.logedIn=true;
                String msg="Loged in successfuly\n";
                send2(msg);
                System.out.println(this.login+ " has loged in succesfuly!! "+ (new Date()).toString());
                String onlineMsg= "Online "+ this.login+"\n";
                List<serverWorker> workersList =server.getWorkersList();
                for (serverWorker worker:workersList){
                    if(!this.getLogin().equalsIgnoreCase(worker.getLogin())){
                        if (worker.isLogedIn()){
                            String msg2="Online "+ worker.getLogin()+"\n";
                            this.send2(msg2);
                        }
                    }

                }
                for (serverWorker worker:workersList){
                    if(!this.getLogin().equalsIgnoreCase(worker.getLogin())){
                        worker.send2(onlineMsg);
                    }
                }
            
            }
            else{
                String msg="Wrong username or passwrod!!\n";
                outputStream.write(msg.getBytes());
                System.out.println("failed log in "+ (new Date()).toString());
            }
        }
    }
    private void send(String msg) throws IOException {

       if (this.login!=null){ 
           outputStream.write(msg.getBytes());
       }
    }
    private void send2(String msg) throws IOException {

            this.outputStream.write(("Sys: "+msg).getBytes());
     }
}
