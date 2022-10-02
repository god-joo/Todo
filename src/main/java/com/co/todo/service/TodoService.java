package com.co.todo.service;

import com.co.todo.model.TodoEntity;
import com.co.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public String testService(){
        // TodoEntity create
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        // TodoEntity save
        todoRepository.save(entity);
        // TodoEntity serch
        TodoEntity savedEntity = todoRepository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity entity){
        // Validations
        validate(entity);
        // Save
        todoRepository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());
        return todoRepository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId){
        return todoRepository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity){
        // (1) 저장할 엔티티가 유효한지 확인한다.
        validate(entity);
        // (2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않은 엔티티는 없데이트할 수 없기 때문.
        Optional<TodoEntity> original = todoRepository.findById(entity.getId());

        original.ifPresent(todo -> {
        // (3) 반환된 TodoEntity가존재하면 값을 새 entity 값으로 덮어 씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // (4) 데이터베이스에 새 값을 저장한다.
            todoRepository.save(todo);
        });

        // 2.3.2 Retrieve Todo에서 만든 메서드를 이용해 사용자의 모든 Todo 리스트를 리턴한다.
        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity){
        // (1) 저장할 엔티티가 유효한지 확인한다.
        validate(entity);
        try {
            // (2) 엔티티를 삭제한다.
            todoRepository.delete(entity);
        } catch (Exception e){
            // (3) exception 발생 시 id와 exception을 로깅한다.
            log.error("error deleting entity", entity.getId(), e);
            // (4) 컨트롤러로 exception을 보낸다. 데이터베이스 내부 로직을 캡슐화하려면 e를 리턴하지 않고 새 exception을 리턴한다
            throw new RuntimeException("error deleting entity" + entity.getId());
        }
        // (5) 새 Todo 리스트를 가져와 리턴한다.
        return retrieve(entity.getUserId());

    }

    private void validate(final TodoEntity entity) {
        if(entity == null){
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null");
        }

        if(entity.getUserId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

}
