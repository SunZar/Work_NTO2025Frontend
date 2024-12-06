package ru.myitschool.work.ui;

public class User {

    private int id;
    private String login;
    private String name;
    private String photo;
    private String position;
    private String lastVisit;

    public User(int id, String login, String name, String photo, String position, String lastVisit) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.photo = photo;
        this.position = position;
        this.lastVisit = lastVisit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }
}
