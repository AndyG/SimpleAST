package com.agarron.simpleastcore;

import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;

import com.agarron.simpleast_core.builder.Parser;
import com.agarron.simpleast_core.node.Node;
import com.agarron.simpleast_core.node.StyleNode;
import com.agarron.simpleast_core.node.TextNode;
import com.agarron.simpleast_core.simple.SimpleMarkdownRules;
import com.agarron.simpleast_core.utils.TreeMatcher;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest {

    private Parser parser;
    private TreeMatcher treeMatcher;

    @Before
    public void setup() {
        parser = new Parser();
        parser.addRules(SimpleMarkdownRules.getSimpleMarkdownRules());
        treeMatcher = new TreeMatcher();
        treeMatcher.registerDefaultMatchers();
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testEmptyParse() throws Exception {
        final List<Node> ast = parser.parse("");
        Assert.assertTrue(ast.isEmpty());
    }

    @Test
    public void testParseFormattedText() throws Exception {
        final List<Node> ast = parser.parse("**bold**");

        final StyleNode boldNode = StyleNode.createWithText("bold", Collections.singletonList((CharacterStyle) new StyleSpan(Typeface.BOLD)));

        final List<? extends Node> model = Collections.singletonList(boldNode);
        Assert.assertTrue(treeMatcher.matches(model, ast));
    }

    @Test
    public void testParseLeadingFormatting() throws Exception {
        final List<Node> ast = parser.parse("**bold** and not bold");

        final StyleNode boldNode = StyleNode.createWithText("bold", Collections.singletonList((CharacterStyle) new StyleSpan(Typeface.BOLD)));
        final TextNode trailingText = new TextNode(" and not bold");

        final List<? extends Node> model = Arrays.asList(boldNode, trailingText);
        Assert.assertTrue(treeMatcher.matches(model, ast));
    }

    @Test
    public void testParseTrailingFormatting() throws Exception {
        final List<Node> ast = parser.parse("not bold **and bold**");

        final TextNode leadingText = new TextNode("not bold ");
        final StyleNode boldNode = StyleNode.createWithText("and bold", Collections.singletonList((CharacterStyle) new StyleSpan(Typeface.BOLD)));

        final List<? extends Node> model = Arrays.asList(leadingText, boldNode);
        Assert.assertTrue(treeMatcher.matches(model, ast));
    }

    @Test
    public void testNestedFormatting() throws Exception {
        final List<Node> ast = parser.parse("**bold *and italics* and more bold**");

        final StyleNode boldNode = new StyleNode(Collections.singletonList((CharacterStyle) new StyleSpan(Typeface.BOLD)));
        boldNode.addChild(new TextNode("bold "));
        boldNode.addChild(StyleNode.createWithText("and italics",
            Collections.singletonList((CharacterStyle) new StyleSpan(Typeface.ITALIC))));
        boldNode.addChild(new TextNode(" and more bold"));

        final List<? extends Node> model = Collections.singletonList(boldNode);
        Assert.assertTrue(treeMatcher.matches(model, ast));
    }
}
