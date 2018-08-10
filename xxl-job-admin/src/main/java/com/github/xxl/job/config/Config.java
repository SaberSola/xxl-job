package com.github.xxl.job.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@ComponentScan(
        {
                "com.xxl.job.admin.core.conf",
                "com.xxl.job.admin.service",
                "com.xxl.job.admin.dao",
                "com.xxl.job.admin.controller",
                "com.github.xxl.job.config"
        })
public class Config {


    /**
     * 配置数据源
     */

    @Bean
    public DataSource dataSouce(XxlDBConfig xxlDBConfig) throws Exception{

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(xxlDBConfig.getDriverClass());
        dataSource.setJdbcUrl(xxlDBConfig.getUrl());
        dataSource.setUser(xxlDBConfig.getUser());
        dataSource.setPassword(xxlDBConfig.getPassword());
        dataSource.setInitialPoolSize(xxlDBConfig.getInitialPoolSize());
        dataSource.setMaxPoolSize(xxlDBConfig.getMaxPoolSize());
        dataSource.setMinPoolSize(xxlDBConfig.getMinPoolSize());
        dataSource.setMaxIdleTime(xxlDBConfig.getMaxIdleTime());
        dataSource.setAcquireRetryDelay(xxlDBConfig.getAcquireRetryDelay());
        dataSource.setAcquireRetryAttempts(xxlDBConfig.getAcquireRetryAttempts());
        dataSource.setPreferredTestQuery(xxlDBConfig.getPreferredTestQuery());
        return dataSource;
    }

    /**
     *
     * 配置SqlSessionFactoryBean
     */
     @Bean
     public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException{

         SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
         //设置数据源
         sessionFactoryBean.setDataSource(dataSource);
         Resource[] resources = new PathMatchingResourcePatternResolver()
                 .getResources("classpath:mybatis-mapper/*.xml");

         sessionFactoryBean.setMapperLocations(resources);
         return sessionFactoryBean;
     }

    /**
     *
     * 配置MapperScanner
     */

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.xxl.job.admin.dao");
        return mapperScannerConfigurer;
    }

    /**
     *
     * 配置事务manger
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        //设置数据源
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
    /**
     *
     * 配置quartzSed
     */

    @Bean
    public SchedulerFactoryBean quartzScheduler(DataSource dataSource){

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setStartupDelay(20);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContextKey");
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        return schedulerFactoryBean;
    }

    /**
     *
     * 初始化  XxlJobDynamicScheduler
     */

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public XxlJobDynamicScheduler xxlJobDynamicScheduler(SchedulerFactoryBean quartzScheduler, XxlConfig xxlConfig){
        XxlJobDynamicScheduler xxlJobDynamicScheduler = new XxlJobDynamicScheduler();
        xxlJobDynamicScheduler.setAccessToken(xxlConfig.getAccessToken());
        xxlJobDynamicScheduler.setScheduler(quartzScheduler.getScheduler());
        return xxlJobDynamicScheduler;
    }

}
