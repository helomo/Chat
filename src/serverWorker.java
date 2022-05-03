import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class serverWorker extends Thread {

    private final Socket clientSocket;
    private boolean logedIn=false;
    private String login;
    private String password;

    public serverWorker(Socket clientSocket){
        this.clientSocket=clientSocket;
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
        OutputStream outputStream= clientSocket.getOutputStream();
         
        BufferedReader reader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        
        while ((line=reader.readLine())!= null){
            String [] token= line.split(" ");
            if (token!=null && token.length>0){
                String cmd= token[0];
                if("quit".equalsIgnoreCase(cmd)){
                    this.logedIn=false;
                    break;
                }
                else if ("login".equalsIgnoreCase(cmd)){
                    handelLogIn(outputStream,token);

                }
                else if ("logout".equalsIgnoreCase(cmd)){
                    handelLogOut(outputStream);
                }
                else{ 
                    String msg="not recognizable command "+cmd +"\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        
        clientSocket.close();
    }
    private void handelLogOut(OutputStream outputStream) throws IOException {
        if(this.logedIn){
            String msg="loged out!!";
            outputStream.write(msg.getBytes());
            this.logedIn=false;
            System.out.println(this.login+" has loged out in "+(new Date()).toString());
        }
        else{
            String msg="Can't use this command";
            outputStream.write(msg.getBytes());
        }
    }
    private void handelLogIn(OutputStream outputStream, String[] token) throws IOException{
        if (token.length==3){
            this.login=token[1];
            this.password=token[2];
            if(this.login.equals("admin")&& this.password.equals("admin")){
                this.logedIn=true;
                String msg="Loged in successfuly";
                outputStream.write(msg.getBytes());
                System.out.println("user has loged in succesfuly!! "+ login+" "+ (new Date()).toString());
            }
            else{
                String msg="Wrong username or passwrod!!";
                outputStream.write(msg.getBytes());
                System.out.println("failed log in "+ (new Date()).toString());
            }
        }
    }
}
