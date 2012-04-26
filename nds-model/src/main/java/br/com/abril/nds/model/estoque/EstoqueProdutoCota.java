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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "ESTOQUE_PRODUTO_COTA",uniqueConstraints = { @UniqueConstraint(columnNames = {
		"COTA_ID", "PRODUTO_EDICAO_ID" })})
@SequenceGenerator(name="ESTOQUE_PROD_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class EstoqueProdutoCota {
	
	@Id
	@GeneratedValue(generator = "ESTOQUE_PROD_COTA_SEQ")	
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name = "QTDE_RECEBIDA")
	private BigDecimal qtdeRecebida;
	
	@Column(name = "QTDE_DEVOLVIDA")
	private BigDecimal qtdeDevolvida;
	
	@OneToMany(mappedBy = "estoqueProdutoCota")
	List<MovimentoEstoqueCota> movimentos = new ArrayList<MovimentoEstoqueCota>();
	
	@Version
	@Column(name = "VERSAO")
	private Long versao = 0L;
	
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
	
	public BigDecimal getQtdeRecebida() {
		return qtdeRecebida;
	}
	
	public void setQtdeRecebida(BigDecimal qtdeRecebida) {
		this.qtdeRecebida = qtdeRecebida;
	}
	
	public BigDecimal getQtdeDevolvida() {
		return qtdeDevolvida;
	}
	
	public void setQtdeDevolvida(BigDecimal qtdeDevolvida) {
		this.qtdeDevolvida = qtdeDevolvida;
	}
	
	public List<MovimentoEstoqueCota> getMovimentos() {
		return movimentos;
	}
	
	public void setMovimentos(List<MovimentoEstoqueCota> movimentos) {
		this.movimentos = movimentos;
	}
	
	public Long getVersao() {
		return versao;
	}
	
	public void setVersao(Long versao) {
		this.versao = versao;
	}

}
