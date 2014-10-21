package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
	 * {@inheritDoc}
	 */
	@Override
	public Desconto buscarDescontoCotaProdutoExcessao(TipoDesconto tipoDesconto, BigDecimal valorDesconto, Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select desconto 					");
		hql.append(" from DescontoCotaProdutoExcessao d ");
		hql.append(" inner join d.desconto desconto		");
		
		hql.append(" where ");
		
		hql.append(" d.cota.numeroCota = :numeroCota and 	");
		hql.append(" d.produtoEdicao is null and 			");
		hql.append(" d.tipoDesconto = :tipoDesconto and 	");
		hql.append(" desconto.valor = :valorDesconto 				");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);
		query.setParameter("tipoDesconto", tipoDesconto);
		query.setParameter("valorDesconto", valorDesconto);
		
		query.setMaxResults(1);
		
		return (Desconto) query.uniqueResult();
	}
	
	/**
	 * Retorna query de Tipos de Desconto Específico da Cota
	 * @return StringBuilder
	 */
	private StringBuilder queryDescontoCota(){
		
        StringBuilder hql = new StringBuilder();
        
        hql.append("	select	");
        
        hql.append("	desconto.id as descontoId,				");
		hql.append("	usuario.nome as nomeUsuario,				");
		hql.append("	desconto.valor as desconto,					");
		hql.append("	desconto.dataAlteracao as dataAlteracao,	");
		hql.append("	c.numeroCota as numeroCota,					");
		
		hql.append("	coalesce(pessoa.razaoSocial, pessoa.nome, '') as nomeCota,	");
		hql.append("	(case when (select count(hdcpe1.desconto.id)				");
		hql.append("	from HistoricoDescontoCotaProdutoExcessao hdcpe1			");
		hql.append("	where hdcpe1.desconto.id = dcpe.desconto.id					");
		hql.append("	group by hdcpe1.desconto.id) > 1							");
		hql.append("	then 'Diversos'	");
		
		hql.append("	when (select count(hdcpe1.desconto.id)				");
		hql.append("	from HistoricoDescontoCotaProdutoExcessao hdcpe1	");
		hql.append("	where hdcpe1.desconto.id = dcpe.desconto.id			");
		hql.append("	group by hdcpe1.desconto.id) = 1 then juridica.razaoSocial ");
		hql.append("	else null end) as fornecedor,	");
	    hql.append("	'Específico' as descTipoDesconto ");
        
	    hql.append("	from DescontoCotaProdutoExcessao dcpe ");
        hql.append("	join dcpe.desconto desconto ");
        hql.append("	join dcpe.usuario usuario ");
        hql.append("	join dcpe.cota c ");
        hql.append("	join c.pessoa as pessoa ");
        hql.append("	join dcpe.fornecedor as fornecedor ");
        hql.append("	join fornecedor.juridica as juridica ");
        
        return hql;
	}
	
	/**
	 * Retorna Ordenação da query de Desconto Específico da Cota
	 * @param filtro
	 * @param hql
	 * @return StringBuilder
	 */
	private StringBuilder ordenacaoDescontoCota(FiltroTipoDescontoCotaDTO filtro,StringBuilder hql){
		
		if (filtro ==null || filtro.getOrdenacaoColuna() == null){
			
			return hql;
		}
		
		OrdenacaoColunaConsulta coluna = filtro.getOrdenacaoColuna();
		
		if (coluna == null) {
			
			return hql;
		}
		
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
			case TIPO_DESCONTO:
				nome = " descTipoDesconto ";
				break;			
			default:
				break;
		}
		
		hql.append( " order by " + nome + filtro.getPaginacao().getSortOrder());
		
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

		hql.append(" where dcpe.produto is null and  dcpe.produtoEdicao is null ");
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		if(filtro != null && filtro.getIdCota() != null) {
			
			hql.append(" and c.id = :idCota ");
			param.put("idCota", filtro.getIdCota());
		
		} else if (filtro != null && filtro.getNumeroCota() != null) {
			
			hql.append(" and c.numeroCota = :numeroCota ");
			param.put("numeroCota", filtro.getNumeroCota());
		}
				
		hql.append(" group by c.id, desconto.id  ");
		
		if(filtro != null  && filtro.getPaginacao()!=null){
			
			if (filtro.getPaginacao().getSortColumn() != null && 
				!filtro.getPaginacao().getSortColumn().trim().isEmpty()) {
				
				hql.append(" ORDER BY ");
				hql.append(filtro.getPaginacao().getSortColumn());		
			
				if ( filtro.getPaginacao().getOrdenacao() != null ) {
					
					hql.append(" ");
					hql.append( filtro.getPaginacao().getOrdenacao().toString());
				}
			}
		}
 
		hql = ordenacaoDescontoCota(filtro,hql);
		
		Query query =  getSession().createQuery(hql.toString());
				
		setParameters(query, param);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				TipoDescontoCotaDTO.class));
		
		if (filtro != null) {
			if (filtro.getPaginacao() != null
					&& filtro.getPaginacao().getPosicaoInicial() != null)
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			if (filtro.getPaginacao() != null
					&& filtro.getPaginacao().getQtdResultadosPorPagina() != null)
				query.setMaxResults(filtro.getPaginacao()
						.getQtdResultadosPorPagina());
		}
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
        
        hql.append("	select	");
        
        hql.append("	desconto.id, c.id	");
        
	    hql.append("	from DescontoCotaProdutoExcessao dcpe ");
        hql.append("	join dcpe.desconto desconto ");
        hql.append("	join dcpe.usuario usuario ");
        hql.append("	join dcpe.cota c ");
        hql.append("	join c.pessoa as pessoa ");
        hql.append("	join dcpe.fornecedor as fornecedor ");
        hql.append("	join fornecedor.juridica as juridica ");
        
        hql.append(" where dcpe.produto is null and  dcpe.produtoEdicao is null ");
        
        HashMap<String, Object> param = new HashMap<String, Object>();
		if(filtro.getNumeroCota() != null) {
			
			hql.append(" and c.numeroCota = :numeroCota ");
			param.put("numeroCota", filtro.getNumeroCota());
		}
		
		hql.append(" group by c.id, desconto.id  ");
		
		Query query =  getSession().createQuery(hql.toString());
		
		setParameters(query, param);
		
		return query.list().size();
	}

	
}
