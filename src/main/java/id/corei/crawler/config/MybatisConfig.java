package id.corei.crawler.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MybatisConfig {

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage("id.corei.crawler.model");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:id/corei/crawler/model/mapper/**/*.xml"));
        return (SqlSessionFactory) sqlSessionFactory.getObject();
    }

    @Bean("mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() throws Exception{
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("id.corei.crawler.model.mapper");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");

        return mapperScannerConfigurer;
    }
}
