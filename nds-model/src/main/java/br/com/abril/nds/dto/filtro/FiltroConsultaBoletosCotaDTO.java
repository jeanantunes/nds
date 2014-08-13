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
public class FiltroConsultaBoletosCotaDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Export(label = "Cota")
	private Integer numeroCota;
	
	@Export(label = "Nome da Cota")
	private String nomeCota;
	
	@Export(label = "Data de Vencimento Inicial")
	private Date dataVencimentoDe;
	
	@Export(label = "Data de Vencimento Final")
	private Date dataVencimentoAte;
	
	@Export(label = "Status")
	private StatusCobranca status;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaBoletos ordenacaoColuna;
	
	
	/**
	 * Construtor padrão.
	 */
	public FiltroConsultaBoletosCotaDTO(){

	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaBoletosCotaDTO(Integer numeroCota,
			                            Date dataVencimentoDe,
			                            Date dataVencimentoAte,
			                            StatusCobranca status){
		this.numeroCota=numeroCota;
		this.dataVencimentoDe=dataVencimentoDe;
		this.dataVencimentoAte=dataVencimentoAte;
		this.status=status;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaBoletosCotaDTO(Integer numeroCota,
			                            Date dataVencimentoDe,
			                            Date dataVencimentoAte){
		this.numeroCota=numeroCota;
		this.dataVencimentoDe=dataVencimentoDe;
		this.dataVencimentoAte=dataVencimentoAte;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaBoletosCotaDTO(Integer numeroCota,
			                            Date dataVencimentoDe){
		this.numeroCota=numeroCota;
		this.dataVencimentoDe=dataVencimentoDe;
	}
	
	/**
	 * Construtor.
	 */
	public FiltroConsultaBoletosCotaDTO(Integer numeroCota){
		this.numeroCota=numeroCota;
	}
	
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColunaBoletos{
		
		NOSSO_NUMERO("nossoNumero"),
		DATA_EMISSAO("dtEmissao"),
		DATA_VENCIMENTO("dtVencimento"),
		DATA_PAGAMENTO("dtPagto"),
		ENCARGOS("encargos"),
		VALOR("valor"),
		TIPO_BAIXA("tipoBaixa"),
		STATUS_COBRANCA("status");
		
		private String nomeColuna;
		
		private OrdenacaoColunaBoletos(String nomeColuna) {
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
	
	public Date getDataVencimentoDe() {
		return dataVencimentoDe;
	}
	
	public void setDataVencimentoDe(Date dataVencimentoDe) {
		this.dataVencimentoDe = dataVencimentoDe;
	}
	
	public Date getDataVencimentoAte() {
		return dataVencimentoAte;
	}
	
	public void setDataVencimentoAte(Date dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}
	
	public StatusCobranca getStatus() {
		return status;
	}
	
	public void setStatus(StatusCobranca status) {
		this.status = status;
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

	public OrdenacaoColunaBoletos getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColunaBoletos ordenacaoColuna) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dataVencimentoAte == null) ? 0 : dataVencimentoAte
						.hashCode());
		result = prime
				* result
				+ ((dataVencimentoDe == null) ? 0 : dataVencimentoDe.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result
				+ ((status == null) ? 0 : status.hashCode());
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
		FiltroConsultaBoletosCotaDTO other = (FiltroConsultaBoletosCotaDTO) obj;
		if (dataVencimentoAte == null) {
			if (other.dataVencimentoAte != null)
				return false;
		} else if (!dataVencimentoAte.equals(other.dataVencimentoAte))
			return false;
		if (dataVencimentoDe == null) {
			if (other.dataVencimentoDe != null)
				return false;
		} else if (!dataVencimentoDe.equals(other.dataVencimentoDe))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
}