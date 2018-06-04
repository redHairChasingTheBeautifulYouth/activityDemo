
/*
 *
 * @author LiuBing
 * @date 2018/6/4 18:25
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class TestActiviti {
    /**
     * 使用代码方式创建23张表
     */
    @Test
    public void createTable(){
        ProcessEngineConfiguration p = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        p.setJdbcDriver("com.mysql.jdbc.Driver");
        p.setJdbcUrl("jdbc:mysql://localhost:3306/activitydemo?useUnicode=true&characterEncoding=utf8");
        p.setJdbcUsername("root");
        p.setJdbcPassword("root");
        p.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine pe = p.buildProcessEngine();

    }

    /**
     * 使用配置文件创建23张表
     */
    @Test
    public void createTable1(){
        ProcessEngine p = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
    }
}
