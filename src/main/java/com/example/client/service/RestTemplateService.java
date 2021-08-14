package com.example.client.service;

import com.example.client.dto.UserRequest;
import com.example.client.dto.UserResponse;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class RestTemplateService {
    //http://localhost/api/server/hello 로 요청
    //response
    public UserResponse hello(){
        //restTemplate 사용
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/hello")
                .queryParam("name","aaaa")
                .queryParam("age",99)//QueryParam같은 경우는 해당 방법을 이용
                .encode()
                .build()
                .toUri();
        System.out.println(uri.toString());

        //RestTmplate 생성
        RestTemplate restTemplate = new RestTemplate();
        //String result = restTemplate.getForObject(uri,String.class); //두번째 매개변수로는 response Type을 적는다
        //Server to Server간의 HTTP메소드는 모두다 지원 한다.
        //주의할 점, getForEntity 같은경우 반환형태를 지정을 해주어야 되고,
        //getForObject 가은경우는 GenericType으로 받을수이싸
        //getForObject를 하는 순간이 Server to server하는 순간이다.

        ResponseEntity<UserResponse> result = restTemplate.getForEntity(uri,UserResponse.class);
        //UserResponse result = restTemplate.getForObject(uri,UserResponse.class);
        //ResponseEntity의 String타입으로 받는다
        //차이점 : result에 여러가 값들을 볼수 있다.
        System.out.println(result.getStatusCode());
        System.out.println(result.getBody());

        return result.getBody();
    }

    public void post() {
        //http://localhost/api/server/hello/user/{userId}/name/{userName}
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100,"steve")//PathVariable값을 적는다, 순차적 매칭이된다
                // PathVariable이 하나라면 buildAndExpand를 써서 해도 가능하다
                //,로 구분해서 계속 넣으면 된다.
                .toUri();

        System.out.println(uri);
        //http body -> object -> object mapper -> json -> rest template -> http body json
        UserRequest req = new UserRequest();
        req.setName("steve");
        req.setAge(10);
        //Object로만 만들고 전송하면 ObjectMapper가 자동으로 변경해준다.
        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri,req,UserResponse.class);
        //서버가 어떻게 값을 줄지 모를때 String으로 받는다
        //일단 String 형식으로 찍고 돌아온것을 보고 JSON을 설계한다.
        ResponseEntity<String> response = restTemplate.postForEntity(uri,req,String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
        //return response.getBody();
    }

    //header 추가
    public UserResponse exchange(){
        //http://localhost/api/server/hello/user/{userId}/name/{userName}
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100,"steve")
                .toUri();
        UserRequest req = new UserRequest();
        req.setName("steve");
        req.setAge(10);

        RequestEntity<UserRequest> requestEntity = RequestEntity
                .post(uri) //Post형식
                .contentType(MediaType.APPLICATION_JSON) //컨텐츠 타입 지정
                .header("x-authorization","abcd")
                .header("custom-header","fffff")//header는 계속 추가할수있다.
                .body(req);//requestBody에 들어갈 Body내용


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestEntity,UserResponse.class);
        return response.getBody();
    }

}
