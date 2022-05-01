package se.app.vocabulary.model;

public class Words {

    private int id;
    private String english;
    private String hungarian;

    public Words(int id, String english, String hungarian) {
        this.id = id;
        this.english = english;
        this.hungarian = hungarian;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getHungarian() {
        return hungarian;
    }

    public void setHungarian(String hungarian) {
        this.hungarian = hungarian;
    }

    @Override
    public String toString() {
        return "Words {" +
                "id=" + id +
                ", english='" + english + '\'' +
                ", hungarian='" + hungarian + '\'' +
                '}';
    }

}
