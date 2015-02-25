 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <!-- distribuicaoVendaMedia -->
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/autoCompleteCampos.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/distribuicaoVendaMedia.js"></script>

<script language="javascript">

var distribuicaoVendaMedia = new DistribuicaoVendaMedia('${pageContext.request.contextPath}', this.workspace);

var autoComplete = new AutoCompleteCampos();


$(function() {
		$( "#tab-distribuicao" ).tabs();
		distribuicaoVendaMedia.confirmarProdutosEdicaoBasePopup();
		$('#componenteInformacoesComplementares').change(function(){
			distribuicaoVendaMedia.selectElementoRegiaoDistribuicao('componenteInformacoesComplementares', 'elementoInformacoesComplementares1');
			distribuicaoVendaMedia.selectElementoRegiaoDistribuicao('componenteInformacoesComplementares', 'elementoInformacoesComplementares2');
			distribuicaoVendaMedia.selectElementoRegiaoDistribuicao('componenteInformacoesComplementares', 'elementoInformacoesComplementares3');
		});
		$(".ui-tabs-selected").find("span[class*='ui-icon-close']").click(function() {
			distribuicaoVendaMedia.produtoEdicaoBases=[];
		});
		distribuicaoVendaMedia.carregarEventos();
		distribuicaoVendaMedia.verificarICD();
	});

