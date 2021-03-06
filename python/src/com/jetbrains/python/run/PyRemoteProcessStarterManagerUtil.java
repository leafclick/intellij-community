/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package com.jetbrains.python.run;

import com.jetbrains.python.remote.PyRemoteSdkAdditionalDataBase;
import com.jetbrains.python.remote.UnsupportedPythonSdkTypeException;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Koshevoy
 */
public class PyRemoteProcessStarterManagerUtil {
  private PyRemoteProcessStarterManagerUtil() {
  }

  /**
   * Returns an instance of {@link PyRemoteProcessStarterManager} that
   * corresponds to the provided additional SDK data.
   *
   * @param pyRemoteSdkAdditionalDataBase additional SDK data
   * @return an instance of {@link PyRemoteProcessStarterManager}
   * @throws UnsupportedPythonSdkTypeException if support cannot be found for
   *                                           the type of the provided
   *                                           additional SDK data
   */
  @NotNull
  public static PyRemoteProcessStarterManager getManager(@NotNull PyRemoteSdkAdditionalDataBase pyRemoteSdkAdditionalDataBase) {
    for (PyRemoteProcessStarterManager processManager : PyRemoteProcessStarterManager.EP_NAME.getExtensions()) {
      if (processManager.supports(pyRemoteSdkAdditionalDataBase)) {
        return processManager;
      }
    }
    throw new UnsupportedPythonSdkTypeException();
  }
}
