<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produtoEdicao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>

<script language="javascript" type="text/javascript">

function pesquisarEdicoes() {

	var codigoProduto = $("#pCodigoProduto").val();
	var nomeProduto = $("#pNomeProduto").val();
	var dataLancamentoDe = $("#pDateLanctoDe").val();	
	var precoDe = $("#pPrecoDe").val();
	var precoAte = $("#pPrecoAte").val();
	var dataLancamentoAte = $("#pDateLanctoAte").val();
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
			     {name:'dataLancamentoDe', value: dataLancamentoDe },
			     {name:'dataLancamentoAte', value: dataLancamentoAte },
			     {name:'precoDe', value: precoDe },
			     {name:'precoAte', value: precoAte },
			     {name:'situacaoLancamento', value: situacaoLancamento },
			     {name:'codigoDeBarras', value: codigoDeBarras },
			     {name:'brinde', value : brinde }],
		newp: 1,
	});
	
	$(".edicoesGrid").flexReload();
}


function executarPreProcessamento(resultado) {

	// Exibe mensagem de erro/alerta, se houver:
	var mensagens = (resultado.mensagens) ? resultado.mensagens : resultado.result;   
	var tipoMensagem = (mensagens && mensagens.tipoMensagem) ? mensagens.tipoMensagem : null; 
	var listaMensagens = (mensagens && mensagens.listaMensagens) ? mensagens.listaMensagens : null;
	if (tipoMensagem && listaMensagens) {
		exibirMensagem(tipoMensagem, listaMensagens);
		return;
	}
	
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
		if(row.cell.nomeProduto){
			nProduto = row.cell.nomeProduto;
		}else{
			row.cell.nomeProduto = '';
		}
		cProduto = row.cell.codigoProduto;
	});
		
	$(".grids").show();

	//
	var txt = '';
	if (nProduto != null || cProduto != null) {
		txt = ": " + cProduto + " - " + nProduto;
	}
	$("#labelNomeProduto").html(txt);
	$("#codigoProduto").val(cProduto);
	
	return resultado;
}

function salvaUmaEdicao() {
	
	$("#formUpload").ajaxSubmit({
		success: function(responseText, statusText, xhr, $form)  { 
			var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
			var tipoMensagem = mensagens.tipoMensagem;
			var listaMensagens = mensagens.listaMensagens;
			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
			}
			
			prepararTela(null);
			carregarDialog(null);
		}, 
		url: "<c:url value='/cadastro/edicao/salvar' />",
		type: 'POST',
		dataType: 'json',
		data: { codigoProduto : $("#codigoProduto").val() }
	});

}

function novaEdicao() {
	popup(null);
};

function editarEdicao(id) {
	popup(id);
}

function prepararTela(id) {
	
	// limpar os campos:
	form_clear('formUpload');
	carregarImagemCapa(id);
	$('#formUpload').find(':input').each(
		function() {
			switch(this.type) {
			case 'text':
			case 'textarea':
			case 'password':
				$(this).attr("readonly", true);
				break;
			
			case 'file':
			case 'select-multiple':
			case 'select-one':
			case 'checkbox':
			case 'radio':
				$(this).attr("disabled", true);
				break;
			}
		}
	);
	
	
	// Popular a lista de Edições:
	$(".prodsPesqGrid").flexOptions({
		url: "<c:url value='/cadastro/edicao/ultimasEdicoes.json' />",
		params: [{name:'codigoProduto', value: $("#codigoProduto").val() }],
		newp: 1,
	});

	$(".prodsPesqGrid").flexReload();	
}

