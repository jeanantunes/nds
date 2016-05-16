package br.com.abril.nds.repository;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.abril.nds.model.planejamento.InformacoesReparteComplementarEstudo;

public interface InformacoesReparteEstudoComplementarRepository extends Repository<InformacoesReparteComplementarEstudo, Long> {

	InformacoesReparteComplementarEstudo buscarInformacoesIdEstudo(Long idestudo);
     
}
