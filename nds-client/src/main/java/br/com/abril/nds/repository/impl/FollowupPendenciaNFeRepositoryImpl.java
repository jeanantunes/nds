package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;

import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.FollowupPendenciaNFeRepository;

public class FollowupPendenciaNFeRepositoryImpl extends AbstractRepositoryModel<Cota,Long> implements FollowupPendenciaNFeRepository {

	public FollowupPendenciaNFeRepositoryImpl() {
		super(Cota.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ConsultaFollowupPendenciaNFeDTO> obterConsignadosParaChamadao(
			FiltroFollowupPendenciaNFeDTO filtro) {
		StringBuilder myqrystr = new StringBuilder();
		
		myqrystr.append("select * from cota");
		Query qry2db = this.getSession().createSQLQuery(myqrystr.toString());

		return qry2db.list();
	}

}
