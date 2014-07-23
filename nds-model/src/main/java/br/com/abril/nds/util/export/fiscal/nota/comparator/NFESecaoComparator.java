package br.com.abril.nds.util.export.fiscal.nota.comparator;

import java.util.Comparator;

import br.com.abril.nds.util.TipoSecao;

public class NFESecaoComparator implements Comparator<TipoSecao> {

	@Override
	public int compare(TipoSecao secao1, TipoSecao secao2) {
		return secao1.getSigla().compareTo(secao2.getSigla());
	}

}
