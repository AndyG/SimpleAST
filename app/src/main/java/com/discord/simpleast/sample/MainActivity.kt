package com.discord.simpleast.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.agarron.simpleast.R
import com.discord.simpleast.core.parser.ParseSpec
import com.discord.simpleast.core.parser.Parser
import com.discord.simpleast.core.parser.Rule
import com.discord.simpleast.core.renderer.SpannableRenderableNode
import com.discord.simpleast.core.renderer.SpannableRenderer
import com.discord.simpleast.core.simple.SimpleMarkdownRules
import com.discord.simpleast.core.simple.SimpleRenderer
import java.util.regex.Matcher
import java.util.regex.Pattern

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

    findViewById(R.id.test_btn).setOnClickListener {
      testParseWithContext(input.text, resultText)
    }
  }

  private fun testParseWithContext(source: CharSequence?, resultText: TextView) {
    val parser = Parser<SpannableRenderableNode<TestRenderContext>>()

    parser.addRule(object : Rule<SpannableRenderableNode<TestRenderContext>>(Pattern.compile("^<replacement>")) {
      override fun parse(matcher: Matcher, parser: Parser<SpannableRenderableNode<TestRenderContext>>, isNested: Boolean): ParseSpec<out SpannableRenderableNode<TestRenderContext>> {
        return ParseSpec.createTerminal(TextNodeWithContext())
      }
    })

    parser.addRules(SimpleMarkdownRules.createSimpleMarkdownRules())

    val ast = parser.parse(source)
    resultText.text = SpannableRenderer.render(SpannableStringBuilder(), ast, TestRenderContext("poooooooop"))
  }

  data class TestRenderContext(val string: String)

  private fun createTestText() = """[0;31mERROR:[0m Signature extraction failed: Traceback (most recent call last):
  File "/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/youtube.py", line 1011, in _decrypt_signature
    video_id, player_url, s
  File "/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/youtube.py", line 925, in _extract_signature_function
    errnote='Download of %s failed' % player_url)
  File "/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/common.py", line 519, in _download_webpage
    res = self._download_webpage_handle(url_or_request, video_id, note, errnote, fatal, encoding=encoding, data=data, headers=headers, query=query)
  File "/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/common.py", line 426, in _download_webpage_handle
    urlh = self._request_webpage(url_or_request, video_id, note, errnote, fatal, data=data, headers=headers, query=query)
  File "/usr/local/lib/python3.5/dist-packages/youtube_dl/extractor/common.py", line 406, in _request_webpage
    return self._downloader.urlopen(url_or_request)
  File "/usr/local/lib/python3.5/dist-packages/youtube_dl/YoutubeDL.py", line 2000, in urlopen
    req = sanitized_Request(req)
  File "/usr/local/lib/python3.5/dist-packages/youtube_dl/utils.py", line 518, in sanitized_Request
    return compat_urllib_request.Request(sanitize_url(url), *args, **kwargs)
  File "/usr/lib/python3.5/urllib/request.py", line 269, in init
    self.full_url = url
  File "/usr/lib/python3.5/urllib/request.py", line 295, in full_url
    self._parse()
  File "/usr/lib/python3.5/urllib/request.py", line 324, in _parse
    raise ValueError("unknown url type: %r" % self.full_url)
ValueError: unknown url type: '/yts/jsbin/player-en_US-vflkk7pUE/base.js'
 (caused by ValueError("unknown url type: '/yts/jsbin/player-en_US-vflkk7pUE/base.js'",))"""

  private fun testParse(times: Int) {
    val text = createTestText()

    for (i in 0 until times) {
      SimpleRenderer.renderBasicMarkdown(text, resultText)
    }
  }

  private class TextNodeWithContext : SpannableRenderableNode<TestRenderContext>() {
    override fun render(builder: SpannableStringBuilder, renderContext: TestRenderContext) {
      builder.append(renderContext?.string)
    }
  }
}

