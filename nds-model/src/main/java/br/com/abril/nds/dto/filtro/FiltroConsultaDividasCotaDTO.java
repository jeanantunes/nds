package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de boletos
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroConsultaDividasCotaDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Export(label = "Cota")
	private Integer numeroCota;
	
	@Export(label = "Nome da Cota")
	private String nomeCota;
	
	@Export(label = "Data de Vencimento")
	private Date dataVencimento;
	
	
	
	@Export(label = "Status da Cobrança")
	private StatusCobranca statusCobranca;
	
	private boolean acumulaDivida;
	
	@Export(label = "Acumula Divida")
	private String descricaoAcumulaDivida;

	private String nossoNumero;
	
	private boolean somenteBaixadas;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaDividas ordenacaoColuna;
	
	/**
	 * Construtor padrão.
	 */
	public FiltroConsultaDividasCotaDTO(){

	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaDividasCotaDTO(Integer numeroCota){
		this.numeroCota=numeroCota;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaDividasCotaDTO(Integer numeroCota,
			                            Date dataVencimento){
		this.numeroCota=numeroCota;
		this.dataVencimento=dataVencimento;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaDividasCotaDTO(Integer numeroCota,
			                            Date dataVencimento,
			                            StatusCobranca statusCobranca){
		this.numeroCota=numeroCota;
		this.dataVencimento=dataVencimento;
		this.statusCobranca = statusCobranca;
	}
	
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaDividas{
		
		CODIGO("codigo"),
		NOME_COTA("nome"),
		DATA_EMISSAO("dataEmissao"),
		DATA_VENCIMENTO("dataVencimento"),
		VALOR("valor");
		
		private String nomeColuna;
		
		private OrdenacaoColunaDividas(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	/**
	 * @return the somenteBaixadas
	 */
	public boolean isSomenteBaixadas() {
		return somenteBaixadas;
	}

	/**
	 * @param somenteBaixadas the somenteBaixadas to set
	 */
	public void setSomenteBaixadas(boolean somenteBaixadas) {
		this.somenteBaixadas = somenteBaixadas;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public OrdenacaoColunaDividas getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColunaDividas ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public StatusCobranca getStatusCobranca() {
		return statusCobranca;
	}

	public void setStatusCobranca(StatusCobranca statusCobranca) {
		this.statusCobranca = statusCobranca;
	}

	public boolean isAcumulaDivida() {
		return acumulaDivida;
	}

	public void setAcumulaDivida(boolean acumulaDivida) {
		this.acumulaDivida = acumulaDivida;
		setDescricaoAcumulaDivida(acumulaDivida);
	}

	/**
	 * @return tradução da opção acumulaDivida
	 */
	public String getDescricaoAcumulaDivida() {
		return descricaoAcumulaDivida;
	}

	/**
	 * Traduz acumulaDivida de boolean para String
	 * @param acumulaDivida
	 */
	public void setDescricaoAcumulaDivida(boolean acumulaDivida) {
		this.descricaoAcumulaDivida = (acumulaDivida?"Sim":"Não");
	}

	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataVencimento == null) ? 0 : dataVencimento.hashCode());
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
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
		FiltroConsultaDividasCotaDTO other = (FiltroConsultaDividasCotaDTO) obj;
		if (dataVencimento == null) {
			if (other.dataVencimento != null)
				return false;
		} else if (!dataVencimento.equals(other.dataVencimento))
			return false;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
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
