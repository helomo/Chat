import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class serverWorker extends Thread {

    private final Socket clientSocket;
    private boolean logedIn=false;
    private String login;
    private String password;
    private Server server;
    private OutputStream outputStream;
    private Users users;
   
    

    public serverWorker(Server server, Socket clientSocket, Users users2){
        this.server=server;
        this.clientSocket=clientSocket;
        this.users=users2;
    }
    public Boolean isLogedIn(){
        return this.logedIn;
    }
    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
                if("quit".equalsIgnoreCase(cmd) || "logout".equalsIgnoreCase(cmd)){
                    handelLogOut(outputStream);
                    break;
                }
                else if ("login".equalsIgnoreCase(cmd)){
                    handelLogIn(outputStream,token);

                }
                else if("msg".equalsIgnoreCase(cmd)){
                    handelMsg(token);

                }
                else if ("join".equalsIgnoreCase(cmd)){
                    handlJoin(token);
                }
                else{ 
                    String msg="not recognizable command "+cmd +"\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        
        clientSocket.close();
    }

    private void handlJoin(String[] token) {
        if (token.length==2){
            
        }
    }
    //"msg" "login(username)" "the message"
    private void handelMsg(String[] token) throws IOException {
        String recever= token[1];

        String msgBody= msg(token);
        List<serverWorker> workersList =server.getWorkersList();
        for (serverWorker worker:workersList){
            if(recever.equalsIgnoreCase(worker.getLogin())){
                String outMsg="Msg "+this.login+" :"+msgBody+"\n";
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
            String msg="loged out!!";
            outputStream.write(msg.getBytes());
            this.logedIn=false;
            System.out.println(this.login+" has loged out in "+(new Date()).toString());
            List<serverWorker> workersList =server.getWorkersList();
            
            for (serverWorker worker:workersList){
                if(!this.getLogin().equalsIgnoreCase(worker.getLogin())){
                    worker.send(this.getLogin()+" has loged out!!");
                }
            }
            this.login=null;
        }
        else{
            String msg="Can't use this command \n";
            outputStream.write(msg.getBytes());
        }
    }
    private void handelLogIn(OutputStream outputStream, String[] token) throws IOException{
        if (token.length==3){
            this.login=token[1];
            this.password=token[2];
            if(users.checkUser(this.login, this.password)){
                this.logedIn=true;
                String msg="Loged in successfuly";
                outputStream.write(msg.getBytes());
                System.out.println(this.login+ " has loged in succesfuly!! "+ (new Date()).toString());
                String onlineMsg= "Online "+ this.login+"\n";
                List<serverWorker> workersList =server.getWorkersList();
                for (serverWorker worker:workersList){
                    if(!this.getLogin().equalsIgnoreCase(worker.getLogin())){
                        if (worker.isLogedIn()){
                            String msg2="Online "+ worker.getLogin()+"\n";
                            this.send(msg2);
                        }
                    }

                }
                for (serverWorker worker:workersList){
                    if(!this.getLogin().equalsIgnoreCase(worker.getLogin())){
                        worker.send(onlineMsg);
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
}
