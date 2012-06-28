package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ConsultaFollowupCadastroDTO implements Serializable {

	private static final long serialVersionUID = -7544892338598259055L;

	public ConsultaFollowupCadastroDTO() {}
	
	public ConsultaFollowupCadastroDTO( Long numeroCota, String nomeJornaleiro, String tipoAtualizacao,	
		    String statusAtual, Date dataVencimento) {
		this.numeroCota = numeroCota;	
		this.nomeJornaleiro = nomeJornaleiro;	
		this.tipoAtualizacao = tipoAtualizacao;	
		this.statusAtual = statusAtual;
		this.dataVencimento = dataVencimento;   	
	}

	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private Long numeroCota;	
	@Export(label = "Nome", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String nomeJornaleiro;	
	@Export(label = "Tipo Atualizacao", alignment=Alignment.CENTER, exhibitionOrder = 2)
    private String tipoAtualizacao;	
	@Export(label = "Status", alignment=Alignment.CENTER, exhibitionOrder = 2)
    private String statusAtual;
	@Export(label = "Data Vencto", alignment=Alignment.CENTER, exhibitionOrder = 2)
   	private Date dataVencimento;   	

	public Long getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Long numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}

	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}

	public String getTipoAtualizacao() {
		return tipoAtualizacao;
	}

	public void setTipoAtualizacao(String tipoAtualizacao) {
		this.tipoAtualizacao = tipoAtualizacao;
	}

	public String getStatusAtual() {
		return statusAtual;
	}

	public void setStatusAtual(String statusAtual) {
		this.statusAtual = statusAtual;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
}
