package br.com.abril.nds.model.estoque;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.movimentacao.MovimentoCota;

@Entity
@Table(name = "	ESTOQUE_COTA")
@SequenceGenerator(name="ESTOQUE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class EstoqueProdutoCota {
	
	@Id
	@GeneratedValue(generator = "ESTOQUE_COTA_SEQ")	
	@Column(name = "ID")
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	@OneToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	@Column(name = "QTDE_ENVIADA")
	private BigDecimal qtdeEnviada;
	@Column(name = "QTDE_DEVEOLVIDA")
	private BigDecimal qtdeDevolvida;
	@OneToMany(mappedBy = "estoqueProdutoCota")
	List<MovimentoCota> movimentos = new ArrayList<MovimentoCota>();
	@Version
	@Column(name = "VERSAO")
	private Long versao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public BigDecimal getQtdeEnviada() {
		return qtdeEnviada;
	}
	
	public void setQtdeEnviada(BigDecimal qtdeEnviada) {
		this.qtdeEnviada = qtdeEnviada;
	}
	
	public BigDecimal getQtdeDevolvida() {
		return qtdeDevolvida;
	}
	
	public void setQtdeDevolvida(BigDecimal qtdeDevolvida) {
		this.qtdeDevolvida = qtdeDevolvida;
	}
	
	public List<MovimentoCota> getMovimentos() {
		return movimentos;
	}
	
	public void setMovimentos(List<MovimentoCota> movimentos) {
		this.movimentos = movimentos;
	}
	
	public Long getVersao() {
		return versao;
	}
	
	public void setVersao(Long versao) {
		this.versao = versao;
	}

}
