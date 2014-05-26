package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.enums.Dominio;
import br.com.abril.nds.enums.Flag;
import br.com.abril.nds.model.cadastro.FlagPendenteAtivacao;
import br.com.abril.nds.model.fiscal.TipoEntidadeDestinoFlag;

public interface FlagPendenteAtivacaoRepository extends Repository<FlagPendenteAtivacao, Long> {

	FlagPendenteAtivacao obterPor(Flag tipoFlag, Long idAlteracao);

	List<FlagPendenteAtivacao> obterPor(TipoEntidadeDestinoFlag tipoEntidadeDestinoFlag);
	
	List<FlagPendenteAtivacao> obterPor(Dominio dominio);
	
	List<FlagPendenteAtivacao> liberarFlag(FlagPendenteAtivacao flagPendenteAtivacao);

}