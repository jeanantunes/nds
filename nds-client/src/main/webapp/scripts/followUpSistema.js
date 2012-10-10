var followUpSistemaController = $.extend(true, {
	
	init : function () {
		$( "#tab-followup", followUpSistemaController.workspace ).tabs();
		
		$(".chamadaoGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			url : contextPath + '/followup/pesquisaDadosChamadao',
			dataType : 'json',
			preProcess: followUpSistemaController.exPreProcFollowupChamadao,
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 300,
				sortable : true,
				align : 'left'
			}, {
				display : 'Consignado R$',
				name : 'valorTotalConsignadoFormatado',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Suspenso (dias)',
				name : 'qtdDiasSuspensao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Programado',
				name : 'dataProgramadoChamadao',
				width : 85,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 120,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
	    }));

		$(".pendenciasGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			url : contextPath + '/followup/pesquisaDadosPendenciaNFEEncalhe',
			dataType : 'json',
			preProcess: followUpSistemaController.exPreProcFollowupPendenciasnfe,
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 302,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Pend&ecirc;ncia',
				name : 'tipoPendencia',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Dt. Entrada',
				name : 'dataEntrada',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vlr. Diferença R$',
				name : 'valorDiferencaFormatado',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Telefone',
				name : 'numeroTelefone',
				width : 125,
				sortable : true,
				align : 'left'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
		}));

		$(".alteracaoStatusGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			url : contextPath + '/followup/pesquisaDadosStatusCota',
	        preProcess:  followUpSistemaController.exPreProcFollowupStatusCota, 
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Per&iacuteodo',
				name : 'periodoStatus',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'statusAtual',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Novo Status',
				name : 'statusNovo',
				width : 83,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 155,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
		}));

		$(".atualizacaoCadastralGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			url : contextPath + '/followup/pesquisaDadosCadastrais',
	        preProcess:  followUpSistemaController.exPreProcFollowupCadastro, 
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 290,
				sortable : true,
				align : 'left'
			}, {
				display : 'Responsável',
				name : 'responsavel',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Documento',
				name : 'tipo',
				width : 125,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 125,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt. Vencto.',
				name : 'dataVencimento',
				width : 85,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
		}));

		$(".negociacaoGrid", followUpSistemaController.workspace).flexigrid({
			url : contextPath + '/followup/pesquisaDadosNegociacao',
	        preProcess:  followUpSistemaController.exPreProcFollowupNegociacao, 
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 65,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Negociação',
				name : 'descricaoNegociacao',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Parcela',
				name : 'descricaoParcelamento',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Forma de Pagamento',
				name : 'descricaoFormaPagamento',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Vencto',
				name : 'dataVencimento',
				width : 85,
				sortable : true,
				align : 'center'
			},{
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			sortname : "nomeJornaleiro",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
		});
		
		$(".atualizacaoCadastralParcialGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			url : contextPath + '/followup/pesquisaDadosCadastroParcial',
	        preProcess:  followUpSistemaController.exPreProcFollowupCadastroParcial, 
			dataType : 'json',
			colModel : [ {
				display : 'Codigo',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeProduto',
				width : 290,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi&ccedil;&atilde;o',
				name : 'numeroEdicao',
				width : 150,
				sortable : true,
				align : 'left'
			}],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
		}));
		
	},
		
	exPreProcFollowupNegociacao : function (resultado) {
	
		$.each(resultado.rows, function(index, negociacao) {
			negociacao.cell.acao = "<a href=\"javascript:;\" onclick=\"followUpSistemaController.cancelarNegociacao("+negociacao.cell.idNegociacao+");\"> "+
			 "<img src=\"" + contextPath + "/images/ico_excluir.gif\" title=\"Excluir\" hspace=\"5\" border=\"0\" /></a>";
		});
		
		return resultado;
	},
	
	cancelarNegociacao : function(idNegociacao) {
		
		var parametro = {'idNegociacao' : idNegociacao};
		
		$.postJSON(contextPath + "/followup/cancelarNegociacao", 
				parametro);				
	},
	
	exPreProcFollowupChamadao : function(resultado) {
		$.each(resultado.rows, function(index, row) {						
			
			url = '\'devolucao/chamadao/popularGridFollowUp?numeroCota='+row.cell.numeroCota+'&data='+row.cell.dataProgramadoChamadao + '\'';
				
			var linkExcluir = '<a href="javascript:;" onclick="$(\'#workspace\').tabs(\'addTab\', \'Chamadão\', ' + url + ')"  style="cursor:pointer">' +
							   	 '<img title="Excluir Desconto" src="' + contextPath + '/images/ico_reprogramar.gif" hspace="5" border="0px" />Programar' +
							   '</a>';
			
			row.cell.acao = linkExcluir;
		});
		
		$(".grids", followUpSistemaController.workspace).show();
		return resultado;
	},

	exPreProcFollowupStatusCota : function(resultado) {		
			$.each(resultado.rows, function(index, row) {						
			
			url = '\'financeiro/manutencaoStatusCota/popularGridFollowUp?numeroCota='+row.cell.numeroCota + '\'';
			
			var linkExcluir = '<a href="javascript:;" onclick="$(\'#workspace\').tabs(\'addTab\', \'Manutenção de Status Cota\', ' + url + ')"  style="cursor:pointer">' +
							   	 '<img title="Excluir Desconto" src="' + contextPath + '/images/ico_negociar.png" hspace="5" border="0px" />Manutenção Status' +
							   '</a>';
			
			row.cell.acao = linkExcluir;
		});
		
		$(".grids", followUpSistemaController.workspace).show();
		return resultado;
	},
	
	exPreProcFollowupCadastro : function(resultado) {
	$.each(resultado.rows, function(index, row) {		
			
		if ( row.cell.dataVencimento == undefined ){
				row.cell.dataVencimento='';
		}
			
		});
		return resultado;
	},
	
	exPreProcFollowupPendenciasnfe : function(resultado) {        		
   		return resultado;
	},
	exPreProcFollowupCadastroParcial : function (resultado) {
		//alert("exefollowPreProcessamentoNegociacao");
		return resultado;
	}
	
}, BaseController);



