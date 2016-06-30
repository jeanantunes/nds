package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsultaNotaEnvioDTO  implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 3159178471266292426L;
    
    private Date dataEmissao;
	
	private List<Long> idFornecedores;
	
	private Long idRota;
	
	private Long idRoteiro;

	private Intervalo<Date> intervaloMovimento;
	
	private Intervalo<Integer> intervaloCota;
	
	private Intervalo<Integer> intervaloBox;
	
	private SituacaoCadastro cadastro;
	
	private PaginacaoVO paginacaoVO;
	
	private String exibirNotasEnvio;
	
	private boolean filtroRoteiroEspecial;
	
	
	private boolean isImpressao;
	
	private boolean isEnvioEmail;
	
	
	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	/**
	 * @return the idFornecedores
	 */
	public List<Long> getIdFornecedores() {
		return idFornecedores;
	}

	/**
	 * @param idFornecedores the idFornecedores to set
	 */
	public void setIdFornecedores(List<Long> idFornecedores) {
		this.idFornecedores = idFornecedores;
	}

	/**
	 * @return the idRota
	 */
	public Long getIdRota() {
		return idRota;
	}

	/**
	 * @param idRota the idRota to set
	 */
	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	/**
	 * @return the idRoteiro
	 */
	public Long getIdRoteiro() {
		return idRoteiro;
	}

	/**
	 * @param idRoteiro the idRoteiro to set
	 */
	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	/**
	 * @return the intervaloMovimento
	 */
	public Intervalo<Date> getIntervaloMovimento() {
		return intervaloMovimento;
	}

	/**
	 * @param intervaloMovimento the intervaloMovimento to set
	 */
	public void setIntervaloMovimento(Intervalo<Date> intervaloMovimento) {
		this.intervaloMovimento = intervaloMovimento;
	}

	/**
	 * @return the intervaloCota
	 */
	public Intervalo<Integer> getIntervaloCota() {
		return intervaloCota;
	}

	/**
	 * @param intervaloCota the intervaloCota to set
	 */
	public void setIntervaloCota(Intervalo<Integer> intervaloCota) {
		this.intervaloCota = intervaloCota;
	}

	/**
	 * @return the intervaloBox
	 */
	public Intervalo<Integer> getIntervaloBox() {
		return intervaloBox;
	}

	/**
	 * @param intervaloBox the intervaloBox to set
	 */
	public void setIntervaloBox(Intervalo<Integer> intervaloBox) {
		this.intervaloBox = intervaloBox;
	}

	/**
	 * @return the paginacaoVO
	 */
	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	/**
	 * @param paginacaoVO the paginacaoVO to set
	 */
	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}

	public SituacaoCadastro getCadastro() {
		return cadastro;
	}

	public void setCadastro(SituacaoCadastro cadastro) {
		this.cadastro = cadastro;
	}

	public String getExibirNotasEnvio() {
		return exibirNotasEnvio;
	}

	public void setExibirNotasEnvio(String exibirNotasEnvio) {
		this.exibirNotasEnvio = exibirNotasEnvio;
	}
	
	public boolean isFiltroBoxEspecial() {

		if (this.intervaloBox != null) {
			return (this.intervaloBox.getDe() != null && this.intervaloBox.getAte() != null) &&
					(this.intervaloBox.getDe() == 0 && this.intervaloBox.getAte() == 0);
		}

		return false;
	}

	/**
	 * @return the filtroRoteiroEspecial
	 */
	public boolean isFiltroEspecial() {
		return filtroRoteiroEspecial || this.isFiltroBoxEspecial();
	}

	/**
	 * @param filtroRoteiroEspecial the filtroRoteiroEspecial to set
	 */
	public void setFiltroRoteiroEspecial(boolean filtroRoteiroEspecial) {
		this.filtroRoteiroEspecial = filtroRoteiroEspecial;
	}

	public boolean isImpressao() {
		return isImpressao;
	}

	public void setImpressao(boolean isImpressao) {
		this.isImpressao = isImpressao;
	}

	public boolean isEnvioEmail() {
		return isEnvioEmail;
	}

	public void setEnvioEmail(boolean isEnvioEmail) {
		this.isEnvioEmail = isEnvioEmail;
	}
	
	
}
