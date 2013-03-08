package br.com.abril.nds.process.ajustecota;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.TipoAjusteReparte;
import br.com.abril.nds.model.TipoSegmentoProduto;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.jornaleirosnovos.JornaleirosNovos;

/**
 * Processo que tem como objetivo efetuar o calculo da divisao do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- N/A
 * 
 * Processo Anterior: {@link Bonificacoes}
 * Próximo Processo: {@link JornaleirosNovos}
 * </p>
 */
public class AjusteCota extends ProcessoAbstrato {

    public AjusteCota(Cota cota) {
	super(cota);
    }

    @Override
    protected void executarProcesso() {

	BigDecimal indiceAjusteCota = BigDecimal.ONE;

	Cota cota = (Cota) super.genericDTO;

	BigDecimal ajusteAplicado = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, null, new TipoAjusteReparte[] {
		TipoAjusteReparte.AJUSTE_ENCALHE_MAX, TipoAjusteReparte.AJUSTE_HISTORICO, TipoAjusteReparte.AJUSTE_VENDA_MEDIA });

	if (ajusteAplicado != null && ajusteAplicado.compareTo(indiceAjusteCota) == 1)
	    indiceAjusteCota = ajusteAplicado;

	List<ProdutoEdicao> listProdutoEdicao = cota.getEdicoesRecebidas();

	if (listProdutoEdicao != null && !listProdutoEdicao.isEmpty()) {

	    // O Tipo de Segmento é por produto(publicação) e o produto tem várias edições.
	    // Assim, a lista de edição do produto deve vir com o mesmo tipo de segmento e o mesmo produto!
	    TipoSegmentoProduto tipoSegmentoProduto = listProdutoEdicao.iterator().next().getTipoSegmentoProduto();

	    BigDecimal ajusteAplicadoSegmento = new CotaDAO().getAjusteAplicadoByCotaTipoSegmentoFormaAjuste(cota, tipoSegmentoProduto,
		    new TipoAjusteReparte[] { TipoAjusteReparte.AJUSTE_SEGMENTO });

	    if (ajusteAplicadoSegmento != null && ajusteAplicadoSegmento.compareTo(indiceAjusteCota) == 1) {
		indiceAjusteCota = ajusteAplicadoSegmento;
	    }
	}

	cota.setIndiceAjusteCota(indiceAjusteCota);

    }

}
