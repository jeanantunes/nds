package br.com.abril.nds.dto;

import java.util.List;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;

public class NfeImpressaoDTO extends NfeDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5371527354389347503L;

	private Long id;
	
	private Identificacao identificacao;
	
	private IdentificacaoEmitente identificacaoEmitente;
	
	private IdentificacaoDestinatario identificacaoDestinatario;

	private List<ProdutoServico> produtosServicos;
	
	private InformacaoValoresTotais informacaoValoresTotais;
	
	private InformacaoTransporte informacaoTransporte;
	
	private InformacaoAdicional informacaoAdicional;
	
	private InformacaoEletronica informacaoEletronica;
	
	private StatusProcessamentoInterno statusProcessamentoInterno;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Identificacao getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(Identificacao identificacao) {
		this.identificacao = identificacao;
	}

	public IdentificacaoEmitente getIdentificacaoEmitente() {
		return identificacaoEmitente;
	}

	public void setIdentificacaoEmitente(IdentificacaoEmitente identificacaoEmitente) {
		this.identificacaoEmitente = identificacaoEmitente;
	}

	public IdentificacaoDestinatario getIdentificacaoDestinatario() {
		return identificacaoDestinatario;
	}

	public void setIdentificacaoDestinatario(
			IdentificacaoDestinatario identificacaoDestinatario) {
		this.identificacaoDestinatario = identificacaoDestinatario;
	}

	public List<ProdutoServico> getProdutosServicos() {
		return produtosServicos;
	}

	public void setProdutosServicos(List<ProdutoServico> produtosServicos) {
		this.produtosServicos = produtosServicos;
	}

	public InformacaoValoresTotais getInformacaoValoresTotais() {
		return informacaoValoresTotais;
	}

	public void setInformacaoValoresTotais(
			InformacaoValoresTotais informacaoValoresTotais) {
		this.informacaoValoresTotais = informacaoValoresTotais;
	}

	public InformacaoTransporte getInformacaoTransporte() {
		return informacaoTransporte;
	}

	public void setInformacaoTransporte(InformacaoTransporte informacaoTransporte) {
		this.informacaoTransporte = informacaoTransporte;
	}

	public InformacaoAdicional getInformacaoAdicional() {
		return informacaoAdicional;
	}

	public void setInformacaoAdicional(InformacaoAdicional informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
	}

	public InformacaoEletronica getInformacaoEletronica() {
		return informacaoEletronica;
	}

	public void setInformacaoEletronica(InformacaoEletronica informacaoEletronica) {
		this.informacaoEletronica = informacaoEletronica;
	}

	public StatusProcessamentoInterno getStatusProcessamentoInterno() {
		return statusProcessamentoInterno;
	}

	public void setStatusProcessamentoInterno(
			StatusProcessamentoInterno statusProcessamentoInterno) {
		this.statusProcessamentoInterno = statusProcessamentoInterno;
	}
	
}
