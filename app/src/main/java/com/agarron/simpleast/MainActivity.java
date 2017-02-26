package com.agarron.simpleast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.agarron.simpleast_core.simple.SimpleRenderer;

import java.io.InputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_UNDERSCORES = 4000;

    private TextView resultTextView;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView) findViewById(R.id.result_text);
        input = (EditText) findViewById(R.id.input);

        input.setText("**bold _and italics_ and more bold**");

        findViewById(R.id.crash_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String testText = loadTestText();
                SimpleRenderer.renderBasicMarkdown(resultTextView, testText);
            }
        });

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleRenderer.renderBasicMarkdown(resultTextView, input.getText());
            }
        });
    }

    private String createTestText() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < NUM_UNDERSCORES; i++) {
            builder.append('_');
        }

        builder.append('h');

        for (int i = 0; i < NUM_UNDERSCORES; i++) {
            builder.append('_');
        }

        return builder.toString();
    }

    private String loadTestText() {
        final InputStream inputStream = getResources().openRawResource(R.raw.test_text);
        return convertStreamToString(inputStream);
    }

    static String convertStreamToString(final InputStream inputStream) {
        final Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
