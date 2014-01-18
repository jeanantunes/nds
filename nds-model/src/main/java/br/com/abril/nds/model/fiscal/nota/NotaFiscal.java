package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Entity
@Table(name = "NOTA_FISCAL_NOVO")
@SequenceGenerator(name = "NOTA_FISCAL_SEQ", initialValue = 1, allocationSize = 1)
@XmlRootElement(name="NFe", namespace="http://www.portalfiscal.inf.br/nfe") 
@XmlType(name="NotaFiscalNds")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotaFiscal implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -580456077685816545L;
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(generator = "NOTA_FISCAL_SEQ")
	@NFEExport(secao = TipoSecao.B, posicao = 1, mascara="00000000")
	@XmlTransient
	private Long id;
	
	@Embedded
	@XmlElement(name="infNFe")
	private NotaFiscalInformacoes notaFiscalInformacoes;
	
	
	/**
	 * Construtor padrão.
	 */
	public NotaFiscal() {
		
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public NotaFiscalInformacoes getNotaFiscalInformacoes() {
		return notaFiscalInformacoes;
	}

	public void setNotaFiscalInformacoes(NotaFiscalInformacoes notaFiscalInformacoes) {
		this.notaFiscalInformacoes = notaFiscalInformacoes;
	}
	
/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result
				+ ((this.getProcessos() == null) ? 0 : this.getProcessos().hashCode());
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
		NotaFiscalInformacoes other = (NotaFiscalInformacoes) obj;
		if (this.getId() == null) {
			if (other.id != null)
				return false;
		} else if (!this.getId().equals(other.id))
			return false;
		if (this.notaFiscalInformacoes.getProcessos() == null) {
			if (other.processos != null)
				return false;
		} else if (!this.getProcessos().equals(other.processos))
			return false;
		return true;
	}
*/
	
}