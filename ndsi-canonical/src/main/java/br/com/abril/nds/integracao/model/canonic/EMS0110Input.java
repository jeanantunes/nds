package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;

public class EMS0110Input extends IntegracaoDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codDistrib;
	private String dataGeracaoArq;
	private String horaGeracaoArq;
	private String mnemonicoTab;
	private String contextoProd;
	private String codForncProd;
	private String codProd;  			//Ok
	private Long edicaoProd; 			//Ok
	private String nomeProd; 			//Ok
	private String codBarra;			//Ok
	private BigDecimal pesoUni; 		//Ok
	private String tipoProd;
	private int peb;					//Ok
 	private String largura;
	private String comprimento;
	private String expessura;
	private String codSitTributaria;
	private String codSitFiscal;
	private String pactPadrao;
	private String tipoMaterialPromocional;
	private String tipoMaterialDivulgacao;
	private String tipoMaterialTroca;
	private String valorValeDesconto;
	private String valorMaterialTroca;
	private boolean contemBrinde;		//Ok
	private String codNBM;
	private String descBrinde;
	private String condVendeSeparado;
	private String statusProd;
	private String dataDesativacao;
	private String chamadaCapa;
	private String edicao;
	private String regimeRecolhimento;
	private String segmentacaoClasseSocial;
	private String segmentacaoPeriodicidade;
	private String segmentacaoFormaFiscal;
	private String segmentacaoSexo;
	private String segmentacaoIdade;
	private String segmentacaoLancamento;
	private String segmentacaoTemaPrincipal;
	private String segmentacaoTemaSecundario;
	private String codCategoria;
	private String contextoProdReferencia;
	private String codFornecProdReferencia;
	private String codProdReferencia;
	private BigDecimal tipoDesconto;	//Ok
	private String contextoEditor;
	private String codEditor;
	private String contextoPublicacao;
	private String codFornecPublicacao;
	private String codColecao;
	private String formaInclusao;
	private String codPublicacao;
	
}
