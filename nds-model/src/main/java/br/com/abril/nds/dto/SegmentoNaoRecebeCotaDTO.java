package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.util.DateUtil;

/**
 * @author InfoA2 - Samuel Mendes
 * 
 *         <h1>Classe DTO que representa as informações do grid
 *         "Segmentos Que Não Recebem Cota" no menu Distribuição > Segmento Não
 *         Recebido</h1>
 */
public class SegmentoNaoRecebeCotaDTO {

	private Long idTipoSegmento;
	private String nomeSegmento;
	private String nomeUsuario;

	private Date dataAlteracao;
	private String dataAlteracaoFormatada;
	private String horaAlteracaoFormatada;

	public String getNomeSegmento() {
		return nomeSegmento;
	}

	public void setNomeSegmento(String nomeSegmento) {
		this.nomeSegmento = nomeSegmento;
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

	public Long getIdTipoSegmento() {
		return idTipoSegmento;
	}

	public void setIdTipoSegmento(Long idTipoSegmento) {
		this.idTipoSegmento = idTipoSegmento;
	}
}
