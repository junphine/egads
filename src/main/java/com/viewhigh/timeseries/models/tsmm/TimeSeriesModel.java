/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the GPL License.
 * See the accompanying LICENSE file for terms.
 */

// interface

package com.viewhigh.timeseries.models.tsmm;

import com.viewhigh.timeseries.data.Model;
import com.viewhigh.timeseries.data.TimeSeries;

public interface TimeSeriesModel extends Model {
    // methods ////////////////////////////////////////////////

    void train(TimeSeries.DataSequence data) throws Exception;

    public abstract void update(TimeSeries.DataSequence data) throws Exception;

    // predicts the values of the time series specified by the 'time' fields of the sequence and sets the 'value' fields of the sequence
    public abstract void predict(TimeSeries.DataSequence sequence) throws Exception;
}
