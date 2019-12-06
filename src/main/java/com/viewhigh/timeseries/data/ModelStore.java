package com.viewhigh.timeseries.data;

public interface ModelStore {
	public void storeModel(String tag, Model m);
	public Model retrieveModel (String tag);
}
