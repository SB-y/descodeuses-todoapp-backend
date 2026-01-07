package com.descodeuses.planit.repository;

import com.descodeuses.planit.entity.MessageEntity;
import com.descodeuses.planit.entity.ActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByTodoOrderByCreatedAtAsc(ActionEntity todo);
}
