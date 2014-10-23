package br.com.abril.nds.model.cadastro.desconto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "DESCONTO_COTA_PRODUTO_EXCESSOES",
	   uniqueConstraints= {@UniqueConstraint(columnNames = {"FORNECEDOR_ID", "COTA_ID", "EDITOR_ID", "PRODUTO_ID", "PRODUTO_EDICAO_ID"})})
@SequenceGenerator(name="DESCONTO_COTA_PRODUTO_EXC_SEQ", initialValue = 1, allocationSize = 1)
public class DescontoCotaProdutoExcessao implements Serializable {

	private static final long serialVersionUID = -6962370571993126767L;

	@Id
	@GeneratedValue(generator = "DESCONTO_COTA_PRODUTO_EXC_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_ALTERACAO")
	private Date dataAlteracao;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "DESCONTO_ID")
	private Desconto desconto;

	@ManyToOne(optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID", referencedColumnName="ID")
	private Distribuidor distribuidor;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "EDITOR_ID")
	private Editor editor;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "PRODUTO_ID")
	private Produto produto;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_DESCONTO", nullable = false)
	private TipoDesconto tipoDesconto;
	
	@Column(name = "DESCONTO_PREDOMINANTE", nullable = false)
	private boolean descontoPredominante;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Desconto getDesconto() {
		return desconto;
	}

	public void setDesconto(Desconto desconto) {
		this.desconto = desconto;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Editor getEditor() {
		return editor;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public TipoDesconto getTipoDesconto() {
		return tipoDesconto;
	}

	public void setTipoDesconto(TipoDesconto tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public boolean isDescontoPredominante() {
		return descontoPredominante;
	}

	public void setDescontoPredominante(boolean descontoPredominante) {
		this.descontoPredominante = descontoPredominante;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
