package com.test.taskopencode.repository;

import com.test.taskopencode.model.Questionnaire;
import com.test.taskopencode.model.User;
import com.test.taskopencode.model.UsersAnswers;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface UsersAnswersRepository extends CrudRepository<UsersAnswers, Long> {
    int countByQuestionnaireAndUser(Questionnaire questionnaire, User user);
    void deleteByQuestionnaireAndUser(Questionnaire questionnaire, User user);

    ArrayList<UsersAnswers> getUsersAnswersByQuestionnaire (Questionnaire questionnaire);
    ArrayList<UsersAnswers> getUsersAnswersByQuestionnaireAndUser(Questionnaire questionnaire, User user);
}
