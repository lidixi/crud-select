# crud-select
纯粹的select

接收的参数为：问卷表主键surveyId（字符串为空则为全部问卷）、科室编码deptCode（字符串为空则为全部科室）、开始时间startDate和结束时间endDate、来源source（1为门诊、2为住院、3为手术）。接收前端的参数后会根据这些参数来筛选符合条件的患者，如source为1则对应从门诊患者信息表中就诊时间在开始时间和结束时间之间的患者，source为2、3同理。

先根据科室编码deptCode、开始时间startDate和结束时间endDate、来源source进行初步筛选，将符合筛选条件的所有患者主键用list统一保存后用于后续的统计。

通过随访任务表，完成随访任务统计：
1.统计患者主键的个数，作为总人数；
2.统计随访任务表中，出现过的患者主键的个数（患者主键不重复），作为创建任务人数；
3.计算（步骤1-步骤2），作为未创建任务人数；
4.统计随访任务表中，status不为0的且plan_answer_id为问卷表主键surveyId的患者主键的个数（患者主键不重复），作为已完成随访人数；
5.统计随访任务表中，status为1的且plan_answer_id为问卷表主键surveyId的患者主键的个数（患者主键不重复），作为已提交答卷人数；
6.计算（步骤5/步骤6），作为随访成功率。
7.统计随访任务表中，所有患者主键对应的记录中next_plan_date的最大值，作为随访截止日期。

通过随访记录表，完成答卷满意度统计：
1.总满意度统计：在随访记录表中，根据patient_id对应患者主键且plan_answer_id对应问卷表主键surveyId的记录的result_answer_id，计算对应的答卷的满意度题目（satisfaction_type为1的题目）的所有选项的选项结果的平均分，平均分大于等于3分表示该答卷为满意答卷，再计算（满意答卷的个数/对应的答卷的个数），作为总满意度；
2.满意比例排名前3个和满意比例排名后3个的选项内容及对应的满意比例的展示：在随访记录表中，根据patient_id对应患者主键且plan_answer_id对应问卷表主键surveyId的记录的result_answer_id，找出对应答卷的题目的所有选项及其选项结果，该选项下的选项结果大于等于3分即为满意选项，计算（对应选项的选项结果的满意个数/对应选项的选项结果的总数）为选项满意比例，然后将该问卷下的所有选项按满意比例排序，展示满意比例排名前3个和满意比例排名后3个的选项内容及对应的满意比例。


t_question题目表：qu_id主键，belong_id关联的问卷表主键，satisfaction_type是否为满意度题目（1为是，0为不是）。该表用于记录所有题目信息。
t_qu_score 评分题选项表：score_qu_id主键，qu_id所属的题目表主键，option_name选项名称。该表用于记录所有选项信息。
t_an_score评分题结果表：score_an_id主键，belong_answer_id所属的答卷表主键，belong_id所属的问卷表主键，qu_row_id所属的评分题选项表主键，qu_id所属的题目表主键，answer_score结果分数。该表用于记录所有选项结果。

t_follow_execute_plan随访任务表：patient_id所属的患者主键，dept_code所属的科室编码，status随访任务状态（1为成功，2为失败，0为未随访），source来源（1为门诊，2为住院，3为手术），plan_answer_id计划关联的问卷主键，next_plan_date随访任务的截止日期。该表用于记录所有任务信息。

t_follow_up_record随访记录表：ececute_id所属的随访任务主键，patient_id所属的患者主键，answer_status答卷状态（0为无答卷，1为有答卷），result_answer_id关联的答卷主键，dept_code所属的科室编码，plan_answer_id实际关联的问卷主键。该表记录所有答卷信息。

t_operation_master手术患者信息表：patient_id所属的患者主键，scheduled_date_time手术时间，exec_dept_code执行科室编码。该表用于记录所有手术患者信息。

t_outpatient_info门诊患者信息表：patient_id所属的患者主键，dept_code就诊科室编码，first_diagnosis_time就诊时间。该表用于记录所有门诊患者信息。

t_pats_in_hospital住院患者信息表：patient_id所属的患者主键，dept_admission_to_code入院科室编码，discharge_date_time出院时间。该表用于记录所有住院患者信息。



患者表（门诊、住院、手术）
       ↓
随访任务表（t_follow_execute_plan） ←→  问卷表
       ↓                                    ↓
随访记录表（t_follow_up_record）        问卷题目表（t_question）
       ↓                                    ↓
   答卷表                                  评分题选项表（t_qu_score）
       ↓                                    ↓
评分结果表（t_an_score）   ←→  具体选项、题目、分数
