package com.test.taskopencode.repository;

import com.test.taskopencode.model.Answer;
import com.test.taskopencode.model.Question;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepository extends CrudRepository<Answer, Long> {

}
