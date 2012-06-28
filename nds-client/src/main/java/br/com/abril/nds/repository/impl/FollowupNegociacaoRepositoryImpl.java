package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.FollowupNegociacaoRepository;

@Repository
public class FollowupNegociacaoRepositoryImpl  extends AbstractRepositoryModel<Cota,Long> implements FollowupNegociacaoRepository {

	public FollowupNegociacaoRepositoryImpl() {
		super(Cota.class);
	}

	@Override
	public List<ConsultaFollowupNegociacaoDTO> obterConsignadosParaChamadao(
			FiltroFollowupNegociacaoDTO filtro) {
		StringBuilder myqrystr = new StringBuilder();
		
		myqrystr.append("select * from cota");
		Query qry2db = this.getSession().createSQLQuery(myqrystr.toString());

		return qry2db.list();
	}



}
