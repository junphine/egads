/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the GPL License.
 * See the accompanying LICENSE file for terms.
 */

package com.viewhigh.timeseries.utilities;

// An interface which other classes extend
// based on what type of input is passed in.

import java.util.Properties;

public interface InputProcessor {
    
    void processInput(Properties p) throws Exception;
}
