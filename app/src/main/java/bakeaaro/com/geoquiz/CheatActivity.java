package bakeaaro.com.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "bakeaaro.com.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "bakeaaro.com.geoquiz.answer_shown";
    private boolean mAnswerIsTrue;
    private boolean mAnswerShown;
    private TextView mAnswerTV;
    private Button mShowAnswerButton;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("answer", mAnswerIsTrue);
        savedInstanceState.putBoolean("shown", mAnswerShown);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerTV = (TextView) findViewById(R.id.answer_text_view);

        if (savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean("answer");
            mAnswerShown = savedInstanceState.getBoolean("shown");
            setAnswerShownResult(mAnswerShown);
            if (mAnswerIsTrue) {
                mAnswerTV.setText(R.string.true_button);
            } else {
                mAnswerTV.setText(R.string.false_button);
            }
        }


        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mShowAnswerButton = (Button)findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerIsTrue) {
                    mAnswerTV.setText(R.string.true_button);
                } else {
                    mAnswerTV.setText(R.string.false_button);
                }
                mAnswerShown = true;
                setAnswerShownResult(true);
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

}
