import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class A2Server extends Thread{
    static ServerSocket serverSocket;

    static {
        try {
            serverSocket = new ServerSocket(8081);
            System.out.println("Server running on 8081");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){

        while(true){
            try {
                Socket socket = serverSocket.accept();
                new Thread(){
                    public void run() {
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            InputStream inputStream = socket.getInputStream();
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                            String clientInput = (String)objectInputStream.readObject();
                            switch (clientInput){
                                case "NameList":
                                    objectOutputStream.writeObject(ServerContentProcess.fetchNameList());
                                    break;
                                default:
                                    try {
                                        int hashCode = Integer.parseInt(clientInput);
                                        objectOutputStream.writeObject(ServerContentProcess.fetchBlock(hashCode));
                                    }catch (Exception e){
                                        objectOutputStream.writeObject(ServerContentProcess.fetchImage(clientInput));
                                    }
                            }

                            socket.close();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
