package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * DTO com informações do resumo de fechamento diário das cotas.
 * 
 * @author Discover Technology
 */
@SuppressWarnings("serial")
public class ResumoFechamentoDiarioCotasDTO implements Serializable {

	private Long quantidadeTotal;
	
	private Long quantidadeAtivas;
	
	private List<CotaResumoDTO> ausentesExpedicaoReparte;
	
	private List<CotaResumoDTO> ausentesRecolhimentoEncalhe;
	
	private List<CotaResumoDTO> novas;
	
	private List<CotaResumoDTO> inativas;

	/**
	 * Construtor padrão.
	 */
	public ResumoFechamentoDiarioCotasDTO() {
		
	}
	
	/**
	 * Construtor.
	 * 
	 * @param quantidadeTotal - quantidade total de cotas
	 * @param quantidadeAtivas - quantidade de cotas ativas
	 * @param ausentesExpedicaoReparte - cotas ausentes na expedição do reparte
	 * @param ausentesRecolhimentoEncalhe - cotas ausentes no recolhimento do encalhe
	 * @param novas - cotas novas
	 * @param inativas - cotas inativas
	 */
	public ResumoFechamentoDiarioCotasDTO(Long quantidadeTotal, Long quantidadeAtivas,
										  List<CotaResumoDTO> ausentesExpedicaoReparte,
										  List<CotaResumoDTO> ausentesRecolhimentoEncalhe,
										  List<CotaResumoDTO> novas, List<CotaResumoDTO> inativas) {
		
		this.quantidadeTotal = quantidadeTotal;
		this.quantidadeAtivas = quantidadeAtivas;
		this.ausentesExpedicaoReparte = ausentesExpedicaoReparte;
		this.ausentesRecolhimentoEncalhe = ausentesRecolhimentoEncalhe;
		this.novas = novas;
		this.inativas = inativas;
	}
	
	/**
     * Enum com os tipos de resumo 
     *
     */
    public static enum TipoResumo {
            
    	TOTAL("total"), 
    	ATIVAS("ativas"), 
    	AUSENTES_REPARTE("ausentes-reparte"), 
    	AUSENTES_ENCALHE("ausentes-encalhe"), 
    	NOVAS("novas"), 
    	INATIVAS("inativas");
    	
    	private String descricao;
    	
    	private TipoResumo(String descricao) {
    		
    		this.descricao = descricao;
    	}
    	
    	public String getDescricao() {
    		
			return descricao;
		}
    }

	/**
	 * @return the quantidadeTotal
	 */
	public Long getQuantidadeTotal() {
		return quantidadeTotal;
	}

	/**
	 * @param quantidadeTotal the quantidadeTotal to set
	 */
	public void setQuantidadeTotal(Long quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}

	/**
	 * @return the quantidadeAtivas
	 */
	public Long getQuantidadeAtivas() {
		return quantidadeAtivas;
	}

	/**
	 * @param quantidadeAtivas the quantidadeAtivas to set
	 */
	public void setQuantidadeAtivas(Long quantidadeAtivas) {
		this.quantidadeAtivas = quantidadeAtivas;
	}

	/**
	 * @return the quantidadeAusentesExpedicaoReparte
	 */
	public Long getQuantidadeAusentesExpedicaoReparte() {
		
		if (this.ausentesExpedicaoReparte != null) {
			
			return new Long(this.ausentesExpedicaoReparte.size());
		}
		
		return 0L;
	}

	/**
	 * @return the quantidadeAusentesRecolhimentoEncalhe
	 */
	public Long getQuantidadeAusentesRecolhimentoEncalhe() {

		if (this.ausentesRecolhimentoEncalhe != null) {
			
			return new Long(this.ausentesRecolhimentoEncalhe.size());
		}
		
		return 0L;
	}

	/**
	 * @return the quantidadeNovas
	 */
	public Long getQuantidadeNovas() {
		
		if (this.novas != null) {
			
			return new Long(this.novas.size());
		}
		
		return 0L;
	}

	/**
	 * @return the quantidadeInativas
	 */
	public Long getQuantidadeInativas() {
		
		if (this.inativas != null) {
			
			return new Long(this.inativas.size());
		}
		
		return 0L;
	}

	/**
	 * @return the ausentesExpedicaoReparte
	 */
	public List<CotaResumoDTO> getAusentesExpedicaoReparte() {
		return ausentesExpedicaoReparte;
	}

	/**
	 * @param ausentesExpedicaoReparte the ausentesExpedicaoReparte to set
	 */
	public void setAusentesExpedicaoReparte(List<CotaResumoDTO> ausentesExpedicaoReparte) {
		this.ausentesExpedicaoReparte = ausentesExpedicaoReparte;
	}

	/**
	 * @return the ausentesRecolhimentoEncalhe
	 */
	public List<CotaResumoDTO> getAusentesRecolhimentoEncalhe() {
		return ausentesRecolhimentoEncalhe;
	}

	/**
	 * @param ausentesRecolhimentoEncalhe the ausentesRecolhimentoEncalhe to set
	 */
	public void setAusentesRecolhimentoEncalhe(
			List<CotaResumoDTO> ausentesRecolhimentoEncalhe) {
		this.ausentesRecolhimentoEncalhe = ausentesRecolhimentoEncalhe;
	}

	/**
	 * @return the novas
	 */
	public List<CotaResumoDTO> getNovas() {
		return novas;
	}

	/**
	 * @param novas the novas to set
	 */
	public void setNovas(List<CotaResumoDTO> novas) {
		this.novas = novas;
	}

	/**
	 * @return the inativas
	 */
	public List<CotaResumoDTO> getInativas() {
		return inativas;
	}

	/**
	 * @param inativas the inativas to set
	 */
	public void setInativas(List<CotaResumoDTO> inativas) {
		this.inativas = inativas;
	}
	
}
