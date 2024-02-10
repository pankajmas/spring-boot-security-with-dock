package com.example.demo.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponse {

	private String message;
	
	public MessageResponse(String message) {
		// TODO Auto-generated constructor stub
		this.message =message;
	}
	
}
