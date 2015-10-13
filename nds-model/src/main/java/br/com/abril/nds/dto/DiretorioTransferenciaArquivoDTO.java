package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroDTO;

public class DiretorioTransferenciaArquivoDTO extends FiltroDTO  implements Serializable {

	private static final long serialVersionUID = 2586153337951366320L;
	
	private Long idDiretorio;
	private String nomeDiretorio;
	private String pastaDiretorio;

	public Long getIdDiretorio() {
		return idDiretorio;
	}
	public void setIdDiretorio(Long idDiretorio) {
		this.idDiretorio = idDiretorio;
	}
	public String getNomeDiretorio() {
		return nomeDiretorio;
	}
	public void setNomeDiretorio(String nomeDiretorio) {
		this.nomeDiretorio = nomeDiretorio;
	}
	public String getPastaDiretorio() {
		return pastaDiretorio;
	}
	public void setPastaDiretorio(String pastaDiretorio) {
		this.pastaDiretorio = pastaDiretorio;
	}
	
}
