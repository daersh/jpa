package com.ohgiraffers.restapi.section01.section02.responseentity;

import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {
    private List<UserDTO> users;

    public ResponseEntityTestController(){
        this.users = new ArrayList<>();
        users.add(new UserDTO(1,"user01","pass01","홍길동",new Date()));
        users.add(new UserDTO(2,"user02","pass02","유관순",new Date()));
        users.add(new UserDTO(3,"user03","pass03","이순신",new Date()));
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));
        /*요청 리소스(회원번호와 일치하는 회원 한명)를 추출*/
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList()).get(0);
        Map<String , Object> responseMap = new HashMap<>();
        responseMap.put("user",foundUser);
        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200,"조회 성공!",responseMap));
    }

    /*
    * 기존에 배웠던 @RequestParam과 달리 json 문자열이 해들러 메소드로 넘어올 때는 @RequestBody를 붙이고 받는다.
    * json 문자열의 각 프로퍼티 별로 받을 수도 있지만 한번에 하나의 타입으로 다 받아낼 때는 커맨드 객체(UserDTO)를 활용해야 하며 커맨드
    * 객체는 json 문자열의 프로퍼티 명과 일치해야 한다.
    * {
    *   "id" : "user04"
    * }
    * 이 때  [ "id" : "user04" ]을 프로퍼티, id를 프퍼티 명이라 한다.
    *
    * */
    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser){//command 객체?
        int lastUserNo = users.get(users.size()-1).getNo();
        newUser.setNo(lastUserNo+1);
        users.add(newUser);
        System.out.println("newUser = " + newUser);
        return ResponseEntity.created(URI.create("/entity/users/"+users.get(users.size()-1).getNo())).build();
    }

    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@RequestBody UserDTO data, @PathVariable int userNo){
        /*PathVariable로 넘어온 번호와 일치하는 회원 한명 추출*/
        UserDTO foundUser = users.stream().filter(user->user.getNo()==userNo).collect(Collectors.toList()).get(0);
        /*사용자가 넘겨준 수정하고자 한 데이터로 회원 정보 수정*/
        foundUser.setId(data.getId());
        foundUser.setPwd(data.getPwd());
        foundUser.setName(data.getName());

        return ResponseEntity.created(URI.create("/entity/users/"+userNo)).build();
    }

    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo){
        UserDTO foundUser = users.stream().filter(user->user.getNo()==userNo).collect(Collectors.toList()).get(0);
        /*arrayList에서 제공하는 remove는 인덱스 대신 자신이 관리하는 객체를 찾아 지워줄 수 있음*/
        users.remove(foundUser);
        return ResponseEntity.noContent().build();// 204 응답코드
    }
}
