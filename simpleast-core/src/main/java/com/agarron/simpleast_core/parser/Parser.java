package com.agarron.simpleast_core.parser;

import android.support.annotation.Nullable;
import android.util.Log;

import com.agarron.simpleast_core.node.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

public class Parser<T extends Node> {

  private static final String TAG = "Parser";

  private boolean enableDebugging = false;

  public Parser() {
    this(false);
  }

  public Parser(final boolean enableDebugging) {
    this.enableDebugging = enableDebugging;
  }

  private final List<Rule<T>> rules = new ArrayList<>();

  public Parser<T> addRule(final Rule<T> rule) {
    rules.add(rule);
    return this;
  }

  public Parser<T> addRules(final Collection<Rule<T>> rules) {
    for (final Rule<T> rule : rules) {
      addRule(rule);
    }
    return this;
  }

  public List<T> parse(final @Nullable CharSequence source) {
    return parse(source, false);
  }

  public List<T> parse(final @Nullable CharSequence source, boolean isNested) {
    final Stack<SubtreeSpec<T>> stack = new Stack<>();
    final List<T> nodes = new ArrayList<>();

    if (!isTextEmpty(source)) {
      stack.add(new SubtreeSpec<T>(null, 0, source.length()));
    }

    while (!stack.isEmpty()) {
      final SubtreeSpec<T> builder = stack.pop();

      if (builder.startIndex >= builder.endIndex) {
        break;
      }

      final CharSequence inspectionSource = source.subSequence(builder.startIndex, builder.endIndex);
      final int offset = builder.startIndex;

      boolean foundRule = false;
      for (final Rule<T> rule : rules) {
        if (isNested && !rule.getApplyOnNestedParse()) {
          continue;
        }

        final Matcher matcher = rule.getMatcher().reset(inspectionSource);

        if (matcher.find()) {
          logMatch(rule, inspectionSource);
          final int matcherSourceEnd = matcher.end() + offset;
          foundRule = true;

          final SubtreeSpec<T> newBuilder = rule.parse(matcher, this, isNested);
          final T parent = builder.root;

          if (parent != null) {
            parent.addChild(newBuilder.root);
          } else {
            // If the parent is null, add the node to the top level node list.
            nodes.add(newBuilder.root);
          }

          // In case the last match didn't consume the rest of the source for this subtree,
          // make sure the rest of the source is consumed.
          if (matcherSourceEnd != builder.endIndex) {
            stack.push(SubtreeSpec.createNonterminal(parent, matcherSourceEnd, builder.endIndex));
          }

          // We want to speak in terms of indices within the source string,
          // but the Rules only see the matchers in the context of the substring
          // being examined. Adding this offset address that issue.
          if (!newBuilder.isTerminal) {
            newBuilder.applyOffset(offset);
            stack.push(newBuilder);
          }

          System.out.println("source: " + inspectionSource + " -- depth: " + stack.size());

          break;
        } else {
          logMiss(rule, inspectionSource);
        }
      }

      if (!foundRule) {
        throw new RuntimeException("failed to find rule to match source: \"" + inspectionSource + "\"");
      }
    }

    return nodes;
  }

  private void logMatch(final Rule rule, final CharSequence source) {
    if (enableDebugging) {
      Log.i(TAG, "MATCH: with rule with pattern: " + rule.getMatcher().pattern().toString() + " to source: " + source);
    }
  }

  private void logMiss(final Rule rule, final CharSequence source) {
    if (enableDebugging) {
      Log.i(TAG, "MISS: with rule with pattern: " + rule.getMatcher().pattern().toString() + " to source: " + source);
    }
  }

  private static boolean isTextEmpty(final CharSequence text) {
    return text == null || text.length() == 0;
  }
}
