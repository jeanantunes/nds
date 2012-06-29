package br.com.abril.nds.util.export.fiscal.nota;

import java.util.List;

import br.com.abril.nds.util.export.fiscal.nota.condition.CNPJCondition;
import br.com.abril.nds.util.export.fiscal.nota.condition.CPFCondition;
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

public enum NFEConditions {	
	
	CPF(new CPFCondition()), 
	CNPJ(new CNPJCondition()),
	IDENTIFICACAO_EMITENTE(new IdentificacaoEmitenteCondition()),
	IDENTIFICACAO_DESTINATARIO(new IdentificacaoDestinatarioCondition()),
	ICMS00(new ICMS00Condition()),
	ICMS10(new ICMS10Condition()),
	ICMS20(new ICMS20Condition()),
	ICMS30(new ICMS30Condition()),
	ICMS40(new ICMS40Condition()),
	ICMS51(new ICMS51Condition()),
	ICMS60(new ICMS60Condition()),
	ICMS70(new ICMS70Condition()),
	ICMS90(new ICMS90Condition()),
	ICMSST10(new ICMSST10Condition()),
	ICMSST30(new ICMSST30Condition()),
	ICMSST60(new ICMSST60Condition()),
	ICMSST70(new ICMSST70Condition()),
	ICMSST90(new ICMSST90Condition()),
	IPIT(new IPITCondition()),
	IPINT(new IPINTCondition()),
	IPIT_ALIQ(new IPITAliqCondition()),
	IPIT_UNID(new IPITUnidCondition()),
	ISSQN(new ISSQNCondition());
	
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