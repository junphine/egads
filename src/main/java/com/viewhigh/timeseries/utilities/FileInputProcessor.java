/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the GPL License.
 * See the accompanying LICENSE file for terms.
 */

package com.viewhigh.timeseries.utilities;

import java.util.Properties;

import com.viewhigh.timeseries.control.ProcessableObject;
import com.viewhigh.timeseries.control.ProcessableObjectFactory;
import com.viewhigh.timeseries.data.TimeSeries;

import java.util.ArrayList;

public class FileInputProcessor implements InputProcessor {
    
    private String file = null;
    
    public FileInputProcessor(String file) {
        this.file = file;
    }
    
    public void processInput(Properties p) throws Exception {
        // Parse the input timeseries.
        ArrayList<TimeSeries> metrics = com.viewhigh.timeseries.utilities.FileUtils
                .createTimeSeries(this.file, p);
        for (TimeSeries ts : metrics) {
            ProcessableObject po = ProcessableObjectFactory.create(ts, p);
            po.process();
        }
    }
}
