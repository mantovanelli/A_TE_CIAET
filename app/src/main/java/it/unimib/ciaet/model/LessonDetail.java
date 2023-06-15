package it.unimib.ciaet.model;

public class LessonDetail {

    private String key;

    private String name;
    private String image;
    private String text;

    public LessonDetail(String key, String name, String image, String text) {
        this.key = key;
        this.name = name;
        this.image = image;
        this.text = text;
    }

    public LessonDetail() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
