package postcard.card.post;


import java.util.Date;

public class Wall_post {


    String user_id, post_text, image_uri, thumb_image;
    Date time_stamp;



    //for our model class (constrictor)
    public Wall_post(){

    }

    //constrictor
    public Wall_post(String user_id, String post_text, String image_uri, String thumb_image, Date time_stamp) {
        this.user_id = user_id;
        this.post_text = post_text;
        this.image_uri = image_uri;
        this.thumb_image = thumb_image;
        this.time_stamp = time_stamp;

    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_text() { //used in Wall_post_RecyclerAdapter to get the data
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }


    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }





}
