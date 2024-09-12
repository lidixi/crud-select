package com.dyzc.function.domain;

public class QueryFilter {
    private String surveyId;     // 问卷表主键
    private String deptCode;     // 科室编码
    private String startDate;    // 开始时间
    private String endDate;      // 结束时间
    private String source;       // 来源 (1为门诊，2为住院，3为手术)

    public String getSurveyId() { return surveyId; }
    public void setSurveyId(String surveyId) { this.surveyId = surveyId; }

    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
