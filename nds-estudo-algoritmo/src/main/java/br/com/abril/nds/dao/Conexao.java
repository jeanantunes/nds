package br.com.abril.nds.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {

    private static Connection con;

    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        
        if (con == null) {
            
            try {
                
                String jdbcDriverClassName = "com.mysql.jdbc.Driver";
                String jdbcUrl = "jdbc:mysql://localhost:3306/nds-client";
                String user = "root";
                String password = "root";
                
                File jarFile = new File(Conexao.class.getProtectionDomain().getCodeSource().getLocation().toURI()
                        .getPath());
                String parentJarDir = jarFile.getParentFile().getParent();
                String decodedParentJarDir = URLDecoder.decode(parentJarDir, "UTF-8");
                
                File fileProperty = new File(decodedParentJarDir + "\\resources\\NDSTest.properties");
                
                if (fileProperty.exists()) {
                    FileInputStream fileInputStreamNDSTest = new FileInputStream(fileProperty);
                    
                    if (fileInputStreamNDSTest != null) {
                        
                        Properties propertiesNDSTest = new Properties();
                        propertiesNDSTest.load(fileInputStreamNDSTest);
                        fileInputStreamNDSTest.close();
                        
                        jdbcDriverClassName = propertiesNDSTest.getProperty("database.jdbcDriverClassName");
                        jdbcUrl = propertiesNDSTest.getProperty("database.jdbcUrl");
                        user = propertiesNDSTest.getProperty("database.user");
                        password = propertiesNDSTest.getProperty("database.password");
                    }
                }
                
                Class.forName(jdbcDriverClassName);
                con = DriverManager.getConnection(jdbcUrl, user, password);
                
            } catch (URISyntaxException e) {
                throw new SQLException(e);
            } catch (UnsupportedEncodingException e) {
                throw new SQLException(e);
            } catch (FileNotFoundException e) {
                throw new SQLException(e);
            } catch (IOException e) {
                throw new SQLException(e);
            }
        }
        
        return con;
    }
}
