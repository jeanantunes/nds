package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;

@Entity
@Table(name = "MOVIMENTO_ESTOQUE_COTA")
public class MovimentoEstoqueCota  extends AbstractMovimentoEstoque implements Cloneable, Serializable {
	
	@Embedded
	private ValoresAplicados valoresAplicados;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "ESTOQUE_PROD_COTA_ID")
	private EstoqueProdutoCota estoqueProdutoCota;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "ESTOQUE_PROD_COTA_JURAMENTADO_ID")
	private EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentado;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_FURO_ID")
	MovimentoEstoqueCota movimentoEstoqueCotaFuro;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_ESTORNO_ID")
	MovimentoEstoqueCota movimentoEstoqueCotaEstorno;
	
	// Esta data é utilizada para a data do lançamento do distribuidor aparecer corretamente na consulta consignado cota
	// Implementado em conjunto com Cesar Pop Punk
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_LANCAMENTO_ORIGINAL")
	private Date dataLancamentoOriginal;
	
	/**
	 * Estudo cota que originou o movimento, 
	 * caso o movimento seja de reparte
	 */
	@ManyToOne(optional = true)
	@JoinColumn(name = "ESTUDO_COTA_ID")
	private EstudoCota estudoCota;
	
	@ManyToMany(mappedBy="listaMovimentoEstoqueCota")
	@Cascade(value = {CascadeType.ALL})
	private List<ProdutoServico> listaProdutoServicos;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "LANCAMENTO_ID")
	private Lancamento lancamento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_ESTOQUE_FINANCEIRO")
	private StatusEstoqueFinanceiro statusEstoqueFinanceiro;
	
	@ManyToOne
	@JoinColumn(name = "MOVIMENTO_FINANCEIRO_COTA_ID")
	private MovimentoFinanceiroCota movimentoFinanceiroCota;
	
	@Column(name = "NOTA_FISCAL_EMITIDA")
	private boolean notaFiscalEmitida;
		
	public Object clone() {

		MovimentoEstoqueCota mec = new MovimentoEstoqueCota();
		mec.setAprovadoAutomaticamente(this.isAprovadoAutomaticamente());
		mec.setAprovador(this.getAprovador());
		mec.setCota(this.getCota());
		mec.setData(this.getData());
		mec.setDataAprovacao(this.getDataAprovacao());
		mec.setDataCriacao(this.getDataCriacao());
		mec.setDataIntegracao(this.getDataIntegracao());
		mec.setEstoqueProdutoCota(this.getEstoqueProdutoCota());
		mec.setEstoqueProdutoCotaJuramentado(this.getEstoqueProdutoCotaJuramentado());
		mec.setLancamento(this.getLancamento());
		mec.setListaProdutoServicos(this.getListaProdutoServicos());
		mec.setMotivo(this.getMotivo());
		mec.setProdutoEdicao(this.getProdutoEdicao());
		mec.setQtde(this.getQtde());
		mec.setStatus(this.getStatus());
		mec.setStatusEstoqueFinanceiro(this.getStatusEstoqueFinanceiro());
		mec.setStatusIntegracao(this.getStatusIntegracao());
		mec.setTipoMovimento(this.getTipoMovimento());
		mec.setUsuario(this.getUsuario());
		mec.setValoresAplicados(this.getValoresAplicados());

        return mec;
    }
	
	/**
	 * @return the statusEstoqueFinanceiro
	 */
	public StatusEstoqueFinanceiro getStatusEstoqueFinanceiro() {
		return statusEstoqueFinanceiro;
	}

	/**
	 * @param statusEstoqueFinanceiro the statusEstoqueFinanceiro to set
	 */
	public void setStatusEstoqueFinanceiro(
			StatusEstoqueFinanceiro statusEstoqueFinanceiro) {
		this.statusEstoqueFinanceiro = statusEstoqueFinanceiro;
	}

	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public EstoqueProdutoCota getEstoqueProdutoCota() {
		return estoqueProdutoCota;
	}
	
	public void setEstoqueProdutoCota(EstoqueProdutoCota estoqueProdutoCota) {
		this.estoqueProdutoCota = estoqueProdutoCota;
	}

	/**
	 * @return the estudoCota
	 */
	public EstudoCota getEstudoCota() {
		return estudoCota;
	}

	/**
	 * @param estudoCota the estudoCota to set
	 */
	public void setEstudoCota(EstudoCota estudoCota) {
		this.estudoCota = estudoCota;
	}

	/**
	 * @return the listaProdutoServicos
	 */
	public List<ProdutoServico> getListaProdutoServicos() {
		return listaProdutoServicos;
	}

	/**
	 * @param listaProdutoServicos the listaProdutoServicos to set
	 */
	public void setListaProdutoServicos(List<ProdutoServico> listaProdutoServicos) {
		this.listaProdutoServicos = listaProdutoServicos;
	}

	/**
	 * @return the lancamento
	 */
	public Lancamento getLancamento() {
		return lancamento;
	}

	/**
	 * @param lancamento the lancamento to set
	 */
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public EstoqueProdutoCotaJuramentado getEstoqueProdutoCotaJuramentado() {
		return estoqueProdutoCotaJuramentado;
	}

	public void setEstoqueProdutoCotaJuramentado(
			EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentado) {
		this.estoqueProdutoCotaJuramentado = estoqueProdutoCotaJuramentado;
	}

	public ValoresAplicados getValoresAplicados() {
		return valoresAplicados;
	}

	public void setValoresAplicados(ValoresAplicados valoresAplicados) {
		this.valoresAplicados = valoresAplicados;
	}

	public MovimentoEstoqueCota getMovimentoEstoqueCotaFuro() {
		return movimentoEstoqueCotaFuro;
	}

	public void setMovimentoEstoqueCotaFuro(
			MovimentoEstoqueCota movimentoEstoqueCotaFuro) {
		this.movimentoEstoqueCotaFuro = movimentoEstoqueCotaFuro;
	}

	public Date getDataLancamentoOriginal() {
		return dataLancamentoOriginal;
	}

	public void setDataLancamentoOriginal(Date dataLancamentoOriginal) {
		this.dataLancamentoOriginal = dataLancamentoOriginal;
	}

	public MovimentoFinanceiroCota getMovimentoFinanceiroCota() {
		return movimentoFinanceiroCota;
	}

	public void setMovimentoFinanceiroCota(
			MovimentoFinanceiroCota movimentoFinanceiroCota) {
		this.movimentoFinanceiroCota = movimentoFinanceiroCota;
	}

	public MovimentoEstoqueCota getMovimentoEstoqueCotaEstorno() {
		return movimentoEstoqueCotaEstorno;
	}

	public void setMovimentoEstoqueCotaEstorno(
			MovimentoEstoqueCota movimentoEstoqueCotaEstorno) {
		this.movimentoEstoqueCotaEstorno = movimentoEstoqueCotaEstorno;
	}

	public boolean isNotaFiscalEmitida() {
		return notaFiscalEmitida;
	}

	public void setNotaFiscalEmitida(boolean notaFiscalEmitida) {
		this.notaFiscalEmitida = notaFiscalEmitida;
	}
	
}