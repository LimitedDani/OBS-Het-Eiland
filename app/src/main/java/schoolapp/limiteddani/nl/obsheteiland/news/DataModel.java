package schoolapp.limiteddani.nl.obsheteiland.news;

/**
 * Created by daniq on 7-4-2017.
 */

public class DataModel {
    String image;
    String title;
    String description;
    String pdf;
    public DataModel(String image, String title, String description, String pdf) {
        this.image=image;
        this.title=title;
        this.description=description;
        this.pdf = pdf;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getPDF() {
        return pdf;
    }
}
