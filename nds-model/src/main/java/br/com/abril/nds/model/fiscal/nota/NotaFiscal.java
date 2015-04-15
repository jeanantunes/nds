package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Entity
@Table(name = "NOTA_FISCAL_NOVO")
@XmlRootElement(name="NFe", namespace="http://www.portalfiscal.inf.br/nfe") 
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
	@GeneratedValue(strategy=GenerationType.AUTO)
	@NFEExport(secao = TipoSecao.B, posicao = 1, mascara="00000000")
	@XmlTransient
	private Long id;
	
	@Embedded
	@XmlElement(name="infNFe")
	private NotaFiscalInformacoes notaFiscalInformacoes;
	
	@XmlTransient
	@OneToOne
	@JoinColumn(name="USUARIO_ID", nullable=false)
	private Usuario usuario;
	
	
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getNotaFiscalInformacoes() == null) ? 0 :this.getNotaFiscalInformacoes().hashCode());
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
		NotaFiscal other = (NotaFiscal) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getNotaFiscalInformacoes() == null) {
			if (other.getNotaFiscalInformacoes() != null)
				return false;
		} else if (!this.getNotaFiscalInformacoes().equals(other.getNotaFiscalInformacoes()))
			return false;
		return true;
	}
}