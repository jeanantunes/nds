package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheDiferencaCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaCotaDTO;
import br.com.abril.nds.dto.RateioDiferencaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheDiferencaCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RateioDiferencaRepository;

@Repository
public class RateioDiferencaRepositoryImpl extends AbstractRepositoryModel<RateioDiferenca, Long>
		implements RateioDiferencaRepository {

	public RateioDiferencaRepositoryImpl() {
		super(RateioDiferenca.class);
	}
		
	public void removerRateioDiferencaPorDiferenca(Long idDiferenca){
		StringBuilder hql = new StringBuilder();
		hql.append("delete from RateioDiferenca r where r.diferenca.id = :idDiferenca");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idDiferenca", idDiferenca);
		
		query.executeUpdate();
	}
	
	public void removerRateiosNaoAssociadosDiferenca( Long idDiferenca, List<Long> idRateios){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("delete from RateioDiferenca r where r.id not in (:idRateios) and r.diferenca.id = :idDiferenca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idRateios", idRateios);
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
		
		String hql = getHQLDetalhesDiferencaCota(filtro);

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
				hql += " mec.data ";
				break;
			case EXEMPLARES:
				hql += " rateioDiferenca.qtde ";
				break;
			case NOME:
				hql += " rateioDiferenca.cota.pessoa.nome ";
				break;
			case PRECO_DESCONTO:
				hql += " mec.valoresAplicados.precoComDesconto ";
				break;
			case TOTAL:
			case TOTAL_APROVADAS:
			case TOTAL_REJEITADAS:
				hql += " rateioDiferenca.qtde * (mec.valoresAplicados.precoComDesconto) ";
				break;
			}

			hql += filtro.getPaginacao().getSortOrder();
		}

		Query query = this.getSession().createQuery(hql);

		if (filtro.getIdDiferenca() != null) {

			query.setParameter("idDiferenca", filtro.getIdDiferenca());
		}
		
        if (filtro.getNumeroCota()!=null){
		    
			query.setParameter("numeroCota", filtro.getNumeroCota());
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

		String hql = "";
		
		hql = " select "
			+ " count(distinct rateioDiferenca) as quantidadeTotalRegistrosDiferencaCota, ";
				
		if (filtro.getNumeroCota()!=null){
			
			hql += " (select sum(rd.qtde) from RateioDiferenca rd where rd.cota.numeroCota = :numeroCota and rd.diferenca.id = :idDiferenca) as totalExemplares, "
				+  " ( (select sum(rd2.qtde) from RateioDiferenca rd2 where rd2.cota.numeroCota = :numeroCota and rd2.diferenca.id = :idDiferenca) * "
				+  "   (select sum(coalesce(va.precoComDesconto,movEst.produtoEdicao.precoVenda)) "
			    +  "    from RateioDiferenca rd3 join rd3.diferenca as dif join dif.lancamentoDiferenca as ldif join ldif.movimentosEstoqueCota as movEst join movEst.valoresAplicados va where rd3.cota.numeroCota = :numeroCota and movEst.cota.numeroCota = :numeroCota and dif.id = :idDiferenca) ) as valorTotal ";
		}
		else{
			
			hql += " sum(rateioDiferenca.qtde) as totalExemplares, "
				+  " sum(rateioDiferenca.qtde * coalesce(mec.valoresAplicados.precoComDesconto,mec.produtoEdicao.precoVenda)) as valorTotal ";	
		}
	
		hql += this.getFromHQLDetalhesDiferencaCota(filtro);
		
		Query query = this.getSession().createQuery(hql);

		if (filtro.getIdDiferenca() != null) {

			query.setParameter("idDiferenca", filtro.getIdDiferenca());
		}
		
		if (filtro.getNumeroCota()!=null){
		    
			query.setParameter("numeroCota", filtro.getNumeroCota());
		}

		query.setMaxResults(1);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DetalheDiferencaCotaDTO.class));

		return (DetalheDiferencaCotaDTO) query.uniqueResult();
	}

	private String getHQLDetalhesDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select ")
		   .append(" mec.data as data, ")
		   .append(" rateioDiferenca.cota.numeroCota as numeroCota, ")
		   .append(" case rateioDiferenca.cota.pessoa.class ")
		   .append("       when 'F' then rateioDiferenca.cota.pessoa.nome ")
		   .append("       when 'J' then rateioDiferenca.cota.pessoa.razaoSocial end  as nomeCota,")
		   .append(" rateioDiferenca.cota.box.codigo as codigoBox, ")
		   .append(" rateioDiferenca.qtde as exemplares, ")
		   .append(" coalesce(mec.valoresAplicados.precoComDesconto,produtoEdicao.precoVenda) as precoDesconto, ")
		   .append(" case when diferenca.statusConfirmacao = :statusConfirmado then ")
		   .append(" (coalesce(rateioDiferenca.qtde * (coalesce(mec.valoresAplicados.precoComDesconto,produtoEdicao.precoVenda)),0)) ")
		   .append(" else 0 end as totalAprovadas, ")
		   .append(" case when diferenca.statusConfirmacao = :statusPendente then ")
		   .append(" (rateioDiferenca.qtde * (coalesce(mec.valoresAplicados.precoComDesconto,produtoEdicao.precoVenda))) ")
		   .append(" else 0 end as totalRejeitadas, ")
		   .append(" coalesce(rateioDiferenca.qtde * (coalesce(mec.valoresAplicados.precoComDesconto,produtoEdicao.precoVenda)),0) as valorTotal ")
		   
		   .append(this.getFromHQLDetalhesDiferencaCota(filtro));
		   
		return hql.toString();
	}
	
	private StringBuilder getFromHQLDetalhesDiferencaCota(FiltroDetalheDiferencaCotaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from RateioDiferenca rateioDiferenca ");
	    hql.append(" inner join rateioDiferenca.diferenca.produtoEdicao as produtoEdicao ");
	    hql.append(" inner join rateioDiferenca.diferenca.lancamentoDiferenca.movimentosEstoqueCota as mec ");
	    hql.append(" inner join produtoEdicao.produto.fornecedores as fornecedor ");
	    hql.append(" inner join rateioDiferenca.diferenca as diferenca ");
	    hql.append(" where rateioDiferenca.diferenca.id = :idDiferenca ");

			if (filtro.getNumeroCota()!=null){
			    
				hql.append(" and rateioDiferenca.cota.numeroCota = :numeroCota ");
			}
		   
			hql.append(" group by rateioDiferenca.id ");
			
		return hql;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<RateioDiferenca> obterRateiosPorDiferenca(Long id) {
		
		String hql = "select r from RateioDiferenca r where r.diferenca.id = :idDiferenca";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("idDiferenca", id);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RateioDiferencaDTO> obterRateiosParaImpressaoPorDiferenca(Long idProdutoEdicao, Date dataMovimento) {
		
		String hql = " select cota.numeroCota as numeroCota, rd.qtde as qtde " +
					 " from RateioDiferenca rd " +
					 " inner join rd.cota cota " +
					 " where rd.diferenca.produtoEdicao.id = :idProdutoEdicao " +
					 " and rd.diferenca.dataMovimento = :dataMovimento " +
					 " and rd.diferenca.statusConfirmacao != :statusConfirmacao " +
					 " order by cota.numeroCota";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("dataMovimento", dataMovimento);
		query.setParameter("statusConfirmacao", StatusConfirmacao.CANCELADO);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RateioDiferencaDTO.class));
		
		return query.list();
	}

}