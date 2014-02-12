package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroMonitorNfeDTO implements Serializable {
	
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean indDocumentoCPF;
	
	@Export(label="Box")
	private Integer box;
	
	@Export(label="Período de")
	private Date dataInicial;
	
	@Export(label="Até")
	private Date dataFinal;
	
	@Export(label="Destinatário / Remetente")
	private String documentoPessoa;
	
	@Export(label="Tipo de Nf-e")
	private String tipoNfe;
	
	@Export(label="Número")
	private Long numeroNotaInicial;
	
	@Export(label="Até")
	private Long numeroNotaFinal;
	
	@Export(label="Chave de Acesso NF-e")
	private String chaveAcesso;
	
	@Export(label="Situação Nf-e")
	private String situacaoNfe;
	
	@Export(label="Série")
	private Integer serie;
	
	private String numeroDocumento;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna {
		
		NOTA("numero"),
		SERIE("serie"),
		EMISSAO("emissao"),
		TIPO_EMISSAO("tipoEmissao"),
		CNPJ_DESTINATARIO("cnpjDestinatario"),
		CNPJ_REMETENTE("cnpjRemetente"),
		CPF_REMETENTE("cpfRemetente"),
		STATUS_NFE("statusNfe"),
		TIPO_NFE("tipoNfe"),
		MOVIMENTO_INTEGRACAO("movimentoIntegracao");		
		
		private String nomeColuna;
		
		private OrdenacaoColuna(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
	}	

	
	/**
	 * Obtém box
	 *
	 * @return String
	 */
	public Integer getBox() {
		return box;
	}
	/**
	 * Atribuí box
	 * @param box 
	 */
	public void setBox(Integer box) {
		this.box = box;
	}
	/**
	 * Obtém dataInicial
	 *
	 * @return Date
	 */
	public Date getDataInicial() {
		return dataInicial;
	}
	/**
	 * Atribuí dataInicial
	 * @param dataInicial 
	 */
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	/**
	 * Obtém dataFinal
	 *
	 * @return Date
	 */
	public Date getDataFinal() {
		return dataFinal;
	}
	/**
	 * Atribuí dataFinal
	 * @param dataFinal 
	 */
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	/**
	 * Obtém documentoPessoa
	 *
	 * @return String
	 */
	public String getDocumentoPessoa() {
		return documentoPessoa;
	}
	/**
	 * Atribuí documentoPessoa
	 * @param documentoPessoa 
	 */
	public void setDocumentoPessoa(String documentoPessoa) {
		this.documentoPessoa = documentoPessoa;
	}
	/**
	 * Obtém tipoNfe
	 *
	 * @return String
	 */
	public String getTipoNfe() {
		return tipoNfe;
	}
	/**
	 * Atribuí tipoNfe
	 * @param tipoNfe 
	 */
	public void setTipoNfe(String tipoNfe) {
		this.tipoNfe = tipoNfe;
	}
	/**
	 * Obtém numeroNotaInicial
	 *
	 * @return Long
	 */
	public Long getNumeroNotaInicial() {
		return numeroNotaInicial;
	}
	/**
	 * Atribuí numeroNotaInicial
	 * @param numeroNotaInicial 
	 */
	public void setNumeroNotaInicial(Long numeroNotaInicial) {
		this.numeroNotaInicial = numeroNotaInicial;
	}
	/**
	 * Obtém numeroNotaFinal
	 *
	 * @return Long
	 */
	public Long getNumeroNotaFinal() {
		return numeroNotaFinal;
	}
	/**
	 * Atribuí numeroNotaFinal
	 * @param numeroNotaFinal 
	 */
	public void setNumeroNotaFinal(Long numeroNotaFinal) {
		this.numeroNotaFinal = numeroNotaFinal;
	}
	/**
	 * Obtém chaveAcesso
	 *
	 * @return String
	 */
	public String getChaveAcesso() {
		return chaveAcesso;
	}
	/**
	 * Atribuí chaveAcesso
	 * @param chaveAcesso 
	 */
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	/**
	 * Obtém situacaoNfe
	 *
	 * @return String
	 */
	public String getSituacaoNfe() {
		return situacaoNfe;
	}
	/**
	 * Atribuí situacaoNfe
	 * @param situacaoNfe 
	 */
	public void setSituacaoNfe(String situacaoNfe) {
		this.situacaoNfe = situacaoNfe;
	}
	/**
	 * Obtém indDocumentoCPF
	 *
	 * @return boolean
	 */
	public boolean isIndDocumentoCPF() {
		return indDocumentoCPF;
	}
	/**
	 * Atribuí indDocumentoCPF
	 * @param indDocumentoCPF 
	 */
	public void setIndDocumentoCPF(boolean indDocumentoCPF) {
		this.indDocumentoCPF = indDocumentoCPF;
	}
	/**
	 * Obtém paginacao
	 *
	 * @return PaginacaoVO
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	/**
	 * Atribuí paginacao
	 * @param paginacao 
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	/**
	 * Obtém ordenacaoColuna
	 *
	 * @return OrdenacaoColuna
	 */
	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}
	/**
	 * Atribuí ordenacaoColuna
	 * @param ordenacaoColuna 
	 */
	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
	/**
	 * Obtém serie
	 *
	 * @return Integer
	 */
	public Integer getSerie() {
		return serie;
	}
	
	/**
	 * Atribuí serie
	 * @param serie 
	 */
	public void setSerie(Integer serie) {
		this.serie = serie;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	
	public void setNumeroDocumento(String tipoDocumento) {
		this.numeroDocumento = tipoDocumento;
	}
}