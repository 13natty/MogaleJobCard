package com.nattySoft.mogalejobcard;

public class ChatMessage {
	public boolean left;
	public String message;
	public String time;
	public String owner;

	public ChatMessage(boolean left, String message, String time, String owner) {
		super();
		this.left = left;
		this.message = message;
		this.time = time;
		this.owner = owner;
	}
}
