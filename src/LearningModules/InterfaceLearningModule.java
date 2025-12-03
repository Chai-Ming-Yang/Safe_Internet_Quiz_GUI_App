package LearningModules;
public interface InterfaceLearningModule {
    boolean hasPages();
    int getNumPages();
    String getTitle(int idx);
    String getText(int idx);
    String getImg(int idx);
    void test();
}