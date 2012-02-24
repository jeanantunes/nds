package br.com.abril.nds.model.fiscal;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "ITEM_NOTA_FISCAL")
public class ItemNotaFiscal {

	@Id
	private Long id;
	private BigDecimal quantidade;
	@OneToMany
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private List<NotaFiscal> notaFiscal;
	@ManyToOne
	private ProdutoEdicao produtoEdicao;
	@ManyToOne
	private Usuario usuario;
	@Enumerated(EnumType.STRING)
	private Origem origem;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}
	
	public List<NotaFiscal> getNotaFiscal() {
		return notaFiscal;
	}
	
	public void setNotaFiscal(List<NotaFiscal> notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
	
	


}