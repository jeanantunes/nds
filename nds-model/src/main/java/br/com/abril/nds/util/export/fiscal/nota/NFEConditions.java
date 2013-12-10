package br.com.abril.nds.util.export.fiscal.nota;

import java.util.List;

import br.com.abril.nds.util.export.fiscal.nota.condition.COFINSNTCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.COFINSOutrasAliqCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.COFINSOutrasCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.COFINSOutrasQtdeCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.COFINSSTCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.COFINSTAliqCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.COFINSTQtdeCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS00Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS10Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS20Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS30Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS40Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS51Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS60Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS70Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMS90Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMSST10Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMSST30Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMSST60Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMSST70Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ICMSST90Condition;
import br.com.abril.nds.util.export.fiscal.nota.condition.IPINTCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.IPITAliqCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.IPITCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.IPITUnidCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.ISSQNCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.IdentificacaoDestinatarioCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.IdentificacaoEmitenteCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.PISNTCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.PISOutrasAliqCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.PISOutrasCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.PISOutrasQtdeCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.PISSTCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.PISTAliqCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.PISTQtdeCondition;

public enum NFEConditions {	
	
	IDENTIFICACAO_EMITENTE(new IdentificacaoEmitenteCondition()),
	IDENTIFICACAO_DESTINATARIO(new IdentificacaoDestinatarioCondition()),
	ICMS_00(new ICMS00Condition()),
	ICMS_10(new ICMS10Condition()),
	ICMS_20(new ICMS20Condition()),
	ICMS_30(new ICMS30Condition()),
	ICMS_40(new ICMS40Condition()),
	ICMS_51(new ICMS51Condition()),
	ICMS_60(new ICMS60Condition()),
	ICMS_70(new ICMS70Condition()),
	ICMS_90(new ICMS90Condition()),
	ICMS_ST_10(new ICMSST10Condition()),
	ICMS_ST_30(new ICMSST30Condition()),
	ICMS_ST_60(new ICMSST60Condition()),
	ICMS_ST_70(new ICMSST70Condition()),
	ICMS_ST_90(new ICMSST90Condition()),
	IPI_TRIB(new IPITCondition()),
	IPI_NAO_TRIB(new IPINTCondition()),
	IPI_TRIB_ALIQ(new IPITAliqCondition()),
	IPI_TRIB_UNID(new IPITUnidCondition()),
	ISSQN(new ISSQNCondition()),
	COFINS_TRIB_ALIQ(new COFINSTAliqCondition()),
	COFINS_TRIB_QTDE(new COFINSTQtdeCondition()),
	COFINS_NAO_TRIB(new COFINSNTCondition()),
	COFINS_OUTROS(new COFINSOutrasCondition()),
	COFINS_OUTROS_QTDE(new COFINSOutrasQtdeCondition()),
	COFINS_OUTROS_ALIQ(new COFINSOutrasAliqCondition()),
	COFINS_SUBSTITUICAO_TRIB(new COFINSSTCondition()),
	PIS_TRIB_ALIQ(new PISTAliqCondition()),
	PIS_TRIB_QTDE(new PISTQtdeCondition()),
	PIS_NAO_TRIB(new PISNTCondition()),
	PIS_OUTROS(new PISOutrasCondition()),
	PIS_OUTROS_QTDE(new PISOutrasQtdeCondition()),
	PIS_OUTROS_ALIQ(new PISOutrasAliqCondition()),
	PIS_SUBSTITUICAO_TRIB(new PISSTCondition());
	
	private Condition condition;

	private NFEConditions(Condition condition) {
		this.condition = condition;
	}

	/**
	 * @param object
	 * @return
	 * @see br.com.abril.nds.util.export.fiscal.nota.condition.Condition#valid(java.lang.Object)
	 */
	public boolean valid(Object object) {
		return condition.valid(object);
	}
	
	public boolean validParent(Object object) {
		return condition.validParent(object);
	}
	
	public boolean validParents(List<Object> object) {
		return condition.validParents(object);
	}
	
}