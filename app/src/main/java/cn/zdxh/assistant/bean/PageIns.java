package cn.zdxh.assistant.bean;

import java.io.Serializable;
import java.util.List;

public class PageIns implements Serializable {
    private List<Invitation> content;

    public List<Invitation> getContent() {
        return content;
    }

    public void setContent(List<Invitation> content) {
        this.content = content;
    }
}
