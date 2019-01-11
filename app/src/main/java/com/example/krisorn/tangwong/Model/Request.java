package com.example.krisorn.tangwong.Model;



import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String moreDetail;
    private String total;
    private List<Order> items;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoreDetail() {
        return moreDetail;
    }

    public void setMoreDetail(String moreDetail) {
        this.moreDetail = moreDetail;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getItems() {
        return items;
    }

    public void setItems(List<Order> items) {
        this.items = items;
    }

    public Request() {

    }

    public Request(String phone, String name, String moreDetail, String total, List<Order> items) {

        this.phone = phone;
        this.name = name;
        this.moreDetail = moreDetail;
        this.total = total;
        this.items = items;
    }
}
