package com.listerdigital.MSA.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import java.text.*;

public class Client {
	@NotEmpty(message="Client Name cannot be left empty")
	private String name;
	
	@Email
	private String bcc;
	
	private long folderSize;
	private String lastUpdated;
	private int total_mails;
	private String imagePath;
	private long allocated=1024;
	private String percentageConsumed="0.00";
	public String getPercentageConsumed() {
		return percentageConsumed;
	}
	public void setPercentageConsumed() {
		double fs=(double)this.folderSize;
		double alc=(double)this.allocated;
		double res=(fs/alc)*100;
		DecimalFormat df = new DecimalFormat("#.##");
		this.percentageConsumed=df.format(res);
	}
	public long getAllocated() {
		return allocated;
	}
	public void setAllocated(long allocated) {
		this.allocated = allocated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public long getFolderSize() {
		return folderSize;
	}
	public void setFolderSize(long folderSize) {
		this.folderSize = folderSize;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public int getTotal_mails() {
		return total_mails;
	}
	public void setTotal_mails(int total_mails) {
		this.total_mails = total_mails;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
