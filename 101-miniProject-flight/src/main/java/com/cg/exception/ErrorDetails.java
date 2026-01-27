package com.cg.exception;

import java.util.Date;

public class ErrorDetails {
private String message;
private Date timeStamp;
private String Detail;
public ErrorDetails(String message, Date timeStamp, String detail) {
	super();
	this.message = message;
	this.timeStamp = timeStamp;
	Detail = detail;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Date getTimeStamp() {
	return timeStamp;
}
public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
}
public String getDetail() {
	return Detail;
}
public void setDetail(String detail) {
	Detail = detail;
}

}
