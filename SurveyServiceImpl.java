package com.dyzc.function.service.impl;

import com.dyzc.function.domain.*;
import com.dyzc.function.mapper.SurveyMapper;
import com.dyzc.function.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyMapper surveyMapper;

    @Override
    public SurveyStatistics getSurveyStatistics(QueryFilter queryFilter) {

        // 筛选符合条件的患者主键
        List<String> patientIds = surveyMapper.filterPatients(queryFilter);

        // 如果没有符合条件的患者
        if (patientIds == null || patientIds.isEmpty()) {
            return new SurveyStatistics();
        }

        // 获取随访任务统计
        SurveyStatistics statistics = new SurveyStatistics();
        statistics.setTotalPatients(String.valueOf(patientIds.size()));

        // 获取已创建任务人数
        int createdTaskPatients = surveyMapper.getCreatedTaskPatients(patientIds);
        statistics.setCreatedTaskPatients(String.valueOf(createdTaskPatients));

        // 未创建任务人数 = 总人数 - 已创建任务人数
        int uncreatedTaskPatients = Math.max(patientIds.size() - createdTaskPatients, 0);
        statistics.setUncreatedTaskPatients(String.valueOf(uncreatedTaskPatients));

        // 获取已完成随访人数
        int completedFollowUpPatients = surveyMapper.getCompletedFollowUpPatients(queryFilter, patientIds);
        statistics.setCompletedFollowUpPatients(String.valueOf(completedFollowUpPatients));

        // 获取已提交答卷人数
        int submittedSurveyPatients = surveyMapper.getSubmittedSurveyPatients(queryFilter, patientIds);
        statistics.setSubmittedSurveyPatients(String.valueOf(submittedSurveyPatients));

        // 随访成功率 = 已提交答卷人数 / 已完成随访人数
        if (completedFollowUpPatients > 0) {
            int followUpSuccessRate = (int) (((double) submittedSurveyPatients / completedFollowUpPatients) * 100);
            statistics.setFollowUpSuccessRate(String.valueOf(followUpSuccessRate));
        } else {
            statistics.setFollowUpSuccessRate("0");
        }

        // 获取最大随访截止日期
        String nextPlanDate = Optional.ofNullable(surveyMapper.getNextPlanDate(patientIds)).orElse("N/A");
        statistics.setNextPlanDate(nextPlanDate);

        // 满意度统计
        String totalSatisfactionRate = Optional.ofNullable(surveyMapper.getTotalSatisfactionRate(queryFilter, patientIds)).orElse("0");
        statistics.setTotalSatisfactionRate(totalSatisfactionRate);

        // 获取满意比例排名前3和后3的选项
        statistics.setTopSatisfiedOptions(surveyMapper.getTopSatisfiedOptions(queryFilter, patientIds));
        statistics.setBottomSatisfiedOptions(surveyMapper.getBottomSatisfiedOptions(queryFilter, patientIds));

        return statistics;
    }
}