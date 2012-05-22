<head>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<title>Conferencia Encalhe</title>

	<script type="text/javascript" src='<c:url value="/"/>scripts/produto.js'></script>

	<script type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

	<script type="text/javascript" src='<c:url value="/"/>/scripts/shortcut.js'></script>

	<script type="text/javascript">
		
		var modalAberta = false;
		
		var idProdutoEdicao = "";
		
		var idConferenciaEncalheVisualizacao = "";
	
		var ConferenciaEncalhe = {
			
			pesquisarCota : function() {
				
				var data = [{name : 'numeroCota', value : jQuery("#numeroCota").val()}];
				
				$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/verificarReabertura'/>", data,
					function(result){
						
						if (result.listaMensagens && result.listaMensagens[0] == "REABERTURA"){
							
							modalAberta = true;
							
							$("#dialog-reabertura").dialog({
								resizable : false,
								height : 200,
								width : 360,
								modal : true,
								buttons : {
									"Sim" : function() {
										
										ConferenciaEncalhe.carregarListaConferencia(data);
										ConferenciaEncalhe.popup_alert();
									},
									"Não" : function() {
										$("#dialog-reabertura").dialog("close");
									}
								}, close : function(){
									
									modalAberta = false;
								}
							});
						} else {
							
							ConferenciaEncalhe.carregarListaConferencia(data);
							ConferenciaEncalhe.popup_alert();
						}
					}
				);
			},
			/*
			carregarDadosCota : function(data){
				$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/pesquisarCota'/>", 
						data,
						function(result){
						
							if (result){
								
								$(".dadosFiltro").show();
								
								$("#nomeCota").text(result[0]);
								$("#statusCota").text(result[1]);
								
								$("#dialog-reabertura").dialog("close");
								
								modalAberta = false;
								
								popup_alert();
							}
						},
						function (){
							
							$(".dadosFiltro").hide();
							$("#numeroCota").focus();
						},
						false,
						"idTelaConferenciaEncalhe"
					);
			},
			*/
			carregarListaConferencia : function(data){
				
				$(".conferenciaEncalheGrid").flexOptions({
					url : '<c:url value="/devolucao/conferenciaEncalhe/carregarListaConferencia"/>',
					preProcess : ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe,
					params: data,
					dataType : 'json'
				});
				
				$(".conferenciaEncalheGrid").flexReload();
				
				$("#cod_barras").focus();
			},
	
			preProcessarConsultaConferenciaEncalhe : function(result) {
				
				if (result.mensagens){
					
					exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
					return;
				}
				
				var modeloConferenciaEncalhe = result[0];
	
				$.each(modeloConferenciaEncalhe.rows, 
					function(index, value) {
						
						var valorExemplares = parseInt(value.cell.qtdExemplar);
						
						var inputExemplares = '<input name="qtdExemplaresGrid" onchange="ConferenciaEncalhe.recalcularValores(this);" style="width:50px;" value="' + valorExemplares + '"/>' +
							'<input name="idConferenciaEncalheHidden" type="hidden" value="' + value.cell.idConferenciaEncalhe + '"/>';
						
						value.cell.qtdExemplar = inputExemplares;
						
						value.cell.desconto = parseFloat(value.cell.desconto).toFixed(2);
						
						value.cell.valorTotal = parseFloat(value.cell.valorTotal).toFixed(2);
						
						if (value.cell.dia > 0){
						
							value.cell.dia = value.cell.dia + "º";
						} else {
							
							value.cell.dia = value.cell.dataRecolhimento.$;
						}
						
						var inputCheckBoxJuramentada = 
							'<input type="checkbox" ' + (value.cell.juramentada == "true" ? 'checked="checked"' : '')
								+ (!value.cell.juramentada ? 'disabled="disabled"' : '')
								+ ' name="checkGroupJuramentada" style="align: center;"/>';
						
						value.cell.juramentada = inputCheckBoxJuramentada;
						
						var imgDetalhar = '<img src="' + contextPath + '/images/ico_detalhes.png" border="0" hspace="3"/>';
						value.cell.detalhes = '<a href="javascript:;" onclick="ConferenciaEncalhe.exibirDetalhesConferencia(' + value.cell.idConferenciaEncalhe + ');">' + imgDetalhar + '</a>';
						
						var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
						value.cell.acao = '<a href="javascript:;" onclick="ConferenciaEncalhe.excluirConferencia(' + value.cell.idConferenciaEncalhe + ');">' + imgExclusao + '</a>';
					}
				);
				
				$(".outrosVlrsGrid").flexAddData({
					page: result[1].page, total: result[1].total, rows: result[1].rows
				});
				
				$("#totalReparte").text(parseFloat(result[2]).toFixed(2));
				$("#totalEncalhe").text(parseFloat(result[3]).toFixed(2));
				$("#valorVendaDia").text(parseFloat(result[4]).toFixed(2));
				$("#totalOutrosValores").text(parseFloat(result[5]).toFixed(2));
				$("#valorAPagar").text(parseFloat(result[6]).toFixed(2));
				
				$(".dadosFiltro").show();
				$("#nomeCota").text(result[7]);
				$("#statusCota").text(result[8]);
				
				$("#dialog-reabertura").dialog("close");
				
				modalAberta = false;
	
				return modeloConferenciaEncalhe;
			},
			
			finaliazarConferenciaPreProcess : function(result){
				
				if (result.mensagens){
					
					exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
					return;
				}
				
				var notaFiscal = result[9];
				
				if (notaFiscal){
				
					$("#numeroNotaFiscalExibir").text(notaFiscal.numero);
					$("#serieExibir").text(notaFiscal.serie);
					$("#dataExibir").text(notaFiscal.dataEmissao);
					$("#valorTotalNotaFiscalExibir").text(parseFloat(notaFiscal.valorProdutos).toFixed(2));
					$("#chaveAcessoExibir").text(notaFiscal.chaveAcesso);
				}
				
				var modeloConferenciaEncalhe = result[0];
				
				$.each(modeloConferenciaEncalhe.rows, 
					function(index, value) {
					
						value.cell.codigo = value.cell.codigo + 
							'<input name="idConferenciaEncalheHiddenFinalizarConf" type="hidden" value="' + value.cell.idConferenciaEncalhe + '"/>';
					
						if (value.cell.dia > 0){
							
							value.cell.dia = value.cell.dia + "º";
						} else {
							
							value.cell.dia = value.cell.dataRecolhimento.$;
						}
						
						value.cell.qtdRecebida = parseInt(value.cell.qtdRecebida);
					
						value.cell.qtdInformada = 
							'<input name="qtdeInformadaFinalizarConf" onchange="ConferenciaEncalhe.recalcularValoresFinalizar(this);" type="text" style="width:50px;" value="' + parseInt(value.cell.qtdExemplar) + '"/>';
						
						value.cell.precoCapa = 
							'<input name="precoCapaFinalizarConf" onchange="ConferenciaEncalhe.recalcularValoresFinalizar(this);" style="width:50px;" value="' + parseFloat(value.cell.precoCapa).toFixed(2) + '"/>';
						
						value.cell.desconto = parseFloat(value.cell.desconto).toFixed(2);
						
						value.cell.valorTotal = parseFloat(value.cell.valorTotal).toFixed(2);
						
						var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
						value.cell.acao = '<a href="javascript:;" onclick="ConferenciaEncalhe.excluirConferencia(' + value.cell.idConferenciaEncalhe + ');">' + imgExclusao + '</a>';
					}
				);
				
				$("#somatorioQtdInformada").text(parseInt(result[10]));
				$("#somatorioQtdRecebida").text(parseInt(result[11]));
				$("#somatorioTotal").text(parseFloat(result[6]).toFixed(2));
				
				modalAberta = true;
				
				$("#dialog-dadosNotaFiscal").dialog({
					resizable : false,
					height : 'auto',
					width : 860,
					modal : true,
					buttons : {
						"Confirmar" : function() {
							
							$.postJSON('<c:url value="/devolucao/conferenciaEncalhe/finalizarConferencia"/>', null,
								function(result){
									
									exibirMensagem(result.tipoMensagem, result.listaMensagens);
									
									$("#dialog-dadosNotaFiscal").dialog("close");
								}, null, true, "idModalDadosNotaFiscal"
							);
						},
						"Cancelar" : function() {
							
							$(this).dialog("close");
						}
					}, close : function(){
						
						modalAberta = false;
					}
				});
				
				return modeloConferenciaEncalhe;
			},
			
			recalcularValores : function(campo){
				
				if (campo && campo.value == ""){
					
					campo.value = 0;
				}
				
				var qtdsExemplaresInput = $('input[name="qtdExemplaresGrid"]');
				var checksJuramentada = $('input[name="checkGroupJuramentada"]');
				
				var data = [];
				
				$.each($('input[name="idConferenciaEncalheHidden"]'), function(index, value){
					
					data.push({name:'idsConferencia['+ index +']', value: value.value});
					data.push({name:'qtdsExemplares['+ index +']', value: qtdsExemplaresInput[index].value});
					data.push({name:'juramentada['+ index +']', value: checksJuramentada[index].checked});
				});
				
				$(".conferenciaEncalheGrid").flexOptions({			
					url : "<c:url value='/devolucao/conferenciaEncalhe/recalcularConferencia'/>",
					dataType : 'json',
					preProcess: ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe,
					params: data
				});
				
				$(".conferenciaEncalheGrid").flexReload();
			},
			
			recalcularValoresFinalizar : function(campo){
				
				if (campo && campo.value == ""){
					
					campo.value = 0;
				}
				
				var qtdsExemplaresInput = $('input[name="qtdeInformadaFinalizarConf"]');
				var valoresCapaInput = $('input[name="precoCapaFinalizarConf"]');
				
				var data = [];
				
				$.each($('input[name="idConferenciaEncalheHiddenFinalizarConf"]'), function(index, value){
					
					data.push({name:'idsConferencia['+ index +']', value: value.value});
					data.push({name:'qtdsExemplares['+ index +']', value: qtdsExemplaresInput[index].value});
					data.push({name:'valoresCapa['+ index +']', value : valoresCapaInput[index].value});
				});
				
				$(".pesqProdutosNotaGrid").flexOptions({			
					url : "<c:url value='/devolucao/conferenciaEncalhe/recalcularConferencia'/>",
					dataType : 'json',
					preProcess: ConferenciaEncalhe.finaliazarConferenciaPreProcess,
					params: data
				});
				
				$(".pesqProdutosNotaGrid").flexReload();
			},
			
			exibirDetalhesConferencia : function(idConferenciaEncalhe){
				
				idConferenciaEncalheVisualizacao = idConferenciaEncalhe;
				
				var data = [{name: "idConferenciaEncalhe", value: idConferenciaEncalhe}];
				
				$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/buscarDetalhesProduto'/>", data,
					function (result){
						
						$("#nomeProdutoDetalhe").text(result.nomeProduto);
						$("#precoCapaDetalhe").text(result.precoCapa);
						$("#chamadaCapa").text(result.chamadaCapa);
						$("#fornecedor").text(result.nomeFornecedor);
						$("#brinde").text(result.possuiBrinde == "true" ? "Sim" : "Não");
						$("#editor").text(result.nomeEditor);
						$("#pacotePadrao").text(result.pacotePadrao);
						//TODO ???
						$("#imagemProduto").attr("src", "");
						
						$("#precoDesconto").text((parseFloat(result.precoCapa) - parseFloat(result.desconto)).toFixed(2));
						
						$("#observacaoReadOnly").text(result.observacao ? result.observacao : "");
						$("#observacao").val(result.observacao ? result.observacao : "");
						$("#observacaoReadOnly").show();
						$("#observacao").show();
						$("#btObs").show();
						
						ConferenciaEncalhe.popup_detalhe_publicacao();
					}
				);
			},
			
			excluirConferencia : function(idConferenciaEncalhe){
				
				modalAberta = true;
				
				$("#dialog-excluir-conferencia").dialog({
					resizable : false,
					height : 180,
					width : 460,
					modal : true,
					buttons : {
						"Confirmar" : function() {
							
							var data = [{name : "idConferenciaEncalhe", value : idConferenciaEncalhe}];
							
							$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/excluirConferencia'/>", data,
								function (result){
									
									$(".conferenciaEncalheGrid").flexReload();
									$(".pesqProdutosNotaGrid").flexReload();
									$("#dialog-excluir-conferencia").dialog("close");
								}
							);
						},
						"Cancelar" : function() {
							
							$(this).dialog("close");
							$('#pesq_cota').focus();
						}
					}, close : function(){
						
						modalAberta = false;
					}
				});
			},
			
			popup_logado : function(){
				
				modalAberta = true;
				
				$("#dialog-logado").dialog({
					resizable : false,
					height : 180,
					width : 460,
					modal : true,
					buttons : {
						"Confirmar" : function() {
							
							$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/salvarIdBoxSessao'/>", "idBox=" + $("#boxLogado").val(), 
								function(){
									
									$("#dialog-logado").dialog("close");
									$('#numeroCota').focus();
								}
							);
						},
						"Cancelar" : function() {
							$(this).dialog("close");
							$('#pesq_cota').focus();
						}
					}, open : function(){
						
						$("#boxLogado").focus();
					}, close : function(){
						
						modalAberta = false;
					}
				});
			},
			
			setarValoresPesquisados : function(result){
				
				if (ultimoCodeBar && ultimoCodeBar != result.codigoDeBarras){
				
					$(".conferenciaEncalheGrid").flexReload();
				}
				
				ultimoCodeBar = result.codigoDeBarras;
				ultimoSM = result.codigoSM;
				ultimoCodigo = result.idProdutoEdicao;
				
				$("#cod_barras").val(result.codigoDeBarras);
				$("#sm").val(result.codigoSM);
				$("#codProduto").val(result.idProdutoEdicao);
				
				$("#nomeProduto").text(result.nomeProduto);
				$("#edicaoProduto").text(result.numeroEdicao);
				$("#precoCapa").text(result.precoCapa);
				$("#desconto").text(result.desconto);
				
				$("#valorTotal").text(((parseFloat(result.precoCapa) - parseFloat(result.desconto)) * parseFloat(result.qtdExemplar)).toFixed(2));
				
				$("#qtdeExemplar").val(parseInt(result.qtdExemplar));
			},
			
			adicionarProdutoConferido : function(){
				
				var data = [{name: "quantidade", value: $("#qtdeExemplar").val()}, 
				            {name: "idProdutoEdicao", value: $("#codProduto").val()}];
				
				$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/adicionarProdutoConferido'/>", data,
					function(result){
						
						$(".conferenciaEncalheGrid").flexReload();
						
						ConferenciaEncalhe.limparDadosProduto();
						
						$("#cod_barras").focus();
					}
				);
			},
			
			limparDadosProduto : function(){
				
				$("#qtdeExemplar").val("");
				$("#cod_barras").val("");
				$("#sm").val("");
				$("#codProduto").val("");
				
				$("#nomeProduto").text("");
				$("#edicaoProduto").text("");
				$("#precoCapa").text("");
				$("#desconto").text("");
				$("#valorTotal").text("");
				
				ultimoCodeBar = "";
				ultimoSM = "";
				ultimoCodigo = "";
			},
		
			popup_alert : function() {
				
				modalAberta = true;
				
				$("#dialog-alert").dialog({
					resizable : false,
					height : 190,
					width : 460,
					modal : true,
					buttons : {
						"Sim" : function() {
							
							$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/carregarNotaFiscal'/>", null, 
								function(result){
									
									$("#numNotaFiscal").val(result.numero);
									$("#serieNotaFiscal").val(result.serie);
									$("#dataNotaFiscal").val(result.dataEmissao);
								    $("#chaveAcessoNFE").val(result.chaveAcesso);
									
								    if (result.valorProdutos){
									    
								    	$("#valorNotaFiscal").val(parseFloat(result.valorProdutos).toFixed(2));
										$("#vlrCE").val(parseFloat(result.valorProdutos).toFixed(2));
								    }
									
									$("#dialog-alert").dialog("close");
									ConferenciaEncalhe.popup_notaFiscal();
								}
							);
						},
						"Não" : function() {
							
							$("#dialog-alert").dialog("close");
							$("#vlrCE").focus();
						}
					}, open : function(){
						
						$(this).parent('div').find('button:contains("Sim")').focus();
					}, close : function(){
						
						modalAberta = false;
					}
				});
				
				$("#dialog-alert").show();
			},
		
			popup_notaFiscal : function() {
				
				modalAberta = true;
				
				$("#dialog-notaFiscal").dialog({
					resizable : false,
					height : 360,
					width : 750,
					modal : true,
					buttons : {
						"Confirmar" : function() {
							
							var data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal").val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal").val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal").val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal").val()},
								{name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE").val()}
							];
							
							$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/salvarNotaFiscal'/>", data, 
								function(result){
									
									$("#dialog-notaFiscal").dialog("close");
									
									$("#vlrCE").val(parseFloat($("#valorNotaFiscal").val()).toFixed(2));
								}
							);
						},
						"Cancelar" : function() {
							
							$(this).dialog("close");
						}
					}, open : function(){
						
						$("#numNotaFiscal").focus();
					}, close : function(){
						
						modalAberta = false;
					}
				});
		
			},
		
			popup_dadosNotaFiscal : function(result) {
				
				ConferenciaEncalhe.finaliazarConferenciaPreProcess(result);
			},
		
			popup_pesquisar : function() {
				
				modalAberta = true;
				
				$("#dialog-pesquisar").dialog({
					resizable : false,
					height : 470,
					width : 560,
					modal : true,
					buttons : {
						"Confirmar" : function() {
							
							$(this).dialog("close");
						},
						"Cancelar" : function() {
							
							$(this).dialog("close");
						}
					}, close : function(){
						
						modalAberta = false;
					}
				});
		
			},
		
			popup_detalhe_publicacao : function() {
				
				modalAberta = true;
				
				$('#observacao').focus();
				
				$("#dialog-detalhe-publicacao").dialog({
					resizable : false,
					height : 420,
					width : 755,
					modal : true,
					buttons : {
						"Fechar" : function() {
							
							$(this).dialog("close");
						}
					}, close : function(){
						
						modalAberta = false;
					}
				});
		
			},
			
			gravaObs : function() {
				
				var data = [{name: "idConferenciaEncalhe", value: idConferenciaEncalheVisualizacao},
				            {name: "observacao", value: $("#observacao").val()}];
				
				$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/gravarObservacaoConferecnia'/>", data, 
					function(result){
					
						$("#observacao").fadeOut();
						$(".obs").fadeIn("slow");
						$(".tit").fadeOut("slow");
						$("#btObs").fadeOut("slow");
						
						$("#observacaoReadOnly").text($("#observacao").val());
					}
				);
			},
		
			popup_outros_valores : function() {
				
				modalAberta = true;
				
				$("#dialog-outros-valores").dialog({
					resizable : false,
					height : 430,
					width : 460,
					modal : true,
					buttons : {
						"Fechar" : function() {
							
							$(this).dialog("close");
						}
					}, close : function(){
						
						modalAberta = false;
					}
				});
		
			},
		
			popup_salvarInfos : function() {
				
				modalAberta = true;
				
				$("#dialog-salvar").dialog({
					resizable : false,
					height : 190,
					width : 460,
					modal : true,
					buttons : {
						"Confirmar" : function() {
							
							$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/salvarConferencia'/>", null,
								function(result){
									
									exibirMensagem(result.tipoMensagem, result.listaMensagens);
									
									$("#dialog-salvar").dialog("close");
								}
							);
						},
						"Cancelar" : function() {
							
							$(this).dialog("close");
						}
		
					}, close : function(){
						
						modalAberta = false;
					}
				});
		
			},
			
			mostrar_produtos : function() {
				
				var codigoNomeProduto = $("#pesq_prod").val();
				
				if (codigoNomeProduto && codigoNomeProduto.length > 0){
					$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome'/>", 
							"codigoNomeProduto=" + codigoNomeProduto, 
							function(result){
								
								if (result[0]){
									
									$("#pesq_prod").autocomplete({
										source: result,
										select: function(event, ui){
											
											$("#codProduto").val(ui.item.chave.string);
											idProdutoEdicao = ui.item.chave.long;
										}
									});
								}
							}
					);
				}
			},
		
			fechar_produtos : function() {
				$('.pesqProdutosGrid').keypress(function(e) {
					if (e.keyCode == 13) {
						$(".itensPesquisados").show();
					}
				});
			},
		
			pesqMostraCota : function() {
				$('#pesq_cota').keypress(function(e) {
					if (e.keyCode == 13) {
						$('.dadosFiltro').fadeIn('fast');
						popup_alert();
					}
				});
			},
	
			conferenciaEncalheGridModel : [   {
				display : 'Exemplares',
				name : 'qtdExemplar',
				width : 65,
				sortable : false,
				align : 'left'
			}, {
				display : 'Código de Barras',
				name : 'codigoDeBarras',
				width : 165,
				sortable : false,
				align : 'left'
			}, {
				display : 'SM',
				name : 'codigoSM',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : 'Código',
				name : 'codigo',
				width : 65,
				sortable : false,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 70,
				sortable : false,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 40,
				sortable : false,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 70,
				sortable : false,
				align : 'right'
			}, {
				display : 'Desconto R$',
				name : 'desconto',
				width : 50,
				sortable : false,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'valorTotal',
				width : 50,
				sortable : false,
				align : 'right'
			}, {
				display : 'Dia',
				name : 'dia',
				width : 20,
				sortable : false,
				align : 'center'
			}, {
				display : 'Juramentada',
				name : 'juramentada',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Detalhe',
				name : 'detalhes',
				width : 45,
				sortable : false,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : false,
				align : 'center'
			} ],
	
			pesqProdutosNotaGridModel :
			[  {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dia',
				name : 'dia',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde. Info',
				name : 'qtdInformada',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde. Recebida',
				name : 'qtdRecebida',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Desc R$',
				name : 'desconto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'valorTotal',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			} ],
	
			outrosVlrsGridModel : [ {
				display : 'Data',
				name : 'dataLancamento',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Lançamento',
				name : 'tipoLancamento',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 100,
				sortable : true,
				align : 'right'
			} ]
	
		};
		
		var ultimoCodeBar = "";
		var ultimoSM = "";
		var ultimoCodigo = "";
		
		$(function() {
	
			$('#qtdeExemplar').focus();
	
			$("#datepickerDe").datepicker({
				showOn : "button",
				buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly : true
			});
	
			$("#datepickerDe1").datepicker({
				showOn : "button",
				buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly : true
			});
	
			$(".conferenciaEncalheGrid").flexigrid({
				dataType : 'json',
				colModel : ConferenciaEncalhe.conferenciaEncalheGridModel,
				width : 960,
				height : 250,
				disableSelect: true
			});
	
			$(".pesqProdutosNotaGrid").flexigrid({
				dataType : 'json',
				colModel : ConferenciaEncalhe.pesqProdutosNotaGridModel,
				width : 810,
				height : 250,
				disableSelect: true
			});
	
			$(".outrosVlrsGrid").flexigrid({
				dataType : 'json',
				colModel : ConferenciaEncalhe.outrosVlrsGridModel,
				width : 400,
				height : 250,
				disableSelect: true
			});
	
			//$("#pesq_prod").autocomplete({source : ""});
			
			$("#numeroCota").numeric();
			
			$("#qtdeExemplar").numeric();
			
			$("#vlrCE").numeric();
			
			$("#dataNotaFiscal").mask("99/99/9999");
			
			$("#valorNotaFiscal").numeric();
			
			$("#sm").numeric();
			
			$("#numeroCota").keypress(function(e) {
				
				if(e.keyCode == 13) {
					
					ConferenciaEncalhe.pesquisarCota();
				}
			});
			
			$("#vlrCE").keypress(function(e) {
				
				if (e.keyCode == 13) {
					
					$("#cod_barras").focus();
				}
			});
			
			$('#cod_barras').keypress(function(e) {
				
				if (e.keyCode == 13) {
					
					if (ultimoCodeBar != "" && ultimoCodeBar == $("#cod_barras").val()){
						
						var qtd = $("#qtdeExemplar").val() == "" ? 0 : parseInt($("#qtdeExemplar").val());
						
						$("#qtdeExemplar").val(qtd + 1);
					} else {
						
						var data = [{name: "codigoBarra", value: $("#cod_barras").val()}, 
						            {name: "sm", value: ""}, 
						            {name: "idProdutoEdicao", value: ""},
						            {name: "codigoAnterior", value: ultimoCodigo},
						            {name: "quantidade", value: $("qtdeExemplar").val()}];
						
						$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao'/>", data,
							function(result){
							
								ConferenciaEncalhe.setarValoresPesquisados(result);
								$("#cod_barras").focus();
							}, null, true, "idModalPesquisarProdutos"
						);
					}
				}
			});
			
			$('#sm').keypress(function(e) {
				
				if (e.keyCode == 13) {
					
					if (ultimoSM != "" && ultimoSM == $("#sm").val()){
						
						var qtd = $("#qtdeExemplar").val() == "" ? 0 : parseInt($("#qtdeExemplar").val());
						
						$("#qtdeExemplar").val(qtd + 1);
					} else {
						
						var _sm = $("#sm").val();
						
						var data = [{name: "codigoBarra", value: ""}, 
						            {name: "sm", value: $("#sm").val()}, 
						            {name: "idProdutoEdicao", value: ""},
						            {name: "codigoAnterior", value: ultimoCodigo},
						            {name: "quantidade", value: $("qtdeExemplar").val()}];
						
						$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao'/>", data,
							function(result){
							
								ConferenciaEncalhe.setarValoresPesquisados(result);
								$("#sm").focus();
							}, null, true, "idModalPesquisarProdutos"
						);
					}
				}
			});
			
			$("#pesq_prod").keyup(function (e){
				
				if (e.keyCode == 13) {
					
					if (ultimoCodigo != "" && ultimoCodigo == $("#pesq_prod").val()){
						
						var qtd = $("#qtdeExemplar").val() == "" ? 0 : parseInt($("#qtdeExemplar").val());
						
						$("#qtdeExemplar").val(qtd + 1);
						
						$("#dialog-pesquisar").dialog("destroy");
						$('#cod_barras').focus();
					} else {
						
						var _codProduto = $("#codProduto").val();
						
						var data = [{name: "codigoBarra", value: ""}, 
						            {name: "sm", value: ""}, 
						            {name: "idProdutoEdicao", value: idProdutoEdicao},
						            {name: "codigoAnterior", value: ultimoCodigo},
						            {name: "quantidade", value: $("qtdeExemplar").val()}];
						
						$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao'/>", data,
							function(result){
								
								ConferenciaEncalhe.setarValoresPesquisados(result);
								$("#dialog-pesquisar").dialog("destroy");
								$('#cod_barras').focus();
							}, null, true, "idModalPesquisarProdutos"
						);
					}
				} else {
					
					if (e.keyCode != 38 && e.keyCode != 40){
						
						ConferenciaEncalhe.mostrar_produtos();
					}
				}
			});
			
			$('#codProduto').keypress(function(e) {
				
				$("#pesq_prod").val("");
				ConferenciaEncalhe.popup_pesquisar();
			});
			
			$('#observacao').keypress(function(e) {
				if (e.keyCode == 13) {
					ConferenciaEncalhe.gravaObs();
				}
			});
			
			ConferenciaEncalhe.popup_logado();
		});
		
		shortcut.add("F2", function() {
			
			if (!modalAberta){
			
				ConferenciaEncalhe.adicionarProdutoConferido();
			}
		});
		
		shortcut.add("F6", function() {
			
			if (!modalAberta){
				
				ConferenciaEncalhe.popup_notaFiscal();
			}
		});
	
		shortcut.add("F8", function() {
			
			if (!modalAberta){
				
				$.postJSON("<c:url value='/devolucao/conferenciaEncalhe/verificarValorTotalNotaFiscal'/>", null,
					function(result){
						
						exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
					},
					function(){
						
						ConferenciaEncalhe.recalcularValoresFinalizar();
					}, true, "idModalDadosNotaFiscal"
				);
			}
		});
		
		shortcut.add("F9", function() {
			
			if (!modalAberta){
				
				ConferenciaEncalhe.popup_salvarInfos();
			}
		});
	</script>
