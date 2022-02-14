package com.test.taskopencode.repository;

import com.test.taskopencode.model.Question;
import com.test.taskopencode.model.Questionnaire;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> {

}
