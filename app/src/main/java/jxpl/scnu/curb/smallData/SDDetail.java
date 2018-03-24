package jxpl.scnu.curb.smallData;


import java.util.UUID;

public class SDDetail {
    private UUID sd_id;
    private int question_num; //题号
    private String question;     //题目
    private String optionOne;    //选项1
    private String optionTwo;    //选项2

    public SDDetail(UUID sd_id, int question_num, String question, String optionOne, String optionTwo) {
        this.sd_id = sd_id;
        this.question_num = question_num;
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
    }

    public UUID getSd_id() {
        return sd_id;
    }

    public void setSd_id(UUID sd_id) {
        this.sd_id = sd_id;
    }

    public int getQuestion_num() {
        return question_num;
    }

    public void setQuestion_num(int question_num) {
        this.question_num = question_num;
    }

    public String getQuestion() {

        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public void setOptionOne(String optionOne) {
        this.optionOne = optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public void setOptionTwo(String optionTwo) {
        this.optionTwo = optionTwo;
    }
}
