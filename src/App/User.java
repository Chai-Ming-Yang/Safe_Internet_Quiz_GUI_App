package App;
public class User {
    private String  id, 
                    username;
    private int stars;

    public User (String curId, String curName) {
        id = curId;
        username = curName;
        stars = 0;
    }
    public User (String curId, String curName, int curStars) {
        id = curId;
        username = curName;
        stars = curStars;
    }
    public String getID() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public int getStars(){
        return stars;
    }
    public void incrementStars() {
        ++stars;
    }
}