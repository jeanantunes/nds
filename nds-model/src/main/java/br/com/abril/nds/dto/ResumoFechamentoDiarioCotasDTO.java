package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO com informações do resumo de fechamento diário das cotas.
 * 
 * @author Discover Technology
 */
@SuppressWarnings("serial")
public class ResumoFechamentoDiarioCotasDTO implements Serializable {

	private Long quantidadeTotal;
	
	private Long quantidadeAtivas;
	
	private Long quantidadeAusentesExpedicaoReparte;
	
	private Long quantidadeAusentesRecolhimentoEncalhe;
	
	private Long quantidadeNovas;
	
	private Long quantidadeInativas;
	
	private List<CotaResumoDTO> ausentesExpedicaoReparte;
	
	private List<CotaResumoDTO> ausentesRecolhimentoEncalhe;
	
	private List<CotaResumoDTO> novas;
	
	private List<CotaResumoDTO> inativas;

	/**
	 * Construtor padrão.
	 */
	public ResumoFechamentoDiarioCotasDTO() {
		
	}
	
	public ResumoFechamentoDiarioCotasDTO(Long quantidadeTotal,Long quantidadeAtivas,
										  Long quantidadeAusentesExpedicaoReparte,
										  Long quantidadeAusentesRecolhimentoEncalhe,
										  Long quantidadeNovas,
										  Long quantidadeInativas) {
		
		this.quantidadeTotal = quantidadeTotal;
		this.quantidadeAtivas = quantidadeAtivas;
		this.quantidadeAusentesExpedicaoReparte = quantidadeAusentesExpedicaoReparte;
		this.quantidadeAusentesRecolhimentoEncalhe = quantidadeAusentesRecolhimentoEncalhe;
		this.quantidadeNovas= quantidadeNovas;
		this.quantidadeInativas = quantidadeInativas;
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

	public Long getQuantidadeAusentesExpedicaoReparte() {
		return quantidadeAusentesExpedicaoReparte;
	}

	public void setQuantidadeAusentesExpedicaoReparte(
			Long quantidadeAusentesExpedicaoReparte) {
		this.quantidadeAusentesExpedicaoReparte = quantidadeAusentesExpedicaoReparte;
	}

	public Long getQuantidadeAusentesRecolhimentoEncalhe() {
		return quantidadeAusentesRecolhimentoEncalhe;
	}

	public void setQuantidadeAusentesRecolhimentoEncalhe(
			Long quantidadeAusentesRecolhimentoEncalhe) {
		this.quantidadeAusentesRecolhimentoEncalhe = quantidadeAusentesRecolhimentoEncalhe;
	}

	public Long getQuantidadeNovas() {
		return quantidadeNovas;
	}

	public void setQuantidadeNovas(Long quantidadeNovas) {
		this.quantidadeNovas = quantidadeNovas;
	}

	public Long getQuantidadeInativas() {
		return quantidadeInativas;
	}

	public void setQuantidadeInativas(Long quantidadeInativas) {
		this.quantidadeInativas = quantidadeInativas;
	}

	public List<CotaResumoDTO> getAusentesExpedicaoReparte() {
		return ausentesExpedicaoReparte;
	}

	public void setAusentesExpedicaoReparte(
			List<CotaResumoDTO> ausentesExpedicaoReparte) {
		this.ausentesExpedicaoReparte = ausentesExpedicaoReparte;
	}

	public List<CotaResumoDTO> getAusentesRecolhimentoEncalhe() {
		return ausentesRecolhimentoEncalhe;
	}

	public void setAusentesRecolhimentoEncalhe(
			List<CotaResumoDTO> ausentesRecolhimentoEncalhe) {
		this.ausentesRecolhimentoEncalhe = ausentesRecolhimentoEncalhe;
	}

	public List<CotaResumoDTO> getNovas() {
		return novas;
	}

	public void setNovas(List<CotaResumoDTO> novas) {
		this.novas = novas;
	}

	public List<CotaResumoDTO> getInativas() {
		return inativas;
	}

	public void setInativas(List<CotaResumoDTO> inativas) {
		this.inativas = inativas;
	}

}