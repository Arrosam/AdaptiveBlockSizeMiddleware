import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClientInterface extends Canvas {

    static Image imageToPaint;

    public void paint(Graphics graphics){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        if(imageToPaint == null){
            System.out.println("null") ;
        }else{
            graphics.drawImage(imageToPaint, 10, 52, this);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final String[][] name = {new String[0]};
        try {
            name[0] = ClientNetwork.fetchNameList();
        }catch (IOException e){}
        JFrame jFrame = new JFrame("Get File");
        JButton getButton = new JButton("Get this file");
        getButton.setBounds(266,10, 128, 32);
        JButton refreshButton = new JButton("Refresh file list");
        refreshButton.setBounds(394,10, 192, 32);
        final JComboBox[] dropDownMenu = {new JComboBox(name[0])};
        dropDownMenu[0].setBounds(10, 10, 256, 32);
        ClientInterface clientInterface = new ClientInterface();

        getButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ClientInterface.imageToPaint = getFile(dropDownMenu[0]);
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
                clientInterface.repaint();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] name = ClientNetwork.fetchNameList();
                    dropDownMenu[0].setModel(new JComboBox<>(name).getModel());
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        jFrame.add(refreshButton);
        jFrame.add(dropDownMenu[0]);
        jFrame.add(getButton);
        jFrame.add(clientInterface);

        jFrame.setSize(606,512);
        jFrame.setVisible(true);
    }

    public static Image getFile(JComboBox dropDownMenu) throws IOException, ClassNotFoundException {
        String selectedItem = (String) dropDownMenu.getItemAt(dropDownMenu.getSelectedIndex());
        return ClientNetwork.fetchImage(selectedItem);
    }
}
