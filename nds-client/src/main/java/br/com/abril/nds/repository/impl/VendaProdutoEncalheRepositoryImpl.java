package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.model.estoque.VendaProduto;
import br.com.abril.nds.repository.VendaProdutoEncalheRepository;

@Repository
public class VendaProdutoEncalheRepositoryImpl extends AbstractRepository<VendaProduto, Long> implements VendaProdutoEncalheRepository {

	public VendaProdutoEncalheRepositoryImpl() {
		super(VendaProduto.class);
	}
	
	@Override
	public Long buscarQntVendaEncalhe(FiltroVendaEncalheDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqlFromEWhereVendaEncalhe(filtro,true));
		
		Query query = getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosVendaProdutoEncalhe(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		return (Long) query.uniqueResult();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VendaEncalheDTO> buscarVendasEncalhe(FiltroVendaEncalheDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqlFromEWhereVendaEncalhe(filtro,false));
		
		hql.append(getOrderVendaEncalhe(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosVendaProdutoEncalhe(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaEncalheDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	private String getSqlFromEWhereVendaEncalhe(FiltroVendaEncalheDTO filtro, boolean isCount){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select ");
		
		if(isCount){
			
			hql.append(" count ( venda.id ) ");
		}
		else{
			
			hql.append(" venda.id as idVenda ,")
			.append(" venda.dataVenda as dataVenda ,")
			.append(" case venda.cota.pessoa.class when 'F' then venda.cota.pessoa.nome when 'J' then venda.cota.pessoa.razaoSocial end  as nomeCota ,")
			.append(" venda.cota.numeroCota as numeroCota ,")
			.append(" venda.tipoVenda as tipoVendaEncalhe ,")
			.append(" venda.produtoEdicao.numeroEdicao as numeroEdicao ,")
			.append(" venda.produtoEdicao.produto.nome as nomeProduto ,")
			.append(" venda.produtoEdicao.produto.codigo as codigoProduto ,")
			.append(" venda.produtoEdicao.precoVenda - venda.produtoEdicao.desconto as precoCapa ,")
			.append(" venda.produtoEdicao.desconto as precoDesconto ,")
			.append(" venda.valorTotalVenda as valoTotalProduto ,")
			.append(" venda.qntProduto as qntProduto ");
		}
	
		hql.append(" from VendaProduto venda ")
			.append(" where venda.cota.numeroCota=:numeroCota ")
			.append(" and venda.dataVenda between :periodoInicial and :periodoFinal ");
		
		if(filtro.getTipoVendaEncalhe()!= null){
			hql.append(" and venda.tipoVenda=:tipoVenda ");
		}
		
		return hql.toString();
	}
	
	private HashMap<String,Object> getParametrosVendaProdutoEncalhe(FiltroVendaEncalheDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("numeroCota", filtro.getNumeroCota());
		param.put("periodoInicial", filtro.getPeriodoInicial());
		param.put("periodoFinal", filtro.getPeriodoFinal());
		
		if(filtro.getTipoVendaEncalhe() != null ){ 
			param.put("tipoVenda", filtro.getTipoVendaEncalhe());
		}
	
		return param;
	}
	
	private String getOrderVendaEncalhe(FiltroVendaEncalheDTO filtro){
		
		if(filtro.getOrdenacaoColuna() == null){
			return "";
		}

		StringBuilder hql = new StringBuilder();
		
		switch (filtro.getOrdenacaoColuna()) {
			case CODIGO_PRODUTO:	
				hql.append(" order by venda.produtoEdicao.produto.codigo ");
				break;
			case DATA:	
				hql.append(" order by venda.dataVenda ");
				break;
			case NOME_COTA:	
				hql.append(" order by case venda.cota.pessoa.class when 'F' then venda.cota.pessoa.nome when 'J' then venda.cota.pessoa.razaoSocial end ");
				break;
			case NOME_PRODUTO:	
				hql.append(" order by venda.produtoEdicao.produto.nome ");
				break;
			case NUMERO_COTA:	
				hql.append(" order by  venda.cota.numeroCota ");
				break;
			case NUMERO_EDICAO:	
				hql.append(" order by venda.produtoEdicao.numeroEdicao ");
				break;
			case PRECO_CAPA:	
				hql.append(" order by venda.produtoEdicao.precoVenda - venda.produtoEdicao.desconto ");
				break;
			case PRECO_DESCONTO:	
				hql.append(" order by venda.produtoEdicao.desconto ");
				break;
			case QNT_PRODUTO:	
				hql.append(" order by venda.qntProduto ");
				break;
			case TIPO_VENDA:	
				hql.append(" order by venda.tipoVenda ");
				break;
			case TOTAL_VENDA:	
				hql.append(" order by venda.valorTotalVenda ");
				break;
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	@Override
	public VendaEncalheDTO buscarVendaProdutoEncalhe(Long idVendaProduto){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select ")
			.append(" venda.id as idVenda ,")
			.append(" venda.dataVenda as dataVenda ,")
			.append(" case venda.cota.pessoa.class when 'F' then venda.cota.pessoa.nome when 'J' then venda.cota.pessoa.razaoSocial end  as nomeCota ,")
			.append(" venda.cota.numeroCota as numeroCota ,")
			.append(" venda.produtoEdicao.numeroEdicao as numeroEdicao ,")
			.append(" venda.produtoEdicao.produto.nome as nomeProduto ,")
			.append(" venda.produtoEdicao.produto.codigo as codigoProduto ,")
			.append(" venda.produtoEdicao.precoVenda - venda.produtoEdicao.desconto as precoCapa ,")
			.append(" venda.produtoEdicao.desconto as precoDesconto ,")
			.append(" venda.valorTotalVenda as valoTotalProduto ,")
			.append(" venda.qntProduto as qntProduto ,")
			.append(" venda.produtoEdicao.codigoDeBarras as codigoBarras ,")
			.append(" venda.produtoEdicao.produto.formaComercializacao as formaVenda ,")
			.append(" venda.cota.box.codigo as codBox ,")
			.append(" venda.tipoVenda as tipoVendaEncalhe ,")
			.append(" venda.dataVencimentoDebito as dataVencimentoDebito ")
	        .append(" from VendaProduto venda ")
			.append(" where venda.id=:idVendaProduto");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idVendaProduto", idVendaProduto);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaEncalheDTO.class));
		
		return (VendaEncalheDTO) query.uniqueResult();
			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VendaProduto> buscarVendaProdutoEncalhe(Long numeroCota, Date dataVenda,Long... idProdutoEdicao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select venda from VendaProduto venda join venda.cota cota join venda.produtoEdicao produto " )
			.append("where cota.numeroCota=:numeroCota and produto.id in(:idProdutoEdicao) and venda.dataVenda =:dataVenda ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("numeroCota", numeroCota.intValue());
		query.setParameter("dataVenda", dataVenda);
		query.setParameterList("idProdutoEdicao", idProdutoEdicao);
		
		return query.list();
	}
}
