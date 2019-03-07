package com.danielkioko.peachnotes;

public class Story {

    private String heading, story;
    private int image;

    public Story() {
    }

    public Story(String heading, String story, int image) {
        this.heading = heading;
        this.story = story;
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
