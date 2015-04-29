package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DESTINO_ENCALHE")
public class DestinoEncalhe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -915449470804760670L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODIGO_DISTRIBUIDOR", nullable = false)	
	private String codigoDistribuidor;

	@Column(name = "SEMANA_RECOLHIMENTO", nullable = false)
	private String semanaRecolhimento;

	@Column(name = "SITUACAO_ATENDIMENTO_DDE", nullable = false)
	private String situacaoAtendimentoDDE;

	@Column(name = "CODIGO_PRODUTO", nullable = false)
	private String codigoProduto;

	@Column(name = "NUMERO_EDICAO", nullable = false)
	private Long numeroEdicao;
	
	@Column(name = "NOME_COMERCIAL", nullable = false)
	private String nomeComercial;

	@Column(name = "NOME_DESTINO_DDE", nullable = false)
	private String nomeDestinoDDE;

	@Column(name = "NUMERO_PRIORIDADE_ATENDIMENTO_DDE", nullable = false)
	private Long numeroPrioridadeAtendimentoDDE;

	@Column(name = "TIPO_ATENDIMENTO_DDE", nullable = false)
	private String tipoAtendimentoDDE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public String getSemanaRecolhimento() {
		return semanaRecolhimento;
	}

	public void setSemanaRecolhimento(String semanaRecolhimento) {
		this.semanaRecolhimento = semanaRecolhimento;
	}

	public String getSituacaoAtendimentoDDE() {
		return situacaoAtendimentoDDE;
	}

	public void setSituacaoAtendimentoDDE(String situacaoAtendimentoDDE) {
		this.situacaoAtendimentoDDE = situacaoAtendimentoDDE;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getNomeComercial() {
		return nomeComercial;
	}

	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}

	public String getNomeDestinoDDE() {
		return nomeDestinoDDE;
	}

	public void setNomeDestinoDDE(String nomeDestinoDDE) {
		this.nomeDestinoDDE = nomeDestinoDDE;
	}

	public Long getNumeroPrioridadeAtendimentoDDE() {
		return numeroPrioridadeAtendimentoDDE;
	}

	public void setNumeroPrioridadeAtendimentoDDE(Long numeroPrioridadeAtendimentoDDE) {
		this.numeroPrioridadeAtendimentoDDE = numeroPrioridadeAtendimentoDDE;
	}

	public String getTipoAtendimentoDDE() {
		return tipoAtendimentoDDE;
	}

	public void setTipoAtendimentoDDE(String tipoAtendimentoDDE) {
		this.tipoAtendimentoDDE = tipoAtendimentoDDE;
	}
	
}