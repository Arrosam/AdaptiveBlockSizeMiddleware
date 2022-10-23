import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

public class ClientNetwork {
    public static Image fetchImage(String selectedItem) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 8080);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(selectedItem);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        LinkedList<byte[]> blockList = (LinkedList<byte[]>) objectInputStream.readObject();
        socket.close();
        FileOutputStream fileOutputStream = new FileOutputStream(selectedItem);
        Iterator<byte[]> iterator = blockList.iterator();
        while (iterator.hasNext()){
            fileOutputStream.write(iterator.next());
        }

        return ImageIO.read(new File(selectedItem));
    }

    public static String[] fetchNameList() throws IOException, ClassNotFoundException {
        System.out.println("start fetching name");
        Socket socket = new Socket("localhost", 8080);
        InputStream inputStream = socket.getInputStream();
        System.out.println("    get input stream");
        OutputStream outputStream = socket.getOutputStream();
        System.out.println("    get output stream");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("    get object output stream");
        objectOutputStream.writeObject("NameList");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        System.out.println("    get object input stream");
        String[] nameList = (String[]) objectInputStream.readObject();
        for (String s : nameList) {
            System.out.println(s);
        }
        System.out.println("    get name list");
        socket.close();
        System.out.println("    socket close");
        return nameList;
    }
}
