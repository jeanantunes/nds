package br.com.abril.nds.model.movimentacao;
import java.math.BigDecimal;
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

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "MOVIMENTO_ESTOQUE")
@SequenceGenerator(name="MOVIMENTO_ESTOQUE_SEQ", initialValue = 1, allocationSize = 1)
public class MovimentoEstoque {

	@Id
	@GeneratedValue(generator = "MOVIMENTO_ESTOQUE_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DATA_INCLUSAO", nullable = false)
	private Date dataInclusao;
	@Column(name = "QTDE", nullable = false)
	private BigDecimal qtde;
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_MOVIMENTO_ID")
	private TipoMovimento tipoMovimento;
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	@OneToOne
	private Diferenca diferenca;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	@OneToOne(optional = true)
	@JoinColumn(name = "ITEM_REC_FISICO_ID")
	private ItemRecebimentoFisico itemRecebimentoFisico;
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTOQUE_PRODUTO_ID")
	private EstoqueProduto estoqueProduto;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataInclusao() {
		return dataInclusao;
	}
	
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	
	public BigDecimal getQtde() {
		return qtde;
	}
	
	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}
	
	public TipoMovimento getTipoMovimento() {
		return tipoMovimento;
	}
	
	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Diferenca getDiferenca() {
		return diferenca;
	}
	
	public void setDiferenca(Diferenca diferenca) {
		this.diferenca = diferenca;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public ItemRecebimentoFisico getItemRecebimentoFisico() {
		return itemRecebimentoFisico;
	}
	
	public void setItemRecebimentoFisico(ItemRecebimentoFisico itemRecebimentoFisico) {
		this.itemRecebimentoFisico = itemRecebimentoFisico;
	}
	
	public EstoqueProduto getEstoqueProduto() {
		return estoqueProduto;
	}
	
	public void setEstoqueProduto(EstoqueProduto estoqueProduto) {
		this.estoqueProduto = estoqueProduto;
	}

}