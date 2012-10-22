package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.AlteracaoCotaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 */
@Repository
public class AlteracaoCotaRepositoryImpl extends AbstractRepositoryModel<Cota, Long> implements AlteracaoCotaRepository {

	/**
	 * Construtor padrão.
	 */
	public AlteracaoCotaRepositoryImpl() {
		super(Cota.class);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO){
		
		StringBuilder hql = new StringBuilder();
		boolean addedAnd = false;
		
		hql.append(" select new ").append(ConsultaAlteracaoCotaDTO.class.getCanonicalName());
		hql.append(" (cota.id, cota.numeroCota, pessoa.nome,  parametroCobranca.fatorVencimento, parametroCobranca.valorMininoCobranca, parametroDistribuicao.descricaoTipoEntrega, box.nome) ");
		hql.append(" from Cota cota ");
		hql.append(" LEFT JOIN cota.parametroDistribuicao parametroDistribuicao ");
		hql.append(" LEFT JOIN cota.pessoa pessoa ");
		hql.append(" LEFT JOIN cota.enderecos enderecoCota 	");
		hql.append(" LEFT JOIN enderecoCota.endereco endereco 	");
        hql.append(" LEFT JOIN cota.box box ");
        hql.append(" LEFT JOIN cota.parametroCobranca parametroCobranca ");

		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota()>0) {
			if(addedAnd)
				hql.append(" and ");
			else
				hql.append(" where ");
			
			hql.append(" cota.numeroCota = :numeroCota ");
			
			addedAnd = true;

		}
		
		if (filtroAlteracaoCotaDTO.getNomeCota() != null && !filtroAlteracaoCotaDTO.getNomeCota().isEmpty()&& !"-1".equals(filtroAlteracaoCotaDTO.getNomeCota())) {
			if(addedAnd)
				hql.append(" and ");
			else
				hql.append(" where ");	
			
			hql.append("  (upper(pessoa.nome) like upper(:nomeCota) OR upper(pessoa.razaoSocial) like  upper(:nomeCota ) )");
			
			addedAnd = true;

		}
		
		if (filtroAlteracaoCotaDTO.getIdBairro() != null && filtroAlteracaoCotaDTO.getIdBairro()>0) {
			if(addedAnd)
				hql.append(" and ");
			else
				hql.append(" where ");	
		
			hql.append(" endereco.codigoBairro = :idBairro ");
			
			addedAnd = true;

		}
		
		if (filtroAlteracaoCotaDTO.getIdMunicipio() != null && !filtroAlteracaoCotaDTO.getIdMunicipio().isEmpty()&& !"-1".equals(filtroAlteracaoCotaDTO.getIdMunicipio())) {
			if(addedAnd)
				hql.append(" and ");
			else
				hql.append(" where ");	
			
			hql.append("  upper(endereco.cidade) like :idMunicipio ");
			
			addedAnd = true;

		}
		
		
		if (filtroAlteracaoCotaDTO.getIdVrMinimo() != null && !filtroAlteracaoCotaDTO.getIdVrMinimo().toString().isEmpty() && filtroAlteracaoCotaDTO.getIdVrMinimo().doubleValue() > 0) {
			if(addedAnd)
				hql.append(" and ");
			else
				hql.append(" where ");		
			
			hql.append(" parametroCobranca.valorMininoCobranca >= :idVrMinimo ");
			
			addedAnd = true;
		}
		
		if (filtroAlteracaoCotaDTO.getDescricaoTipoEntrega() != null ) {
			if(addedAnd)
				hql.append(" and ");
			else
				hql.append(" where ");
			
			hql.append(" parametroDistribuicao.descricaoTipoEntrega = :descricaoTipoEntrega ");
			
			addedAnd = true;

		}
		
		if (filtroAlteracaoCotaDTO.getIdVencimento() != null && filtroAlteracaoCotaDTO.getIdVencimento()>0) {
			if(addedAnd)
				hql.append(" and ");
			else
				hql.append(" where ");
			
			hql.append(" parametroCobranca.fatorVencimento = :fatorVencimento ");
			
			addedAnd = true;

		}
		
		
		
		
		
		if (filtroAlteracaoCotaDTO.getPaginacao() != null && filtroAlteracaoCotaDTO.getPaginacao().getSortOrder() != null) {
			hql.append(" order by ").append(filtroAlteracaoCotaDTO.getPaginacao().getSortOrder()).append(" ").append(filtroAlteracaoCotaDTO.getPaginacao().getOrdenacao().getOrdenacao());
		}
		
