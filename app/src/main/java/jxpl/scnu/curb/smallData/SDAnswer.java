package jxpl.scnu.curb.smallData;

import java.util.UUID;

public class SDAnswer {
    private UUID summaryId;
    private int questionNum;
    private int answer; //‘1’代表option1，‘2’代表option2

    public SDAnswer(UUID summaryId, int questionNum, int answer) {
        this.summaryId = summaryId;
        this.questionNum = questionNum;
        this.answer = answer;
    }

    public UUID getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(UUID smallDataId) {
        this.summaryId = smallDataId;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
