package cz.inovett.simplyidea;

/**
 * Created by uzivatel on 06.05.2017.
 */

public class Blog {

    private  String title;
    private String Category;
    private String Text;

    public Blog(String title, String category, String text) {
        this.title = title;
        Category = category;
        Text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }






     public Blog(){

     }





}
