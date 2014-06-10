package br.com.abril.nds.model.estudo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoGerado;

@Entity
@SuppressWarnings("serial")
@Table(name = "estudo_produto_edicao")
@SequenceGenerator(name="ESTUDO_PRODUTO_EDICAO_SEQ", initialValue = 1, allocationSize = 1)
public class EstudoProdutoEdicao implements Serializable {

	@Id
    @GeneratedValue(generator = "ESTUDO_PRODUTO_EDICAO_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "ESTUDO_ID")
    private EstudoGerado estudo;
	
	@ManyToOne
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "COTA_ID")
    private Cota cota;
	
	@Column(name = "REPARTE", nullable = false)
	private BigInteger reparte;
	
	@Column(name = "VENDA", nullable = false)
	private BigInteger venda;
	
	@Column(name = "INDICE_CORRECAO")	
	private BigDecimal indiceCorrecao;
	
	@Column(name = "VENDA_CORRIGIDA")	
	private BigDecimal vendaCorrigida;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EstudoGerado getEstudo() {
		return estudo;
	}

	public void setEstudo(EstudoGerado estudo) {
		this.estudo = estudo;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getVenda() {
		return venda;
	}

	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}

	public BigDecimal getIndiceCorrecao() {
		return indiceCorrecao;
	}

	public void setIndiceCorrecao(BigDecimal indiceCorrecao) {
		this.indiceCorrecao = indiceCorrecao;
	}

	public BigDecimal getVendaCorrigida() {
		return vendaCorrigida;
	}

	public void setVendaCorrigida(BigDecimal vendaCorrigida) {
		this.vendaCorrigida = vendaCorrigida;
	}
	
}
