package com.dyzc.function.mapper;

import com.dyzc.function.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SurveyMapper {
    List<String> filterPatients(@Param("queryFilter") QueryFilter queryFilter);

    int getCreatedTaskPatients(@Param("patientIds") List<String> patientIds);

    int getCompletedFollowUpPatients(@Param("queryFilter") QueryFilter queryFilter, @Param("patientIds") List<String> patientIds);

    int getSubmittedSurveyPatients(@Param("queryFilter") QueryFilter queryFilter, @Param("patientIds") List<String> patientIds);

    String getNextPlanDate(@Param("patientIds") List<String> patientIds);

    String getTotalSatisfactionRate(@Param("queryFilter") QueryFilter queryFilter, @Param("patientIds") List<String> patientIds);

    List<OptionSatisfactionRate> getTopSatisfiedOptions(@Param("queryFilter") QueryFilter queryFilter, @Param("patientIds") List<String> patientIds);

    List<OptionSatisfactionRate> getBottomSatisfiedOptions(@Param("queryFilter") QueryFilter queryFilter, @Param("patientIds") List<String> patientIds);
}