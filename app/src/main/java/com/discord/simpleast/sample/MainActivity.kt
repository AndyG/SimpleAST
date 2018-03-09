package com.discord.simpleast.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.agarron.simpleast.R
import com.discord.simpleast.core.simple.SimpleRenderer

class MainActivity : AppCompatActivity() {

  private lateinit var resultText: TextView
  private lateinit var input: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    resultText = findViewById(R.id.result_text) as TextView
    input = findViewById(R.id.input) as EditText

    input.setText("*t*")

    findViewById(R.id.benchmark_btn).setOnClickListener {
      val times = 50.0
      var totalDuration = 0L
      var i = 0
      while (i < times) {
        val start = System.currentTimeMillis()
        testParse(50)
        val end = System.currentTimeMillis()
        val duration = end - start
        totalDuration += duration
        Log.d("timer", "duration of parse: $duration ms")
        i++
      }
      Log.d("timer", "average parse time: " + totalDuration / times + " ms")
    }

    findViewById(R.id.test_btn).setOnClickListener { SimpleRenderer.renderBasicMarkdown(input.text, resultText) }
  }

  private fun createTestText(): String {
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
        " (caused by ValueError(\"unknown url type: '/yts/jsbin/player-en_US-vflkk7pUE/base.js'\",))"
  }

  private fun testParse(times: Int) {
    val text = createTestText()

    for (i in 0 until times) {
      SimpleRenderer.renderBasicMarkdown(text, resultText)
    }
  }
}

