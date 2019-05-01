package cn.zdxh.assistant.bean;


import java.io.Serializable;

public class Reply implements Serializable {
    //主键
    private Integer id;
    //回复类型
    private Integer reType;
    //回复内容
    private String reContent;
    //回复的赞数
    private Integer reLaud;
    //回复的帖子外键
    private Integer comId;
    //回复人的外键
    private Integer userIdFrom;
    //被回复人的外键
    private Integer userIdTo;
    //回复人
    private User userFrom;
    //被回复人
    private User userTo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReType() {
        return reType;
    }

    public void setReType(Integer reType) {
        this.reType = reType;
    }

    public String getReContent() {
        return reContent;
    }

    public void setReContent(String reContent) {
        this.reContent = reContent;
    }

    public Integer getReLaud() {
        return reLaud;
    }

    public void setReLaud(Integer reLaud) {
        this.reLaud = reLaud;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public Integer getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(Integer userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public Integer getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(Integer userIdTo) {
        this.userIdTo = userIdTo;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", reType=" + reType +
                ", reContent='" + reContent + '\'' +
                ", reLaud=" + reLaud +
                ", comId=" + comId +
                ", userIdFrom=" + userIdFrom +
                ", userIdTo=" + userIdTo +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                '}';
    }
}
