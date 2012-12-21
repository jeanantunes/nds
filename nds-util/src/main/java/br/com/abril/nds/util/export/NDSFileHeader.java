package br.com.abril.nds.util.export;

import java.io.InputStream;
import java.util.Date;

/**
 * Classe que representa o cabeçalho dos arquivos exportados
 * pelo sistema.
 * 
 * @author Discover Technology
 *
 */
public class NDSFileHeader {
	
	private String nomeDistribuidor;
	
	private String cnpjDistribuidor;
	
	private Date data;
	
	private String nomeUsuario;
	
	private InputStream logo;
	
	/**
	 * Construtor padrão.
	 */
	public NDSFileHeader() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param nomeDistribuidor - nome do distribuidor
	 * @param cnpjDistribuidor - CNPJ do distribuidor
	 * @param data - data
	 * @param nomeUsuario - nome do usuário
	 */
	public NDSFileHeader(String nomeDistribuidor, 
					 String cnpjDistribuidor, 
					 Date data, 
					 String nomeUsuario) {
		
		this.nomeDistribuidor = nomeDistribuidor;
		this.cnpjDistribuidor = cnpjDistribuidor;
		this.data = data;
		this.nomeUsuario = nomeUsuario;
	}

	/**
	 * @return the nomeDistribuidor
	 */
	public String getNomeDistribuidor() {
		return nomeDistribuidor;
	}

	/**
	 * @param nomeDistribuidor the nomeDistribuidor to set
	 */
	public void setNomeDistribuidor(String nomeDistribuidor) {
		this.nomeDistribuidor = nomeDistribuidor;
	}

	/**
	 * @return the cnpjDistribuidor
	 */
	public String getCnpjDistribuidor() {
		return cnpjDistribuidor;
	}

	/**
	 * @param cnpjDistribuidor the cnpjDistribuidor to set
	 */
	public void setCnpjDistribuidor(String cnpjDistribuidor) {
		this.cnpjDistribuidor = cnpjDistribuidor;
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return the nomeUsuario
	 */
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	/**
	 * @param nomeUsuario the nomeUsuario to set
	 */
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public InputStream getLogo() {
		return logo;
	}

	public void setLogo(InputStream logo) {
		this.logo = logo;
	}
	
	

}
