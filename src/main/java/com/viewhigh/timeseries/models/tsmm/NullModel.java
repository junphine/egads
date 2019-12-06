/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the GPL License.
 * See the accompanying LICENSE file for terms.
 */

// Null model, returns prediction the same as a prediction.

package com.viewhigh.timeseries.models.tsmm;

import java.util.Properties;

import com.viewhigh.timeseries.data.*;
import com.viewhigh.timeseries.data.TimeSeries.Entry;
import com.viewhigh.timeseries.utilities.FileUtils;

import java.util.ArrayList;
import java.util.Collections;

public class NullModel extends TimeSeriesAbstractModel {
    // methods ////////////////////////////////////////////////

	private static final long serialVersionUID = 1L;
        private TimeSeries.DataSequence data = null;

    public NullModel(Properties config) {
        super(config);
    }

    public void reset() {
        // At this point, reset does nothing.
    }

    public void train(TimeSeries.DataSequence data) {
        this.data = data;
    }

    public void update(TimeSeries.DataSequence data) {
    }

    public String getModelName() {
        return "NullModel";
    }
    
    public void predict(TimeSeries.DataSequence sequence) throws Exception {
        int n = data.size();
        for (int i = 0; i < n; i++) {
            sequence.set(i, (new Entry(data.get(i).time, (long) 0.0)));
            logger.info(data.get(i).time + "," + data.get(i).value + "," + data.get(i).value);
        }
    }
}
