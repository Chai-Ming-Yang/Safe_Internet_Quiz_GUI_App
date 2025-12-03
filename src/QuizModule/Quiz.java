package QuizModule;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileReader;

import javax.swing.JOptionPane;
import java.io.IOException;

import com.google.gson.Gson;

public class Quiz implements InterfaceQuiz {
    public static final String  EASY = "easy",
                                NORMAL = "normal",
                                HARD = "hard";
    private String currentDifficulty;
    private List<Question>  questions_easy,
                            questions_normal,
                            questions_hard,
                            currentQuestions;
    private int currentQuestionIndex,
                hearts,
                numCorrect;
    public final int NUM_QUESTIONS = 20;

    public int getHearts() {
        return hearts;
    }

    private List<Question> getQuestions(String difficulty) {
        currentDifficulty = difficulty;
        List<Question> questions = new ArrayList<>();
        if (difficulty == EASY)
            questions.addAll(questions_easy);
        else if (difficulty == NORMAL)
            questions.addAll(questions_normal);
        else if (difficulty == HARD)
            questions.addAll(questions_hard);
        return getRandomSubset(questions, NUM_QUESTIONS);
    }

    private List<Question> getRandomSubset(List<Question> questions, int subsetSize) {
        List<Question> questionsCopy = new ArrayList<>(questions);
        Collections.shuffle(questionsCopy);
        return questionsCopy.subList(0, Math.min(subsetSize, questionsCopy.size()));
    }

    public void startGame(String difficulty) {
        hearts = 3;
        currentQuestions = getQuestions(difficulty);
        currentQuestionIndex = 0;
    }

    public String getCurrentDifficulty() {
        return currentDifficulty;
    }
    
    public Question getCurrentQuestion() {
        if (currentQuestions != null && currentQuestionIndex < currentQuestions.size()) {
            return currentQuestions.get(currentQuestionIndex);
        }
        return null;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public void answerQuestion(boolean correct) {
        if (!correct) 
            hearts--;
        else 
            ++numCorrect;
        currentQuestionIndex++;
    }

    public boolean hasMoreQuestions() {
        return currentQuestions != null && currentQuestionIndex < currentQuestions.size();
    }

    public boolean isGameOver() {
        return hearts <= 0;
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        Quiz quiz = new Quiz();
        try {
            FileReader reader = new FileReader(System.getProperty("user.dir") + File.separator + "resources\\quiz.json");

            quiz = gson.fromJson(reader, Quiz.class);
            System.out.println(reader);
            if (quiz == null) {
                System.out.println("Pages are null after deserialization");
            } else {
                List<Question> questions = quiz.getQuestions(Quiz.EASY);
                for (Question q: questions) {
                    System.err.println(q.getType());
                    System.err.println(q.getQuestion());
                    System.err.println(q.getAnswer() + "\n");
                }
            }

            reader.close();
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Parsing learning_module.json. Error: "+e);
            return;
        }
    }
}
