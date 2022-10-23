import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class ServerContentProcess {
    static HashMap<Integer, byte[]> cachedBlock = new HashMap<>();

    public static byte[] fetchBlock(int hashCode){

        return cachedBlock.get(hashCode);
    }
    public static LinkedList<Integer> fetchImage(String selectedItem) throws IOException {
        LinkedList<Integer> hashRequireSet = new LinkedList<>();
        System.out.println("Start fetching image");
        byte[] fileContent = Files.readAllBytes(new File("./picLib/" + selectedItem).toPath());
        int i = 0;
        int lastWindowIndex = fileContent.length - 2;
        LinkedList<Byte> blockBuilder = new LinkedList<>();

        while(i < lastWindowIndex){
            if(("" + (128 + fileContent[i]) + (128 + fileContent[i + 1]) + (128 + fileContent[i + 2])).hashCode() % 2048 == 0){
                blockBuilder.addLast(fileContent[i]);
                blockBuilder.addLast(fileContent[i + 1]);
                blockBuilder.addLast(fileContent[i + 2]);
                byte[] cacheBlock = convertLinkedListToByteArray(blockBuilder);
                int hashCode = Arrays.toString(cacheBlock).hashCode();
                if(!cachedBlock.containsKey(hashCode)){
                    cachedBlock.put(hashCode, cacheBlock);
                }
                hashRequireSet.addLast(hashCode);
                blockBuilder = new LinkedList<>();
                i += 3;
            }else {
                blockBuilder.addLast(fileContent[i]);
                i++;
            }
        }
        while (i < fileContent.length){
            blockBuilder.addLast(fileContent[i]);
            i++;
        }
        if(blockBuilder.size() > 0){
            byte[] cacheBlock = convertLinkedListToByteArray(blockBuilder);
            int hashCode = Arrays.toString(cacheBlock).hashCode();
            if(!cachedBlock.containsKey(hashCode)){
                cachedBlock.put(hashCode, cacheBlock);
            }
            hashRequireSet.addLast(hashCode);
        }
        System.out.printf("File: %s has %d blocks\n", selectedItem, hashRequireSet.size());
        return hashRequireSet;
    }

    public static String[] fetchNameList() throws IOException, ClassNotFoundException {
        System.out.println("Start fetching name list");
        String[] nameList;
        File fileDirectory = new File("./picLib");
        fileDirectory.createNewFile();
        nameList = fileDirectory.list();
        return nameList;
    }

    public static byte[] convertLinkedListToByteArray(LinkedList<Byte> linkedList){
        byte[] block = new byte[linkedList.size()];
        Object[] objects = linkedList.toArray();
        for (int i = 0; i < objects.length; i++) {
            block[i] = (byte)objects[i];
        }
        return block;
    }
}