package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;

public interface FixacaoReparteRepository  extends Repository<FixacaoReparte, Long> {
	
	public List<FixacaoReparte> obterFixacoesRepartePorProduto(Produto produto);
	
	public List<FixacaoReparte> obterFixacoesRepartePorCota(Cota cota);

}
