package br.com.abril.nds.dto.filtro;

import java.util.Date;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroEdicoesFechadasDTO {

	@Export(label="Data De")
	private Date dateDe;

	@Export(label="Data Até")
	private Date dateAte;
	
	@Export(label="Fornecedor")
	private Fornecedor fornecedor;

	public FiltroEdicoesFechadasDTO() {
		System.out.println("teste");
	}
	
	public FiltroEdicoesFechadasDTO(Object o1, Object o2, Object o3) {
		System.out.println(o1);
	}
	
	public FiltroEdicoesFechadasDTO(Date dateDe, Date dateAte, Fornecedor fornecedor) {
		this.dateDe = dateDe;
		this.dateAte = dateAte;
		this.fornecedor = fornecedor;
	}

	public FiltroEdicoesFechadasDTO(Date dateDe, Date dateAte) {
		this.dateDe = dateDe;
		this.dateAte = dateAte;
	}

	public Date getDateDe() {
		return dateDe;
	}

	public void setDateDe(Date dateDe) {
		this.dateDe = dateDe;
	}

	public Date getDateAte() {
		return dateAte;
	}

	public void setDateAte(Date dateAte) {
		this.dateAte = dateAte;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
}
