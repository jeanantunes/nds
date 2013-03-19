package br.com.abril.nds.dto.filtro;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@SuppressWarnings("serial")
@Exportable
public class FiltroConsultaFixacaoProdutoDTO extends FiltroDTO{
    
	private String codigoProduto;
	private String nomeProduto;
	private String classificacaoProduto;
	private String codigoCota;
	private Long idFixacao;
	private PaginacaoVO paginacaoVO;
	
	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}
	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getClassificacaoProduto() {
		return classificacaoProduto;
	}
	public void setClassificacaoProduto(String classificacaoProduto) {
		this.classificacaoProduto = classificacaoProduto;
	}
	public String getCodigoCota() {
		return codigoCota;
	}
	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}
	public Long getIdFixacao() {
		return idFixacao;
	}
	public void setIdFixacao(Long idFixacao) {
		this.idFixacao = idFixacao;
	}
	

	
}
