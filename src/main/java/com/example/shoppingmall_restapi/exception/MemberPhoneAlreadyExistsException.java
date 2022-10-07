package com.example.shoppingmall_restapi.exception;

public class MemberPhoneAlreadyExistsException extends RuntimeException{
    public MemberPhoneAlreadyExistsException(String message) {
        super(message);
    }
}
