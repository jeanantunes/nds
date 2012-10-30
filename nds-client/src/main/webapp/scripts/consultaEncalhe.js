var ConsultaEncalhe = $.extend(true, {

		init : function() {
			
			var colunas = ConsultaEncalhe.obterColModel();
			var colunasDetalhes = ConsultaEncalhe.obterColModelDetalhes();
			var colunasOutrosValores = ConsultaEncalhe.obterColModelOutrosValores();
			
			$("#cota", ConsultaEncalhe.workspace).numeric();
			
			$("#gridConsultaEncalhe", ConsultaEncalhe.workspace).flexigrid({
				
				dataType : 'json',
				preProcess:ConsultaEncalhe.executarPreProcessamento,
				colModel : colunas,
				sortname : "codigoProduto",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
			
			$("#dadosDetalheEncalheGrid", ConsultaEncalhe.workspace).flexigrid({
				
				dataType : 'json',
				preProcess:ConsultaEncalhe.executarPreProcessamentoDetalhe,
				colModel : colunasDetalhes,
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 600,
				height : 180
			});
			
			$("#outrosValoresGrid", ConsultaEncalhe.workspace).flexigrid({
				dataType : 'json',
				colModel : colunasOutrosValores,
				width : 540,
				height : 250,
				disableSelect: true
			});

			$('.datePicker', ConsultaEncalhe.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('.datePicker', ConsultaEncalhe.workspace).mask("99/99/9999");
			
		},
		
		pesquisar: function() {
			
			var dataRecolhimentoInicial = $("#dataRecolhimentoInicial", ConsultaEncalhe.workspace).val();
			var dataRecolhimentoFinal 	= $("#dataRecolhimentoFinal", ConsultaEncalhe.workspace).val();
			var idFornecedor			= $("#idFornecedor", ConsultaEncalhe.workspace).val();
			var numeroCota				= $("#cota", ConsultaEncalhe.workspace).val();
			
			var formData = [
			        
			        {name:'dataRecolhimentoInicial', value: dataRecolhimentoInicial},
			        {name:'dataRecolhimentoFinal', value: dataRecolhimentoFinal},
			        {name:'idFornecedor', value: idFornecedor},
			        {name:'numeroCota', value: numeroCota }
			];
			
			$("#gridConsultaEncalhe", ConsultaEncalhe.workspace).flexOptions({
				url: contextPath + "/devolucao/consultaEncalhe/pesquisar",
				params: formData
			});
			
			$("#gridConsultaEncalhe", ConsultaEncalhe.workspace).flexReload();

		},
	
		gerarSlip: function() {
			
			var dataRecolhimentoInicial = $("#dataRecolhimentoInicial", ConsultaEncalhe.workspace).val();
			var dataRecolhimentoFinal 	= $("#dataRecolhimentoFinal", ConsultaEncalhe.workspace).val();
			var idFornecedor			= $("#idFornecedor", ConsultaEncalhe.workspace).val();
			var numeroCota				= $("#cota", ConsultaEncalhe.workspace).val();
			
			var link = contextPath + '/devolucao/consultaEncalhe/gerarSlip' +
									'?dataRecolhimentoInicial=' + dataRecolhimentoInicial +
									'&dataRecolhimentoFinal=' + dataRecolhimentoFinal +
									'&idFornecedor=' + idFornecedor +
									'&numeroCota=' + numeroCota;

			$("#download-iframe", ConsultaEncalhe.workspace).attr('src', link);
			
		},

		popupOutrosValores: function() {

			$("#dialog-outros-valores", ConsultaEncalhe.workspace).dialog({
				resizable : false,
				height : 430,
				width : 600,
				modal : true,
				buttons : [ 
					          {
						           id:"bt_fechar",
						           text:"Fechar", 
						           click: function() {
						        	   $( this ).dialog( "close" );
						           }
					           }
					      ],
				form: $("#dialog-outros-valores", this.workspace).parents("form")
			});

		},

		executarPreProcessamento: function(resultado) {
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids", ConsultaEncalhe.workspace).hide();

				return resultado.tableModel;
			}
			
			$.each(resultado.tableModel.rows, function(index, row) {
				
				var detalhes = '<a href="javascript:;" onclick="ConsultaEncalhe.popupDetalhe(\'' + row.cell.idCota + '\', \'' + row.cell.idFornecedor + '\', \'' + row.cell.idProdutoEdicao + '\', \'' + row.cell.dataMovimento + '\', \'' + row.cell.dataRecolhimento + '\');" style="cursor:pointer">' +
						 	   '<img title="Detalhes do Encalhe" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
							   '</a>';	
			
				row.cell.acao = detalhes;
				
			});
			
			$("#outrosValoresGrid", ConsultaEncalhe.workspace).flexAddData({
				page: resultado.tableModelDebitoCredito.page, 
				total: resultado.tableModelDebitoCredito.total, 
				rows: resultado.tableModelDebitoCredito.rows
			});
			
			$(".grids", ConsultaEncalhe.workspace).show();
			
			$("#totalReparte").text(parseFloat(resultado.valorReparte).toFixed(2));
			$("#totalEncalhe").text(parseFloat(resultado.valorEncalhe).toFixed(2));
			$("#valorVendaDia").text(parseFloat(resultado.valorVendaDia).toFixed(2));
			$("#totalOutrosValores").text(parseFloat(resultado.valorDebitoCredito).toFixed(2));
			$("#valorAPagar").text(parseFloat(resultado.valorPagar).toFixed(2));
			
			return resultado.tableModel;
			
		},
		
		executarPreProcessamentoDetalhe: function(resultado) {
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {
				
				$( "#dialog-detalhes-encalhe", ConsultaEncalhe.workspace ).dialog( "close" );
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);

				return resultado.tableModel;
			}
			
			$("#dataOperacao").html(resultado.dataOperacao);
			$("#codigoProduto").html(resultado.codigoProduto);
			$("#nomeProduto").html(resultado.nomeProduto);
			$("#edicaoProduto").html(resultado.numeroEdicao);

			$(".grids", ConsultaEncalhe.workspace).show();
			
			return resultado.tableModel;
			
		},
		
		popupDetalhe : function(idCota, idFornecedor, idProdutoEdicao, dataMovimento, dataRecolhimento) {
			
			ConsultaEncalhe.obterDetalhesEncalhe(idCota, idFornecedor, idProdutoEdicao, dataMovimento, dataRecolhimento);
			
			$( "#dialog-detalhes-encalhe", ConsultaEncalhe.workspace ).dialog({
				resizable: false,
				height:450,
				width:650,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_fechar",
					           text:"Fechar", 
					           click: function() {
					        	   
					        	   $( this ).dialog( "close" );
					           }
				           }
		        ],
				form: $("#dialog-detalhes-encalhe", this.workspace).parents("form")
			});
		},
		
	    //POPULA GRADE DE DETALHES DA ENCALHE
	    obterDetalhesEncalhe : function(idCota, idFornecedor, idProdutoEdicao, dataMovimento, dataRecolhimento){
	    	
			$("#dadosDetalheEncalheGrid", ConsultaEncalhe.workspace).flexOptions({
				url: contextPath + "/devolucao/consultaEncalhe/pesquisarDetalhe",
				params: [
						{name:'idProdutoEdicao', value: idProdutoEdicao},
						{name:'idFornecedor', value: idFornecedor},
						{name:'idCota', value: idCota},
						{name:'dataRecolhimento', value: dataRecolhimento},
						{name:'dataMovimento', value: dataMovimento}
				        ] ,
				        newp: 1
			});
			$("#dadosDetalheEncalheGrid", ConsultaEncalhe.workspace).flexReload();
			$(".grids", ConsultaEncalhe.workspace).show();
		},
		
		obterColModel : function() {


			var colModel = [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço com Desc. R$',
				name : 'precoComDesconto',
				width : 110,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 85,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor c/ Desc.',
				name : 'valorComDesconto',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Recolhimento',
				name : 'recolhimento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Detalhes',
				name : 'acao',
				width : 50,
				sortable : false,
				align : 'center',
			}];	
			
			return colModel;
	},
	
	obterColModelDetalhes : function() {


		var colModel = [ {
			display : 'Cota',
			name : 'numeroCota',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nomeCota',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Observação',
			name : 'observacao',
			width : 330,
			sortable : true,
			align : 'left'
		}];	
		
		return colModel;
	},
	
	obterColModelOutrosValores : function() {
		
		var colModel = [{
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
			display : 'Observações',
			name : 'observacoes',
			width : 140,
			sortable : true,
			align : 'left'
		}, {
			display : 'Valor R$',
			name : 'valor',
			width : 100,
			sortable : true,
			align : 'right'
		}];
	
		return colModel;
	}
	
}, BaseController);
