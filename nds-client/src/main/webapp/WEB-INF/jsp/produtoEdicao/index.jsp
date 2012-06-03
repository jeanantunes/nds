<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produtoEdicao.js"></script>

<script language="javascript" type="text/javascript">

function pesquisarEdicoes() {

	var codigoProduto = $("#pCodigoProduto").val();
	var nomeProduto = $("#pNomeProduto").val();
	var dataLancamento = $("#pDataLancamento").val();
	var situacaoLancamento = $("#pSituacaoLancamento").val();
	var codigoDeBarras = $("#pCodigoDeBarras").val();
	
	$("#pBrinde").val(0);
	if (document.getElementById('pBrinde').checked){
		$("#pBrinde").val(1);
	}
	var brinde = $("#pBrinde").val();
	
	$(".edicoesGrid").flexOptions({
		url: "<c:url value='/cadastro/edicao/pesquisarEdicoes.json' />",
		params: [{name:'codigoProduto', value: codigoProduto },
			     {name:'nomeProduto', value: nomeProduto },
			     {name:'dataLancamento', value: dataLancamento },
			     {name:'situacaoLancamento', value: situacaoLancamento },
			     {name:'codigoDeBarras', value: codigoDeBarras },
			     {name:'brinde', value : brinde }],
		newp: 1,
	});
	
	$(".edicoesGrid").flexReload();
}

function executarPreProcessamento(resultado) {

	$.each(resultado.rows, function(index, row) {
		
		var linkAprovar = '<a href="javascript:;" onclick="editarEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
				     	  	'<img title="Editar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
				  		  '</a>';
		
		var linkExcluir = '<a href="javascript:;" onclick="removerEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
						   	 '<img title="Excluir" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
						   '</a>';
		
		row.cell.acao = linkAprovar + linkExcluir;
	});
		
	$(".grids").show();
	
	return resultado;
}


