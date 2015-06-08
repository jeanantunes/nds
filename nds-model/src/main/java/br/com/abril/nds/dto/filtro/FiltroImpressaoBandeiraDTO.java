package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroImpressaoBandeiraDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7314601525203193630L;

	private Long codigoProduto;

	private String nomeProduto;

	private Date dataEmissao;
	
	private Date dataEmissaoInicial;
	
	private Date dataEmissaoFinal;

	private Long idTipoEmissao;

	private Long idNaturezaOperacao;

	private List<Long> idsFornecedores;

	private List<Long> codigosProdutos;

	private List<Long> numerosNotas;
	
	private List<Long> listIdFornecedor;
	
	private ColunaOrdenacaoImpressaoBandeira ordenacaoColuna;

	private PaginacaoVO paginacao;

	public enum ColunaOrdenacaoImpressaoBandeira {

		EDITOR("editor");

		private String nomeColuna;

		private ColunaOrdenacaoImpressaoBandeira(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}

		@Override
		public String toString() {
			return this.nomeColuna;
		}

		public static ColunaOrdenacaoImpressaoBandeira getPorDescricao(String descricao) {
			for(ColunaOrdenacaoImpressaoBandeira coluna: ColunaOrdenacaoImpressaoBandeira.values()) {
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

		return (erros != null && erros.size() > 0) ? erros : null;
	}

	/*
	 * Validadores
	 */

	public HashMap<String,String> validarDataMovimentoInicial() {
		HashMap<String,String> erros = new HashMap<String,String>();

		if( this.getDataEmissaoInicial() != null 
				&& !DateUtil.isValidDatePTBR(new SimpleDateFormat("dd/MM/yyyy", new Locale("PT_BR")).format(this.getDataEmissaoInicial())) ) {
			erros.put("dataInicialMovimento", "A Data Inicial de Movimento é inválida.");
		}

		return !erros.isEmpty() ? erros : null; 
	}

	public HashMap<String,String> validarDataMovimentoFinal() {
		HashMap<String,String> erros = new HashMap<String,String>();

		if( this.getDataEmissaoFinal() != null
				&& !DateUtil.isValidDatePTBR(new SimpleDateFormat("dd/MM/yyyy", new Locale("PT_BR")).format(this.getDataEmissaoFinal())) ) {

			erros.put("dataInicialMovimento", "A Data Final de Movimento é inválida.");

			if(erros.get("dataFinalMovimento") == null) {
				if( DateUtil.isDataInicialMaiorDataFinal(this.getDataEmissaoInicial(), this.getDataEmissaoFinal()) ) {
					erros.put("dataFinalMovimento", "A Data Final de Movimento é inválida por ser menor que a Data Inicial.");
				}
			}			
		}

		return !erros.isEmpty() ? erros : null; 
	}

	/*
	 * Getters / Setters
	 */

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

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
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

	public ColunaOrdenacaoImpressaoBandeira getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(ColunaOrdenacaoImpressaoBandeira ordenacaoColuna) {
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
}