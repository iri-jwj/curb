package jxpl.scnu.curb.homePage.smallData;


import java.util.UUID;

public class SDDetail {
    private UUID sdId;
    private int questionNum; //题号
    private String question;     //题目
    private String option1;    //选项1
    private String option2;    //选项2

    public SDDetail(UUID sdId, int questionNum, String question, String option1, String option2) {
        this.sdId = sdId;
        this.questionNum = questionNum;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
    }

    public UUID getSdId() {
        return sdId;
    }

    public void setSdId(UUID sd_id) {
        this.sdId = sd_id;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int question_num) {
        this.questionNum = question_num;
    }

    public String getQuestion() {

        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String optionOne) {
        this.option1 = optionOne;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String optionTwo) {
        this.option2 = optionTwo;
    }
}
