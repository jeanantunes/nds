package br.com.abril.nds.model.fiscal;
import java.math.BigDecimal;

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
@Table(name = "ITEM_NOTA_FISCAL_SAIDA")
@SequenceGenerator(name="ITEM_NF_SAIDA_SEQ", initialValue = 1, allocationSize = 1)
public class ItemNotaFiscalSaida {

	@Id
	@GeneratedValue(generator = "ITEM_NF_SAIDA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "QTDE", nullable = false)
	private BigDecimal qtde;

	@ManyToOne(optional = false)
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private NotaFiscalSaida notaFiscal;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getQtde() {
		return qtde;
	}

	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}	
	
	public NotaFiscalSaida getNotaFiscal() {
		return notaFiscal;
	}
	
	public void setNotaFiscal(NotaFiscalSaida notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
}