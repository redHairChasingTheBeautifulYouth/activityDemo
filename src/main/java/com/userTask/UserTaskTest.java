package com.userTask;
/*
 *
 * @author LiuBing
 * @date 2018/6/7 20:15
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UserTaskTest {
    ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
    /**
     *  部署流程定义（从classpath）,
     *  act_re_deployment--部署对象表，存放流程定义的显示名和部署时间，每部署一次就增加一次记录
     *  act_re_procdef--流程定义表，存放流程定义的属性信息，部署每个新的流程定义都会在这张表中增加一条记录，注意，当流程定义的key相同情况下，使用的是版本升级
     *  act_ge_bytearray--资源文件表，存放流程相关的部署信息，即规则文件
     *  act_ge_property--主键生成策略表
     */
    @Test
    public void deploymentProcessDefinition(){
        InputStream in = this.getClass().getResourceAsStream("/diagrams/userTask.bpmn");
        InputStream in1 = this.getClass().getResourceAsStream("/diagrams/userTask.png");
        Deployment deployment = pe.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("userTask")//添加部署名称
                .addInputStream("userTask.bpmn",in)//使用资源文件的名称
                .addInputStream("userTask.png",in1)//使用资源文件的名称
                .deploy();//部署完成
        System.out.println("部署ID:"+deployment.getId());
        System.out.println("部署名字："+deployment.getName());
    }


    //#######################################################################################

    //分配个人任务第一种方式--直接指定，在userTask的Main Config的Assignee字段中直接指定
    //略

    //#######################################################################################

    //分配个人任务第二种方式--通过流程变量指定

    /**
     * 启动流程实例
     * act_ru_execution--正在执行的执行对象表，如果是单例流程（没有分支），那么流程实例ID和执行对象ID（这张表中的ID）相同
     * act_hi_procinst--流程实例历史表，一个流程流程实例只有一个，执行对象可以有多个
     * act_ru_task--正在执行的任务表，只有节点是userTask,该表中存在数据
     * acu_hi_taskinst--任务历史表 ，只有节点是userTask,该表中存在数据
     * acu_hi_actinst--所有活动节点的历史表
     */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey= "userTask";
        //启动流程实例同时设置流程变量，来指定用户办理人
        Map<String ,Object> map = new HashMap<String, Object>();
        map.put("userId","周芷若");
        ProcessInstance pi = pe.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey,map);//使用流程定义的key启动流程实例，key对应helloWord.bpmn文件中id的属性值,使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID："+pi.getId());
        System.out.println("流程定义ID："+pi.getProcessDefinitionId());
    }

    //#######################################################################################

    //分配个人任务第三种方式--通过类
    //在userTask的Listeners属性中指定监听器（TaskListerImpl的类路径）











    //##########################################################################
    //非陪个人任务从一个人到另一个人（认领任务）
    @Test
    public void setAssigneeTask(){
        String taskId = "1212";
        String userId = "翠花";
        pe.getTaskService().setAssignee(taskId,userId);
    }
}
