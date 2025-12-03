package QuizModule;

public class Question {
    private final String question;
    private final String answer;
    private final String type;
    public static final String  TRUE_FALSE = "truefalse",
                                MCQ = "mcq";

    public Question(String question, String answer, String type) {
        this.question = question;
        this.answer = answer;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getType() {
        return type;
    }
}