package br.com.abril.nds.integracao.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
// Não descomente isso porque nós trocamos a estrategia de como setar os parametros do banco
//@PropertySource("classpath:ndsi-database.properties")
public class NdasiDatabaseConfig {

   @Bean
   public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
      return new PropertySourcesPlaceholderConfigurer();
   }
}