package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.repository.NotaFiscalRepository;


/**
 * Implementação do repositório de Pessoa
 
 * @author francisco.garcia
 *
 */
@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepository<NotaFiscal, Long> implements
		NotaFiscalRepository {

	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}

	
	@Override
	@SuppressWarnings("unchecked")	
	public List<NotaFiscal> buscarNotaFiscal(Date emissao, Date entradaRecebimento,Long valorBruto, Long valorLiquido, Long valorDescontado, String tipoNota, String cfop) {
		/*select * from Nota_Fiscal nota, CFOP cfop, tipo_Nota_Fiscal tipoNota,
		Item_Nota_Fiscal itemNota, Produto_Edicao pe, Recebimento_Fisico recebimento 
		where nota.dataEmissao ='1970-01-01 00:00:01' and 
		recebimento.data = '1970-01-01 00:00:01' and 
		(itemNota.quantidade * pe.precoCusto) = 230 and
						(itemNota.quantidade * pe.precoVenda) = 230 and
						cfop.codigo = 23453 and 
		                tipoNota.descricao = "Desc_nota" and
						(select sum(pe.desconto) from Produto_Edicao pe) = 345;*/
		String hql = "from NotaFiscal nota join fetch nota.cfop as cfop join fetch nota.tipoNotaFiscal as tipoNota," +
				"ItemNotaFiscal itemNota join fetch itemNota.notaFiscal join fetch itemNota.produtoEdicao pe, " +
				"RecebimentoFisico recebimento join fetch recebimento.notaFiscal " +
				"where nota.dataEmissao = :emissao and " +
				"recebimento.data = :entradaRecebimento and " +
				"(itemNota.quantidade * pe.precoCusto) = :valorBruto and " +
				"(itemNota.quantidade * pe.precoVenda) = :valorLiquido and " +
				"cfop.codigo = :cfop and " +
				"tipoNota.descricao = :tipoNota and " +
				"(select sum(pe.desconto) from pe) = :valorDescontado";
		
		
		Query query = getSession().createQuery(hql);
		query.setDate("emissao",emissao);
		query.setDate("entradaRecebimento", entradaRecebimento);
		query.setLong("valorBruto", valorBruto);
		query.setLong("valorLiquido", valorLiquido);
		query.setLong("valorDescontado", valorDescontado);
		query.setString("tipoNota", tipoNota);
		query.setString("cfop", cfop);
		
		return query.list();
	}
	
	@Override	
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		this.adicionar(notaFiscal);
		
	}

	
}
