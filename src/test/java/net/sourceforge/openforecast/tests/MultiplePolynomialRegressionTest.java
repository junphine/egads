//
//  OpenForecast - open source, general-purpose forecasting package.
//  Copyright (C) 2002-2011  Steven R. Gould
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

package net.sourceforge.openforecast.tests;

import java.util.Hashtable;

import net.sourceforge.openforecast.EvaluationCriteria;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.models.MultipleLinearRegressionModel;
import net.sourceforge.openforecast.models.MultiplePolynomialRegressionModel;


/**
 * Tests the Mutiple Variable Linear Regression model. This contains two test
 * cases - one to invoke and test the MultipleLinearRegressionModel directly;
 * the second to invoke the Forecaster and ensure that it uses the
 * MultipleLinearRegressionModel, and again gives the expected forecasts.
 */
public class MultiplePolynomialRegressionTest extends OpenForecastTestCase
{
    /**
     * Initialized by setUp to contain a sample data set.
     * @see setUp
     */
    private DataSet observedData;


    /**
     * Initializes the observedData data set with some sample data. Note that
     * the independent variable <code>x3</code> is a dummy, uncorrelated
     * variable included to make sure that it gets a zero coefficient in a
     * multiple linear regression model.
     */
    protected void setUp()
    {
    	//y=5-3*x2+4*x1+x1*x2
        observedData = new DataSet();
        DataPoint dp;

        dp = new Observation( 5.0 );
        dp.setIndependentValue( "x1", 0.0 );
        dp.setIndependentValue( "x2", 0.0 );
        dp.setIndependentValue( "x3", 0.0 );
        observedData.add( dp );

        dp = new Observation( 10.0+2 );
        dp.setIndependentValue( "x1", 2.0 );
        dp.setIndependentValue( "x2", 1.0 );
        dp.setIndependentValue( "x3", 1.0 );
        observedData.add( dp );

        dp = new Observation( 9.0+5 );
        dp.setIndependentValue( "x1", 2.5 );
        dp.setIndependentValue( "x2", 2.0 );
        dp.setIndependentValue( "x3", 2.0 );
        observedData.add( dp );

        dp = new Observation( 0.0+3 );
        dp.setIndependentValue( "x1", 1.0 );
        dp.setIndependentValue( "x2", 3.0 );
        dp.setIndependentValue( "x3", 3.0 );
        observedData.add( dp );

        dp = new Observation( 3.0+24 );
        dp.setIndependentValue( "x1", 4.0 );
        dp.setIndependentValue( "x2", 6.0 );
        dp.setIndependentValue( "x3", 4.0 );
        observedData.add( dp );

        dp = new Observation( 27.0 +14 );
        dp.setIndependentValue( "x1", 7.0 );
        dp.setIndependentValue( "x2", 2.0 );
        dp.setIndependentValue( "x3", 5.0 );
        observedData.add( dp );
    }

    /**
     * Tests the Multiple Polynomial Regression model directly. Uses the data
     * set initialized by setUp, where x3 is an independent variable that
     * does not affect the value of the dependent variable. In other words,
     * x3 should end up with a zero coefficient.
     */
    public void testMultiplePolynomialRegression()
    {
        // Obtain a Multiple Linear Regression forecasting model
        //  given this data set
        String independentVars[] = { "x1", "x2","x3" };
        MultiplePolynomialRegressionModel forecaster
            = new MultiplePolynomialRegressionModel(2,independentVars);

        // Initialize the model
        forecaster.init( observedData );

        // Create a data set for forecasting
        DataSet fcValues = new DataSet();

        DataPoint dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 5.0 );
        dp.setIndependentValue( "x2", 5.0 );
        dp.setIndependentValue( "x3", 5.0 );
        fcValues.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 2.0 );
        dp.setIndependentValue( "x2", 5.0 );
        dp.setIndependentValue( "x3", 7.0 );
        fcValues.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 8.0 );
        dp.setIndependentValue( "x2", 16.0 );
        dp.setIndependentValue( "x3", 22.0 );
        fcValues.add( dp );

        // Get forecast values
        DataSet results = forecaster.forecast( fcValues );
        assertTrue( fcValues.size() == results.size() );

        //y=5-3*x2+4*x1+x1*x2
        // These are the expected results
        double expectedResult[] = { 10.0+25, -2.0+10, -11.0+8*16 };

        // Check results against expected results
        checkResults( results, expectedResult );
    }

    public void testForecaster()
    {
        // Obtain a ForecastingModel
        ForecastingModel forecaster = Forecaster.getBestPolynomialForecast( observedData, EvaluationCriteria.MSE );

        // Ensure that a Multiple Linear Regression Model was chosen
        assertTrue( forecaster
                    .getClass()
                    .getName()
                    .equals("net.sourceforge.openforecast.models.MultiplePolynomialRegressionModel") );
        //y=5-3*x2+4*x1+x1*x2
        // Create a data set for forecasting
        DataSet fcValues = new DataSet();

        DataPoint dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 5.0 );
        dp.setIndependentValue( "x2", 5.0 );
        dp.setIndependentValue( "x3", 5.0 );
        fcValues.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 2.0 );
        dp.setIndependentValue( "x2", 5.0 );
        dp.setIndependentValue( "x3", 7.0 );
        fcValues.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 8.0 );
        dp.setIndependentValue( "x2", 16.0 );
        dp.setIndependentValue( "x3", 22.0 );
        fcValues.add( dp );

        // Get forecast values
        DataSet results = forecaster.forecast( fcValues );
        assertTrue( fcValues.size() == results.size() );

        // These are the expected results
        double expectedResult[] = { 10.0+25, -2.0+10, -11.0+8*16 };

        // Check results against expected results
        checkResults( results, expectedResult );
    }    
    
    public MultiplePolynomialRegressionTest( String name )
    {
        super(name);
    }
}
// Local variables:
// tab-width: 4
// End:
