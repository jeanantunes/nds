package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class MovimentoEstoqueCotaDTO implements Serializable {

	private static final long serialVersionUID = -1230165588204635338L;

	private Long idCota;
	
	private Long idProdEd;
	
	private String codigoProd;
	
	private Long edicaoProd;
	
	private String nomeProd;
	
	private Integer qtdeReparte;
	
	private List<RateioDTO> rateios;
	
	private Long idFornecedor;
	
	
	/***/
	private boolean aprovadoAutomaticamente;
	
	private Long usuarioAprovadorId;
	
	private Date dataAprovacao;
	
	private String motivo;
	
	private String status;
	
	private Date data;
	
	private Date dataCriacao;
	
	private Date dataIntegracao;
	
	private String statusIntegracao;
	
	private Long tipoMovimentoId;
	
	private Long usuarioId;
	
	private BigInteger qtde;
	
	private Date dataLancamentoOriginal;
	
	private Long estoqueProdutoEdicaoCotaId;
	
	private Long estudoCotaId;
	
	private Long notaEnvioItemNotaEnvioId;
	
	private Long notaEnvioItemSequencia;
	
	private Long lancamentoId;
	
	private Long movimentoEstoqueCotaFuroId;
	
	private Long movimentoFinanceiroCotaId;
	
	private String statusEstoqueFinanceiro;
	
	private BigDecimal precoComDesconto;
	
	private BigDecimal precoVenda;
	
	private BigDecimal valorDesconto;
	
	private String formaComercializacao;
	
	private boolean cotaContribuinteExigeNF;
	
	public MovimentoEstoqueCotaDTO() {
	}
	
	public MovimentoEstoqueCotaDTO(Long idCota, Long idProdEd,
			String codigoProd, Long edicaoProd, String nomeProd,
			Integer qtdeReparte, List<RateioDTO> rateios) {
		super();
		this.idCota = idCota;
		this.idProdEd = idProdEd;
		this.codigoProd = codigoProd;
		this.edicaoProd = edicaoProd;
		this.nomeProd = nomeProd;
		this.qtdeReparte = qtdeReparte;
		this.rateios = rateios;
	}

	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	public Long getIdProdEd() {
		return idProdEd;
	}
	public void setIdProdEd(Long idProdEd) {
		this.idProdEd = idProdEd;
	}
	public String getCodigoProd() {
		return codigoProd;
	}
	public void setCodigoProd(String codigoProd) {
		this.codigoProd = codigoProd;
	}
	public Long getEdicaoProd() {
		return edicaoProd;
	}
	public void setEdicaoProd(Long edicaoProd) {
		this.edicaoProd = edicaoProd;
	}
	public String getNomeProd() {
		return nomeProd;
	}
	public void setNomeProd(String nomeProd) {
		this.nomeProd = nomeProd;
	}
	public Integer getQtdeReparte() {
		return qtdeReparte;
	}
	public void setQtdeReparte(BigInteger qtdeReparte) {
		this.qtdeReparte = qtdeReparte.intValue();
	}
	public List<RateioDTO> getRateios() {
		return rateios;
	}
	public void setRateios(List<RateioDTO> rateios) {
		this.rateios = rateios;
	}

	/***/
	public boolean isAprovadoAutomaticamente() {
		return aprovadoAutomaticamente;
	}

	public void setAprovadoAutomaticamente(boolean aprovadoAutomaticamente) {
		this.aprovadoAutomaticamente = aprovadoAutomaticamente;
	}

	public Long getUsuarioAprovadorId() {
		return usuarioAprovadorId;
	}

	public void setUsuarioAprovadorId(Long usuarioAprovadorId) {
		this.usuarioAprovadorId = usuarioAprovadorId;
	}

	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataIntegracao() {
		return dataIntegracao;
	}

	public void setDataIntegracao(Date dataIntegracao) {
		this.dataIntegracao = dataIntegracao;
	}

	public String getStatusIntegracao() {
		return statusIntegracao;
	}

	public void setStatusIntegracao(String statusIntegracao) {
		this.statusIntegracao = statusIntegracao;
	}

	public Long getTipoMovimentoId() {
		return tipoMovimentoId;
	}

	public void setTipoMovimentoId(Long tipoMovimentoId) {
		this.tipoMovimentoId = tipoMovimentoId;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public Date getDataLancamentoOriginal() {
		return dataLancamentoOriginal;
	}

	public void setDataLancamentoOriginal(Date dataLancamentoOriginal) {
		this.dataLancamentoOriginal = dataLancamentoOriginal;
	}

	public Long getEstoqueProdutoEdicaoCotaId() {
		return estoqueProdutoEdicaoCotaId;
	}

	public void setEstoqueProdutoEdicaoCotaId(Long estoqueProdutoEdicaoCotaId) {
		this.estoqueProdutoEdicaoCotaId = estoqueProdutoEdicaoCotaId;
	}

	public Long getEstudoCotaId() {
		return estudoCotaId;
	}

	public void setEstudoCotaId(Long estudoCotaId) {
		this.estudoCotaId = estudoCotaId;
	}

	public Long getNotaEnvioItemNotaEnvioId() {
		return notaEnvioItemNotaEnvioId;
	}

	public void setNotaEnvioItemNotaEnvioId(Long notaEnvioItemNotaEnvioId) {
		this.notaEnvioItemNotaEnvioId = notaEnvioItemNotaEnvioId;
	}

	public Long getNotaEnvioItemSequencia() {
		return notaEnvioItemSequencia;
	}

	public void setNotaEnvioItemSequencia(Long notaEnvioItemSequencia) {
		this.notaEnvioItemSequencia = notaEnvioItemSequencia;
	}

	public Long getLancamentoId() {
		return lancamentoId;
	}

	public void setLancamentoId(Long lancamentoId) {
		this.lancamentoId = lancamentoId;
	}

	public Long getMovimentoEstoqueCotaFuroId() {
		return movimentoEstoqueCotaFuroId;
	}

	public void setMovimentoEstoqueCotaFuroId(Long movimentoEstoqueCotaFuroId) {
		this.movimentoEstoqueCotaFuroId = movimentoEstoqueCotaFuroId;
	}

	public Long getMovimentoFinanceiroCotaId() {
		return movimentoFinanceiroCotaId;
	}

	public void setMovimentoFinanceiroCotaId(Long movimentoFinanceiroCotaId) {
		this.movimentoFinanceiroCotaId = movimentoFinanceiroCotaId;
	}

	public String getStatusEstoqueFinanceiro() {
		return statusEstoqueFinanceiro;
	}

	public void setStatusEstoqueFinanceiro(String statusEstoqueFinanceiro) {
		this.statusEstoqueFinanceiro = statusEstoqueFinanceiro;
	}

	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public String getFormaComercializacao() {
		return formaComercializacao;
	}

	public void setFormaComercializacao(String formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
	}

	public boolean isCotaContribuinteExigeNF() {
		return cotaContribuinteExigeNF;
	}

	public void setCotaContribuinteExigeNF(boolean cotaContribuinteExigeNF) {
		this.cotaContribuinteExigeNF = cotaContribuinteExigeNF;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	
}