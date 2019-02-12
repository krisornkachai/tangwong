package com.example.krisorn.tangwong.Model;



import java.util.List;

public class Request {
    private String status;
    private String uid;
    private String nameuser;

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    private String phone;
    private String roomNumber;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Request(String roomNumber) {
        this.roomNumber = roomNumber;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Request(String status, String uid, String phone, String name, String moreDetail, String total, List<Order> items,String roomNumber,String nameuser) {
        this.status = status;
        this.uid = uid;
        this.phone = phone;
        this.name = name;
        this.moreDetail = moreDetail;
        this.total = total;
        this.items = items;
        this.roomNumber= roomNumber;
        this.nameuser = nameuser;

    }
}
