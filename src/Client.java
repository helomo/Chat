import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
   public static void main(String[] args) throws UnknownHostException, IOException {
       Socket socket=new Socket("localhost",8188);
       InputStream in= socket.getInputStream();
       OutputStream out= socket.getOutputStream();
       BufferedReader bf= new BufferedReader(new InputStreamReader(in));
       Scanner scn = new Scanner(System.in);

       Thread write= new Thread(){
           @Override
           public void run() {
            String msg="" ;
            try {
                while(!msg.equals("quit")){
                    msg= scn.nextLine()+"\n";
                    out.write(msg.getBytes());
                }
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

           }
       };
       

       Thread listen= new Thread(){
           @Override
           public void run() {
            String line;
               try {
                while((line=bf.readLine()) != null){
                       System.out.println(line);
                   }
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

           }
       };
       write.start();
       listen.start();

      // socket.close();
   } 
void login(OutputStream outputStream,String username, String password) throws IOException{
       outputStream.write(("login "+username+" "+password+"\n").getBytes());

   }
void logout(OutputStream outputStream) throws IOException{
    outputStream.write(("logout\n").getBytes());

}
void reg(OutputStream outputStream,String username, String password) throws IOException{
    outputStream.write(("reg "+username+" "+password+"\n").getBytes());

}
void quit(OutputStream outputStream) throws IOException{
    outputStream.write(("quit\n").getBytes());

}
void msg(OutputStream outputStream,String reciver, String theMsg) throws IOException{
    outputStream.write(("msg "+reciver+" "+theMsg+"\n").getBytes());

}
void join(OutputStream outputStream,String groupName) throws IOException{
    outputStream.write(("join "+groupName+"\n").getBytes());
}
void leave(OutputStream outputStream,String groupName) throws IOException{
    outputStream.write(("leave "+groupName+"\n").getBytes());
}
void groupMsg(OutputStream outputStream,String groupName, String theMsg) throws IOException{
    outputStream.write(("GroupMsg "+groupName+" "+theMsg+"\n").getBytes());

}

}
