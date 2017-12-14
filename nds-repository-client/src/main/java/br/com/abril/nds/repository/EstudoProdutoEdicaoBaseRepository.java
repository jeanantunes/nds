package br.com.abril.nds.repository;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;

public interface EstudoProdutoEdicaoBaseRepository {

	List<EdicaoBaseEstudoDTO> obterEdicoesBase(Long estudoId);

	void copiarEdicoesBase(Long idOrigem, Long estudoDividido);

	EdicaoBaseEstudoDTO obterEdicoesBaseEstudoOrigemCopiaEstudo(Long estudoId);

}
