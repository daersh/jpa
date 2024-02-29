package com.ohgiraffers.restapi.section01.section02.responseentity;

import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.*;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {
    private List<UserDTO> users;

    public ResponseEntityTestController(){
        this.users = new ArrayList<>();
        users.add(new UserDTO(1,"user01","pass01","홍길동",new Date()));
        users.add(new UserDTO(1,"user02","pass02","유관순",new Date()));
        users.add(new UserDTO(1,"user03","pass03","이순신",new Date()));
    }

    /*1. 직접 ResponseEntity 객체 만들기*/
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        Map<String, Object> reponseMap = new HashMap<>();
        reponseMap.put("users",users);
        ResponseMessage responseMessage = new ResponseMessage(200,"조회성공!",reponseMap);

        return new ResponseEntity<>(responseMessage,headers, HttpStatus.OK);
    }

    /*2. 빌더를 활용한 메소드 체이닝 방식으로 ResponseEntity 객체 만들기(요즘 유행- 매개변수많은 경우, 순서 지정할 필요 없어 사용) */
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo){

        return ResponseEntity.ok();
    }

}
