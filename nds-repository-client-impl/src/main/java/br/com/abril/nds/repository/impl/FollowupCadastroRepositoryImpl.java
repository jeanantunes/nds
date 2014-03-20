package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FollowupCadastroRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FollowupCadastroRepositoryImpl extends AbstractRepositoryModel<Cota,Long> implements FollowupCadastroRepository {

	public FollowupCadastroRepositoryImpl() {
		super(Cota.class);
	}

	@Override
	public List<ConsultaFollowupCadastroDTO> obterConsignadosParaChamadao(FiltroFollowupCadastroDTO filtro) {
		
		List<ConsultaFollowupCadastroDTO> lista =  new ArrayList<ConsultaFollowupCadastroDTO>();

		lista.addAll(getContratos(filtro));
		lista.addAll(getFornecedores(filtro));
		lista.addAll(getCotaDistribuicao(filtro)); 
		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getContratos(FiltroFollowupCadastroDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("       coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeJornaleiro, ");
		hql.append("       coalesce(pdv.contato, '') as responsavel, ");
		hql.append("       contrato.dataTermino as dataVencimento, ");
		hql.append("       coalesce(case contrato.recebido  when 1 then 'a vencer/vencido' end, 'Contrato não recebido') as tipo ");
		hql.append(" from Cota as cota ");
		hql.append(" JOIN cota.contratoCota as contrato ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");		

		hql.append(" WHERE (((datediff(contrato.dataTermino, sysdate()))-(select d.prazoFollowUp from Distribuidor d))<0) ");

	    hql.append(" group by cota.id order by cota.numeroCota");
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupCadastroDTO.class));
		
		this.configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getCotaDistribuicao(FiltroFollowupCadastroDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota ");
		hql.append(" , coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeJornaleiro ");
		hql.append(" , coalesce(pdv.contato, '') as responsavel");
		hql.append(" , coalesce(case cota.parametroDistribuicao.utilizaTermoAdesao  when 1 then 'Termo Adesão não Recebido' end, 'Procuração não recebida') as tipo ");
		hql.append(" from Cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");		
		hql.append(" WHERE  ( cota.parametroDistribuicao.utilizaTermoAdesao = true and cota.parametroDistribuicao.termoAdesaoRecebido= false )    ");
		hql.append("  or  ( cota.parametroDistribuicao.utilizaProcuracao = true and cota.parametroDistribuicao.procuracaoRecebida = false )    ");
		hql.append(" group by cota.id "); 
		hql.append(" order by cota.numeroCota ");

		Query query =  getSession().createQuery(hql.toString());		

		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupCadastroDTO.class));
		
		this.configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getFornecedores(FiltroFollowupCadastroDTO filtro) {
		
		StringBuilder	sql = new StringBuilder();
		
		sql.append(" SELECT ");
		
		sql.append(" pes.RAZAO_SOCIAL as nomeJornaleiro, ");
		sql.append(" 'Contrato fornecedor a vencer/Vencido' as tipo, ");
		sql.append(" forn.VALIDADE_CONTRATO as dataVencimento, ");
		sql.append(" forn.RESPONSAVEL as responsavel ");
		
		sql.append(" from fornecedor forn ");
		
		sql.append(" join pessoa pes  ");		
		sql.append(" 	ON forn.JURIDICA_ID = pes.ID ");
		
		sql.append(" WHERE forn.POSSUI_CONTRATO = true ");
		sql.append(" and (((datediff(forn.VALIDADE_CONTRATO, sysdate()))-(select d.PRAZO_FOLLOW_UP from distribuidor d))<0) ");
		
		Query query =  getSession().createSQLQuery(sql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupCadastroDTO.class));
		
		this.configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {

		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}
}
