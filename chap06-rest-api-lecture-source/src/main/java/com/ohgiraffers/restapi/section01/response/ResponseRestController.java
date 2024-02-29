package com.ohgiraffers.restapi.section01.response;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/* 설명. RestController는 모든 핸들러 메소드에 @ResponseBody를 붙여주는 어노테이션이다.*/
/* 설명. 이 컨트롤ㄹ러의 핸들러 메소드 반환값은 이제 viewResolver가 아닌 JSON문자열로 반환한다.*/
@RestController     //rest Api를 위한 컨트롤러
@RequestMapping("/response")
public class ResponseRestController {
    @GetMapping("/hello")
    public String helloWorld(){
        return "hello world!";
    }
    @GetMapping("/random")
    public int getRandomNumber(){
        Random random = new Random();
        return random.nextInt(1,6);
    }

    @GetMapping("/message")
    public Message getMessage(){
        return new Message(200,"메시지를 응답합니다.");
    }


    /*RestController에서 반환한 것(자바 타입)은 모두 JSONArray 형태([]) 또는 JSONOBJECT형태({})로 바뀌어 문자열로 반환된다.!
    * 1. Map 또는 일반 클래스 타입은 -> {} 형태
    * 2. ArrayList -> [] 형태
    *
    * JSON(javascript object Notation): 자바스크립트가 인지할 수 있는 객체 형태의 문자열
    * */
    @GetMapping("/test")
    public List<Map<String, Message>> getTest(){
        List<Map<String, Message>> list = new ArrayList<>();
        Map<String, Message> map= new HashMap<>();
        map.put("test1", new Message(200,"성공1"));
        map.put("test2", new Message(200,"성공2"));
        list.add(map);
        return list;
    }

    @GetMapping("/list")
    public List<String> getList(){
        return List.of(new String[]{"사과","바나나","복숭아"});
    }
    @GetMapping("/map")
    public Map<Integer,String> getMapping(){
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message(200,"정상응답"));
        messageList.add(new Message(404,"페이지를 찾을 수 없습니다."));
        messageList.add(new Message(500,"개발자의 잘못"));
        return messageList.stream().collect(Collectors.toMap(Message::getHttpStatusCode,Message::getMessage));
    }

    /*이미지 응답하기
    * produces는 response header의 content-type 설정이다.
    * */
    @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage() throws IOException {
        return getClass().getResourceAsStream("/static/gom.png").readAllBytes();
    }

    /*ResponseEntity는 필수는 아니지만 유용하게 사용헐 수 있다.*/
    @GetMapping("/entity")
    public ResponseEntity<Message> getEntity(){
        return ResponseEntity.ok(new Message(200,"응답 성공"));
    }

    
}
