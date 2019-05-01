package cn.zdxh.assistant.bean;


import java.io.Serializable;

public class User implements Serializable {
    //主键id
    private Integer id;
    //登录名
    private String userLoginname;
    //登录密码
    private String userPassword;
    //姓名
    private String userName;
    //学号
    private String userStudentID;
    //头像
    private String userImage;
    //微信号
    private String userWxNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserLoginname() {
        return userLoginname;
    }

    public void setUserLoginname(String userLoginname) {
        this.userLoginname = userLoginname;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStudentID() {
        return userStudentID;
    }

    public void setUserStudentID(String userStudentID) {
        this.userStudentID = userStudentID;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserWxNumber() {
        return userWxNumber;
    }

    public void setUserWxNumber(String userWxNumber) {
        this.userWxNumber = userWxNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userLoginname='" + userLoginname + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", userStudentID='" + userStudentID + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userWxNumber='" + userWxNumber + '\'' +
                '}';
    }
}
