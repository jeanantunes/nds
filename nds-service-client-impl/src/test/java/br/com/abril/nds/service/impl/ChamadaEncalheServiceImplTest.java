package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.dto.NotaEnvioProdutoEdicao;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;

@RunWith( MockitoJUnitRunner.class )
public class ChamadaEncalheServiceImplTest {

	public static Date criarData(int dia, int mes, int ano) {
		Calendar data = criarCalendar(dia, mes, ano, 0, 0, 0);
		return data.getTime();
	}
	
	private static Calendar criarCalendar(int dia, int mes, int ano, int hora,
			int minuto, int segundo) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, dia);
		data.set(Calendar.MONTH, mes);
		data.set(Calendar.YEAR, ano);
		data.set(Calendar.HOUR_OF_DAY, hora);
		data.set(Calendar.MINUTE, minuto);
		data.set(Calendar.SECOND, segundo);
		data.clear(Calendar.MILLISECOND);
		return data;
	}
	
	private List<NotaEnvioProdutoEdicao> obterListaNotaEnvioProdutoEdicao() {
		
		List<NotaEnvioProdutoEdicao> lista = new ArrayList<>();
		
		NotaEnvioProdutoEdicao nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(1L);
		nota.setDataEmissao(criarData(10, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.TEN);
		lista.add(nota);
		
		
		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(2L);
		nota.setDataEmissao(criarData(13, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.ONE);
		lista.add(nota);
		
		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(2L);
		nota.setDataEmissao(criarData(14, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.TEN);
		lista.add(nota);
		
		
		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(3L);
		nota.setDataEmissao(criarData(15, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.TEN);
		lista.add(nota);
		
		
		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(4L);
		nota.setDataEmissao(criarData(16, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.TEN);
		lista.add(nota);
	
		return lista;
	}
	

	
	private Map<Long, List<NotaEnvioProdutoEdicao>> obterMapaNEPE() {
		
		Map<Long, List<NotaEnvioProdutoEdicao>> mapa = new HashMap<Long, List<NotaEnvioProdutoEdicao>>();
		
		List<NotaEnvioProdutoEdicao> lista = new ArrayList<>();
		NotaEnvioProdutoEdicao nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(1L);
		nota.setDataEmissao(criarData(10, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.TEN);
		lista.add(nota);
		mapa.put(1L, lista);
		
		
		lista = new ArrayList<>();
		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(2L);
		nota.setDataEmissao(criarData(13, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.ONE);
		lista.add(nota);
		
		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(2L);
		nota.setDataEmissao(criarData(14, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.TEN);
		lista.add(nota);
		mapa.put(2L, lista);
		
		lista = new ArrayList<>();
		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(3L);
		nota.setDataEmissao(criarData(15, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.TEN);
		lista.add(nota);
		mapa.put(3L, lista);

		
		
		lista = new ArrayList<>();
		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(4L);
		nota.setDataEmissao(criarData(16, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.ONE);
		lista.add(nota);

		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(4L);
		nota.setDataEmissao(criarData(17, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.ZERO);
		lista.add(nota);

		nota = new NotaEnvioProdutoEdicao();
		nota.setIdProdutoEdicao(4L);
		nota.setDataEmissao(criarData(18, Calendar.JANUARY, 2013));
		nota.setNumeroNotaEnvio(new BigInteger("100"));
		nota.setReparte(BigInteger.TEN);
		lista.add(nota);
		mapa.put(4L, lista);

		
		
		return mapa;		
		
		
	}
	
	
	private List<ProdutoEmissaoDTO> obterListaProdutoEmissaoCE(boolean possuiDetalheMultiNotaEnvio){
		
		List<ProdutoEmissaoDTO> produtosEmissao = new ArrayList<>();
		
		ProdutoEmissaoDTO p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(1L);
		p.setCodigoProduto("0001");
		p.setNomeProduto("CARAS");
		p.setReparte(BigInteger.TEN);
		produtosEmissao.add(p);
		
		
		p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(2L);
		p.setCodigoProduto("0002");
		p.setNomeProduto("VEJA");
		p.setReparte(BigInteger.TEN);
		if(possuiDetalheMultiNotaEnvio) {
			p.setDescricaoNotaEnvio("Nota 1 xxxx Nota 2 kkkkk Nota 3 dddddd");
		}
		produtosEmissao.add(p);
		
		
		p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(3L);
		p.setCodigoProduto("0003");
		p.setNomeProduto("VIVA MAIS");
		p.setReparte(BigInteger.TEN);
		produtosEmissao.add(p);
		
		
		p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(4L);
		p.setCodigoProduto("0004");
		p.setNomeProduto("COQUETEL");
		p.setReparte(BigInteger.TEN);
		if(possuiDetalheMultiNotaEnvio) {
			p.setDescricaoNotaEnvio("Nota 34 yyyNota 15 oooooo");
		}
		produtosEmissao.add(p);
		
		
		return produtosEmissao;
		
	}
	
	private List<ProdutoEmissaoDTO> obterListaProdutoEmissaoCEComDetalheNotaDuplicado(){
		
		List<ProdutoEmissaoDTO> produtosEmissao = new ArrayList<>();
		
		ProdutoEmissaoDTO p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(1L);
		p.setCodigoProduto("0001");
		p.setNomeProduto("CARAS");
		produtosEmissao.add(p);
		
		p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(2L);
		p.setCodigoProduto("0002");
		p.setNomeProduto("VEJA");
		p.setDescricaoNotaEnvio("Nota 1 xxxx Nota 2 kkkkk Nota 3 dddddd");
		produtosEmissao.add(p);

		p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(2L);
		p.setCodigoProduto("0002");
		p.setNomeProduto("VEJA");
		p.setDescricaoNotaEnvio("Nota 1 xxxx Nota 2 kkkkk Nota 3 dddddd");
		p.setProdutoDuplicadoDetalheNota(true);
		produtosEmissao.add(p);

		
		p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(3L);
		p.setCodigoProduto("0003");
		p.setNomeProduto("VIVA MAIS");
		produtosEmissao.add(p);
		
		p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(4L);
		p.setCodigoProduto("0004");
		p.setNomeProduto("COQUETEL");
		p.setDescricaoNotaEnvio("Nota 34 yyyNota 15 oooooo");
		produtosEmissao.add(p);
		
		p = new ProdutoEmissaoDTO();
		p.setIdProdutoEdicao(4L);
		p.setCodigoProduto("0004");
		p.setNomeProduto("COQUETEL");
		p.setDescricaoNotaEnvio("Nota 34 yyyNota 15 oooooo");
		p.setProdutoDuplicadoDetalheNota(true);
		produtosEmissao.add(p);
		
		return produtosEmissao;
		
	}
	
	
	
	
	
	@Test
	public void test_gerar_mapa_produto_edicao_notas_emitidas() {
		
		ChamadaEncalheServiceImpl service = new ChamadaEncalheServiceImpl();
		
		Map<Long, List<NotaEnvioProdutoEdicao>> mpaNotaPE = service.gerarMapaProdutoEdicaoNotasEmitidas(obterListaNotaEnvioProdutoEdicao());
		
		Assert.assertTrue(mpaNotaPE.containsKey(1L));
		Assert.assertTrue(mpaNotaPE.containsKey(2L));
		Assert.assertTrue(mpaNotaPE.containsKey(3L));
		Assert.assertTrue(mpaNotaPE.containsKey(4L));

		Assert.assertTrue(mpaNotaPE.get(1L).size()==1);
		Assert.assertTrue(mpaNotaPE.get(2L).size()==2);
		Assert.assertTrue(mpaNotaPE.get(3L).size()==1);
		Assert.assertTrue(mpaNotaPE.get(4L).size()==1);
		
		
	}

	
	@Test
	public void test_carregar_dados_nota_envio_produto_edicao() {
		
		ChamadaEncalheServiceImpl service = new ChamadaEncalheServiceImpl();
		
		List<ProdutoEmissaoDTO> produtos = obterListaProdutoEmissaoCE(false);
		
		Map<Long, List<NotaEnvioProdutoEdicao>> mapa = obterMapaNEPE();
		
		service.carregarDadosNotaEnvioProdutoEdicao(mapa, produtos);
		
		for(ProdutoEmissaoDTO produto : produtos) {
			
			System.out.println(produto.getNotaEnvio());
			System.out.println(produto.getDescricaoNotaEnvio());
			System.out.println("\n");
		}
		
		Assert.assertTrue(produtos.size()==4);
		
		
	}

	@Test
	public void test_adicionar_linhas_produto_com_informacoes_nota_envio() {
		
		ChamadaEncalheServiceImpl service = new ChamadaEncalheServiceImpl();
		
		List<ProdutoEmissaoDTO> produtos = obterListaProdutoEmissaoCE(true);
		
		Assert.assertTrue(produtos.size() == 4);
		
		Assert.assertTrue(produtos.get(0).getIdProdutoEdicao().equals(1L));
		Assert.assertTrue(produtos.get(1).getIdProdutoEdicao().equals(2L));
		Assert.assertTrue(produtos.get(2).getIdProdutoEdicao().equals(3L));
		Assert.assertTrue(produtos.get(3).getIdProdutoEdicao().equals(4L));
		
		service.adicionarLinhasProdutoComInformacoesNotaEnvio(produtos);
		
		Assert.assertTrue(produtos.size() == 6);
		

		Assert.assertTrue(produtos.get(0).getIdProdutoEdicao().equals(1L));
		
		Assert.assertTrue(produtos.get(1).getIdProdutoEdicao().equals(2L));
		
		//VALIDANDO QUE O PRODUTO EDICAO 2 FOI DUPLICADO SENDO 
		//QUE SEGUNDO OBJETO POSSUI DETALHES DA NOTA
		Assert.assertTrue(produtos.get(2).getIdProdutoEdicao().equals(2L));
		Assert.assertTrue(produtos.get(2).isProdutoDuplicadoDetalheNota());
		
		Assert.assertTrue(produtos.get(3).getIdProdutoEdicao().equals(3L));
		
		Assert.assertTrue(produtos.get(4).getIdProdutoEdicao().equals(4L));

		//VALIDANDO QUE O PRODUTO EDICAO 4 FOI DUPLICADO SENDO 
		//QUE SEGUNDO OBJETO POSSUI DETALHES DA NOTA
		Assert.assertTrue(produtos.get(5).getIdProdutoEdicao().equals(4L));
		Assert.assertTrue(produtos.get(5).isProdutoDuplicadoDetalheNota());
		Assert.assertTrue(!produtos.get(5).getDescricaoNotaEnvio().isEmpty());
		
	}

	@Test
	public void test_remover_produto_emissao_detalhe_nota(){
		
		ChamadaEncalheServiceImpl service = new ChamadaEncalheServiceImpl();
		
		
		List<ProdutoEmissaoDTO> produtos = obterListaProdutoEmissaoCEComDetalheNotaDuplicado();		
		
		Assert.assertTrue(produtos.size()==6);
		
		service.removerProdutoEmissaoDetalheNota(produtos);
		
		Assert.assertTrue(produtos.size()==4);
		
		
		
	}
	
}
