package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroImpressaoNFEDTO implements Serializable {

	private static final long serialVersionUID = -750037999779899243L;
	
	private String tipoNFe;
	
	private Date dataInicialMovimento;
	
	private Date dataFinalMovimento;
	
	private Date dataEmissao;
	
	private Long idRoteiro;
	
	private Long idRota;
	
	private Long idTipoEmissao;
	
	private Long idCotaInicial;
	
	private Long idCotaFinal;
	
	private Long idBoxInicial;
	
	private Long idBoxFinal;
	
	private Set<Long> idsFornecedores;
	
	private Set<Long> idsProdutos;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoImpressaoNFE ordenacaoColuna;
	
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
	 * Valida se o filtro é válido. Se for retorna null, se não, retorna os erros
	 * 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> validarFiltro() {
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("PT_BR"));
		HashMap<String,String> erros = new HashMap<String,String>();
		
		if(	(this.getTipoNFe() == null || this.getTipoNFe() != null && this.getTipoNFe().isEmpty()) ) {
			erros.put("tipoNFe", "O Tipo de NF-e é inválido.");
		}
		
		if( this.getDataInicialMovimento() != null 
				&& !DateUtil.isValidDatePTBR(sdf.format(this.getDataInicialMovimento())) ) {
			erros.put("dataInicialMovimento", "A Data Inicial é inválida.");
		}
			
		if( this.getDataFinalMovimento() != null) {
		
			if( !DateUtil.isValidDatePTBR(sdf.format(this.getDataFinalMovimento())) ) {
				erros.put("dataInicialMovimento", "A Data Final é inválida.");
			} 
			
			if(erros.get("dataFinalMovimento") == null) {
				if( DateUtil.isDataInicialMaiorDataFinal(this.getDataInicialMovimento(), this.getDataFinalMovimento()) ) {
					erros.put("dataFinalMovimento", "A Data Final é inválida por ser menor que a Data Inicial.");
				}
			}			
		}
				
		if( this.getDataEmissao() == null
				|| (this.getDataEmissao() != null && !DateUtil.isValidDatePTBR(sdf.format(this.getDataEmissao()))) ) {
			erros.put("dataEmissao", "A Data de Emissão é inválida.");
		}
		
		if( this.getIdCotaInicial() != null && this.getIdCotaInicial().longValue() < 0 ) {
			erros.put("idCotaInicial", "A Cota Inicial é inválida.");
		}
		
		if( this.getIdCotaFinal() != null ) {
			if( this.getIdCotaFinal().longValue() < 0 ) {
				erros.put("idCotaFinal", "A Cota Inicial é inválida.");
			}
			
			if(erros.get("idCotaFinal") == null) {
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
			
			if(erros.get("idBoxFinal") == null) {
				if(this.getIdCotaFinal().longValue() < this.getIdCotaInicial().longValue()) {
					erros.put("idBoxFinal", "O Box Final é inválido por ser menor que o Box Inicial.");
				}
			}
		}
		
		return (erros != null && erros.size() > 0) ? erros : null;
		
		//TODO : Sérgio : Verificar se necessita validação
		/*
		if( this.getIdRoteiro() != null ) { }
		if( this.getIdRota() != null ) { }
		if( this.getIdTipoEmissao() != null ) { }
		if( this.getIdsFornecedores() != null ) { }
		if( this.getIdsProdutos() != null ) { }
		if( this.getPaginacao() != null ) { }
		if( this.getOrdenacaoColuna() != null) { }
		*/
		
	}
	
	/*
	 * Getters / Setters
	 */
	
	public String getTipoNFe() {
		return tipoNFe;
	}

	public void setTipoNFe(String tipoNFe) {
		this.tipoNFe = tipoNFe;
	}

	public Date getDataInicialMovimento() {
		return dataInicialMovimento;
	}

	public void setDataInicialMovimento(Date dataInicialMovimento) {
		this.dataInicialMovimento = dataInicialMovimento;
	}

	public Date getDataFinalMovimento() {
		return dataFinalMovimento;
	}

	public void setDataFinalMovimento(Date dataFinalMovimento) {
		this.dataFinalMovimento = dataFinalMovimento;
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

	public Long getIdBoxInicial() {
		return idBoxInicial;
	}

	public void setIdBoxInicial(Long idBoxInicial) {
		this.idBoxInicial = idBoxInicial;
	}

	public Long getIdBoxFinal() {
		return idBoxFinal;
	}

	public void setIdBoxFinal(Long idBoxFinal) {
		this.idBoxFinal = idBoxFinal;
	}

	public Set<Long> getIdsFornecedores() {
		return idsFornecedores;
	}

	public void setIdsFornecedores(Set<Long> idsFornecedores) {
		this.idsFornecedores = idsFornecedores;
	}

	public Set<Long> getIdsProdutos() {
		return idsProdutos;
	}

	public void setIdsProdutos(Set<Long> idsProdutos) {
		this.idsProdutos = idsProdutos;
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

}
