package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@SuppressWarnings("serial")
@Exportable
public class MixProdutoDTO implements Serializable{
	
	private Long id;
	
	@Export(label="codigo",exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label="nome",exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label="reparte médio", exhibitionOrder = 3)
	private Long reparteMedio;
	
	@Export(label="venda média",exhibitionOrder = 4)
	private Long vendaMedia;
	
	@Export(label="último reparte",exhibitionOrder = 5)
	private Long ultimoReparte;
	
	@Export(label="reparte mínimo" ,exhibitionOrder = 6)
	private Long reparteMinimo;
	
	@Export(label="reparte máximo" ,exhibitionOrder = 7)
	private Long reparteMaximo;

	@Export(label="usuário",exhibitionOrder = 8)
	private String usuario;
	
	@Export(label="data" ,exhibitionOrder = 10)
	private String data;
	
	@Export(label="hora",exhibitionOrder = 11)
	private String hora;
	
	private Date dataHora;
	
	private PaginacaoVO paginacaoVO;
	

	public String getNomeProduto() {
		return nomeCota;
	}
	public void setNomeProduto(String nomeProduto) {
		if(nomeProduto==null){
			this.nomeCota="";
		}else{
			this.nomeCota = nomeProduto;
		}
	}
	
	public Long getReparteMedio() {
		return reparteMedio;
	}
	public void setReparteMedio(Long reparteMedio) {
		if(reparteMedio==null){
			this.reparteMedio=0L;
		}else{
			this.reparteMedio = reparteMedio;
		}
	}
	public Long getVendaMedia() {
		return vendaMedia;
	}
	public void setVendaMedia(Long vendaMedia) {
		if(vendaMedia==null){
			this.vendaMedia=0L;
		}else{
			this.vendaMedia = vendaMedia;
		}
	}
	public Long getUltimoReparte() {
		return ultimoReparte;
	}
	public void setUltimoReparte(Long ultimoReparte) {
		if(ultimoReparte==null){
			this.ultimoReparte=0L;
		}else{
			this.ultimoReparte = ultimoReparte;
		}
	}
	public Long getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(Long reparteMinimo) {
		if(reparteMinimo==null){
			this.reparteMinimo=0L;
		}else{
			this.reparteMinimo = reparteMinimo;
		}
	}
	public Long getReparteMaximo() {
		return reparteMaximo;
	}
	public void setReparteMaximo(Long reparteMaximo) {
		if(reparteMaximo==null){
			this.reparteMaximo =0L;
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
		if(dataHora !=null){
			this.setData(DateUtil.formatarData(dataHora, "dd/MM/yyyy"));
			this.setHora(DateUtil.formatarHoraMinuto(dataHora));
		}
		this.dataHora = dataHora;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		if(numeroCota==null){
			this.numeroCota =0;
		}else{
			this.numeroCota = numeroCota;
		}
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		if(nomeCota==null){
			this.nomeCota ="";
		}else{
			this.nomeCota = nomeCota;
		}
	}
	
	

}

