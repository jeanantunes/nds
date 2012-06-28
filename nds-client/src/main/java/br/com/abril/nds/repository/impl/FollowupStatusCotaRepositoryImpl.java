package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;

import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.FollowupStatusCotaRepository;

public class FollowupStatusCotaRepositoryImpl  extends AbstractRepositoryModel<Cota,Long> implements FollowupStatusCotaRepository {

	public FollowupStatusCotaRepositoryImpl() {
		super(Cota.class);
	}

	@Override
	public List<ConsultaFollowupStatusCotaDTO> obterConsignadosParaChamadao(
			FiltroFollowupStatusCotaDTO filtro) {
		StringBuilder myqrystr = new StringBuilder();
		
		myqrystr.append("select * from cota");
		Query qry2db = this.getSession().createSQLQuery(myqrystr.toString());

		return qry2db.list();
	}

}
