package br.com.abril.nds.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

public class HTMLTableUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HTMLTableUtil.class);


    public static String estudoToHTML(EstudoTransient estudo) {
	
	StringBuilder h = new StringBuilder();
	h.append("<html>");
	h.append("<body>");
	h.append("<table border='1'>");
	
	h.append("<tr><td>Numero Cota</td>");
	h.append("<td>Cota Nova</td>");
	h.append("<td>Qtde PDV</td>");
	h.append("<td>Soma Rep Base</td>");
	h.append("<td>Soma Venda Base</td>");
	h.append("<td>Qtde Ed. Base</td>");
	for (int i = 0; i < 6; i++) {
	    if (i < estudo.getEdicoesBase().size()) {
		h.append(String.format("<td>Rep%s %s</td>", i, estudo.getEdicoesBase().get(i).getNumeroEdicao()));
		h.append(String.format("<td>Venda%s %s</td>", i, estudo.getEdicoesBase().get(i).getNumeroEdicao()));
		h.append(String.format("<td>Peso%s %s</td>", i, estudo.getEdicoesBase().get(i).getNumeroEdicao()));
	    } else {
		h.append(String.format("<td>Rep%s</td>", i));
		h.append(String.format("<td>Venda%s</td>", i));
		h.append(String.format("<td>Peso%s</td>", i));
	    }
	}
	for (int i = 0; i < 6; i++) {
	    h.append(String.format("<td>Venda Corrigida %s</td>", i));
	}
	h.append("<td>Menor Venda</td>");
	h.append("<td>Peso Menor Venda</td>");
	h.append("<td>Venda Media Nominal</td>");
	h.append("<td>Venda Media Corrigida</td>");
	h.append("<td>Venda Crescente (Ed 1 / Ed 0)</td>");
	h.append("<td>Venda Crescente (Ed 2 / Ed 1)</td>");
	h.append("<td>Venda Crescente (Ed 3 / Ed 2)</td>");
	h.append("<td>Venda Crescente (Ed 4 / Ed 3)</td>");
	h.append("<td>Venda Crescente (Ed 5 / Ed 4)</td>");
	h.append("<td>Indice Correcao Tendencia</td>");
	h.append("<td>Indice Ajuste Cota</td>");
	h.append("<td>Indice Tratamento Regional</td>");
	h.append("<td>Indice Venda Crescente</td>");
	h.append("<td>Venda Media Final</td>");
	h.append("<td>Reparte Minimo</td>");
	h.append("<td>Reparte Minimo Final</td>");
	h.append("<td>Reparte Final</td>");
	h.append("<td>Classificacao</td>");
	h.append("</tr>");

	for (CotaEstudo ce : estudo.getCotas()) {
	    if (ce.getReparteCalculado() != null && ce.getReparteCalculado().compareTo(BigInteger.ZERO) > 0) {
		h.append("	<tr>");
		h.append(String.format(" <td>%s</td>", ce.getNumeroCota()));
		h.append(String.format(" <td>%s</td>", ce.isNova()));
		h.append(String.format(" <td>%s</td>", ce.getQuantidadePDVs()));
		BigInteger reparte = BigInteger.ZERO;
		BigInteger venda = BigInteger.ZERO;
		if (ce.getEdicoesRecebidas() != null) {
		    for (ProdutoEdicaoEstudo pr : ce.getEdicoesRecebidas()) {
			reparte = reparte.add(pr.getReparte().setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
			venda = venda.add(pr.getVenda().setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger());
		    }
		}
		h.append(String.format(" <td>%s</td>", reparte));
		h.append(String.format(" <td>%s</td>", venda));
		h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().size()));
		for (int i = 0; i < 6; i++) {
		    if (i < ce.getEdicoesRecebidas().size()) {
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getReparte()));
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getVenda()));
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getIndicePeso()));
		    } else {
			h.append(" <td></td>");
			h.append(" <td></td>");
			h.append(" <td></td>");    
		    }
		}
		for (int i = 0; i < 6; i++) {
		    if (i < ce.getEdicoesRecebidas().size()) {
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getVendaCorrigida()));
		    } else {
			h.append(" <td></td>");
		    }
		}
		h.append(String.format(" <td>%s</td>", ce.getMenorVenda()));
		h.append(String.format(" <td>%s</td>", ce.getPesoMenorVenda()));
		h.append(String.format(" <td>%s</td>", ce.getVendaMediaNominal()));
		h.append(String.format(" <td>%s</td>", ce.getVendaMediaCorrigida()));

		for (int i = 0; i < 5; i++) {
		    if (i < ce.getEdicoesRecebidas().size()) {
			if (ce.getEdicoesRecebidas().get(i).getDivisaoVendaCrescente() == null) {
			    h.append(" <td></td>");
			} else {
			    h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getDivisaoVendaCrescente()));
			}
		    } else {
			h.append(" <td></td>");
		    }
		}
		h.append(String.format(" <td>%s</td>", ce.getIndiceCorrecaoTendencia()));
		if (ce.getVendaMediaMaisN() != null) {
		    h.append(String.format(" <td>%s</td>", ce.getVendaMediaMaisN()));
		} else if (ce.getPercentualEncalheMaximo() != null) {
		    h.append(String.format(" <td>%s</td>", ce.getPercentualEncalheMaximo()));
		} else {
		    h.append(String.format(" <td>%s</td>", ce.getIndiceAjusteCota()));
		}
		h.append(String.format(" <td>%s</td>", ce.getIndiceTratamentoRegional()));
		h.append(String.format(" <td>%s</td>", ce.getIndiceVendaCrescente()));
		h.append(String.format(" <td>%s</td>", ce.getVendaMedia()));
		h.append(String.format(" <td>%s</td>", ce.getReparteMinimo()));
		h.append(String.format(" <td>%s</td>", ce.getReparteMinimoFinal()));
		h.append(String.format(" <td>%s</td>", ce.getReparteCalculado()));
		h.append(String.format(" <td>%s</td>", ce.getClassificacao()));
		h.append("	</tr>");
	    }
	}
	h.append("</table> <br/> <br/> <br/>");
	h.append("<table border='1'>");
	h.append("<tr>");
	h.append("<td>Numero Cota</td>");
	h.append("<td>Classificacao</td>");
	h.append("</tr>");
	for (CotaEstudo ce : estudo.getCotas()) {
	    if (ce.getReparteCalculado() == null || ce.getReparteCalculado().compareTo(BigInteger.ZERO) == 0) {
		h.append("<tr>");
		h.append(String.format("<td>%s</td>", ce.getNumeroCota()));
		h.append(String.format("<td>%s</td>", ce.getClassificacao()));
		h.append("</tr>");
	    }
	}
	h.append("</table>");
	
	int qtdeCotasAtivas = 0;
	int qtdeCotasSuspensas = 0;
	int qtdeCotasComReparte = 0;
	int qtdeCotasSemReparte = 0;
	int qtdeCotasComplementares = 0;
	BigInteger totalFixacao = BigInteger.ZERO;
	BigDecimal vendaMediaTotal = BigDecimal.ZERO;
	for (CotaEstudo ce : estudo.getCotas()) {
	    if (ce.getSituacaoCadastro() != null) {
		if (ce.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO)) {
		    qtdeCotasAtivas++;
		}
		if (ce.getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {
		    qtdeCotasSuspensas++;
		}
		if (ce.getClassificacao().equals(ClassificacaoCota.BancaEstudoComplementar)) {
		    qtdeCotasComplementares++;
		}
	    }
	    if (ce.getReparteCalculado() != null && ce.getReparteCalculado().compareTo(BigInteger.ZERO) > 0) {
		qtdeCotasComReparte++;
	    } else {
		qtdeCotasSemReparte++;
	    }
	    if (ce.getReparteFixado() != null) {
		totalFixacao = totalFixacao.add(ce.getReparteFixado());
	    }
	    vendaMediaTotal = vendaMediaTotal.add(ce.getVendaMedia());
	}
	
	h.append("<table border='1'>");
	
	h.append("<tr>");
	h.append("<td>Total</td>");
	h.append("<td>Valor</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Sobra</td>");
	h.append("<td>").append(estudo.getReparteDistribuir()).append("</td>");
	h.append("</tr>");

	h.append("<tr>");
        h.append("<td>Total Venda Média</td>");
	h.append("<td>").append(vendaMediaTotal).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Total Reparte Complementar</td>");
	h.append("<td>").append(estudo.getReparteComplementarInicial()).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Total Reparte Ajuste</td>");
	h.append("<td>").append(estudo.getReservaAjusteInicial()).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>% Excedente</td>");
	h.append("<td>").append(estudo.getPercentualExcedente()).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Excedente</td>");
	h.append("<td>").append(estudo.getExcedente()).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Indice Redutor (Menor Venda)</td>");
	h.append("<td>").append(estudo.getMenorVenda()).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Total Fixacao</td>");
	h.append("<td>").append(totalFixacao).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Total Reparte A Distribuir Inicial</td>");
	h.append("<td>").append(estudo.getReparteDistribuirInicial()).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Qtde Cotas Ativas</td>");
	h.append("<td>").append(qtdeCotasAtivas).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Qtde Cotas Suspensas</td>");
	h.append("<td>").append(qtdeCotasSuspensas).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Qtde Cotas Complementares</td>");
	h.append("<td>").append(qtdeCotasComplementares).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Qtde Cotas Com Reparte</td>");
	h.append("<td>").append(qtdeCotasComReparte).append("</td>");
	h.append("</tr>");
	
	h.append("<tr>");
	h.append("<td>Qtde Cotas Sem Reparte</td>");
	h.append("<td>").append(qtdeCotasSemReparte).append("</td>");
	h.append("</tr>");
	
	h.append("</table>");
	h.append("</body>");
	h.append("</html>");

	return h.toString();
	
