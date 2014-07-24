package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.upload.XlsMapper;
import br.com.abril.nds.vo.PaginacaoVO;

@SuppressWarnings("serial")
@Exportable
public class MixCotaDTO implements Serializable{
	
	private BigInteger id;
	
	@Export(label="codigo",exhibitionOrder = 1)
	@XlsMapper(value="codigoProduto")
	private String codigoProduto;
	
	@Export(label="nome",exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label="classificacao",exhibitionOrder = 3)
	@XlsMapper(value="classificacao")
	private String classificacaoProduto;
	
	private BigInteger tipoClassificacaoProdutoID;
	
	@Export(label="Reparte Médio", exhibitionOrder = 4)
	private BigDecimal reparteMedio;
	
	@Export(label="Venda Média",exhibitionOrder = 5)
	private BigDecimal vendaMedia;
	
	@Export(label="Último Reparte",exhibitionOrder = 6)
	private BigDecimal ultimoReparte;
	
	@Export(label="Reparte Mínimo" ,exhibitionOrder = 7)
	@XlsMapper(value="reparteMinimo")
	private BigInteger reparteMinimo;
	
	@Export(label="Reparte Máximo" ,exhibitionOrder = 8)
	@XlsMapper(value="reparteMaximo")
	private BigInteger reparteMaximo;
	
	@Export(label="Usuário",exhibitionOrder = 9)
	private String usuario;
	
	@Export(label="Data" ,exhibitionOrder = 10)
	private String data;
	
	
	@Export(label="hora",exhibitionOrder = 11)
	private String hora;
	
//	necessario para modal reparte por pdv
	private BigInteger qtdPdv;
	private Date dataHora;
	private BigInteger idCota;
	private BigInteger idProduto;
	
//	necessario para modal adicionar mix 
	@XlsMapper(value="numeroCota")
	private Integer numeroCota;
	
	private PaginacaoVO paginacaoVO;
	
	private String codigoICD;
	private String error;
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		if(codigoProduto==null){
			this.codigoProduto="";
		}else{
			this.codigoProduto = codigoProduto;
		}
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		if(nomeProduto==null){
			this.nomeProduto="";
		}else{
			this.nomeProduto = nomeProduto;
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
	public BigDecimal getReparteMedio() {
			return reparteMedio;
	}
	public void setReparteMedio(BigDecimal reparteMedio) {
		if(reparteMedio==null){
			this.reparteMedio=new BigDecimal(0);
		}else{
			this.reparteMedio=reparteMedio;
		}
	}
	public BigDecimal getVendaMedia() {
		return vendaMedia;
	}
	public void setVendaMedia(BigDecimal vendaMedia) {
		if(vendaMedia==null){
			this.vendaMedia=new BigDecimal(0);
		}else{
			this.vendaMedia = vendaMedia;
		}
	}
	public BigDecimal getUltimoReparte() {
		return ultimoReparte;
	}
	public void setUltimoReparte(BigDecimal ultimoReparte) {
		if(ultimoReparte==null){
			this.ultimoReparte=BigDecimal.ZERO;
		}else{
			this.ultimoReparte = ultimoReparte;
		}
	}
	public BigInteger getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(BigInteger reparteMinimo) {
		if(reparteMinimo==null){
			this.reparteMinimo=BigInteger.ZERO;
		}else{
			this.reparteMinimo = reparteMinimo;
		}
	}
	public BigInteger getReparteMaximo() {
		return reparteMaximo;
	}
	public void setReparteMaximo(BigInteger reparteMaximo) {
		if(reparteMaximo==null){
		}else{
			this.reparteMaximo = reparteMaximo;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		if(data==null){
			this.data="";
		}else{
			this.data = data;	
		}
		
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		if(hora==null){
			this.hora="";
		}else{
			this.hora = hora;
		}
	}
	public Date getDataHora() {
		return dataHora;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public BigInteger getQtdPdv() {
		return qtdPdv;
	}
	public void setQtdPdv(BigInteger qtdPdv) {
		this.qtdPdv = qtdPdv;
	}
	public BigInteger getIdCota() {
		return idCota;
	}
	public void setIdCota(BigInteger idCota) {
		this.idCota = idCota;
	}
	public BigInteger getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(BigInteger idProduto) {
		this.idProduto = idProduto;
	}
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getCodigoICD() {
		return codigoICD;
	}
	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;
	}
	public BigInteger getTipoClassificacaoProdutoID() {
		return tipoClassificacaoProdutoID;
	}
	public void setTipoClassificacaoProdutoID(BigInteger tipoClassificacaoProdutoID) {
		this.tipoClassificacaoProdutoID = tipoClassificacaoProdutoID;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	
	
}

