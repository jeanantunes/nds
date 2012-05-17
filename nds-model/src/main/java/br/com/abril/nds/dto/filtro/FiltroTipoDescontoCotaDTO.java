package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroTipoDescontoCotaDTO implements Serializable {
	
	private static final long serialVersionUID = 2281254940257591061L;
	
	@Export(label = "Desconto")
	private Long desconto;
	
	@Export(label = "Data Alteração")
	private Date dataAlteracao;
	
	@Export(label = "Usuário")
	private String usuario;
	
	private PaginacaoVO paginacao;
	
	public FiltroTipoDescontoCotaDTO() {
		
	}
	
	public FiltroTipoDescontoCotaDTO(Long desconto, Date dataAlteracao, String usuario) {
		super();
		this.desconto = desconto;
		this.dataAlteracao = dataAlteracao;
		this.usuario = usuario;		
	}

	public Long getDesconto() {
		return desconto;
	}

	public void setDesconto(Long desconto) {
		this.desconto = desconto;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	
}