//	StringBuilder sb = new StringBuilder();
//	sb.append(HTMLTableUtil.buildHTMLTable(estudoAutomatico));
//	sb.append("<br>");
//	sb.append(HTMLTableUtil.buildHTMLTable(estudoAutomatico.getEdicoesBase()));
//	sb.append("<br>");
//	sb.append(HTMLTableUtil.buildHTMLTable(estudoAutomatico.getCotas()));
//	return sb.toString();
    }
    
	@SuppressWarnings("unchecked")
	public static <T> String buildHTMLTable(T type) {
		StringBuffer sb = new StringBuffer("<table border='1' cellspacing='0' cellpadding='2'>");
		List<T> listObjects = new ArrayList<>();

		if (type instanceof List) {
			listObjects.addAll((List<? extends T>) type);
		} else {
			listObjects.add(type);
		}

		Class<? extends Object> clazz = listObjects.get(0).getClass();
		Method[] methods = clazz.getMethods();
		Map<String, Method> sortedMethods = new TreeMap<String, Method>();
		for (Method method : methods) {
			String name = method.getName();
			if ((name.startsWith("get") || name.startsWith("is"))
					&& !name.equalsIgnoreCase("getClass") && !name.equalsIgnoreCase("getCotas")) {
				sortedMethods.put(name.replaceFirst("is|get", ""), method);
			}
		}

		Map<String, Method> finalOrderMethods = new LinkedHashMap<String, Method>();
		if (sortedMethods.containsKey("Id")) {
			finalOrderMethods.put("Id", sortedMethods.remove("Id"));
		}
		if (sortedMethods.containsKey("Numero")) {
			finalOrderMethods.put("Numero", sortedMethods.remove("Numero"));
		}
		if (sortedMethods.containsKey("NumeroCota")) {
			finalOrderMethods.put("NumeroCota", sortedMethods.remove("NumeroCota"));
		}
		if (sortedMethods.containsKey("NumeroEdicao")) {
			finalOrderMethods.put("NumeroEdicao", sortedMethods.remove("NumeroEdicao"));
		}
		finalOrderMethods.putAll(sortedMethods);
		
		Set<Entry<String, Method>> headers = finalOrderMethods.entrySet();
		buildHTMLTableHeader(headers, sb);

		for (T rowObject : listObjects) {
			buildHTMLTableRow(headers, rowObject, sb);
		}
		
		sb.append("</table>");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static <T, K, V> void buildHTMLTableRow(Set<Entry<String, Method>> headers, T rowObject, StringBuffer sb) {
		sb.append("<tr>");
		for (Entry<String, Method> entry : headers) {
			sb.append("<td>");
			Object rowValue = null;
			try {
				rowValue = entry.getValue().invoke(rowObject);
				if (rowValue instanceof Map) {
					Map<K, V> tempMap = new HashMap<>();
					tempMap.putAll((Map<? extends K, ? extends V>) rowValue);
					rowValue = tempMap.values();
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.error(e.getMessage(), e);
			}
			sb.append(String.valueOf(rowValue));
			sb.append("</td>");
		}
		sb.append("</tr>");
	}

	private static void buildHTMLTableHeader(Set<Entry<String, Method>> headers, StringBuffer sb) {
		sb.append("<tr>");
		for (Entry<String, Method> entry : headers) {
			sb.append("<th>");
			sb.append(entry.getKey());
			sb.append("</th>");
		}		
		sb.append("</tr>");
	}
}
