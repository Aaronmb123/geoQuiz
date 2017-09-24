package bakeaaro.com.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Math;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_ocean, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    //TODO
    private int[] mQuestionsBankDisabledButtons = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            //new int[mQuestionBank.length];
    //Arrays.fill(mQuestionsBankDisabledButtons, 0);

    private boolean[] mQuestionsBankCheated = {false, false, false, false, false,
                                               false, false, false, false, false};
    private int mCurrentIndex = 0;
    private int mNumQuestionsAnswered = 0;
    private int mNumCorrectAnswers = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean("cheater");
            mQuestionsBankCheated[mCurrentIndex] = mIsCheater;
        }

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_tv);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_b);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = mQuestionsBankCheated[mCurrentIndex];

                updateQuestion();
                if (mQuestionsBankDisabledButtons[mCurrentIndex] == 0) {
                    enableAnswerButtons();
                } else {
                    disableAnswerButtons();
                }
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_b);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                }
                mIsCheater = mQuestionsBankCheated[mCurrentIndex];
                updateQuestion();
                if (mQuestionsBankDisabledButtons[mCurrentIndex] == 0) {
                    enableAnswerButtons();
                } else {
                    disableAnswerButtons();
                }
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumQuestionsAnswered += 1;
                checkAnswer(true);
                mQuestionsBankDisabledButtons[mCurrentIndex] = 1;
                disableAnswerButtons();
                displayScore();

            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumQuestionsAnswered += 1;
                checkAnswer(false);
                mQuestionsBankDisabledButtons[mCurrentIndex] = 1;
                disableAnswerButtons();
                displayScore();
            }
        });

        updateQuestion();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(Bundle) called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume(Bundle) called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause(Bundle) called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean("cheater", mIsCheater);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mNumCorrectAnswers += 1;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void disableAnswerButtons() {
        mTrueButton.setClickable(false);
        mFalseButton.setClickable(false);
        //mTrueButton.setTextColor(0xD3D3D3);
        //mFalseButton.setTextColor(0xD3D3D3);
        //Log.d("mQuestionBankDisabledButtons ", + Arrays.toString(mQuestionsBankDisabledButtons));
        for(int num : mQuestionsBankDisabledButtons)
           Log.v("index: ", Integer.toString(num));
    }

    private void enableAnswerButtons() {
        mTrueButton.setClickable(true);
        mFalseButton.setClickable(true);
        //mTrueButton.setTextColor(0x000000);
        //mFalseButton.setTextColor(0x000000);
    }

    private void displayScore() {
        if (mNumQuestionsAnswered == mQuestionBank.length) {
            float score = (mNumQuestionsAnswered / mNumCorrectAnswers) * 100;
            Toast.makeText(QuizActivity.this, "Your Score is " + String.valueOf(score) + "%", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mQuestionsBankCheated[mCurrentIndex] = mIsCheater;

        }
    }

}
