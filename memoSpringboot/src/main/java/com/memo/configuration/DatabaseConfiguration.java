package com.memo.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {
	@Autowired
	ApplicationContext applicationContext;
	
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	@Bean
	DataSource dataSource() {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		System.out.println(dataSource);
		return dataSource;
	}
	
	// SQLSession : MyBatis에서 데이터베이스와 상호작용(SQL 실행, 트랜잭션을 관리, ...)하는 인터페이스
    @Bean
    SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(
            applicationContext.getResources("classpath:/mapper/**/sql-*.xml")
        );
        
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sessionFactoryBean.setConfiguration(configuration);
        
        return sessionFactoryBean.getObject();
    }
    
    // SqlSessionTemplate: MyBatis와 스프링 프레임워크를 통합할 때 사용하는 클래스
    @Bean
    SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
    }
}
