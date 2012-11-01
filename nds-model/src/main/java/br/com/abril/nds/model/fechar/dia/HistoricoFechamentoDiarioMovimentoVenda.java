package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@SequenceGenerator(name="HISTORICO_FECHAMENTO_DIARIO_MOVIMENTO_VENDAS_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class HistoricoFechamentoDiarioMovimentoVenda implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "HISTORICO_FECHAMENTO_DIARIO_MOVIMENTO_VENDAS_SEQ",strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private Long id;	

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QUANTIDADE")
	private BigInteger quantidade;
	
	@Column(name="VALOR")
	private BigDecimal valor;
	
	@Column(name="DATA_VENCIMENTO")
	@Temporal(TemporalType.DATE)
	private Date dataRecebimento;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigInteger getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
}
