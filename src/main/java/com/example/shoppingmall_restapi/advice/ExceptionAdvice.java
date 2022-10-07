package com.example.shoppingmall_restapi.advice;

import com.example.shoppingmall_restapi.exception.*;
import com.example.shoppingmall_restapi.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    // 500 에러
    @ExceptionHandler(IllegalArgumentException.class) // 지정한 예외가 발생하면 해당 메소드 실행
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 각 예외마다 상태 코드 지정
    public Response illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(500, e.getMessage().toString());
    }

    // 400 에러
    // validation, MethodArgumentNotValidException
    // 각 검증 어노테이션 별로 지정해놨던 메시지를 응답
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) { // 2
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    // 401 응답
    // 아이디 혹은 비밀번호 오류시
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return Response.failure(401, "로그인에 실패하였습니다.");
    }


    // 409 응답
    // username 중복
    @ExceptionHandler(MemberUsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberUsernameAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 아이디 입니다.");
    }

    // 409 응답
    // nickname 중복
    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 닉네임 입니다.");
    }

    // 409 응답
    // email 중복
    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 이메일 입니다.");
    }

    // 409 응답
    // phone 중복
    @ExceptionHandler(MemberPhoneAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberPhoneAlreadyExistsException(MemberPhoneAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 번호 입니다.");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNotFoundException() {
        return Response.failure(404,"해당 유저가 없습니다.");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response productNotFoundException() {
        return Response.failure(404,"해당 상품이 없습니다.");
    }
    @ExceptionHandler(MemberNotEqualsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNotEqualsException() {
        return Response.failure(401,"유저가 일치하지 않습니다.");
    }



}