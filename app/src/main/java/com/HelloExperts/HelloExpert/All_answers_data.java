package com.HelloExperts.HelloExpert;




public class All_answers_data
{
    private String question;
    private String date;
    private String status;
    private String price;
    private int question_id;

    public All_answers_data(String question, String date, String status,String price,int question_id){
        this.question=question;
        this.date=date;
        this.status=status;
        this.price = price;
        this.question_id = question_id;
    }
    public void setQuestion(String question){
        this.question = question;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getQuestion(){
        return this.question;
    }
    public String getDate(){
        return this.date;
    }
    public String getStatus(){
        return this.status;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
    public int getQuestion_id(){
        return this.question_id;
    }
    public void setQuestion_id(int question_id){
        this.question_id = question_id;
    }
}