		Query query = getSession().createQuery(hql.toString());

		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota()>0) {
			query.setParameter("numeroCota", filtroAlteracaoCotaDTO.getNumeroCota());
		}
		
		if (filtroAlteracaoCotaDTO.getNomeCota() != null && !filtroAlteracaoCotaDTO.getNomeCota().isEmpty() && !"-1".equals(filtroAlteracaoCotaDTO.getNomeCota())) {
			query.setParameter("nomeCota", filtroAlteracaoCotaDTO.getNomeCota().toUpperCase() +"%" );
		}
		
		if (filtroAlteracaoCotaDTO.getIdBairro() != null && filtroAlteracaoCotaDTO.getIdBairro()>0) {
			query.setParameter("idBairro", filtroAlteracaoCotaDTO.getIdBairro());
		}
		
		if (filtroAlteracaoCotaDTO.getIdMunicipio() != null && !filtroAlteracaoCotaDTO.getIdMunicipio().isEmpty() && !"-1".equals(filtroAlteracaoCotaDTO.getIdMunicipio())) {
			query.setParameter("idMunicipio", filtroAlteracaoCotaDTO.getIdMunicipio().toUpperCase() +"%" );
		}
		
		if (filtroAlteracaoCotaDTO.getIdVrMinimo() != null && !filtroAlteracaoCotaDTO.getIdVrMinimo().toString().isEmpty() && filtroAlteracaoCotaDTO.getIdVrMinimo().doubleValue() > 0) {
			query.setParameter("idVrMinimo", filtroAlteracaoCotaDTO.getIdVrMinimo());
		}
		
		if (filtroAlteracaoCotaDTO.getDescricaoTipoEntrega() != null ) {
			query.setParameter("descricaoTipoEntrega",filtroAlteracaoCotaDTO.getDescricaoTipoEntrega());
		}
		
		if (filtroAlteracaoCotaDTO.getIdVencimento() != null && filtroAlteracaoCotaDTO.getIdVencimento()>0) {
			query.setParameter("fatorVencimento",filtroAlteracaoCotaDTO.getIdVencimento());
		}
		
		
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCotaFornecedor(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO){
		
		StringBuilder sql = new StringBuilder();
		
		boolean addedAnd = false;
		
		sql.append(" SELECT ");
		sql.append(" cota.ID AS idCota, ");
		sql.append(" pessoajuridica.RAZAO_SOCIAL AS nomeFornecedor, ");
		sql.append(" desconto.TIPO_DESCONTO AS tipoDesconto  ");
		sql.append(" FROM COTA cota ");
		sql.append(" INNER JOIN COTA_FORNECEDOR cotaFornecedor");
		sql.append(" ON cota.ID = cotaFornecedor.COTA_ID ");
		sql.append(" INNER JOIN FORNECEDOR fornecedor ");
		sql.append(" ON cotaFornecedor.FORNECEDOR_ID = fornecedor.ID  ");
		sql.append(" INNER JOIN PESSOA pessoajuridica ");
		sql.append(" ON fornecedor.JURIDICA_ID = pessoajuridica.ID  ");
		sql.append(" LEFT JOIN DESCONTO_PRODUTO_EDICAO desconto  ");
		sql.append(" ON cota.ID = desconto.COTA_ID ");
		sql.append(" AND desconto.FORNECEDOR_ID = fornecedor.ID ");
		
		
		/*hql.append(" select new ").append(ConsultaAlteracaoCotaDTO.class.getCanonicalName());
		hql.append(" (cota.id, pessoapj.razaoSocial, descontosProdutoEdicao.tipoDesconto) ");
		hql.append(" from Cota cota ");
		hql.append(" JOIN cota.fornecedores fornecedor ");
		hql.append(" JOIN fornecedor.juridica pessoapj ");
		hql.append(" LEFT JOIN cota.descontosProdutoEdicao descontosProdutoEdicao ");*/
		
		
		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota()>0) {
			if(addedAnd)
				sql.append(" AND ");
			else
				sql.append(" WHERE ");
			
			sql.append(" cota.NUMERO_COTA = :numeroCota ");
			
			addedAnd = true;

		}
		
		if (filtroAlteracaoCotaDTO.getIdFornecedor() != null && filtroAlteracaoCotaDTO.getIdFornecedor() > 0) {
			if(addedAnd)
				sql.append(" AND ");
			else
				sql.append(" WHERE ");
			
			sql.append(" fornecedor.ID = :idFornecedor ");
			
			addedAnd = true;

		}
		
		if (filtroAlteracaoCotaDTO.getTipoDesconto() != null ) {
			if(addedAnd)
				sql.append(" AND ");
			else
				sql.append(" WHERE ");
			
			sql.append(" desconto.TIPO_DESCONTO = :tipoDesconto ");
			
			addedAnd = true;

		}
		
		sql.append(" ORDER BY ");
		sql.append(" cota.NUMERO_COTA ");
		
		/*if (filtroAlteracaoCotaDTO.getPaginacao().getSortOrder() != null && filtroAlteracaoCotaDTO.getPaginacao().getSortOrder() != null) {
			sql.append(" order by ").append(filtroAlteracaoCotaDTO.getPaginacao().getSortOrder()).append(" ").append(filtroAlteracaoCotaDTO.getPaginacao().getOrdenacao().getOrdenacao());
		}*/
		
		Query query = getSession().createSQLQuery(sql.toString())
				.addScalar("idCota", StandardBasicTypes.LONG)
				//.addScalar("numeroCota")
				//.addScalar("nomeRazaoSocial")
				.addScalar("nomeFornecedor", StandardBasicTypes.STRING)
				.addScalar("tipoDesconto");
				//.addScalar("vencimento")
				//.addScalar("valorMinimo")
				//.addScalar("tipoEntrega")
				//.addScalar("box");

		
		
		if (filtroAlteracaoCotaDTO.getIdFornecedor() != null && filtroAlteracaoCotaDTO.getIdFornecedor() >0) {
			query.setParameter("idFornecedor", filtroAlteracaoCotaDTO.getIdFornecedor());
		}

		if (filtroAlteracaoCotaDTO.getTipoDesconto() != null) {
			query.setParameter("tipoDesconto", filtroAlteracaoCotaDTO.getTipoDesconto().name());
		}
		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota()>0) {
			query.setParameter("numeroCota", filtroAlteracaoCotaDTO.getNumeroCota());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaAlteracaoCotaDTO.class));
		
		return query.list();
	}

	
}