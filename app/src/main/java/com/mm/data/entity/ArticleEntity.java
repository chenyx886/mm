package com.mm.data.entity;

import java.util.List;
/**
 * Company：苗苗
 * Class Describe：文章实体
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class ArticleEntity extends BaseEntity {

    private int ArticleID;
    private int NodeID;
    private String ArticleTitle;
    private String Keyword;
    private String Abstract;
    private String CTime;
    private String Rime;
    private String MTime;
    private String Author;
    private String Source;
    private String SourceLink;
    private int ClickCounter;
    private String IsTop;
    private String IsElite;
    private String CountComment;
    private List<ImageEntity> ImgList;

    public int getArticleID() {
        return ArticleID;
    }

    public void setArticleID(int articleID) {
        ArticleID = articleID;
    }

    public int getNodeID() {
        return NodeID;
    }

    public void setNodeID(int nodeID) {
        NodeID = nodeID;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String keyword) {
        Keyword = keyword;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getCTime() {
        return CTime;
    }

    public void setCTime(String CTime) {
        this.CTime = CTime;
    }

    public String getRime() {
        return Rime;
    }

    public void setRime(String rime) {
        Rime = rime;
    }

    public String getMTime() {
        return MTime;
    }

    public void setMTime(String MTime) {
        this.MTime = MTime;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getSourceLink() {
        return SourceLink;
    }

    public void setSourceLink(String sourceLink) {
        SourceLink = sourceLink;
    }

    public int getClickCounter() {
        return ClickCounter;
    }

    public void setClickCounter(int clickCounter) {
        ClickCounter = clickCounter;
    }

    public String getIsTop() {
        return IsTop;
    }

    public void setIsTop(String isTop) {
        IsTop = isTop;
    }

    public String getIsElite() {
        return IsElite;
    }

    public void setIsElite(String isElite) {
        IsElite = isElite;
    }

    public String getCountComment() {
        return CountComment;
    }

    public void setCountComment(String countComment) {
        CountComment = countComment;
    }

    public List<ImageEntity> getImgList() {
        return ImgList;
    }

    public void setImgList(List<ImageEntity> imgList) {
        ImgList = imgList;
    }
}