function popup() {

	if ($(".edicoesGrid > tbody").data() == null || $(".edicoesGrid > tbody").data() == undefined) {
		exibirMensagem('WARNING', ['Por favor, escolha um produto para adicionar a Edi&ccedil;&atilde;o!'], "");
		return;
	}

	// Popular a lista de Edições:
	// TODO: Pegar o código do Produto!
	$(".prodsPesqGrid").flexOptions({
		url: "<c:url value='/cadastro/edicao/ultimasEdicoes.json' />",
		params: [{name:'codigoProduto', value: 1 }],
		newp: 1,
	});
	
	$(".prodsPesqGrid").flexReload();

	

	//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:615,
			width:960,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:615,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					$( "#abaPdv" ).show( );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function popup_excluir() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function popup_excluir_capa() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir-capa" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
	function popup_capa() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-capa" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	$(function() {
			$( "#tabEdicoes" ).tabs();
	});
	
	$(function() {
		$( "#pDataLancamento" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dateLancto_pop" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dtLancto" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
function mostrar_prod(){
	$( "#pesqProdutos" ).fadeIn('slow');
	
	}
	function fecha_prod(){
	$( "#pesqProdutos" ).fadeOut('slow');
	
	}
function mostraLinhaProd(){
	
	$( ".prodLinhas" ).show('slow');
	}
</script>
<style>
label{ vertical-align:super;}
#dialog-novo label{width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px;}
.prodLinhas{display:none;}
.ui-tabs .ui-tabs-panel {
   padding: 6px!important;
}
.ldForm{float:left; width:652px!important; margin-left:15px;}
fieldset {
    margin-right:0px!important;
}
.ldPesq{float:left; width:210px;}
</style>
</head>

<body>

<div id="dialog-capa" title="Incluir Imagem Capa" style="display:none;">
	<br />
	<p><input type="file" size="30" /></p>
</div>

<div id="dialog-excluir-capa" title="Excluir Capa" style="display:none;">
	<p>Confirma a exclus&atilde;o desta Capa?</p>
</div>

<div id="dialog-excluir" title="Excluir Edi&ccedil;&atilde;o">
	<p>Confirma a exclus&atilde;o desta Edi&ccedil;&atilde;o?</p>
</div>



<div id="dialog-novo" title="Incluir Nova Edi&ccedil;&atilde;o">
	<div id="tabEdicoes">
		<ul>
			<li><a href="#tabEdicoes-1">Identifica&ccedil;&atilde;o</a></li>
			<li><a href="#tabEdicoes-2">Caracter&iacute;sticas do Lan&ccedil;amento</a></li>
			<li><a href="#tabEdicoes-3">Segmenta&ccedil;&atilde;o</a></li>
		</ul>
		
		<div id="tabEdicoes-1">
			<div class="ldPesq">
				<fieldset id="pesqProdutos" style="width:200px!important;">
					<legend>Produtos Pesquisados</legend>
					<table class="prodsPesqGrid"></table>
				</fieldset>
				
				<span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span>
			</div>
			
			<div class="ldForm">
				<fieldset style="width:655px!important; margin-bottom:5px;">
					<legend>Identifica&ccedil;&atilde;o</legend>
					<table width="648" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="181"><strong>C&oacute;digo:</strong></td>
								<td width="100"><input type="text" name="textfield" id="textfield" style="width:100px;" /></td>
								<td width="90">&nbsp;</td>
								<td width="108">&nbsp;</td>
								<td width="153" rowspan="8" align="center">
									<img src="../capas/v.jpg" width="144" height="185" alt="Veja" />
									<br clear="all" />
									<a href="javascript:;" onclick="popup_capa();"><img src="../images/bt_cadastros.png" alt="Editar Capa" width="15" height="15" hspace="5" vspace="3" border="0" /></a><a href="javascript:;" onclick="popup_excluir_capa();"><img src="../images/ico_excluir.gif" alt="Excluir Capa" width="15" height="15" hspace="5" vspace="3" border="0" /></a>
								</td>
							</tr>
							<tr>
								<td><strong>Nome Publica&ccedil;&atilde;o:</strong></td>
								<td colspan="3"><input type="text" name="textfield8" id="textfield8" style="width:250px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>Nome Comercial Produto:</strong></td>
								<td colspan="3"><input type="text" name="textfield13" id="textfield22" style="width:250px;" /></td>
							</tr>
							<tr>
								<td><strong>Fornecedor:</strong></td>
								<td colspan="3"><input type="text" name="textfield6" id="textfield6" style="width:250px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>Situa&ccedil;&atilde;o:</strong></td>
								<td colspan="3"><input type="text" name="textfield5" id="textfield7" style="width:250px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>Edi&ccedil;&atilde;o:</strong></td>
								<td><input type="text" name="textfield17" id="textfield16" style="width:50px;" /></td>
								<td><strong>Fase:</strong></td>
								<td><input type="text" name="textfield18" id="textfield17" style="width:50px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>N&ordm; Lancto:</strong></td>
								<td><input type="text" name="textfield19" id="textfield18" style="width:50px;" disabled="disabled"  /></td>
								<td><strong>Pct. Padr&atilde;o:</strong></td>
								<td><input type="text" name="textfield16" id="textfield19" style="width:50px;" /></td>
							</tr>
							<tr>
								<td><strong>Tipo de Lan&ccedil;amento:</strong></td>
								<td colspan="3">
									<select name="select2" id="select2" style="width:260px;" >
										<option selected="selected">Selecione...</option>
										<option>Lan&ccedil;amento</option>
										<option>Ed. Parcial</option>
										<option>Relan&ccedil;amento</option>
										<option>Redistribui&ccedil;&atilde;o</option>
										<option>Supl. Compuls</option>
									</select>
								</td>
							</tr>
						</tbody>
					</table>
				</fieldset>
				<fieldset style="width:220px!important; margin-bottom:2px; float:right; margin-right:0px;">
					<legend>Reparte</legend>
					<table width="190" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="103"><strong>Previsto:</strong></td>
								<td width="80"><input type="text" name="textfield4" id="textfield6" style="width:80px; float:left;" /></td>
							</tr>
							<tr>
								<td><strong>Distribuido:</strong></td>
								<td><input type="text" name="textfield20" id="textfield20" style="width:80px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>Promocional:</strong></td>
								<td><input type="text" name="textfield21" id="textfield21" style="width:80px; float:left;" /></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
				<fieldset style="width:350px!important; margin-bottom:2px; float:left;">
					<legend>Pre&ccedil;o da Capa</legend>
					<table width="309" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="76"><strong>Previsto:</strong></td>
								<td width="99"><input type="text" name="textfield4" id="textfield6" style="width:70px; float:left;" /></td>
								<td width="51"><strong>Real:</strong></td>
								<td width="70"><input type="text" name="textfield9" id="textfield10" style="width:70px; text-align:right;" disabled="disabled" /></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
				<fieldset style="width:350px!important; margin-bottom:2px; float:left;">
					<legend>Data Lan&ccedil;amento</legend>
					<table width="309" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="76"><strong>Previsto:</strong></td>
								<td width="99"><input type="text" name="textfield4" id="textfield6" style="width:70px; float:left;" /></td>
								<td width="51"><strong>Real:</strong></td>
								<td width="70"><input type="text" name="textfield9" id="textfield10" style="width:70px; text-align:right;" disabled="disabled" /></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
			</div>
			<br clear="all" />
		</div>
		
		<div id="tabEdicoes-2">
			<div class="ldPesq">
				<fieldset id="pesqProdutos" style="width:200px!important;">
					<legend>Produtos Pesquisados</legend>
					<table class="prodsPesqGrid"></table>
				</fieldset>
				
				<span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span>
			</div>
			
			<div class="ldForm">
				<fieldset style="width:350px!important; margin-bottom:5px;">
					<legend>Caracter&iacute;sticas do L&ccedil;amento</legend>
					<table width="345" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="145">Categoria:</td>
								<td width="193">
									<select name="select2" id="select2" style="width:180px;" >
										<option selected="selected">Selecione...</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>Cod. de Barras:</td>
								<td><input type="text" name="textfield5" id="textfield3" style="width:180px;" /></td>
							</tr>
							<tr>
								<td>Cod. Barras Corporativo:</td>
								<td><input type="text" name="textfield3" id="textfield4" style="width:180px;" /></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
				<fieldset style="width:250px!important; margin-bottom:5px; float:right;">
					<legend>Tipos de Desconto</legend>
					<table width="250" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td colspan="2"><input type="text" name="textfield9" id="textfield9" style="width:235px;" /></td>
							</tr>
							<tr>
								<td>Desconto:</td>
								<td><input type="text" name="textfield10" id="textfield10" style="width:113px;" /></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
				<fieldset style="width:250px!important; float:right; margin-bottom:5px;">
					<legend>Caracter&iacute;stica F&iacute;sica</legend>
					<table width="152" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="59">Peso:</td>
								<td width="86"><input type="text" name="textfield14" id="textfield14" style="width:80px;" /></td>
							</tr>
							<tr>
								<td width="59">Largura:</td>
								<td width="86"><input type="text" name="textfield14" id="textfield14" style="width:80px;" /></td>
							</tr>
							<tr>
								<td width="59">Comprimento:</td>
								<td width="86"><input type="text" name="textfield14" id="textfield14" style="width:80px;" /></td>
							</tr>
							<tr>
								<td width="59">Espessura:</td>
								<td width="86"><input type="text" name="textfield14" id="textfield14" style="width:80px;" /></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
				<fieldset style="width:350px!important; float:left; margin-bottom:5px;">
					<legend>Outros</legend>
					<table width="330" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="130" height="24">Chamada de Capa:</td>
								<td width="193"><input type="text" name="textfield2" id="textfield2" style="width:190px;" /></td>
							</tr>
							<tr>
								<td height="24">Regime Recolhimento:</td>
								<td>
									<select name="select3" id="select3" style="width:190px;" >
										<option selected="selected">Selecione...</option>
									</select>
								</td>
							</tr>
							<tr>
								<td height="24">Brinde:</td>
								<td><input type="checkbox" name="checkbox" id="checkbox" /></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
				<fieldset style="width:640px!important; float:left; margin-bottom:5px;">
					<legend>Texto Boletim Informativo</legend>
					<table width="600" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="600"><textarea name="textfield2" rows="5" id="textfield2" style="width:610px;"></textarea></td>
						</tbody>
					</table>
				</fieldset>
				<br clear="all" />
			</div>
			<br clear="all" />
		</div>
		
		<div id="tabEdicoes-3">
			<div class="ldPesq">
				<fieldset id="pesqProdutos" style="width:200px!important;">
					<legend>Produtos Pesquisados</legend>
					<table class="prodsPesqGrid"></table>
				</fieldset>
				
				<span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span>
			</div>
			
			<div class="ldForm">
				<fieldset style="width:410px!important;">
					<legend>P&uacute;blico Alvo</legend>
					<table width="400" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="112"><strong>Classe Social:</strong></td>
								<td width="281">
									<select name="select4" id="select7" style="width:250px;" disabled="disabled">
										<option selected="selected">Selecione...</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><strong>Sexo:</strong></td>
								<td>
									<select name="select4" id="select8" style="width:250px;" disabled="disabled">
										<option selected="selected">Selecione...</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><strong>Faixa-Et&aacute;ria:</strong></td>
								<td>
									<select name="select4" id="select10" style="width:250px;" disabled="disabled">
										<option selected="selected">Selecione...</option>
									</select>
								</td>
							</tr>
						</tbody>
					</table>
				</fieldset>
				<fieldset style="width:410px!important;">
					<legend>Outros</legend>
					<table width="400" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="112"><strong>Tema Principal:</strong></td>
								<td width="281">
									<select name="select" id="select5" style="width:250px;" disabled="disabled">
										<option selected="selected">Selecione...</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><strong>Tema Secund&aacute;rio:</strong></td>
								<td>
									<select name="select" id="select6" style="width:250px;" disabled="disabled">
										<option selected="selected">Selecione...</option>
									</select>
								</td>
						</tbody>
					</table>
				</fieldset>
			</div>
			<br clear="all" />
		</div>
	</div>
</div>








<div class="corpo">
	
	<br clear="all"/>
	<br />

	<div class="container">
	
		<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Edi&ccedil;&atilde;o < evento > com < status >.</b>
			</p>
		</div>
		
		<fieldset class="classFieldset">
			<legend>Pesquisar Produto</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<thead/>
				<tbody>
					<tr>
						<td width="72">C&oacute;digo:</td>
						<td width="80">
							<input type="text" name="pCodigoProduto" id="pCodigoProduto" maxlength="255" 
									style="width:80px;" 
									onchange="produtoEdicao.pesquisarPorCodigoProduto('#pCodigoProduto', '#pNomeProduto', false,
											undefined,
											undefined);" />
						</td>
						<td width="47">Produto:</td>
						<td width="172">
							<input type="text" name="pNomeProduto" id="pNomeProduto" maxlength="255" 
									style="width:170px;"
									onkeyup="produtoEdicao.autoCompletarPorNomeProduto('#pNomeProduto', false);"
									onblur="produtoEdicao.pesquisarPorNomeProduto('#pCodigoProduto', '#pNomeProduto', false,
										undefined,
										undefined);" />
						</td>
						<td width="100">Data Lan&ccedil;amento:</td>
						<td width="105"><input type="text" name="pDataLancamento" id="pDataLancamento" style="width:80px;"/></td>
						<td width="50">Situa&ccedil;&atilde;o:</td>
						<td width="168">
							<select name="select" id="pSituacaoLancamento" name="pSituacaoLancamento" style="width:150px;">
								<option value="" selected="selected">Selecione...</option>
								<option value="Transmitido">Transmitido</option>
								<option value="Previsto">Previsto</option>
								<option value="C&aacute;lculo Solicitado">C&aacute;lculo Solicitado</option>
								<option value="Calculado">Calculado</option>
								<option value="Furo">Furo</option>
								<option value="Emitido">Emitido</option>
								<option value="Liberar C&aacute;lculo">Liberar C&aacute;lculo</option>
								<option value="Confirmado">Confirmado</option>
								<option value="Lan&ccedil;ado">Lan&ccedil;ado</option>
								<option value="Em Recolhimento">Em Recolhimento</option>
								<option value="Recolhido">Recolhido</option>
								<option value="Fechado">Fechado</option>
							</select>
						</td>
						<td width="110">&nbsp;</td>
					</tr>
					<tr>
						<td>C&oacute;d. Barras:</td>
						<td colspan="3" ><input type="text" name="pCodigoDeBarras" id="pCodigoDeBarras" style="width:311px;"/></td>
						<td align="right"><input type="checkbox" name="pBrinde" id="pBrinde" value=""/></td>
						<td>Brinde</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisarEdicoes();">Pesquisar</a></span></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
		<fieldset class="classFieldset">
			<legend>Edi&ccedil;&otilde;es do Produto<span id="labelNomeProduto" /></legend>
			<div class="grids" style="display:none;">
				<table class="edicoesGrid"></table>
			</div>
			
			<span class="bt_novos" title="Novo">
				<a href="javascript:;" onclick="popup();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a>
			</span>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
	</div>
</div> 
<script>
	$(".bonificacoesGrid").flexigrid({
			//url : '../xml/produtos_bonificacoes-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Faixa',
				name : 'faixa',
				width : 320,
				sortable : true,
				align : 'left'
			}, {
				display : 'Quantidade Adicional',
				name : 'qtdeAdicional',
				width : 160,
				sortable : true,
				align : 'center'
			}, {
				display : 'A&ccedil;ões',
				name : 'acoes',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 620,
			height : 120
		});

	$(".prodsPesqGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Produto',
				name : 'nomeProduto',
				width : 115,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi&ccedil;&atilde;o',
				name : 'numeroEdicao',
				width : 40,
				sortable : true,
				align : 'left'
			}],
			width : 200,
			height : 350
		});
		
		
	$(".edicoesGrid").flexigrid({
		preProcess: executarPreProcessamento,
		dataType : 'json',
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome Comercial',
				name : 'nomeProduto',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi&ccedil;&atilde;o',
				name : 'numeroEdicao',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 200,
				sortable : true,
				align : 'left',
			}, {
				display : 'Tipo de Lan&ccedil;amento',
				name : 'statusLancamento',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Situa&ccedil;&atilde;o',
				name : 'statusSituacao',
				width : 115,
				sortable : true,
				align : 'left'
			}, {
				display : 'Brinde',
				name : 'temBrinde',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		
		$(".produtoEdicaoBaseGrid").flexigrid({
			preProcess: executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 280,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto &Uacute;nico',
				name : 'produtoUnico',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Edi&ccedil;&atilde;o Base',
				name : 'edicaoBase',
				width : 100,
				sortable : true,
				align : 'center',
			}],
			width : 640,
			height : 120
		});
</script>
</body>
</html>
