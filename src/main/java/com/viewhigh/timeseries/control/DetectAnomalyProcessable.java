/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the GPL License.
 * See the accompanying LICENSE file for terms.
 */

// A template for doing Anomaly Detection.

package com.viewhigh.timeseries.control;

import java.util.ArrayList;
import java.util.Properties;

import com.viewhigh.timeseries.data.Anomaly;
import com.viewhigh.timeseries.data.TimeSeries;
import com.viewhigh.timeseries.utilities.GUIUtils;

public class DetectAnomalyProcessable implements ProcessableObject {
    private ModelAdapter ma;
    private AnomalyDetector ad;
    private Properties config;
    private ArrayList<Anomaly> anomalyList;

    public ArrayList<Anomaly> getAnomalyList() {
        return anomalyList;
    }


    DetectAnomalyProcessable(ModelAdapter ma, AnomalyDetector ad, Properties config) {
        this.ma = ma;
        this.ad = ad;
        this.config = config;
        anomalyList = new ArrayList<>();
    }

    public void process() throws Exception {

        // Resetting the models
        ma.reset();

        // Training the model with the whole metric
        ma.train();

        // Finding the expected values
        ArrayList<TimeSeries.DataSequence> list = ma.forecast(
            ma.metric.startTime(), ma.metric.lastTime());

        // For each model's prediction in the ModelAdapter
        for (TimeSeries.DataSequence ds : list) {
            // Reseting the anomaly detectors
            ad.reset();

            // Unsupervised tuning of the anomaly detectors
            ad.tune(ds, null);

            // Detecting anomalies for each anomaly detection model in anomaly detector
            anomalyList = ad.detect(ad.metric, ds);

            // Writing the anomalies to AnomalyDB
            if (config.getProperty("OUTPUT") != null && config.getProperty("OUTPUT").equals("ANOMALY_DB")) {
                for (Anomaly anomaly : anomalyList) {
                    // TODO: Batch Anomaly Process.
                }
            } else if (config.getProperty("OUTPUT") != null && config.getProperty("OUTPUT").equals("GUI")) {
                GUIUtils.plotResults(ma.metric.data, ds, anomalyList, config);
            } else if (config.getProperty("OUTPUT") != null && config.getProperty("OUTPUT").equals("PLOT")) {
                for (Anomaly anomaly : anomalyList) {
                    System.out.print(anomaly.toPlotString());
                }
            } else {
                for (Anomaly anomaly : anomalyList) {
                    System.out.print(anomaly.toPerlString());
                }
            }
        }
    }

    public ArrayList<Anomaly> result() throws Exception {
        return getAnomalyList();
    }
}
