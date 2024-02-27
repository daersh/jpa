package com.ohgiraffers.section05.compositekey.subsection01.embedded;

import jakarta.persistence.*;

@Table(name = "tbl_member_section05_subsection01")
@Entity(name = "member_section05_subsection01")
public class Member {

    @EmbeddedId
    private  MEMBERPK memberpk; //복합키
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;

    public Member() {
    }

    public Member(MEMBERPK memberpk, String phone, String address) {
        this.memberpk = memberpk;
        this.phone = phone;
        this.address = address;
    }

    public MEMBERPK getMemberpk() {
        return memberpk;
    }

    public void setMemberpk(MEMBERPK memberpk) {
        this.memberpk = memberpk;
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
    @Override
    public String toString() {
        return "Member{" +
                "memberpk=" + memberpk +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
