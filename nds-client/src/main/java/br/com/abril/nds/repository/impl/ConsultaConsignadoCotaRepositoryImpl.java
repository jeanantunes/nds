package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;

@Repository
public class ConsultaConsignadoCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements
		ConsultaConsignadoCotaRepository {

	public ConsultaConsignadoCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(
			FiltroConsultaConsignadoCotaDTO filtro, String limitar) {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT produto.codigo as codigoProduto, ");
		hql.append("produto.nome as nomeProduto, ");		
		hql.append("pe.numeroEdicao as numeroEdicao, ");
		hql.append("pessoa.razaoSocial as nomeFornecedor, ");
		hql.append("movimento.data as dataLancamento, ");
		hql.append("pe.precoVenda as precoCapa, ");
		hql.append("(pe.precoVenda - pe.desconto) as precoDesconto, ");
		hql.append(" movimento.qtde as reparte, ");		
		hql.append(" (pe.precoVenda * movimento.qtde) as total, ");
		hql.append(" ( (pe.precoVenda - pe.desconto) * movimento.qtde)  as totalDesconto ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosConsignadoCota(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar.equals("limitar")) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return  query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro, String limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoaCota.nome as nomeCota, ");
		hql.append(" movimento.qtde as reparte, ");
		hql.append(" (pe.precoVenda * movimento.qtde) as total, ");
		hql.append(" ( (pe.precoVenda - pe.desconto) * movimento.qtde)  as totalDesconto, ");
		hql.append("pessoa.razaoSocial as nomeFornecedor ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosConsignadoCota(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaPeloFornecedorDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar.equals("limitar")) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return  query.list();
		 
	}
	
	private String getHQLFromEWhereConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	

		hql.append(" from MovimentoEstoqueCota movimento ");
		hql.append(" LEFT JOIN movimento.cota as cota ");
		hql.append(" LEFT JOIN movimento.tipoMovimento as tipoMovimento ");
		hql.append(" LEFT JOIN movimento.produtoEdicao as pe ");
		hql.append(" LEFT JOIN pe.produto as produto ");
		hql.append(" LEFT JOIN cota.parametroCobranca parametroCobranca ");
		hql.append(" LEFT JOIN cota.fornecedores as fornecedor ");
		hql.append(" LEFT JOIN fornecedor.juridica as pessoa ");
		if(filtro.getIdCota() == null){
			hql.append(" LEFT JOIN cota.pessoa as pessoaCota ");
		}
		
		hql.append(" WHERE  tipoMovimento.grupoMovimentoEstoque = :tipoMovimento  " );
		hql.append(" AND  parametroCobranca.tipoCota = :tipoCota  " );

		if(filtro.getIdCota() != null ) { 
			hql.append(" AND cota.id =:numeroCota");
		}
		if(filtro.getIdFornecedor() != null ) { 
			hql.append(" AND fornecedor.id =:idFornecedor");
		}
		

		return hql.toString();
	}
	
	private String getOrderBy(FiltroConsultaConsignadoCotaDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		if(filtro.getIdCota() != null){
			hql.append(" order by cota.numeroCota asc ");
		}
		
		if(filtro.getIdFornecedor() != null){
			hql.append(" order by cota.numeroCota ");
		}
		
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	private HashMap<String,Object> buscarParametrosConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("tipoMovimento", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		param.put("tipoCota", TipoCota.CONSIGNADO);
		
		if(filtro.getIdCota() != null ) { 
			param.put("numeroCota", filtro.getIdCota());
		}
		if(filtro.getIdFornecedor() != null ) { 
			param.put("idFornecedor", filtro.getIdFornecedor());
		}
		
		return param;
	}
	
	@Override
	public Integer buscarTodasMovimentacoesPorCota(
			FiltroConsultaConsignadoCotaDTO filtro, String limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT count(movimento)");
		
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosConsignadoCota(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
		
		
	}

}
