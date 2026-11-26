package edu.icet.model.dto;

public class MemberDTO {

    private int memberId;
    private String name;
    private String email;
    private String phone;
    private String address;

    public MemberDTO() {
    }

    public MemberDTO(int id, String name, String email, String phone, String address) {
        this.memberId = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public MemberDTO(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int id) {
        this.memberId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
