package com.test.taskopencode.repository;

import com.test.taskopencode.model.Question;
import com.test.taskopencode.model.Questionnaire;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    int countQuestionsByQuestionnaire(Questionnaire questionnaire);
}
