package hu.drorszagkriszaxel.jokelibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowJoke extends AppCompatActivity {

    public static final String EXTRA_JOKE_TEXT = "extrajoketext";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_joke);

        if (getIntent() != null) {

            TextView textView = findViewById(R.id.tv_joke_text);
            textView.setText(getIntent().getStringExtra(EXTRA_JOKE_TEXT ));

        }

    }
}
