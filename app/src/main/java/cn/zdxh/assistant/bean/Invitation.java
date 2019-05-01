package cn.zdxh.assistant.bean;


import java.io.Serializable;
import java.util.List;

public class Invitation implements Serializable{
    //主键，id不要导错包
    private Integer id;
    //帖子内容
    private String invContent;
    //帖子赞数
    private Integer invLaud;
    //一个帖子有多个评论
    private List<Comment> comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvContent() {
        return invContent;
    }

    public void setInvContent(String invContent) {
        this.invContent = invContent;
    }

    public Integer getInvLaud() {
        return invLaud;
    }

    public void setInvLaud(Integer invLaud) {
        this.invLaud = invLaud;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "id=" + id +
                ", invContent='" + invContent + '\'' +
                ", invLaud=" + invLaud +
                ", comments=" + comments +
                '}';
    }
}
