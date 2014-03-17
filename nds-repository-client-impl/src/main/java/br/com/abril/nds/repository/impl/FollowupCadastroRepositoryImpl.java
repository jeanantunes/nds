package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupCadastroDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FollowupCadastroRepository;

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
		hql.append("       'Contrato' as tipo ");
		hql.append(" from Cota as cota, ");
		hql.append("      Distribuidor as distribuidor ");
		hql.append(" JOIN cota.contratoCota as contrato ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");		

		hql.append(" WHERE (((datediff(contrato.dataTermino, sysdate()))-distribuidor.prazoFollowUp)>0) ");

	    hql.append(" order by cota.numeroCota");
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao() != null) {
		
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null){
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null){
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getCotaDistribuicao(FiltroFollowupCadastroDTO filtro) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT cota.numeroCota as numeroCota ");
		hql.append(" , coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeJornaleiro ");
		hql.append(" , coalesce(pdv.contato, '') as responsavel");
		hql.append(" , 'Cota Distribuicao' as tipo ");
		hql.append(" from Cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");		
		hql.append(" WHERE  ( cota.parametroDistribuicao.utilizaTermoAdesao = true and cota.parametroDistribuicao.termoAdesaoRecebido= false )    ");
		hql.append("  or  ( cota.parametroDistribuicao.utilizaProcuracao = true and cota.parametroDistribuicao.procuracaoRecebida = false )    ");
		hql.append(" order by cota.numeroCota");

		Query query =  getSession().createQuery(hql.toString());		

		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao() != null) {
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getFornecedores(FiltroFollowupCadastroDTO filtro) {
		StringBuilder	hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '')  as nomeJornaleiro, ");
		hql.append(" 'Fornecedores' as tipo, ");
		hql.append(" fornecedores.validadeContrato as dataVencimento, ");
		hql.append(" pdv.contato as responsavel ");
		
		hql.append(" from Cota as cota, ");
		hql.append(" Distribuidor as distribuidor ");
		
		hql.append(" JOIN cota.fornecedores as fornecedores ");		
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		
		hql.append(" WHERE    fornecedores.possuiContrato = true ");
		hql.append(" and (((datediff(fornecedores.validadeContrato, sysdate()))-distribuidor.prazoFollowUp)>0) ");
		
		hql.append(" order by cota.numeroCota");
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao() != null) {
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
	}
}
