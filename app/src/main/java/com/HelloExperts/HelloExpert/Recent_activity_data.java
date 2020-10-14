package com.HelloExperts.HelloExpert;




public class Recent_activity_data
{
    private int question_id;
    private String question;
    private String date;
    private String status;

    public Recent_activity_data(String question,String date, String status, int question_id){
        this.question=question;
        this.date=date;
        this.status=status;
        this.question_id=question_id;
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

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getQuestion_id() {
        return question_id;
    }
}
