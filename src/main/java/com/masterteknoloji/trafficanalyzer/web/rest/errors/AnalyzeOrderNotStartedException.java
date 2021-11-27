package com.masterteknoloji.trafficanalyzer.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

public class AnalyzeOrderNotStartedException extends AbstractThrowableProblem {

    public AnalyzeOrderNotStartedException(String error) {
        super(ErrorConstants.EMAIL_NOT_FOUND_TYPE, "Analiz Baslamadi. Hata:"+error);
    }
}
