package br.com.abril.nds.model.fiscal;
import java.math.BigDecimal;
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

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;


@Entity
@Table(name = "ITEM_NOTA_FISCAL_ENTRADA")
@SequenceGenerator(name="ITEM_NF_ENTRADA_SEQ", initialValue = 1, allocationSize = 1)
public class ItemNotaFiscalEntrada {

	@Id
	@GeneratedValue(generator = "ITEM_NF_ENTRADA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "QTDE", nullable = false)
	private BigDecimal qtde;
	@ManyToOne(optional = false)
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private NotaFiscalEntrada notaFiscal;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM")
	private Origem origem;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_LANCAMENTO", nullable = false)
	private Date dataLancamento;	
	
	@Column(name = "TIPO_LANCAMENTO", nullable = false)
	private TipoLancamento tipoLancamento;	
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_REC", nullable = false)
	private Date dataRecolhimento;	
	
	@OneToOne(mappedBy = "itemNotaFiscal")
	private ItemRecebimentoFisico recebimentoFisico;

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
	
	public NotaFiscalEntrada getNotaFiscal() {
		return notaFiscal;
	}
	
	public void setNotaFiscal(NotaFiscalEntrada notaFiscal) {
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
	
	public Date getDataLancamento() {
		return dataLancamento;
	}
	
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	public ItemRecebimentoFisico getRecebimentoFisico() {
		return recebimentoFisico;
	}
	
	public void setRecebimentoFisico(ItemRecebimentoFisico recebimentoFisico) {
		this.recebimentoFisico = recebimentoFisico;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
}