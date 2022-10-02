package com.co.todo.controller;

import com.co.todo.dto.ResponseDTO;
import com.co.todo.dto.TestRequestBodyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test") //resource
public class TestController {
    @GetMapping("/testGetMapping")
    public String testController(){
        return "Hello World!";
    }

    @GetMapping("/{id}")
    public String testControllerWithPathVariables(@PathVariable(required = false) int id){
        return "Hello World!" + id;
    }

    @GetMapping("/testRequestParam")
    public String testControllerWithRequestParam(@RequestParam(required = false) int id){
        return "Hello World!" + id;
    }

    @GetMapping("/testRequestBody")
    public String testControllerWithRequestBody(@RequestBody(required = false) TestRequestBodyDTO requestBodyDTO){
        return "Hello World!" + requestBodyDTO.getId() + "message: " + requestBodyDTO.getMessage();
    }

    @GetMapping("/testResponseBody")
    public ResponseDTO<String> testControllerWithResponseBody(@RequestBody(required = false) TestRequestBodyDTO requestBodyDTO){
        List<String> list = new ArrayList<>();
        list.add("Hello world! I'm ResponseDTO");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return response;
    }

    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testControllerResponseEntity(){
        List<String> list = new ArrayList<>();
        list.add("Hello world! I'm ResponseEntity. And you got 400!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        // http status = 400
        return ResponseEntity.badRequest().body(response);
    }
}
