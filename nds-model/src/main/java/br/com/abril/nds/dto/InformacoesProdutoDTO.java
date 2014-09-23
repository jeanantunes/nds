package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoGeracaoEstudo;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class InformacoesProdutoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -1681228943537091591L;
	
    @Export(label = "Numero Ediçao", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private Long numeroEdicao;
	
    @Export(label = "Nome Edição", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
    @Export(label = "Período", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private Integer periodo;
	
    @Export(label = "Preço R$", alignment = Alignment.LEFT, exhibitionOrder = 4)
	private String preco;
	
	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private StatusLancamento status;

	@Export(label = "Reparte", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private BigInteger reparteDistribuido;
	
	@Export(label = "Venda", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private BigInteger venda;

    @Export(label = "Abrangência", alignment = Alignment.LEFT, exhibitionOrder = 8)
	private String percentualAbrangencia;
	
	@Export(label = "Data Lcto", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private String dataLcto;
	
	@Export(label = "Data Rcto", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private String dataRcto;	
	
	@Export(label = "Algoritmo", alignment=Alignment.LEFT, exhibitionOrder = 11)
	private String algoritmo;	
	
	@Export(label = "Rep. Min.", alignment=Alignment.LEFT, exhibitionOrder = 12)
	private BigInteger reparteMinimo;
	
	@Export(label = "Estudo", alignment=Alignment.LEFT, exhibitionOrder = 13)
	private Long estudo;
	
    @Export(label = "Usuário", alignment = Alignment.LEFT, exhibitionOrder = 14)
	private String nomeUsuario;
	
	@Export(label = "Data", alignment=Alignment.LEFT, exhibitionOrder = 15)
	private String dataAlteracao;
	
	@Export(label = "Hora", alignment=Alignment.LEFT, exhibitionOrder = 16)
	private String hora;
	
	private BigInteger reparte;
	private BigInteger encalhe;
	private String datalanc;
	private String dataRecb;
	private String dataInser;
	private Usuario usuario;
	private String codProduto;
	private String tipoClassificacaoProdutoDescricao;
	private Boolean estudoLiberado;
	private BigInteger qtdeReparteEstudo;
	private String codigoICD;
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
	public void setNomeUsuario(String nomeUsuario) {
		if(nomeUsuario == null || nomeUsuario.equals("")){
			this.nomeUsuario = "";
		}else{
			this.nomeUsuario = nomeUsuario;
		}
	}
	public String getHora() {
		return hora;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	public String getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = CurrencyUtil.formatarValor(preco);
	}
	public StatusLancamento getStatus() {
		return status;
	}
	public void setStatus(StatusLancamento status) {
		this.status = status;
	}
	public String getAlgoritmo() {
		return algoritmo;
	}
	public void setAlgoritmo(TipoGeracaoEstudo tipoGeracaoEstudo) {
		if ((tipoGeracaoEstudo == null) || (tipoGeracaoEstudo.equals(""))){
			this.algoritmo = "";
		}else{
			this.algoritmo = tipoGeracaoEstudo.getDescricao();
		}
	}
	public BigInteger getReparteDistribuido() {
		return reparteDistribuido;
	}
	public void setReparteDistribuido(BigInteger reparteDistribuido) {
        if (reparteDistribuido == null) {
			this.reparteDistribuido = BigInteger.ZERO;
		}else{
			this.reparteDistribuido = reparteDistribuido;
		}
	}
	
	public BigInteger getVenda() {
		
		return this.reparte.subtract(this.encalhe);
		//return venda;
	}
	
	public void setVenda(BigInteger venda) {
		if(venda == null) {
			this.venda = BigInteger.ZERO;	
		} else {
			this.venda = venda;
		}
	}
	public String getPercentualAbrangencia() {
		return percentualAbrangencia;
	}
	public void setPercentualAbrangencia(BigDecimal percentualAbrangencia) {
			this.percentualAbrangencia = CurrencyUtil.formatarValor(percentualAbrangencia);
	}
	public String getDataLcto() {
		return dataLcto;
	}
	public void setDataLcto(Date dataLcto) {
		this.dataLcto = DateUtil.formatarDataPTBR(dataLcto);
	}
	public String getDataRcto() {
		return dataRcto;
	}
	public void setDataRcto(Date dataRcto) {
		this.dataRcto = DateUtil.formatarDataPTBR(dataRcto);
	}
	public String getDataInser() {
		return dataInser;
	}
	public void setDataInser(String dataInser) {
		this.dataInser = dataInser;
	}
	public BigInteger getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(BigInteger reparteMinimo) {
        if (reparteMinimo == null) {
			this.reparteMinimo = BigInteger.ZERO;
		}else{
			this.reparteMinimo = reparteMinimo;
		}
	}

	public Long getEstudo() {
		return estudo;
	}
	public void setEstudo(Long estudo) {
		this.estudo = estudo;
	}
	public String getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date data) {
		this.dataAlteracao = DateUtil.formatarDataPTBR(data);
		this.hora = DateUtil.formatarHoraMinuto(data);
	}
	
	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}

	public String getDatalanc() {
		return datalanc;
	}
	public void setDatalanc(Date datalanc) {
		this.datalanc = DateUtil.formatarDataPTBR(datalanc);
	}
	
	public String getDataRecb() {
		return dataRecb;
	}
	public void setDataRecb(String dataRecb) {
		this.dataRecb = dataRecb;
	}
	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}

	public String getTipoClassificacaoProdutoDescricao() {
		return tipoClassificacaoProdutoDescricao;
	}

	public void setTipoClassificacaoProdutoDescricao(
			String tipoClassificacaoProdutoDescricao) {
		this.tipoClassificacaoProdutoDescricao = tipoClassificacaoProdutoDescricao;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Boolean getEstudoLiberado() {
		return estudoLiberado;
	}

	public void setEstudoLiberado(Boolean estudoLiberado) {
		this.estudoLiberado = estudoLiberado;
	}

	public BigInteger getQtdeReparteEstudo() {
		return qtdeReparteEstudo;
	}

	public void setQtdeReparteEstudo(BigInteger qtdeReparteEstudo) {
		this.qtdeReparteEstudo = qtdeReparteEstudo;
	}
	public String getCodigoICD() {
		return codigoICD;
	}
	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;
	}
}
