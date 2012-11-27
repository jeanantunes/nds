package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.RomaneioRepository;

@Repository
public class RomaneioRepositoryImpl extends AbstractRepositoryModel<Box, Long> implements RomaneioRepository {

	public RomaneioRepositoryImpl() {
		super(Box.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RomaneioDTO> buscarRomaneios(FiltroRomaneioDTO filtro, 
			boolean limitar) {
		
		Query query = this.createQueryBuscarRomaneio(filtro, true);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				RomaneioDTO.class));
		
		// Realiza a paginação:
		if (limitar) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return  query.list();
		//return  popularEndereco(query.list());
	}
	
	
	/**
	 * Gera a consulta que pesquisa os romaneios com os critérios definidos
	 * pelo usuário.
	 * 
	 * @param filtro
	 * @param ordenarConsulta
	 * 
	 * @return
	 */
	private Query createQueryBuscarRomaneio(FiltroRomaneioDTO filtro, 
			boolean ordenarConsulta) {
		
		StringBuilder hql = new StringBuilder();
		
		/*
		 * Incluido a condição DISTINCT para retirar a repetição 
		 * (plano cartesiano) pelos ProdutosEdição contido nas NotaEnvioItem,
		 * RotaPDV e Cotas.
		 */
		hql.append("SELECT DISTINCT ");
		hql.append("  notaEnvio.destinatario.numeroCota as numeroCota ");
		hql.append(", notaEnvio.destinatario.nome as nome ");
//		hql.append(", cota.id as idCota ");
//		hql.append(", rotaPDV.id as idRota ");
		hql.append(", notaEnvio.numero as numeroNotaEnvio ");
		hql.append(", endereco.logradouro as logradouro ");
//		hql.append(", endereco.bairro as bairro ");		
//		hql.append(", endereco.cidade as cidade ");
//		hql.append(", endereco.uf as uf ");
		
		//if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
		if (filtro.getProdutos() != null){
		
			hql.append(", round(itemNota.reparte / lancamento.produtoEdicao.pacotePadrao) as pacote ");
			//hql.append(", lancDif.diferenca.qtde as quebra ");
			hql.append(", itemNota.reparte as reparteTotal ");
		}
		
		// Código comentado pelo Eduardo Punk Rock
		/*if (filtro.getProdutos() != null && filtro.getProdutos().size() > 1){
			
			this.getHQLProdutos(hql, filtro);
		}*/
		
		hql.append(getSqlFromEWhereRomaneio(filtro));
		
		if (ordenarConsulta) {
			hql.append(getOrderBy(filtro, false));
		}
		
		Query query =  getSession().createQuery(hql.toString());
		this.setarParametrosRomaneio(filtro, query);
		
		return query;
	}
	
	private String getSqlFromEWhereRomaneio(FiltroRomaneioDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Cota cota, Lancamento lancamento, NotaEnvio notaEnvio ");
		
		/*if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
			
			hql.append(", LancamentoDiferenca lancDif ");
		}*/
		
		hql.append(" JOIN cota.box as box ");
		hql.append(" JOIN cota.pdvs as pdv ");
		hql.append(" JOIN pdv.rotas as rotaPDV ");
		hql.append(" JOIN rotaPDV.rota as rota ");
		hql.append(" JOIN rota.roteiro as roteiro ");
		hql.append(" JOIN notaEnvio.listaItemNotaEnvio as itemNota ");
		hql.append(" JOIN notaEnvio.destinatario.endereco as endereco ");		
		
		/*if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
			
			hql.append(" JOIN lancDif.movimentoEstoqueCota as movEstCotaLancDif ");
		}*/
		
		hql.append(" where cota.numeroCota = notaEnvio.destinatario.numeroCota ");
		hql.append(" and lancamento.produtoEdicao.id = itemNota.produtoEdicao.id ");
		hql.append(" and cota.situacaoCadastro != :situacaoInativo ");
		hql.append(" and pdv.caracteristicas.pontoPrincipal = :pontoPrincipal ");
		
		/*if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
			
			hql.append(" and movEstCotaLancDif.id = movimentoEstoque.id ");
		}*/

		if(filtro.getIdBox() != null) {
			
			hql.append( " and box.id = :idBox ");
		}
		
		if(filtro.getIdRoteiro() != null){
			
			hql.append( " and roteiro.id = :idRoteiro ");
		}
		
		if(filtro.getIdRota() != null){
			
			hql.append( " and rota.id = :idRota ");
		}
		
		if(filtro.getData() != null){
			
			hql.append(" and lancamento.dataLancamentoDistribuidor = :data ");
		}
		
		if (filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()){
			
			hql.append(" and lancamento.produtoEdicao.id in (:produtos) ");
		}

		return hql.toString();
	}
	
	
	/**
	 * Incluir uma virgula (separador) para os critérios de order by.<br>
	 * Caso não haja consulta HQL não incluirá nada, retornando o próprio
	 * parâmetro.
	 * 
	 * @param hql
	 * @return
	 */
	private StringBuilder addSeparadorOrderBy(StringBuilder hql) {
		if (hql != null && hql.length() > 0) {
			hql.append(", ");
		}
		
		return hql;
	}
	
