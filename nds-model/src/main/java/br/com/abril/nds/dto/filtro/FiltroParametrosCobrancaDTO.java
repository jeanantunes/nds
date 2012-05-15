package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de formas de cobrança
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroParametrosCobrancaDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Export(label = "Forma Pagamento")
	private TipoCobranca tipoCobranca;

	@Export(label = "Banco")
	private Long idBanco;

	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaParametrosCobranca ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroParametrosCobrancaDTO(){

	}
	
	/**
	 * Construtor.
	 */
	public FiltroParametrosCobrancaDTO(Long idBanco){
		this.idBanco=idBanco;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroParametrosCobrancaDTO(TipoCobranca tipoCobranca){
		this.tipoCobranca=tipoCobranca;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroParametrosCobrancaDTO(Long idBanco, TipoCobranca tipoCobranca){
		this.idBanco=idBanco;
		this.tipoCobranca=tipoCobranca;
	}
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaParametrosCobranca{
		
		TIPO_COBRANCA("forma"),
		NOME_BANCO("banco"),
		VALOR_MINIMO_EMISSAO("vlr_minimo_emissao"),
		ACUMULA_DIVIDA("acumula_divida"),
		COBRANCA_UNIFICADA("cobranca_unificada"),
		FORMA_EMISSAO("formaEmissao"),
		ENVIO_EMAIL("envio_email");
		
		private String nomeColuna;
		
		private OrdenacaoColunaParametrosCobranca(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
	}
	
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public OrdenacaoColunaParametrosCobranca getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipoCobranca == null) ? 0 : tipoCobranca.hashCode());
		result = prime * result + ((idBanco == null) ? 0 : idBanco.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroParametrosCobrancaDTO other = (FiltroParametrosCobrancaDTO) obj;
		if (tipoCobranca != other.tipoCobranca)
			return false;
		if (idBanco == null) {
			if (other.idBanco != null)
				return false;
		} else if (!idBanco.equals(other.idBanco))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}

	
}
