package com.test.taskopencode.controller;

import com.test.taskopencode.model.*;
import com.test.taskopencode.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class QuestionnaireController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UsersAnswersRepository usersAnswersRepository;

    @GetMapping(value="/questionnaire/all")
    public String allQuestionnaires(Model model) {

        Iterable<Questionnaire> questionnaires = questionnaireRepository.findAll();
        model.addAttribute("questionnaires", questionnaires);

        return "questionnaire-all";
    }

    @GetMapping(value="/questionnaire/add")
    public String addQuestionnaire(Model model) {
        return "questionnaire-add";
    }

    @PostMapping(value="/questionnaire/add")
    public String postAddQuestionnaire(@RequestParam String name, @RequestParam String description, Model model) {
        Questionnaire questionnaire = new Questionnaire(name, description);
        questionnaireRepository.save(questionnaire);

        return "redirect:/questionnaire/all";
    }

    @GetMapping(value="/questionnaire/{id}/go")
    public String questionnaireGo(@PathVariable(value="id") long id, @RequestParam String cntQVal, Model model) {
        if (!questionnaireRepository.existsById(id)) {
            return "redirect:/questionnaire/all";
        }

        Questionnaire questionnaire = getQuestionnaireById(id).get(0);
        int countQuestions = questionRepository.countQuestionsByQuestionnaire(questionnaire);

        if (countQuestions == 0){
            return "redirect:/questionnaire/all";
        }

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userName).orElseThrow();

        int cntQuestionnaireUser = usersAnswersRepository.countByQuestionnaireAndUser(questionnaire, user);

        model.addAttribute("cntQU", cntQuestionnaireUser);
        model.addAttribute("cntQVal", cntQVal);
        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("question", questionnaire.getQuestions().get(Integer.parseInt(cntQVal)));

        return "/questionnaire-go";
    }

    @Transactional
    @PostMapping("/questionnaire/{id}/go")
    public @ResponseBody void saveAnswer(@PathVariable(value="id") long id, String answersIdStr) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userName = auth.getName();

        User user = userRepository.findByEmail(userName).orElseThrow();

        Questionnaire questionnaire = questionnaireRepository.findById(id).orElseThrow();

        System.out.println(auth.getName());

        String[] answerIdArr = answersIdStr.replace("\"", "").split(",");

        for (int i = 0; i < answerIdArr.length; i++) {
            if (!answerIdArr[i].equals("0")) {
                long answerId = Long.parseLong(answerIdArr[i]);

                Answer answer = answerRepository.findById(answerId).orElseThrow();

                UsersAnswers usersAnswers = new UsersAnswers();
                usersAnswers.setChkAnswer(true);
                usersAnswers.setQuestionnaire(questionnaire);
                usersAnswers.setAnswer(answer);
                usersAnswers.setUser(user);
                usersAnswersRepository.save(usersAnswers);
            }
        }
    }

    @Transactional
    @GetMapping(value="/deleteUserQuestionnaire/{id}")
    public @ResponseBody void delUserAnswersByQuestionnaire(@PathVariable(value="id") long id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userName).orElseThrow();

        Questionnaire questionnaire = questionnaireRepository.findById(id).orElseThrow();

        usersAnswersRepository.deleteByQuestionnaireAndUser(questionnaire, user);
    }

    @GetMapping("/answer/next")
//    @ResponseBody
    public ModelAndView nextAnswer(int cntQ, long questionnaireId, Model model) {
        Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId).orElseThrow();

        int countQuestions = questionnaire.getQuestions().size();

        cntQ++;

        if (cntQ == countQuestions) {
            cntQ = -1;
            model.addAttribute("cntQVal", cntQ);
            return new ModelAndView("answer-end :: answersEndFragment");
        }

        Question question = questionnaire.getQuestions().get(cntQ);

        model.addAttribute("question1", question);
        model.addAttribute("cntQVal", cntQ);

        return new ModelAndView("answer-next :: answersFragment");
    }

    @GetMapping(value="/questionnaire/{id}/edit")
    public String questionnaireEdit(@PathVariable(value="id") long id, Model model) {
        if (!questionnaireRepository.existsById(id)) {
            return "redirect:/questionnaire/all";
        }

        model.addAttribute("questionnaire", getQuestionnaireById(id));

        return "questionnaire-edit";
    }

    @PostMapping(value="/questionnaire/{id}/edit")
    public String postEditQuestionnaire(@PathVariable(value="id") long id, @RequestParam String name, @RequestParam String description, Model model) {
        Questionnaire questionnaire = questionnaireRepository.findById(id).orElseThrow();
        questionnaire.setName(name);
        questionnaire.setDescription(description);

        questionnaireRepository.save(questionnaire);

        return "redirect:/questionnaire/all";
    }

    @PostMapping(value="/questionnaire/{id}/remove")
    public String postRemoveQuestion(@PathVariable(value="id") long id, Model model) {
        Questionnaire questionnaire = questionnaireRepository.findById(id).orElseThrow();

        questionnaireRepository.delete(questionnaire);

        return "redirect:/questionnaire/all";
    }

    private List<Questionnaire> getQuestionnaireById(long id) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
        ArrayList<Questionnaire> res = new ArrayList<>();
        questionnaire.ifPresent(res::add);

        return res;
    }
}
