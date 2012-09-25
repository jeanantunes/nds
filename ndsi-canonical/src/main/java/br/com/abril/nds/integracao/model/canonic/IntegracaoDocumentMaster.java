package br.com.abril.nds.integracao.model.canonic;

import java.util.List;

public abstract class IntegracaoDocumentMaster<T extends IntegracaoDocumentDetail> extends IntegracaoDocument {

	public abstract void addItem(IntegracaoDocumentDetail docD);

	public abstract List<T> getItems();

	public abstract boolean sameObject(IntegracaoDocumentMaster<?> docM);
}
