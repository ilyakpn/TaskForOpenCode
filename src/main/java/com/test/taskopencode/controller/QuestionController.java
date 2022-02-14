package com.test.taskopencode.controller;

import com.test.taskopencode.model.Answer;
import com.test.taskopencode.model.Question;
import com.test.taskopencode.model.Questionnaire;
import com.test.taskopencode.repository.QuestionRepository;
import com.test.taskopencode.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @GetMapping(value="/questionnaire/{id}/question/add")
    public String addQuestion(@PathVariable(value="id") long id, Model model) {
        if (!questionnaireRepository.existsById(id)) {
            return "redirect:/questionnaire-all";
        }

        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
        ArrayList<Questionnaire> resQuestionnaire = new ArrayList<>();
        questionnaire.ifPresent(resQuestionnaire::add);

        model.addAttribute("questionnaire", resQuestionnaire);

        return "question-add";
    }

    @PostMapping(value="/questionnaire/{id}/question/add")
    public String postAddQuestion(@PathVariable(value="id") long id, @RequestParam String text, @RequestParam String isOneAnswer, Model model) {
        Question question = new Question(text, isOneAnswer.equals("true"));
        question.setQuestionnaire(questionnaireRepository.findById(id).isPresent()? questionnaireRepository.findById(id).get() : null);
        questionRepository.save(question);

        return String.format("redirect:/questionnaire/%s/edit", id);
    }

    @GetMapping(value="/question/{id}/edit")
    public String questionEdit(@PathVariable(value="id") long id, Model model) {
        if (!questionRepository.existsById(id)) {
            return "redirect:/questionnaire-all";
        }

        Optional<Question> question = questionRepository.findById(id);
        ArrayList<Question> res = new ArrayList<>();
        question.ifPresent(res::add);

        model.addAttribute("question", res);

        long questionnaireId = question.get().getQuestionnaire().getId();

        if (!questionnaireRepository.existsById(questionnaireId)) {
            return "redirect:/questionnaire-all";
        }

        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(questionnaireId);
        ArrayList<Questionnaire> resQuestionnaire = new ArrayList<>();
        questionnaire.ifPresent(resQuestionnaire::add);

        model.addAttribute("questionnaire", resQuestionnaire);

        return "question-edit";
    }

    @PostMapping(value="/question/{id}/edit")
    public String postEditQuestion(@PathVariable(value="id") long id, @RequestParam String text, @RequestParam String isOneAnswer, Model model) {
        Question question = questionRepository.findById(id).orElseThrow();
        question.setText(text);
        question.setOneAnswer(isOneAnswer.equals("true"));

        questionRepository.save(question);

        return String.format("redirect:/questionnaire/%s/edit", question.getQuestionnaire().getId());
    }

    @PostMapping(value="/question/{id}/remove")
    public String postRemoveQuestion(@PathVariable(value="id") long id, Model model) {
        Question question = questionRepository.findById(id).orElseThrow();

        long questionnaireId = question.getQuestionnaire().getId();

        questionRepository.delete(question);

        return String.format("redirect:/questionnaire/%s/edit", questionnaireId);
    }

}
