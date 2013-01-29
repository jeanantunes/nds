package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

public class LancamentoDTO implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;

	private Long id;
	private Long idProdutoEdicao;
	private Date dataPrevista;
	private Date dataDistribuidor;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	public Date getDataPrevista() {
		return dataPrevista;
	}
	public void setDataPrevista(Date dataPrevista) {
		this.dataPrevista = dataPrevista;
	}
	public Date getDataDistribuidor() {
		return dataDistribuidor;
	}
	public void setDataDistribuidor(Date dataDistribuidor) {
		this.dataDistribuidor = dataDistribuidor;
	}

	
}
