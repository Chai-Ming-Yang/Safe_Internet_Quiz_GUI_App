package LearningModules;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.swing.JOptionPane;
import com.google.gson.Gson;

public class LearningModule implements InterfaceLearningModule{
    private Map<String, LearningModulePage> pages;

    public LearningModule() {

    }

    public boolean hasPages() {
        return pages != null;
    }
    public int getNumPages() {
        return pages.size();
    }
    public String getTitle(int idx) {
        return pages.get(Integer.toString(idx)).getTitle();
    }
    public String getText(int idx) {
        return pages.get(Integer.toString(idx)).getText();
    }
    public String getImg(int idx) {
        return pages.get(Integer.toString(idx)).getImage();
    } 

    public void test() {
        if (pages == null) {
            System.out.println("No pages found.");
            return;
        }

        for (Map.Entry<String, LearningModulePage> entry : pages.entrySet()) {
            String pageNumber = entry.getKey();
            LearningModulePage page = entry.getValue();
            System.out.println("Page " + pageNumber + ": " + page.getTitle());
            System.out.println(page.getText());
            System.out.println("Image: " + page.getImage());
            System.out.println("----------");
        }
    }

    public static void main(String[] args) {

        Gson gson = new Gson();
        LearningModule learningModule = new LearningModule();
        try {
            FileReader reader = new FileReader(System.getProperty("user.dir") + File.separator + "resources\\LearningModule\\learning_module.json");

            learningModule = gson.fromJson(reader, LearningModule.class);
            System.out.println(reader);
            if (learningModule.pages == null) {
                System.out.println("Pages are null after deserialization");
            } else {
                learningModule.test();
            }

            reader.close();
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Parsing learning_module.json. Error: "+e);
            return;
        }

    }
    
}