package com.alzheimer.alarm;

import java.io.Serializable;



public class Vic implements Serializable {


    private Integer id;

    private String name;

    private String description;

    private String img;

    private String tours1;

    private String tours2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTours1() {
        return tours1;
    }

    public void setTours1(String tours1) {
        this.tours1 = tours1;
    }

    public String getTours2() {
        return tours2;
    }

    public void setTours2(String tours2) {
        this.tours2 = tours2;
    }
}
