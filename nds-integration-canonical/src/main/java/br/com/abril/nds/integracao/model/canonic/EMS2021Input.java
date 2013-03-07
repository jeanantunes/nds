package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.List;

public class EMS2021Input extends IntegracaoDocumentMaster<EMS2021InputItem> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3176527752303474423L;

	@Override
	public void addItem(IntegracaoDocumentDetail docD) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<EMS2021InputItem> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sameObject(IntegracaoDocumentMaster<?> docM) {
		// TODO Auto-generated method stub
		return false;
	}

}
