package com.processDefinition;
/*
 *
 * @author LiuBing
 * @date 2018/6/5 17:50
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

public class ProcessDefinitionTest {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    /**
     *  部署流程定义（从classpath）,
     *  act_re_deployment--部署对象表，存放流程定义的显示名和部署时间，每部署一次就增加一次记录
     *  act_re_procdef--流程定义表，存放流程定义的属性信息，部署每个新的流程定义都会在这张表中增加一条记录，注意，当流程定义的key相同情况下，使用的是版本升级
     *  act_ge_bytearray--资源文件表，存放流程相关的部署信息，即规则文件
     *  act_ge_property--主键生成策略表
     */
    @Test
    public void deploymentProcessDefinition(){
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("流程定义")//添加部署名称
                .addClasspathResource("diagrams/helloWord.bpmn")//从classPath的资源中加载，一次只能加载一个文件
                .addClasspathResource("diagrams/helloWord.png")//从classPath的资源中加载，一次只能加载一个文件
                .deploy();//部署完成
        System.out.println("部署ID:"+deployment.getId());
        System.out.println("部署名字："+deployment.getName());
    }
    /**
     *  部署流程定义（从zip）
     *  act_re_deployment--部署对象表，存放流程定义的显示名和部署时间，每部署一次就增加一次记录
     *  act_re_procdef--流程定义表，存放流程定义的属性信息，部署每个新的流程定义都会在这张表中增加一条记录，注意，当流程定义的key相同情况下，使用的是版本升级
     *  act_ge_bytearray--资源文件表，存放流程相关的部署信息，即规则文件
     *  act_ge_property--主键生成策略表
     */
    @Test
    public void deploymentProcessDefinition1(){
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloWord.zip");
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("流程定义")//添加部署名称
                .addZipInputStream(new ZipInputStream(in))//从zip文件加载
                .deploy();//部署完成
        System.out.println("部署ID:"+deployment.getId());
        System.out.println("部署名字："+deployment.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void fingProcessDefinition(){
        //次结果集对应act_re_procdef表
        List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                        .createProcessDefinitionQuery()//创建一个流程定义查询
                        //.deploymentId()//使用部署对象ID查询
                        //.processDefinitionId()//使用流程定义ID查询
                        //.processDefinitionKey()//使用流程定义的key查询
                        //.processDefinitionNameLike()//使用流程定义的名称模糊查询
                    /**      排序        **/
                        //.orderByProcessDefinitionVersion().asc()//按照版本升序排序
                    /**      返回结果集        **/
                        .list();//返回一个集合列表，封装流程定义
                        //.singleResult();//返回唯一结果集
                        //.count()//结果集数量
                        //.listPage()//分页查询
        if (list != null && list.size() > 0) {
            for (ProcessDefinition p : list) {
                System.out.println("流程定义的ID："+ p.getId());//流程定义的key+版本+随机参数
                System.out.println("流程定义的名称："+ p.getName());//对应helloWoed.bpmn文件中的name属性值
                System.out.println("流程定义的key："+ p.getKey());//对应helloWoed.bpmn文件中的id属性值
                System.out.println("流程定义的版本："+ p.getVersion());//key相同情况下，版本升级
                System.out.println("资源名称的bpmn文件："+ p.getResourceName());
                System.out.println("资源名称的png文件："+ p.getDiagramResourceName());
                System.out.println("部署对象的ID："+ p.getDeploymentId());
                System.out.println("#######################");
            }
        }
    }

    /**
     * 查询最新版本的流程定义
     */
    @Test
    public void fingProcessDefinition1(){
        //次结果集对应act_re_procdef表
        List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createProcessDefinitionQuery()//创建一个流程定义查询
                .orderByProcessDefinitionVersion().asc()//升序排序
                .list();
        Map<String ,ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
        if(list != null && list.size()>0){
            for (ProcessDefinition pd : list) {
                map.put(pd.getKey(),pd);
            }
        }
        List<ProcessDefinition> pds = new ArrayList<ProcessDefinition>(map.values());
    }


    /**
     * 删除流程定义
     */
    @Test
    public void delProcessDefinition(){
        //使用部署ID删除
        String deploymentId = "1";
        /**
         * 不带级联删除，只能删除没有启动的流程，如果流程启动，抛出异常
         */
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
        /**
         * 级联删除，不管流程是否启动，都能启动
         */
        processEngine.getRepositoryService().deleteDeployment(deploymentId ,true);
    }

    /**
     * 查看流程图
     */
    @Test
    public void viewPio(){
        /**
         * 将生成的图片放到文件夹下
         */
        String deploymentId = "1";
        //获取图片资源名称
        List<String> list = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        String resourceName = "";
        if (list != null && list.size()>0){
            for(String name : list){
                if (name.indexOf(".png")>=0) {
                    resourceName = name;
                    break;
                }
            }
        }
        InputStream in = processEngine.getRepositoryService()
                        .getResourceAsStream(deploymentId ,resourceName);
        File file = new File("d:/"+resourceName);
        System.out.println("输出");
    }

    //注意：流程定义不能修改，只能修改流程图重新部署再启动，以前部署的流程可以用，知道没人使用才使用最新版本

}
