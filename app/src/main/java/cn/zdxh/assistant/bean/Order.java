package cn.zdxh.assistant.bean;


import java.io.Serializable;

public class Order implements Serializable {
   //对方的预约信息
    private User user;
    //发布的信息
    private Publish publish;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Publish getPublish() {
        return publish;
    }

    public void setPublish(Publish publish) {
        this.publish = publish;
    }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user +
                ", publish=" + publish +
                '}';
    }
}
