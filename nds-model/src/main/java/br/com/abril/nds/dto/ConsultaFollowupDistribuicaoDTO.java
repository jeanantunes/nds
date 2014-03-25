package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class ConsultaFollowupDistribuicaoDTO implements Serializable {

	private static final long serialVersionUID = 7165488409804743705L;

	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String nomeJornaleiro;
	
	@Export(label = "Mensagem", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String mensagem = "Ajuste prestes a expirar.";
	
	@Export(label = "Dias restantes", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private Integer qtdDiasRestantes;
	
	private PaginacaoVO paginacao;
	
	public ConsultaFollowupDistribuicaoDTO() {}
	
	public ConsultaFollowupDistribuicaoDTO(Integer numeroCota,
			String nomeJornaleiro, String mensagem, Integer qtdDiasRestantes) {
		super();
		this.numeroCota = numeroCota;
		this.nomeJornaleiro = nomeJornaleiro;
		this.mensagem = mensagem;
		this.qtdDiasRestantes = qtdDiasRestantes;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}

	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}

	public String getMensagem() {
		return mensagem;
	}

//	public void setMensagem(String mensagem) {
//		this.mensagem = mensagem;
//	}

	public Integer getQtdDiasRestantes() {
		return qtdDiasRestantes;
	}

	public void setQtdDiasRestantes(Integer qtdDiasRestantes) {
		this.qtdDiasRestantes = qtdDiasRestantes;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	
}
