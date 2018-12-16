package dev.Kdice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    String line = null;
    BufferedReader inputStream = null;
    PrintWriter outputStream = null;
    Socket socket = null;

    public ServerThread(Socket socket){
        this.socket=socket;
    }

    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());
        } catch(IOException e){
            System.out.println("IO error in server thread");
        }

        outputStream.println("POLACZONO");
        outputStream.flush();


        /* Odbieranie stringa
        try {
            line=inputStream.readLine();
            while(line.compareTo("QUIT")!=0){

                outputStream.println(line);
                outputStream.flush();
                System.out.println("Response to Client  :  "+line);
                line=inputStream.readLine();
            }
        } catch (IOException e) {

            line=this.getName(); //reused String line for getting thread name
            System.out.println("IO Error/ Client "+line+" terminated abruptly");
        }
        catch(NullPointerException e){
            line=this.getName(); //reused String line for getting thread name
            System.out.println("Client "+line+" Closed");
        }

        finally{
            try{
                System.out.println("Connection Closing..");
                if (inputStream!=null){
                    inputStream.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if(outputStream!=null){
                    outputStream.close();
                    System.out.println("Socket Out Closed");
                }
                if (socket!=null){
                    socket.close();
                    System.out.println("Socket Closed");
                }

            }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }//end finally
        */
    }
}
