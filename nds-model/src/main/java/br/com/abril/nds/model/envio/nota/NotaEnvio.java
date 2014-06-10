/**
 * 
 */
package br.com.abril.nds.model.envio.nota;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Diego Fernandes
 * 
 */
@Entity
@Table(name = "NOTA_ENVIO")
@SequenceGenerator(name = "NOTA_ENVIO_SEQ", initialValue = 1, allocationSize = 1)
public class NotaEnvio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -194455126920689804L;

	@Id
	@GeneratedValue(generator = "NOTA_ENVIO_SEQ")
	private Long numero;

	@Column(name = "CODIGO_NATUREZA_OPERACAO")
	private Integer codigoNaturezaOperacao;
	@Column(name = "DESCRICAO_NATUREZA_OPERACAO")
	private String descricaoNaturezaOperacao;

	@Column(name = "CHAVE_ACESSO", length = 44, nullable = true)
	private String chaveAcesso;
	
	@Temporal(TemporalType.DATE)
	private Date dataEmissao;

	@Column(name = "NOTA_IMPRESSA", nullable = false)
	private boolean notaImpressa;
	
	@Embedded
	private IdentificacaoEmitente emitente;
	
	@Embedded
	private IdentificacaoDestinatario destinatario;

	@OneToMany(mappedBy = "itemNotaEnvioPK.notaEnvio")
	private List<ItemNotaEnvio> listaItemNotaEnvio;

	/**
	 * @return the numero
	 */
	public Long getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(Long numero) {
		this.numero = numero;
	}

	/**
	 * @return the codigoNaturezaOperacao
	 */
	public Integer getCodigoNaturezaOperacao() {
		return codigoNaturezaOperacao;
	}

	/**
	 * @param codigoNaturezaOperacao the codigoNaturezaOperacao to set
	 */
	public void setCodigoNaturezaOperacao(Integer codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	/**
	 * @return the descricaoNaturezaOperacao
	 */
	public String getDescricaoNaturezaOperacao() {
		return descricaoNaturezaOperacao;
	}

	/**
	 * @param descricaoNaturezaOperacao the descricaoNaturezaOperacao to set
	 */
	public void setDescricaoNaturezaOperacao(String descricaoNaturezaOperacao) {
		this.descricaoNaturezaOperacao = descricaoNaturezaOperacao;
	}

	/**
	 * @return the chaveAcesso
	 */
	public String getChaveAcesso() {
		return chaveAcesso;
	}

	/**
	 * @param chaveAcesso the chaveAcesso to set
	 */
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	/**
	 * @return the emitente
	 */
	public IdentificacaoEmitente getEmitente() {
		return emitente;
	}

	/**
	 * @param emitente the emitente to set
	 */
	public void setEmitente(IdentificacaoEmitente emitente) {
		this.emitente = emitente;
	}

	/**
	 * @return the destinatario
	 */
	public IdentificacaoDestinatario getDestinatario() {
		return destinatario;
	}

	/**
	 * @param destinatario the destinatario to set
	 */
	public void setDestinatario(IdentificacaoDestinatario destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * @return the listaItemNotaEnvio
	 */
	public List<ItemNotaEnvio> getListaItemNotaEnvio() {
		return listaItemNotaEnvio;
	}

	/**
	 * @param listaItemNotaEnvio the listaItemNotaEnvio to set
	 */
	public void setListaItemNotaEnvio(List<ItemNotaEnvio> listaItemNotaEnvio) {
		this.listaItemNotaEnvio = listaItemNotaEnvio;
	}

	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	/**
	 * @return
	 */
	public boolean isNotaImpressa() {
		return notaImpressa;
	}

	/**
	 * @param notaImpressa
	 */
	public void setNotaImpressa(boolean notaImpressa) {
		this.notaImpressa = notaImpressa;
	}

}
