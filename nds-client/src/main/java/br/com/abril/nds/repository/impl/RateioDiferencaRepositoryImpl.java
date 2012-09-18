package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.repository.RateioDiferencaRepository;
import br.com.abril.nds.util.TipoMensagem;

@Repository
public class RateioDiferencaRepositoryImpl extends AbstractRepositoryModel<RateioDiferenca, Long>
		implements RateioDiferencaRepository {

	public RateioDiferencaRepositoryImpl() {
		super(RateioDiferenca.class);
	}
	
	public RateioDiferenca obterRateioDiferencaPorDiferenca(Long idDiferenca){
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(RateioDiferenca.class.getCanonicalName())
		   .append(" (rateioDiferenca, rateioDiferenca.cota, rateioDiferenca.estudoCota) ")
		   .append(" from RateioDiferenca rateioDiferenca, Diferenca diferenca ")
		   .append(" where rateioDiferenca.diferenca.id = diferenca.id ")
		   .append(" and diferenca.id = :idDiferenca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idDiferenca", idDiferenca);
		query.setMaxResults(1);
		
		return ((RateioDiferenca)query.uniqueResult());
	}
	
	public void removerRateioDiferencaPorDiferenca(Long idDiferenca){
		StringBuilder hql = new StringBuilder();
		hql.append("delete from RateioDiferenca r where r.diferenca.id = :idDiferenca");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idDiferenca", idDiferenca);
		
		query.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<RateioDiferencaCotaDTO> obterRateioDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro) {

		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro inválido.");
		}

		String hql = getHQLDetalhesDiferencaCota();

		if (filtro.getColunaOrdenacao() != null && filtro.getPaginacao() != null) {

			hql += " order by ";
			
			switch(filtro.getColunaOrdenacao()) {
			case BOX:
				hql += " rateioDiferenca.cota.box.codigo ";
				break;
			case COTA:
				hql += " rateioDiferenca.cota.numeroCota ";
				break;
			case DATA:
				hql += " rateioDiferenca.diferenca.lancamentoDiferenca.movimentoEstoque.data ";
				break;
			case EXEMPLARES:
				hql += " rateioDiferenca.qtde ";
				break;
			case NOME:
				hql += " rateioDiferenca.cota.pessoa.nome ";
				break;
			case PRECO_DESCONTO:
				hql += " produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (desconto.desconto / 100)) ";
				break;
			case TOTAL:
				hql += " rateioDiferenca.qtde * (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (desconto.desconto / 100))) ";
				break;
			case TOTAL_APROVADAS:
				hql += " rateioDiferenca.qtde * (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (desconto.desconto / 100))) ";
				break;
			case TOTAL_REJEITADAS:
				hql += " rateioDiferenca.qtde * (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (desconto.desconto / 100))) ";
				break;
			}

			hql += filtro.getPaginacao().getSortOrder();
		}

		Query query = this.getSession().createQuery(hql);

		if (filtro.getIdDiferenca() != null) {

			query.setParameter("idDiferenca", filtro.getIdDiferenca());
		}

		query.setParameter("statusConfirmado", StatusConfirmacao.CONFIRMADO);
		query.setParameter("statusPendente", StatusConfirmacao.PENDENTE);
		
		if (filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null) {

			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());

			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(RateioDiferencaCotaDTO.class));

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DetalheDiferencaCotaDTO obterDetalhesDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro) {

		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro inválido.");
		}

		String hql = getHQLDetalhesDiferencaCota();

		String hqlDesconto = obterHQLDesconto();

		hql = " select "
			+ " count(rateioDiferenca) as quantidadeTotalRegistrosDiferencaCota, "
			+ " sum(rateioDiferenca.qtde) as totalExemplares, "
			+ " sum(rateioDiferenca.qtde * (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (" + hqlDesconto + " / 100)))) as valorTotal "
			+ hql.substring(hql.lastIndexOf(" from "));
		
		Query query = this.getSession().createQuery(hql);

		if (filtro.getIdDiferenca() != null) {

			query.setParameter("idDiferenca", filtro.getIdDiferenca());
		}

		query.setMaxResults(1);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DetalheDiferencaCotaDTO.class));

		return (DetalheDiferencaCotaDTO) query.uniqueResult();
	}

	private String getHQLDetalhesDiferencaCota() {

		StringBuilder hql = new StringBuilder();

		String hqlDesconto = obterHQLDesconto();
		
		hql.append(" select ")
		   .append(" rateioDiferenca.diferenca.lancamentoDiferenca.movimentoEstoque.data as data, ")
		   .append(" rateioDiferenca.cota.numeroCota as numeroCota, ")
		   .append(" rateioDiferenca.cota.pessoa.nome as nomeCota, ")
		   .append(" rateioDiferenca.cota.box.codigo as codigoBox, ")
		   .append(" rateioDiferenca.qtde as exemplares, ")
		   .append(" produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (" + hqlDesconto + " / 100)) as precoDesconto, ")
		   .append(" case when diferenca.statusConfirmacao = :statusConfirmado then ")
		   .append(" (rateioDiferenca.qtde * (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (" + hqlDesconto + " / 100)))) ")
		   .append(" else 0 end as totalAprovadas, ")
		   .append(" case when diferenca.statusConfirmacao = :statusPendente then ")
		   .append(" (rateioDiferenca.qtde * (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (" + hqlDesconto + " / 100)))) ")
		   .append(" else 0 end as totalRejeitadas, ")
		   .append(" rateioDiferenca.qtde * (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * (" + hqlDesconto + "  / 100))) as valorTotal ")
		   .append(" from RateioDiferenca rateioDiferenca ")
		   .append(" inner join rateioDiferenca.diferenca.produtoEdicao as produtoEdicao ")
		   .append(" inner join produtoEdicao.produto.fornecedores as fornecedor ")
		   .append(" inner join rateioDiferenca.diferenca as diferenca ")
		   .append(" where rateioDiferenca.diferenca.id = :idDiferenca ");

		return hql.toString();
	}

	private String obterHQLDesconto() {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" coalesce ( ")
		   .append(" coalesce(( ")
		   .append(" select view.desconto")
		   .append(" from ViewDesconto view ")
		   .append(" where view.cotaId = rateioDiferenca.cota.id ")
		   .append(" and view.produtoEdicaoId = diferenca.produtoEdicao.id ")
		   .append(" and view.fornecedorId = fornecedor.id), diferenca.produtoEdicao.produto.desconto), 0) ");
		
		return hql.toString();
	}
}