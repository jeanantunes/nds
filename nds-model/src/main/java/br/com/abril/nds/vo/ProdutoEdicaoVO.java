package br.com.abril.nds.vo;

import java.io.Serializable;

public class ProdutoEdicaoVO implements Serializable {
    
	private static final long serialVersionUID = -7568941971653076485L;

    private Long id;
    
    private String nome, codigoBarras, precoCapaFormatado;
    
    private Long edicao;
    
    private Integer qtdeEmxs;
    
    public ProdutoEdicaoVO(Long id, String nome, String codigoBarra, Long edicao, String precoCapa){

    	this.id = id;
        this.nome = nome;
        this.codigoBarras = codigoBarra;
        this.edicao = edicao;
        this.precoCapaFormatado = precoCapa;
        
    }
    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public String getNome() {
        return nome;
    }

    
    public void setNome(String nome) {
        this.nome = nome;
    }

    
    public String getCodigoBarras() {
        return codigoBarras;
    }

    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    
    public Long getEdicao() {
        return edicao;
    }
    
    public void setEdicao(Long edicao) {
        this.edicao = edicao;
    }
    
    public String getPrecoCapaFormatado() {
        return precoCapaFormatado;
    }
    
    public void setPrecoCapaFormatado(String precoCapaFormatado) {
        this.precoCapaFormatado = precoCapaFormatado;
    }
    
	public Integer getQtdeEmxs() {
		return qtdeEmxs;
	}

	public void setQtdeEmxs(Integer qtdeEmxs) {
		this.qtdeEmxs = qtdeEmxs;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProdutoEdicaoVO other = (ProdutoEdicaoVO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
