package com.userTask;
/*
 *
 * @author LiuBing
 * @date 2018/6/8 8:44
 */

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListerImpl implements TaskListener{
    /**
     * 用来指定任务办理人
     * @param delegateTask
     */
    public void notify(DelegateTask delegateTask) {
        //指定个人任务的办理人，也可以指定组任务的办理人
        //通过类去查询数据库，将下一个任务的办理人查询获取，指定办理人
        delegateTask.setAssignee("灭绝师太");
    }
}
