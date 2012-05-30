var modalAberta = false;

var idProdutoEdicao = "";

var idConferenciaEncalheVisualizacao = "";

var ConferenciaEncalhe = {
	
	pesquisarCota : function() {
		
		var data = [{name : 'numeroCota', value : $("#numeroCota").val()}];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarReabertura", data,
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
								$("#dialog-reabertura").dialog("close");
							},
							"Não" : function() {
								$("#dialog-reabertura").dialog("close");
								modalAberta = false;
							}
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
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
				data, 
				function(result){
					
					ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
					$("#cod_barras").focus();
				}
		);
		
//		$(".conferenciaEncalheGrid").flexOptions({
//			url : contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia',
//			preProcess : ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe,
//			params: data,
//			dataType : 'json'
//		});
//		
//		$(".conferenciaEncalheGrid").flexReload();
	},

	preProcessarConsultaConferenciaEncalhe : function(result) {
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		var innerTable = '';
		
		var modeloConferenciaEncalhe = result[0];
		
		$("._dados").remove();
		
		$.each(modeloConferenciaEncalhe, 
			function(index, value) {
				
				var _class;
				
				if (index % 2 == 0){
					_class = "class_linha_1 _dados";
				} else {
					_class = "class_linha_2 _dados";
				}
			
				innerTable += "<tr class='" + _class + "'><td nowrap='nowrap'>";
				
				var valorExemplares = parseInt(value.qtdExemplar);
				
				var inputExemplares = '<input name="qtdExemplaresGrid" onchange="ConferenciaEncalhe.recalcularValores('+ index +');" style="width:50px; text-align: center;" value="' + valorExemplares + '"/>' +
					'<input name="idConferenciaEncalheHidden" type="hidden" value="' + value.idConferenciaEncalhe + '"/>';
				
				innerTable += inputExemplares + "</td>";
				
				innerTable += "<td nowrap='nowrap'>" + value.codigoDeBarras + "</td>";
				
				innerTable += "<td nowrap='nowrap'>" + value.codigoSM + "</td>";
				
				innerTable += "<td nowrap='nowrap'>" + value.codigo + "</td>";
				
				innerTable += "<td nowrap='nowrap'>" + value.nomeProduto + "</td>";
				
				innerTable += "<td nowrap='nowrap'>" + value.numeroEdicao + "</td>";
				
				innerTable += "<td align='right' nowrap='nowrap'>" + parseFloat(value.precoCapa).toFixed(2) + "</td>";
				
				innerTable += "<td align='right' nowrap='nowrap'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
				
				innerTable += "<td align='right' nowrap='nowrap' name='valorTotalConferencia'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
				
				if (value.dia || value.dataRecolhimento){
				
					if (value.dia && value.dia > 0){
					
						innerTable += "<td align='center' nowrap='nowrap'>" + value.dia + "º" + "</td>";
					} else {
						
						innerTable += "<td align='right' nowrap='nowrap' style='width: 20px;'>" + value.dataRecolhimento.$ + "</td>";
					}
				} else {
					
					innerTable += "<td></td>";
				}
				
				var inputCheckBoxJuramentada = 
					'<input type="checkbox" ' + (value.juramentada == "true" ? 'checked="checked"' : '')
						+ (!value.juramentada ? 'disabled="disabled"' : '')
						+ ' name="checkGroupJuramentada"/>';
				
				innerTable += "<td align='center' nowrap='nowrap'>" + inputCheckBoxJuramentada + "</td>";
				
				var imgDetalhar = '<img src="' + contextPath + '/images/ico_detalhes.png" border="0" hspace="3"/>';
				innerTable += '<td align="center" nowrap="nowrap"><a href="javascript:;" onclick="ConferenciaEncalhe.exibirDetalhesConferencia(' + value.idConferenciaEncalhe + ');">' + imgDetalhar + '</a></td>';
				
				var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
				innerTable += '<td align="center" nowrap="nowrap"><a href="javascript:;" onclick="ConferenciaEncalhe.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
				
				innerTable += "</tr>";
				
				$(innerTable).appendTo("#dadosGridConferenciaEncalhe");
				
				innerTable = '';
			}
		);
		
		//document.getElementById("dadosTabelaConferencia").innerHTML += innerTable;
		
		//$(".conferenciaEncalheGrid").html(innerTable);
		
		$('input[name="qtdExemplaresGrid"]').numeric();
		
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
		
		
		$('input[name="qtdeInformadaFinalizarConf"]').numeric();
		$('input[name="precoCapaFinalizarConf"]').numeric();
		
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
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/finalizarConferencia', null,
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
	
	recalcularValores : function(index){
		
		var data = [
		            {name: "idConferencia", value: $('input[name="idConferenciaEncalheHidden"]')[index].value},
		            {name: "qtdExemplares", value: $('input[name="qtdExemplaresGrid"]')[index].value},
		            {name: "juramentada", value: $('input[name="checkGroupJuramentada"]')[index].disabled ? "" : $('input[name="checkGroupJuramentada"]')[index].checked}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/recalcularConferencia", 
				data, 
				function(result){
					
					$("td[name='valorTotalConferencia']")[index].innerHTML = parseFloat(result[0].valorTotal).toFixed(2);
					
					$("#totalReparte").text(parseFloat(result[1]).toFixed(2));
					$("#totalEncalhe").text(parseFloat(result[2]).toFixed(2));
					$("#valorVendaDia").text(parseFloat(result[3]).toFixed(2));
					$("#totalOutrosValores").text(parseFloat(result[4]).toFixed(2));
					$("#valorAPagar").text(parseFloat(result[5]).toFixed(2));
				}
		);
		
//		$(".conferenciaEncalheGrid").flexOptions({			
//			url : contextPath + "/devolucao/conferenciaEncalhe/recalcularConferencia",
//			dataType : 'json',
//			preProcess: ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe,
//			params: data
//		});
//		
//		$(".conferenciaEncalheGrid").flexReload();
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
			url : contextPath + '/devolucao/conferenciaEncalhe/recalcularConferencia',
			dataType : 'json',
			preProcess: ConferenciaEncalhe.finaliazarConferenciaPreProcess,
			params: data
		});
		
		$(".pesqProdutosNotaGrid").flexReload();
	},
	
	exibirDetalhesConferencia : function(idConferenciaEncalhe){
		
		idConferenciaEncalheVisualizacao = idConferenciaEncalhe;
		
		var data = [{name: "idConferenciaEncalhe", value: idConferenciaEncalhe}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/buscarDetalhesProduto', data,
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
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/excluirConferencia', data,
						function (result){
							
							ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
							
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
					
					$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/salvarIdBoxSessao", "idBox=" + $("#boxLogado").val(), 
						function(){
							
							$("#dialog-logado").dialog("close");
							$('#numeroCota').focus();
						}, null, true, "idModalBoxRecolhimento"
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
		
			//TODO
			//$(".conferenciaEncalheGrid").flexReload();
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
		$("#desconto").text(parseFloat(result.desconto).toFixed(2));
		
		$("#valorTotal").text(((parseFloat(result.precoCapa) - parseFloat(result.desconto)) * parseFloat(result.qtdExemplar)).toFixed(2));
		
		$("#qtdeExemplar").val(parseInt(result.qtdExemplar));
	},
	
	adicionarProdutoConferido : function(){
		
		var data = [{name: "quantidade", value: $("#qtdeExemplar").val()}, 
		            {name: "idProdutoEdicao", value: $("#codProduto").val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			function(result){
				
				ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
				
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
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarNotaFiscal', null, 
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
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
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
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/gravarObservacaoConferecnia', data, 
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
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarConferencia', null,
						function(result){
							
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
							
							$("#dialog-salvar").dialog("close");
						},
						null, true, "idModalConfirmarSalvarConf"
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
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome', 
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

//	conferenciaEncalheGridModel : [   {
//		display : 'Exemplares',
//		name : 'qtdExemplar',
//		width : 65,
//		sortable : false,
//		align : 'left'
//	}, {
//		display : 'Código de Barras',
//		name : 'codigoDeBarras',
//		width : 165,
//		sortable : false,
//		align : 'left'
//	}, {
//		display : 'SM',
//		name : 'codigoSM',
//		width : 50,
//		sortable : false,
//		align : 'center'
//	}, {
//		display : 'Código',
//		name : 'codigo',
//		width : 65,
//		sortable : false,
//		align : 'left'
//	}, {
//		display : 'Produto',
//		name : 'nomeProduto',
//		width : 70,
//		sortable : false,
//		align : 'left'
//	}, {
//		display : 'Edição',
//		name : 'numeroEdicao',
//		width : 40,
//		sortable : false,
//		align : 'left'
//	}, {
//		display : 'Preço Capa R$',
//		name : 'precoCapa',
//		width : 70,
//		sortable : false,
//		align : 'right'
//	}, {
//		display : 'Desconto R$',
//		name : 'desconto',
//		width : 50,
//		sortable : false,
//		align : 'right'
//	}, {
//		display : 'Total R$',
//		name : 'valorTotal',
//		width : 50,
//		sortable : false,
//		align : 'right'
//	}, {
//		display : 'Dia',
//		name : 'dia',
//		width : 20,
//		sortable : false,
//		align : 'center'
//	}, {
//		display : 'Juramentada',
//		name : 'juramentada',
//		width : 70,
//		sortable : false,
//		align : 'center'
//	}, {
//		display : 'Detalhe',
//		name : 'detalhes',
//		width : 45,
//		sortable : false,
//		align : 'center'
//	}, {
//		display : 'Ação',
//		name : 'acao',
//		width : 30,
//		sortable : false,
//		align : 'center'
//	} ],

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

//	$(".conferenciaEncalheGrid").flexigrid({
//		dataType : 'json',
//		colModel : ConferenciaEncalhe.conferenciaEncalheGridModel,
//		width : 960,
//		height : 250,
//		disableSelect: true
//	});

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
				
				$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
					function(result){
					
						ConferenciaEncalhe.setarValoresPesquisados(result);
						$("#cod_barras").focus();
					}, null, false, "idTelaConferenciaEncalhe"
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
				
				var data = [{name: "codigoBarra", value: ""}, 
				            {name: "sm", value: $("#sm").val()}, 
				            {name: "idProdutoEdicao", value: ""},
				            {name: "codigoAnterior", value: ultimoCodigo},
				            {name: "quantidade", value: $("qtdeExemplar").val()}];
				
				$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
					function(result){
					
						ConferenciaEncalhe.setarValoresPesquisados(result);
						$("#sm").focus();
					}, null, false, "idTelaConferenciaEncalhe"
				);
			}
		}
	});
	
	$("#pesq_prod").keyup(function (e){
		
		if (e.keyCode == 13) {
			
			if (ultimoCodigo != "" && ultimoCodigo == $("#pesq_prod").val()){
				
				var qtd = $("#qtdeExemplar").val() == "" ? 0 : parseInt($("#qtdeExemplar").val());
				
				$("#qtdeExemplar").val(qtd + 1);
				
				$("#dialog-pesquisar").dialog("close");
				$('#cod_barras').focus();
			} else {
				
				var data = [{name: "codigoBarra", value: ""}, 
				            {name: "sm", value: ""}, 
				            {name: "idProdutoEdicao", value: idProdutoEdicao},
				            {name: "codigoAnterior", value: ultimoCodigo},
				            {name: "quantidade", value: $("qtdeExemplar").val()}];
				
				$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
					function(result){
						
						ConferenciaEncalhe.setarValoresPesquisados(result);
						$("#dialog-pesquisar").dialog("close");
						$('#cod_barras').focus();
					},
					function(){
						
						$("#codProduto").val("");
					}, true, "idModalPesquisarProdutos"
				);
			}
		} else {
			
			if (e.keyCode != 38 && e.keyCode != 40){
				
				ConferenciaEncalhe.mostrar_produtos();
			}
		}
	});
	
	$('#codProduto').keypress(function(e) {
		
		if (e.keyCode == 0){
		
			$("#pesq_prod").val("");
			ConferenciaEncalhe.popup_pesquisar();
		}
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
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalNotaFiscal', null,
			function(result){
				
				exibirMensagem(result.tipoMensagem, result.listaMensagens);
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