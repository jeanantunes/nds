package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroContasAPagarDTO implements Serializable {

	private static final long serialVersionUID = 5604665210912108694L;
	
	private boolean primeiraCarga; /** define se a flexigrid esta sendo carregada pela primeira vez */
	
	private Date dataDe;
	private Date dataAte;
	private Integer ce;
	private String produto;
	private Long edicao;
	private String produtoConsignado;
	private Long edicaoConsignado;
	private	List <Long> produtoEdicaoIDs;
	private List<Long> idsFornecedores;
	private PaginacaoVO paginacaoVO;
	private Date dataDetalhe;
	
	@Export(label="Código", exhibitionOrder=1)
	private String codigoProduto;
	
	@Export(label="Produto", exhibitionOrder=2)
	private String nomeProduto;
	
	@Export(label="Edição", exhibitionOrder=3)
	private String edicaoProduto;
	
	@Export(label="Fornecedor", exhibitionOrder=4)
	private String nomeFornecedor;
	
	public boolean isPrimeiraCarga() {
		return primeiraCarga;
	}
	public void setPrimeiraCarga(boolean primeiraCarga) {
		this.primeiraCarga = primeiraCarga;
	}
	public Date getDataDe() {
		return dataDe;
	}
	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}
	public Date getDataAte() {
		return dataAte;
	}
	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}
	public Integer getCe() {
		return ce;
	}
	public void setCe(Integer ce) {
		this.ce = ce;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public List<Long> getProdutoEdicaoIDs() {
		return produtoEdicaoIDs;
	}
	public void setProdutoEdicaoIDs(List<Long> produtoEdicaoIDs) {
		this.produtoEdicaoIDs = produtoEdicaoIDs;
	}
	public List<Long> getIdsFornecedores() {
		return idsFornecedores;
	}
	public void setIdsFornecedores(List<Long> idsFornecedores) {
		this.idsFornecedores = idsFornecedores;
	}
	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}
	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
	public Date getDataDetalhe() {
		return dataDetalhe;
	}
	public void setDataDetalhe(Date dataDetalhe) {
		this.dataDetalhe = dataDetalhe;
	}
	public String getProdutoConsignado() {
		return produtoConsignado;
	}
	public void setProdutoConsignado(String produtoConsignado) {
		this.produtoConsignado = produtoConsignado;
	}
	public Long getEdicaoConsignado() {
		return edicaoConsignado;
	}
	public void setEdicaoConsignado(Long edicaoConsignado) {
		this.edicaoConsignado = edicaoConsignado;
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
    
    public String getEdicaoProduto() {
        return edicaoProduto;
    }
    
    public void setEdicaoProduto(String edicaoProduto) {
        this.edicaoProduto = edicaoProduto;
    }
    
    public String getNomeFornecedor() {
        return nomeFornecedor;
    }
    
    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }
}