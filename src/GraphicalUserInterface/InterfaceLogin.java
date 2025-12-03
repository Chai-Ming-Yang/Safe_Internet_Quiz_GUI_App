package GraphicalUserInterface;
public interface InterfaceLogin {
    void signup();
    void login();
    boolean validName();
    boolean validPass();
    String getNameFeedback();
    String getPasswordFeedback();
}