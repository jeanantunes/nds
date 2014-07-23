package br.com.abril.nds.model.fechar.dia;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CONSIGNADO")
public class FechamentoDiarioResumoConsignado extends FechamentoDiarioConsignado {

	private static final long serialVersionUID = 1L;

}
