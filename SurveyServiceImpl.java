package com.dyzc.function.service.impl;

import com.dyzc.function.domain.*;
import com.dyzc.function.mapper.SurveyMapper;
import com.dyzc.function.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyMapper surveyMapper;

    private static final int BATCH_SIZE = 999;  // Oracle 限制是 1000，安全起见设为 999

    @Override
    public SurveyStatistics getSurveyStatistics(QueryFilter queryFilter) {

        // 筛选符合条件的患者主键
        List<String> patientIds = surveyMapper.filterPatients(queryFilter);

        // 如果 patientIds 为空或没有符合条件的患者，直接返回空的统计对象，避免后续查询
        if (patientIds == null || patientIds.isEmpty()) {
            return createEmptySurveyStatistics();
        }

        // 获取随访任务统计
        SurveyStatistics statistics = new SurveyStatistics();
        statistics.setTotalPatients(String.valueOf(patientIds.size()));

        // 分批查询并累加统计
        int createdTaskPatients = 0;
        int completedFollowUpPatients = 0;
        int submittedSurveyPatients = 0;

        // 分批处理 patientIds
        for (int i = 0; i < patientIds.size(); i += BATCH_SIZE) {
            List<String> batch = patientIds.subList(i, Math.min(i + BATCH_SIZE, patientIds.size()));

            // 获取已创建任务人数
            createdTaskPatients += surveyMapper.getCreatedTaskPatients(batch);

            // 获取已完成随访人数
            completedFollowUpPatients += surveyMapper.getCompletedFollowUpPatients(queryFilter, batch);

            // 获取已提交答卷人数
            submittedSurveyPatients += surveyMapper.getSubmittedSurveyPatients(queryFilter, batch);
        }

        statistics.setCreatedTaskPatients(String.valueOf(createdTaskPatients));

        // 未创建任务人数 = 总人数 - 已创建任务人数
        int uncreatedTaskPatients = Math.max(patientIds.size() - createdTaskPatients, 0);
        statistics.setUncreatedTaskPatients(String.valueOf(uncreatedTaskPatients));

        statistics.setCompletedFollowUpPatients(String.valueOf(completedFollowUpPatients));
        statistics.setSubmittedSurveyPatients(String.valueOf(submittedSurveyPatients));

        // 随访成功率 = 已提交答卷人数 / 已完成随访人数
        if (completedFollowUpPatients > 0) {
            int followUpSuccessRate = (int) (((double) submittedSurveyPatients / completedFollowUpPatients) * 100);
            statistics.setFollowUpSuccessRate(String.valueOf(followUpSuccessRate));
        } else {
            statistics.setFollowUpSuccessRate("0");
        }

        // 获取最大随访截止日期
        String nextPlanDate = getMaxNextPlanDate(queryFilter, patientIds);
        statistics.setNextPlanDate(nextPlanDate);

        // 满意度统计
        String totalSatisfactionRate = getTotalSatisfactionRate(queryFilter, patientIds);
        statistics.setTotalSatisfactionRate(totalSatisfactionRate);

        // 获取满意比例排名前3和后3的选项
        statistics.setTopSatisfiedOptions(getTopSatisfiedOptions(queryFilter, patientIds));
        statistics.setBottomSatisfiedOptions(getBottomSatisfiedOptions(queryFilter, patientIds));

        return statistics;
    }

    // 创建空的 SurveyStatistics 对象，当没有符合条件的患者时使用
    private SurveyStatistics createEmptySurveyStatistics() {
        SurveyStatistics emptyStats = new SurveyStatistics();
        emptyStats.setTotalPatients("0");
        emptyStats.setCreatedTaskPatients("0");
        emptyStats.setUncreatedTaskPatients("0");
        emptyStats.setCompletedFollowUpPatients("0");
        emptyStats.setSubmittedSurveyPatients("0");
        emptyStats.setFollowUpSuccessRate("0");
        emptyStats.setNextPlanDate("N/A");
        emptyStats.setTotalSatisfactionRate("0");
        emptyStats.setTopSatisfiedOptions(Collections.emptyList());
        emptyStats.setBottomSatisfiedOptions(Collections.emptyList());
        return emptyStats;
    }

    private String getMaxNextPlanDate(QueryFilter queryFilter, List<String> patientIds) {
        String nextPlanDate = "N/A";
        for (int i = 0; i < patientIds.size(); i += BATCH_SIZE) {
            List<String> batch = patientIds.subList(i, Math.min(i + BATCH_SIZE, patientIds.size()));
            String batchNextPlanDate = Optional.ofNullable(surveyMapper.getNextPlanDate(queryFilter, batch)).orElse("N/A");
            if (!"N/A".equals(batchNextPlanDate) && "N/A".equals(nextPlanDate)) {
                nextPlanDate = batchNextPlanDate;
            }
        }
        return nextPlanDate;
    }

    private String getTotalSatisfactionRate(QueryFilter queryFilter, List<String> patientIds) {
        String totalSatisfactionRate = "0";
        for (int i = 0; i < patientIds.size(); i += BATCH_SIZE) {
            List<String> batch = patientIds.subList(i, Math.min(i + BATCH_SIZE, patientIds.size()));
            String batchTotalSatisfactionRate = Optional.ofNullable(surveyMapper.getTotalSatisfactionRate(queryFilter, batch)).orElse("0");
            if (Double.parseDouble(batchTotalSatisfactionRate) > Double.parseDouble(totalSatisfactionRate)) {
                totalSatisfactionRate = batchTotalSatisfactionRate;
            }
        }
        return totalSatisfactionRate;
    }

    private List<OptionSatisfactionRate> getTopSatisfiedOptions(QueryFilter queryFilter, List<String> patientIds) {
        List<OptionSatisfactionRate> topSatisfiedOptions = new ArrayList<>();
        for (int i = 0; i < patientIds.size(); i += BATCH_SIZE) {
            List<String> batch = patientIds.subList(i, Math.min(i + BATCH_SIZE, patientIds.size()));
            topSatisfiedOptions.addAll(surveyMapper.getTopSatisfiedOptions(queryFilter, batch));
        }
        return topSatisfiedOptions;
    }

    private List<OptionSatisfactionRate> getBottomSatisfiedOptions(QueryFilter queryFilter, List<String> patientIds) {
        List<OptionSatisfactionRate> bottomSatisfiedOptions = new ArrayList<>();
        for (int i = 0; i < patientIds.size(); i += BATCH_SIZE) {
            List<String> batch = patientIds.subList(i, Math.min(i + BATCH_SIZE, patientIds.size()));
            bottomSatisfiedOptions.addAll(surveyMapper.getBottomSatisfiedOptions(queryFilter, batch));
        }
        return bottomSatisfiedOptions;
    }
}
