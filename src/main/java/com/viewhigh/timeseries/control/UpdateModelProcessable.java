/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the GPL License.
 * See the accompanying LICENSE file for terms.
 */

// A template for updateing the model given the data and the model config.

package com.viewhigh.timeseries.control;

import java.util.Properties;

import com.viewhigh.timeseries.data.TimeSeries;

public class UpdateModelProcessable implements ProcessableObject {

    private ModelAdapter ma;
    private TimeSeries.DataSequence newData;
    private Properties config;

    UpdateModelProcessable(ModelAdapter ma, TimeSeries.DataSequence newData, Properties config) {
        this.ma = ma;
        this.newData = newData;
        this.config = config;
    }

    public void process() throws Exception {
        this.ma.train();
        this.ma.update(this.newData);
    }

    public Object result() throws Exception {
        return "Updated";
    }
}
