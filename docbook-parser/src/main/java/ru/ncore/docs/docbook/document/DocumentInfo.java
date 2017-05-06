package ru.ncore.docs.docbook.document;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class DocumentInfo {
    String title;
    String titleAbbrev;
    String productName;
    String subTitle;
    String contractNum;
    String issueNum;
    String pubDate;

    public DocumentInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAbbrev() {
        return titleAbbrev;
    }

    public void setTitleAbbrev(String titleAbbrev) {
        this.titleAbbrev = titleAbbrev;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getIssueNum() {
        return issueNum;
    }

    public void setIssueNum(String issueNum) {
        this.issueNum = issueNum;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
