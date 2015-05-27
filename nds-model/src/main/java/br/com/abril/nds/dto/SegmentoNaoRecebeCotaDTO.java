package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * @author InfoA2 - Samuel Mendes
 * 
 *         <h1>Classe DTO que representa as informações do grid
 *         "Segmentos Que Não Recebem Cota" no menu Distribuição > Segmento Não
 *         Recebido</h1>
 */
@Exportable
public class SegmentoNaoRecebeCotaDTO{

	private Long segmentoNaoRecebidoId;
	
	@Export(label = "Segmento", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String nomeSegmento;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeUsuario;

	private Date dataAlteracao;
	
	@Export(label = "Data Alteração", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String dataAlteracaoFormatada;
	
	@Export(label = "Hora Alteração", alignment=Alignment.LEFT, exhibitionOrder = 4)
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

	public Long getSegmentoNaoRecebidoId() {
		return segmentoNaoRecebidoId;
	}

	public void setSegmentoNaoRecebidoId(Long segmentoNaoRecebidoId) {
		this.segmentoNaoRecebidoId = segmentoNaoRecebidoId;
	}

	
	
}
