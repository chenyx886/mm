package com.mm.data.entity;

/**
 * Company：苗苗
 * Class Describe：版本更新实体
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class VersionEntity extends BaseEntity {


    private int ID;
    private int Code;
    private String Name;
    private String Path;
    private String Content;
    private int State;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }
}
