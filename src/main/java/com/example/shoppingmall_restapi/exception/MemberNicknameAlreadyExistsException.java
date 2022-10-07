package com.example.shoppingmall_restapi.exception;

public class MemberNicknameAlreadyExistsException extends RuntimeException{
    public MemberNicknameAlreadyExistsException(String message) {
        super(message);
    }
}
