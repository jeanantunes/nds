package br.com.abril.nds.integracao.couchdb;

import java.io.Serializable;

public class CouchDbProperties implements Serializable {

	private static final long serialVersionUID = 7368140699846048088L;
	
	private String protocol;
	private String host;
	private Integer port;
	private String username;
	private String password;
	private Integer bachSize;
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setBachSize(Integer bachSize) {
		this.bachSize = bachSize;
	}
	public Integer getBachSize() {
		return bachSize;
	}
}
