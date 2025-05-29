package models;

public class SecurityQuestion {
    private int id;
    private int uId;
    private String answer;

    public SecurityQuestion() {}

    public SecurityQuestion(int id, int uId, String answer) {
        this.id = id;
        this.uId = uId;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}