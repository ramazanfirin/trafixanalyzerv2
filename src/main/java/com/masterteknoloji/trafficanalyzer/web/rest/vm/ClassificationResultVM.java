package com.masterteknoloji.trafficanalyzer.web.rest.vm;

import java.util.ArrayList;
import java.util.List;

public class ClassificationResultVM {

	String lineName;
	
	List<ClassificationResultDetailsVM> datas = new ArrayList<ClassificationResultDetailsVM>();

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public List<ClassificationResultDetailsVM> getDatas() {
		return datas;
	}

	public void setDatas(List<ClassificationResultDetailsVM> datas) {
		this.datas = datas;
	}

	
}
