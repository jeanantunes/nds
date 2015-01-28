var entradaNFETerceirosController = $.extend(true, {
	
	path : contextPath + "/nfe/entradaNFETerceiros/",
	
	init : function() {
		$( "#dataInicial", this.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#dataFinal", this.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		this.initEncalheNfeGrid();
		this.initNotaRecebidaGrid();
		this.initPesqProdutosNotaGrid();
	},
	
	

	cadastrarNota : function(idControleConferenciaEncalheCota){		

		$.postJSON(
				this.path +'cadastrarNota',
				[
					{ name: "nota.numero", value: $('#numeroNotaCadastroNota', this.workspace).val() },
					{ name: "nota.serie", value: $('#serieNotaCadastroNota', this.workspace).val() },
					{ name: "nota.chaveAcesso", value: $('#chaveAcessoCadastroNota', this.workspace).val() },
					{ name: "nota.valorNF", value: $('#valorNotaCadastroNota', this.workspace).val() },
					{ name: "numeroCota", value: $('#cotaCadastroNota', this.workspace).val() },
					{ name: "idControleConferenciaEncalheCota", value: idControleConferenciaEncalheCota }
				],
				function(result) {

					if (result.listaMensagens) {

						exibirMensagem(
							result.tipoMensagem, 
							result.listaMensagens
						);
					}
				},
				null,
				true
			);
	},
  
	callback : function() {
			setTimeout(function() {
				$( "#effect:visible", this.workspace).removeAttr( "style" ).fadeOut();

		}, 1000 );
	},	

	
	confirmar : function(){
		$(".dados", this.workspace).show();
	},
	
	pesquisarEncalhe : function(){
		$(".dadosFiltro", this.workspace).show();		
		var status = $('#situacaoNfe', this.workspace).val();		

		$("#btnRegistrarNFe").hide();
		
		if(status != ''){
			if(status == 'RECEBIDA'){
				$("#btnRegistrarNFe").show();
				this.pesquisarNotaRecebidas();		
			}else{			
				this.pesquisarNotasPendente();
			}
		}else{
			exibirMensagem("WARNING", ["Escolha o status da nota"]);
			
		}
		
	},
	
	pesquisarNotaRecebidas : function(){
		
		var params = $("#form-pesquisa-nfe").serialize();
		
		$(".notaRecebidaGrid", this.workspace).flexOptions({
			url: this.path + "pesquisarNotasRecebidas?" + params,
			newp : 1
		});

		$(".notaRecebidaGrid", this.workspace).flexReload();
	},
	
	pesquisarNotasPendente : function(){
		
		var params = $("#form-pesquisa-nfe").serialize();
		
		$(".encalheNfeGrid", this.workspace).flexOptions({
			url: this.path +"pesquisarNotasPendentes?" + params,
			newp: 1
		});

		$(".encalheNfeGrid", this.workspace).flexReload();		
	},
	
	mostrar_nfes : function(){
		$(".nfes", this.workspace).show();
	},
	
	executarPreProcessamento : function(resultado) {
			
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", this.workspace).hide();

			return resultado;
		}
		var status = $('#situacaoNfe', this.workspace).val();					
		if(status == 'RECEBIDA'){
			$.each(resultado.rows, function(index, row) {
				
				var linkAviso = '<a href="javascript:;" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_alert.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAviso;
			});
			
		}else{
			
			$.each(resultado.rows, function(index, row) {					
				
				var linkLancamento = '<a isEdicao="true" href="javascript:;"  onclick="entradaNFETerceirosController.popup_nfe(\''+
									 row.cell.numeroCota+'\',\''+row.cell.nome+'\',\''+row.cell.idControleConferenciaEncalheCota+
									 '\');" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_lancamento.png" hspace="5" border="0px" />' +
								   '</a>';
				var linkCadastro = '<a isEdicao="true" href="javascript:;" onclick="entradaNFETerceirosController.popup_dadosNotaFiscal('+row.cell.numeroNfe+','
					   																		+row.cell.dataEncalhe+','
					   																		+row.cell.chaveAcesso+','
					   																		+row.cell.serie+','
					   																		+row.cell.valorNota+','
					   																		+row.cell.idControleConferenciaEncalheCota+','
					   																		+row.cell.idNotaFiscalEntrada+');" style="cursor:pointer">' +
							   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_cadastros.png" hspace="5" border="0px" />' +
		                         '</a>';

			   row.cell.acao = linkLancamento + linkCadastro;
			});
		
		}
		
		$(".grids", this.workspace).show();
		
		return resultado;
	},

	executarPreProcessamentoProdutosNotaFiscal: function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);

			return resultado;
		}

		$.each(resultado.rows, function(index, row) {

			var inputQtdeInfo  = "<input type='text' name='inputQtdeInfo' style='text-align:center' size='5' value='" + row.cell.qtdInformada + "' />";
			var inputPrecoCapa = "<input type='text' name='inputPrecoCapa' style='text-align:right' size='10' value='" + row.cell.precoCapaFormatado + "' />";

			row.cell.qtdInformada = inputQtdeInfo;
			row.cell.precoCapaFormatado = inputPrecoCapa;
		});

		return resultado;
	},
	
	//***OK***//
	pesquisarCota : function() {
 		
		numeroCota = $("#entrada-terceiro-numeroCota", this.workspace).val();
 		
 		$.postJSON(
			contextPath + '/nfe/entradaNFETerceiros/buscarCotaPorNumero',
			{ "numeroCota": numeroCota },
			function(result) {
				
				$("#nomeCota", this.workspace).html(result);
				
			},
			null,
			true
		);
 	},
 	
 	//***OK***//
	pesquisarCotaCadastroNota : function() {
 		
		numeroCota = $("#cotaCadastroNota", this.workspace).val();
 		
 		$.postJSON(
			contextPath + '/nfe/entradaNFETerceiros/buscarCotaPorNumero',
			{ "numeroCota": numeroCota },
			function(result) {
				
				$("#nomeCotaCadastroNota", this.workspace).val(result);
				
			},
			null,
			true
		);
 	},
 	
 	popup : function() {
		
		$( "#dialog-novo", this.workspace ).dialog({
			resizable: 
				 false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-novo", this.workspace).dialog("close")
		});
	},
	
	popup_confirm : function() {
	
		$( "#dialog-confirm", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:280,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-confirm", this.workspace).dialog("close")
		});	
		      
	},
	
	popup_rejeitar : function() {
	
		$( "#dialog-rejeitar", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:280,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-rejeitar", this.workspace).dialog("close")
		});
	},
	
	popup_dadosNotaFiscal : function(numeroNfe, dataEncalhe, chaveAcesso, serie, valorNota, idControleConferenciaEncalheCota, statusNotaFiscalEntrada) {
		
		$('#numeroNotaFiscalPopUp', this.workspace).text(numeroNfe);
		$('#dataNotaFiscalPopUp', this.workspace).text(dataEncalhe);
		$('#chaveAcessoNotaFiscalPopUp', this.workspace).text(chaveAcesso);
		$('#serieNotaFiscalPopUp', this.workspace).text(serie);
		$('#valorNotaFiscalPopUp', this.workspace).text(valorNota);
		
		$(".pesquisarProdutosNotaGrid", entradaNFETerceirosController.workspace).flexOptions({
			url: contextPath + "/nfe/entradaNFETerceiros/pesquisarItensPorNota",
			params: [{name:"idControleConferencia", value:idControleConferenciaEncalheCota}],
			dataType : 'json'
		});

		$(".pesquisarProdutosNotaGrid", entradaNFETerceirosController.workspace).flexReload();
	
		$( "#dialog-dadosNotaFiscal", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:860,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-dadosNotaFiscal", this.workspace).parents("form")
		});	
		      
	},
	
	popup_confirmar : function() {
	
		$( "#dialog-confirmar-cancelamento", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:280,
			modal: true,
			buttons: {
				"Confirmar": function() {					
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-confirmar-cancelamento", this.workspace).parents("form")
		});
	},
	
	popup_nfe : function(numeroCota, nome, idControleConferenciaEncalheCota){
		
		$('#serieNotaCadastroNota', this.workspace).val('');
		$('#chaveAcessoCadastroNota', this.workspace).val('');
		$('#valorNotaCadastroNota', this.workspace).val('');
		$('#numeroNotaCadastroNota', this.workspace).val('');
		
		if(numeroCota != '0'){
			$('#cotaCadastroNota', this.workspace).val(numeroCota);
			$('#cotaCadastroNota', this.workspace).attr('readonly', true);
			$('#nomeCotaCadastroNota', this.workspace).val(nome);			
		}else{
			$('#cotaCadastroNota', this.workspace).val('');
			$('#cotaCadastroNota', this.workspace).attr('readonly', false);
			$('#nomeCotaCadastroNota', this.workspace).val('');
		}
		$('#nomeCotaCadastroNota', this.workspace).attr('readonly', true);
		
		$('#valorNotaCadastroNota', this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});

		$( "#dialog-nfe", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					entradaNFETerceirosController.cadastrarNota(idControleConferenciaEncalheCota);
					$( this ).dialog( "close" );					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-nfe", this.workspace).parents("form")
		});
	},
	
	initEncalheNfeGrid :function(){
		$(".encalheNfeGrid", this.workspace).flexigrid({
			preProcess: this.executarPreProcessamento,
			onSuccess: function() {bloquearItensEdicao(entradaNFETerceirosController.workspace);},
			dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Data Encalhe',
					name : 'dataEncalhe',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo de Nota',
					name : 'tipoNotaFiscal',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor Nota R$',
					name : 'valorNota',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Valor Real R$',
					name : 'valorReal',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Diferença',
					name : 'diferenca',
					width : 60,
					sortable : true,
					align : 'right'
				}, {
					display : 'Status',
					name : 'status',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 50,
					sortable : true,
					align : 'center'
				}],
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto'
			});
	},
	
	initNotaRecebidaGrid : function() {
		$(".notaRecebidaGrid").flexigrid({
			preProcess: this.executarPreProcessamento,
			dataType : 'json',
				colModel : [ {
					display : 'Nº Nota',
					name : 'numeroNota',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Série',
					name : 'serie',
					width : 30,
					sortable : true,
					align : 'left'
				}, {
					display : 'Chave Acesso',
					name : 'chaveAcesso',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'Dt. Emissão',
					name : 'dataEmissao',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo Nota',
					name : 'tipoNotaFiscal',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor/Cota',
					name : 'nome',
					width : 210,
					sortable : true,
					align : 'left'
				}, {
					display : 'Vlr. Nota R$',
					name : 'valorNota',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 30,
					sortable : true,
					align : 'center'
				}],
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
		});
	},
	
	initPesqProdutosNotaGrid : function() {
		$(".pesquisarProdutosNotaGrid", this.workspace).flexigrid({
			preProcess: this.executarPreProcessamentoProdutosNotaFiscal,
			dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 120,
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
					name : 'precoCapaFormatado',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Desc R$',
					name : 'precoDescontoFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total R$',
					name : 'totalDoItemFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				}],
				sortname : "codigoProduto",
				sortorder : "asc",
				width : 810,
				height : 250
			});
	}
	
}, BaseController);