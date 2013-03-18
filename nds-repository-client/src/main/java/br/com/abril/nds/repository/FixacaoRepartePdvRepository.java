<<<<<<< HEAD
package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;

public interface FixacaoRepartePdvRepository  extends Repository<FixacaoRepartePdv, Long> {
	
	public List<FixacaoRepartePdv> obterFixacaoRepartePdvPorFixacaoReparte(FixacaoReparte fixacaoReparte);

}
=======
package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;

public interface FixacaoRepartePdvRepository  extends Repository<FixacaoRepartePdv, Long> {
	
	public List<FixacaoRepartePdv> obterFixacaoRepartePdvPorFixacaoReparte(FixacaoReparte fixacaoReparte);
	
	public void removerFixacaoReparte(FixacaoReparte fixacaoReparte);

}
>>>>>>> 03f1ca6c8da04a45696f13aca9cd81446f5232f7
