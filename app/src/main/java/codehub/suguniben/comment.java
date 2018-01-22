package codehub.suguniben;

/**
 * Created by IK on 2017-11-11.
 */

public class comment {

    private String image;
    private String user;
    private String comment;
    private String uid;
    public  comment(){


    }

    public comment(String image, String user, String comment,String uid) {
        this.image = image;
        this.user = user;
        this.comment = comment;
        this.uid = uid;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
