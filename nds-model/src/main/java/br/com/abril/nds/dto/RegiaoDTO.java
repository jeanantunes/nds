package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class RegiaoDTO implements Serializable {
	private static final long serialVersionUID = 6078766867606414190L;
	
	private Long idRegiao;
	private String nomeRegiao;
	private String dataAlteracao;
	private boolean isFixa;	
	private String nomeUsuario;
	
	private PaginacaoVO paginacao;
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public Long getIdRegiao() {
		return idRegiao;
	}
	public void setIdRegiao(Long idRegiao) {
		this.idRegiao = idRegiao;
	}
	public String getNomeRegiao() {
		return nomeRegiao;
	}
	public void setNomeRegiao(String nomeRegiao) {
		this.nomeRegiao = nomeRegiao;
	}
	public String getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = DateUtil.formatarDataPTBR(dataAlteracao);
	}
	public boolean isFixa() {
		return isFixa;
	}
	public void setFixa(boolean isFixa) {
		this.isFixa = isFixa;
	}
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
}
