/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the GPL License.
 * See the accompanying LICENSE file for terms.
 */

// interface

package com.viewhigh.timeseries.models.adm;

import com.viewhigh.timeseries.data.Anomaly;
import com.viewhigh.timeseries.data.Model;
import com.viewhigh.timeseries.data.TimeSeries;

public interface AnomalyDetectionModel extends Model {
    // methods ////////////////////////////////////////////////

    // returns the type of anomalies detected by the model
    public String getType();

    // tune the anomaly detection parameters based on the training data.
    public void tune(TimeSeries.DataSequence observedSeries,
            TimeSeries.DataSequence expectedSeries,
            Anomaly.IntervalSequence anomalySequence) throws Exception;

    // method to check whether the anomaly value is inside the
    // detection window or not
    public boolean isDetectionWindowPoint(int maxHrsAgo,
                                          long windowStart,
                                          long anomalyTime,
                                          long startTime);

    // detect anomalies.
    public Anomaly.IntervalSequence detect(
            TimeSeries.DataSequence observedSeries,
            TimeSeries.DataSequence expectedSeries) throws Exception;
}
