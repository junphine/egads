package com.unbiz.price;

import java.io.FileNotFoundException;

import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.input.CSVBuilder;

public class HousePricePredictor {

	public static void main(String[] args) throws Exception {
		String csvFile = "data/house/account_data.txt";
		CSVBuilder csvBuilder = new CSVBuilder(csvFile){
			{
				//this.addVariable("year");
				
				
			}
		};
		
		csvBuilder.setDependentVariableName("sales");
		DataSet dataset = csvBuilder.build();
		
		dataset.setTimeVariable("year");
		
		String output = dataset.toString();
		
	}

}
