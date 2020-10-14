package com.HelloExperts.HelloExpert;

public class Question_Categories_data {
    private String question;
    private String skill;
    private String date;
    private String price;
    private int question_id;
    public Question_Categories_data(String question,String date, String price,String skill,int question_id){
        this.question=question;
        this.date=date;
        this.price=price;
        this.skill = skill;
        this.question_id = question_id;
    }
    public void setQuestion(String question){
        this.question = question;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkill() {
        return skill;
    }

    public void setDate(String date){
        this.date = date;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public void setQuestion_id(int question_id){
        this.question_id= question_id;
    }
    public String getQuestion(){
        return this.question;
    }
    public String getDate(){
        return this.date;
    }
    public String getPrice(){
        return this.price;
    }

    public int getQuestion_id() {
        return question_id;
    }
}
