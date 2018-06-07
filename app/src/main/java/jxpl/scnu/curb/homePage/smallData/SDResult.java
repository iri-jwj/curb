package jxpl.scnu.curb.homePage.smallData;


public class SDResult {
    private int question_num;//题号
    private String question;
    private String option1;
    private String option2;
    private float option1ans;
    private float option2ans;

    public SDResult(int para_question_num, String para_question, String para_option1, String para_option2, float para_option1ans, float para_option2ans) {
        question_num = para_question_num;
        question = para_question;
        option1 = para_option1;
        option2 = para_option2;
        option1ans = para_option1ans;
        option2ans = para_option2ans;
    }

    public int getQuestion_num() {
        return question_num;
    }

    public void setQuestion_num(int para_question_num) {
        question_num = para_question_num;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String para_question) {
        question = para_question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String para_option1) {
        option1 = para_option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String para_option2) {
        option2 = para_option2;
    }

    public float getOption1ans() {
        return option1ans;
    }

    public void setOption1ans(int para_option1ans) {
        option1ans = para_option1ans;
    }

    public float getOption2ans() {
        return option2ans;
    }

    public void setOption2ans(int para_option2ans) {
        option2ans = para_option2ans;
    }

    @Override
    public String toString() {
        return "questionNum:" + question_num + "\tquestion:" + question +
                "\toption1:" + option1 + "\toption2" + option2 + "\toption1Ans:" + option1ans +
                "\toption2Ans:" + option2ans;
    }
}
