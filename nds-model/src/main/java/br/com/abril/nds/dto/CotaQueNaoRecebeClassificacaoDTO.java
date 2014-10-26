package br.com.abril.nds.dto;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaQueNaoRecebeClassificacaoDTO extends UsuarioLogDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6162837741216511779L;

	private Long idClassificacaoNaoRecebida;

	@Export(label = "Cota", alignment = Alignment.LEFT, exhibitionOrder = 0)
	private Integer numeroCota;

	@Export(label = "Nome", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private String nomePessoa;
	
	public Long getIdClassificacaoNaoRecebida() {
		return idClassificacaoNaoRecebida;
	}

	public void setIdClassificacaoNaoRecebida(Long idClassificacaoNaoRecebida) {
		this.idClassificacaoNaoRecebida = idClassificacaoNaoRecebida;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

}
