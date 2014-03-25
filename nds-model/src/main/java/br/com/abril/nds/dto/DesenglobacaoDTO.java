package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class DesenglobacaoDTO implements Serializable {

    private static final long serialVersionUID = -1222818279134713406L;

    @Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
    private Integer numeroCotaEnglobada;
    private Long idCotaEnglobada;

    @Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
    private String nomeCotaEnglobada;

    @Export(label = "Nome do PDV", alignment=Alignment.LEFT, exhibitionOrder = 3)
    private String nomePDV;

    @Export(label = "% da Cota", alignment=Alignment.LEFT, exhibitionOrder = 4)
    private Float porcentagemCota;

    @Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 10)
    private String nomeUsuario;

    @Export(label = "Data Alteração", alignment=Alignment.LEFT, exhibitionOrder = 11)
    private String dataAlteracao;

    @Export(label = "Hora", alignment=Alignment.LEFT, exhibitionOrder = 12)
    private String hora;

    private Integer numeroCotaDesenglobada;
    private Long idCotaDesenglobada;
    private String nomeCotaDesenglobada;

    private Long idDesenglobacao;

    public Integer getNumeroCotaEnglobada() {
	return numeroCotaEnglobada;
    }
    public void setNumeroCotaEnglobada(Integer numeroCotaEnglobada) {
	this.numeroCotaEnglobada = numeroCotaEnglobada;
    }
    public String getNomeCotaEnglobada() {
	return nomeCotaEnglobada;
    }
    public void setNomeCotaEnglobada(String nomeCotaEnglobada) {
	this.nomeCotaEnglobada = nomeCotaEnglobada;
    }
    public String getNomePDV() {
	return nomePDV;
    }
    public void setNomePDV(String nomePDV) {
	if(nomePDV == null){
	    this.nomePDV = "";
	}else{
	    this.nomePDV = nomePDV;			
	}
    }

    public Float getPorcentagemCota() {
	return porcentagemCota;
    }
    public void setPorcentagemCota(Float porcentagemCota) {
	this.porcentagemCota = porcentagemCota;
    }
    public String getNomeUsuario() {
	return nomeUsuario;
    }
    public void setNomeUsuario(String nomeUsuario) {
	this.nomeUsuario = nomeUsuario;
    }
    public String getDataAlteracao() {
	return dataAlteracao;
    }
    public void setDataAlteracao(Date dataAlteracao) {
	this.dataAlteracao = DateUtil.formatarDataPTBR(dataAlteracao);
	this.setHora(DateUtil.formatarHoraMinuto(dataAlteracao));
    }
    public String getHora() {
	return hora;
    }
    public void setHora(String hora) {
	this.hora = hora;
    }
    public Integer getNumeroCotaDesenglobada() {
	return numeroCotaDesenglobada;
    }
    public void setNumeroCotaDesenglobada(Integer numeroCotaDesenglobada) {
	this.numeroCotaDesenglobada = numeroCotaDesenglobada;
    }
    public Long getIdDesenglobacao() {
	return idDesenglobacao;
    }
    public void setIdDesenglobacao(Long idDesenglobacao) {
	this.idDesenglobacao = idDesenglobacao;
    }
    public Long getIdCotaEnglobada() {
        return idCotaEnglobada;
    }
    public void setIdCotaEnglobada(Long idCotaEnglobada) {
        this.idCotaEnglobada = idCotaEnglobada;
    }
    public Long getIdCotaDesenglobada() {
        return idCotaDesenglobada;
    }
    public void setIdCotaDesenglobada(Long idCotaDesenglobada) {
        this.idCotaDesenglobada = idCotaDesenglobada;
    }
    public String getNomeCotaDesenglobada() {
        return nomeCotaDesenglobada;
    }
    public void setNomeCotaDesenglobada(String nomeCotaDesenglobada) {
        this.nomeCotaDesenglobada = nomeCotaDesenglobada;
    }
}
