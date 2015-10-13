package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.util.DateUtil;

public class TransferenciaArquivoDownloadDTO extends FiltroDTO  implements Serializable{

	private static final long serialVersionUID = -8697800318271655385L;
	
	private String nomeArquivo;
	private String pathArquivo;
	private String pathAbsolutoArquivo;
	private Long tamanhoArquivo;
	private Long ultimaModificacao;
	private String dataUltimaModificacaoFormatada;

	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getPathArquivo() {
		return pathArquivo;
	}
	public void setPathArquivo(String pathArquivo) {
		this.pathArquivo = pathArquivo;
	}
	public Long getTamanhoArquivo() {
		return tamanhoArquivo;
	}
	public void setTamanhoArquivo(Long tamanhoArquivo) {
		this.tamanhoArquivo = tamanhoArquivo;
	}
	public Long getUltimaModificacao() {
		return ultimaModificacao;
	}
	public void setUltimaModificacao(Long ultimaModificacao) {
		this.ultimaModificacao = ultimaModificacao;
		this.setDataUltimaModificacaoFormatada(new Date(ultimaModificacao));
	}
	public String getDataUltimaModificacaoFormatada() {
		return dataUltimaModificacaoFormatada;
	}
	public void setDataUltimaModificacaoFormatada(Date dataUltimaModificacaoFormatada) {
		this.dataUltimaModificacaoFormatada = DateUtil.formatarDataPTBR(dataUltimaModificacaoFormatada)+" - "+DateUtil.formatarHoraMinuto(dataUltimaModificacaoFormatada);
	}
	public String getPathAbsolutoArquivo() {
		return pathAbsolutoArquivo;
	}
	public void setPathAbsolutoArquivo(String pathAbsolutoArquivo) {
		this.pathAbsolutoArquivo = pathAbsolutoArquivo;
	}
}
