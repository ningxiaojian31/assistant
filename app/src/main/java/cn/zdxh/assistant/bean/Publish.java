package cn.zdxh.assistant.bean;


import java.io.Serializable;

public class Publish implements Serializable {
    //主键
    private Integer id;
    //标记是拼饭还是拼伞
    private String pubSign;
    //发布信息的照片
    private String pubImage;
    //发布的详细内容
    private String pubContent;
    //发布信息的用户外键
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPubSign() {
        return pubSign;
    }

    public void setPubSign(String pubSign) {
        this.pubSign = pubSign;
    }

    public String getPubImage() {
        return pubImage;
    }

    public void setPubImage(String pubImage) {
        this.pubImage = pubImage;
    }

    public String getPubContent() {
        return pubContent;
    }

    public void setPubContent(String pubContent) {
        this.pubContent = pubContent;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return "Publish{" +
                "id=" + id +
                ", pubSign='" + pubSign + '\'' +
                ", pubImage='" + pubImage + '\'' +
                ", pubContent='" + pubContent + '\'' +
                ", userId=" + userId +
                '}';
    }
}
