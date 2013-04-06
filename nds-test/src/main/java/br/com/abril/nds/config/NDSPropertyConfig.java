package br.com.abril.nds.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import br.com.abril.nds.util.PropertyLoader;

@Configuration
// @PropertySource("classpath:query-estudo.properties")
public class NDSPropertyConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer query_estudo() {

	PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
	propertySourcesPlaceholderConfigurer.setProperties(PropertyLoader.getPropertiesQueryEstudo());

	PropertiesFactoryBean propertiesFactoryBean = PropertyLoader.getPropertiesFactoryBean();

	return propertySourcesPlaceholderConfigurer;
    }

    @Bean
    public String queryProdutoEdicaoPorCota() {

	String queryProdutoEdicaoPorCota = PropertyLoader.getProperty("queryProdutoEdicaoPorCota");

	return queryProdutoEdicaoPorCota;
    }
}
