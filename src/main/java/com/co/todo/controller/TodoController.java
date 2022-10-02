package com.co.todo.controller;

import com.co.todo.dto.ResponseDTO;
import com.co.todo.dto.TodoDTO;
import com.co.todo.model.TodoEntity;
import com.co.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo(){
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto){
        try {
            String temporaryUserId = "temporary-user";

            // (1) TodoEntity로 변환한다
            TodoEntity todoEntity = TodoDTO.toEntity(dto);

            // (2) id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문이다.
            todoEntity.setId(null);

            // (3) 임시 사용자 아이디를 설정해 준다.
            todoEntity.setUserId(temporaryUserId);

            // (4) 서비스를 이용해 Todo 엔티티를 생성한다.
            List<TodoEntity> todoEntityList = service.create(todoEntity);

            // (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> todoDTOList = todoEntityList.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (6) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(todoDTOList).build();

            // (7) ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            // (8) 혹시 예외가 있는 경우 dto 대신 error에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(){
        String temporaryUserId = "temporary-user";

        // (1) 서비스 메소드의 retrieve() 메서드를 사용해 Todo 리스트를 가져온다.
        List<TodoEntity> todoEntities = service.retrieve(temporaryUserId);
        // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> todoDTOS = todoEntities.stream().map(TodoDTO::new).collect(Collectors.toList());
        // (3) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화 한다.
        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(todoDTOS).build();
        // (4) ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(responseDTO);

    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto){
        String temporaryUserId = "temporary-user";

        // (1) dto를 entity로 변환한다.
        TodoEntity todoEntity = TodoDTO.toEntity(dto);
        // (2) id를 temporaryUserId로 초기화한다.
        todoEntity.setUserId(temporaryUserId);
        // (3) 서비스를 이용해 entity를 업데이트한다.
        List<TodoEntity> entities = service.update(todoEntity);
        // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        // (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        // (6) ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto){
        String temporaryUserId = "temporary-user";

        TodoEntity todoEntity = TodoDTO.toEntity(dto);
        todoEntity.setUserId(temporaryUserId);
        List<TodoEntity> entities = service.delete(todoEntity);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
