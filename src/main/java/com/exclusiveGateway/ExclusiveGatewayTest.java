package com.exclusiveGateway;
/*
 *
 * @author LiuBing
 * @date 2018/6/7 14:53
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExclusiveGatewayTest {
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
        InputStream in = this.getClass().getResourceAsStream("/diagrams/exclusiveGateWay.bpmn");
        InputStream in1 = this.getClass().getResourceAsStream("/diagrams/exclusiveGateWay.png");
        Deployment deployment = pe.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("排他网关")//添加部署名称
                .addInputStream("exclusiveGateWay.bpmn",in)//使用资源文件的名称
                .addInputStream("exclusiveGateWay.png",in1)//使用资源文件的名称
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
        String processDefinitionKey= "exclusiveGateway";
        ProcessInstance pi = pe.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloWord.bpmn文件中id的属性值,使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID："+pi.getId());
        System.out.println("流程定义ID："+pi.getProcessDefinitionId());
    }


    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyTask(){
        String name = "战士 ";
        List<Task> list = pe.getTaskService()//正在执行的任务管理者相关的Service
                .createTaskQuery()//创建任务查询对象
                /**  查询条件  **/
                .taskAssignee(name)//指定个人任务查询，指定办理人
                //.taskCandidateUser()//组任务的办理人查询
                //.processDefinitionId()//流程定义id查询
                //.processInstanceId()//流程实例id查询
                //.executionId()//使用执行对象id查询
                /**  排序  **/


                /**  返回结果集  **/
                .list();
        if(null != list && list.size()>0){
            for (Task task : list){
                System.out.println("任务ID："+task.getId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务创建时间："+task.getCreateTime());
                System.out.println("任务办理人："+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID："+task.getExecutionId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
                System.out.println("#################################");

            }

        }
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyTask(){
        //任务ID
        String taskId = "47504";
        //完成任务的同时设置流程变量，使用流程变量来指定下一个连线，对应sequence文件中的（重要，不重要）
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("money",600);
        pe.getTaskService()//与正在执行的任务管理相关的Serv
                .complete(taskId,map);
        System.out.println("完成任务，任务ID"+taskId);
    }




}
