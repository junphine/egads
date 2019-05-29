package com.unbiz.price;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.yahoo.egads.control.ProcessableObject;
import com.yahoo.egads.control.ProcessableObjectFactory;
import com.yahoo.egads.data.Anomaly;
import com.yahoo.egads.data.TimeSeries;
import com.yahoo.egads.utilities.FileUtils;
import com.yahoo.egads.utilities.PlotUtils;

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.EvaluationCriteria;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.input.CSVBuilder;

public class HousePricePredictor {
	
	
	
	public static void testDetectAnomalyProcessable() throws Exception {
		String csvFile = "data/house/account_data.txt";
		InputStream is = new FileInputStream("data/house/account_config.ini");
		Properties p = new Properties();
		p.load(is);
		//-p.setProperty("TS_MODEL", "OlympicModel");
		p.setProperty("AD_MODEL", "KSigmaModel");
		p.setProperty("MAX_ANOMALY_TIME_AGO", "0");
		p.setProperty("OP_TYPE", "DETECT_ANOMALY");

		ArrayList<TimeSeries> metrics = FileUtils.createTimeSeries(csvFile, p);

		// generate expected result
		//Long anomalousTime = 1417194000L;
		//Anomaly anomaly = new Anomaly("value", null);
		//anomaly.addInterval(anomalousTime, anomalousTime, 0.0f);

		// actual result
		ProcessableObject po = ProcessableObjectFactory.create(metrics.get(5), p);
		po.process();
		
		//PlotUtils.plotResults(metrics,metrics.get(1),null,p);

	}

	public static void main(String[] args) throws Exception {
		String csvFile = "data/house/account_data.txt";
		
		testDetectAnomalyProcessable();
		
		CSVBuilder csvBuilder = new CSVBuilder(csvFile){
			{
				//this.addVariable("year");
				
				
			}
		};
		
		csvBuilder.setDependentVariableName("sales");
		DataSet dataset = csvBuilder.build();
		
		dataset.setTimeVariable("year");
		
		String output = dataset.toString();
		
		 // Obtain a ForecastingModel
        ForecastingModel forecaster = Forecaster.getBestPolynomialForecast( dataset, EvaluationCriteria.MSE );

        

        // Create a data set for forecasting
        DataSet fcValues = new DataSet();

        DataPoint dp = new Observation( 0.0 );
        dp.setIndependentValue( "year", 2017 );
        dp.setIndependentValue( "rate", 5.0 );
        dp.setIndependentValue( "income", 5.0 );
        fcValues.add( dp );

        dp.setIndependentValue( "year", 2018 );
        dp.setIndependentValue( "rate", 5.0 );
        dp.setIndependentValue( "income", 5.0 );
        fcValues.add( dp );

        fcValues.add( dp );
        
        DataSet result = forecaster.forecast(fcValues);
		
	}

}
