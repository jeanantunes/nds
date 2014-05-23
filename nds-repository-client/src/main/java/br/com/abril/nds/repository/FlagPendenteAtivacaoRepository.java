package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.enums.TipoFlag;
import br.com.abril.nds.model.cadastro.FlagPendenteAtivacao;
import br.com.abril.nds.model.fiscal.TipoEntidadeDestinoFlag;

public interface FlagPendenteAtivacaoRepository extends Repository<FlagPendenteAtivacao, Long> {

	FlagPendenteAtivacao obterPor(TipoFlag tipoFlag, TipoEntidadeDestinoFlag tipoEntidadeDestinoFlag, Long idAlteracao);

	List<FlagPendenteAtivacao> obterPor(TipoEntidadeDestinoFlag tipoEntidadeDestinoFlag);
	
	List<FlagPendenteAtivacao> liberarFlag(FlagPendenteAtivacao flagPendenteAtivacao);

}