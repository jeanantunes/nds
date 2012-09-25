package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
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

	@Override
	public List<ConsultaFollowupCadastroDTO> obterConsignadosParaChamadao(FiltroFollowupCadastroDTO filtro) {
		
		List<ConsultaFollowupCadastroDTO> lista =  new ArrayList<ConsultaFollowupCadastroDTO>();
		lista.addAll(getContratos(filtro));
		lista.addAll(getChequeCaucao(filtro));
		lista.addAll(getNotaPromissoria(filtro));
		lista.addAll(getOutros(filtro));
		lista.addAll(getCotaDistribuicao(filtro));
		lista.addAll(getFornecedores(filtro));
		 
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getContratos(
			FiltroFollowupCadastroDTO filtro) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT cota.numeroCota as numeroCota ");
		hql.append(" , pessoa.nome as nomeJornaleiro ");
		hql.append(" , pdv.contato as responsavel");
		hql.append(" , contrato.dataTermino as dataVencimento, ");
		hql.append(" , 'Contrato' as tipo ");
		hql.append(" , 0.0 as valor ");
		hql.append(" from Cota as cota, ");
		hql.append(" Distribuidor as distribuidor ");
		hql.append("  JOIN cota.contratoCota as contrato ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");		
		hql.append(" WHERE datediff(contrato.dataTermino, sysdate()) < distribuidor.prazoFollowUp ");
	   
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getNotaPromissoria(
			FiltroFollowupCadastroDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append(" 'Nota Promissoria' as tipo, ");
		hql.append("np.valor as valor, ");
		hql.append("np.vencimento as dataVencimento, ");
		hql.append("pdv.contato as responsavel ");
		
		hql.append(" from CotaGarantia as cotaGarantia, ");
		hql.append(" Distribuidor as distribuidor ");
		hql.append(" JOIN cotaGarantia.notaPromissoria as np ");		
		hql.append(" LEFT JOIN cotaGarantia.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		
		hql.append(" WHERE datediff(np.vencimento, sysdate()) < distribuidor.prazoFollowUp ");
		
		
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getChequeCaucao(
			FiltroFollowupCadastroDTO filtro) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append(" 'Cheque Caução' as tipo, ");
		hql.append("cheque.valor as valor, ");
		hql.append("cheque.validade as dataVencimento, ");
		hql.append("pdv.contato as responsavel ");
		
		hql.append(" from CotaGarantia as cotaGarantia, ");
		hql.append(" Distribuidor as distribuidor ");
		hql.append("  JOIN cotaGarantia.cheque as cheque ");		
		hql.append(" LEFT JOIN cotaGarantia.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		
		hql.append(" WHERE datediff(cheque.validade, sysdate()) < distribuidor.prazoFollowUp ");
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getOutros(
			FiltroFollowupCadastroDTO filtro) {
		StringBuilder	hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append(" 'Outros' as tipo, ");
		hql.append("outros.valor as valor, ");
		hql.append("outros.validade as dataVencimento, ");
		hql.append("pdv.contato as responsavel ");
		
		hql.append(" from CotaGarantia as cotaGarantia, ");
		hql.append(" Distribuidor as distribuidor ");
		hql.append("  JOIN cotaGarantia.outros as outros ");		
		hql.append(" LEFT JOIN cotaGarantia.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		
		hql.append(" WHERE datediff(outros.validade, sysdate()) < distribuidor.prazoFollowUp ");
		
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getCotaDistribuicao(
			FiltroFollowupCadastroDTO filtro) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT cota.numeroCota as numeroCota ");
		hql.append(" , pessoa.nome as nomeJornaleiro ");
		hql.append(" , pdv.contato as responsavel");
		hql.append(" , 'Cota Distribuicao' as tipo ");
		hql.append(" , 0.0 as valor ");
		hql.append(" from Cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");		
		hql.append(" WHERE  ( cota.parametroDistribuicao.utilizaTermoAdesao = true and cota.parametroDistribuicao.termoAdesaoRecebido= false )    ");
		hql.append("  or  ( cota.parametroDistribuicao.utilizaProcuracao = true and cota.parametroDistribuicao.procuracaoRecebida = false )    ");
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	private List<ConsultaFollowupCadastroDTO> getFornecedores(
			FiltroFollowupCadastroDTO filtro) {
		StringBuilder	hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append(" 'Fornecedores' as tipo, ");
		hql.append(" 0.0 as valor, ");
		hql.append(" fornecedores.validadeContrato as dataVencimento, ");
		hql.append(" pdv.contato as responsavel ");
		
		hql.append(" from Cota as cota, ");
		hql.append(" Distribuidor as distribuidor ");
		hql.append(" JOIN cota.fornecedores as fornecedores ");		
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		
		hql.append(" WHERE    fornecedores.possuiContrato = true   and   datediff( fornecedores.validadeContrato, sysdate()) < distribuidor.prazoFollowUp ");
		
		
		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupCadastroDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	

}
