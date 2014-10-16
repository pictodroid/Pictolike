package com.app.pictolike.data;

public class PictoFile {
	public String username;
	public String filename;
	public String dateCreated;
	public String locationCreated;
	public String img221B;
	public long noOfLikes;
	public long noOfViews;
    public void like(){
        noOfLikes+=1;
    }
}
