package br.com.abril.nds.process.calculoreparte;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;

public class ReparteComplementarPorCotaTest {

	ReparteComplementarPorCota reparteComplementarPorCota;
	Estudo estudo;
	
	@Before
	public void setUp() throws Exception {
		
		
		estudo = new Estudo();
		estudo.setCotas(new ArrayList<Cota>());
		
		
		ProdutoEdicaoBase peb = new ProdutoEdicaoBase();
		peb.setId(91956l);
		estudo.setProduto(peb);
		estudo.setEdicoesBase(new ArrayList<ProdutoEdicaoBase>());
	
		ProdutoEdicaoBase p = new ProdutoEdicaoBase();
		p.setIdProduto(1l);
		p.setId(1l);
		estudo.getEdicoesBase().add(p);
		estudo.setReparteDistribuir(BigDecimal.ONE);
		
		//cota tipo A)
		Cota cota = new Cota();
		cota.setId(348l);
		cota.setClassificacao(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga);
		cota.setCotaSoRecebeuEdicaoAberta(true);
		cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicao>());
		
		peb = new ProdutoEdicao();
		peb.setEdicaoAberta(true);
		peb.setIdProduto(1l);
		
		cota.getEdicoesRecebidas().add((ProdutoEdicao)peb);
		cota.setReparteMinimo(BigDecimal.ONE);
		
		estudo.setReparteComplementar(new BigDecimal(5));
		estudo.getCotas().add(cota);
		
		
		//cota para B)
		cota = new Cota();
		peb.setEdicaoAberta(true);
		peb.setIdProduto(2l);
		cota.setClassificacao(ClassificacaoCota.BancaComReparteZeroMinimoZeroCotaAntiga);
		cota.setReparteMinimo(BigDecimal.ONE);
		estudo.getCotas().add(cota);
		
		this.reparteComplementarPorCota = new ReparteComplementarPorCota(estudo);
		
//		cota para C)
		for (int i = 1; i < 4; i++) {
			cota = new Cota();
			Long id = new Long(Integer.toString(i));
			cota.setId(id);
			this.reparteComplementarPorCota.getCotasIdList().add(id);
			cota.setReparteMinimo(BigDecimal.ONE);
			peb.setEdicaoAberta(false);
			peb.setIdProduto(1l);
			cota.setEdicoesRecebidas(new ArrayList<ProdutoEdicao>());
			cota.setClassificacao(ClassificacaoCota.BancaComTotalVendaZeraMinimoZeroCotaAntiga);

			for (int j = 0; j < i; j++) {
				ProdutoEdicao pe = new ProdutoEdicao();
				pe.setId(1l);
				cota.getEdicoesRecebidas().add(pe);
			}
			
			
			estudo.getCotas().add(cota);
		}
		
		
	}

	@Test
	public void test() {
		try {
			this.reparteComplementarPorCota.executar();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Erro ao executar o teste.");
		}
	}

	
	
}
