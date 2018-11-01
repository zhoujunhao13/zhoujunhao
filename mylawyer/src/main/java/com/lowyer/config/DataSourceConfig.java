package com.lowyer.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@EnableAutoConfiguration
public class DataSourceConfig {
	@Autowired
	protected ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	@Bean(name="dataSource")
	@Qualifier("dataSource")
	@ConfigurationProperties(prefix="spring.datasource.druid")
	public DruidDataSource onDruidDataSource() {
		return new DruidDataSource();
	}
	
	@Autowired
	@Bean(name="sqlSessionFactory")
	@Qualifier("sqlSessionFactory")
	public SqlSessionFactoryBean onSqlsessionFactory(@Qualifier("dataSource") DataSource dataSource) {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		Resource resource = resourceLoader.getResource("classpath:mybatis-config.xml");
		factoryBean.setConfigLocation(resource);
		factoryBean.setDataSource(dataSource);
		return factoryBean;
	}
	
	@Bean(name="SqlSessionTemplate")
	@Qualifier("SqlSessionTemplate")
	public SqlSessionTemplate sessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}
	
	@Bean(name="trans")
	public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
