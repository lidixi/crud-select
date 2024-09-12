package com.dyzc.function.domain;

import java.util.List;

public class SurveyStatistics {
    private String totalPatients;
    private String createdTaskPatients;
    private String uncreatedTaskPatients;
    private String completedFollowUpPatients;
    private String submittedSurveyPatients;
    private String followUpSuccessRate;
    private String nextPlanDate;
    private String totalSatisfactionRate;
    private List<OptionSatisfactionRate> topSatisfiedOptions;
    private List<OptionSatisfactionRate> bottomSatisfiedOptions;

    // Getters and Setters
    public String getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(String totalPatients) {
        this.totalPatients = totalPatients;
    }

    public String getCreatedTaskPatients() {
        return createdTaskPatients;
    }

    public void setCreatedTaskPatients(String createdTaskPatients) {
        this.createdTaskPatients = createdTaskPatients;
    }

    public String getUncreatedTaskPatients() {
        return uncreatedTaskPatients;
    }

    public void setUncreatedTaskPatients(String uncreatedTaskPatients) {
        this.uncreatedTaskPatients = uncreatedTaskPatients;
    }

    public String getCompletedFollowUpPatients() {
        return completedFollowUpPatients;
    }

    public void setCompletedFollowUpPatients(String completedFollowUpPatients) {
        this.completedFollowUpPatients = completedFollowUpPatients;
    }

    public String getSubmittedSurveyPatients() {
        return submittedSurveyPatients;
    }

    public void setSubmittedSurveyPatients(String submittedSurveyPatients) {
        this.submittedSurveyPatients = submittedSurveyPatients;
    }

    public String getFollowUpSuccessRate() {
        return followUpSuccessRate;
    }

    public void setFollowUpSuccessRate(String followUpSuccessRate) {
        this.followUpSuccessRate = followUpSuccessRate;
    }

    public String getNextPlanDate() {
        return nextPlanDate;
    }

    public void setNextPlanDate(String nextPlanDate) {
        this.nextPlanDate = nextPlanDate;
    }

    public String getTotalSatisfactionRate() {
        return totalSatisfactionRate;
    }

    public void setTotalSatisfactionRate(String totalSatisfactionRate) {
        this.totalSatisfactionRate = totalSatisfactionRate;
    }

    public List<OptionSatisfactionRate> getTopSatisfiedOptions() {
        return topSatisfiedOptions;
    }

    public void setTopSatisfiedOptions(List<OptionSatisfactionRate> topSatisfiedOptions) {
        this.topSatisfiedOptions = topSatisfiedOptions;
    }

    public List<OptionSatisfactionRate> getBottomSatisfiedOptions() {
        return bottomSatisfiedOptions;
    }

    public void setBottomSatisfiedOptions(List<OptionSatisfactionRate> bottomSatisfiedOptions) {
        this.bottomSatisfiedOptions = bottomSatisfiedOptions;
    }
}