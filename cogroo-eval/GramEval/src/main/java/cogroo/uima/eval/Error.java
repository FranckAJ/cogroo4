/**
 * Copyright (C) 2012 cogroo <cogroo@cogroo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cogroo.uima.eval;

/**
 * Class for storing start and end integer offsets.
 **/
public class Error implements Comparable<Error> {

  private final int start;
  private final int end;

  private final String type;
  private String ruleId;
  private final String suggestion;

  /**
   * Initializes a new Span Object.
   * 
   * @param s
   *          start of span.
   * @param e
   *          end of span.
   * @param type
   *          the type of the span
   */
  public Error(int s, int e, String type, String suggestion) {

    if (s < 0 || e < 0)
      throw new IllegalArgumentException(
          "start and end index must be zero or greater!");

    if (s > e)
      throw new IllegalArgumentException(
          "start index must not be larger than end index!");

    start = s;
    end = e;
    this.type = type;
    if(suggestion == null) {
      this.suggestion = "";
    } else {
      this.suggestion = suggestion;
    }
  }
  
  public Error(int s, int e, String type) {
    this(s, e, type, "");
  }

  /**
   * Initializes a new Span Object.
   * 
   * @param s
   *          start of span.
   * @param e
   *          end of span.
   */
  public Error(int s, int e) {
    this(s, e, null, "");
  }

  /**
   * Initializes a new Span object with an existing Span which is shifted by an
   * offset.
   * 
   * @param span
   * @param offset
   */
  public Error(Error span, int offset) {
    this(span.start + offset, span.end + offset, span.getType(), "");
  }

  public String getSuggestion() {
    return suggestion;
  }
  
  /**
   * Return the start of a span.
   * 
   * @return the start of a span.
   **/
  public int getStart() {
    return start;
  }

  /**
   * Return the end of a span.
   * 
   * @return the end of a span.
   **/
  public int getEnd() {
    return end;
  }

  /**
   * Retrieves the type of the span.
   * 
   * @return the type or null if not set
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the length of this span.
   * 
   * @return the length of the span.
   */
  public int length() {
    return end - start;
  }

  /**
   * Returns true if the specified span is contained by this span. Identical
   * spans are considered to contain each other.
   * 
   * @param s
   *          The span to compare with this span.
   * 
   * @return true is the specified span is contained by this span; false
   *         otherwise.
   */
  public boolean contains(Error s) {
    return start <= s.getStart() && s.getEnd() <= end;
  }

  public boolean contains(int index) {
    return start <= index && index <= end;
  }

  /**
   * Returns true if the specified span is the begin of this span and the
   * specified span is contained in this span.
   * 
   * @param s
   *          The span to compare with this span.
   * 
   * @return true if the specified span starts with this span and is contained
   *         in this span; false otherwise
   */
  public boolean startsWith(Error s) {
    return getStart() == s.getStart() && contains(s);
  }

  /**
   * Returns true if the specified span intersects with this span.
   * 
   * @param s
   *          The span to compare with this span.
   * 
   * @return true is the spans overlap; false otherwise.
   */
  public boolean intersects(Error s) {
    int sstart = s.getStart();
    // either s's start is in this or this' start is in s
    return this.contains(s) || s.contains(this) || getStart() <= sstart
        && sstart < getEnd() || sstart <= getStart() && getStart() < s.getEnd();
  }

  /**
   * Returns true is the specified span crosses this span.
   * 
   * @param s
   *          The span to compare with this span.
   * 
   * @return true is the specified span overlaps this span and contains a
   *         non-overlapping section; false otherwise.
   */
  public boolean crosses(Error s) {
    int sstart = s.getStart();
    // either s's start is in this or this' start is in s
    return !this.contains(s)
        && !s.contains(this)
        && (getStart() <= sstart && sstart < getEnd() || sstart <= getStart()
            && getStart() < s.getEnd());
  }

  /**
   * Retrieves the string covered by the current span of the specified text.
   * 
   * @param text
   * 
   * @return the substring covered by the current span
   */
  public CharSequence getCoveredText(CharSequence text) {
    if (getEnd() > text.length()) {
      throw new IllegalArgumentException("The span " + toString()
          + " is outside the given text!");
    }

    return text.subSequence(getStart(), getEnd());
  }

  /**
   * Compares the specified span to the current span.
   */
  public int compareTo(Error s) {
    if (getStart() < s.getStart()) {
      return -1;
    } else if (getStart() == s.getStart()) {
      if (getEnd() > s.getEnd()) {
        return -1;
      } else if (getEnd() < s.getEnd()) {
        return 1;
      } else {
        return 0;
      }
    } else {
      return 1;
    }
  }

  /**
   * Generates a hash code of the current span.
   */
  public int hashCode() {
    int res = 23;
    res = res * 37 + getStart();
    res = res * 37 + getEnd();
    if (getType() == null) {
      res = res * 37;
    } else {
      res = res * 37 + getType().hashCode();
    }

    return res;
  }

  /**
   * Checks if the specified span is equal to the current span.
   */
  public boolean equals(Object o) {

    boolean result;

    if (o == this) {
      result = true;
    } else if (o instanceof Error) {
      Error s = (Error) o;

      result = (getStart() == s.getStart()) && (getEnd() == s.getEnd())
          && (getType() != null ? type.equals(s.getType()) : true)
          && (s.getType() != null ? s.getType().equals(getType()) : true);
    } else {
      result = false;
    }

    return result;
  }

  /**
   * Generates a human readable string.
   */
  public String toString() {
    StringBuffer toStringBuffer = new StringBuffer(15);
    toStringBuffer.append(getStart());
    toStringBuffer.append("..");
    toStringBuffer.append(getEnd());

    return toStringBuffer.toString();
  }

  /**
   * Converts an array of {@link Error}s to an array of {@link String}s.
   * 
   * @param spans
   * @param s
   * @return the strings
   */
  public static String[] spansToStrings(Error[] spans, CharSequence s) {
    String[] tokens = new String[spans.length];

    for (int si = 0, sl = spans.length; si < sl; si++) {
      tokens[si] = spans[si].getCoveredText(s).toString();
    }

    return tokens;
  }

  public static String[] spansToStrings(Error[] spans, String[] tokens) {
    String[] chunks = new String[spans.length];
    StringBuffer cb = new StringBuffer();
    for (int si = 0, sl = spans.length; si < sl; si++) {
      cb.setLength(0);
      for (int ti = spans[si].getStart(); ti < spans[si].getEnd(); ti++) {
        cb.append(tokens[ti]).append(" ");
      }
      chunks[si] = cb.substring(0, cb.length() - 1);
    }
    return chunks;
  }

  public void setRuleId(String ruleId) {
    this.ruleId = ruleId;
  }

  public String getRuleId() {
    return this.ruleId;
  }
}
