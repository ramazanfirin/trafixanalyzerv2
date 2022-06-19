package com.masterteknoloji.trafficanalyzer.web.rest.util;

import java.util.List;

import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.Video;

public class AnalyzeOrderMultiple {
	 private List<Video> videoList;
	 private AnalyzeOrder analyzeOrder;
	public List<Video> getVideoList() {
		return videoList;
	}
	public void setVideoList(List<Video> videoList) {
		this.videoList = videoList;
	}
	public AnalyzeOrder getAnalyzeOrder() {
		return analyzeOrder;
	}
	public void setAnalyzeOrder(AnalyzeOrder analyzeOrder) {
		this.analyzeOrder = analyzeOrder;
	}

}
