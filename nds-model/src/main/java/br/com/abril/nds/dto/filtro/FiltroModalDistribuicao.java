package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroModalDistribuicao implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -544142158601446426L;
	private String nmAssitPromoComercial;
	private String nmGerenteComercial;
	private String observacao;

	private boolean isRepartePontoVenda; 
	private boolean isSolicitacaoNumAtrasoInternet;
	private boolean isRecebeRecolheProdutosParciais;

	private DescricaoTipoEntrega descricaoTipoEntrega; 
	
	private FiltroCheckDistribEmisDoc filtroCheckDistribEmisDoc = new FiltroCheckDistribEmisDoc();

	public String getNmAssitPromoComercial() {
		return nmAssitPromoComercial;
	}

	public void setNmAssitPromoComercial(String nmAssitPromoComercial) {
		this.nmAssitPromoComercial = nmAssitPromoComercial;
	}

	public String getNmGerenteComercial() {
		return nmGerenteComercial;
	}

	public void setNmGerenteComercial(String nmGerenteComercial) {
		this.nmGerenteComercial = nmGerenteComercial;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean getIsRepartePontoVenda() {
		return isRepartePontoVenda;
	}

	public void setIsRepartePontoVenda(boolean isRepartePontoVenda) {
		this.isRepartePontoVenda = isRepartePontoVenda;
	}

	public boolean getIsSolicitacaoNumAtrasoInternet() {
		return isSolicitacaoNumAtrasoInternet;
	}

	public void setIsSolicitacaoNumAtrasoInternet(
			boolean isSolicitacaoNumAtrasoInternet) {
		this.isSolicitacaoNumAtrasoInternet = isSolicitacaoNumAtrasoInternet;
	}

	public boolean getIsRecebeRecolheProdutosParciais() {
		return isRecebeRecolheProdutosParciais;
	}

	public void setIsRecebeRecolheProdutosParciais(
			boolean isRecebeRecolheProdutosParciais) {
		this.isRecebeRecolheProdutosParciais = isRecebeRecolheProdutosParciais;
	}


	public FiltroCheckDistribEmisDoc getFiltroCheckDistribEmisDoc() {
		return filtroCheckDistribEmisDoc;
	}

	public void setFiltroCheckDistribEmisDoc(
			FiltroCheckDistribEmisDoc filtroCheckDistribEmisDoc) {
		this.filtroCheckDistribEmisDoc = filtroCheckDistribEmisDoc;
	}

	public DescricaoTipoEntrega getDescricaoTipoEntrega() {
		return descricaoTipoEntrega;
	}

	public void setDescricaoTipoEntrega(DescricaoTipoEntrega descricaoTipoEntrega) {
		this.descricaoTipoEntrega = descricaoTipoEntrega;
	}

	
}
