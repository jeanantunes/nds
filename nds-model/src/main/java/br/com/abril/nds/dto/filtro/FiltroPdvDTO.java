package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroPdvDTO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private Long idCota;
	
	/**
	 * Identificador do histórico de titularidade da
	 * cota
	 */
	private Long idHistorico;
	
	private ColunaOrdenacao colunaOrdenacao;
	
	private PaginacaoVO paginacao;
	
	public enum ColunaOrdenacao{
		
		NOME_PDV("nomePdv"),
		TIPO_PONTO("tipoPonto"),
		CONTATO("contato"),
		TELEFONE("telefone"),
		ENDERECO("endereco"),
		PRINCIPAL("principal"),
		STATUS("status"),
		FATURAMENTO("faturamento");
		
		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/**
     * @return the idHistorico
     */
    public Long getIdHistorico() {
        return idHistorico;
    }

    /**
     * @param idHistorico the idHistorico to set
     */
    public void setIdHistorico(Long idHistorico) {
        this.idHistorico = idHistorico;
    }

    /**
	 * @return the colunaOrdenacao
	 */
	public ColunaOrdenacao getColunaOrdenacao() {
		return colunaOrdenacao;
	}

	/**
	 * @param colunaOrdenacao the colunaOrdenacao to set
	 */
	public void setColunaOrdenacao(ColunaOrdenacao colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
	}

	/**
	 * @return the paginacao
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	/**
	 * @param paginacao the paginacao to set
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	
	
	
	
}
