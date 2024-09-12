package com.dyzc.function.service;

import com.dyzc.function.domain.QueryFilter;
import com.dyzc.function.domain.SurveyStatistics;

public interface SurveyService {
    SurveyStatistics getSurveyStatistics(QueryFilter queryFilter);
}