	private String getOrderBy(FiltroRomaneioDTO filtro, boolean isImpressao) {
		
		if(filtro.getPaginacao() == null 
				|| filtro.getPaginacao().getSortColumn() == null) {
			return "";
		}
		
		StringBuilder hql = new StringBuilder();
		
		if (filtro.getIdRoteiro() == null && filtro.getIdRota() != null) {
			hql = addSeparadorOrderBy(hql);
			hql.append(" rotaPDV.ordem ");
		}
		
		if (filtro.getIdRoteiro() != null && filtro.getIdRota() == null) {
			hql = addSeparadorOrderBy(hql);
			hql.append(" roteiro.ordem ");
		}
		
		if (filtro.getIdRoteiro() != null && filtro.getIdRota() != null) {
			hql = addSeparadorOrderBy(hql);
			hql.append(" roteiro.ordem asc, rotaPDV.ordem ");
		}
		
		if ("numeroCota".equals(filtro.getPaginacao().getSortColumn())) {
			hql = addSeparadorOrderBy(hql);
			hql.append(" cota.numeroCota ");
		} else if ("nome".equals(filtro.getPaginacao().getSortColumn())) {
			hql = addSeparadorOrderBy(hql);
			hql.append(" notaEnvio.destinatario.nome ");
		} else {
			hql = addSeparadorOrderBy(hql);
			hql.append(" notaEnvio.numero ");
		}
		
		
		if (hql.length() > 0 && filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		/* Quando for impressão, a primeira ordenação é pela rotaPDV.id */
		if (isImpressao) {
			hql.insert(0, " rotaPDV.id asc, ");
		}
		
		
		if (hql.length() > 0) {
			hql.insert(0, " order by ");
		}
		
		
		return hql.toString();
	}
	
	private void setarParametrosRomaneio(FiltroRomaneioDTO filtro, Query query) {
		
		query.setParameter("situacaoInativo", SituacaoCadastro.INATIVO);
		query.setParameter("pontoPrincipal", true);
		
		if(filtro.getIdBox() != null) { 
			
			query.setParameter("idBox", filtro.getIdBox());
		}
		
		if(filtro.getIdRoteiro() != null){
			
			query.setParameter("idRoteiro", filtro.getIdRoteiro());
		}
		
		if(filtro.getIdRota() != null){
			
			query.setParameter("idRota", filtro.getIdRota());
		}
		
		if (filtro.getData() != null){
			
			query.setParameter("data", filtro.getData());
		}
		
		if (filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()){
		
			query.setParameterList("produtos", filtro.getProdutos());

		}
	}

	@Override
	public Integer buscarTotal(FiltroRomaneioDTO filtro, boolean countCotas) {
		
		Number totalRegistros = null;
		if (countCotas) {
			
			StringBuilder hql = new StringBuilder();
			
			hql.append("select count( "+ (countCotas ? "distinct" : "") +" cota.numeroCota) ");
			hql.append(getSqlFromEWhereRomaneio(filtro));
			hql.append(getOrderBy(filtro, false));
			
			Query query =  getSession().createQuery(hql.toString());
			
			this.setarParametrosRomaneio(filtro, query);
			
			totalRegistros = (Long) query.uniqueResult();
		} else {
			
			Query query = createQueryBuscarRomaneio(filtro, false);
			
			@SuppressWarnings("rawtypes")
			List lst = query.list();
			
			totalRegistros = (lst == null || lst.isEmpty()) ? 0 : lst.size();
		}
	
		return totalRegistros.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RomaneioDTO> buscarRomaneiosParaExportacao(
			FiltroRomaneioDTO filtro) {
		
		Query query = this.createQueryBuscarRomaneioParaExportacao(filtro);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				RomaneioDTO.class));
		
		return query.list();
	}
	
	/**
	 * Gera a consulta que pesquisa os romaneios com os critérios definidos
	 * pelo usuário.
	 * 
	 * @param filtro
	 * @param ordenarConsulta
	 * 
	 * @return
	 */
	private Query createQueryBuscarRomaneioParaExportacao(
			FiltroRomaneioDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		/*
		 * Incluido a condição DISTINCT para retirar a repetição 
		 * (plano cartesiano) pelos ProdutosEdição contido nas NotaEnvioItem,
		 * RotaPDV e Cotas.
		 */
		hql.append("SELECT DISTINCT ");
		hql.append("  notaEnvio.destinatario.numeroCota as numeroCota ");
		hql.append(", notaEnvio.destinatario.nome as nome ");
//		hql.append(", cota.id as idCota ");
		hql.append(", rotaPDV.id as idRota ");
		hql.append(", notaEnvio.numero as numeroNotaEnvio ");
		hql.append(", endereco.logradouro as logradouro ");
//		hql.append(", endereco.bairro as bairro ");		
//		hql.append(", endereco.cidade as cidade ");
//		hql.append(", endereco.uf as uf ");
		
		//if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
		if (filtro.getProdutos() != null){
		
			hql.append(", round(itemNota.reparte / lancamento.produtoEdicao.pacotePadrao) as pacote ");
			//hql.append(", lancDif.diferenca.qtde as quebra ");
			hql.append(", itemNota.reparte as reparteTotal ");
		}
		
		// Código comentado pelo Eduardo Punk Rock
		/*if (filtro.getProdutos() != null && filtro.getProdutos().size() > 1){
			
			this.getHQLProdutos(hql, filtro);
		}*/
		
		hql.append(getSqlFromEWhereRomaneio(filtro));
		hql.append(getOrderBy(filtro, true));		
		Query query =  getSession().createQuery(hql.toString());
		this.setarParametrosRomaneio(filtro, query);
		
		return query;
	}
	
}