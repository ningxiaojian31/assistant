package cn.zdxh.assistant.bean;

import java.io.Serializable;

/**
 * 接收订单的详细信息
 */
public class UserAndPublish implements Serializable{
    User user=new User();
    Publish publish=new Publish();

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
        return "UserAndPublish{" + "user=" + user + ", publish=" + publish + '}';
    }
}
