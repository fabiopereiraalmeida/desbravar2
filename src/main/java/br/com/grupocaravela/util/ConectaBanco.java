package br.com.grupocaravela.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.com.grupocaravela.configuracao.Empresa;

public class ConectaBanco {
	private static final String URL_MYSQL = "jdbc:mysql://" + Empresa.getIpServidor() + "/desbravar";
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String USER = "root";
	private static final String PASS = "peperoni";

	public static Connection getConnection() {
		System.out.println("Conectando ao Banco de Dados");
		try {
			Class.forName(DRIVER_CLASS);
			return DriverManager.getConnection(URL_MYSQL, USER, PASS);
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
