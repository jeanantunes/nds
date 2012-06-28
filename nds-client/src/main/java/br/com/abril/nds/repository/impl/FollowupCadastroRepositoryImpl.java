package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.FollowupCadastroRepository;

public class FollowupCadastroRepositoryImpl extends AbstractRepositoryModel<Cota,Long> implements FollowupCadastroRepository {

	public FollowupCadastroRepositoryImpl() {
		super(Cota.class);
	}

	@Override
	public List<ConsultaFollowupCadastroDTO> obterConsignadosParaChamadao(
			FiltroFollowupCadastroDTO filtro) {
		StringBuilder myqrystr = new StringBuilder();
		
		myqrystr.append("select * from cota");
		Query qry2db = this.getSession().createSQLQuery(myqrystr.toString());

		return qry2db.list();
	}


}
