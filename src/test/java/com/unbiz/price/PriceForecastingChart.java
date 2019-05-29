//
//  OpenForecast - open source, general-purpose forecasting package.
//  Copyright (C) 2002-2004  Steven R. Gould
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//

package com.unbiz.price;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Year;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.input.CSVBuilder;
import net.sourceforge.openforecast.models.MovingAverageModel;
import net.sourceforge.openforecast.models.NaiveForecastingModel;
import net.sourceforge.openforecast.models.RegressionModel;
import net.sourceforge.openforecast.models.PolynomialRegressionModel;

/**
 * An example of a time series chart, showing an initial series of
 * observations (the first data series), overlaid with forecasts produced
 * using a variety of different forecasting models. This both demonstrates
 * the use of JFreeChart, as well as provides a graphical comparison of
 * some of the different forecasting models.
 */
public class PriceForecastingChart extends ApplicationFrame
{
    /** The set of data points for which forecast values are required. */
    private TimeSeries fc;
    
    boolean useMultipleVariable = false;
    
    /**
     * A demonstration application showing a Yearly time series
     * along with the forecast values.
     * @param title the frame title.
     * @throws IOException 
     */
    public PriceForecastingChart(String title) throws IOException
    {
        super(title);
        
        // Create a title...
        String chartTitle = "OpenForecast Demo";
        XYDataset dataset = createDataset();
        
        JFreeChart chart
            = ChartFactory.createTimeSeriesChart(chartTitle,
                                                 "Date",
                                                 "Yearly Sales (Units sold)",
                                                 dataset,
                                                 true,  // Legend
                                                 true,  // Tooltips
                                                 false);// URLs
        
        XYPlot plot = chart.getXYPlot();
        XYItemRenderer renderer = plot.getRenderer();
        if (renderer instanceof StandardXYItemRenderer)
            {
                StandardXYItemRenderer r = (StandardXYItemRenderer) renderer;
                //r.setPlotShapes(true);
                //r.setDefaultShapesFilled(Boolean.TRUE);
                r.setShapesFilled(Boolean.TRUE);
            }
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    /**
     * Creates a dataset, consisting of two series of monthly data.
     * @return the dataset.
     * @throws IOException 
     */
    public XYDataset createDataset() throws IOException
    {
        //30 years
        fc = new TimeSeries("Forecast values", Year.class);
        for(int y=1999;y<2029;y++){
        	fc.add(new Year(y), 0.0);
        }       
        
        //DataSet initDataSet = getDataSet( observations, 0, 100 );
        String csvFile = "data/house/account_data.txt";	
		CSVBuilder csvBuilder = new CSVBuilder(csvFile);
		
		csvBuilder.setDependentVariableName("balance");
		DataSet initDataSet = csvBuilder.build();
		//1999-2016 18年
		initDataSet.setTimeVariable("year");
		
		 // Create a new TimeSeries
        TimeSeries observations
            = new TimeSeries("Samples",fc.getTimePeriodClass());
        
        // Iterator through the forecast results, adding to the series
        Iterator it = initDataSet.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = (DataPoint)it.next();               
                int year  = (int)dp.getIndependentValue("year");
                observations.add( new Year(year), dp.getDependentValue() );
            }
        
        TimeSeries naiveSeries
            = getForecastTimeSeries(new NaiveForecastingModel(),
                                    initDataSet,
                                    1, 19,
                                    "Naive forecast");
        TimeSeries ma4Series
            = getForecastTimeSeries(new MovingAverageModel(4),
                                    initDataSet,
                                    4, 20,
                                    "4 Period Moving Average");
       
        TimeSeries regressionSeries
            = getForecastTimeSeries(new RegressionModel("year"),
                                    initDataSet,
                                    0, 29,
                                    "Linear regression");
        TimeSeries polyRegressSeries
            = getForecastTimeSeries(new PolynomialRegressionModel("year",7),
                                    initDataSet,
                                    0, 29,
                                    "5th order polynomial regression");        
        
       
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(observations);
        dataset.addSeries(naiveSeries);
        dataset.addSeries(ma4Series);        
        dataset.addSeries(regressionSeries);
        dataset.addSeries(polyRegressSeries);
        
        if(useMultipleVariable){
	        ForecastingModel forecaster = Forecaster.getBestForecast( initDataSet );
	        
	        TimeSeries ma8Series = getForecastTimeSeries(forecaster,
	                                initDataSet,
	                                0, 29,
	                                "Best choose");
	        dataset.addSeries(ma8Series);
        }
        return dataset;
    }
    
    /**
     * A helper function to convert data points (from startIndex to
     * endIndex) of a (JFreeChart) TimeSeries object into an
     * OpenForecast DataSet.
     * @param series the series of data points stored as a JFreeChart
     * TimeSeries object.
     * @param startIndex the index of the first data point required from the
     * series.
     * @param endIndex the index of the last data point required from the
     * series.
     * @return an OpenForecast DataSet representing the data points extracted
     * from the TimeSeries.
     */
    private DataSet getDataSet( TimeSeries series,
                                int startIndex, int endIndex )
    {
        DataSet dataSet = new DataSet();
        if ( endIndex > series.getItemCount() )
            endIndex = series.getItemCount();
        
        for ( int i=startIndex; i<endIndex; i++ )
            {
                TimeSeriesDataItem dataPair = series.getDataItem(i);
                DataPoint dp = new Observation( dataPair.getValue().doubleValue() );
               // dp.setIndependentValue("t", i );
                dp.setIndependentValue("year", ((Year)dataPair.getPeriod()).getYear());
                dataSet.add( dp );
            }
        
        return dataSet;
    }
    
    /**
     * Use the given forecasting model to produce a TimeSeries object
     * representing the periods startIndex through endIndex, and containing
     * the forecast values produced by the model.
     * @param model the forecasting model to use to generate the forecast
     * series.
     * @param initDataSet data set to use to initialize the forecasting model.
     * @param startIndex the index of the first data point to use from the
     * set of potential forecast values.
     * @param endIndex the index of the last data point to use from the set
     * of potential forecast values.
     * @param title a title to give to the TimeSeries created.
     */
    private TimeSeries getForecastTimeSeries( ForecastingModel model,
                                              DataSet initDataSet,
                                              int startIndex,
                                              int endIndex,
                                              String title )
    {
        // Initialize the forecasting model
        model.init( initDataSet );
        
        // Get range of data required for forecast
        DataSet fcDataSet = getDataSet( fc, startIndex, endIndex );
        
        // Obtain forecast values for the forecast data set
        model.forecast( fcDataSet );
        
        // Create a new TimeSeries
        TimeSeries series
            = new TimeSeries(title,fc.getTimePeriodClass());
        
        // Iterator through the forecast results, adding to the series
        Iterator it = fcDataSet.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = (DataPoint)it.next();
                //-int index = (int)dp.getIndependentValue("t");
                int year  = (int)dp.getIndependentValue("year");
                series.add( new Year(year), dp.getDependentValue() );
            }
        
        return series;
    }
    
    /**
     * Starting point for the forecasting charting demo application.
     * @param args ignored.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
    {
    	PriceForecastingChart demo
            = new PriceForecastingChart("Forecasting Demo: Time Series");
        demo.pack();
        demo.setVisible(true);
    }
}
// Local Variables:
// tab-width: 4
// End:
