package com.processVariable;
/*
 *
 * @author LiuBing
 * @date 2018/6/6 14:02
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;

/**
 * 流程变量的作用域就是流程实例，不管在哪个阶段设置
 *
 */
public class ProcessVariableTest {
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
        InputStream in = this.getClass().getResourceAsStream("/diagrams/helloWord.bpmn");
        InputStream in1 = this.getClass().getResourceAsStream("/diagrams/helloWord.png");
        Deployment deployment = pe.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("HelloWold")//添加部署名称
                .addInputStream("helloWord.bpmn",in)//使用资源文件的名称
                .addInputStream("helloWord.png",in1)//使用资源文件的名称
                .deploy();//部署完成
        System.out.println("部署ID:"+deployment.getId());
        System.out.println("部署名字："+deployment.getName());
    }

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
        String processDefinitionKey= "helloWord";
        ProcessInstance pi = pe.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloWord.bpmn文件中id的属性值,使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID："+pi.getId());
        System.out.println("流程定义ID："+pi.getProcessDefinitionId());
    }
    /**
     * 设置流程变量
     * act_ru_variable--正在执行的流程变量表
     * act_hi_variable--历史的流程变量表
     *
     */
    @Test
    public void setVariable(){
        TaskService taskService = pe.getTaskService();
        String taskId = "7502";
        /**  使用基本数据类型设置流程变量  **/
        //taskService.setVariableLocal(taskId,"请假天数" ,3);//与任务Id绑定
        //taskService.setVariable(taskId,"请假日期" ,new Date());
        //taskService.setVariable(taskId,"请假原因" ,"回家探亲");



        /*   使用javaBean流程变量
         *   当一个javaBean（实现序列化）放置到流程变量中，要求javaBean属性不能发生变化，否则获取抛出异常
         *   解决方案，在javaBean中添加版本号
         *   act_ge_property--资源文件表，存放对象
         *
         */
        Person p = new Person();
        p.setId(1);
        p.setName("ceshi");
        taskService.setVariable(taskId,"人员信息" ,p);
    }


    /**
     * 获取流程变量
     */
    @Test
    public void getVariable(){
        TaskService taskService = pe.getTaskService();
        String taskId = "5002";
        //获取流程变量，使用基本数据类型
        Integer days = (Integer) taskService.getVariable(taskId,"请假天数");
        System.out.println(days);
        //获取流程变量，使用javaBean
        Person p = (Person) taskService.getVariable(taskId,"人员信息");
    }

    /**
     * 模拟设置和获取流程变量的场景
     */
    public void getAndSetVariable(){
        //与流程实例，执行对象
        RuntimeService runtimeService = pe.getRuntimeService();
        //与任务
        TaskService taskService = pe.getTaskService();

        //设置流程变量
        //runtimeService.setVariable(executionId,variableName,value);//表示使用执行对象Id，和流程变量的名称，设置流程变量的值，一次只能设置一个值
        //runtimeService.setVariable(executionId,variables);//表示使用执行对象ID，Map集合设置流程变量，map集合的key就是流程变量的名称，value为值
        //taskService.setVariable(taskId,variableName,value);//表示使用任务Id，和流程变量的名称，设置流程变量的值，一次只能设置一个值
        //taskService.setVariable(taskId,variables);//表示使用任务Id，和流程变量的名称，设置流程变量的值，一次只能设置一个值

        //runtimeService.startProcessInstanceByKey(processDefinitionKey,variables)//启动流程实例的同事设置流程变量，map集合的key就是流程变量的名称，value为值
        //taskService.complete(taskId,variables);//完成任务的同时设置流程变量


        //获取流程变量
        //runtimeService.getVariable(executionId,variableName)//使用执行对象ID，流程变量名称获取值
        //runtimeService.getVariable(executionId)//获取所有的流程变量，将流程变量放置在map中
        //runtimeService.getVariable(executionId,variables)//获取指定流程变量名称的值


        //taskService.getVariable(taskId,variableName)//使用任务ID，流程变量名称获取值
        //taskService.getVariable(taskId)//获取所有的流程变量，将流程变量放置在map中
        //taskService.getVariable(taskId,variables)//获取指定流程变量名称的值
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyTask(){
        //任务ID
        String taskId = "2504";
        pe.getTaskService()//与正在执行的任务管理相关的Serv
                .complete(taskId);
        System.out.println("完成任务，任务ID"+taskId);
    }
}
