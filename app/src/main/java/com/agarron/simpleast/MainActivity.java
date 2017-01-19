package com.agarron.simpleast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.agarron.simpleast_core.builder.Parser;
import com.agarron.simpleast_core.simple.SimpleMarkdownRules;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_UNDERSCORES = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.crash_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Parser().addRules(SimpleMarkdownRules.getSimpleMarkdownRules()).safeParse(createTestText());
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
}
