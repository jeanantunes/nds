package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.DateUtil;

/**
 * @author InfoA2 - Samuel Mendes
 * 
 *         <h1>Classe DTO que representa as informações do grid
 *         "Cotas Que Não Recebem Segmento" no menu Distribuição > Segmento Não
 *         Recebido</h1>
 * 
 */

public class CotaNaoRecebeSegmentoDTO {

	private Long segmentoNaoRecebidoId;
	private Integer numeroCota;
	private SituacaoCadastro statusCota;
	private String nomeCota;
	private String nomeUsuario;

	private Date dataAlteracao;
	private String dataAlteracaoFormatada;
	private String horaAlteracaoFormatada;

	public Long getSegmentoNaoRecebidoId() {
		return segmentoNaoRecebidoId;
	}

	public void setSegmentoNaoRecebidoId(Long segmentoNaoRecebidoId) {
		this.segmentoNaoRecebidoId = segmentoNaoRecebidoId;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public SituacaoCadastro getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(SituacaoCadastro statusCota) {
		this.statusCota = statusCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
		
		this.dataAlteracaoFormatada = DateUtil.formatarDataPTBR(dataAlteracao);
		this.horaAlteracaoFormatada = DateUtil.formatarHoraMinuto(dataAlteracao);
	}

	public String getDataAlteracaoFormatada() {
		return dataAlteracaoFormatada;
	}

	public String getHoraAlteracaoFormatada() {
		return horaAlteracaoFormatada;
	}

}
