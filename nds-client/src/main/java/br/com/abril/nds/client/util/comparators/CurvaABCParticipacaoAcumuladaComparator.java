package br.com.abril.nds.client.util.comparators;

import java.util.Comparator;

import br.com.abril.nds.client.vo.RegistroCurvaABC;

/**
 * @author infoA2
 * Classe utilizada para comparar a participação total nos relatórios de Curva ABC
 */
public class CurvaABCParticipacaoAcumuladaComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object o1, Object o2) {
		
		RegistroCurvaABC registro1 = (RegistroCurvaABC) o1;
		RegistroCurvaABC registro2 = (RegistroCurvaABC) o2;
		
		return registro1.getParticipacaoAcumulada().compareTo(registro2.getParticipacaoAcumulada());
	}

}
