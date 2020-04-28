package com.alzheimer.alarm;



/**
 * Created by Yao on 2020/4/25.
 */

public class Note_table {

    private Integer id;

    private String eventname;

    /**
     * base64 picture
     */
    private String picture;

    /**
     * at home outhome
     */
    private String typename;

    private String description;

    private String time;

    private String createtime;

    private Integer colorkey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getColorkey() {
        return colorkey;
    }

    public void setColorkey(Integer colorkey) {
        this.colorkey = colorkey;
    }
}
