package br.com.abril.nds.model.movimentacao;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "FURO_PRODUTO")
@SequenceGenerator(name="FURO_PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class FuroProduto {

	@Id
	@GeneratedValue(generator = "FURO_PRODUTO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DATA", nullable = false)
	private Date data;
	
	/**
	 * Campo corresponde a data de lancamento do 
	 * distribuidor antes da mesma ser alterada
	 * para nova data devido a este registro de
	 * furo de publicação.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_LCTO_DISTRIBUIDOR", nullable = true)
	private Date dataLancamentoDistribuidor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;
    
	@ManyToOne
	@JoinColumn(name = "LANCAMENTO_ID")
	private Lancamento lancamento;
	
    public Long getId() {
		return id;
	}
	
    public void setId(Long id) {
		this.id = id;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
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
	
	public Lancamento getLancamento() {
		return lancamento;
	}
	
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public Date getDataLancamentoDistribuidor() {
		return dataLancamentoDistribuidor;
	}

	public void setDataLancamentoDistribuidor(Date dataLancamentoDistribuidor) {
		this.dataLancamentoDistribuidor = dataLancamentoDistribuidor;
	}
	
}