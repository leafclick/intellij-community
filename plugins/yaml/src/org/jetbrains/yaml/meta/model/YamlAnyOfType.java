// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.yaml.meta.model;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YamlAnyOfType extends YamlComposedTypeBase {

  public static YamlMetaType anyOf(YamlMetaType... types) {
    if (types.length == 0) {
      throw new IllegalArgumentException();
    }
    if (types.length == 1) {
      return types[0];
    }
    String name = "AnyOf[" + Stream.of(types).map(YamlMetaType::getDisplayName).collect(Collectors.joining(",")) + "]";
    return new YamlAnyOfType(name, flattenTypes(types));
  }

  protected YamlAnyOfType(@NotNull String typeName, List<YamlMetaType> types) {
    super(typeName, types);
  }

  @Override
  public void validateKey(@NotNull YAMLKeyValue keyValue, @NotNull ProblemsHolder problemsHolder) {
    List<ProblemsHolder> allProblems = allProblemsOrEmpty(problemsHolder, myTypes,
                                                          (nextType, nextHolder) -> nextType.validateKey(keyValue, nextHolder));

    allProblems.stream()
      .flatMap(h -> h.getResults().stream())
      .forEach(problemsHolder::registerProblem);
  }

  @Override
  public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
    List<ProblemsHolder> allProblems = allProblemsOrEmpty(problemsHolder, myTypes,
                                                          (nextType, nextHolder) -> nextType.validateDeep(value, nextHolder));

    allProblems.stream()
      .flatMap(h -> h.getResults().stream())
      .forEach(problemsHolder::registerProblem);
  }

  @NotNull
  @Override
  public List<? extends LookupElement> getValueLookups(@NotNull YAMLScalar insertedScalar, @Nullable CompletionContext completionContext) {
    return streamSubTypes()
      .flatMap(type -> type.getValueLookups(insertedScalar, completionContext).stream())
      .collect(Collectors.toList());
  }

  @NotNull
  private static List<ProblemsHolder> allProblemsOrEmpty(@NotNull ProblemsHolder problemsHolder, @NotNull List<YamlMetaType> types,
                                                         @NotNull BiConsumer<YamlMetaType, ProblemsHolder> oneValidation) {
    List<ProblemsHolder> problems = new SmartList<>();
    for (YamlMetaType nextType : types) {
      ProblemsHolder nextHolder = makeCopy(problemsHolder);
      oneValidation.accept(nextType, nextHolder);
      if (!nextHolder.hasResults()) {
        return Collections.emptyList();
      }
      problems.add(nextHolder);
    }
    return problems;
  }
}
