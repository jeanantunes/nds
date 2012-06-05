<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produtoEdicao.js"></script>

<script language="javascript" type="text/javascript">

var codProduto;

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

	var nProduto = '';
	var cProduto = '';
	$.each(resultado.rows, function(index, row) {
		
		var linkAprovar = '<a href="javascript:;" onclick="editarEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
				     	  	'<img title="Editar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
				  		  '</a>';
		
		var linkExcluir = '<a href="javascript:;" onclick="removerEdicao(' + row.cell.id + ');" style="cursor:pointer">' +
						   	 '<img title="Excluir" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
						   '</a>';
		
		row.cell.acao = linkAprovar + linkExcluir;

		//
		nProduto = row.cell.nomeProduto;
		cProduto = row.cell.codigoProduto;
	});
		
	$(".grids").show();

	//
	codProduto = cProduto; 
	var txt = '';
	if (nProduto != null || cProduto != null) {
		txt = ": " + cProduto + " - " + nProduto;
	}
	$("#labelNomeProduto").html(txt);
	
	return resultado;
}


function editarEdicao(id) {
	
	if ($(".edicoesGrid > tbody").data() == null || $(".edicoesGrid > tbody").data() == undefined) {
		exibirMensagem('WARNING', ['Por favor, escolha um produto para adicionar a Edi&ccedil;&atilde;o!'], "");
		return;
	}
	
	// Popular a lista de Edições:
	$(".prodsPesqGrid").flexOptions({
		url: "<c:url value='/cadastro/edicao/ultimasEdicoes.json' />",
		params: [{name:'codigoProduto', value: 1 }],
		newp: 1,
	});

	$(".prodsPesqGrid").flexReload();

	// Exibir os dados do Produto:
	$.postJSON(
		"<c:url value='/cadastro/edicao/carregarDadosProdutoEdicao.json' />",
		{ codigoProduto : codProduto, 
		  idProdutoEdicao : id},
		function(result) {
			if (result) {
				$("#codigoProdutoEdicao").val(result.codigoProduto);
				$("#nomePublicacao").val(result.nomeProduto);
				$("#nomeComercialProduto").val(result.nomeComercialProduto);
				$("#nomeFornecedor").val(result.nomeFornecedor);
				//$("#situacao").val(result.situacao);
				$("#numeroEdicao").val(result.numeroEdicao)
				$("#fase").val(result.fase);
				$("#numeroLancamento").val(result.numeroLancamento);
				$("#pacotePadrao").val(result.pacotePadrao);
				$("#tipoLancamento").val(result.tipoLancamento);
				$("#precoPrevisto").val(result.precoPrevisto);
				$("#precoVenda").val(result.precoVenda);
				$("#dataLancamentoPrevisto").val(result.dataLancamentoPrevisto.$);
				$("#dataLancamento").val(result.dataLancamentoPrevisto.$);
				$("#repartePrevisto").val(result.repartePrevisto)
				$("#reparteDistribuido").val(result.reparteDistribuido);
				$("#repartePromocional").val(result.repartePromocional);
				//$("#categoria").val();
				$("#codigoDeBarras").val(result.codigoDeBarras);
				$("#codigoDeBarrasCorporativo").val(result.codigoDeBarrasCorporativo);
				$("#desconto").val(result.desconto);
				$("#peso").val(result.peso);
				$("#largura").val(result.largura);
				$("#comprimento").val(result.comprimento);
				$("#espessura").val(result.espessura);
				$("#chamadaCapa").val(result.chamadaCapa);
				$('#parcial').val(result.parcial + "");
				$('#possuiBrinde').attr('checked', result.possuiBrinde);
				$('#boletimInformativo').val(result.boletimInformativo);
			}
		},
		function(result) { 
			exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "");
			},
		true
	);

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

	if (id != null) {

		var imgPath = "<c:url value='/capa/' />" + id;
		var img = $("<img />").attr('src', imgPath).attr('width', '144').attr('height', '185').attr('alt', 'Capa')
		.load(function() {
			if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
				$("#div_imagem_capa").empty();
				$("#div_imagem_capa").append(img);
			}
		});
	}
};


