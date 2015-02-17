package com.nattySoft.mogalejobcard;

public class DrawerItem {

	String ItemName;
	int imgResID;

	String title;
	boolean isSpinner;
	
	String type = null;
	
	int imgCountBG;
	String count;

	public DrawerItem(String itemName, int imgResID) {
		super();
		ItemName = itemName;
		this.imgResID = imgResID;
	}

	public DrawerItem(boolean isSpinner) {
		this(null, 0);
		this.isSpinner = isSpinner;
	}

	public DrawerItem(String title) {
		this(null, 0);
		this.title = title;
	}

	public DrawerItem(String itemName, int imgResID, String type) {
		this(itemName, imgResID);
		this.type = type;
	}

	public DrawerItem(String itemName, int imgResID, int imgCountBG) {
		this(itemName, imgResID);
		this.imgCountBG = imgCountBG;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public int getImgResID() {
		return imgResID;
	}

	public void setImgResID(int imgResID) {
		this.imgResID = imgResID;
	}
	
	public int getImgCountBG() {
		return imgCountBG;
	}

	public void setimgCountBG(int imgCountBG) {
		this.imgCountBG = imgCountBG;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isSpinner() {
		return isSpinner;
	}

}
