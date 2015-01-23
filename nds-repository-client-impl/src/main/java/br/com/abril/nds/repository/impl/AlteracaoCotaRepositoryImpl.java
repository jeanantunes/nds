package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.enums.OpcoesFiltro;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AlteracaoCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 */
@Repository
public class AlteracaoCotaRepositoryImpl extends
		AbstractRepositoryModel<Cota, Long> implements AlteracaoCotaRepository {

	/**
	 * Construtor padrão.
	 */
	public AlteracaoCotaRepositoryImpl() {
		super(Cota.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {

		SQLQuery query = templatePesquisaAlteracaoCota(filtroAlteracaoCotaDTO, false);
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeRazaoSocial", StandardBasicTypes.STRING);
		query.addScalar("nomeFornecedor", StandardBasicTypes.STRING);
		query.addScalar("tipoDesconto", StandardBasicTypes.STRING);
		query.addScalar("vencimento", StandardBasicTypes.INTEGER);
		query.addScalar("valorMinimo", StandardBasicTypes.STRING);
		query.addScalar("tipoEntrega", StandardBasicTypes.STRING);
		query.addScalar("box", StandardBasicTypes.STRING);

		PaginacaoVO vo = filtroAlteracaoCotaDTO.getPaginacao();
		
		if (vo != null) {
			query.setMaxResults(vo.getQtdResultadosPorPagina());
			query.setFirstResult(vo.getPosicaoInicial());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaAlteracaoCotaDTO.class));

		return query.list();
	}

	/**
	 * @param filtroAlteracaoCotaDTO
	 * @return
	 * @throws HibernateException
	 */
	private SQLQuery templatePesquisaAlteracaoCota(
			FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, boolean count)
			throws HibernateException {
		StringBuilder sql = new StringBuilder();

		sql.append("select ");
		if (count) {
			sql.append("COUNT(DISTINCT cota.ID) ");
		} else {
			sql.append("cota.ID as idCota, ")
					.append("cota.NUMERO_COTA as numeroCota, ")
					.append("CASE pessoaCota.TIPO WHEN 'J' THEN pessoaCota.RAZAO_SOCIAL ELSE pessoaCota.NOME END as nomeRazaoSocial, ")
					.append("parametroCobranca.FATOR_VENCIMENTO as vencimento, ")
					.append("ROUND(cota.VALOR_MINIMO_COBRANCA, 2) as valorMinimo, ")
					.append("cota.DESCRICAO_TIPO_ENTREGA as tipoEntrega, ")
					.append("box.NOME as box, ")
					.append("GROUP_CONCAT(pessoaFornecedor.RAZAO_SOCIAL SEPARATOR '/ ') AS nomeFornecedor, ")
					.append("GROUP_CONCAT(desconto.TIPO_DESCONTO SEPARATOR '/ ' ) AS tipoDesconto ");
		}
		sql.append("FROM COTA cota ")
				.append("LEFT OUTER JOIN PESSOA pessoaCota on cota.PESSOA_ID=pessoaCota.ID ")
				.append("LEFT OUTER JOIN ENDERECO_COTA enderecoCota on cota.ID=enderecoCota.COTA_ID ")
				.append("LEFT OUTER JOIN ENDERECO endereco on enderecoCota.ENDERECO_ID=endereco.ID ")
				.append("LEFT OUTER JOIN BOX box on cota.BOX_ID=box.ID ")
				.append("LEFT OUTER JOIN PARAMETRO_COBRANCA_COTA parametroCobranca on cota.ID=parametroCobranca.COTA_ID  ");
		
				if (filtroAlteracaoCotaDTO.getUtilizaParametroCobrancaDistribuidor() != null 
						&& OpcoesFiltro.SIM.equals(filtroAlteracaoCotaDTO.getUtilizaParametroCobrancaDistribuidor())) {
					sql.append("LEFT OUTER JOIN FORMA_COBRANCA formaCobranca on formaCobranca.PARAMETRO_COBRANCA_COTA_ID=parametroCobranca.ID AND formaCobranca.ATIVA = false ");
				} else if (filtroAlteracaoCotaDTO.getUtilizaParametroCobrancaDistribuidor() != null 
						&& OpcoesFiltro.NAO.equals(filtroAlteracaoCotaDTO.getUtilizaParametroCobrancaDistribuidor())) {
					sql.append("INNER JOIN FORMA_COBRANCA formaCobranca on formaCobranca.PARAMETRO_COBRANCA_COTA_ID=parametroCobranca.ID AND formaCobranca.ATIVA = true ");
				}
				
				sql.append("LEFT JOIN COTA_FORNECEDOR cotaFornecedor ON cota.ID = cotaFornecedor.COTA_ID ")
				.append("LEFT JOIN FORNECEDOR fornecedor ON cotaFornecedor.FORNECEDOR_ID = fornecedor.ID ")
				.append("LEFT JOIN PESSOA pessoaFornecedor ON fornecedor.JURIDICA_ID = pessoaFornecedor.ID ")
				.append("LEFT JOIN DESCONTO_PRODUTO_EDICAO desconto ON cota.ID = desconto.COTA_ID AND desconto.FORNECEDOR_ID = fornecedor.ID ");

		sql.append(" WHERE  enderecoCota.PRINCIPAL = :enderecoPrincipal ");

		if (filtroAlteracaoCotaDTO.getUtilizaParametroCobrancaDistribuidor() != null 
				&& OpcoesFiltro.SIM.equals(filtroAlteracaoCotaDTO.getUtilizaParametroCobrancaDistribuidor())) {
			sql.append("AND (SELECT count(0) FROM FORMA_COBRANCA formaCobranca WHERE formaCobranca.PARAMETRO_COBRANCA_COTA_ID=parametroCobranca.ID AND formaCobranca.ATIVA = true) = 0 ");
		}
				
		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota() > 0) {
			sql.append("AND cota.NUMERO_COTA = :numeroCota ");
		}

		if (filtroAlteracaoCotaDTO.getNomeCota() != null
				&& !filtroAlteracaoCotaDTO.getNomeCota().isEmpty()
				&& !"-1".equals(filtroAlteracaoCotaDTO.getNomeCota())) {
			sql.append("AND  (UPPER(pessoaCota.NOME) LIKE UPPER(:nomeCota) OR UPPER(pessoaCota.RAZAO_SOCIAL) LIKE  UPPER(:nomeCota ) )");
		}

		if (filtroAlteracaoCotaDTO.getIdBairro() != null
				&& !filtroAlteracaoCotaDTO.getIdBairro().isEmpty()
				&& !"-1".equals(filtroAlteracaoCotaDTO.getIdBairro())) {
			sql.append(" AND endereco.BAIRRO = :idBairro ");
		}

		if (filtroAlteracaoCotaDTO.getIdMunicipio() != null
				&& !filtroAlteracaoCotaDTO.getIdMunicipio().isEmpty()
				&& !"-1".equals(filtroAlteracaoCotaDTO.getIdMunicipio())) {
			sql.append(" AND  UPPER(endereco.CIDADE) LIKE :idMunicipio ");
		}

		if (filtroAlteracaoCotaDTO.getIdVrMinimo() != null
				&& !filtroAlteracaoCotaDTO.getIdVrMinimo().toString().isEmpty()
				&& filtroAlteracaoCotaDTO.getIdVrMinimo().doubleValue() > 0) {
			sql.append("AND cota.VALOR_MINIMO_COBRANCA = :idVrMinimo ");
		}

		if (filtroAlteracaoCotaDTO.getDescricaoTipoEntrega() != null) {
			sql.append("AND cota.DESCRICAO_TIPO_ENTREGA LIKE :descricaoTipoEntrega ");
		}

		if (filtroAlteracaoCotaDTO.getIdVencimento() != null && filtroAlteracaoCotaDTO.getIdVencimento() > 0) {
			sql.append("AND parametroCobranca.FATOR_VENCIMENTO = :fatorVencimento ");
		}

		if (filtroAlteracaoCotaDTO.getIdFornecedor() != null && filtroAlteracaoCotaDTO.getIdFornecedor() > 0) {
			sql.append("AND fornecedor.ID = :idFornecedor ");
		}

		if (filtroAlteracaoCotaDTO.getTipoDesconto() != null) {
			sql.append("AND desconto.TIPO_DESCONTO =:tipoDesconto ");
		}
		
		if(!count) {
			sql.append("GROUP BY cota.ID");
	
			if (filtroAlteracaoCotaDTO.getPaginacao() != null && filtroAlteracaoCotaDTO.getPaginacao().getSortOrder() != null) {
				sql.append(" ORDER BY ")
						.append(filtroAlteracaoCotaDTO.getPaginacao().getSortOrder())
						.append(" ")
						.append(filtroAlteracaoCotaDTO.getPaginacao().getOrdenacao().getOrdenacao());
			}
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("enderecoPrincipal", true);

		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota() > 0) {
			query.setParameter("numeroCota", filtroAlteracaoCotaDTO.getNumeroCota());
		}

		if (filtroAlteracaoCotaDTO.getNomeCota() != null
				&& !filtroAlteracaoCotaDTO.getNomeCota().isEmpty()
				&& !"-1".equals(filtroAlteracaoCotaDTO.getNomeCota())) {
			query.setParameter("nomeCota", filtroAlteracaoCotaDTO.getNomeCota().toUpperCase() + "%");
		}

		if (filtroAlteracaoCotaDTO.getIdBairro() != null
				&& !filtroAlteracaoCotaDTO.getIdBairro().isEmpty()
				&& !"-1".equals(filtroAlteracaoCotaDTO.getIdBairro())) {
			query.setParameter("idBairro", filtroAlteracaoCotaDTO.getIdBairro());
		}

		if (filtroAlteracaoCotaDTO.getIdMunicipio() != null
				&& !filtroAlteracaoCotaDTO.getIdMunicipio().isEmpty()
				&& !"-1".equals(filtroAlteracaoCotaDTO.getIdMunicipio())) {
			query.setParameter("idMunicipio", filtroAlteracaoCotaDTO.getIdMunicipio().toUpperCase() + "%");
		}

		if (filtroAlteracaoCotaDTO.getIdVrMinimo() != null
				&& !filtroAlteracaoCotaDTO.getIdVrMinimo().toString().isEmpty()
				&& filtroAlteracaoCotaDTO.getIdVrMinimo().doubleValue() > 0) {
			query.setParameter("idVrMinimo", filtroAlteracaoCotaDTO.getIdVrMinimo());
		}

		if (filtroAlteracaoCotaDTO.getDescricaoTipoEntrega() != null) {
			query.setParameter("descricaoTipoEntrega", filtroAlteracaoCotaDTO.getDescricaoTipoEntrega().name());
		}

		if (filtroAlteracaoCotaDTO.getIdVencimento() != null
				&& filtroAlteracaoCotaDTO.getIdVencimento() > 0) {
			query.setParameter("fatorVencimento", filtroAlteracaoCotaDTO.getIdVencimento());
		}

		if (filtroAlteracaoCotaDTO.getIdFornecedor() != null
				&& filtroAlteracaoCotaDTO.getIdFornecedor() > 0) {
			query.setParameter("idFornecedor", filtroAlteracaoCotaDTO.getIdFornecedor());
		}

		if (filtroAlteracaoCotaDTO.getTipoDesconto() != null) {
			query.setParameter("tipoDesconto", filtroAlteracaoCotaDTO.getTipoDesconto().name());
		}
		
		return query;
	}

	@Override
	public int contarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {

		Query query =  templatePesquisaAlteracaoCota(filtroAlteracaoCotaDTO, true);

		return ((Number) query.uniqueResult()).intValue();
	}

}