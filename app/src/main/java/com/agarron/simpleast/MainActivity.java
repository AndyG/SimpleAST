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

    private TextView resultText;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView) findViewById(R.id.result_text);
        input = (EditText) findViewById(R.id.input);

        input.setText("*t*");

        findViewById(R.id.crash_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Node> ast = new Parser<>()
                    .addRules(SimpleMarkdownRules.getSimpleMarkdownRules())
                    .parse(createTestText());

                resultText.setText(SimpleRenderer.render(ast));
            }
        });

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleRenderer.renderBasicMarkdown(resultText, input.getText());
            }
        });
    }

    private String createTestText() {
        return "\u001B[0;31mERROR:\u001B[0m Signature extraction failed: Traceback (most recent call last):\n" +
            "  File \"/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/youtube.py\", line 1011, in _decrypt_signature\n" +
            "    video_id, player_url, s\n" +
            "  File \"/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/youtube.py\", line 925, in _extract_signature_function\n" +
            "    errnote='Download of %s failed' % player_url)\n" +
            "  File \"/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/common.py\", line 519, in _download_webpage\n" +
            "    res = self._download_webpage_handle(url_or_request, video_id, note, errnote, fatal, encoding=encoding, data=data, headers=headers, query=query)\n" +
            "  File \"/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/common.py\", line 426, in _download_webpage_handle\n" +
            "    urlh = self._request_webpage(url_or_request, video_id, note, errnote, fatal, data=data, headers=headers, query=query)\n" +
            "  File \"/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/common.py\", line 406, in _request_webpage\n" +
            "    return self._downloader.urlopen(url_or_request)\n" +
            "  File \"/usr/local/lib/python3.5/dist-packages/youtube_dl/YoutubeDL.py\", line 2000, in urlopen\n" +
            "    req = sanitized_Request(req)\n" +
            "  File \"/usr/local/lib/python3.5/dist-packages/youtube_dl/utils.py\", line 518, in sanitized_Request\n" +
            "    return compat_urllib_request.Request(sanitize_url(url), *args, **kwargs)\n" +
            "  File \"/usr/lib/python3.5/urllib/request.py\", line 269, in init\n" +
            "    self.full_url = url\n" +
            "  File \"/usr/lib/python3.5/urllib/request.py\", line 295, in full_url\n" +
            "    self._parse()\n" +
            "  File \"/usr/lib/python3.5/urllib/request.py\", line 324, in _parse\n" +
            "    raise ValueError(\"unknown url type: %r\" % self.full_url)\n" +
            "ValueError: unknown url type: '/yts/jsbin/player-en_US-vflkk7pUE/base.js'\n" +
            " (caused by ValueError(\"unknown url type: '/yts/jsbin/player-en_US-vflkk7pUE/base.js'\",))";
    }
}
