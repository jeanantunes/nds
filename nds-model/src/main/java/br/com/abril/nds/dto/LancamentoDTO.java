package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class LancamentoDTO implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;

	private Long id;
	
	private Long idProdutoEdicao;
	
	private Long idProduto;
	
	private Date dataPrevista;
	
	private Date dataDistribuidor;
	
	private BigInteger reparte;
	
	private StatusLancamento statusLancamento;
	
	private TipoLancamento tipoLancamento;
	
	private Integer numeroPeriodo;
	
	private FormaComercializacao formaComercializacaoProduto;
	
	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

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
	
	public Long getIdProduto() {
		return idProduto;
	}
	
	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
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
	
	/**
	 * @return the reparte
	 */
	public BigInteger getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	
	public StatusLancamento getStatusLancamento() {
		return statusLancamento;
	}
	
	public void setStatusLancamento(StatusLancamento statusLancamento) {
		this.statusLancamento = statusLancamento;
	}

    
    public Integer getNumeroPeriodo() {
        return numeroPeriodo;
    }

    
    public void setNumeroPeriodo(Integer numeroPeriodo) {
        this.numeroPeriodo = numeroPeriodo;
    }

	public FormaComercializacao getFormaComercializacaoProduto() {
		return formaComercializacaoProduto;
	}

	public void setFormaComercializacaoProduto(
			FormaComercializacao formaComercializacaoProduto) {
		this.formaComercializacaoProduto = formaComercializacaoProduto;
	}
    
    
	
}
