var fecharDiaController =  $.extend(true, {
	
	init : function() {
		
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
				name : 'precoVenda',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'qtdReparte',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Sobra em',
				name : 'qtdSobras',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Falta em',
				name : 'qtdFaltas',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Transf.',
				name : 'qtdTransferido',
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
				name : 'qtdDistribuido',
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
		
		$(".vendasDialogGrid").flexigrid({
			preProcess: fecharDiaController.executarPreProcessamentoVendaSuplementar,
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
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt. Rclto',
				name : 'dataRecolhimento',
				width : 90,
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
        
        $(".recolhimentoDialogGrid").flexigrid({
        	preProcess: fecharDiaController.executarPreProcessamentoEncalhe,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Encalhe',
				name : 'venda',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'diferenca',
				width : 70,
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
        
        $(".suplementarDialogGrid").flexigrid({
        	preProcess: fecharDiaController.executarPreProcessamentoSuplementar,
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
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Qtde Contabil',
				name : 'qtdeContabil',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde Fisico',
				name : 'qtdeFisico',
				width : 80,
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
	
	popup_vendasTot : function() {
		
		$(".vendasDialogGrid", fecharDiaController.workspace).flexOptions({
			url: contextPath + "/administracao/fecharDia/obterGridVendaSuplementar",
			dataType : 'json',
			params: []
		});
		
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
		
		$("#lnkExportacaoCotaXLS").click(function(event) {
			_this.exportacaoDetalhesCota("XLS", tipoResumo);
		});
		
		$("#lnkExportacaoCotaPDF").click(function(event) {
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
		
		
		      
	},
	
	limparTabela : function(){
		$('#baixaBancaria').remove();
		$('#geracaoDeCobranca').remove();
		$('#recebimentoFisico').remove();
		$('#confirmacaoDeExpedicao').remove();
		$('#fechamentoDeEncalhe').remove();
		$('#lancamentoDeFaltasESobras').remove();
		
	},
	
	iniciarValidacoes : function(){
		$.postJSON(contextPath + "/administracao/fecharDia/inicializarValidacoes", null,
				function(result){					
					fecharDiaController.validacaoBaixaBancaria(result);
					fecharDiaController.validacaoGeracaoCobranca(result);
					fecharDiaController.validacaoRecebimentoFisico(result);
					fecharDiaController.validacaoConfirmacaoDeExpedicao(result);
					fecharDiaController.validacaoFechamentoDeEncalhe(result);
					fecharDiaController.validacaoLancamentoFaltasESobras(result);
					fecharDiaController.validacaoControleDeAprovacao(result);
				});
	},
	
	validacaoBaixaBancaria : function(result){
		var baixaBancaria = "<tr class='class_linha_1' id='baixaBancaria'><td>Baixa Bancária</td>";					
		var iconeBaixaBancaria = null;
		if(result.baixaBancaria){
			iconeBaixaBancaria = 'ico_bloquear.gif';
		}else{
			iconeBaixaBancaria = 'ico_check.gif';
		}
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeBaixaBancaria+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao').append(baixaBancaria + imagem);
	},
	
	validacaoGeracaoCobranca : function(result){
		var geracaoCobranca = "<tr class='class_linha_2' id='geracaoDeCobranca'><td>Geração de Cobrança</td>";					
		var iconeGeracaoCobranca = null;
		if(result.geracaoDeCobranca){
			iconeGeracaoCobranca = 'ico_check.gif';
		}else{
			iconeGeracaoCobranca = 'ico_bloquear.gif';
		}
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeGeracaoCobranca+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao').append(geracaoCobranca + imagem);
	},
	
	validacaoRecebimentoFisico : function(result){
		var recebimentoFisico = null;				
		var iconeRecebimentoFisico = null;		
		if(result.recebimentoFisico){
			recebimentoFisico = "<tr class='class_linha_1' id='recebimentoFisico'><td>Recebimento Físico:</td>";
			iconeRecebimentoFisico = 'ico_check.gif';
		}else{
			recebimentoFisico = "<tr class='class_linha_1' id='recebimentoFisico'><td><a href='javascript:;' onclick='fecharDiaController.popup_recebimentoFisico();'>Recebimento Físico</a>:</td>";
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
			confirmacaoDeExpedicao = "<tr class='class_linha_2' id='confirmacaoDeExpedicao'><td>Confirmação de Expedição:</td>";
			iconeConfirmacaoDeExpedicao = 'ico_check.gif';
		}else{
			confirmacaoDeExpedicao = "<tr class='class_linha_2' id='confirmacaoDeExpedicao'><td><a href='javascript:;' onclick='fecharDiaController.popup_confirma_expedicao();'>Confirmação de Expedição</a>:</td>";
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
	
	validacaoFechamentoDeEncalhe : function(result){
		var fechamentoDeEncalhe = "<tr class='class_linha_1' id='fechamentoDeEncalhe'>" + 
        "<td>Fechamento de Encalhe:</td>" +
        "<td align='center'><img src='"+ contextPath +"/images/ico_check.gif' width='16' height='16' alt='Com DiferenÃ§a' /></td>" +
        "</tr>";
		$('#tabela-validacao').append(fechamentoDeEncalhe);
	},
	
	validacaoLancamentoFaltasESobras : function(result){
		var lancamentoFaltasESobras = null;				
		var iconeLancamentoFaltasESobras = null;		
		if(result.lancamentoFaltasESobras){
			lancamentoFaltasESobras = "<tr class='class_linha_2' id='lancamentoDeFaltasESobras'><td>Lançamento de Faltas e Sobras:</td>";
			iconeLancamentoFaltasESobras = 'ico_check.gif';
		}else{
			lancamentoFaltasESobras = "<tr class='class_linha_2' id='lancamentoDeFaltasESobras'><td><a href='javascript:;' onclick='fecharDiaController.popup_lctoFaltas();'>Lançamento de Faltas e Sobras</a>:</td>";
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
	
	validacaoControleDeAprovacao : function(result){
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
					$("#totalReparte").html(result.totalReparte);
					$("#totalSobras").html(result.totalSobras);
					$("#totalFaltas").html(result.totalFaltas);
					$("#totalTransferencia").html(result.totalTranferencia);
					$("#totalADistribuir").html(result.totalADistribuir);
					$("#totalDistribuido").html(result.totalDistribuido);
					$("#totalSobraDistribuido").html(result.sobraDistribuido);
					$("#totalDiferenca").html(result.diferenca);
				}
			);
	},
	
	iniciarResumoEncalhe : function(){
		$.postJSON(contextPath + "/administracao/fecharDia/obterResumoQuadroEncalhe", null,
				function(result){
					$("#totalEncalheLogico").html(result.totalLogico);
					$("#totalEncalheFisico").html(result.totalFisico);
					$("#totalEncalheJuramentada").html(result.totalJuramentado);
					$("#vendaEncalhe").html(result.venda);
					$("#totalSobraEncalhe").html(result.totalSobras);
					$("#totalFaltaEncalhe").html(result.totalFaltas);
					$("#saldoEncalhe").html(result.saldo);
				}
			);
	},
	
	iniciarResumoSuplementar : function(){
		$.postJSON(contextPath + "/administracao/fecharDia/obterResumoQuadroSuplementar", null,
				function(result){
					$("#totalSuplementarEstoqueLogico").html(result.totalEstoqueLogico);
					$("#totalSuplementarTransferencia").html(result.totalTransferencia);
					$("#totalSuplementarVenda").html(result.totalVenda);
					$("#totalSuplementarSaldo").html(result.saldo);
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
    	
    	$("#produtolancamento",fecharDiaController.workspace).html(resumo.resumoEstoqueProduto.quantidadeLancamento);
    	$("#produtoJuramentado",fecharDiaController.workspace).html(resumo.resumoEstoqueProduto.quantidadeJuramentado);
    	$("#produtoSuplenetar",fecharDiaController.workspace).html(resumo.resumoEstoqueProduto.quantidadeSuplementar);
    	$("#produtoRecolhimento",fecharDiaController.workspace).html(resumo.resumoEstoqueProduto.quantidadeRecolhimento);
    	$("#produtoDanificados",fecharDiaController.workspace).html(resumo.resumoEstoqueProduto.quantidadeDanificados);
    	
    	$("#exemplarlancamento",fecharDiaController.workspace).html(resumo.resumoEstoqueExemplar.quantidadeLancamento);
    	$("#exemplarJuramentado",fecharDiaController.workspace).html(resumo.resumoEstoqueExemplar.quantidadeJuramentado);
    	$("#exemplarSuplenetar",fecharDiaController.workspacer).html(resumo.resumoEstoqueExemplar.quantidadeSuplementar);
    	$("#exemplarRecolhimento",fecharDiaController.workspace).html(resumo.resumoEstoqueExemplar.quantidadeRecolhimento);
    	$("#exemplarDanificados",fecharDiaController.workspace).html(resumo.resumoEstoqueExemplar.quantidadeDanificados);
    	
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
	
	gerarLinhaResumoConsignado : function(resumo) {
		
		var linhaResumo = "<tr>";
		
		linhaResumo += "<td width=\"164\" style=\"border-bottom:1px solid #ccc;\">Consignado</td>";
		linhaResumo += "<td width=\"180\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoConsignado.saldoAnterior + "</td>";
		linhaResumo += "<td width=\"180\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoConsignado.valorEntradas + "</td>";
		linhaResumo += "<td width=\"180\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoConsignado.valorSaidas + "</td>";
		linhaResumo += "<td width=\"180\" align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoConsignado.saldoAtual + "</td>";
		
		linhaResumo += "</tr>";
		
		linhaResumo += "<tr>";
		
		linhaResumo += "<td style=\"border-bottom:1px solid #ccc;\">A Vista</td>";
		linhaResumo += "<td align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoAVista.saldoAnterior + "</td>";
		linhaResumo += "<td align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoAVista.valorEntradas + "</td>";
		linhaResumo += "<td align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoAVista.valorSaidas + "</td>";
		linhaResumo += "<td align=\"right\" style=\"border-bottom:1px solid #ccc;\">" + resumo.resumoAVista.saldoAtual + "</td>";
		
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
	}

}, BaseController);