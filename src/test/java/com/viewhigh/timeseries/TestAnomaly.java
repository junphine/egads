/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the GPL License.
 * See the accompanying LICENSE file for terms.
 */

package com.viewhigh.timeseries;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.viewhigh.timeseries.data.*;

public class TestAnomaly {

    @Test
    public void test() {
        // just test construction and printing
        Anomaly anomaly = new Anomaly("ANOMALY_ID", null);
        anomaly.addInterval(1, 11, 1.11f);
        System.out.print("\n anomaly = " + anomaly);
    }

}
