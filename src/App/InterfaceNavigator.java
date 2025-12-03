package App;

public interface InterfaceNavigator {
    void setUser(String id, String username, int stars);
    void clearUser();
    User getUser();
    void showScreen(String screen_name);
}