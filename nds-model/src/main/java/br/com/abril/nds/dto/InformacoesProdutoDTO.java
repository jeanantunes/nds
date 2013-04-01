package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class InformacoesProdutoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -1681228943537091591L;
	
	@Export(label = "Numero Ediçao", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Long numeroEdicao;
	
	@Export(label = "Nome Edição", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Período", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private PeriodicidadeProduto periodo;
	
	@Export(label = "Preço R$", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private BigDecimal preco;
	
	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private TipoLancamento status;

	@Export(label = "Reparte", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private BigInteger reparteDistribuido;
	
	@Export(label = "Venda", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private BigDecimal venda;

	@Export(label = "Abrangência", alignment=Alignment.LEFT, exhibitionOrder = 8)
	private BigInteger percentualAbrangencia;
	
	@Export(label = "Data Lcto", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private String dataLcto;
	
	@Export(label = "Data Rcto", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private String dataRcto;	
	
	@Export(label = "Algoritmo", alignment=Alignment.LEFT, exhibitionOrder = 11)
	private String algoritmo;	
	
	@Export(label = "Rep. Min.", alignment=Alignment.LEFT, exhibitionOrder = 12)
	private String reparteMinimoGhoma;
	
	@Export(label = "Estudo", alignment=Alignment.LEFT, exhibitionOrder = 13)
	private Long estudo;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 14)
	private String nomeUsuario;
	
	@Export(label = "Data", alignment=Alignment.LEFT, exhibitionOrder = 15)
	private Date dataInsercao;
	
	@Export(label = "Hora", alignment=Alignment.LEFT, exhibitionOrder = 16)
	private String hora;
	
	private String datalanc;
	private String dataRecb;
	private String dataInser;
	private Usuario usuario;
	private String codProduto;
		
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		if(nomeUsuario.equals("") || nomeUsuario == null){
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
	public PeriodicidadeProduto getPeriodo() {
		return periodo;
	}
	public void setPeriodo(PeriodicidadeProduto periodo) {
		this.periodo = periodo;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	public TipoLancamento getStatus() {
		return status;
	}
	public void setStatus(TipoLancamento status) {
		this.status = status;
	}
	public String getAlgoritmo() {
		return algoritmo;
	}
	public void setAlgoritmo(String algoritmo) {
		if ((algoritmo == null) || (algoritmo.equals(""))){
			this.algoritmo = "";
		}else{
			this.algoritmo = algoritmo;
		}
	}
	public BigInteger getReparteDistribuido() {
		return reparteDistribuido;
	}
	public void setReparteDistribuido(BigInteger reparteDistribuido) {
		if ((reparteDistribuido == null) || (reparteDistribuido.equals(""))){
			this.reparteDistribuido = new BigInteger("0");
		}else{
			this.reparteDistribuido = reparteDistribuido;
		}
	}
	
	public BigDecimal getVenda() {
		return venda;
	}
	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}
	public BigInteger getPercentualAbrangencia() {
		return percentualAbrangencia;
	}
	public void setPercentualAbrangencia(BigInteger percentualAbrangencia) {
		if ((percentualAbrangencia == null) || (percentualAbrangencia.equals(""))){
			this.percentualAbrangencia = new BigInteger("0");
		}else{
			this.percentualAbrangencia = percentualAbrangencia;
		}
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
	public String getReparteMinimoGhoma() {
		return reparteMinimoGhoma;
	}
	public void setReparteMinimoGhoma(String reparteMinimoGhoma) {
		if ((reparteMinimoGhoma == null) || (reparteMinimoGhoma.equals(""))){
			this.reparteMinimoGhoma = ("");
		}else{
			this.reparteMinimoGhoma = reparteMinimoGhoma;
		}
	}
	public Long getEstudo() {
		return estudo;
	}
	public void setEstudo(Long estudo) {
		this.estudo = estudo;
	}
	public Date getDataInsercao() {
		return dataInsercao;
	}
	public void setDataInsercao(Date dataInsercao) {
		this.dataInsercao = dataInsercao;
	}
	public String getDatalanc() {
		return datalanc;
	}
	public void setDatalanc(String datalanc) {
		this.datalanc = datalanc;
	}
	public String getDataRecb() {
		return dataRecb;
	}
	public void setDataRecb(String dataRecb) {
		this.dataRecb = dataRecb;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
}
