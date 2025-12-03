package QuizModule;

public interface InterfaceQuiz {
    int getHearts();
    void startGame(String difficulty);
    String getCurrentDifficulty();
    Question getCurrentQuestion();
    int getCurrentQuestionIndex();
    int getNumCorrect();
    void answerQuestion(boolean correct);
    boolean hasMoreQuestions();
    boolean isGameOver();
}