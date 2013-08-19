package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroDetalheEstoqueCotaDTO implements Serializable {

	private static final long serialVersionUID = 7622821251078514008L;

	private Long idDiferenca;

	private PaginacaoVO paginacao;

	/**
	 * @return the idDiferenca
	 */
	public Long getIdDiferenca() {
		return idDiferenca;
	}

	/**
	 * @param idDiferenca the idDiferenca to set
	 */
	public void setIdDiferenca(Long idDiferenca) {
		this.idDiferenca = idDiferenca;
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
