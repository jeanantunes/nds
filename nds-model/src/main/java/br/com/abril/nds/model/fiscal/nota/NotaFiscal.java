package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Entity
@Table(name = "NOTA_FISCAL_NOVO")
@SequenceGenerator(name = "NOTA_FISCAL_SEQ", initialValue = 1, allocationSize = 1)
public class NotaFiscal implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -580456077685816545L;
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(generator = "NOTA_FISCAL_SEQ")
	@NFEExport(secao = TipoSecao.B, posicao = 1, mascara="00000000")
	private Long id;
	
	/**
	 * IDE
	 */
	@Embedded
	@NFEExportType
	private Identificacao identificacao;
	
	/**
	 * EMIT
	 */
	@Embedded
	@NFEExportType
	private IdentificacaoEmitente identificacaoEmitente;
	
	/**
	 * DEST
	 */
	@Embedded
	@NFEExportType
	private IdentificacaoDestinatario identificacaoDestinatario;
	
	/**
	 * DET -> PROD
	 */
	@OneToMany(mappedBy = "produtoServicoPK.notaFiscal")
	@NFEExportType
	private List<ProdutoServico> produtosServicos;
	
	/**
	 * TOTAL
	 */
	@Embedded
	@NFEExportType(secaoPadrao = TipoSecao.W)
	private InformacaoValoresTotais informacaoValoresTotais;
	
	/**
	 * TRANSP
	 */
	@Embedded
	@NFEExportType
	private InformacaoTransporte informacaoTransporte;
	
		
	/**
	 * INFADIC
	 */
	@Embedded
	@NFEExportType
	private InformacaoAdicional informacaoAdicional;
	
	/**
	 * Informações da comunicação eletrônica.
	 */
	@Embedded
	@NFEExportType
	private InformacaoEletronica informacaoEletronica;
	
	/**
	 * Status de processamento interno da nota fiscal
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_PROCESSAMENTO_INTERNO")
	private StatusProcessamentoInterno statusProcessamentoInterno;
	
	/**
	 * Construtor padrão.
	 */
	public NotaFiscal() {
		
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the identificacao
	 */
	public Identificacao getIdentificacao() {
		return identificacao;
	}

	/**
	 * @param identificacao the identificacao to set
	 */
	public void setIdentificacao(Identificacao identificacao) {
		this.identificacao = identificacao;
	}

	/**
	 * @return the identificacaoEmitente
	 */
	public IdentificacaoEmitente getIdentificacaoEmitente() {
		return identificacaoEmitente;
	}

	/**
	 * @param identificacaoEmitente the identificacaoEmitente to set
	 */
	public void setIdentificacaoEmitente(IdentificacaoEmitente identificacaoEmitente) {
		this.identificacaoEmitente = identificacaoEmitente;
	}

	/**
	 * @return the identificacaoDestinatario
	 */
	public IdentificacaoDestinatario getIdentificacaoDestinatario() {
		return identificacaoDestinatario;
	}

	/**
	 * @param identificacaoDestinatario the identificacaoDestinatario to set
	 */
	public void setIdentificacaoDestinatario(
			IdentificacaoDestinatario identificacaoDestinatario) {
		this.identificacaoDestinatario = identificacaoDestinatario;
	}

	/**
	 * @return the produtosServicos
	 */
	public List<ProdutoServico> getProdutosServicos() {
		return produtosServicos;
	}

	/**
	 * @param produtosServicos the produtosServicos to set
	 */
	public void setProdutosServicos(List<ProdutoServico> produtosServicos) {
		this.produtosServicos = produtosServicos;
	}

	/**
	 * @return the informacaoValoresTotais
	 */
	public InformacaoValoresTotais getInformacaoValoresTotais() {
		return informacaoValoresTotais;
	}

	/**
	 * @param informacaoValoresTotais the informacaoValoresTotais to set
	 */
	public void setInformacaoValoresTotais(
			InformacaoValoresTotais informacaoValoresTotais) {
		this.informacaoValoresTotais = informacaoValoresTotais;
	}

	/**
	 * @return the informacaoTransporte
	 */
	public InformacaoTransporte getInformacaoTransporte() {
		return informacaoTransporte;
	}

	/**
	 * @param informacaoTransporte the informacaoTransporte to set
	 */
	public void setInformacaoTransporte(InformacaoTransporte informacaoTransporte) {
		this.informacaoTransporte = informacaoTransporte;
	}

	

	/**
	 * @return the informacaoAdicional
	 */
	public InformacaoAdicional getInformacaoAdicional() {
		return informacaoAdicional;
	}

	/**
	 * @param informacaoAdicional the informacaoAdicional to set
	 */
	public void setInformacaoAdicional(InformacaoAdicional informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
	}

	/**
	 * @return the informacaoEletronica
	 */
	public InformacaoEletronica getInformacaoEletronica() {
		return informacaoEletronica;
	}

	/**
	 * @param informacaoEletronica the informacaoEletronica to set
	 */
	public void setInformacaoEletronica(InformacaoEletronica informacaoEletronica) {
		this.informacaoEletronica = informacaoEletronica;
	}

	/**
	 * @return the statusProcessamentoInterno
	 */
	public StatusProcessamentoInterno getStatusProcessamentoInterno() {
		return statusProcessamentoInterno;
	}

	/**
	 * @param statusProcessamentoInterno the statusProcessamentoInterno to set
	 */
	public void setStatusProcessamentoInterno(
			StatusProcessamentoInterno statusProcessamentoInterno) {
		this.statusProcessamentoInterno = statusProcessamentoInterno;
	}
	
}
