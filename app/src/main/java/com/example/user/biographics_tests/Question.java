package com.example.user.biographics_tests;

/**
 * Created by elithe on 10/13/2015.
 */
public class Question {
    int answered;
    String question;

    public Question(String q)
    {
        answered = 0;
        question = q;
    }

    public Question(String q, int answer)
    {
        answered = answer;
        question = q;
    }

    public int getAnswered()
    {
        return answered;
    }

    public String getQuestion()
    {
        return question;
    }

    public boolean compareTo(String questionStr){
        return this.question.equals(questionStr);
    }
}
