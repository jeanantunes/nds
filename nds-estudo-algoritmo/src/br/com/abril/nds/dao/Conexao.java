package br.com.abril.nds.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

	private static Connection con;
	
	public static Connection getConexao() throws ClassNotFoundException, SQLException {
		if (con == null) {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nds-client-homolog", "root", "root");
		}
		return con;
	}
}
