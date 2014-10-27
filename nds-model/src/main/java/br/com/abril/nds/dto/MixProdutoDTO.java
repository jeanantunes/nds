package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;


@SuppressWarnings("serial")
@Exportable
public class MixProdutoDTO implements Serializable {
	
	private BigInteger id;
	
	@Export(label="codigo",exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label="nome",exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label="reparte médio", exhibitionOrder = 3)
	private BigDecimal reparteMedio;
	
	@Export(label="venda média",exhibitionOrder = 4)
	private BigDecimal vendaMedia;
	
	@Export(label="ultimo reparte",exhibitionOrder = 5)
	private BigDecimal ultimoReparte;
	
	@Export(label="reparte minimo" ,exhibitionOrder = 6)
	private BigInteger reparteMinimo;
	
	@Export(label="reparte maximo" ,exhibitionOrder = 7)
	private BigInteger reparteMaximo;

	@Export(label="usuário",exhibitionOrder = 8)
	private String usuario;
	
	@Export(label="data" ,exhibitionOrder = 10)
	private String data;
	
	@Export(label="hora",exhibitionOrder = 11)
	private String hora;

	//necessario para modal reparte por pdv
	private Date dataHora;
	private BigInteger qtdPdv;
	private BigInteger idCota;
	private BigInteger idProduto;
	private String codigoProduto;
	private String codigoICD;
	
	private BigInteger somaPdv;
	
	private String classificacaoProduto;
	private BigInteger classificacaoProdutoID;
	private PaginacaoVO paginacaoVO;
	

	public String getNomeProduto() {
		return nomeCota;
	}
	public void setNomeProduto(String nomeProduto) {
		if(nomeProduto==null && id !=null){
			this.nomeCota="";
		}else{
			this.nomeCota = nomeProduto;
		}
	}
	
	public BigDecimal getReparteMedio() {
		return reparteMedio;
	}
	public void setReparteMedio(BigDecimal reparteMedio) {
		if(reparteMedio==null && id !=null){
			this.reparteMedio=BigDecimal.ZERO;
		}else{
			this.reparteMedio = reparteMedio;
		}
	}
	public BigDecimal getVendaMedia() {
		return vendaMedia;
	}
	public void setVendaMedia(BigDecimal vendaMedia) {
		if(vendaMedia==null && id !=null){
			this.vendaMedia=BigDecimal.ZERO;
		}else{
			this.vendaMedia = vendaMedia;
		}
	}
	public BigDecimal getUltimoReparte() {
		return ultimoReparte;
	}
	public void setUltimoReparte(BigDecimal ultimoReparte) {
		if(ultimoReparte==null && id !=null){
			this.ultimoReparte=BigDecimal.ZERO;
		}else{
			this.ultimoReparte = ultimoReparte;
		}
	}
	public BigInteger getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(BigInteger reparteMinimo) {
		if(reparteMinimo==null && id !=null){
			this.reparteMinimo=BigInteger.ZERO;
		}else{
			this.reparteMinimo = reparteMinimo;
		}
	}
	public BigInteger getReparteMaximo() {
		return reparteMaximo;
	}
	public void setReparteMaximo(BigInteger reparteMaximo) {
		if(reparteMaximo==null && id !=null){
			this.reparteMaximo =BigInteger.ZERO;
		}else{
			this.reparteMaximo = reparteMaximo;
		}
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		if(usuario==null && id !=null){
			this.usuario="";
		}else{
			this.usuario = usuario;
		}
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		if(data==null && id !=null){
			this.data="";
		}else{
			this.data = data;	
		}
		
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		if(hora==null && id !=null){
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
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		if(numeroCota==null && id !=null){
			this.numeroCota =0;
		}else{
			this.numeroCota = numeroCota;
		}
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		if(nomeCota==null && id !=null){
			this.nomeCota ="";
		}else{
			this.nomeCota = nomeCota;
		}
	}
	public BigInteger getQtdPdv() {
		return qtdPdv;
	}
	public void setQtdPdv(BigInteger qtdPdv) {
		if(id !=null && nomeCota==null){
			this.qtdPdv=BigInteger.ZERO;
		}else{
			this.qtdPdv = qtdPdv;
		}
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
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getClassificacaoProduto() {
		return classificacaoProduto;
	}
	public void setClassificacaoProduto(String classificacaoProduto) {
		this.classificacaoProduto = classificacaoProduto;
	}
	public String getCodigoICD() {
		return codigoICD;
	}
	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;
	}
	public BigInteger getClassificacaoProdutoID() {
		return classificacaoProdutoID;
	}
	public void setClassificacaoProdutoID(BigInteger classificacaoProdutoID) {
		this.classificacaoProdutoID = classificacaoProdutoID;
	}

	public BigInteger getSomaPdv() {
		return somaPdv;
	}
	
	public void setSomaPdv(Number somaPdv) {
		this.somaPdv = somaPdv != null ? BigInteger.valueOf(somaPdv.intValue()) : null;
	}

}

