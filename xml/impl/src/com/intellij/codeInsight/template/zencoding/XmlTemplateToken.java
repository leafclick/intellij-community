/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.codeInsight.template.zencoding;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.xml.XmlTag;

import java.util.List;

/**
 * @author Eugene.Kudelevsky
 */
public class XmlTemplateToken extends TemplateToken {
  private final List<Pair<String, String>> myAttribute2Value;
  private XmlTag myTag;

  public XmlTemplateToken(String key, List<Pair<String, String>> attribute2value) {
    super(key);
    myAttribute2Value = attribute2value;
  }

  public List<Pair<String, String>> getAttribute2Value() {
    return myAttribute2Value;
  }

  public XmlTag getTag() {
    return myTag;
  }

  public void setTag(XmlTag tag) {
    myTag = tag;
  }
}
