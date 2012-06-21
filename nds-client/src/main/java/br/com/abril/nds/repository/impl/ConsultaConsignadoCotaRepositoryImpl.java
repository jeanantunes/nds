package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
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
		hql.append("pe.precoVenda as precoCapa ");
		
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
		
		hql.append(" WHERE  tipoMovimento.grupoMovimentoEstoque = :tipoMovimento  " );
		hql.append(" AND  parametroCobranca.tipoCota = :tipoCota  " );

		if(filtro.getIdCota() != null ) { 
			hql.append(" AND cota.id =:numeroCota");
		}

		return hql.toString();
	}
	
	private String getOrderBy(FiltroConsultaConsignadoCotaDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		if(filtro.getIdCota() != null){
			hql.append(" order by cota.id asc ");
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
		
		return param;
	}

}
