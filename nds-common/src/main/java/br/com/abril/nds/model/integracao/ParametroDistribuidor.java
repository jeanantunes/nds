package br.com.abril.nds.model.integracao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PARAMETRO_DISTRIBUIDOR")
@SequenceGenerator(name="PARAMETRO_DISTRIBUIDOR_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroDistribuidor {

	
	@Id
	@GeneratedValue(generator = "PARAMETRO_DISTRIBUIDOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGO_DINAP", nullable = false)
	private String codigoDinap;
	
	@Column(name = "CODIGO_FC", nullable = false)
	private String codigoFC;
	
	@Column(name = "NOME_FANTASIA", nullable = false)
	private String nomeFantasia;
	
	
	@Column(name = "RAZAO_SOCIAL", nullable = false)
	private String razaoSocial;
	
	
	@Column(name = "CNPJ", nullable = false)
	private String cnpj;

	
	@Column(name = "TIPO_DISTRIBUIDOR", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoDistribuidor tipoDistribuidor;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getCodigoDinap() {
		return codigoDinap;
	}


	public void setCodigoDinap(String codigoDinap) {
		this.codigoDinap = codigoDinap;
	}


	public String getCodigoFC() {
		return codigoFC;
	}


	public void setCodigoFC(String codigoFC) {
		this.codigoFC = codigoFC;
	}


	public String getNomeFantasia() {
		return nomeFantasia;
	}


	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}


	public String getRazaoSocial() {
		return razaoSocial;
	}


	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}


	public String getCnpj() {
		return cnpj;
	}


	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}


	public TipoDistribuidor getTipoDistribuidor() {
		return tipoDistribuidor;
	}


	public void setTipoDistribuidor(TipoDistribuidor tipoDistribuidor) {
		this.tipoDistribuidor = tipoDistribuidor;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result
				+ ((codigoDinap == null) ? 0 : codigoDinap.hashCode());
		result = prime * result
				+ ((codigoFC == null) ? 0 : codigoFC.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result
				+ ((razaoSocial == null) ? 0 : razaoSocial.hashCode());
		result = prime
				* result
				+ ((tipoDistribuidor == null) ? 0 : tipoDistribuidor.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParametroDistribuidor other = (ParametroDistribuidor) obj;
		if (cnpj == null) {
			if (other.cnpj != null)
				return false;
		} else if (!cnpj.equals(other.cnpj))
			return false;
		if (codigoDinap == null) {
			if (other.codigoDinap != null)
				return false;
		} else if (!codigoDinap.equals(other.codigoDinap))
			return false;
		if (codigoFC == null) {
			if (other.codigoFC != null)
				return false;
		} else if (!codigoFC.equals(other.codigoFC))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomeFantasia == null) {
			if (other.nomeFantasia != null)
				return false;
		} else if (!nomeFantasia.equals(other.nomeFantasia))
			return false;
		if (razaoSocial == null) {
			if (other.razaoSocial != null)
				return false;
		} else if (!razaoSocial.equals(other.razaoSocial))
			return false;
		if (tipoDistribuidor != other.tipoDistribuidor)
			return false;
		return true;
	}


		
}
