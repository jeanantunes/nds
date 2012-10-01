package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.RomaneioRepository;

@Repository
public class RomaneioRepositoryImpl extends AbstractRepositoryModel<Box, Long> implements RomaneioRepository {

	public RomaneioRepositoryImpl() {
		super(Box.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RomaneioDTO> buscarRomaneios(FiltroRomaneioDTO filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nome, ");
		hql.append("cota.id as idCota, ");
		hql.append("rota.id as idRota, ");
		hql.append("itemNota.itemNotaEnvioPK.notaEnvio.numero as numeroNotaEnvio ");
		
		if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
		
			hql.append(", (round(estudo.qtdeReparte / lancamento.produtoEdicao.pacotePadrao)) as pacote ");
			hql.append(", lancDif.diferenca.qtde as quebra ");
			hql.append(", estudo.qtdeReparte as reparteTotal ");
		}
		
		if (filtro.getProdutos() != null && filtro.getProdutos().size() > 1){
			
			this.getHQLProdutos(hql, filtro);
		}
		
		hql.append(getSqlFromEWhereRomaneio(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		this.setarParametrosRomaneio(filtro, query, false);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RomaneioDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null){ 
			
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar){ 
			
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
				
		return  popularEndereco(query.list());
	}
	
	private void getHQLProdutos(StringBuilder hql, FiltroRomaneioDTO filtro) {
		
		for (int index = 0 ; index < filtro.getProdutos().size() ; index++){
			
			hql.append(", coalesce((select estudo.qtdeReparte ")
			   .append(" from estudo ")
			   .append(" where estudo.id = estudoCota.estudo.id ")
			   .append(" and estudoCota.cota.id = cota.id ")
			   .append(" and lancamento.estudo.id = estudo.id ")
			   .append(" and lancamento.produtoEdicao.id = :idProdutoEdicao").append(index)
			   .append(" and lancamento.dataLancamentoDistribuidor = :data),0) as qtdProduto").append(index);
		}
	}

	private List<RomaneioDTO> popularEndereco(List<RomaneioDTO> listaRomaneios){
		List<RomaneioDTO> listaAux = new ArrayList<RomaneioDTO>();
		for(RomaneioDTO romaneio:listaRomaneios){
			StringBuilder hql = new StringBuilder();
			
			hql.append("SELECT endereco.logradouro as logradouro, ");
			hql.append("endereco.bairro as bairro, ");		
			hql.append("endereco.cidade as cidade, ");
			hql.append("endereco.uf as uf ");
			
			hql.append(" from EnderecoCota endCota ");
			hql.append(" LEFT JOIN endCota.endereco as endereco ");
			
			hql.append( " WHERE endCota.cota.id =:idCota ");
			hql.append( " AND (endCota.tipoEndereco = 'LOCAL_ENTREGA' OR endCota.principal = 1) ");
			
			Query query =  getSession().createQuery(hql.toString());
			
			query.setParameter("idCota", romaneio.getIdCota());
			
			query.setMaxResults(1);
			
			query.setResultTransformer(new AliasToBeanResultTransformer(Endereco.class));
			
			Endereco dto = (Endereco) query.uniqueResult();
			
			if(dto != null){
				
				romaneio.setLogradouro(
					dto.getLogradouro() + ", " + 
					dto.getBairro() + ", " + 
					dto.getCidade() + " - " + 
					dto.getUf());
			}
			
			listaAux.add(romaneio);
		}
		return listaAux;
	}
	
	private String getSqlFromEWhereRomaneio(FiltroRomaneioDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Cota cota, Lancamento lancamento, Estudo estudo, EstudoCota estudoCota ");
		
		if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
			
			hql.append(", LancamentoDiferenca lancDif ");
		}
		
		hql.append(" JOIN cota.pessoa as pessoa ");
		hql.append(" JOIN cota.box as box ");
		hql.append(" JOIN cota.pdvs as pdv ");
		hql.append(" JOIN pdv.rotas as rota ");
		hql.append(" JOIN rota.roteiro as roteiro ");
		hql.append(" JOIN lancamento.movimentoEstoqueCotas as movimentoEstoque ");
		hql.append(" JOIN movimentoEstoque.listaItemNotaEnvio as itemNota ");
		
		if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
			
			hql.append(" JOIN lancDif.movimentoEstoqueCota as movEstCotaLancDif ");
		}
		
		hql.append(" where estudoCota.estudo.id = estudo.id ");
		hql.append(" and estudoCota.cota.id = cota.id ");
		hql.append(" and lancamento.estudo.id = estudo.id ");
		hql.append(" and cota.situacaoCadastro != :situacaoInativo ");
		
		if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
			
			hql.append(" and movEstCotaLancDif.id = movimentoEstoque.id ");
		}

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
		
		if (filtro.getProdutos() != null && filtro.getProdutos().size() == 1){
			
			hql.append(" and lancamento.produtoEdicao.id in (:produtos) ");
		}

		return hql.toString();
	}
	
	private String getOrderBy(FiltroRomaneioDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by rota.id asc ");
		
		if(filtro.getIdRoteiro() == null && filtro.getIdRota() != null){
			
			hql.append(", rota.ordem ");
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRota() == null){
			
			hql.append(", roteiro.ordem ");
		}
		
		if(filtro.getIdRoteiro() != null && filtro.getIdRota() != null){
			
			hql.append(", roteiro.ordem asc, rota.ordem asc ");
		}
		
		if ("numeroCota".equals(filtro.getPaginacao().getSortColumn())){
			
			hql.append(", cota.numeroCota ");
		} else if ("nome".equals(filtro.getPaginacao().getSortColumn())){
			
			hql.append(", pessoa.nome ");
		} else {
			
			hql.append(", itemNota.itemNotaEnvioPK.notaEnvio.numero ");
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	private void setarParametrosRomaneio(FiltroRomaneioDTO filtro, Query query, boolean queryCount){
		
		query.setParameter("situacaoInativo", SituacaoCadastro.INATIVO);
		
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
			
			if (filtro.getProdutos().size() == 1){
			
				query.setParameterList("produtos", filtro.getProdutos());
			} else if (!queryCount){
				
				for (int index = 0 ; index < filtro.getProdutos().size() ; index++){
					
					query.setParameter("idProdutoEdicao" + index, filtro.getProdutos().get(index));
				}
			}
		}
	}

	@Override
	public Integer buscarTotal(FiltroRomaneioDTO filtro, boolean countCotas) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select count( "+ (countCotas ? "distinct" : "") +" cota.numeroCota) ");
		
		hql.append(getSqlFromEWhereRomaneio(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		this.setarParametrosRomaneio(filtro, query, true);
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}
}