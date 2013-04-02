<<<<<<< HEAD
package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;

public interface FixacaoRepartePdvRepository  extends Repository<FixacaoRepartePdv, Long> {
	
	public List<FixacaoRepartePdv> obterFixacaoRepartePdvPorFixacaoReparte(FixacaoReparte fixacaoReparte);
	
	public void removerFixacaoReparte(FixacaoReparte fixacaoReparte);

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
>>>>>>> refs/remotes/DGBTi/fase2
