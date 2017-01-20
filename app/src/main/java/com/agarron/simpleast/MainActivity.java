package com.agarron.simpleast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.agarron.simpleast_core.builder.Parser;
import com.agarron.simpleast_core.node.Node;
import com.agarron.simpleast_core.simple.SimpleMarkdownRules;
import com.agarron.simpleast_core.simple.SimpleRenderer;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_UNDERSCORES = 3;

    private TextView resultText;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView) findViewById(R.id.result_text);
        input = (EditText) findViewById(R.id.input);

        input.setText("**bold _and italics_ and more bold**");

        findViewById(R.id.crash_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Node> ast = new Parser().addRules(SimpleMarkdownRules.getSimpleMarkdownRules()).parse(createTestText());
                resultText.setText(SimpleRenderer.render(ast));
            }
        });

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Node> ast = new Parser().addRules(SimpleMarkdownRules.getSimpleMarkdownRules()).parse(input.getText());
                resultText.setText(SimpleRenderer.render(ast));
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
