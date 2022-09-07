package bean;

import java.util.ArrayList;

public class Merchandise {
    String url;
    String des;
    String price;
    String id;

    public Merchandise(String id,String url, String des, String price) {
        this.url = url;
        this.des = des;
        this.price = price;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String toString(){
        return "{" +  "\"url\"" + ':' + this.url  + ","+ "\"des\""+':'  +this.des +','+ "\"price\""+':' +  this.price+ "}";
    }
}
