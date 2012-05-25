package br.com.abril.nds.client.util.comparators;

import java.util.Comparator;

import br.com.abril.nds.client.vo.RegistroCurvaABC;

public class CurvaABCParticipacaoComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {

		RegistroCurvaABC registro1 = (RegistroCurvaABC) o1;
		RegistroCurvaABC registro2 = (RegistroCurvaABC) o2;

		return registro1.getParticipacao().compareTo(
				registro2.getParticipacao());
	}

}