</head>

<body>

	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idTelaConferenciaEncalhe" name="messageDialog"/>
	</jsp:include>

	<jsp:include page="dialog.jsp" />

	<div class="container">

		<fieldset class="classFieldset">

			<legend> Pesquisar Encalhe</legend>

			<table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
				<tr>
					<td width="40">Cota:</td>
					<td width="121">
						<input type="text" id="numeroCota" style="width: 80px; float: left; margin-right: 5px;" />
						<span class="classPesquisar">
							<a href="javascript:;" onclick="ConferenciaEncalhe.pesquisarCota();">&nbsp;</a>
						</span>
					</td>

					<td colspan="2">
						<span class="dadosFiltro" id="nomeCota"></span>
					</td>
					
					<td width="44"><span class="dadosFiltro">Status:</span></td>
					<td width="91"><span class="dadosFiltro" id="statusCota"></span></td>
					<td width="144"><span class="dadosFiltro">Valor CE Jornaleiro R$:</span></td>
					<td width="100">
						<span class="dadosFiltro">
							<input type="text" name="vlrCE" id="vlrCE" style="width: 100px; text-align: right;" />
						</span>
					</td>
				</tr>
			</table>
			
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">

			<table width="950" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="126">
						<span class="bt_novos" title="Contingência">
							<a href="../Recolhimento/conferencia_encalhe_jornaleiro_contingencia.htm">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_expedicao_box.gif" />Contingência
							</a>
						</span>
					</td>
					<td width="314">&nbsp;</td>
					<td width="60" align="center" bgcolor="#F4F4F4"><strong>Atalhos:</strong></td>
					<td width="102" bgcolor="#F8F8F8"><strong>F2</strong>-Novo Produto</td>
					<td width="119" bgcolor="#F8F8F8"><strong>F6</strong>-Nova Nota Fiscal</td>
					<td width="145" bgcolor="#F8F8F8"><strong>F8</strong>-Finalizar Conferência</td>
					<td width="62" bgcolor="#F8F8F8"><strong>F9</strong>-Salvar</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">

			<legend>Encalhe</legend>

			<table width="950" border="0" cellspacing="1" cellpadding="2">
				<tr class="header_table">
					<td width="65" align="center" style="border-left: 1px solid #666; border-top: 1px solid #666;">Qtde</td>
					<td width="168" align="center" style="border-top: 1px solid #666;">Código de Barras</td>
					<td width="42" align="center" style="border-top: 1px solid #666;">SM</td>
					<td width="107" align="center" style="border-top: 1px solid #666; border-right: 1px solid #666;">Código</td>
					<td width="158">Produto</td>
					<td width="69" align="center">Edição</td>
					<td width="93">Preço Capa R$</td>
					<td width="79" align="center">Desc. R$</td>
					<td width="87" align="center">Valor Total R$</td>
					<td width="31">&nbsp;</td>
				</tr>
				<tr>
					<td class="class_linha_1" align="center" style="border-left: 1px solid #666; border-bottom: 1px solid #666;">
						<input name="qtdeExemplar" type="text" id="qtdeExemplar" style="width: 60px; text-align: center;"/>
					</td>
					<td class="class_linha_1" align="center" style="border-bottom: 1px solid #666;">
						<input name="cod_barras" type="text" id="cod_barras" style="width: 160px;"/>
					</td>
					<td class="class_linha_1" align="center" style="border-bottom: 1px solid #666;">
						<input name="sm" type="text" id="sm" style="width: 40px;" />
					</td>
					<td class="class_linha_1" align="center" style="border-bottom: 1px solid #666; border-right: 1px solid #666;">
						<input name="codProduto" type="text" id="codProduto" style="width: 100px;" />
					</td>
					<td class="class_linha_2" id="nomeProduto"></td>
					<td class="class_linha_2" align="center" id="edicaoProduto"></td>
					<td class="class_linha_2" align="center" id="precoCapa"></td>
					<td class="class_linha_2" align="center" id="desconto"></td>
					<td class="class_linha_2" align="center" id="valorTotal"></td>
					<td align="center">
						<a href="javascript:;" class="ok_filtro" onclick="ConferenciaEncalhe.adicionarProdutoConferido();">
							<img src="${pageContext.request.contextPath}/images/bt_check.gif" alt="Incluir" width="22" height="22" border="0" />
						</a>
					</td>
				</tr>
			</table>
			
			<div class="grids" style="display: block; clear: left; margin-top: 10px;">

				<table class="conferenciaEncalheGrid"></table>

				<br clear="all" />

				<table width="950" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="51"><strong>Reparte:</strong></td>
						<td width="85" id="totalReparte"></td>
						<td width="83"><strong> ( - ) Encalhe:</strong></td>
						<td width="87" id="totalEncalhe"></td>
						<td width="126" align="center" bgcolor="#EFEFEF" style="border: 1px solid #000;">
							<strong>( = )Valor Venda Dia:</strong>
						</td>
						<td width="80" align="center" bgcolor="#EFEFEF"	style="border: 1px solid #000;" id="valorVendaDia"></td>
						<td width="130">&nbsp;&nbsp;
							<strong>
								<a href="javascript:;" onclick="popup_outros_valores();"> ( + )Outros valores </a>:
							</strong>
						</td>
						<td width="68" id="totalOutrosValores"></td>
						<td width="122"><strong>( = )Valor a pagar R$:</strong></td>
						<td width="77" id="valorAPagar"></td>
						<td width="17">&nbsp;</td>
					</tr>
				</table>
			</div>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
	</div>
</body>