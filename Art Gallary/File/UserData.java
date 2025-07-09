package File;

import Entity.ArtItem;
import java.io.*;
import java.util.List;

public class UserData {
    public static void saveData(String method, List<ArtItem> paintings, List<Integer> quantities, double total) {
        try {
            FileWriter writer = new FileWriter("Painting_purchase_history.txt", true);
            writer.write("Method: " + method + "\n");
            for (int i = 0; i < paintings.size(); i++) {
                ArtItem p = paintings.get(i);
                int quantity = quantities.get(i);
                writer.write("- " + p.getName() + " (" + p.getAuthor() + ") - BDT" + p.getPrice() + " x " + quantity + " = BDT" + (p.getPrice() * quantity) + "\n");
            }
            writer.write("Total: BDT" + total + "\n-----\n");
            writer.close();
        } 
		
		catch (IOException e) {
            e.printStackTrace();
        }
    }
}