function popup_novo() {
	$("#edicaoProdCadastradosGrid-1").flexAddData({
			rows : [],
			page : 1,
			total : 1
		});
	
	$("#codigoPesquisaBases").val("");
	$("#produtoPesquisaBases").val("");
	$("#edicaoPesquisaBases").val("");
	
	distribuicaoVendaMedia.produtoEdicaoPesquisaBases = [];
	
	$("#dialog-edicoes-base").dialog({
	    escondeHeader: false,
		resizable: false,
		height:450,
		width:700,
		modal: true,
		buttons: {
			"Confirmar": function() {
				distribuicaoVendaMedia.confirmarProdutosEdicaoBasePopup();
				$( this ).dialog( "close" );
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		},
	});
};
function popup_cancelar() {

	$( "#dialog-cancelar" ).dialog({
	    escondeHeader: false,
		resizable: false,
		height:'auto',
		width:380,
		modal: true,
		buttons: {
			"Confirmar": function() {
				$( this ).dialog( "close" );
				distribuicaoVendaMedia.cancelar();
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};

function popup_excluir_bonificacao(index) {
	$( "#dialog-excluir" ).dialog({
	    escondeHeader: false,
		resizable: false,
		height:'auto',
		width:380,
		modal: true,
		buttons: {
			"Confirmar": function() {
				distribuicaoVendaMedia.removerBonificacao(index);
				$( this ).dialog( "close" );
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
}
function popup_excluir() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$( "#dialog-excluir" ).dialog({
	    escondeHeader: false,
		resizable: false,
		height:'auto',
		width:380,
		modal: true,
		buttons: {
			"Confirmar": function() {
				distribuicaoVendaMedia.removerProdutoEdicaoDaBase();
				$( this ).dialog( "close" );
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};
function popup_excluir_todos() {
    $( "#dialog-excluir" ).dialog({
        escondeHeader: false,
	resizable: false,
	height:'auto',
	width:380,
	modal: true,
	buttons: {
		"Confirmar": function() {
			distribuicaoVendaMedia.removerTodasEdicoesDeBase();
			$( this ).dialog( "close" );
		},
		"Cancelar": function() {
			$( this ).dialog( "close" );
		}
	}
});  
};
function popup_pesqRegiao() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$( "#dialog-regiao" ).dialog({
	    escondeHeader: false,
		resizable: false,
		height:'auto',
		width:440,
		modal: true,
		buttons: {
			"Confirmar": function() {
				$( this ).dialog( "close" );
				mostra_regiao();
								
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};

/* function popup_detalhes() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-detalhes" ).dialog({
		    escondeHeader: false,
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false,
		});
	};*/
function popup_detalhes_close() {
	  $( "#dialog-detalhes" ).dialog( "close" );
	  
	  
	  } 
function nenhuma(){
	$('#selRegiao').hide();
	$('#qtdeBancas').hide();
	$('#rrGeral').hide();
	$('#rrSegmento').hide();
	$('#historico').hide();
	$('#lstComponentes').hide();
	$('#lstExcecao').hide();
	}
function mostraExcecao(){
	$('#selRegiao').hide();
	$('#qtdeBancas').hide();
	$('#rrGeral').hide();
	$('#rrSegmento').hide();
	$('#historico').hide();
	$('#lstComponentes').hide();
	$('#lstExcecao').show();
	}
function mostra_regiao(){
	$('#selRegiao').show();
	$('#qtdeBancas').hide();
	$('#rrGeral').hide();
	$('#rrSegmento').hide();
	$('#historico').hide();
	$('#lstComponentes').hide();
	$('#lstExcecao').hide();
	}
function mostra_qtdeBancas(){
	
	if(distribuicaoVendaMedia.produtoEdicaoBases != undefined && distribuicaoVendaMedia.produtoEdicaoBases.length === 0){
		exibirMensagemDialog('WARNING',  ['Não existe base para informar abrangência.']);
		$('#qtdeBancas').hide();
		$('#selRegiao').hide();
		$('#rrGeral').hide();
		$('#rrSegmento').hide();
		$('#historico').hide();
		$('#lstComponentes').hide();
		$('#lstExcecao').hide();
	}else{
		$('#selRegiao').hide();
		$('#qtdeBancas').show();
		$('#rrGeral').hide();
		$('#rrSegmento').hide();
		$('#historico').hide();
		$('#lstComponentes').hide();
		$('#lstExcecao').hide();
	}
	
	$("#RDabrangenciaCriterio").val('');
	$("#RDabrangencia").val(0);
	
	
}
	
function mostrarTodasCotas(){
	$('#selRegiao').hide();
	$('#qtdeBancas').hide();
	$('#rrGeral').hide();
	$('#rrSegmento').hide();
	$('#historico').hide();
	$('#lstComponentes').hide();
	$('#lstExcecao').hide();
}
function mostraComponentes(){
	$('#selRegiao').hide();
	$('#qtdeBancas').hide();
	$('#rrGeral').hide();
	$('#rrSegmento').hide();
	$('#historico').hide();
	$('#lstComponentes').show();
	$('#lstExcecao').hide();
	
	$("#componenteRegiaoDistribuicao, select[name=elementoRegiaoDistribuicao]").val("");
	}
function mostra_PercBancas(){
	$('#selRegiao').hide();
	$('#qtdeBancas').hide();
	$('#rrGeral').show();
	$('#rrSegmento').hide();
	$('#historico').hide();
	$('#lstComponentes').hide();
	$('#lstExcecao').hide();
	}

function mostra_redutor(){
	$('.redutorManual').show();
	}
function esconde_redutor(){
	$('.redutorManual').hide();
	}

function limparLstExcecao1(){

	if($("#lstExcecao1").is(":visible")){
		$("#componenteInformacoesComplementares,#elementoInformacoesComplementares1,#elementoInformacoesComplementares2,#elementoInformacoesComplementares3").val("");
	}
}

 var pesquisaProduto = new PesquisaProduto();

</script>

<div id="dialog-regiao" title="Pesquisar Região" style="display:none;">
    <fieldset style="width:400px!important;">
      <legend>Pesquisar Região</legend>
            <table width="380" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="52"><strong>Região:</strong></td>
                <td width="253"><input name="" type="text" style="width:250px;" /></td>
                <td width="55"><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
              </tr>
            </table>

    </fieldset>
 
    <fieldset style="width:400px!important; margin-top:5px;">
            <legend>Selecionar Regiões</legend>
            <table class="listaRegiaoGrid"></table>
    </fieldset>
</div>
<div id="dialog-excluir" title="Excluir Produto" style="display:none;">  
    <fieldset style="width:320px!important;">
            <legend>Excluir Produto</legend>
            <p>Confirma a exclusão destes Produtos?</p>
    </fieldset>
</div>

<div id="dialog-edicoesbase" title="Alerta" style="display:none;">  
    <fieldset style="width:320px!important;">
            <p>Existe apenas uma edição base. Deseja continuar?</p>
    </fieldset>
</div>

<!-- T.produtoEdicaoBases.length -->
<div id="dialog-detalhes" title="Visualizando Produto" style="margin-right:0px!important; float:right!important;">
	
	<img src="images/loading.gif" id="loadingCapa"/>
	<!-- <img src="capas/revista-nautica-11.jpg" width="235" height="314" id="imagemCapaEdicao" style="display:none"/>  -->
	<img  width="235" height="314" id="imagemCapaEdicao" style="display:none"/>
</div>

<div id="dialog-excluir" title="Excluir Produto" style="display:none;">  
    <fieldset style="width:320px!important;">
            <legend>Excluir Produto</legend>
            <p>Confirma a exclusão destes Produtos?</p>
    </fieldset>
</div>

<div id="dialog-cancelar" title="Cancelar Operação" style="display:none;">  
    <fieldset style="width:320px!important;">
            <legend>Cancelar Operação</legend>
            <p>Confirma o Cancelamento desta Operação?</p>
    </fieldset>
</div>

<div id="dialog-edicoes-base" title="Pesquisar Edições de Produto" style="display: none; margin-right: 0px; padding-right: 0px; width: 650px ">  
<fieldset style="width:560px!important;">
    <legend>Pesquisar Produto</legend>
        
      <table width="545" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="43">Código:</td>
            <td width="78">
            	<input type="text" name="textfield1" id="codigoPesquisaBases" style="width:60px;"
            	 	onchange="autoComplete.pesquisarPorCodigo('/produto/pesquisarPorCodigoProduto','#codigoPesquisaBases', '#produtoPesquisaBases');"/>
            </td>
            
            <td width="48">Produto:</td>
            <td width="184">
            	<input type="text" name="textfield2" id="produtoPesquisaBases" style="width:160px;" onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#produtoPesquisaBases', true);" 
                	 onblur="pesquisaProduto.pesquisarPorNomeProduto('#codigoPesquisaBases', '#produtoPesquisaBases', '#edicaoPesquisaBases', true);" /> 
            </td>
            
            <td width="42">Edição:</td>
            <td width="62">
            	<input type="text" name="textfield3" id="edicaoPesquisaBases" style="width:60px;" /></td>
          </tr>
            
          	<tr>
	          	<td width="76">Classifica&ccedil;&atilde;o:</td>
	            <td colspan="3">
		        <select name="filtro.idTipoClassificacaoProduto" id="distribuicao-venda-media-selectClassificacao" style="width:200px;">
			        <option value="-1" selected="selected">Selecione...</option>
			        <c:forEach items="${listaTipoClassificacao}" var="tipoClassificacao">
			            <option value="${tipoClassificacao.key}">${tipoClassificacao.value}</option>
			        </c:forEach>
		        </select>
		        </td>
	          	<td>
					<span class="bt_novos">
						<a href="javascript:distribuicaoVendaMedia.pesquisarBases();" rel="tipsy" title="Pesquisar">
							<img src="images/ico_pesquisar.png" hspace="5" border="0" />
						</a>
					</span>
	          	<!-- 
	          		<span class="bt_pesquisar">
	          			<a onclick="distribuicaoVendaMedia.pesquisarBases('${pageContext.request.contextPath}');">&nbsp;</a>
	          		</span>
	          	 -->
	          	</td>
          	</tr>
        </table>

    </fieldset>
    <br clear="all" />

    <fieldset style="width:655px!important; margin-top:10px;">
    	<legend>Edições do Produto</legend>
        <table class="edicaoProdCadastradosGrid-1" id="edicaoProdCadastradosGrid-1"></table>
     </fieldset>
</div>

<div>
  
  
    <br clear="all"/>
    <br />
   
    <div class="container">
    
      <fieldset style="width:650px!important; float:left;">
       	  <legend>Distribuição</legend>
        <div class="grids" style="display:block;">
        	<div id="tab-distribuicao">
                <ul>
                    <li><a href="#tab-distribuicao-1">Distribuição</a></li>
                    <li><a href="#tab-distribuicao-2">Bases</a></li>
                    <li><a href="#tab-distribuicao-3">Bonificação</a></li>
                    <li><a href="#tab-distribuicao-4">Região Distribuição</a></li>
                </ul>
                <div id="tab-distribuicao-1" class="distribuicaoVendaMedia-tab">
                	<fieldset style="width:280px!important; margin-bottom:10px; float:left;">
                    	<legend>Dados da Distribuição</legend>
                        <input type="hidden" id="idLancamento" value="${lancamento.id}"/>
                        <input type="hidden" id="idProdutoEdicao" value="${lancamento.produtoEdicao.id}"/>
                        <input type="hidden" id="numeroLancamentoDvm" value="${lancamento.numeroLancamento}"/>
                        <input type="hidden" id="modoAnalise" value="${modoAnalise}"/>
                    	<table width="280" border="0" cellspacing="2" cellpadding="2">
                          <tr>
                            <td>Produto:</td>
                            <td>${ produtoEdicao.nomeProduto }</td>
                          </tr>
                          <tr>
                            <td>Nome Comercial:</td>
                            <td>${ produtoEdicao.nomeComercial }</td>
                          </tr>
                          <tr>
                            <td width="110">Código</td>
                            <td id="codigoProduto">${ produtoEdicao.codigoProduto }</td>
                          </tr>
                          <tr>
                            <td>Edição:</td>
                            <td id="numeroEdicao">${ produtoEdicao.numeroEdicao }</td>
                          </tr>
                          <tr>
                            <td>Período:</td>
                            <td id='periodoDvm'>${ lancamento.periodoLancamentoParcial.numeroPeriodo }</td>
                          </tr>
                          <tr>
                            <td>Preço R$</td>
                            <td> <fmt:formatNumber type="currency" minFractionDigits="2" currencySymbol="" value="${ produtoEdicao.precoVenda }" /> </td>
                          </tr>
                          <tr>
                            <td>Pacote Padrão:</td>
                            <td id='pctPadrao'>${ produtoEdicao.pacotePadrao }</td>
                          </tr>
                          <tr>
                            <td>Data Lançamento:</td>
                            <td id="dist-venda-media-dataLancamento">${ produtoEdicao.dataLancamentoFormatada }</td>
                          </tr>
                          <tr>
                            <td>Data Recolhimento:</td>
                            <td>${ produtoEdicao.dataRecolhimentoDistribuidorFormatada }</td>
                          </tr>
                          <tr>
                            <td>Segmento:</td>
                            <td>${ produtoEdicao.segmentacao }</td>
                          </tr>
                          <tr>
                            <td>Classificação</td>
                            <td width="156">${ produtoEdicao.classificacao }</td>
                          </tr>
                        </table>
                        
                    </fieldset>
                  <fieldset style="width:270px!important; margin-bottom:10px; margin-left:10px; float:left;">
               	    <legend>Estudo Nº: <span id="idEstudo">${idEstudo}</span></legend>
                   	  <table width="270" border="0" cellspacing="2" cellpadding="2">
                        <tr class="class_linha_1">
                          <td width="99"><strong>Status do Estudo:</strong></td>
                          <td width="157"><strong id="idStatusEstudo">${statusEstudo}</strong></td>
                        </tr>
                      </table>
                   	  <table width="275" border="0" cellspacing="2" cellpadding="2">
                   	    <tr>
                   	      <td>Lançamento:</td>
                   	      <td colspan="2" id="reparteTotal">${ lancado }</td>
               	        </tr>
                   	      <td>Sobra:</td>
                   	      <td colspan="2" id="sobra">${sobra}</td>
               	        </tr>
                   	    <tr>
                   	      <td>Reparte Distribuir:</td>
                   	      <td colspan="2"><input type="text" name="textfield6" id="reparteDistribuir" style="width:40px; text-align:center;" value="${repDistrib}" /></td>
               	        </tr>
                   	    <tr>
                   	      <td>Reparte Mínimo:</td>
                   	      <td colspan="2"><input type="text" name="textfield4" id="reparteMinimo" style="width:40px; text-align:center;"  /></td>
               	        </tr>
                   	    <tr>
                   	      <td align="right"><input name="checkbox2" type="checkbox" id="usarFixacao" checked="checked" /></td>
                   	      <td colspan="2">Usar Fixação</td>
               	        </tr>
               	        <tr>
                   	      <td align="right"><input name="checkbox2" type="checkbox" id="usarMix" checked="checked" /></td>
                   	      <td colspan="2">Usar MIX (Rep. Min e Rep. Max)</td>
               	        </tr>
                   	    <tr>
                   	      <td align="right"><input type="checkbox" name="checkbox3" id="distribuicaoPorMultiplo" onclick="$('.distrMult').toggle(); $('#multiplo').val($('#pctPadrao').text());" /></td>
                   	      <td width="120">Distribuição por multiplo</td>
                   	      <td width="44"><input type="text" class="distrMult" style="width:40px; text-align:center; display:none;" id="multiplo" value="00" /></td>
               	        </tr>
               	    </table>
                  </fieldset>
                    <br clear="all" />
                     <span class="bt_novos"><a href="javascript:;" onclick="distribuicaoVendaMedia.cancelar()"><img src="images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />Voltar</a></span>
                     <span class="bt_novos"><a href="javascript:;" onclick="distribuicaoVendaMedia.verificacoesGerar()"><img src="images/ico_check.gif" alt="Gerar" hspace="5" border="0" />Gerar</a></span>
                     <span class="bt_novos"><a href="javascript:;" onclick="distribuicaoVendaMedia.redirectToTelaAnalise()"><img src="images/ico_copia_distrib.gif" alt="Analisar" hspace="5" border="0" />Analisar</a></span>
                </div>
                <div id="tab-distribuicao-2" class="distribuicaoVendaMedia-tab">
               	  <fieldset style="width:610px!important;">
                   	<legend>Bases</legend>
                   	  <table width="610" border="0" cellspacing="2" cellpadding="2">
                   	    <tr>
                   	      <td width="36">Código:</td>
                   	      <td width="56">${ produtoEdicao.codigoProduto }</td>
                   	      <td width="41">Produto:</td>
                   	      <td width="226">${ produtoEdicao.nomeProduto }</td>
                          <td>Classif.:</td>
                          <td width="156">${ produtoEdicao.classificacao }</td>
                   	      <td width="35">Edição:</td>
                   	      <td width="40">${ produtoEdicao.numeroEdicao }</td>
                   	      <td width="35">Período:</td>
                   	      <td width="30">${ lancamento.periodoLancamentoParcial.numeroPeriodo }</td>
                   	      <td width="79"><span class="bt_novos"><a href="javascript:;" onclick="popup_novo();" style="margin-right: 1px;"><img src="images/ico_salvar.gif" alt="Liberar" hspace="5" border="0" />Novo</a></span></td>
               	        </tr>
               	    </table>
                   	</fieldset>
                    <fieldset style="width:610px!important; margin-top:10px;">
                    	<legend>Produtos</legend>
                       <table class="dadosBasesGrid"></table>


                    </fieldset>
                    <br clear="all" />
	                <span class="bt_novos">
	                	<a href="javascript:;" onclick="popup_excluir_todos();">
	                		<img src="images/ico_excluir.gif" hspace="5" alt="Excluir Todos" border="0" />Excluir Todos
	                	</a>
	                	<a href="javascript:;" onclick="popup_excluir();">
	                		<img src="images/ico_excluir.gif" hspace="5" alt="Excluir" border="0" />Excluir
	                	</a>
	                </span>
                
                </div>
                
                <div id="tab-distribuicao-3" class="distribuicaoVendaMedia-tab">
                <fieldset style="width:600px!important; margin-bottom:10px;">
                   	<legend>Produto</legend>
                   	<table width="600" border="0" cellspacing="2" cellpadding="2">
                   	  <tr>
                   	    <td width="36">Código:</td>
                   	    <td width="60">${ produtoEdicao.codigoProduto }</td>
                   	    <td width="42">Produto:</td>
                   	    <td width="235">${ produtoEdicao.nomeComercial }</td>
                   	    <td>Classificação:</td>
                        <td width="156">${ produtoEdicao.classificacao }</td>
                   	    <td width="35">Edição:</td>
                   	    <td width="66">${ produtoEdicao.numeroEdicao }</td>
                   	    <td width="69">&nbsp;</td>
               	      </tr>
               	  </table>
                </fieldset>
                <br clear="all" />

                <fieldset style="width:600px!important; margin-bottom:10px;">
    <legend>Bonificação</legend>
    <table width="594" border="0" cellspacing="2" cellpadding="2">
        <tr class="header_table">
            <td>Componentes</td>
            <td style="width:10px;">&nbsp;</td>
            <td>Elementos</td>
            </tr>
    	<tr>
            <td width="281" valign="top">
                <table class="lstComponentesGrid">
                	<c:forEach items="${ componentes }" var="componente" varStatus="idx">
	                	<tr >
	                		<td onclick="distribuicaoVendaMedia.selectComponenteBonificacao(${ idx.count - 1 }, '${ componente.descricao }', '${ componente }')">${ componente.descricao }</td>
	                		<td><input type="checkbox" id="componenteBonificacao${ idx.count - 1 }" onclick="if($(this).is(':checked')){distribuicaoVendaMedia.loadBonificacoesGrid(${ idx.count - 1 },'${ componente.descricao }', '${ componente }');}else{distribuicaoVendaMedia.unloadBonificacoesGrid();}" /></td>
	                	</tr>
                	</c:forEach>
                </table>
            </td>
            <td width="12" style="width:10px;">&nbsp;</td>
            <td width="281" valign="top">
              <table class="bonificacoesGrid" id="bonificacoesGrid"></table>
            </td>
            </tr>
    </table>
    <span class="bt_novos" style="float:right!important; margin-right:20px;">
    	<a href="javascript:;" onclick="distribuicaoVendaMedia.confirmarSelecaoBonificacao();">
    		<img src="images/ico_add.gif" alt="Confirmar Seleção" hspace="5" border="0" />
    			Confirmar Seleção
    	</a>
    </span>
    <br />

    <table width="590" border="0" cellspacing="2" cellpadding="2">
        <tr>
          <td width="672" valign="top">
            <table class="elemento1Grid" id="elemento1Grid"></table>
            </td>
        </tr>
    </table>                 
<br clear="all" />
</fieldset>
                <br clear="all" />

                </div>
                
                <div id="tab-distribuicao-4" class="distribuicaoVendaMedia-tab">
                <fieldset style="width:600px!important; margin-bottom:10px;">
               	  <legend>Região Distribuição</legend>
                   	<table width="587" border="0" cellspacing="2" cellpadding="2">
                   	  <tr>
                   	    <td width="36">Código:</td>
                   	    <td width="60">${ produtoEdicao.codigoProduto }</td>
                   	    <td width="42">Produto:</td>
                   	    <td width="235">${ produtoEdicao.nomeComercial }</td>
                   	    <td>Classificação:</td>
                        <td width="156">${ produtoEdicao.classificacao }</td>
                   	    <td width="35">Edição:</td>
                   	    <td width="66">${ produtoEdicao.numeroEdicao }</td>
                   	    <td width="69">&nbsp;</td>
               	      </tr>
               	  </table>
                </fieldset>
                    <br clear="all" />

                  <fieldset style="width:600px!important; margin-bottom:10px;">
               		<legend>Região</legend>
                        <table width="600" border="0" cellspacing="2" cellpadding="2">
                          <tr>
                            <td width="20"><input name="regiao" type="radio" id="RDtodasAsCotas" value="radio" onclick="mostrarTodasCotas();" checked="checked"  /></td>
                            <td width="85"><label for="RDtodasAsCotas">Todas as Cotas</label></td>
                            <td width="475">&nbsp;</td>
                          </tr>
                          <tr>
                            <td><input type="radio" name="regiao" id="RDcomponente" value="radio" onclick="mostraComponentes();" /><!--mostra_regiao()--></td>
                            <td><label for="RDcomponente">Componente:</label></td>
                 <td><table width="369" border="0" cellspacing="1" cellpadding="1" id="lstComponentes" style="display:none;">
                   <tr>
                     <td width="156">
	                     <select name="componenteRegiaoDistribuicao" id="componenteRegiaoDistribuicao"
	                     	onchange="distribuicaoVendaMedia.selectElementoRegiaoDistribuicao('componenteRegiaoDistribuicao',  $('select[name=elementoRegiaoDistribuicao]'))"  
	                     	style="width:110px;" class="filtroComponentes">
	                  		<option selected="selected">Selecione...</option>
		                    <c:forEach items="${componentes}" var="componente" varStatus="idx">
								<option value="${idx.count-1}">${componente.descricao}</option>
							</c:forEach>
		                </select>
                     </td>
                     <td width="51">Elemento:</td>
                     <td width="152">
                     	<select name="elementoRegiaoDistribuicao" id="elementoRegiaoDistribuicao" style="width:150px;">
                     	</select>
                     	<select name="elementoRegiaoDistribuicao"  style="width:150px;"></select>
                     	<select name="elementoRegiaoDistribuicao"  style="width:150px;"></select>
                     </td>
                   </tr>
                 </table></td>
                          </tr>
                          <tr>
                            <td><input type="radio" name="regiao" id="RDAbrangencia" value="radio" onclick="mostra_qtdeBancas();" /></td>
                            <td><label for="RDAbrangencia">% Abrangência</label> </td>
                            <td>
	                            <div id="qtdeBancas" style="display:none;">Critério:&nbsp;
		                            <select name="" style="margin-right:10px;" id="RDabrangenciaCriterio">
		                              <option selected="selected">Selecione...</option>
		                              <option>Faturamento</option>
		                              <option>Segmento</option>
		                              <option>Histórico</option>
		                            </select>
		                            <input type="text"  style="width:40px; text-align:center; margin-right:5px;" value="0" id="RDabrangencia"/>%
	                            </div>	
                            </td>
                          </tr>
                        </table>
                    </fieldset>
                  <fieldset style="width:600px!important; margin-bottom:10px;">
               		<legend>Informações Complementares</legend>

				    <table width="600" border="0" cellspacing="2" cellpadding="2">
                        <tr>
                          <td width="20"><input type="checkbox" name="checkbox" id="RDroteiroEntrega" onclick="$('#selRoteiro').toggle(); if($('#selRoteiro').is(':visible')){ $('#selRoteiro').val(''); }" /></td>
                          <td width="142">Roteiro de Entrega</td>
                          <td width="418">
                          <select name="selRoteiro" id="selRoteiro" style="width:200px; display:none;">
                            <option>Selecione...</option>
                            <c:forEach items="${ roteiros }" var="roteiro">
                            	<option value="${ roteiro.id }">${ roteiro.descricaoRoteiro }</option>
                            </c:forEach>
                          </select></td>
                        </tr>
                          <tr>
                            <td><input name="checkbox4" type="checkbox" id="complementarAutomatico" checked="checked" /></td>
                            <td>Complementar Automático</td>
                            <td>&nbsp;</td>
                          </tr>
                          <tr>
                            <td><input name="checkbox5" type="checkbox" id="cotasAVista" checked="checked" /></td>
                            <td>Cotas à Vista</td>
                            <td>&nbsp;</td>
                          </tr>
                          <tr>
                            <td><input type="checkbox" name="checkbox6" id="RDExcecaoBancas" onclick="$('#lstExcecao1').toggle(); limparLstExcecao1();" /></td>
                            <td>Exceção de Bancas</td>
                            <td><table width="369" border="0" cellspacing="1" cellpadding="1" id="lstExcecao1" style="display:none;">
                   <tr>
                     <td width="156">
	                     <select name="componenteInformacoesComplementares" id="componenteInformacoesComplementares"
	                     	onchange="distribuicaoVendaMedia.selectElementoRegiaoDistribuicao('componenteInformacoesComplementares', 'elementoInformacoesComplementares1')"  
	                     	style="width:110px;" class="filtroComponentes">
	                  		<option selected="selected">Selecione...</option>
		                    <c:forEach items="${componentes}" var="componente" varStatus="idx">
								<option value="${idx.count-1}">${componente.descricao}</option>
							</c:forEach>
		                </select>
                     </td>
                     <td width="51">Elemento:</td>
                     <td width="152">
	                    <select 
	                    	name="elementoInformacoesComplementares" 
	                    	id="elementoInformacoesComplementares1" 
	                    	style="width:150px;">
	                    </select>
 	                    <select 
 	                    	name="elementoInformacoesComplementares" 
 	                    	id="elementoInformacoesComplementares2" 
 	                    	style="width:150px; ">
 	                    </select>
                       	<select 
                       		name="elementoInformacoesComplementares" 
                       		id="elementoInformacoesComplementares3" 
                       		style="width:150px;">
                       	</select>
                     </td>
                   </tr>
                 </table></td>
                          </tr>
                    </table>
                        
                    </fieldset>
                </div>
                <br clear="all" />

	</div>

   		</div>
      </fieldset>
      <fieldset style="float: left; width: 280px !important;">
       	  <legend>Estratégia</legend>
          <table width="270" border="0" cellspacing="2" cellpadding="0">
  <tr>
    <td width="219">${ produtoEdicao.nomeProduto } - Edição: ${ produtoEdicao.numeroEdicao }</td>
    <td width="28">Capa:</td>
    <td width="15"><a href="javascript:;" onmouseover="distribuicaoVendaMedia.popup_detalhes('${ produtoEdicao.codigoProduto }','${ produtoEdicao.numeroEdicao }');" onmouseout="popup_detalhes_close();"><img src="images/ico_detalhes.png" alt="Capa" width="15" height="15" border="0" /></a></td>
  </tr>
</table>
<p><strong>Período:</strong> ${ estrategia.periodo }</p>
<p><strong>Chamada de Capa:</strong> ${ estrategia.produtoEdicao.chamadaCapa }</p>


        <table class="estrategiaGrid">
        	<c:forEach var="estrategiaEdicao" items="${ estrategia.basesEstrategia }">
        		<tr>
        			<td>
        				${ estrategiaEdicao.produtoEdicao.produto.codigo }
        			</td>
        			<td>
        				${ estrategiaEdicao.produtoEdicao.produto.nome }
        			</td>
        			<td>
        				${ estrategiaEdicao.produtoEdicao.numeroEdicao }
        			</td>
        			<td>
        				${ estrategiaEdicao.periodoEdicao}
        			</td>
        			<td>
        				${ estrategiaEdicao.produtoEdicao.peso }
        			</td>
        		</tr>
        	</c:forEach>
        </table>
        <br />


          <p><strong>Reparte Mínimo:</strong> ${ estrategia.reparteMinimo }</p>

          <p><strong>Abrangência:</strong> ${ estrategia.abrangencia }%</p>
          
        <p><strong>Oportunidade de Venda:</strong></p>
          <textarea cols="30" rows="6" >${estrategia.oportunidadeVenda}</textarea>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      

        

    
    </div>
</div>

<script>
$(".listaRegiaoGrid").flexigrid({
	//url : '../xml/pesqRegiao-xml.xml',
	dataType : 'json',
	colModel : [ {
		display : 'Região',
		name : 'regiao',
		width : 330,
		sortable : true,
		align : 'left'
	}, {
		display : '',
		name : 'sel',
		width : 20,
		sortable : true,
		align : 'center'
	}],
	width : 400,
	height : 200
});
$(".edicaoProdCadastradosGrid-1").flexigrid({	
	dataType : 'json',
	colModel : [ {
		display : 'Código',
		name : 'codigoProduto',
		width : 60,
		sortable : true,
		align : 'left'
	}, {
		display : 'Edição',
		name : 'numeroEdicao',
		width : 40,
		sortable : true,
		align : 'left'
	}, {
		display : 'Período',
		name : 'periodo',
		width : 40,
		sortable : true,
		align : 'center'
	}, {
		display : 'Data Lançamento',
		name : 'dataLancamentoFormatada',
		width : 90,
		sortable : true,
		align : 'center'
	}, {
		display : 'Reparte',
		name : 'reparte',
		width : 40,
		sortable : true,
		align : 'right'
	}, {
		display : 'Venda',
		name : 'venda',
		width : 35,
		sortable : true,
		align : 'right'
	}, {
		display : 'Status',
		name : 'status',
		width : 70,
		sortable : true,
		align : 'left'
	}, {
		display : 'Classificação',
		name : 'classificacao',
		width : 70,
		sortable : true,
		align : 'left'
	}, {
		display : 'Capa',
		name : 'capa',
		width : 25,
		sortable : true,
		align : 'center'
	}, {
		display : '',
		name : 'select',
		width : 20,
		sortable : true,
		align : 'center'
	}],
	width : 650,
	height : 200,
	sortname : "dataLancamentoFormatada",
	sortorder : "desc",
	usepager : true,
    useRp : true,
    rp : 15
});
$(".elemento1Grid").flexigrid({
	//url : '../xml/elemento1-xml.xml',
	dataType : 'json',
	colModel : [ {
		display : 'Componentes',
		name : 'componenteDesc',
		width : 120,
		sortable : true,
		align : 'left'
	}, {
		display : 'Elementos',
		name : 'elementoDesc',
		width : 145,
		sortable : true,
		align : 'left'
	}, {
		display : '% Bonificacao',
		name : 'percBonificacaoInput',
		width : 80,
		sortable : true,
		align : 'center'
	}, {
		display : 'Reparte Min.',
		name : 'reparteMinimoInput',
		width : 70,
		sortable : true,
		align : 'center'
	}, {
		display : 'Todas<br/> as Cotas',
		name : 'sel',
		width : 45,
		sortable : true,
		align : 'center'
	}, {
		display : 'Ação',
		name : 'acao',
		width : 25,
		sortable : true,
		align : 'center'
	}],
	width : 580,
	height : 130
});


$(".bonificacoesGrid").flexigrid({
	//url : '../xml/criarRegiao1-xml.xml',
	dataType : 'json',
	colModel : [{
		display : 'Elementos',
		name : 'descricao',
		width : 205,
		sortable : true,
		align : 'left'
	}, {
		display : 'Ação',
		name : 'acao',
		width : 30,
		sortable : true,
		align : 'center'
	}],
	width : 280,
	height : 110
});



$(".estrategiaGrid").flexigrid({
	//url : '../xml/listaEstrategias-xml.xml',
	dataType : 'json',
	colModel : [ {
		display : 'Código',
		name : 'codigo',
		width : 35,
		sortable : true,
		align : 'left'
	},{
		display : 'Prod.',
		name : 'produto',
		width : 90,
		sortable : true,
		align : 'left'
	},{
		display : 'Edição',
		name : 'edicao',
		width : 35,
		sortable : true,
		align : 'left'
	},{
		display : 'Per.',
		name : 'periodo',
		width : 20,
		sortable : true,
		align : 'left'
	}, {
		display : 'Peso',
		name : 'peso',
		width : 20,
		sortable : true,
		align : 'center'
	}],
	width : 280,
	height : 110
});



$(".lstComponentesGrid").flexigrid({
	//url : '../xml/listaComponentes-xml.xml',
	dataType : 'json',
	colModel : [ {
		display : 'Componentes',
		name : 'componentes',
		width : 205,
		sortable : true,
		align : 'left'
	}, {
		display : 'Ação',
		name : 'acao',
		width : 30,
		sortable : true,
		align : 'center'
	}],
	width : 280,
	height : 110
});
$(".dadosBasesGrid").flexigrid({
	//url : '../xml/dadosDistribB-xml.xml',
	dataType : 'json',
	colModel : [{display: '',           name: 'select',          width: 15, sortable: true, align: 'center'},
	    	    {display: 'Peso',       name: 'pesoInput',       width: 30, sortable: true, align: 'center'},
	    	    {display: 'Código',     name: 'codigoProduto',   width: 45, sortable: true, align: 'left'},
	    	    {display: 'Produto',    name: 'nome',            width: 70, sortable: true, align: 'left'},
	    	    {display: 'Edição',     name: 'numeroEdicao',    width: 30, sortable: true, align: 'left'},
	    	    {display: 'Classific.', name: 'classificacao',   width: 60, sortable: true, align: 'left'},
	    	    {display: 'Per.',       name: 'periodo',         width: 20, sortable: true, align: 'center'},
	    	    {display: 'Data Lcto.', name: 'dataLancamentoFormatada',  width: 60, sortable: true, align: 'right'},
	    	    {display: 'Status',     name: 'status',          width: 55, sortable: true, align: 'left'},
	    	    {display: 'Reparte',    name: 'reparte',         width: 35, sortable: true, align: 'center'},
	    	    {display: 'Venda',      name: 'venda',           width: 35, sortable: true, align: 'center' },
	    	    {display: 'Venda %',    name: 'percentualVenda', width: 40, sortable: true, align: 'right'}
	    	    ],
	width : 610,
	height : 240
});

// Recalcular estudo, preenchimento do estado inicial.
$(function () {
    var vendaMediaDTO = ${vendaMediaDTO == null ? 'false' : vendaMediaDTO};
    distribuicaoVendaMedia.recuperarEstadoDaTela(vendaMediaDTO);
});
</script>