function carregarDialog(id) {
	
	// Exibir os dados do Produto:
	$.postJSON(
		"<c:url value='/cadastro/edicao/carregarDadosProdutoEdicao.json' />",
		{ codigoProduto : $("#codigoProduto").val(), 
		  idProdutoEdicao : id},
		function(result) {
			$("#tabSegmentacao").show();	
			if (result) {
				$("#idProdutoEdicao").val(result.id);
				$("#codigoProdutoEdicao").val(result.codigoProduto);
				$("#nomePublicacao").val(result.nomeProduto);
				$("#nomeComercialProduto").val(result.nomeComercialProduto);
				$("#nomeFornecedor").val(result.nomeFornecedor);
				$("#situacao").val(result.situacao);
				$("#numeroEdicao").val(result.numeroEdicao);
				$("#fase").val(result.fase);
				$("#numeroLancamento").val(result.numeroLancamento);
				$("#pacotePadrao").val(result.pacotePadrao);
				$("#tipoLancamento").val(result.tipoLancamento);
				$("#precoPrevisto").val(result.precoPrevisto);
				$("#precoVenda").val(result.precoVenda);
				$("#dataLancamentoPrevisto").val(result.dataLancamentoPrevisto == undefined ? '' : result.dataLancamentoPrevisto.$);
				$("#dataLancamento").val(result.dataLancamento == undefined ? '' : result.dataLancamento.$);
				$("#repartePrevisto").val(result.repartePrevisto)
				$("#expectativaVenda").val(result.expectativaVenda);
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
				$('#possuiBrinde').attr('checked', result.possuiBrinde).change();
				$('#boletimInformativo').val(result.boletimInformativo);
				
				$('#dataRecolhimentoPrevisto').val(result.dataRecolhimentoPrevisto == undefined ? '' : result.dataRecolhimentoPrevisto.$).attr("readonly", false);
				$('#semanaRecolhimento').val(result.semanaRecolhimento);
				$('#dataRecolhimentoReal').val(result.dataRecolhimentoReal == undefined ? '' : result.dataRecolhimentoReal.$);
				$("#ped").val(result.ped).attr("readonly", false);		
				$("#descricaoProduto").val(result.descricaoProduto).attr("readonly", false);
				$("#descricaoBrinde").val(result.descricaoBrinde).attr("readonly", false);
				
				if (result.origemInterface) {
					$("#precoVenda").attr("readonly", false);	
				} else {
					$("#tabSegmentacao").hide();	
					$("#codigoProdutoEdicao").attr("readonly", false);
					$("#nomeComercialProduto").attr("readonly", false);
					$("#numeroEdicao").attr("readonly", (result.numeroEdicao == 1));
					$("#pacotePadrao").attr("readonly", false);
					$("#tipoLancamento").attr("disabled", false);
					$("#precoPrevisto").attr("readonly", false);
					$("#dataLancamentoPrevisto").attr("readonly", false);
					$("#repartePrevisto").attr("readonly", false);
					$("#repartePromocional").attr("readonly", false);
					$("#codigoDeBarrasCorporativo").attr("readonly", false);
					$('#parcial').attr("disabled", false);
					$("#desconto").attr("readonly", false);
					$("#largura").attr("readonly", false);
					$("#comprimento").attr("readonly", false);
					$("#espessura").attr("readonly", false);
					$('#boletimInformativo').attr("readonly", false);
				}

				$("#numeroLancamento").attr("readonly", false); 
				$("#imagemCapa").attr("disabled", false);
				$("#codigoDeBarras").attr("readonly", false);				
				$("#chamadaCapa").attr("readonly", false);
				$('#possuiBrinde').attr("disabled", false);
				$("#peso").attr("readonly", false);
			}
		},
		function(result) { 
			exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "");
			},
		true
	);

}

function popup(id) {
	
	if ($("#pCodigoProduto").val() != $("#codigoProduto").val()){
		$("#codigoProduto").val($("#pCodigoProduto").val());
	}
	
	//if ($(".edicoesGrid > tbody").data() == null || $(".edicoesGrid > tbody").data() == undefined) {
	if ($("#codigoProduto").val() == "") {
		exibirMensagem('WARNING', ['Por favor, escolha um produto para adicionar a Edi&ccedil;&atilde;o!'], "");
		return;
	}
	
	pesquisarEdicoes();
	prepararTela(id);
	carregarDialog(id);

	$( "#dialog-novo" ).dialog({
		resizable: false,
		height:615,
		width:960,
		modal: true,
		buttons: {
			"Confirmar": function() {

				$("#formUpload").ajaxSubmit({
					beforeSubmit: function(arr, formData, options) {
						// Incluir aqui as validacoes;
					},
					success: function(responseText, statusText, xhr, $form)  { 
						var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
						var tipoMensagem = mensagens.tipoMensagem;
						var listaMensagens = mensagens.listaMensagens;
						if (tipoMensagem && listaMensagens) {
							//exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');

							$("#dialog-novo").dialog( "close" );
							pesquisarEdicoes();
							exibirMensagem(tipoMensagem, listaMensagens);
						}
					}, 
					url: "<c:url value='/cadastro/edicao/salvar' />",
					type: 'POST',
					dataType: 'json',
					data: { codigoProduto : $("#codigoProduto").val() }
				});
			},
			"Cancelar": function() {
				$("#dialog-novo").dialog( "close" );
			}
		}
	});
};

