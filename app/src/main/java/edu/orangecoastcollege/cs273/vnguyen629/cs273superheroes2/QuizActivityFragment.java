package edu.orangecoastcollege.cs273.vnguyen629.cs273superheroes2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * QuizActivityFragment contains the Quiz logic (correct/incorrect/statistics).
 * It also handles the delay of guessing correctly, so user can see "Correct!" message
 * before the next hero is displayed.
 */
public class QuizActivityFragment extends Fragment {

    private static final String TAG = "CS273 Heroes Activity";
    private static final int CORRECT_ANSWERS_IN_QUIZ = 10;

    private List<String> fileNameList;
    private List<String> quizHeroesList;
    private Set<String> quizTypeSet;
    private String correctAnswer;
    private int totalGuesses;
    private int correctAnswers;
    private int guessRows;
    private SecureRandom random;
    private Handler handler;

    private TextView questionNumberTextView;
    private ImageView heroImageView;
    private LinearLayout[] guessLinearLayout;
    private TextView answerTextView;

    /**
     * Configures the QuizActivityFragment when its View is created
     * @param inflater The layout inflater
     * @param container The view group contain in which the fragment resides
     * @param savedInstanceState Any saves state to restore in this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_quiz, container, false);

        fileNameList = new ArrayList<>();
        quizHeroesList = new ArrayList<>();
        random = new SecureRandom();
        handler = new Handler();

        questionNumberTextView =
                (TextView) view.findViewById(R.id.questionNumberTextView);

        heroImageView = (ImageView) view.findViewById(R.id.superheroImageView);

        guessLinearLayout = new LinearLayout[2];
        guessLinearLayout[0] =
                (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        guessLinearLayout[1] =
                (LinearLayout) view.findViewById(R.id.row2LinearLayout);

        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        for (LinearLayout row : guessLinearLayout) {
            for (int column = 0; column < row.getChildCount(); column++) {
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(guessButtonListener);
            }
        }

        questionNumberTextView.setText(
                getString(R.string.question, 1, CORRECT_ANSWERS_IN_QUIZ));

        return view;
    }

    /**
     * Parses the superhero file name and returns
     * the superhero name, replacing underscores with spaces.
     * @param name The hero name
     * @return The hero name, parsed from the file name
     */
    public String getHeroName(String name) {
        String heroName = name.substring(name.indexOf('-') + 1);
        return heroName.replace('_', ' ');
    }

    /**
     * Utility method that disables all answer Buttons
     */
    public void disableButtons() {
        for (int row = 0; row < guessRows; row++) {
            LinearLayout guessRow = guessLinearLayout[row];
            for (int i = 0; i < guessRow.getChildCount(); i++)
                guessRow.getChildAt(i).setEnabled(false);
        }
    }

    /**
     * updateGuessRows is called from QuizActivity when the app is launched,
     * displaying 4 buttons (2 rows).
     */
    public void updateGuessRows() {
        String choices = "4";
        guessRows = 2;
        // guessRows = Integer.parseInt(choices) / 2;

        for (LinearLayout layout : guessLinearLayout)
            layout.setVisibility(View.GONE);

        for (int row = 0; row < guessRows; row++)
            guessLinearLayout[row].setVisibility(View.VISIBLE);
    }

    /**
     * Updates the quiz type for the quiz based on values in sharedPreferences.
     * @param sharedPreferences The shared preferences defined in preferences.xml
     */
    public void updateType(SharedPreferences sharedPreferences) {
        quizTypeSet =
                sharedPreferences.getStringSet(QuizActivity.TYPE, null);
    }

    /**
     * Configure and start up a new quiz based on the settings.
     */
    public void resetQuiz() {
        AssetManager assets = getActivity().getAssets();
        fileNameList.clear();

        try {
            for (String hero : quizTypeSet) {
                String[] paths = assets.list(hero);

                for (String path : paths)
                    fileNameList.add(path.replace(".png", ""));
            }
        }
        catch (IOException exception) {
            Log.e(TAG, "Error loading image file names", exception);
        }

        correctAnswers = 0;
        totalGuesses = 0;
        quizHeroesList.clear();

        int heroCounter = 1;
        int numberOfHeroes = fileNameList.size();

        while (heroCounter <= CORRECT_ANSWERS_IN_QUIZ) {
            int randomIndex = random.nextInt(numberOfHeroes);

            String fileName = fileNameList.get(randomIndex);

            if (!quizHeroesList.contains(fileName)) {
                quizHeroesList.add(fileName);
                heroCounter++;
            }
        }

        loadNextHero();
    }

    /**
     * After user guesses a hero correctly, load the next hero.
     */
    private void loadNextHero() {
        String nextImage = quizHeroesList.remove(0);
        correctAnswer = nextImage;
        answerTextView.setText("");

        questionNumberTextView.setText(getString(
                R.string.question, (correctAnswers + 1), CORRECT_ANSWERS_IN_QUIZ));

        String hero = nextImage.substring(0, nextImage.indexOf('-'));

        AssetManager assets = getActivity().getAssets();

        try (InputStream stream =
                     assets.open(hero + "/" + nextImage + ".png")) {
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            heroImageView.setImageDrawable(flag);
        }
        catch (IOException exception) {
            Log.e(TAG, "Error loading " + nextImage, exception);
        }

        Collections.shuffle(fileNameList);

        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));

        for (int row = 0; row < guessRows; row++) {
            for (int column = 0;
                 column < guessLinearLayout[row].getChildCount();
                 column++) {
                Button newGuessButton =
                        (Button) guessLinearLayout[row].getChildAt(column);
                newGuessButton.setEnabled(true);

                String fileName = fileNameList.get((row * 2) + column);
                newGuessButton.setText(getHeroName(fileName));
            }
        }

        int row = random.nextInt(guessRows);
        int column = random.nextInt(2);
        LinearLayout randomRow = guessLinearLayout[row];
        String superheroName = getHeroName(correctAnswer);
        ((Button) randomRow.getChildAt(column)).setText(superheroName);
    }

    /**
     * Called when a guess button is clicked. This listener is used for all buttons
     * in the quiz.
     */
    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            String answer = getHeroName(correctAnswer);
            totalGuesses++;

            if (guess.equals(answer)) {
                correctAnswers++;

                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(
                        getResources().getColor(R.color.correct_answer,
                                getContext().getTheme()));

                disableButtons();

                if (correctAnswers == CORRECT_ANSWERS_IN_QUIZ) {
                    DialogFragment quizResults =
                            new DialogFragment() {
                                @Override
                                public Dialog onCreateDialog(Bundle bundle) {
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(getActivity());
                                    builder.setMessage(
                                            getString(R.string.results,
                                                    totalGuesses,
                                                    (1000 / (double) totalGuesses)));

                                    builder.setPositiveButton(R.string.reset_quiz,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int id) {
                                                    resetQuiz();
                                                }
                                            });

                                    return builder.create();
                                }
                            };

                    quizResults.setCancelable(false);
                    quizResults.show(getFragmentManager(), "quiz results");
                }
                else {
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    loadNextHero();
                                }
                            }, 2000);
                }
            }
            else {
                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, getContext().getTheme()));
                guessButton.setEnabled(false);
            }
        }
    };
}