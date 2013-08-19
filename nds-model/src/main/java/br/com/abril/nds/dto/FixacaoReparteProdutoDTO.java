package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;




@SuppressWarnings("serial")
@Exportable
public class FixacaoReparteProdutoDTO implements Serializable{
	
	private Long id;
	@Export(label ="codigo",exhibitionOrder=1)
	private Integer cotaFixada;
	
	@Export(label ="cota",exhibitionOrder=2)
	private String nomeCota;
	
	@Export(label ="ed. inicial",exhibitionOrder=3)
	private Integer edicaoInicial;
	
	@Export(label ="ed. final",exhibitionOrder=4)
	private Integer edicaoFinal;

	@Export(label ="ed. atendidas",exhibitionOrder=5)
	private Integer edicoesAtendidas;

	@Export(label ="qtde ed",exhibitionOrder=6)
	private Integer qtdeEdicoes;
	
	@Export(label ="exemplares",exhibitionOrder=7)
	private Integer qtdeExemplares;
	
	@Export(label ="usuario",exhibitionOrder=8)
	private String usuario;
	
	@Export(label ="data",exhibitionOrder=9)
	private String data;
	
	@Export(label ="hora",exhibitionOrder=10)
	private String hora;
	

	private Date dataHora;
	private String classificacaoProduto;
	private String cotaFixadaString;
	private String produtoFixado;
	private Long qtdPdv;
	private String nomeProduto;
	private BigInteger edicao;
	private String edicaoString;
	private BigDecimal reparte;
	private String reparteString;
	private BigDecimal venda;
	private String vendaString;
	private Date dataLancamento;
	private String dataLancamentoString;
	private Date dataRecolhimento;
	private String dataRecolhimentoString;
	private String status;
	private PaginacaoVO paginacaoVO;
	private boolean qtdeEdicoesMarcado;
	private String codigoProduto;

	public String getDataLancamentoString() {
		return dataLancamentoString;
	}

	public void setDataLancamentoString(String dataLancamentoString) {
		this.dataLancamentoString = dataLancamentoString;
	}

	public String getDataRecolhimentoString() {
		return dataRecolhimentoString;
	}

	public void setDataRecolhimentoString(String dataRecolhimentoString) {
		this.dataRecolhimentoString = dataRecolhimentoString;
	}

	public String getEdicaoString() {
		return this.edicao.toString();
	}

	public void setEdicaoString(String edicaoString) {
		this.edicaoString = edicaoString;
	}

	public String getReparteString() {
		if(this.reparte==null){
			return "";
		}else{
			return this.reparte.toString();
		}
	}

	public void setReparteString(String reparteString) {
		this.reparteString = reparteString;
	}

	public String getVendaString() {
		return this.vendaString;
	}

	public void setVendaString(String vendaString) {
		this.vendaString = vendaString;
	}

	public BigInteger getEdicao() {
		if(this.edicao==null){
			return null;
		}else{
			 this.setEdicaoString(this.edicao.toString());
		}
		return edicao;
	}

	public void setEdicao(BigInteger edicao) {
		this.edicaoString = edicao.toString();
		this.edicao = edicao;
	}

	public BigDecimal getReparte() {
		this.setReparteString(this.reparteString.toString());
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparteString= reparte.toString();
		this.reparte = reparte;
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.vendaString= venda.toString();
		this.venda = venda;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		
		this.dataLancamentoString =DateUtil.formatarData(dataLancamento, "dd/MM/yyyy");
		this.dataLancamento = dataLancamento;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimentoString =DateUtil.formatarData(dataRecolhimento, "dd/MM/yyyy");
		this.dataRecolhimento = dataRecolhimento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if(status==null){
			status="";
		}else{
			this.status = status;			
		}
		
	}

	public String getClassificacaoProduto() {
		return classificacaoProduto;
	}

	public void setClassificacaoProduto(String classificacaoProduto) {
		if(classificacaoProduto==null){
			this.classificacaoProduto="";
		}else{
			this.classificacaoProduto = classificacaoProduto;	
		}
		
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		if(nomeProduto ==null){
			this.nomeProduto="";
		}else{
			this.nomeProduto = nomeProduto;			
		}
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		if(usuario==null){
			this.usuario="";
		}else{
			this.usuario = usuario;
		}
		
	}

	public Integer getQtdeEdicoes() {
		return qtdeEdicoes;
	}

	public void setQtdeEdicoes(Integer qtdeEdicoes) {
		if(qtdeEdicoes==null){
			this.qtdeEdicoes = 0;
		}else{
			this.qtdeEdicoes = qtdeEdicoes;
		}
	}

	public Integer getQtdeExemplares() {
		
		return qtdeExemplares;
	}

	public void setQtdeExemplares(Integer qtdeExemplares) {
		
		if(qtdeExemplares==null){
			this.qtdeExemplares = 0;
		}else{
			this.qtdeExemplares = qtdeExemplares;
		}
		
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data =data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCotaFixada() {
		return cotaFixada;
	}

	public void setCotaFixada(Integer cotaFixada) {
		this.cotaFixada = cotaFixada;
	}

	public String getProdutoFixado() {
		return produtoFixado;
	}

	public void setProdutoFixado(String produtoFixado) {
		this.produtoFixado = produtoFixado;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public Integer getEdicaoInicial() {
		return edicaoInicial;
	}

	public void setEdicaoInicial(Integer edicaoInicial) {
		this.edicaoInicial = edicaoInicial;
	}

	public Integer getEdicaoFinal() {
		return edicaoFinal;
	}

	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}

	public void setEdicaoFinal(Integer edicaoFinal) {
		this.edicaoFinal = edicaoFinal;
	}

	public Integer getEdicoesAtendidas() {
		return edicoesAtendidas;
	}

	public void setEdicoesAtendidas(Integer edicoesAtendidas) {
		if(edicoesAtendidas == null){
			this.edicoesAtendidas = 0;
		}else{
			this.edicoesAtendidas = edicoesAtendidas;			
		}
	}

	public String getCotaFixadaString() {
		return cotaFixadaString;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
		this.setData(DateUtil.formatarData(dataHora, "dd/MM"));
		this.setHora(DateUtil.formatarHoraMinuto(dataHora));
	}

	public void setCotaFixadaString(String cotaFixadaString) {
		this.cotaFixadaString = cotaFixadaString;
	}

	public Long getQtdPdv() {
		return qtdPdv;
	}

	public void setQtdPdv(Long qtdPdv) {
		this.qtdPdv = qtdPdv;
	}

	public boolean isQtdeEdicoesMarcado() {
		return qtdeEdicoesMarcado;
	}

	public void setQtdeEdicoesMarcado(boolean qtdeEdicoesMarcado) {
		this.qtdeEdicoesMarcado = qtdeEdicoesMarcado;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	
}
