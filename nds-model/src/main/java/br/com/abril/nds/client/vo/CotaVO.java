package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object para cotas.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class CotaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7996603170304278590L;

	private Long idCota;
	
	@Export(label = "Código", exhibitionOrder = 0)
	private Integer numero;
	
	@Export(label = "Nome / Razão Social", exhibitionOrder = 1)
	private String nome;
	
	private BigDecimal qtdeReparte;
	
	private BigDecimal qtdeRateio;
	
	private String codigoBox;
	
	@Export(label = "CPF / CNPJ", exhibitionOrder = 2)
	private String numeroCpfCnpj;
	
	@Export(label = "Contato", exhibitionOrder = 4)
	private String contato;
	
	@Export(label = "Telefone", exhibitionOrder = 5)
	private String telefone;
	
	@Export(label = "E-Mail", exhibitionOrder = 6)
	private String email;
	
	@Export(label = "Status", exhibitionOrder = 7)
	private String status;
	
	@Export(label = "Box", exhibitionOrder = 3)
	private String descricaoBox;
	
	/**
	 * Construtor padrão.
	 */
	public CotaVO() {
		
		
	}
	
	
	
	public Long getIdCota() {
		return idCota;
	}



	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}



	/**
	 * Construtor.
	 * 
	 * @param numero - número da cota
	 * @param nome - nome da cota
	 */
	public CotaVO(Integer numero, String nome) {
		
		this.numero = numero;
		this.nome = nome;
	}

	/**
	 * @return the numero
	 */
	public Integer getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the qtdeReparte
	 */
	public BigDecimal getQtdeReparte() {
		return qtdeReparte;
	}

	/**
	 * @param qtdeReparte the qtdeReparte to set
	 */
	public void setQtdeReparte(BigDecimal qtdeReparte) {
		this.qtdeReparte = qtdeReparte;
	}

	/**
	 * @return the qtdeRateio
	 */
	public BigDecimal getQtdeRateio() {
		return qtdeRateio;
	}

	/**
	 * @param qtdeRateio the qtdeRateio to set
	 */
	public void setQtdeRateio(BigDecimal qtdeRateio) {
		this.qtdeRateio = qtdeRateio;
	}
	
	/**
	 * @return the codigoBox
	 */
	public String getCodigoBox() {
		return codigoBox;
	}

	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}

	public String getNumeroCpfCnpj() {
		return numeroCpfCnpj;
	}

	public void setNumeroCpfCnpj(String numeroCpfCnpj) {
		this.numeroCpfCnpj = numeroCpfCnpj;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescricaoBox() {
		return descricaoBox;
	}

	public void setDescricaoBox(String descricaoBox) {
		this.descricaoBox = descricaoBox;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result
				+ ((qtdeRateio == null) ? 0 : qtdeRateio.hashCode());
		result = prime * result
				+ ((qtdeReparte == null) ? 0 : qtdeReparte.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CotaVO other = (CotaVO) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (qtdeRateio == null) {
			if (other.qtdeRateio != null)
				return false;
		} else if (!qtdeRateio.equals(other.qtdeRateio))
			return false;
		if (qtdeReparte == null) {
			if (other.qtdeReparte != null)
				return false;
		} else if (!qtdeReparte.equals(other.qtdeReparte))
			return false;
		return true;
	}
	
}
