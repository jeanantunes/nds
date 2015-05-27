package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;

@Entity
@Table(name="CONFERENCIA_ENCALHE_BACKUP")
@SequenceGenerator(name="CONFERENCIA_ENCALHE_BACKUP_SEQ", initialValue = 1, allocationSize = 1)
public class ConferenciaEncalheBackup implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CONFERENCIA_ENCALHE_BACKUP_SEQ")
	private Long id;
	
	@Column(name="ID_CONFERENCIA_ORIGINAL")
	private Long idConferenciaOriginal;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_OPERACAO")
	private Date dataOperacao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CHAMADA_ENCALHE_COTA_ID")
	private ChamadaEncalheCota chamadaEncalheCota;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_CRIACAO")
	private Date dataCriacao;
	
	@Column(name="DIA_RECOLHIMENTO")
	private Integer diaRecolhimento;
	
	@Column(name="JURAMENTADA")
	private boolean juramentada;
	
	@Column(name="OBSERVACAO")
	private String observacao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COTA_ID")
	private Cota cota;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QTDE")
	private BigInteger qtde;

	@Column(name="QTDE_INFORMADA")
	private BigInteger qtdeInformada;
	
	@Column(name="PRECO_CAPA_INFORMADO")
	private BigDecimal precoCapaInformado;
	
	@Column(name="PRECO_CAPA")
	private BigDecimal precoCapa;
	
	@Column(name="PRECO_COM_DESCONTO")
	private BigDecimal precoComDesconto;
	
	@Column(name = "PROCESSO_UTILIZA_NFE", nullable = false)
	private boolean processoUtilizaNfe;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public ChamadaEncalheCota getChamadaEncalheCota() {
		return chamadaEncalheCota;
	}

	public void setChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota) {
		this.chamadaEncalheCota = chamadaEncalheCota;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public boolean isJuramentada() {
		return juramentada;
	}

	public void setJuramentada(boolean juramentada) {
		this.juramentada = juramentada;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public BigInteger getQtdeInformada() {
		return qtdeInformada;
	}

	public void setQtdeInformada(BigInteger qtdeInformada) {
		this.qtdeInformada = qtdeInformada;
	}

	public BigDecimal getPrecoCapaInformado() {
		return precoCapaInformado;
	}

	public void setPrecoCapaInformado(BigDecimal precoCapaInformado) {
		this.precoCapaInformado = precoCapaInformado;
	}

	public Integer getDiaRecolhimento() {
		return diaRecolhimento;
	}

	public void setDiaRecolhimento(Integer diaRecolhimento) {
		this.diaRecolhimento = diaRecolhimento;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	public Long getIdConferenciaOriginal() {
		return idConferenciaOriginal;
	}

	public void setIdConferenciaOriginal(Long idConferenciaOriginal) {
		this.idConferenciaOriginal = idConferenciaOriginal;
	}

	public boolean isProcessoUtilizaNfe() {
		return processoUtilizaNfe;
	}

	public void setProcessoUtilizaNfe(boolean processoUtilizaNfe) {
		this.processoUtilizaNfe = processoUtilizaNfe;
	}
}
