package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "GRUPO_TRIBUTO_ALIQUOTA")
@AssociationOverrides({
        @AssociationOverride(name = "pk.tributo", 
            joinColumns = @JoinColumn(name ="TRIBUTO_ID")),
        @AssociationOverride(name = "pk.aliquota", 
            joinColumns = @JoinColumn(name="ALIQUOTA_ID"))})
public class GrupoTributoAliquota implements Serializable {

	private static final long serialVersionUID = 5392503333835977858L;
	
	@EmbeddedId
	private GrupoTributoAliquotaID pk = new GrupoTributoAliquotaID();
	
    public GrupoTributoAliquotaID getPk() {
        return pk;
    }

	public void setPk(GrupoTributoAliquotaID pk) {
		this.pk = pk;
	}
	
}
