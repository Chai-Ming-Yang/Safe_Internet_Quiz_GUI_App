package LearningModules;
public class LearningModulePage {
    private String title;
    private String text;
    private String image;

    public LearningModulePage() {}

    public LearningModulePage(String newTitle, String newText, String newImage) {
        title = newTitle;
        text = newText;
        image = newImage;
    }
    
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }
}