function novaEdicao() {
	editarEdicao(null);	
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
	
function removerEdicao(id) {

	$( "#dialog-excluir" ).dialog({
		resizable: false,
		height:170,
		width:380,
		modal: true,
		buttons: {
			"Confirmar": function() {

				$.postJSON(
					"<c:url value='/cadastro/edicao/removerEdicao.json' />",
					{idProdutoEdicao : id},
					function(result) {
				   		$("#dialog-excluir").dialog("close");
				   		
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagemDialog(tipoMensagem, listaMensagens);
						}
								
						$(".edicoesGrid").flexReload();
					},
					function(result) {
				   		$("#dialog-excluir").dialog("close");
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagemDialog(tipoMensagem, listaMensagens);
						}
					},
					true
				);
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		},
		beforeClose: function() {
			clearMessageDialogTimeout();
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
				
				<span class="bt_add"><a href="javascript:;" onclick="novaEdicao();">Incluir Novo</a></span>
			</div>
			
			<div class="ldForm">
				<fieldset style="width:655px!important; margin-bottom:5px;">
					<legend>Identifica&ccedil;&atilde;o</legend>
					<table width="648" border="0" cellspacing="1" cellpadding="1">
						<thead />
						<tbody>
							<tr>
								<td width="181"><strong>C&oacute;digo:</strong></td>
								<td width="100"><input type="text" name="codigoProdutoEdicao" id="codigoProdutoEdicao" style="width:100px;" /></td>
								<td width="90">&nbsp;</td>
								<td width="108">&nbsp;</td>
								<td width="153" rowspan="8" align="center">
									
									<div id="div_imagem_capa">
										<img width="144" height="185" alt="Capa" />
									</div>
									
									<br clear="all" />
									<a href="javascript:;" onclick="popup_capa();">
										<img src="${pageContext.request.contextPath}/images/bt_cadastros.png" alt="Editar Capa" width="15" height="15" hspace="5" vspace="3" border="0" />
									</a>
									<a href="javascript:;" onclick="popup_excluir_capa();">
										<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir Capa" width="15" height="15" hspace="5" vspace="3" border="0" />
									</a>
								</td>
							</tr>
							<tr>
								<td><strong>Nome Publica&ccedil;&atilde;o:</strong></td>
								<td colspan="3"><input type="text" name="nomePublicacao" id="nomePublicacao" style="width:250px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>Nome Comercial Produto:</strong></td>
								<td colspan="3"><input type="text" name="nomeComercialProduto" id="nomeComercialProduto" style="width:250px;" /></td>
							</tr>
							<tr>
								<td><strong>Fornecedor:</strong></td>
								<td colspan="3"><input type="text" name="nomeFornecedor" id="nomeFornecedor" style="width:250px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>Situa&ccedil;&atilde;o:</strong></td>
								<td colspan="3"><input type="text" name="situacao" id="situacao" style="width:250px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>Edi&ccedil;&atilde;o:</strong></td>
								<td><input type="text" name="numeroEdicao" id="numeroEdicao" style="width:50px;" /></td>
								<td><strong>Fase:</strong></td>
								<td><input type="text" name="fase" id="fase" style="width:50px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>N&ordm; Lancto:</strong></td>
								<td><input type="text" name="numeroLancamento" id="numeroLancamento" style="width:50px;" disabled="disabled"  /></td>
								<td><strong>Pct. Padr&atilde;o:</strong></td>
								<td><input type="text" name="pacotePadrao" id="pacotePadrao" style="width:50px;" /></td>
							</tr>
							<tr>
								<td><strong>Tipo de Lan&ccedil;amento:</strong></td>
								<td colspan="3">
									<select name="tipoLancamento" id="tipoLancamento" style="width:260px;" >
										<option value="">Selecione...</option>
										<option value="LANCAMENTO">Lan&ccedil;amento</option>
										<option value="PARCIAL">Ed. Parcial</option>
										<option value="RELANCAMENTO">Relan&ccedil;amento</option>
										<option value="REDISTRIBUICAO">Redistribui&ccedil;&atilde;o</option>
										<option value="SUPLEMENTAR">Supl. Compuls</option>
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
								<td width="80"><input type="text" name="repartePrevisto" id="repartePrevisto" style="width:80px; float:left;" /></td>
							</tr>
							<tr>
								<td><strong>Distribuido:</strong></td>
								<td><input type="text" name="reparteDistribuido" id="reparteDistribuido" style="width:80px;" disabled="disabled" /></td>
							</tr>
							<tr>
								<td><strong>Promocional:</strong></td>
								<td><input type="text" name="repartePromocional" id="repartePromocional" style="width:80px; float:left;" /></td>
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
								<td width="99"><input type="text" name="precoPrevisto" id="precoPrevisto" style="width:70px; float:left;" /></td>
								<td width="51"><strong>Real:</strong></td>
								<td width="70"><input type="text" name="precoVenda" id="precoVenda" style="width:70px; text-align:right;" disabled="disabled" /></td>
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
								<td width="99"><input type="text" name="dataLancamentoPrevisto" id="dataLancamentoPrevisto" style="width:70px; float:left;" /></td>
								<td width="51"><strong>Real:</strong></td>
								<td width="70"><input type="text" name="dataLancamento" id="dataLancamento" style="width:70px; text-align:right;" disabled="disabled" /></td>
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
				
				<span class="bt_add"><a href="javascript:;" onclick="novaEdicao();">Incluir Novo</a></span>
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
								<td><input type="text" name="codigoDeBarras" id="codigoDeBarras" style="width:180px;" /></td>
							</tr>
							<tr>
								<td>Cod. Barras Corporativo:</td>
								<td><input type="text" name="codigoDeBarrasCorporativo" id="codigoDeBarrasCorporativo" style="width:180px;" /></td>
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
								<td><input type="text" name="desconto" id="desconto" style="width:113px;" /></td>
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
								<td width="86"><input type="text" name="peso" id="peso" style="width:80px;" /></td>
							</tr>
							<tr>
								<td width="59">Largura:</td>
								<td width="86"><input type="text" name="largura" id="largura" style="width:80px;" /></td>
							</tr>
							<tr>
								<td width="59">Comprimento:</td>
								<td width="86"><input type="text" name="comprimento" id="comprimento" style="width:80px;" /></td>
							</tr>
							<tr>
								<td width="59">Espessura:</td>
								<td width="86"><input type="text" name="espessura" id="espessura" style="width:80px;" /></td>
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
								<td width="193"><input type="text" name="chamadaCapa" id="chamadaCapa" style="width:190px;" /></td>
							</tr>
							<tr>
								<td height="24">Regime Recolhimento:</td>
								<td>
									<select name="parcial" id="parcial" style="width:190px;" >
										<option value="">Selecione...</option>
										<option value="true">Parcial</option>
										<option value="false">Normal</option>
									</select>
								</td>
							</tr>
							<tr>
								<td height="24">Brinde:</td>
								<td><input type="checkbox" name="possuiBrinde" id="possuiBrinde" /></td>
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
								<td width="600"><textarea name="boletimInformativo" id="boletimInformativo" rows="5" style="width:610px;"></textarea></td>
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
				
				<span class="bt_add"><a href="javascript:;" onclick="novaEdicao();">Incluir Novo</a></span>
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
				<a href="javascript:;" onclick="novaEdicao();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a>
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
