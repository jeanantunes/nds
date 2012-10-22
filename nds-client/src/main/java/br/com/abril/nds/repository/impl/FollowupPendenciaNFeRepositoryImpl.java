package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.repository.FollowupPendenciaNFeRepository;

@Repository
public class FollowupPendenciaNFeRepositoryImpl extends AbstractRepositoryModel<NotaFiscalEntrada,Long> implements FollowupPendenciaNFeRepository {

	public FollowupPendenciaNFeRepositoryImpl() {
		super(NotaFiscalEntrada.class);		 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupPendenciaNFeDTO> obterConsignadosParaChamadao(
			FiltroFollowupPendenciaNFeDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append("conf.data as dataEntrada, ");		
		hql.append("notaCota.statusNotaFiscal as tipoPendencia, ");		
		hql.append("((conf.qtdeInformada * conf.precoCapaInformado) -  (conf.qtde * conf.precoCapaInformado)) as valorDiferenca, ");
		hql.append(" concat(telefone.ddd, ' ',telefone.numero)  as numeroTelefone ");		
		
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		
		hql.append(getOrderByNotasPendentes(filtro));

		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupPendenciaNFeDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());			
		 
		return query.list();
	}
	
	private String getSqlFromEWhereNotaPendente(FiltroFollowupPendenciaNFeDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from ControleConferenciaEncalheCota as controleCota ");
		hql.append(" LEFT JOIN controleCota.notaFiscalEntradaCota as notaCota ");
		hql.append(" LEFT JOIN notaCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN notaCota.tipoNotaFiscal as tipo ");
		hql.append(" LEFT JOIN controleCota.conferenciasEncalhe as conf");
		hql.append(" LEFT JOIN pessoa.telefones as telefone");
		
		
		hql.append(" where tipo.tipoOperacao = 'ENTRADA' ");
		
		hql.append(" and ((conf.qtdeInformada * conf.precoCapaInformado) -  (conf.qtde * conf.precoCapaInformado)) != 0 ");
		
		return hql.toString();
	}
	
	private String getOrderByNotasPendentes(FiltroFollowupPendenciaNFeDTO filtro){
		
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
