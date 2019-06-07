package com.bookeater.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

    private String Cid;
    private String Cname;

    public String getCid() { return Cid; }
    public void setCid(String cid) { this.Cid = cid; }

    public String getCname() { return Cname; }
    public void setCname(String cname) { this.Cname = cname; }

    @Override
    public String toString() {
        return "Category{" +
                "Cid='" + Cid + '\'' +
                ", Cname='" + Cname + '\'' +
                '}';
    }
}
