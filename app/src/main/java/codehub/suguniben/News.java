package codehub.suguniben;

/**
 * Created by IK on 2017-11-05.
 */

public class News {
    private String title;
    private String desc;
    private String image;
    private String image_name;
    private String time;
    private String uid;

public News(){

}

    public News(String title, String desc, String image, int like, int views, String users, String uid, String image_name, String time) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.uid=uid;
        this.image_name = image_name;

        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