function carregarImagemCapa(idProdutoEdicao) {

	var imgPath = (idProdutoEdicao == null || idProdutoEdicao == undefined)
		? "" : "<c:url value='/capa/' />" + idProdutoEdicao + '?' + Math.random(); 
	var img = $("<img />").attr('src', imgPath).attr('width', '144').attr('height', '185').attr('alt', 'Capa');
	$("#div_imagem_capa").empty();
	$("#div_imagem_capa").append(img);
	
	img.load(function() {
		if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
			$("#div_imagem_capa").append(img);
		}
	});

}



function form_clear(formName) {

	$('#' + formName).find(':input').each(
		function() {
			switch(this.type) {
				case 'hidden': 
				case 'text':
				case 'textarea':
				case 'password':
				case 'select-multiple':
				case 'select-one':
				case 'file':
					$(this).val('');
					break;

				case 'checkbox':
				case 'radio':
					this.checked = false;
					break;
			}
		}
	);
};



	
	function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:615,
			width:960,
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
							
							exibirMensagem(tipoMensagem, listaMensagens);
						}

						carregarImagemCapa(null);
					},
					function(result) {
				   		$("#dialog-excluir").dialog("close");
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagem(tipoMensagem, listaMensagens);
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
					$.postJSON(
						"<c:url value='/capa/removerCapa' />",
						{idProdutoEdicao : $("#idProdutoEdicao").val()},
						function(result) {
							$( "#dialog-excluir-capa" ).dialog( "close" );
							
							var mensagens = (result.mensagens) ? result.mensagens : result;
							var tipoMensagem = mensagens.tipoMensagem;
							var listaMensagens = mensagens.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
								if (tipoMensagem == "SUCCESS") { 
									$("#div_imagem_capa").empty();
								}
							}
						},
						function(result) {
							$( "#dialog-excluir-capa" ).dialog( "close" );

							var mensagens = (result.mensagens) ? result.mensagens : result.result;
							var tipoMensagem = mensagens.tipoMensagem;
							var listaMensagens = mensagens.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
							}
						},
						true
					);
				},
				"Cancelar": function() {
					$( "#dialog-excluir-capa" ).dialog( "close" );
				}
			}
		});
	};
	
	
	$(function() {
			$( "#tabEdicoes" ).tabs();
	});
	
	$(function() {
		$( "#pDateLanctoDe,#pDateLanctoAte" ).datepicker({
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

<script type="text/javascript">

$(function() {
	$("#numeroEdicao").numeric();
	$("#pacotePadrao").numeric();
	$("#repartePrevisto").numeric();
	$("#expectativaVenda").numeric();
	$("#repartePromocional").numeric();
	$("#precoPrevisto").numeric();
	$("#precoVenda").numeric();
	$("#desconto").numeric();
	$("#peso").numeric();
	$("#largura").numeric();
	$("#comprimento").numeric();
	$("#espessura").numeric();
	$("#numeroLancamento").numeric();
	$("#ped").numeric();
	

	$("#dataLancamentoPrevisto").mask("99/99/9999");
	$("#dataRecolhimentoPrevisto").mask("99/99/9999");
	$("#dataRecolhimentoReal").mask("99/99/9999");
	$("#dataLancamento").mask("99/99/9999");
	
	$('#possuiBrinde').change(function(){
		if($(this).attr('checked')){
			$('.descBrinde').show();
		}else{
			$('.descBrinde').hide();
		}
	});
	

});
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
	<p><input type="file" size="30" id="file01" name="file01" /></p>
</div>

<div id="dialog-excluir-capa" title="Excluir Capa" style="display:none;">
	<p>Confirma a exclus&atilde;o desta Capa?</p>
</div>

<div id="dialog-excluir" title="Excluir Edi&ccedil;&atilde;o">
	<p>Confirma a exclus&atilde;o desta Edi&ccedil;&atilde;o?</p>
</div>



<div id="dialog-novo" title="Incluir Nova Edi&ccedil;&atilde;o">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialogMensagemNovo" name="messageDialog"/>
	</jsp:include> 
	
	<form id="formUpload" name="formUpload" method="post" enctype="multipart/form-data" >
		<div id="tabEdicoes">
			<ul>
				<li><a id="tabIdentificacao" href="#tabEdicoes-1">Identifica&ccedil;&atilde;o</a></li>
				<li><a id="tabCaractLancto" href="#tabEdicoes-2">Caracter&iacute;sticas do Lan&ccedil;amento</a></li>
				<li><a id="tabSegmentacao" href="#tabEdicoes-3">Segmenta&ccedil;&atilde;o</a></li>
			</ul>
			
			<div id="tabEdicoes-1">
				<input type="hidden" id="idProdutoEdicao" name="idProdutoEdicao" />
				<div class="ldPesq">
					<fieldset id="pesqProdutos" style="width:200px!important;">
						<legend>Produtos Pesquisados</legend>
						<table class="prodsPesqGrid"></table>
					</fieldset>
					
					<span class="bt_add"><a href="javascript:;" onclick="salvaUmaEdicao();">Incluir Novo</a></span>
				</div>
				
				<div class="ldForm">
					<fieldset style="width:655px!important; margin-bottom:5px;">
						<legend>Identifica&ccedil;&atilde;o</legend>
						<table width="648" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="181"><strong>C&oacute;digo:</strong></td>
									<td width="100" colspan="3"><input type="text" name="codigoProdutoEdicao" id="codigoProdutoEdicao" style="width:100px;" /></td>
									
									<td width="90">&nbsp;</td>
									<td width="108">&nbsp;</td>
									<td width="153" rowspan="8" align="center">
										
										<div id="div_imagem_capa">
											<img alt="Capa" src="" width="144" height="185" />
										</div>
										
										<br clear="all" />
										<a href="javascript:;" onclick="popup_excluir_capa();">
											<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir Capa" width="15" height="15" hspace="5" vspace="3" border="0" />
										</a>
									</td>
								</tr>
								<tr>
									<td><strong>Nome Publica&ccedil;&atilde;o:</strong></td>
									<td colspan="5"><input type="text" name="nomePublicacao" id="nomePublicacao" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td><strong>Nome Comercial Produto:</strong></td>
									<td colspan="5"><input type="text" name="nomeComercialProduto" id="nomeComercialProduto" style="width:340px;" /></td>
								</tr>
								<tr>
									<td><strong>Fornecedor:</strong></td>
									<td colspan="5"><input type="text" name="nomeFornecedor" id="nomeFornecedor" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td><strong>Situa&ccedil;&atilde;o:</strong></td>
									<td colspan="5"><input type="text" name="situacao" id="situacao" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td><strong>Edi&ccedil;&atilde;o:</strong></td>
									<td><input type="text" name="numeroEdicao" id="numeroEdicao" style="width:50px;" /></td>
									<td><strong>PED:</strong></td>
									<td><input type="text" name="ped" id="ped" style="width:50px;" /></td>
									<td><strong>Pct. Padr&atilde;o:</strong></td>
									<td><input type="text" name="pacotePadrao" id="pacotePadrao" style="width:50px;" /></td>
								</tr>
							
								<tr>
									<td><strong>Tipo de Lan&ccedil;amento:</strong></td>
									<td colspan="3">
										<select name="tipoLancamento" id="tipoLancamento" style="width:160px;" >
											<option value="">Selecione...</option>
											<option value="LANCAMENTO">Lan&ccedil;amento</option>
											<option value="PARCIAL">Ed. Parcial</option>
											<option value="RELANCAMENTO">Relan&ccedil;amento</option>
											<option value="REDISTRIBUICAO">Redistribui&ccedil;&atilde;o</option>
											<option value="SUPLEMENTAR">Supl. Compuls</option>
										</select>
									</td>
									<td><strong>N&ordm; Lancto:</strong></td>
									<td><input type="text" name="numeroLancamento" id="numeroLancamento" style="width:50px;" maxlength="9" /></td>
								</tr>
								<tr>
									<td><strong>Capa da Edi&ccedil;&atilde;o:</strong></td>
									<td colspan="5"><input type="file" name="imagemCapa" id="imagemCapa" style="width:340px;" /></td>
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
									<td><strong>Exp. Venda(%):</strong></td>
									<td><input type="text" name="expectativaVenda" id="expectativaVenda" style="width:80px;" disabled="disabled" /></td>
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
									<td width="70"><input type="text" name="precoVenda" id="precoVenda" style="width:70px; text-align:right;" /></td>
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
				    <fieldset style="width: 630px !important; margin-bottom: 2px; float: left;">
				     <legend>Data Recolhimento</legend>
					   	<table border="0" cellSpacing="1" cellPadding="1" width="562">
					      <tbody><tr>
					        <td width="60">Previsto:</td>
					        <td width="91"><input style="width: 70px; float: left;" id="dataRecolhimentoPrevisto" name="dataRecolhimentoPrevisto" type="text"></td>
					        <td width="48" align="right">Real:</td>
					        <td width="79"><input style="width: 70px; text-align: right;" id="dataRecolhimentoReal" disabled="disabled" name="dataRecolhimentoReal" type="text"></td>
					        <td width="180" align="right">Semana de Recolhimento:</td>
					        <td width="85"><input style="width: 70px; float: left;" id="semanaRecolhimento" disabled="disabled" name="semanaRecolhimento" type="text"></td>
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
					
					<span class="bt_add"><a href="javascript:;" onclick="salvaUmaEdicao();">Incluir Novo</a></span>
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
									<td colspan="2"><input type="text" name="descricaoDesconto" id="descricaoDesconto" style="width:235px;" /></td>
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
									<td width="59">Descri&ccedil;&atilde;o Produto:</td>
									<td width="86"><input type="text" name="descricaoProduto" id="descricaoProduto" style="width:80px;" /></td>
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
								<tr class="descBrinde" style="display:none;">
						       	    <td height="24">Descri&ccedil;&atilde;o Brinde:</td>
						       	    <td><input type="text" name="descricaoBrinde" id="descricaoBrinde" style="width:190px;" /></td>
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
					
					<span class="bt_add"><a href="javascript:;" onclick="salvaUmaEdicao();">Incluir Novo</a></span>
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
	</form>
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
			<input type="hidden" id="codigoProduto" name="codigoProduto" value="" />
			<legend>Pesquisar Produto</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<thead/>
				<tbody>
					<tr>
						<td width="74">C&oacute;digo:</td>
						<td width="81">
							<input type="text" name="pCodigoProduto" id="pCodigoProduto" maxlength="255" 
									style="width:80px;" 
									onchange="produtoEdicao.pesquisarPorCodigoProduto('#pCodigoProduto', '#pNomeProduto', false,
											undefined,
											undefined);" />
						</td>
						<td width="48">Produto:</td>
						<td width="167">
							<input type="text" name="pNomeProduto" id="pNomeProduto" maxlength="255" 
									style="width:160px;"
									onkeyup="produtoEdicao.autoCompletarPorNomeProduto('#pNomeProduto', false);"
									onblur="produtoEdicao.pesquisarPorNomeProduto('#pCodigoProduto', '#pNomeProduto', false,
										undefined,
										undefined);" />
						</td>
						<td width="86">Per&iacute;odo Lcto:</td>
		                <td width="103"><input type="text" name="pDateLanctoDe" id="pDateLanctoDe" style="width:80px;"/></td>
		                <td width="22">At&eacute;:</td>
		                <td width="108"><input type="text" name="pDateLanctoAte" id="pDateLanctoAte" style="width:80px;"/></td>
						<td width="20">&nbsp;</td>
						<td width="52">Situa&ccedil;&atilde;o:</td>
						<td width="133">
							<select name="select" id="pSituacaoLancamento" name="pSituacaoLancamento" style="width:130px;">
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
					</tr>
					<tr>
						<td>C&oacute;d. Barras:</td>
						<td colspan="3" ><input type="text" name="pCodigoDeBarras" id="pCodigoDeBarras" style="width:300px;"/></td>						
						<td>Pre&ccedil;o (R$) de:</td>
		                <td><input type="text" name="pPrecoDe" id="pPrecoDe" style="width:80px; text-align:right;"/></td>
		                <td>At&eacute;:</td>
		                <td><input type="text" name="pPrecoAte" id="pPrecoAte" style="width:80px;text-align:right;"/></td>
						<td align="right"><input type="checkbox" name="pBrinde" id="pBrinde" value=""/></td>
						<td><label for="pBrinde">Brinde</label></td>
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
			height : 120,
			singleSelect : true
		});

	$(".prodsPesqGrid").flexigrid({
			dataType : 'json',
			preProcess: executarPreProcessamento,
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
			height : 350,
			singleSelect : true
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
			height : 255,
			singleSelect : true
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
			height : 120,
			singleSelect : true
		});
</script>
</body>
</html>
