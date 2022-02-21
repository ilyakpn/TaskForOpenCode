package com.test.taskopencode.controller;

import com.test.taskopencode.model.*;
import com.test.taskopencode.repository.QuestionnaireRepository;
import com.test.taskopencode.repository.UserRepository;
import com.test.taskopencode.repository.UsersAnswersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import other.UserResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UsersAnswersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private UsersAnswersRepository usersAnswersRepository;

    @GetMapping(value="/questionnaire/{id}/results")
    public String questionnaireResults(@PathVariable(value="id") long id, Model model) {

        Questionnaire questionnaire = questionnaireRepository.findById(id).orElseThrow();

        ArrayList<UsersAnswers> usersAnswersList = usersAnswersRepository.getUsersAnswersByQuestionnaire(questionnaire);

        List<User> users = new ArrayList<>();

        for (UsersAnswers ua : usersAnswersList) {
            users.add(ua.getUser());
        }

        List<User> usersDistinct = users.stream()
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("users", usersDistinct);

        return "questionnaire-results";
    }

    @GetMapping(value="/questionnaire/{id}/results/{userId}")
    public String questionnaireUserResult(@PathVariable(value="id") long id, @PathVariable(value="userId") long userId, Model model) {
        User user = userRepository.findById(userId).orElseThrow();
        Questionnaire questionnaire = questionnaireRepository.findById(id).orElseThrow();

        ArrayList<UsersAnswers> usersAnswersList = usersAnswersRepository.getUsersAnswersByQuestionnaireAndUser(questionnaire, user);

        List<Question> questions = questionnaire.getQuestions();
        UserResult userResult = new UserResult();
        userResult.setUser(user);


        List<Question> sortedQuestions = questions.stream()
                .sorted(Comparator.comparingLong(Question::getId))
                .collect(Collectors.toList());

        for (Question q : sortedQuestions) {
            List<UsersAnswers> usersAnswersQuestion = usersAnswersList.stream()
                    .filter(ua -> ua.getAnswer().getQuestion().getId().equals(q.getId()))
                    .collect(Collectors.toList());

            if (usersAnswersQuestion.size() > 0) {
                List<Answer> answerList = new ArrayList<>();

                usersAnswersQuestion.forEach(uaq -> answerList.add(uaq.getAnswer()));

                userResult.getResults().put(q, answerList);
            }
        }

        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("userResult", userResult);

        return "questionnaire-results-user";
    }

}
