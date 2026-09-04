package com.example.employee.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerResponse<T> {

	private int statusCode;
	private String message;
	private T data;

}