package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.repository.DescontoCotaRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto da cota
 * 
 * @author Discover Technology
 */
@Repository
public class DescontoCotaRepositoryImpl extends AbstractRepositoryModel<DescontoCota, Long> implements DescontoCotaRepository {

	public DescontoCotaRepositoryImpl() {
		super(DescontoCota.class);
	}
	
	/**
	 * Retorna query de Tipos de Desconto Específico da Cota
	 * @return StringBuilder
	 */
	private StringBuilder queryDescontoCota(){
		
        StringBuilder hql = new StringBuilder();
		
		hql.append(" select dc.id as idTipoDesconto, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" case when pessoa.nomeFantasia = null then pessoa.nome else pessoa.nomeFantasia end as nomeCota, ");
		hql.append(" dc.desconto as desconto, ");	
		
		hql.append(" (case ");
		hql.append("     when (select count(fornecedor.id) from DescontoCota descontoCota JOIN descontoCota.fornecedores fornecedor  ");
		hql.append("     where descontoCota.id = dc.id ) > 1 ");
		hql.append("     then 'Diversos' ");
		hql.append("     when (select count(fornecedor.id) from DescontoCota descontoCota JOIN descontoCota.fornecedores fornecedor  ");
		hql.append("     where descontoCota.id = dc.id ) = 1 then fornecedor.juridica.razaoSocial ");
	    hql.append("     else '' end) as fornecedor, ");
	    
		hql.append(" dc.dataAlteracao as dataAlteracao, ");	
		hql.append(" usuario.nome as nomeUsuario ");
		
		hql.append(" from DescontoCota dc "); 
		hql.append(" join dc.cota cota ");	
		hql.append(" join cota.pessoa pessoa ");
		hql.append(" join dc.usuario usuario ");
		hql.append(" left join dc.fornecedores fornecedor ");
        
        return hql;
	}
	
	/**
	 * Retorna Ordenação da query de Desconto Específico da Cota
	 * @param filtro
	 * @param hql
	 * @return StringBuilder
	 */
	private StringBuilder ordenacaoDescontoCota(FiltroTipoDescontoCotaDTO filtro,StringBuilder hql){
		
		OrdenacaoColunaConsulta coluna = filtro.getOrdenacaoColuna();
		
		String nome = null;
		
		switch(coluna) {
			case NUMERO_COTA:
				nome = " numeroCota ";
				break;
			case NOME_COTA: 
				nome = " nomeCota ";
				break;
			case DESCONTO:
				nome = " desconto ";
				break;
			case FORNECEDORES:
				nome = " fornecedor ";
				break;
			case DATA_ALTERACAO:
				nome = " dataAlteracao ";
				break;
			case USUARIO:
				nome = " nomeUsuario ";
				break;
			default:
				break;
		}
		
		hql.append( " order by " + nome);
		
		return hql;
	}
	
	/**
	 * Obtem lista de dados de Tipo de Desconto Específico da Cota
	 * @param filtro
	 * @return List<TipoDescontoCotaDTO>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoCotaDTO> obterDescontoCota(
			FiltroTipoDescontoCotaDTO filtro) {

		StringBuilder hql = queryDescontoCota();

		HashMap<String, Object> param = new HashMap<String, Object>();
		if(filtro.getNumeroCota() != null) {
			
			hql.append(param.isEmpty()?" where ":" and ");
			hql.append(" cota.numeroCota = :numeroCota ");
			param.put("numeroCota", filtro.getNumeroCota());
		}
		
		if(filtro.getNomeCota() != null && !"".equals(filtro.getNomeCota())) {
			
			hql.append(param.isEmpty()?" where ":" and ");
			hql.append(" upper(pessoa.nomeFantasia) like :nomeCota ");
			param.put("nomeCota", "%" + filtro.getNomeCota().toUpperCase() + "%");
		}
		
		hql.append(" group by dc.id ");
 
		hql = ordenacaoDescontoCota(filtro,hql);
		
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				TipoDescontoCotaDTO.class));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());

		return query.list();
	}

	/**
	 * Obtem quantidade de dados de Tipo de Desconto Específico da Cota
	 * @param filtro
	 * @return int
	 */
	@Override
	public int obterQuantidadeDescontoCota(
			FiltroTipoDescontoCotaDTO filtro) {
 
        StringBuilder hql = new StringBuilder();
		
		hql.append(" select dc.id ");
		hql.append(" from DescontoCota dc "); 
		hql.append(" join dc.cota cota ");	
		hql.append(" join cota.pessoa pessoa ");
		hql.append(" join dc.usuario usuario ");
		hql.append(" left join dc.fornecedores fornecedor ");

        HashMap<String, Object> param = new HashMap<String, Object>();
		if(filtro.getNumeroCota() != null) {
			
			hql.append(param.isEmpty()?" where ":" and ");
			hql.append(" cota.numeroCota = :numeroCota ");
			param.put("numeroCota", filtro.getNumeroCota());
		}
		
		if(filtro.getNomeCota() != null && !"".equals(filtro.getNomeCota())) {
			
			hql.append(param.isEmpty()?" where ":" and ");
			hql.append(" upper(pessoa.nomeFantasia) like :nomeCota ");
			param.put("nomeCota", "%" + filtro.getNomeCota().toUpperCase() + "%");
		}
		
		hql.append(" group by dc.id ");
		
		Query query =  getSession().createQuery(hql.toString());
		
		return query.list().size();
	}

	
}
