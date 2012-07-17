package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.FollowupCadastroRepository;

@Repository
public class FollowupCadastroRepositoryImpl extends AbstractRepositoryModel<Cota,Long> implements FollowupCadastroRepository {

	public FollowupCadastroRepositoryImpl() {
		super(Cota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupCadastroDTO> obterConsignadosParaChamadao(FiltroFollowupCadastroDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append("cotaGarantia.class as tipo, ");
		hql.append("cheque.valor as valor, ");
		hql.append("cheque.validade as dataVencimento, ");
		hql.append("pdv.contato as responsavel ");
		
		hql.append(" from CotaGarantia as cotaGarantia, ");
		hql.append(" Distribuidor as distribuidor ");
		hql.append(" LEFT JOIN cotaGarantia.cheque as cheque ");		
		hql.append(" LEFT JOIN cotaGarantia.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		
		hql.append(" WHERE datediff(cheque.validade, sysdate()) < distribuidor.prazoFollowUp ");
		hql.append(" AND cotaGarantia.cheque <> 0");
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		List<ConsultaFollowupCadastroDTO> listaDeCheque = query.list();
		
		for(ConsultaFollowupCadastroDTO dto: listaDeCheque){
			dto.setTipo("Cheque Caução");
		}
		
		
		hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append("cotaGarantia.class as tipo, ");
		hql.append("np.valor as valor, ");
		hql.append("np.vencimento as dataVencimento, ");
		hql.append("pdv.contato as responsavel ");
		
		hql.append(" from CotaGarantia as cotaGarantia, ");
		hql.append(" Distribuidor as distribuidor ");
		hql.append(" LEFT JOIN cotaGarantia.notaPromissoria as np ");		
		hql.append(" LEFT JOIN cotaGarantia.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		
		hql.append(" WHERE datediff(np.vencimento, sysdate()) < distribuidor.prazoFollowUp ");
		hql.append(" AND cotaGarantia.notaPromissoria <> 0");
		
		
		query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		List<ConsultaFollowupCadastroDTO> listaDeNota = query.list();
		
		for(ConsultaFollowupCadastroDTO dto: listaDeNota){
			dto.setTipo("Nota Promissoria");
		}
		
		listaDeCheque.addAll(listaDeNota);
		 
		return listaDeCheque;
	}

	private Object getSqlFromEWhereCadastro(FiltroFollowupCadastroDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from CotaGarantia as cotaGarantia, ");
		hql.append(" Distribuidor as distribuidor ");
		hql.append(" LEFT JOIN cotaGarantia.cheque as cheque ");
		hql.append(" LEFT JOIN cotaGarantia.notaPromissoria as np ");
		hql.append(" LEFT JOIN cotaGarantia.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE datediff(cheque.validade , sysdate() ) < distribuidor.prazoFollowUp ");
		hql.append(" OR datediff(np.vencimento , sysdate() ) < distribuidor.prazoFollowUp ");
		
		return hql.toString();
	}

	private Object getOrderByCadastro(FiltroFollowupCadastroDTO filtro) {
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by cota.numeroCota ");
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}


}
