package br.com.abril.ndsled.actions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.ndsled.exceptions.CarregarCotaLedException;
import br.com.abril.ndsled.exceptions.CarregarLancamentoException;
import br.com.abril.ndsled.modelo.Cota;
import br.com.abril.ndsled.modelo.Lancamento;
import br.com.abril.ndsled.modelo.Produto;

public class AppActions {

	private AppActions() {

	}

	public static List<Lancamento> carregarLancamento(Date date)
			throws CarregarLancamentoException {
		// Aqui vai ter uma chamada para o metodo que vai fazer a comunicacao
		// com o couch.

		List<Lancamento> lancamentos = new ArrayList<Lancamento>();

		Lancamento lancamento = new Lancamento();
		lancamento.setCodigoCota(1);
		lancamento.setCodigoProduto(552);
		lancamento.setDataLacamento(new java.sql.Date(2016, 04, 01));
		lancamento.setDesconto(new BigDecimal(0));
		lancamento.setEdicaoProduto(1);
		lancamento.setNomeProduto("Veja");
		lancamento.setPrecoCapa(new BigDecimal(14.99));
		lancamento.setPrecoCusto(new BigDecimal(14.99));
		lancamento.setQuantidadeReparte(10);
		lancamento.setCodigoLed(2);
		lancamentos.add(lancamento);

		lancamento = new Lancamento();
		lancamento.setCodigoCota(2);
		lancamento.setCodigoProduto(552);
		lancamento.setDataLacamento(new java.sql.Date(2016, 04, 01));
		lancamento.setDesconto(new BigDecimal(0));
		lancamento.setEdicaoProduto(1);
		lancamento.setNomeProduto("Veja");
		lancamento.setPrecoCapa(new BigDecimal(14.99));
		lancamento.setPrecoCusto(new BigDecimal(14.99));
		lancamento.setQuantidadeReparte(7);
		lancamento.setCodigoLed(49);
		lancamentos.add(lancamento);

		lancamento = new Lancamento();
		lancamento.setCodigoCota(1);
		lancamento.setCodigoProduto(111222);
		lancamento.setDataLacamento(new java.sql.Date(2016, 04, 01));
		lancamento.setDesconto(new BigDecimal(0));
		lancamento.setEdicaoProduto(1);
		lancamento.setNomeProduto("Avengers");
		lancamento.setPrecoCapa(new BigDecimal(14.99));
		lancamento.setPrecoCusto(new BigDecimal(14.99));
		lancamento.setQuantidadeReparte(25);
		lancamento.setCodigoLed(2);
		lancamentos.add(lancamento);

		lancamento = new Lancamento();
		lancamento.setCodigoCota(2);
		lancamento.setCodigoProduto(111222);
		lancamento.setDataLacamento(new java.sql.Date(2016, 04, 01));
		lancamento.setDesconto(new BigDecimal(0));
		lancamento.setEdicaoProduto(1);
		lancamento.setNomeProduto("Avengers");
		lancamento.setPrecoCapa(new BigDecimal(4.99));
		lancamento.setPrecoCusto(new BigDecimal(4.99));
		lancamento.setQuantidadeReparte(50);
		lancamento.setCodigoLed(49);
		lancamentos.add(lancamento);

		return lancamentos;

	}

}
