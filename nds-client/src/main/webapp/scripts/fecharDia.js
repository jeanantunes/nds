var fecharDiaController =  $.extend(true, {
	
	init : function(){
		
		$(".recebeFisicoGrid", fecharDiaController.workspace).flexigrid({
			preProcess: fecharDiaController.executarPreProcessamentoRecebimentoFisicoNaoConfirmado,
			dataType : 'json',
			colModel : [ {
				display : 'Nº Nota Fiscal',
				name : 'numeroNotaFiscal',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Inconsistência',
				name : 'inconsistencia',
				width : 235,
				sortable : true,
				align : 'left'
			}],
			width : 350,
			height : 155
		});
		
		$(".confirmaExpedicaoGrid", fecharDiaController.workspace).flexigrid({
			preProcess: fecharDiaController.executarPreProcessamentoConfirmacaoDeExpedicao,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Inconsistência',
				name : 'inconsistencia',
				width : 410,
				sortable : true,
				align : 'left'
			}],
			width : 650,
			height : 165
		});
		
		$(".lctoFaltasSobrasGrid").flexigrid({
			preProcess: fecharDiaController.executarPreProcessamentoLancamentoFaltaESobra,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Inconsistência',
				name : 'inconsistencia',
				width : 180,
				sortable : true,
				align : 'left'
			}],
			width : 500,
			height : 155
		});
		
		$(".reparteDialogGrid").flexigrid({
			preProcess: fecharDiaController.executarPreProcessamentoReparte,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Sobra em',
				name : 'sobras',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Falta em',
				name : 'faltaEm',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Transf.',
				name : 'transf',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'A Distr',
				name : 'aDistr',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Distribuido',
				name : 'distribuido',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Sobra Distr',
				name : 'sobraDistri',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dif.',
				name : 'dif',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 850,
			height : 255
		});
		
	},
	
	executarPreProcessamentoRecebimentoFisicoNaoConfirmado : function(resultado){
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".dialog-recebe-fisico", fecharDiaController.workspace).hide();

			return resultado;
		}
		
		$("#dialog-recebe-fisico", fecharDiaController.workspace).show();
		
		return resultado;
	},
	
	executarPreProcessamentoConfirmacaoDeExpedicao : function(resultado){
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".dialog-confirma-expedicao", fecharDiaController.workspace).hide();

			return resultado;
		}
		
		$("#dialog-confirma-expedicao", fecharDiaController.workspace).show();
		
		return resultado;
		
	},
	
	executarPreProcessamentoLancamentoFaltaESobra : function(resultado){
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".dialog-lancto-faltas-sobras", fecharDiaController.workspace).hide();

			return resultado;
		}
		
		$("#dialog-lancto-faltas-sobras", fecharDiaController.workspace).show();
		
		return resultado;
		
	},
	
	executarPreProcessamentoReparte : function(resultado){
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".dialog-repartes", fecharDiaController.workspace).hide();

			return resultado;
		}
		
		$("#dialog-repartes", fecharDiaController.workspace).show();
		
		return resultado;
	},
	
	popup : function() {	
		
		$( "#dialog-novo", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
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
			},
			form: $("#dialog-novo", fecharDiaController.workspace).parents("form")
		});
	},
	
	popup_repartes : function() {
		
		$(".reparteDialogGrid", fecharDiaController.workspace).flexOptions({
			url: contextPath + "/administracao/fecharDia/obterGridReparte",
			dataType : 'json',
			params: []
		});
		
		$(".reparteDialogGrid", fecharDiaController.workspace).flexReload();
		
		$( "#dialog-repartes", fecharDiaController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-repartes", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_recolhimento : function(){
		
		$( "#dialog-recolhimentos", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-recolhimentos", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_suplementar : function(){		
		
		$( "#dialog-suplementares", fecharDiaController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-suplementares", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_vendasTot : function() {		
		
		$( "#dialog-venda-total",  fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-venda-total", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_cotasGrid : function(){
	
		$( "#dialog-cota-grid", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:390,
			width:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			},
			form: $("#dialog-cota-grid", fecharDiaController.workspace).parents("form")
		});
	},
	
	popup_boletos_baixados : function(){
	
		$( "#dialog-boletos-baixados", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:430,
			width:800,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
				
			},
			form: $("#dialog-boletos-baixados", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	//callback function to bring a hidden box back
	callback : function(){
		setTimeout(function() {
			$( "#effect:visible").removeAttr( "style" ).fadeOut();

		}, 1000 );
	},
	
	mostrar : function(){
		$(".grids", fecharDiaController.workspace).show();
	},
	
	popup_processos : function() {
		fecharDiaController.iniciarValidacoes();
		$( "#dialog-processos", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					$(".grids").show();
					fecharDiaController.iniciarResumoReparte();
					fecharDiaController.iniciarResumoEncalhe();
					fecharDiaController.iniciarResumoSuplementar();
					fecharDiaController.iniciarResumoDividas();
				}
			},
			form: $("#dialog-processos", fecharDiaController.workspace).parents("form")
		});
		
		
		      
	},
	
	iniciarValidacoes : function(){
		$.postJSON(contextPath + "/administracao/fecharDia/inicializarValidacoes", null,
				function(result){
					fecharDiaController.validacaoBaixaBancaria(result);
					fecharDiaController.validacaoRecebimentoFisico(result);
					fecharDiaController.validacaoConfirmacaoDeExpedicao(result);
					fecharDiaController.validacaoLancamentoFaltasESobras(result);
					fecharDiaController.validacaoControleDeAplicao(result);
				});
	},
	
	validacaoBaixaBancaria : function(result){
		var baixaBancaria = "<tr class='class_linha_1'><td>Baixa Bancária</td>";					
		var iconeBaixaBancaria = null;
		if(result.baixaBancaria){
			iconeBaixaBancaria = 'ico_bloquear.gif';
		}else{
			iconeBaixaBancaria = 'ico_check.gif';
		}
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeBaixaBancaria+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao').append(baixaBancaria + imagem);
	},
	
	validacaoRecebimentoFisico : function(result){
		var recebimentoFisico = null;				
		var iconeRecebimentoFisico = null;		
		if(result.recebimentoFisico){
			recebimentoFisico = "<tr class='class_linha_2'><td>Recebimento Físico:</td>";
			iconeRecebimentoFisico = 'ico_check.gif';
		}else{
			recebimentoFisico = "<tr class='class_linha_2'><td><a href='javascript:;' onclick='fecharDiaController.popup_recebimentoFisico();'>Recebimento Físico</a>:</td>";
			iconeRecebimentoFisico = 'ico_bloquear.gif';
		}		
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeRecebimentoFisico+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao').append(recebimentoFisico + imagem);		
	},
	
	popup_recebimentoFisico : function() {
		
		$(".recebeFisicoGrid", fecharDiaController.workspace).flexOptions({
			url: contextPath + "/administracao/fecharDia/obterRecebimentoFisicoNaoConfirmado",
			dataType : 'json',
			params: []
		});
		
		$(".recebeFisicoGrid", fecharDiaController.workspace).flexReload();
	
		$( "#dialog-recebe-fisico", fecharDiaController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:390,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
	},
	
	validacaoConfirmacaoDeExpedicao : function(result){		
		var confirmacaoDeExpedicao = null;				
		var iconeConfirmacaoDeExpedicao = null;		
		if(result.confirmacaoDeExpedicao){
			confirmacaoDeExpedicao = "<tr class='class_linha_1'><td>Confirmação de Expedição:</td>";
			iconeConfirmacaoDeExpedicao = 'ico_check.gif';
		}else{
			confirmacaoDeExpedicao = "<tr class='class_linha_1'><td><a href='javascript:;' onclick='fecharDiaController.popup_confirma_expedicao();'>Confirmação de Expedição</a>:</td>";
			iconeConfirmacaoDeExpedicao = 'ico_bloquear.gif';
		}		
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeConfirmacaoDeExpedicao+"' alt='Com Diferença' width='16' height='16' /></td></tr>";
		$('#tabela-validacao').append(confirmacaoDeExpedicao + imagem);
		
	},
	
	popup_confirma_expedicao : function() {
		
		$(".confirmaExpedicaoGrid", fecharDiaController.workspace).flexOptions({
			url: contextPath + "/administracao/fecharDia/obterConfirmacaoDeExpedicao",
			dataType : 'json',
			params: []
		});
		
		$(".confirmaExpedicaoGrid", fecharDiaController.workspace).flexReload();
	
		$( "#dialog-confirma-expedicao", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:700,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
	},
	
	validacaoLancamentoFaltasESobras : function(result){
		var lancamentoFaltasESobras = null;				
		var iconeLancamentoFaltasESobras = null;		
		if(result.lancamentoFaltasESobras){
			lancamentoFaltasESobras = "<tr class='class_linha_2'><td>Lançamento de Faltas e Sobras:</td>";
			iconeLancamentoFaltasESobras = 'ico_check.gif';
		}else{
			lancamentoFaltasESobras = "<tr class='class_linha_2'><td><a href='javascript:;' onclick='fecharDiaController.popup_lctoFaltas();'>Lançamento de Faltas e Sobras</a>:</td>";
			iconeLancamentoFaltasESobras = 'ico_bloquear.gif';
		}		
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeLancamentoFaltasESobras+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao').append(lancamentoFaltasESobras + imagem);		
	},
	
	popup_lctoFaltas : function() {
		
		$(".lctoFaltasSobrasGrid", fecharDiaController.workspace).flexOptions({
			url: contextPath + "/administracao/fecharDia/obterLancamentoFaltaESobra",
			dataType : 'json',
			params: []
		});
		
		$(".lctoFaltasSobrasGrid", fecharDiaController.workspace).flexReload();
	
		$( "#dialog-lancto-faltas-sobras", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:550,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
	},
	
	validacaoControleDeAplicao : function(result){
		if(result.controleDeAprovacao){
			$.postJSON(contextPath + "/administracao/fecharDia/validacoesDoCotroleDeAprovacao", null,
					function(result){
						if(result){							
							var conferenciaDeAprovacao = "<tr class='class_linha_1'><td>Controle de Aprovações:</td>";
							var imagem = "<td align='center'><img src='"+ contextPath +"/images/ico_bloquear.gif' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
							$('#tabela-validacao').append(conferenciaDeAprovacao + imagem);
						}
						
					}
				);
		}
	},
	
	iniciarResumoReparte : function(){
		
		$.postJSON(contextPath + "/administracao/fecharDia/obterResumoQuadroReparte", null,
				function(result){					
					$("#totalReparte").html(result[0]);
					$("#totalSobras").html(result[1]);
					$("#totalFaltas").html(result[2]);
					$("#totalTransferencia").html(result[3]);
					$("#totalADistribuir").html(result[4]);
					$("#totalDistribuido").html(result[5]);
					$("#totalSobraDistribuido").html(result[6]);
					$("#totalDiferenca").html(result[7]);
				}
			);
	},
	
	iniciarResumoEncalhe : function(){
		$.postJSON(contextPath + "/administracao/fecharDia/obterResumoQuadroEncalhe", null,
				function(result){
					$("#totalEncalheLogico").html(result[0]);
					$("#totalEncalheFisico").html(result[1]);
					$("#totalEncalheJuramentada").html(result[2]);
					$("#vendaEncalhe").html(result[3]);
				}
			);
	},
	
	iniciarResumoSuplementar : function(){
		$.postJSON(contextPath + "/administracao/fecharDia/obterResumoQuadroSuplementar", null,
				function(result){
					$("#totalSuplementarEstoqueLogico").html(result[0]);
					$("#totalSuplementarTransferencia").html(result[1]);
					$("#totalSuplementarVenda").html(result[2]);
				}
			);
	},
	
	iniciarResumoDividas : function() {
		$.postJSON(contextPath + "/administracao/fecharDia/obterSumarizacaoDividas",
				   null,
				   function(result) {
				       fecharDiaController.processarResumoDividasReceber(result.sumarizacao.DIVIDAS_A_RECEBER);
				       fecharDiaController.processarResumoDividasVencer(result.sumarizacao.DIVIDAS_A_VENCER);
					});
	},
	
	processarResumoDividasReceber : function(itens) {
	    var tabela =  $('#tabela_dividas_receber', fecharDiaController.workspace);
	    fecharDiaController.processarResumoDividas(itens, tabela);
	},
	
	processarResumoDividasVencer : function(itens) {
        var tabela =  $('#tabela_dividas_vencer', fecharDiaController.workspace);
        fecharDiaController.processarResumoDividas(itens, tabela);
    },
	
	processarResumoDividas : function(itens, tabela) {
        $(tabela).html("");
        $(tabela).append(fecharDiaController.gerarLinhaBrancoResumoDividas());
        $(tabela).append(fecharDiaController.gerarCabecalhoResumoDividas());
        $.each(itens, function(index, item) {
            $(tabela).append(fecharDiaController.gerarLinhaResumoDividas(item));                       
        });
        $(tabela).append((fecharDiaController.gerarLinhaBrancoResumoDividas()));
    },
	
	gerarLinhaResumoDividas : function(item) {
		var linhaResumo = "<tr>";
		linhaResumo += "<td width=\"120\" align=\"left\" style=\"border-bottom:1px solid #ccc;\">" + item.descricaoTipoCobranca + "</td>";
		linhaResumo += "<td width=\"80\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + item.totalFormatado +"</td>";
		linhaResumo += "<td width=\"80\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + item.valorPagoFormatado +"</td>";
		linhaResumo += "<td width=\"80\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + item.inadimplenciaFormatado +"</td>";
		linhaResumo += "</tr>";
		return linhaResumo;
		
	},
	
	gerarCabecalhoResumoDividas : function() {
       var linhaCabecalho = "<tr class=\"header_table\">";  
       linhaCabecalho += "<td align=\"left\">Forma de Pagamento</td>";
       linhaCabecalho += "<td align=\"right\">Total R$</td>";
       linhaCabecalho += "<td align=\"right\">Valor Pago</td>";
       linhaCabecalho += "<td align=\"right\">Inadimplência</td>";
       linhaCabecalho += "</tr>";
       return linhaCabecalho; 
    },
	
	gerarLinhaBrancoResumoDividas : function() {
	   var linhaBranco = "<tr>";  
	   linhaBranco += "<td align=\"left\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "</tr>";
	   return linhaBranco; 
	}
	

}, BaseController);