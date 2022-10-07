package com.example.shoppingmall_restapi.controller.sign;

import com.example.shoppingmall_restapi.dto.sign.LoginRequestDto;
import com.example.shoppingmall_restapi.dto.sign.ReissueRequestDto;
import com.example.shoppingmall_restapi.dto.sign.SignupRequestDto;
import com.example.shoppingmall_restapi.response.Response;
import com.example.shoppingmall_restapi.service.sign.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(value = "Sign Controller", tags = "Sign")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SignController {

    private final SignService signService;

    @ApiOperation(value = "유저 회원가입", notes = "유저 회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up/user")
    public Response userSignup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        signService.userSignup(signupRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "판매자 회원가입", notes = "판매자 회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up/seller")
    public Response guideSignup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        signService.sellerSignup(signupRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다.")
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody LoginRequestDto req) {
        return Response.success(signService.signIn(req));
    }


    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급 요청")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
        return Response.success(signService.reissue(reissueRequestDto));
    }
}
