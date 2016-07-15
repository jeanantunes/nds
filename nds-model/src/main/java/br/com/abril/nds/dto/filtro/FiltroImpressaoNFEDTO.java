package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroImpressaoNFEDTO implements Serializable {

	private static final long serialVersionUID = -750037999779899243L;

	private String tipoNFe;

	private Long codigoProduto;

	private String nomeProduto;

	private Date dataEmissao;
	
	private Date dataEmissaoInicial;
	
	private Date dataEmissaoFinal;

	private Date dataMovimentoInicial;

	private Date dataMovimentoFinal;

	private Long idCotaInicial;

	private Long idCotaFinal;

	private Integer idBoxInicial;

	private Integer idBoxFinal;

	private Long idRoteiro;

	private Long idRota;

	private Long idTipoEmissao;

	private Long idNaturezaOperacao;

	private List<Long> idsFornecedores;

	private List<Long> codigosProdutos;

	private List<Long> idsCotas;
	
	private List<Long> numerosNotas;
	
	private List<Long> listIdFornecedor;

	private PaginacaoVO paginacao;

	private ColunaOrdenacaoImpressaoNFE ordenacaoColuna;

	private NaturezaOperacao naturezaOperacao;
	
	private boolean bandGerado;
	
	private Long numeroNotaDe;
	
	private Long numeroNotaAte;
	
	private List<Integer> cotasDasNotasJaFiltradas;
	
	public enum ColunaOrdenacaoImpressaoNFE {

		COTA("cota");

		private String nomeColuna;

		private ColunaOrdenacaoImpressaoNFE(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}

		@Override
		public String toString() {
			return this.nomeColuna;
		}

		public static ColunaOrdenacaoImpressaoNFE getPorDescricao(String descricao) {
			for(ColunaOrdenacaoImpressaoNFE coluna: ColunaOrdenacaoImpressaoNFE.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	public boolean isFiltroValido() {
		if(	this.validarFiltro() != null ) {
			return false;			
		}

		return true;
	}

	/**
	 * Valida se o filtro é válido. Se for, retorna null, se não, retorna os erros
	 * 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> validarFiltro() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("PT_BR"));
		HashMap<String,String> erros = new HashMap<String,String>();

		if(	(this.getTipoNFe() != null && this.getTipoNFe().isEmpty()) ) {
			erros.put("tipoNFe", "O Tipo de NF-e é inválido.");
		}

		HashMap<String, String> erro = this.validarDataMovimentoInicial();
		if(erro != null) {
			erros.putAll(erro);
		}

		erro = this.validarDataMovimentoFinal();
		if(erro != null) {
			erros.putAll(erro);
		}

		if( this.getDataEmissao() != null && !DateUtil.isValidDatePTBR(sdf.format(this.getDataEmissao())) ) {
			erros.put("dataEmissao", "A Data de Emissão é inválida.");
		}

		if( this.getIdCotaInicial() != null && this.getIdCotaInicial().longValue() < 0 ) {
			erros.put("idCotaInicial", "A Cota Inicial é inválida.");
		}

		if( this.getIdCotaFinal() != null ) {
			if( this.getIdCotaFinal().longValue() < 0 ) {
				erros.put("idCotaFinal", "A Cota Inicial é inválida.");
			}

			if(erros.get("idCotaFinal") == null && this.getIdCotaInicial() != null) {
				if(this.getIdCotaFinal().longValue() < this.getIdCotaInicial().longValue()) {
					erros.put("idCotaFinal", "A Cota Final é inválida por ser menor que a Cota Inicial.");
				}
			}
		}

		if( this.getIdBoxInicial() != null && this.getIdBoxInicial().longValue() < 0) {
			erros.put("idBoxInicial", "O Box Inicial é inválido.");
		}

		if( this.getIdBoxFinal() != null ) {
			if( this.getIdBoxFinal().longValue() < 0 ) {
				erros.put("idBoxFinal", "O Box Inicial é inválido.");
			}

			if(erros.get("idBoxFinal") == null && this.getIdBoxFinal() != null) {
				if(this.getIdBoxFinal().longValue() < this.getIdBoxInicial().longValue()) {
					erros.put("idBoxFinal", "O Box Final é inválido por ser menor que o Box Inicial.");
				}
			}
		}

		return (erros != null && erros.size() > 0) ? erros : null;
	}

	/*
	 * Validadores
	 */

	public HashMap<String,String> validarDataMovimentoInicial() {
		HashMap<String,String> erros = new HashMap<String,String>();

		if( this.getDataMovimentoInicial() != null 
				&& !DateUtil.isValidDatePTBR(new SimpleDateFormat("dd/MM/yyyy", new Locale("PT_BR")).format(this.getDataMovimentoInicial())) ) {
			erros.put("dataInicialMovimento", "A Data Inicial de Movimento é inválida.");
		}

		return !erros.isEmpty() ? erros : null; 
	}

	public HashMap<String,String> validarDataMovimentoFinal() {
		HashMap<String,String> erros = new HashMap<String,String>();

		if( this.getDataMovimentoFinal() != null
				&& !DateUtil.isValidDatePTBR(new SimpleDateFormat("dd/MM/yyyy", new Locale("PT_BR")).format(this.getDataMovimentoFinal())) ) {

			erros.put("dataInicialMovimento", "A Data Final de Movimento é inválida.");

			if(erros.get("dataFinalMovimento") == null) {
				if( DateUtil.isDataInicialMaiorDataFinal(this.getDataMovimentoInicial(), this.getDataMovimentoFinal()) ) {
					erros.put("dataFinalMovimento", "A Data Final de Movimento é inválida por ser menor que a Data Inicial.");
				}
			}			
		}

		return !erros.isEmpty() ? erros : null; 
	}

	/*
	 * Getters / Setters
	 */

	public String getTipoNFe() {
		return tipoNFe;
	}

	public Long getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Long codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Date getDataEmissaoInicial() {
		return dataEmissaoInicial;
	}

	public void setDataEmissaoInicial(Date dataEmissaoInicial) {
		this.dataEmissaoInicial = dataEmissaoInicial;
	}

	public Date getDataEmissaoFinal() {
		return dataEmissaoFinal;
	}

	public void setDataEmissaoFinal(Date dataEmissaoFinal) {
		this.dataEmissaoFinal = dataEmissaoFinal;
	}

	public Date getDataMovimentoInicial() {
		return dataMovimentoInicial;
	}

	public void setDataMovimentoInicial(Date dataMovimentoInicial) {
		this.dataMovimentoInicial = dataMovimentoInicial;
	}

	public Date getDataMovimentoFinal() {
		return dataMovimentoFinal;
	}

	public void setDataMovimentoFinal(Date dataMovimentoFinal) {
		this.dataMovimentoFinal = dataMovimentoFinal;
	}

	public Long getIdCotaInicial() {
		return idCotaInicial;
	}

	public void setIdCotaInicial(Long idCotaInicial) {
		this.idCotaInicial = idCotaInicial;
	}

	public Long getIdCotaFinal() {
		return idCotaFinal;
	}

	public void setIdCotaFinal(Long idCotaFinal) {
		this.idCotaFinal = idCotaFinal;
	}

	public Integer getIdBoxInicial() {
		return idBoxInicial;
	}

	public void setIdBoxInicial(Integer idBoxInicial) {
		this.idBoxInicial = idBoxInicial;
	}

	public Integer getIdBoxFinal() {
		return idBoxFinal;
	}

	public void setIdBoxFinal(Integer idBoxFinal) {
		this.idBoxFinal = idBoxFinal;
	}

	public void setTipoNFe(String tipoNFe) {
		this.tipoNFe = tipoNFe;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Long getIdRoteiro() {
		return idRoteiro;
	}

	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	public Long getIdRota() {
		return idRota;
	}

	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	public Long getIdTipoEmissao() {
		return idTipoEmissao;
	}

	public void setIdTipoEmissao(Long idTipoEmissao) {
		this.idTipoEmissao = idTipoEmissao;
	}

	public List<Long> getIdsFornecedores() {
		return idsFornecedores;
	}

	public void setIdsFornecedores(List<Long> idsFornecedores) {
		this.idsFornecedores = idsFornecedores;
	}

	public List<Long> getCodigosProdutos() {
		return codigosProdutos;
	}

	public void setCodigosProdutos(List<Long> codigosProdutos) {
		this.codigosProdutos = codigosProdutos;
	}

	public List<Long> getIdsCotas() {
		return idsCotas;
	}

	public void setIdsCotas(List<Long> idsCotas) {
		this.idsCotas = idsCotas;
	}

	public List<Long> getNumerosNotas() {
		return numerosNotas;
	}

	public void setNumerosNotas(List<Long> numerosNotas) {
		this.numerosNotas = numerosNotas;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacaoImpressaoNFE getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(ColunaOrdenacaoImpressaoNFE ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public Long getIdNaturezaOperacao() {
		return idNaturezaOperacao;
	}

	public void setIdNaturezaOperacao(Long idNaturezaOperacao) {
		this.idNaturezaOperacao = idNaturezaOperacao;
	}

	public List<Long> getListIdFornecedor() {
		return listIdFornecedor;
	}

	public void setListIdFornecedor(List<Long> listIdFornecedor) {
		this.listIdFornecedor = listIdFornecedor;
	}

	public NaturezaOperacao getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(NaturezaOperacao naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public boolean isBandGerado() {
		return bandGerado;
	}

	public void setBandGerado(boolean bandGerado) {
		this.bandGerado = bandGerado;
	}

	public Long getNumeroNotaDe() {
		return numeroNotaDe;
	}

	public void setNumeroNotaDe(Long numeroNotaDe) {
		this.numeroNotaDe = numeroNotaDe;
	}

	public Long getNumeroNotaAte() {
		return numeroNotaAte;
	}

	public void setNumeroNotaAte(Long numeroNotaAte) {
		this.numeroNotaAte = numeroNotaAte;
	}

	public List<Integer> getCotasDasNotasJaFiltradas() {
		return cotasDasNotasJaFiltradas;
	}

	public void setCotasDasNotasJaFiltradas(List<Integer> cotasDasNotasJaFiltradas) {
		this.cotasDasNotasJaFiltradas = cotasDasNotasJaFiltradas;
	}
	
}