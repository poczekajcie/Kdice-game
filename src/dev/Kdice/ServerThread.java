package dev.Kdice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    String line = null;
    String login = null;
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

        //Send a welcome message
        outputStream.println("POLACZONO");
        outputStream.flush();

        //Receiving a messages
        try {
            line=inputStream.readLine();
            if (line.startsWith("LOGIN")) {
                String[] output = line.split(" ");
                login = output[1];
                Game.loging(login);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
