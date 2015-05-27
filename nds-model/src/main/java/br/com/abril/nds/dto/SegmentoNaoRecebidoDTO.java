package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public abstract class SegmentoNaoRecebidoDTO {

	private Long segmentoNaoRecebidoId;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeUsuario;

	private Date dataAlteracao;
	
	@Export(label = "Data Alteração", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String dataAlteracaoFormatada;
	
	@Export(label = "Hora Alteração", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String horaAlteracaoFormatada;

	public Long getSegmentoNaoRecebidoId() {
		return segmentoNaoRecebidoId;
	}

	public void setSegmentoNaoRecebidoId(Long segmentoNaoRecebidoId) {
		this.segmentoNaoRecebidoId = segmentoNaoRecebidoId;
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

	public void setDataAlteracaoFormatada(String dataAlteracaoFormatada) {
		this.dataAlteracaoFormatada = dataAlteracaoFormatada;
	}

	public String getHoraAlteracaoFormatada() {
		return horaAlteracaoFormatada;
	}

	public void setHoraAlteracaoFormatada(String horaAlteracaoFormatada) {
		this.horaAlteracaoFormatada = horaAlteracaoFormatada;
	}
	
	
	
}
