package com.test.taskopencode.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users_answers")

// Ответы пользователей
public class UsersAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "chkAnswer")
    private boolean chkAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Questionnaire questionnaire;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Answer answer;


}
