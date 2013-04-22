package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.upload.XlsMapper;
import br.com.abril.nds.vo.PaginacaoVO;

@SuppressWarnings("serial")
@Exportable
public class FixacaoReparteDTO implements Serializable {
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	private Long id;
	
	@Export(label ="exemplares")
	@XlsMapper(value="qtd")
	private Integer qtdeExemplares;
	
	private Date dataHora;
	
	@Export(label ="ed. inicial")
	@XlsMapper(value="edinicial")
	private Integer edicaoInicial;
	
	@Export(label ="ed. final")
	@XlsMapper(value="edfinal")
	private Integer edicaoFinal;
	
	@Export(label ="cota")
	@XlsMapper(value="cota")
	private Integer cotaFixada;
	
	@Export(label ="numero")
	private String nomeCota;
	
	@Export(label ="classificacao")
	@XlsMapper(value="classific")
	private String classificacaoProduto;
	
	@Export(label ="usuario")
	private String usuario;
	
	@Export(label ="produto")
	@XlsMapper(value="codpub")
	private String produtoFixado;
	
	@Export(label ="qtde ed")
	@XlsMapper(value="qtdedicoes")
	private Integer qtdeEdicoes;
	
	@Export(label ="data")
	private String data;
	
	@Export(label ="hora")
	private String hora;
	
	private Long qtdPdv;
	
	@Export(label ="codigo")
	private String nomeProduto;
	
	@Export(label ="ed. atendidas")
	private Integer edicoesAtendidas;
	
	private BigInteger edicao;
	private BigDecimal reparte;
	private BigDecimal venda;
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

	public String getDataRecolhimentoString() {
		return dataRecolhimentoString;
	}

	public String getEdicaoString() {
		if (edicao != null) {
			return edicao.toString();
		}
		return "";
	}

	public String getReparteString() {
		if (reparte != null) {
			return reparte.toString();
		}
		return "";
	}

	public String getVendaString() {
		if (venda != null) {
			return venda.toString();
		}
		return "";
	}

	public BigInteger getEdicao() {
		return edicao;
	}

	public void setEdicao(BigInteger edicao) {
		this.edicao = edicao;
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
		if (dataLancamento != null) {
			dataLancamentoString = formatter.format(dataLancamento);
		}
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
		if (dataRecolhimento != null) {
			dataRecolhimentoString = formatter.format(dataRecolhimento);
		}
	}

	public String getStatus() {
		return StringUtils.trimToEmpty(status);
	}

	public void setStatus(String status) {
		this.status = status;			
	}

	public String getClassificacaoProduto() {
		return StringUtils.trimToEmpty(classificacaoProduto);
	}

	public void setClassificacaoProduto(String classificacaoProduto) {
		this.classificacaoProduto = classificacaoProduto;	
	}

	public String getNomeProduto() {
		return StringUtils.trimToEmpty(nomeProduto);
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;			
	}

	public String getUsuario() {
		return StringUtils.trimToEmpty(usuario);
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getQtdeEdicoes() {
		return qtdeEdicoes;
	}

	public void setQtdeEdicoes(Integer qtdeEdicoes) {
		this.qtdeEdicoes = 0;
		if (qtdeEdicoes != null) {
			this.qtdeEdicoes = qtdeEdicoes;
		}
	}

	public Integer getQtdeExemplares() {
		return qtdeExemplares;
	}

	public void setQtdeExemplares(Integer qtdeExemplares) {
		this.qtdeExemplares = 0;
		if (qtdeExemplares != null) {
			this.qtdeExemplares = qtdeExemplares;
		}
	}

	//FIXME
	public String getData() {
		return data;
	}

	//FIXME
	public void setData(String data) {
		if (data == null) {
			this.data = "";
		} else {
			this.data = DateUtil.formatarData(this.getDataHora(),"dd/MM/yyyy");			
		}
	}

	//FIXME
	public String getHora() {
		return hora;
	}

	//FIXME
	public void setHora(String hora) {
		this.hora = DateUtil.formatarHoraMinuto(this.getDataHora());
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
		this.edicoesAtendidas = 0;
		if (edicoesAtendidas != null) {
			this.edicoesAtendidas = edicoesAtendidas;			
		}
	}

	public String getCotaFixadaString() {
		if (cotaFixada != null) {
			return cotaFixada.toString();
		}
		return "";
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
		this.setData(DateUtil.formatarData(dataHora, "dd/MM"));
		this.setHora(DateUtil.formatarHoraMinuto(dataHora));
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
