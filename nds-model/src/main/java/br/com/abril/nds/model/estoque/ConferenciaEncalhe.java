package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;

/**
 * Entidade de abstrai os movimentos da cota especificos de envio de encalhe 
 * e auxilia a identificar com qual dataRecolhimento original o movimento esta 
 * relacionado (devido a possibilidade de um envio de encalhe antecipado).
 * 
 * @author Discover Technology
 *
 */
@Entity
@Table(name = "CONFERENCIA_ENCALHE")
@SequenceGenerator(name="CONFERENCIA_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class ConferenciaEncalhe implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	
	
	@Id
	@GeneratedValue(generator = "CONFERENCIA_ENCALHE_SEQ")
	private Long id;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_ID")
	private MovimentoEstoqueCota movimentoEstoqueCota;

	@OneToOne(optional = true)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_ID")
	private MovimentoEstoque movimentoEstoque;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "CHAMADA_ENCALHE_COTA_ID")
	private ChamadaEncalheCota chamadaEncalheCota;

	@ManyToOne(optional = true)
	@JoinColumn(name = "CONTROLE_CONFERENCIA_ENCALHE_COTA_ID")
	private ControleConferenciaEncalheCota controleConferenciaEncalheCota;

	@Column(name = "DIA_RECOLHIMENTO")
	private Integer diaRecolhimento;
	
	@Column(name = "OBSERVACAO")
	private String observacao;
	
	@Column(name = "JURAMENTADA")
	private boolean juramentada;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Date data;

	/**
	 * Quantidade relativa ao item de nota fiscal de entrada
	 * fornecida pela cota na operação de conferência de encalhe.
	 */
	@Column(name = "QTDE_INFORMADA")
	private BigInteger qtdeInformada;
	
	/**
	 * Preco capa relativo ao item de nota fiscal de entrada
	 * fornecida pela cota na operação de conferência de encalhe.
	 */
	@Column(name = "PRECO_CAPA_INFORMADO")
	private BigDecimal precoCapaInformado;
	
	/**
	 * Preco ccom desconto relativo ao item de nota fiscal de entrada
	 * fornecida pela cota na operação de conferência de encalhe.
	 */
	@Column(name = "PRECO_COM_DESCONTO")
	private BigDecimal precoComDesconto;
	
	/**
	 * Quantidade replicada na coluna qtde em movimento_estoque_cota 
	 * relativa a conferência de encalhe
	 */
	@Column(name = "QTDE")
	private BigInteger qtde;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;

	/**
	 * Obtém id
	 *
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribuí id
	 * @param id 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém movimentoEstoqueCota
	 *
	 * @return MovimentoEstoqueCota
	 */
	public MovimentoEstoqueCota getMovimentoEstoqueCota() {
		return movimentoEstoqueCota;
	}

	/**
	 * Atribuí movimentoEstoqueCota
	 * @param movimentoEstoqueCota 
	 */
	public void setMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota) {
		this.movimentoEstoqueCota = movimentoEstoqueCota;
	}

	/**
	 * Obtém movimentoEstoque
	 *
	 * @return MovimentoEstoque
	 */
	public MovimentoEstoque getMovimentoEstoque() {
		return movimentoEstoque;
	}

	/**
	 * Atribuí movimentoEstoque
	 * @param movimentoEstoque 
	 */
	public void setMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		this.movimentoEstoque = movimentoEstoque;
	}

	/**
	 * Obtém chamadaEncalheCota
	 *
	 * @return ChamadaEncalheCota
	 */
	public ChamadaEncalheCota getChamadaEncalheCota() {
		return chamadaEncalheCota;
	}

	/**
	 * Atribuí chamadaEncalheCota
	 * @param chamadaEncalheCota 
	 */
	public void setChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota) {
		this.chamadaEncalheCota = chamadaEncalheCota;
	}

	/**
	 * Obtém controleConferenciaEncalheCota
	 *
	 * @return ControleConferenciaEncalheCota
	 */
	public ControleConferenciaEncalheCota getControleConferenciaEncalheCota() {
		return controleConferenciaEncalheCota;
	}

	/**
	 * Atribuí controleConferenciaEncalheCota
	 * @param controleConferenciaEncalheCota 
	 */
	public void setControleConferenciaEncalheCota(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		this.controleConferenciaEncalheCota = controleConferenciaEncalheCota;
	}

	/**
	 * Obtém observacao
	 *
	 * @return String
	 */
	public String getObservacao() {
		return observacao;
	}

	/**
	 * Atribuí observacao
	 * @param observacao 
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * Obtém juramentada
	 *
	 * @return boolean
	 */
	public boolean isJuramentada() {
		return juramentada;
	}

	/**
	 * Atribuí juramentada
	 * @param juramentada 
	 */
	public void setJuramentada(boolean juramentada) {
		this.juramentada = juramentada;
	}

	/**
	 * Obtém data
	 *
	 * @return Date
	 */
	public Date getData() {
		return data;
	}

	/**
	 * Atribuí data
	 * @param data 
	 */
	public void setData(Date data) {
		this.data = data;
	}
	
	/**
	 * Obtém qtdeInformada
	 *
	 * @return BigInteger
	 */
	public BigInteger getQtdeInformada() {
		return qtdeInformada;
	}

	/**
	 * Atribuí qtdeInformada
	 * @param qtdeInformada 
	 */
	public void setQtdeInformada(BigInteger qtdeInformada) {
		this.qtdeInformada = qtdeInformada;
	}

	/**
	 * Obtém precoCapaInformado
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getPrecoCapaInformado() {
		return precoCapaInformado;
	}
	
	/**
	 * Atribuí precoCapaInformado
	 * @param precoCapaInformado 
	 */
	public void setPrecoCapaInformado(BigDecimal precoCapaInformado) {
		this.precoCapaInformado = precoCapaInformado;
	}
	
	/**
	 * Obtém precoComDesconto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	
	/**
	 * Atribuí precoComDesconto
	 * @return BigDecimal
	 */
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	/**
	 * Obtém qtde
	 *
	 * @return BigInteger
	 */
	public BigInteger getQtde() {
		return qtde;
	}

	/**
	 * Atribuí qtde
	 * @param qtde 
	 */
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	/**
	 * Obtém produtoEdicao
	 *
	 * @return ProdutoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * Atribuí produtoEdicao
	 * @param produtoEdicao 
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public Integer getDiaRecolhimento() {
		return diaRecolhimento;
	}

	public void setDiaRecolhimento(Integer diaRecolhimento) {
		this.diaRecolhimento = diaRecolhimento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		ConferenciaEncalhe other = (ConferenciaEncalhe) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
	
}
