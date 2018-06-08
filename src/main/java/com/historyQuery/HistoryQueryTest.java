package com.historyQuery;
/*
 *
 * @author LiuBing
 * @date 2018/6/6 20:51
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

import java.util.List;

public class HistoryQueryTest {
    ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
    /**
     * 查询历史流程实例
     */
    @Test
    public void findHistoryProcessInstance(){
        String processInstanceId = "1001";
        HistoricProcessInstance hp = pe.getHistoryService()//与历史数据相关的service
                .createHistoricProcessInstanceQuery()//创建历史流程实例查询
                .processInstanceId(processInstanceId)//指定流程实例Id查询
                .singleResult();
        System.out.println(hp.getId()+"  "
                +hp.getProcessDefinitionId()+"  "
                +hp.getStartTime()+"  "
                +hp.getEndTime()+"  "
                +hp.getProcessDefinitionVersion());
    }

    /**
     * 查询历史活动
     */
    public void findHistoryActivity(){
        String processInstance = "2109";
        List<HistoricActivityInstance> list = pe.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();
        for (HistoricActivityInstance hi : list) {

        }
    }


    /**
     * 查询历史任务
     */
    @Test
    public void findHistoryTask(){
        String taskAssignee = "张三";
        List<HistoricTaskInstance> list = pe.getHistoryService()//与历史数据相关的service
                .createHistoricTaskInstanceQuery()//创建历史任务实例查询
                .taskAssignee(taskAssignee)//指定历史任务办理人
                .list();
        if(null != list && list.size()>0){
            for(HistoricTaskInstance ht : list){
                System.out.println(ht.getId()+"  "
                        +ht.getName()+"  "
                        +ht.getProcessInstanceId()+"  "
                        +ht.getStartTime()+"  "
                        +ht.getEndTime()+"  "
                        +ht.getDurationInMillis());
                System.out.println("################################");
            }
        }
    }


    /**
     * 查询流程变量历史表
     */
    @Test
    public void findHistoryProcessVariable(){
        List<HistoricVariableInstance> list = pe.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .variableName("请假天数")
                .list();
    }


}
