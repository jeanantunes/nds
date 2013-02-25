package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * 
 * MixCotaProduto
 */

@Repository
public class MixCotaProdutoRepositoryImpl extends
		AbstractRepositoryModel<MixCotaProduto, Long> implements
		MixCotaProdutoRepository {

	public MixCotaProdutoRepositoryImpl() {
		super(MixCotaProduto.class);
	}

	

	@Override
	public List<MixCotaDTO> pesquisarPorCota(
			FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
		StringBuilder hql = new StringBuilder("");

		hql.append("select ")
				.append(" m.id as id, ")
				.append(" produto.codigo as codigoProduto,")
				.append(" produto.nome as nomeProduto,")
				.append(" m.produto.tipoClassificacaoProduto.descricao as classificacaoProduto,")
				.append(" m.dataHora as dataHora,")
				.append(" m.vendaMedia as vendaMedia, ")
				.append(" m.reparteMedio as reparteMedio, ")
				.append(" m.ultimoReparte as ultimoReparte, ")
				.append(" m.reparteMinimo as reparteMinimo, ")
				.append(" m.reparteMaximo as reparteMaximo, ")
				.append(" m.usuario.login as usuario ")

				.append(" from MixCotaProduto m")

				.append(" left join m.cota.pessoa as pessoa ")
				.append(" join m.produto as produto ")
				.append(" where m.cota.tipoDistribuicaoCota = :tipoCota ")
				.append(" and upper(m.cota.numeroCota) = upper(:cota)");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("tipoCota", TipoDistribuicaoCota.ALTERNATIVO);
		query.setParameter("cota", filtroConsultaMixCotaDTO.getCota());
		query.setResultTransformer(new AliasToBeanResultTransformer(
				MixCotaDTO.class));
		configurarPaginacaoCota(filtroConsultaMixCotaDTO, query);
		return query.list();
	}
	
	
	
	private void configurarPaginacaoCota(FiltroConsultaMixPorCotaDTO dto,
			Query query) {

		PaginacaoVO paginacao = dto.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if (paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}



	@Override
	public List<MixProdutoDTO> pesquisarPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO) {
		boolean isClassificacaoPreenchida = filtroConsultaMixProdutoDTO.getClassificacaoProduto()!=null && filtroConsultaMixProdutoDTO.getClassificacaoProduto()!="";
		StringBuilder hql = new StringBuilder("");

		hql.append("select ")
				.append(" m.id as id, ")
				.append(" m.cota.numeroCota as numeroCota,")
				.append(" m.dataHora as dataHora,")
				.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '')  as nomeCota,")
				.append(" m.vendaMedia as vendaMedia, ")
				.append(" m.reparteMedio as reparteMedio, ")
				.append(" m.ultimoReparte as ultimoReparte, ")
				.append(" m.reparteMinimo as reparteMinimo, ")
				.append(" m.reparteMaximo as reparteMaximo, ")
				.append(" m.usuario.login as usuario ")

				.append(" from MixCotaProduto m")

				.append(" left join m.cota.pessoa as pessoa ")
				.append(" join m.produto as produto ")
				.append(" where m.cota.tipoDistribuicaoCota = :tipoCota) ")
				.append(" and  upper(m.produto.codigo) = upper(:codigoProduto)");
				if(isClassificacaoPreenchida){
					hql.append(" and upper(m.produto.tipoClassificacaoProduto.descricao) = upper(:classificacaoProduto)");
				}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("tipoCota", TipoDistribuicaoCota.ALTERNATIVO);
		query.setParameter("codigoProduto", filtroConsultaMixProdutoDTO.getCodigoProduto());
		if(isClassificacaoPreenchida){
			query.setParameter("classificacaoProduto", filtroConsultaMixProdutoDTO.getClassificacaoProduto());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				MixProdutoDTO.class));
		configurarPaginacaoProduto(filtroConsultaMixProdutoDTO, query);
		return query.list();
	}

	private void configurarPaginacaoProduto(FiltroConsultaMixPorProdutoDTO dto,
			Query query) {

		PaginacaoVO paginacao = dto.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if (paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}

	

}
