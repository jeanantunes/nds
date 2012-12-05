package br.com.abril.nds.model.estoque;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;

@Entity
@Table(name = "MOVIMENTO_ESTOQUE_COTA")
public class MovimentoEstoqueCota  extends AbstractMovimentoEstoque implements Cloneable {
	
	@Embedded
	private ValoresAplicados valoresAplicados;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTOQUE_PROD_COTA_ID")
	private EstoqueProdutoCota estoqueProdutoCota;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "ESTOQUE_PROD_COTA_JURAMENTADO_ID")
	private EstoqueProdutoCotaJuramentado estoqueProdutoCotaJuramentado;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_COTA_FURO_ID")
	MovimentoEstoqueCota movimentoEstoqueCotaFuro;
	
	/**
	 * Estudo cota que originou o movimento, 
	 * caso o movimento seja de reparte
	 */
	@OneToOne(optional = true)
	@JoinColumn(name = "ESTUDO_COTA_ID")
	private EstudoCota estudoCota;
	
	@ManyToMany(mappedBy="listaMovimentoEstoqueCota")
	private List<ProdutoServico> listaProdutoServicos;
	
	@OneToMany(mappedBy = "movimentoEstoqueCota")
	private List<ConferenciaEncalhe> listaConferenciasEncalhe;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "LANCAMENTO_ID")
	private Lancamento lancamento;

	@ManyToMany(mappedBy="listaMovimentoEstoqueCota")
	private List<ItemNotaEnvio> listaItemNotaEnvio;

	@Column(name = "STATUS_ESTOQUE_FINANCEIRO")
	private StatusEstoqueFinanceiro statusEstoqueFinanceiro;
	
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
		//mec.setEstudoCota(this.getEstudoCota());
		mec.setLancamento(this.getLancamento());
		mec.setListaConferenciasEncalhe(this.getListaConferenciasEncalhe());
		mec.setListaItemNotaEnvio(this.getListaItemNotaEnvio());
		mec.setListaProdutoServicos(this.getListaProdutoServicos());
		mec.setMotivo(this.getMotivo());
		mec.setProdutoEdicao(this.getProdutoEdicao());
		mec.setQtde(this.getQtde());
		mec.setStatus(this.getStatus());
		mec.setStatusEstoqueFinanceiro(this.getStatusEstoqueFinanceiro());
		mec.setStatusIntegracao(this.getStatusIntegracao());
		mec.setTipoMovimento(this.getTipoMovimento());
		mec.setUsuario(this.getUsuario());

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
	 * @return the listaConferenciasEncalhe
	 */
	public List<ConferenciaEncalhe> getListaConferenciasEncalhe() {
		return listaConferenciasEncalhe;
	}

	/**
	 * @param listaConferenciasEncalhe the listaConferenciasEncalhe to set
	 */
	public void setListaConferenciasEncalhe(
			List<ConferenciaEncalhe> listaConferenciasEncalhe) {
		this.listaConferenciasEncalhe = listaConferenciasEncalhe;
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

	/**
	 * @return the listaItemNotaEnvio
	 */
	public List<ItemNotaEnvio> getListaItemNotaEnvio() {
		return listaItemNotaEnvio;
	}

	/**
	 * @param listaItemNotaEnvio the listaItemNotaEnvio to set
	 */
	public void setListaItemNotaEnvio(List<ItemNotaEnvio> listaItemNotaEnvio) {
		this.listaItemNotaEnvio = listaItemNotaEnvio;
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

}
