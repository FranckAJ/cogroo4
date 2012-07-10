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
package org.cogroo.tools.checker;

import java.util.List;

import org.cogroo.tools.checker.rules.model.Example;

/**
 * Implementation of RuleDefinitionI to be used in Java based rules. This is
 * used to display human readable information about the rules in places like
 * Cogroo Comunidade
 */
public class JavaRuleDefinition implements RuleDefinitionI {

  private final String id;
  private final String category;
  private final String group;
  private final String description;
  private final String message;
  private final String shortMessage;
  private final List<Example> examples;

  /**
   * Creates a new Java rule definition
   * 
   * @param id
   *          prefixed identifier
   * @param category
   *          rule category
   * @param group
   *          rule group
   * @param description
   *          a description
   * @param message
   *          long error message generated by this rule
   * @param shortMessage
   *          short error message generated by this rule
   * @param examples
   *          examples of this errors catch by this rule
   */
  public JavaRuleDefinition(String id, String category, String group,
      String description, String message, String shortMessage,
      List<Example> examples) {
    super();
    this.id = id;
    this.category = category;
    this.group = group;
    this.description = description;
    this.message = message;
    this.shortMessage = shortMessage;
    this.examples = examples;
  }

  public String getId() {
    return id;
  }

  public String getCategory() {
    return category;
  }

  public String getGroup() {
    return group;
  }

  public String getDescription() {
    return description;
  }

  public String getMessage() {
    return message;
  }

  public String getShortMessage() {
    return shortMessage;
  }

  public List<Example> getExamples() {
    return examples;
  }

  /**
   * Will always return RuleType.JAVA
   * 
   * @see org.cogroo.tools.checker.RuleDefinitionI#getRuleType()
   */
  public RuleType getRuleType() {
    return RuleType.JAVA;
  }

  /**
   * Will allways return false
   * 
   * @see org.cogroo.tools.checker.RuleDefinitionI#isXMLBased()
   */
  public boolean isXMLBased() {
    return false;
  }

}