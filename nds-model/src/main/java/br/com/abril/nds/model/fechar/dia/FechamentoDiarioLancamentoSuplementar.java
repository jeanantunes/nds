package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "FECHAMENTO_DIARIO_LANCAMENTO_SUPLEMENTAR")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_LANCAMENTO_SUPLEMENTAR_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioLancamentoSuplementar implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "FECHAMENTO_DIARIO_LANCAMENTO_SUPLEMENTAR_SEQ")
	@Column(name = "ID")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID")
	private FechamentoDiarioConsolidadoSuplementar fechamentoDiarioConsolidadoSuplementar;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;

	@Column(name = "QNT_CONTABILIZADA")
	private BigInteger quantidadeContabilizada;
	
	@Column(name = "QNT_LOGICO")
	private BigInteger quantidadeLogico;
	
	@Column(name = "QNT_VENDA")
	private BigInteger quantidadeVenda;
	
	@Column(name = "QNT_TRANSFERENCIA_ENTRADA")
	private BigInteger quantidadeTransferenciaEntrada;
	
	@Column(name = "QNT_TRANSFERENCIA_SAIDA")
	private BigInteger quantidadeTransferenciaSaida; 
	
	@Column(name = "SALDO")
	private BigInteger saldo;  

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FechamentoDiarioConsolidadoSuplementar getFechamentoDiarioConsolidadoSuplementar() {
		return fechamentoDiarioConsolidadoSuplementar;
	}

	public void setFechamentoDiarioConsolidadoSuplementar(
			FechamentoDiarioConsolidadoSuplementar fechamentoDiarioConsolidadoSuplementar) {
		this.fechamentoDiarioConsolidadoSuplementar = fechamentoDiarioConsolidadoSuplementar;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigInteger getQuantidadeContabilizada() {
		return quantidadeContabilizada;
	}

	public void setQuantidadeContabilizada(BigInteger quantidadeContabilizada) {
		this.quantidadeContabilizada = quantidadeContabilizada;
	}

	public BigInteger getQuantidadeLogico() {
		return quantidadeLogico;
	}

	public void setQuantidadeLogico(BigInteger quantidadeLogico) {
		this.quantidadeLogico = quantidadeLogico;
	}

	public BigInteger getQuantidadeVenda() {
		return quantidadeVenda;
	}

	public void setQuantidadeVenda(BigInteger quantidadeVenda) {
		this.quantidadeVenda = quantidadeVenda;
	}

	public BigInteger getQuantidadeTransferenciaEntrada() {
		return quantidadeTransferenciaEntrada;
	}

	public void setQuantidadeTransferenciaEntrada(
			BigInteger quantidadeTransferenciaEntrada) {
		this.quantidadeTransferenciaEntrada = quantidadeTransferenciaEntrada;
	}

	public BigInteger getQuantidadeTransferenciaSaida() {
		return quantidadeTransferenciaSaida;
	}

	public void setQuantidadeTransferenciaSaida(
			BigInteger quantidadeTransferenciaSaida) {
		this.quantidadeTransferenciaSaida = quantidadeTransferenciaSaida;
	}

	public BigInteger getSaldo() {
		return saldo;
	}

	public void setSaldo(BigInteger saldo) {
		this.saldo = saldo;
	}
	
	

}