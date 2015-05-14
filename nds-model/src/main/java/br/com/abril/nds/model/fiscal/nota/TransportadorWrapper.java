package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="transporta")
public class TransportadorWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2313535449916955751L;
	
	/**
	 * CNPJ ou CPF
	 */
	@Column(name="DOCUMENTO_TRANS", nullable=true, length=14)
	@XmlElement(name="CNPJ")
	private String documento;	
	
	/**
	 * xNome
	 */
	@Column(name="NOME_TRANS", nullable=true, length=60)
	@NFEExport(secao = TipoSecao.X03, posicao = 0, tamanho = 60)
	@XmlElement(name="xNome")
	private String nome;
	
	/**
	 * IE
	 */
	@Column(name="IE_TRANS", nullable=true, length=14)
	@NFEExport(secao = TipoSecao.X03, posicao = 1, tamanho = 14)
	@XmlElement(name="IE")
	private String inscricaoEstadual;

	@XmlElement(name="xEnder")
	@Transient
	private String enderecoXML;
	
	@OneToOne(optional=true, fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="ENDERECO_ID_TRANS")
	@NFEExportType
	@XmlTransient
	private NotaFiscalEndereco endereco;
	
	/**
	 * xMun
	 */
	@Column(name="MUN_TRANS", nullable=true, length=60)
	@NFEExport(secao = TipoSecao.X03, posicao = 4, tamanho = 60)
	@XmlElement(name="xMun")
	private String municipio;
	
	/**
	 * UF
	 */
	@Column(name="UF_TRANS", nullable=true, length=2)
	@NFEExport(secao = TipoSecao.X03, posicao = 3, tamanho = 2)
	@XmlElement(name="UF")
	private String uf;
	
	@OneToOne(mappedBy="notaFiscal")
	@NFEExportType
	private RetencaoICMSTransporte retencaoICMS;
	
	@Embedded
	@NFEExportType
	@XmlTransient
	private Veiculo veiculo;

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
	 * @return the inscricaoEstadual
	 */
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	/**
	 * @param inscricaoEstadual the inscricaoEstadual to set
	 */
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	/**
	 * Obtém endereco
	 *
	 * @return Endereco
	 */
	public NotaFiscalEndereco getEndereco() {
		return endereco;
	}

	/**
	 * Atribuí endereco
	 * @param endereco 
	 */
	public void setEndereco(NotaFiscalEndereco endereco) {
		this.endereco = endereco;
		
		if(endereco.getLogradouro() != null && endereco.getNumero() != null && endereco.getComplemento() != null && endereco.getBairro() != null){
			this.enderecoXML = (endereco != null ? endereco.getLogradouro().concat(" ").concat(endereco.getNumero()).concat(" ").concat(endereco.getComplemento()).concat(endereco.getBairro()) : null);
		}
		
	}

	/**
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}

	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}

	/**
	 * @return the retencaoICMS
	 */
	public RetencaoICMSTransporte getRetencaoICMS() {
		return retencaoICMS;
	}

	/**
	 * @param retencaoICMS the retencaoICMS to set
	 */
	public void setRetencaoICMS(RetencaoICMSTransporte retencaoICMS) {
		this.retencaoICMS = retencaoICMS;
	}

	/**
	 * @return the veiculo
	 */
	public Veiculo getVeiculo() {
		return veiculo;
	}

	/**
	 * @param veiculo the veiculo to set
	 */
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	/**
	 * @return the documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

}
