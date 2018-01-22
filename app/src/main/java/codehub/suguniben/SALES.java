package codehub.suguniben;

import java.util.concurrent.TimeUnit;

/**
 * Created by IK on 2017-11-21.
 */

public class SALES{
    private String name;
    private String amount;
    private String desc;
    private String time;
    private String tel;
    private String type;
    private String img1;
    private String img2;
    private String img3;
    private String specifty;
    public SALES(){

    }

    public SALES(String name, String amount, String desc, String time, String tel, String type, String img1, String img2, String img3, String specifty) {
        this.name = name;
        this.amount = amount;
        this.desc = desc;
        this.time = time;
        this.tel = tel;
        this.type = type;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.specifty = specifty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getSpecifty() {
        return specifty;
    }

    public void setSpecifty(String specifty) {
        this.specifty = specifty;
    }


}
