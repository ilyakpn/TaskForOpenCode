package com.test.taskopencode.controller;

import com.test.taskopencode.model.Answer;
import com.test.taskopencode.model.Question;
import com.test.taskopencode.model.Questionnaire;
import com.test.taskopencode.repository.AnswerRepository;
import com.test.taskopencode.repository.QuestionRepository;
import com.test.taskopencode.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @GetMapping(value="/question/{id}/answer/add")
    public String addAnswer(@PathVariable(value="id") long id, Model model) {
        if (!questionRepository.existsById(id)) {
            return "redirect:/questionnaire/all";
        }

        Optional<Question> question = questionRepository.findById(id);
        ArrayList<Question> resQuestion = new ArrayList<>();
        question.ifPresent(resQuestion::add);

        model.addAttribute("question", resQuestion);

        long questionnaireId = question.get().getQuestionnaire().getId();

        if (!questionnaireRepository.existsById(questionnaireId)) {
            return "redirect:/questionnaire/all";
        }

        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(questionnaireId);
        ArrayList<Questionnaire> resQuestionnaire = new ArrayList<>();
        questionnaire.ifPresent(resQuestionnaire::add);

        model.addAttribute("questionnaire", resQuestionnaire);

        return "answer-add";
    }

    @PostMapping(value="/question/{id}/answer/add")
    public String postAddAnswer(@PathVariable(value="id") long id, @RequestParam String text, Model model) {
        Answer answer = new Answer(text);
        answer.setQuestion(questionRepository.findById(id).isPresent()? questionRepository.findById(id).get() : null);
        answerRepository.save(answer);

        return String.format("redirect:/question/%s/edit", id);
    }

    @GetMapping(value="/answer/{id}/edit")
    public String answerEdit(@PathVariable(value="id") long id, Model model) {
        if (!answerRepository.existsById(id)) {
            return "redirect:/questionnaire/all";
        }

        Optional<Answer> answer = answerRepository.findById(id);
        ArrayList<Answer> res = new ArrayList<>();
        answer.ifPresent(res::add);

        model.addAttribute("answer", res);

        long questionId = answer.get().getQuestion().getId();

        if (!questionRepository.existsById(questionId)) {
            return "redirect:/questionnaire/all";
        }

        Optional<Question> question = questionRepository.findById(questionId);
        ArrayList<Question> resQuestion = new ArrayList<>();
        question.ifPresent(resQuestion::add);

        model.addAttribute("question", resQuestion);

        long questionnaireId = question.get().getQuestionnaire().getId();

        if (!questionnaireRepository.existsById(questionnaireId)) {
            return "redirect:/questionnaire/all";
        }

        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(questionnaireId);
        ArrayList<Questionnaire> resQuestionnaire = new ArrayList<>();
        questionnaire.ifPresent(resQuestionnaire::add);

        model.addAttribute("questionnaire", resQuestionnaire);

        return "answer-edit";
    }

    @PostMapping(value="/answer/{id}/edit")
    public String postEditAnswer(@PathVariable(value="id") long id, @RequestParam String text, Model model) {
        Answer answer = answerRepository.findById(id).orElseThrow();
        answer.setText(text);

        answerRepository.save(answer);

        return String.format("redirect:/question/%s/edit", answer.getQuestion().getId());
    }

    @PostMapping(value="/answer/{id}/remove")
    public String postRemoveAnswer(@PathVariable(value="id") long id, Model model) {
        Answer answer = answerRepository.findById(id).orElseThrow();

        long questionId = answer.getQuestion().getId();

        answerRepository.delete(answer);

        return String.format("redirect:/question/%s/edit", questionId);
    }
}
