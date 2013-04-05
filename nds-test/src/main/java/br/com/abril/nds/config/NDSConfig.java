package br.com.abril.nds.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.process.correcaovendas.CorrecaoIndividual;

@Configuration
@Import(NDSPropertyConfig.class)
public class NDSConfig {

    @Bean
    public CorrecaoIndividual getCorrecaoIndividual() {
	return new CorrecaoIndividual();
    }

    @Bean
    public CotaDAO getCotaDAO() {

	CotaDAO cotaDAO = new CotaDAO();

	return cotaDAO;
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {

	NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	try {

	    File pathFile = new File(NDSConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
	    String parentPath = pathFile.getParentFile().getPath();
	    String decodedParent = URLDecoder.decode(parentPath, "UTF-8");
	    File fileAppllicationContextXML = new File(decodedParent + "\\resources\\NDSTest.properties");

	    if (fileAppllicationContextXML.exists()) {

		FileInputStream fileInputStreamNDSContext = new FileInputStream(fileAppllicationContextXML);

		if (fileInputStreamNDSContext != null) {

		    Properties propertiesNDSTest = new Properties();
		    propertiesNDSTest.load(fileInputStreamNDSContext);

		    String url = propertiesNDSTest.getProperty("url");
		    // String driverClassName = propertiesNDSTest.getProperty("driverClassName");
		    // String username = propertiesNDSTest.getProperty("username");
		    // String password = propertiesNDSTest.getProperty("password");

		    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(url, propertiesNDSTest);

		    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(driverManagerDataSource);

		}
	    }

	} catch (URISyntaxException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return namedParameterJdbcTemplate;
    }

}
