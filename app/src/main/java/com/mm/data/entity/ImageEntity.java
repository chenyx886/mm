package com.mm.data.entity;
/**
 * Company：苗苗
 * Class Describe：附件实体
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class ImageEntity extends BaseEntity {

    private int ImgID;
    private int ArticleID;
    private int FindID;
    private String Path;
    private String Title;
    private String Content;
    private String IsHomepage;
    private String IsInsert;

    private boolean addItem;
    private boolean isSelected;

    private int width;
    private int height;

    public int getImgID() {
        return ImgID;
    }

    public void setImgID(int imgID) {
        ImgID = imgID;
    }

    public int getArticleID() {
        return ArticleID;
    }

    public void setArticleID(int articleID) {
        ArticleID = articleID;
    }

    public int getFindID() {
        return FindID;
    }

    public void setFindID(int findID) {
        FindID = findID;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getIsHomepage() {
        return IsHomepage;
    }

    public void setIsHomepage(String isHomepage) {
        IsHomepage = isHomepage;
    }

    public String getIsInsert() {
        return IsInsert;
    }

    public void setIsInsert(String isInsert) {
        IsInsert = isInsert;
    }

    public boolean isAddItem() {
        return addItem;
    }

    public void setAddItem(boolean addItem) {
        this.addItem = addItem;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
