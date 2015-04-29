var ModoDownload = {
    REGULAR_DOWNLOAD: {value: 'REGULAR_DOWNLOAD'},
    JQUERY_FILE_DOWNLOAD_PLUGIN : {value: 'JQUERY_FILE_DOWNLOAD_PLUGIN'}
};

var fecharDiaController =  $.extend(true, {
	
	init : function() {
		
		$("#totalFaltas", fecharDiaController.workspace).attr("title","Faltas pendentes de aprovação do GFS");
		$("#totalFaltas", fecharDiaController.workspace).tooltip();
					
		$("#totalSobras", fecharDiaController.workspace).attr("title","Sobras pendentes de aprovação do GFS");
		$("#totalSobras", fecharDiaController.workspace).tooltip();
				
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
		
		$(".lctoFaltasSobrasGrid", fecharDiaController.workspace).flexigrid({
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
		
		$(".reparteDialogGrid", fecharDiaController.workspace).flexigrid({
			preProcess: fecharDiaController.executarPreProcessamentoReparte,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : false,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 110,
				sortable : false,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 60,
				sortable : false,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'valorPrecoCapaFormatado',
				width : 60,
				sortable : false,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'qtdeReparte',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : 'Sobras',
				name : 'qtdeSobra',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : 'Faltas',
				name : 'qtdeFalta',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : 'Transf.',
				name : 'qtdeTransferencia',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : 'Prom.',
				name : 'qtdeDiferenca',
				width : 40,
				sortable : false,
				align : 'center'
			} ,{
				display : 'A Distr',
				name : 'qtdeDistribuir',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : 'Distribuido',
				name : 'qtdeDistribuido',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : 'Sobra Distr',
				name : 'qtdeSobraDistribuicao',
				width : 55,
				sortable : false,
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
		
		$(".vendasDialogGrid", fecharDiaController.workspace).flexigrid({
			preProcess: fecharDiaController.executarPreProcessamentoVendaSuplementar,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : false,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 250,
				sortable : false,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 130,
				sortable : false,
				align : 'left'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 110,
				sortable : false,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valorFormatado',
				width : 100,
				sortable : false,
				align : 'right'
			}, {
				display : 'Dt. Rclto',
				name : 'dataRecolhimento',
				width : 90,
				sortable : false,
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
		
		$(".dividasReceberGrid", fecharDiaController.workspace).flexigrid({
            url: contextPath + '/administracao/fecharDia/obterDividasReceber',
            autoload: false,
            dataType : 'json',
            colModel : [ {
                display : 'Cota',
                name : 'numeroCota',
                width : 40,
                sortable : false,
                align : 'left'
            },{
                display : 'Nome',
                name : 'nomeCota',
                width : 100,
                sortable : false,
                align : 'left'
            }, {
                display : 'Banco',
                name : 'nomeBanco',
                width : 50,
                sortable : false,
                align : 'left'
            }, {
                display : 'Conta-Corrente',
                name : 'contaCorrente',
                width : 80,
                sortable : false,
                align : 'left'
            }, {
                display : 'Nosso Número',
                name : 'nossoNumero',
                width : 120,
                sortable : false,
                align : 'left'
            }, {
                display : 'Valor R$',
                name : 'valorFormatado',
                width : 70,
                sortable : false,
                align : 'right'
            }, {
                display : 'Dt. Vencto',
                name : 'dataVencimentoFormatada',
                width : 80,
                sortable : false,
                align : 'center'
            }, {
                display : 'Forma Pgto',
                name : 'descricaoFormaPagamento',
                width : 90,
                sortable : false,
                align : 'left'
            }],
            sortname : "numeroCota",
            sortorder : "asc",
            usepager : true,
            useRp : true,
            rp : 15,
            showTableToggleBtn : true,
            width : 750,
            height : 220
        });
        
        $(".dividasVencerGrid", fecharDiaController.workspace).flexigrid({
            url: contextPath + '/administracao/fecharDia/obterDividasVencer',
            autoload: false,
            dataType : 'json',
            colModel : [ {
                display : 'Cota',
                name : 'numeroCota',
                width : 40,
                sortable : false,
                align : 'left'
            },{
                display : 'Nome',
                name : 'nomeCota',
                width : 100,
                sortable : false,
                align : 'left'
            }, {
                display : 'Banco',
                name : 'nomeBanco',
                width : 50,
                sortable : false,
                align : 'left'
            }, {
                display : 'Conta-Corrente',
                name : 'contaCorrente',
                width : 80,
                sortable : false,
                align : 'left'
            }, {
                display : 'Nosso Número',
                name : 'nossoNumero',
                width : 120,
                sortable : false,
                align : 'left'
            }, {
                display : 'Valor R$',
                name : 'valorFormatado',
                width : 70,
                sortable : false,
                align : 'right'
            }, {
                display : 'Dt. Vencto',
                name : 'dataVencimentoFormatada',
                width : 80,
                sortable : false,
                align : 'center'
            }, {
                display : 'Forma Pgto',
                name : 'descricaoFormaPagamento',
                width : 90,
                sortable : false,
                align : 'left'
            }],
            sortname : "numeroCota",
            sortorder : "asc",
            usepager : true,
            useRp : true,
            rp : 15,
            showTableToggleBtn : true,
            width : 750,
            height : 220
        });
		
        $(".cotasGrid", fecharDiaController.workspace).flexigrid({
            url: contextPath + '/administracao/fecharDia/obterDetalhesResumoCota',
            autoload: false,
            dataType : 'json',
            colModel : [{
                display : 'Cota',
                name : 'numeroCota',
                width : 50,
                sortable : false,
                align : 'left'
            },{
                display : 'Nome',
                name : 'nome',
                width : 250,
                sortable : false,
                align : 'left'
            }],
            sortname : "numeroCota",
            sortorder : "asc",
            usepager : false,
            useRp : false,
            showTableToggleBtn : true,
            width : 330,
            height : 220
        });
        
        $(".recolhimentoDialogGrid", fecharDiaController.workspace).flexigrid({
        	preProcess: fecharDiaController.executarPreProcessamentoEncalhe,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : false,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 200,
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
				name : 'precoVendaFormatado',
				width : 80,
				sortable : false,
				align : 'right'
			}, {
				display : 'Lógico',
				name : 'qtdeLogico',
				width : 80,
				sortable : false,
				align : 'center'
			}, {
				display : 'Físico',
				name : 'qtdeFisico',
				width : 80,
				sortable : false,
				align : 'center'
			}, {
				display : 'Lógico Juramentado',
				name : 'qtdeLogicoJuramentado',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
                display : 'Venda Encalhe',
                name : 'qtdeVendaEncalhe',
                width : 70,
                sortable : false,
                align : 'center'
            }, {
                display : 'Diferenças',
                name : 'qtdeDiferenca',
                width : 60,
                sortable : false,
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
        
        $(".suplementarDialogGrid", fecharDiaController.workspace).flexigrid({
        	preProcess: fecharDiaController.executarPreProcessamentoSuplementar,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 200,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 60,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVendaFormatado',
				width : 60,
				align : 'right'
			}, {
				display : 'Qtde',
				name : 'quantidadeLogico',
				width : 90,
				align : 'center'
			}, {
				display : 'Entrada Suplementar',
				name : 'quantidadeTransferenciaEntrada',
				width : 90,
				align : 'center'
			}, {
				display : 'Saída Suplementar',
				name : 'quantidadeTransferenciaSaida',
				width : 90,
				align : 'center'
			}, {
				display : 'Qtde Venda',
				name : 'quantidadeVenda',
				width : 90,
				align : 'center'
			}, {
				display : 'Saldo',
				name : 'saldo',
				width : 90,
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
        
        $("#dataDaOperacao", fecharDiaController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
        $("#dataDaOperacao", fecharDiaController.workspace).mask("99/99/9999");
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
	
	executarPreProcessamentoVendaSuplementar : function(resultado){
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".dialog-venda-total", fecharDiaController.workspace).hide();

			return resultado;
		}
		
		$("#dialog-venda-total", fecharDiaController.workspace).show();
		
		return resultado;
	},
	
	executarPreProcessamentoEncalhe : function(resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".dialog-recolhimentos", fecharDiaController.workspace).hide();

			return resultado;
		}
		
		$("#dialog-recolhimentos", fecharDiaController.workspace).show();
		
		return resultado;
		
	},
	
	executarPreProcessamentoSuplementar : function(resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".dialog-suplementares", fecharDiaController.workspace).hide();

			return resultado;
		}

		$.each(resultado.rows, function(index, row) {
			
			var entrada = row.cell.quantidadeTransferenciaEntrada ? parseInt(row.cell.quantidadeTransferenciaEntrada) : 0;
			var saida = row.cell.quantidadeTransferenciaSaida ? parseInt(row.cell.quantidadeTransferenciaSaida) : 0;
			
			row.cell.quantidadeTransferenciaEntrada = entrada;
			row.cell.quantidadeTransferenciaSaida = saida;
		});
		
		$("#dialog-suplementares", fecharDiaController.workspace).show();
		
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
					
					fecharDiaController.confirmarFechamento();
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-novo", fecharDiaController.workspace).parents("form")
		});
	},
	
	atualizarDataOperacao : function() {
		
		$.postJSON(
				contextPath + "/administracao/fecharDia/obterDataOperacao",
				null, 
				function(result){
					$('#dataDaOperacao', fecharDiaController.workspace).val(result.string);
					$('.grids', fecharDiaController.workspace).find('legend').html('Confirmação de Valores em: ' + result.string);
				});
				
		
	},
	
	confirmarFechamento:function(){
		
		var dataFechamento = $('#dataDaOperacao', fecharDiaController.workspace).val();
		
		var param = [{name:"dataFechamento", value:dataFechamento}];
		
		$.postJSON(
				contextPath + "/administracao/fecharDia/confirmar",
				param, 
				function(){
					fecharDiaController.atualizarDataOperacao();
				    $.fileDownload(contextPath + "/administracao/fecharDia/gerarRelatorioFechamentoDiario", {
                        httpMethod : "POST",
                        cookiePath : contextPath,
                        data : [{name: 'modoDownload', value: ModoDownload.JQUERY_FILE_DOWNLOAD_PLUGIN.value}],
                        preparingMessageHtml: "Gerando relatório do Fechamento Diário, por favor, aguarde...",
                        dialogOptions: { modal: true, closeOnEscape: false, dialogClass: 'no-close-button' },
                        failCallback : function() {
                            exibirMensagem("ERROR", ["Erro na geração do Relatório de Fechamento Diário!"]);
                        }
                    });
				    
				    $(".grids", fecharDiaController.workspace).hide();
				    $("#btnConfirmarFechamento", fecharDiaController.workspace).hide();
				}
				
			);
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
	
	popup_encalhe : function(){
		
		$(".recolhimentoDialogGrid", fecharDiaController.workspace).flexOptions({
			url: contextPath + "/administracao/fecharDia/obterGridEncalhe",
			dataType : 'json',
			params: []
		});
		
		$(".recolhimentoDialogGrid", fecharDiaController.workspace).flexReload();
		
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
		
		$(".suplementarDialogGrid", fecharDiaController.workspace).flexOptions({
			url: contextPath + "/administracao/fecharDia/obterGridSuplementar",
			dataType : 'json',
			params: []
		});
		
		$(".suplementarDialogGrid", fecharDiaController.workspace).flexReload();
		
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
	
	popup_vendasTot : function(tipoVenda) {
		
		if(tipoVenda == "encalhe"){
			$("#tipoVenda", fecharDiaController.workspace).val(tipoVenda);
			$(".vendasDialogGrid", fecharDiaController.workspace).flexOptions({
				url: contextPath + "/administracao/fecharDia/obterGridVenda",
				dataType : 'json',
				params: [{name: "tipoVenda", value: tipoVenda}]
			});
		}else{
			$("#tipoVenda", fecharDiaController.workspace).val(tipoVenda);
			$(".vendasDialogGrid", fecharDiaController.workspace).flexOptions({
				url: contextPath + "/administracao/fecharDia/obterGridVenda",
				dataType : 'json',
				params: [{name: "tipoVenda", value: tipoVenda}]
			});
			
		}
		
		$(".vendasDialogGrid", fecharDiaController.workspace).flexReload();
		
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
	
	exportacaoDetalhesCota : function(fileType, tipoResumo) {
		
		window.location = contextPath + "/administracao/fecharDia/exportarCotas?fileType=" + fileType + "&tipoResumo=" + tipoResumo; 
	},
	
	popup_cotasGrid : function(tipoResumo) {
		
		var _this = this;
		
		$("#lnkExportacaoCotaXLS", fecharDiaController.workspace).click(function(event) {
			_this.exportacaoDetalhesCota("XLS", tipoResumo);
		});
		
		$("#lnkExportacaoCotaPDF", fecharDiaController.workspace).click(function(event) {
			_this.exportacaoDetalhesCota("PDF", tipoResumo);
		});
		
		$("#dialog-cota-grid", fecharDiaController.workspace).dialog({
			resizable: false,
			height: 410,
			width: 380,
			modal: true,
			open: function(event, ui) {
				$(".cotasGrid", fecharDiaController.workspace).flexOptions({
					url: contextPath + '/administracao/fecharDia/obterDetalhesResumoCota',
					params: [
				       {name: 'tipoResumo', value: tipoResumo}
					]
				});
				
			    $(".cotasGrid", fecharDiaController.workspace).flexReload();
			},
			buttons: {
				"Fechar": function() {
					$(this).dialog( "close" );
				},
			},
			form: $("#dialog-cota-grid", fecharDiaController.workspace).parents("form")
		});
	},
	
	popup_dividas_receber : function() {
	
		$("#dialog-dividas-receber", fecharDiaController.workspace).dialog({
			resizable: false,
			height:430,
			width:800,
			modal: true,
			open: function(event, ui) {
			    $(".dividasReceberGrid", fecharDiaController.workspace).flexReload();
			},
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
				
			},
			form: $("#dialog-dividas-receber", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_dividas_vencer : function() {
    
        $("#dialog-dividas-vencer", fecharDiaController.workspace).dialog({
            resizable: false,
            height:430,
            width:800,
            modal: true,
            open: function(event, ui) {
                $(".dividasVencerGrid", fecharDiaController.workspace).flexReload();
            },
            buttons: {
                "Fechar": function() {
                    $( this ).dialog( "close" );
                },
                
            },
            form: $("#dialog-dividas-vencer", fecharDiaController.workspace).parents("form")
        }); 
              
    },
	
	//callback function to bring a hidden box back
	callback : function(){
		setTimeout(function() {
			$( "#effect:visible", fecharDiaController.workspace).removeAttr( "style" ).fadeOut();

		}, 1000 );
	},
	
	mostrar : function(){
		$(".grids", fecharDiaController.workspace).show();
	},
	
	popup_processos : function() {
		fecharDiaController.iniciarValidacoes();
	},
	
	limparTabela : function(){
		$('#baixaBancaria', fecharDiaController.workspace).remove();
		$('#geracaoDeCobranca', fecharDiaController.workspace).remove();
		$('#recebimentoFisico', fecharDiaController.workspace).remove();
		$('#confirmacaoDeExpedicao', fecharDiaController.workspace).remove();
		$('#fechamentoDeEncalhe', fecharDiaController.workspace).remove();
		$('#lancamentoDeFaltasESobras', fecharDiaController.workspace).remove();
		$('#controleDeAplicacao', fecharDiaController.workspace).remove();
		$('#consolidarFinanceiro', fecharDiaController.workspace).remove();
	},
	
	iniciarValidacoes : function(){
		$.postJSON(contextPath + "/administracao/fecharDia/inicializarValidacoes",
			{data: $('#dataDaOperacao', fecharDiaController.workspace).val()},
			function(result){	
				
				if (!result.fechamentoRealizadoNaData){
						$( "#dialog-processos", fecharDiaController.workspace ).dialog({
							resizable: false,
							height:'auto',
							width:300,
							modal: false,
							buttons: {
								"Fechar": function() {
									$( this ).dialog( "close" );
									$(".grids", fecharDiaController.workspace).show();
									fecharDiaController.limparTabela();
									fecharDiaController.iniciarResumoReparte();
									fecharDiaController.iniciarResumoEncalhe();
									fecharDiaController.iniciarResumoSuplementar();
									fecharDiaController.iniciarResumoDividas();
									fecharDiaController.iniciarResumoCotas();
									fecharDiaController.iniciarResumoEstoque();
									fecharDiaController.iniciarResumoConsignado();
								}
							},
							form: $("#dialog-processos", fecharDiaController.workspace).parents("form")
						});		 
				}else{
					
					$(".grids", fecharDiaController.workspace).show();
					fecharDiaController.limparTabela();
					fecharDiaController.iniciarResumoReparte();
					fecharDiaController.iniciarResumoEncalhe();
					fecharDiaController.iniciarResumoSuplementar();
					fecharDiaController.iniciarResumoDividas();
					fecharDiaController.iniciarResumoCotas();
					fecharDiaController.iniciarResumoEstoque();
					fecharDiaController.iniciarResumoConsignado();
				}
				
				$('#tabela-validacao', fecharDiaController.workspace).clear();
				fecharDiaController.validacaoBaixaBancaria(result);
				fecharDiaController.validacaoRecebimentoFisico(result);
				fecharDiaController.validacaoConfirmacaoDeExpedicao(result);
				fecharDiaController.validacaoFechamentoDeEncalhe(result);
				fecharDiaController.validacaoLancamentoFaltasESobras(result);
				fecharDiaController.validacaoControleDeAprovacao(result);
				fecharDiaController.validacaoConsolidarFinanceiro(result);
				fecharDiaController.validarMatrizRecolhimento(result);
				
				if (result.habilitarConfirmar){
					$('#btnConfirmarFechamento', fecharDiaController.workspace).show();
				} else {
					$('#btnConfirmarFechamento', fecharDiaController.workspace).hide();
				}
			}
		);
	},
	
	validacaoBaixaBancaria : function(result){
		var baixaBancaria = "<tr class='class_linha_1' id='baixaBancaria'><td>Baixa Bancária</td>";					
		var iconeBaixaBancaria = null;
		if(result.baixaBancaria){
			iconeBaixaBancaria = 'ico_check.gif';
		}else{
			iconeBaixaBancaria = 'ico_bloquear.gif';
		}
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeBaixaBancaria+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao', fecharDiaController.workspace).append(baixaBancaria + imagem);
	},
	
	validacaoRecebimentoFisico : function(result){
		var recebimentoFisico = null;				
		var iconeRecebimentoFisico = null;		
		if(result.recebimentoFisico){
			recebimentoFisico = "<tr class='class_linha_2' id='recebimentoFisico'><td>Recebimento Físico:</td>";
			iconeRecebimentoFisico = 'ico_check.gif';
		}else{
			recebimentoFisico = "<tr class='class_linha_2' id='recebimentoFisico'><td><a href='javascript:;' onclick='fecharDiaController.popup_recebimentoFisico();'>Recebimento Físico</a>:</td>";
			iconeRecebimentoFisico = 'ico_bloquear.gif';
		}		
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeRecebimentoFisico+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao', fecharDiaController.workspace).append(recebimentoFisico + imagem);		
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
			},
			form: $("#dialog-recebe-fisico", fecharDiaController.workspace).parents("form")
		});
	},
	
	validacaoConfirmacaoDeExpedicao : function(result){		
		var confirmacaoDeExpedicao = null;				
		var iconeConfirmacaoDeExpedicao = null;		
		if(result.confirmacaoDeExpedicao){
			confirmacaoDeExpedicao = "<tr class='class_linha_1' id='confirmacaoDeExpedicao'><td>Confirmação de Expedição:</td>";
			iconeConfirmacaoDeExpedicao = 'ico_check.gif';
		}else{
			confirmacaoDeExpedicao = "<tr class='class_linha_1' id='confirmacaoDeExpedicao'><td><a href='javascript:;' onclick='fecharDiaController.popup_confirma_expedicao();'>Confirmação de Expedição</a>:</td>";
			iconeConfirmacaoDeExpedicao = 'ico_bloquear.gif';
		}		
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeConfirmacaoDeExpedicao+"' alt='Com Diferença' width='16' height='16' /></td></tr>";
		$('#tabela-validacao', fecharDiaController.workspace).append(confirmacaoDeExpedicao + imagem);
		
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
			},
			form: $("#dialog-confirma-expedicao", fecharDiaController.workspace).parents("form")
		});
	},
	
	validacaoFechamentoDeEncalhe : function(result){
		
		var img = (result.fechamentoEncalhe ? '/images/ico_check.gif' : '/images/ico_bloquear.gif');
		
		var fechamentoDeEncalhe = "<tr class='class_linha_2' id='fechamentoDeEncalhe'>" + 
        "<td>Fechamento de Encalhe:</td>" +
        "<td align='center'><img src='"+ contextPath + img + "' width='16' height='16' alt='Com DiferenÃ§a' /></td>" +
        "</tr>";
		$('#tabela-validacao', fecharDiaController.workspace).append(fechamentoDeEncalhe);
	},
	
	validacaoLancamentoFaltasESobras : function(result){
		var lancamentoFaltasESobras = null;				
		var iconeLancamentoFaltasESobras = null;		
		if(result.lancamentoFaltasESobras){
			lancamentoFaltasESobras = "<tr class='class_linha_1' id='lancamentoDeFaltasESobras'><td>Lançamento de Faltas e Sobras:</td>";
			iconeLancamentoFaltasESobras = 'ico_check.gif';
		}else{
			lancamentoFaltasESobras = "<tr class='class_linha_1' id='lancamentoDeFaltasESobras'><td><a href='javascript:;' onclick='fecharDiaController.popup_lctoFaltas();'>Lançamento de Faltas e Sobras</a>:</td>";
			iconeLancamentoFaltasESobras = 'ico_bloquear.gif';
		}		
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeLancamentoFaltasESobras+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao', fecharDiaController.workspace).append(lancamentoFaltasESobras + imagem);		
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
			},
			open : function(event, ui) {
				fecharDiaController.redirecionarFaltasESobras();
			},
			
			form: $("#dialog-lancto-faltas-sobras", fecharDiaController.workspace).parents("form")
		});
	},
	
	validacaoControleDeAprovacao : function(result){
		if(result.controleDeAprovacao===true){
			$.postJSON(contextPath + "/administracao/fecharDia/validacoesDoControleDeAprovacao", null,
					function(result){
						
						var conferenciaDeAprovacao = 
							"<tr class='class_linha_2' id='controleDeAplicacao'><td>Controle de Aprovações:</td>";
						
						var imagem = "<td align='center'><img src='"+ contextPath;
						if(result.boolean===true){							
							
							imagem += "/images/ico_bloquear.gif' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
						} else {
							
							imagem += "/images/ico_check.gif' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
						}
						$('#tabela-validacao', fecharDiaController.workspace).append(conferenciaDeAprovacao + imagem);
					}
				);
		}
	},
	
	validarMatrizRecolhimento: function(result) {

		var matrizRecolhimentoSalva = "<tr class='class_linha_1' id='consolidarFinanceiro'><td>Matriz de recolhimento salva e não confirmada:</td>";					
		var iconeMatrizRecolhimentoSalva = null;

		if(result.matrizRecolhimentoSalva){

			iconeMatrizRecolhimentoSalva = 'ico_bloquear.gif';

		} else{

			iconeMatrizRecolhimentoSalva = 'ico_check.gif';
		}
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeMatrizRecolhimentoSalva+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao', fecharDiaController.workspace).append(matrizRecolhimentoSalva + imagem);
	},

	validacaoConsolidarFinanceiro : function(result){
		var consolidarFinanceiro = null;					
		var iconeConsolidarFinanceiro = null;
		if(result.consolidadoCota){
			
			consolidarFinanceiro = "<tr class='class_linha_2' id='consolidarFinanceiro'><td>Cobrar/Postergar Financeiro de Cotas À Vista</td>";
			
			iconeConsolidarFinanceiro = 'ico_check.gif';
		}else{
			
			consolidarFinanceiro = "<tr class='class_linha_2' id='consolidarFinanceiro'><td><a href='javascript:;' onclick='fecharDiaController.redirecionarProcessamentoFinanceiro();'>Cobrar/Postergar Financeiro de Cotas À Vista</a>:</td>";
			
			iconeConsolidarFinanceiro = 'ico_bloquear.gif';
		}
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeConsolidarFinanceiro+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao', fecharDiaController.workspace).append(consolidarFinanceiro + imagem);
	},

	redirecionarProcessamentoFinanceiro : function(){
		
		adicionarTab("Processamento Financeiro","/financeiro/movimentoFinanceiroCota/");
	},
	
	iniciarResumoReparte : function(){
		
		$.postJSON(contextPath + "/administracao/fecharDia/obterResumoQuadroReparte", null,
				function(result){					
	
					$("#totalReparte", fecharDiaController.workspace).html(result.totalReparteFormatado);
					$("#totalSobras", fecharDiaController.workspace).html(result.totalSobrasFormatado);
					$("#totalFaltas", fecharDiaController.workspace).html(result.totalFaltasFormatado);
					$("#totalTransferencia", fecharDiaController.workspace).html(result.totalTransferenciasFormatado);
					$("#totalADistribuir", fecharDiaController.workspace).html(result.totalDistribuirFormatado);
					$("#totalDistribuido", fecharDiaController.workspace).html(result.totalDistribuidoFormatado);
					$("#totalSobraDistribuido", fecharDiaController.workspace).html(result.totalSobraDistribuicaoFormatado);
					$("#totalDiferenca", fecharDiaController.workspace).html(result.totalDiferencaFormatado);
			
					if( result.totalFaltas && result.totalFaltas > 0){
						$("#totalFaltas", fecharDiaController.workspace).tooltip().onShow = function() { this.show();};
						$("#totalFaltas", fecharDiaController.workspace).css( "color", "red" );
					}
					else{
						$("#totalFaltas", fecharDiaController.workspace).tooltip().onShow = function() { this.hide();};
						$("#totalFaltas", fecharDiaController.workspace).css( "color", "#222222" );
					}
					
					if(result.totalSobras && result.totalSobras > 0){
						$("#totalSobras", fecharDiaController.workspace).tooltip().onShow = function() { this.show();};
						$("#totalSobras", fecharDiaController.workspace).css( "color", "red" );
					}
					else{
						$("#totalSobras", fecharDiaController.workspace).tooltip().onShow = function() { this.hide();};
						$("#totalSobras", fecharDiaController.workspace).css( "color", "#222222" );
					}
					
				}
			);
	},
	
	iniciarResumoEncalhe : function() {
		
		$.postJSON(contextPath + "/administracao/fecharDia/obterResumoQuadroEncalhe", null,
				function(result) {
					$("#totalEncalheLogico", fecharDiaController.workspace).html(result.totalLogicoFormatado);
					$("#totalEncalheFisico", fecharDiaController.workspace).html(result.totalFisicoFormatado);
					$("#totalEncalheJuramentada", fecharDiaController.workspace).html(result.totalJuramentadoFormatado);
					$("#vendaEncalhe", fecharDiaController.workspace).html(result.vendaFormatado);
					$("#totalSobraEncalhe", fecharDiaController.workspace).html(result.totalSobrasFormatado);
					$("#totalFaltaEncalhe", fecharDiaController.workspace).html(result.totalFaltasFormatado);
					$("#saldoEncalhe", fecharDiaController.workspace).html(result.saldoFormatado);
				}
			);
	},
	
	iniciarResumoSuplementar : function() {
		
		$.postJSON(contextPath + "/administracao/fecharDia/obterResumoQuadroSuplementar", null,
				function(result) {
					$("#totalSuplementarEstoqueLogico", fecharDiaController.workspace).html(result.totalEstoqueLogicoFormatado);
					$("#totalSuplementarTransferencia", fecharDiaController.workspace).html(result.totalTransferenciaFormatado);
					$("#totalSuplementarVenda", fecharDiaController.workspace).html(result.totalVendaFormatado);
					$("#totalInventario", fecharDiaController.workspace).html(result.totalInventarioFormatado);
					$("#totalValorAlteracaoPreco", fecharDiaController.workspace).html(result.totalAlteracaoPrecoFormatado);
					$("#totalSuplementarSaldo", fecharDiaController.workspace).html(result.saldoFormatado);
				}
			);
	},
	
	iniciarResumoDividas : function() {
		$.postJSON(contextPath + "/administracao/fecharDia/obterSumarizacaoDividas",
				   null,
				   function(result) {
				       if (result) {
				            if (result.mensagens) {
                                exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                            } else {
				                $('#tabela_dividas_receber_vencer', fecharDiaController.workspace).show();
				                fecharDiaController.processarResumoDividasReceber(result.sumarizacao.DIVIDA_A_RECEBER);
				                fecharDiaController.processarResumoDividasVencer(result.sumarizacao.DIVIDA_A_VENCER);
                            }
				       } else {
				           $('#tabela_dividas_receber_vencer', fecharDiaController.workspace).hide();
				       }
					});
	},
	
	iniciarResumoCotas : function() {
		$.postJSON(
			contextPath + "/administracao/fecharDia/obterResumoCotas",
			null,
			function(result) {
				fecharDiaController.processarResumoCotas(result.resumo);
			}
		);
	},
	
	iniciarResumoConsignado : function() {
		$.postJSON(
			contextPath + "/administracao/fecharDia/obterResumoConsignado",
			null,
			function(result) {
				fecharDiaController.processarResumoConsignado(result.resumo);
			}
		);
	},
	
	iniciarResumoEstoque : function() {
		$.postJSON(
			contextPath + "/administracao/fecharDia/obterResumoEstoque",
			null,
			function(result) {
				fecharDiaController.processarResumoEstoque(result.resumo);
			}
		);
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
    
    processarResumoCotas : function(resumo) {
    	
    	var tabela =  $('#tabela_cotas', fecharDiaController.workspace);
    	
        $(tabela).html("");
        
        $(tabela).append(fecharDiaController.gerarLinhaBrancoResumoCotas());
        
        $(tabela).append(fecharDiaController.gerarCabecalhoResumoCotas());
        
        $(tabela).append(fecharDiaController.gerarLinhaResumoCotas(resumo));                       

        $(tabela).append((fecharDiaController.gerarLinhaBrancoResumoCotas()));
    },
    
    processarResumoConsignado : function(resumo) {
    	
    	var tabela =  $('#tabela_consignado', fecharDiaController.workspace);
    	
        $(tabela).html("");
        
        $(tabela).append(fecharDiaController.gerarLinhaBrancoResumoConsignado());
        
        $(tabela).append(fecharDiaController.gerarCabecalhoResumoConsignado());
        
        $(tabela).append(fecharDiaController.gerarLinhaResumoConsignado(resumo));                       

        $(tabela).append((fecharDiaController.gerarLinhaBrancoResumoConsignado()));
    },
    
    processarResumoEstoque : function(resumo) {
    	
    	$("#produtolancamento",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueProduto.quantidadeLancamento));
    	$("#produtoJuramentado",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueProduto.quantidadeJuramentado));
    	$("#produtoSuplenetar",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueProduto.quantidadeSuplementar));
    	$("#produtoRecolhimento",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueProduto.quantidadeRecolhimento));
    	$("#produtoDanificados",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueProduto.quantidadeDanificados));
    	
    	$("#exemplarlancamento",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueExemplar.quantidadeLancamento));
    	$("#exemplarJuramentado",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueExemplar.quantidadeJuramentado));
    	$("#exemplarSuplenetar",fecharDiaController.workspacer).html(formatarMilhar(resumo.resumoEstoqueExemplar.quantidadeSuplementar));
    	$("#exemplarRecolhimento",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueExemplar.quantidadeRecolhimento));
    	$("#exemplarDanificados",fecharDiaController.workspace).html(formatarMilhar(resumo.resumoEstoqueExemplar.quantidadeDanificados));
    	
    	$("#valorlancamento",fecharDiaController.workspace).html(resumo.valorResumoEstoque.valorLancamentoFormatado);
    	$("#valorJuramentado",fecharDiaController.workspace).html(resumo.valorResumoEstoque.valorJuramentadoFormatado);
    	$("#valorSuplenetar",fecharDiaController.workspace).html(resumo.valorResumoEstoque.valorSuplementarFormatado);
    	$("#valorRecolhimento",fecharDiaController.workspace).html(resumo.valorResumoEstoque.valorRecolhimentoFormatado);
    	$("#valorDanificados",fecharDiaController.workspace).html(resumo.valorResumoEstoque.valorDanificadosFormatado);
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
	},
    
	gerarLinhaResumoCotas : function(resumo) {
		
		var linhaResumo = "<tr>";
		
		linhaResumo += "<td width=\"222\" align=\"center\" style=\"border-bottom:1px solid #ccc;\">" + resumo.TOTAL + "</td>";
		linhaResumo += "<td width=\"153\" align=\"center\" style=\"border-bottom:1px solid #ccc;\">" + resumo.ATIVAS +"</td>";
		linhaResumo += "<td width=\"158\" align=\"center\" style=\"border-bottom:1px solid #ccc;\"><a href=\"javascript:;\" onclick=\"fecharDiaController.popup_cotasGrid(\'AUSENTES_REPARTE\');\">" + resumo.AUSENTES_REPARTE +"</a></td>";
		linhaResumo += "<td width=\"183\" align=\"center\" style=\"border-bottom:1px solid #ccc;\"><a href=\"javascript:;\" onclick=\"fecharDiaController.popup_cotasGrid(\'AUSENTES_ENCALHE\');\">" + resumo.AUSENTES_ENCALHE +"</a></td>";
		linhaResumo += "<td width=\"183\" align=\"center\" style=\"border-bottom:1px solid #ccc;\"><a href=\"javascript:;\" onclick=\"fecharDiaController.popup_cotasGrid(\'NOVAS\');\">" + resumo.NOVAS +"</a></td>";
		linhaResumo += "<td width=\"188\" align=\"center\" style=\"border-bottom:1px solid #ccc;\"><a href=\"javascript:;\" onclick=\"fecharDiaController.popup_cotasGrid(\'INATIVAS\');\">" + resumo.INATIVAS +"</a></td>";
		linhaResumo += "</tr>";
		
		return linhaResumo;
	},
	
	gerarCabecalhoResumoCotas : function() {
		
       var linhaCabecalho = "<tr class=\"header_table\">";  
       
       linhaCabecalho += "<td align=\"center\">Total</td>";
       linhaCabecalho += "<td align=\"center\">Ativas</td>";
       linhaCabecalho += "<td align=\"center\">Ausentes - Reparte</td>";
       linhaCabecalho += "<td align=\"center\">Ausentes - Encalhe</td>";
       linhaCabecalho += "<td align=\"center\">Novas</td>";
       linhaCabecalho += "<td align=\"center\">Inativas</td>";
       linhaCabecalho += "</tr>";
       
       return linhaCabecalho; 
    },
	
	gerarLinhaBrancoResumoCotas : function() {
	   
	   var linhaBranco = "<tr>";
	   
	   linhaBranco += "<td align=\"left\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   
	   linhaBranco += "</tr>";
	   
	   return linhaBranco; 
	},
	
	popup_valorEntradaConsigando:function(resumo){
		
		$( "#dialog-consolidado-valor-entrada", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			},
			
			form: $("#dialog-consolidado-valor-entrada", fecharDiaController.workspace).parents("form")
		});
	},
	
	popup_valorSaidaConsigando:function(resumo){
		
		$( "#dialog-consolidado-valor-saida", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			},
			
			form: $("#dialog-consolidado-valor-saida", fecharDiaController.workspace).parents("form")
		});
	},
	
	gerarLinhaResumoConsignado : function(resumo) {
		
		$("#consignaddo-entrada-valorCE", fecharDiaController.workspace).html(resumo.resumoConsignado.valorCEFormatado);
		
		$("#consignaddo-entrada-outrosValores", fecharDiaController.workspace).html(resumo.resumoConsignado.valorOutrosValoresEntradaFormatado);
		
		$("#consignaddo-entrada-a-vista", fecharDiaController.workspace).html(resumo.resumoConsignado.valorAVistaCEFormatado);
		
		$("#consignaddo-saida-valorExpedicaoDia", fecharDiaController.workspace).html(resumo.resumoConsignado.valorExpedicaoFormatado);
		
		$("#consignaddo-saida-outrosValores", fecharDiaController.workspace).html(resumo.resumoConsignado.valorOutrosValoresSaidasFormatado);
		
		$("#consignaddo-saida-a-vista", fecharDiaController.workspace).html(resumo.resumoConsignado.valorAVistaFormatado);
		
		$("#consignaddo-alteracao-de-preco", fecharDiaController.workspace).html(resumo.resumoConsignado.valorAlteracaoPrecoFormatado);

		var linhaResumo = "<tr>";
		
		var valorEntrada = "<a href=\"javascript:;\" onclick=\"fecharDiaController.popup_valorEntradaConsigando();\">" + resumo.resumoConsignado.valorEntradasFormatado +"</a>";
		
		var valorSaida = "<a href=\"javascript:;\" onclick=\"fecharDiaController.popup_valorSaidaConsigando();\">" + resumo.resumoConsignado.valorSaidasFormatado +"</a>";
		
		linhaResumo += "<td width=\"164\" style=\"border-bottom:1px solid #ccc;\">Consignado</td>";
		linhaResumo += "<td width=\"180\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoConsignado.saldoAnteriorFormatado + "</td>";
		linhaResumo += "<td width=\"180\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + valorEntrada + "</td>";
		linhaResumo += "<td width=\"180\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + valorSaida + "</td>";
		linhaResumo += "<td width=\"180\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoConsignado.saldoAtualFormatado + "</td>";
		
		linhaResumo += "</tr>";
		
		return linhaResumo;
	},
	
	gerarCabecalhoResumoConsignado : function() {
		
       var linhaCabecalho = "<tr class=\"header_table\">";  
       
       linhaCabecalho += "<td>&nbsp;</td>";
       linhaCabecalho += "<td align=\"right\">Saldo Anterior  R$</td>";
       linhaCabecalho += "<td align=\"right\">Entradas  R$</td>";
       linhaCabecalho += "<td align=\"right\">Saídas  R$</td>";
       linhaCabecalho += "<td align=\"right\">Saldo Atual  R$</td>";
       
       linhaCabecalho += "</tr>";
       
       return linhaCabecalho; 
    },
	
	gerarLinhaBrancoResumoConsignado : function() {
	   
	   var linhaBranco = "<tr>";
	   
	   linhaBranco += "<td align=\"left\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   linhaBranco += "<td align=\"right\">&nbsp;</td>";
	   
	   linhaBranco += "</tr>";
	   
	   return linhaBranco; 
	},
	
	exportarVendaEncalheOuSuplementar : function(tipoArquivo){
		
		params = [];
		
		params.push({
			'name' : 'fileType',
			'value' : tipoArquivo
		},{
			'name' : 'tipoVenda',
			'value' : $("#tipoVenda", fecharDiaController.workspace).val()
		});
		
		
		$.fileDownload(
				contextPath + '/administracao/fecharDia/exportarVendaSuplemntar',{
					httpMethod : "POST",
					data : params
				});
		
	},
	
	redirecionarFaltasESobras : function(){
		url = '\'estoque/diferenca/lancamento\'';
		
		var link = '<span class="bt_novos"> <a href="javascript:;" onclick="$(\'#workspace\').tabs(\'addTab\', \'Lançamento Faltas e Sobras\', ' + url + '),fecharDiaController.customizarDialogFaltasESobras(); "  style="cursor:pointer">' +
						   	 '<img src="' + contextPath + '/images/ico_check.gif" hspace="5" border="0" />Sim' +
						   '</a> </span>';
		$("#linkParaFaltasESobra", fecharDiaController.workspace).html(link);
		
	},
	
	customizarDialogFaltasESobras : function(){		
		
		parent.$('#dialog-lancto-faltas-sobras', fecharDiaController.workspace).dialog("option", "modal", false);
		parent.$('#dialog-lancto-faltas-sobras', fecharDiaController.workspace).dialog('close');
		
	},
	
	tratarExibicaoPesquisa:function(){
		
		$.postJSON(
			contextPath + "/administracao/fecharDia/isDataOperacaoDistribuidor",
			{data: $('#dataDaOperacao', fecharDiaController.workspace).val()},
			function(result) {
				
				if(result.isDataOperacaoDistribuidor){
					$("#idBotaoFechamentoDiario").text("Pesquisar");
					$("#idTituloBoataoFechamentoDiario").attr("title","Pesquisar Fechamento do Dia");
					$("#idImgBotaoFecamentoDiario").attr("src",contextPath+'/images/ico_pesquisar.png');
				}
				else{
					$("#idBotaoFechamentoDiario").text("Iniciar Fechamento do Dia");
					$("#idImgBotaoFecamentoDiario").attr("src",contextPath+'/images/bt_devolucao.png');
					$("#idTituloBoataoFechamentoDiario").attr("title","Iniciar Fechamento do Dia");
				}
			}
		);
	},
	
	transferirDiferencasParaEstoqueDePerdaGanho : function() {
		
		$.postJSON(
			contextPath + "/administracao/fecharDia/transferirDiferencasParaEstoqueDePerdaGanho",
			null,
			function(result) {

				$("#dialog-lancto-faltas-sobras", fecharDiaController.workspace).dialog("close");
				
				$("#dialog-processos", fecharDiaController.workspace).dialog("close");
				
				fecharDiaController.popup_processos();
			}
		);
	}
	
}, BaseController);
//@ sourceURL=fecharDia.js