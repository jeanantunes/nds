var entradaNFETerceirosController = $.extend(true, {
	popup : function() {
		
		$( "#dialog-novo", entradaNFETerceirosController.workspace ).dialog({
			resizable: 
				 false,
			height:'auto',
			width:200,
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
	
		$( "#dialog-confirm", entradaNFETerceirosController.workspace ).dialog({
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
	
		$( "#dialog-rejeitar", entradaNFETerceirosController.workspace ).dialog({
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
	
	popup_dadosNotaFiscal : function(numeroNfe, dataEncalhe, chaveAcesso, serie, vlrNota, idNotaFiscalEntrada) {
		
		$('#numeroNotaFiscalPopUp', entradaNFETerceirosController.workspace).text(numeroNfe);
		$('#dataNotaFiscalPopUp', entradaNFETerceirosController.workspace).text(dataEncalhe);
		$('#chaveAcessoNotaFiscalPopUp', entradaNFETerceirosController.workspace).text(chaveAcesso);
		$('#serieNotaFiscalPopUp', entradaNFETerceirosController.workspace).text(serie);
		$('#valorNotaFiscalPopUp', entradaNFETerceirosController.workspace).text(vlrNota);
		
		$(".pesqProdutosNotaGrid", entradaNFETerceirosController.workspace).flexOptions({
			url: contextPath + "/nfe/entradaNFETerceiros/pesquisarItensPorNota",
			dataType : 'json',
			params: [{name:'filtro.codigoNota', value:idNotaFiscalEntrada}]
		});

		$(".pesqProdutosNotaGrid", entradaNFETerceirosController.workspace).flexReload();
	
		$( "#dialog-dadosNotaFiscal", entradaNFETerceirosController.workspace ).dialog({
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
			form: $("#dialog-dadosNotaFiscal", this.workspace).dialog("close")
		
		
		});	
		      
	},
	
	popup_confirmar : function() {
	
		$( "#dialog-confirmar-cancelamento", entradaNFETerceirosController.workspace ).dialog({
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
	
	popup_nfe : function(numeroCota, nome){
		
		if(numeroCota != '0'){
			$('#cotaCadastroNota', entradaNFETerceirosController.workspace).val(numeroCota);
			$('#nomeCotaCadastroNota', entradaNFETerceirosController.workspace).val(nome);
			$('#cotaCadastroNota', entradaNFETerceirosController.workspace).attr('disabled', 'disabled');
			$('#nomeCotaCadastroNota', entradaNFETerceirosController.workspace).attr('disabled', 'disabled');
			}
			
		$( "#dialog-nfe", entradaNFETerceirosController.workspace ).dialog({
			resizable: false,
			height:280,
			width:350,
			modal: true,
			buttons: {
				"Confirmar": function() {
					entradaNFETerceirosController.cadastrarNota();
					$( this ).dialog( "close" );					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-nfe", this.workspace).parents("form")
		});
	},

	cadastrarNota : function(){		

		$.postJSON(
				contextPath + '/nfe/entradaNFETerceiros/cadastrarNota',
				[
					{ name: "nota.numero", value: $('#numeroNotaCadastroNota', entradaNFETerceirosController.workspace).val() },
					{ name: "nota.serie", value: $('#serieNotaCadastroNota', entradaNFETerceirosController.workspace).val() },
					{ name: "nota.chaveAcesso", value: $('#chaveAcessoCadastroNota', entradaNFETerceirosController.workspace).val() },
					{ name: "numeroCota", value: $('#cotaCadastroNota', entradaNFETerceirosController.workspace).val() },
				],
				function(result) {
					alert(result);					
				},
				null,
				true
			);
	
	},
  
	callback : function() {
			setTimeout(function() {
				$( "#effect:visible", entradaNFETerceirosController.workspace).removeAttr( "style" ).fadeOut();

		}, 1000 );
	},	
	
	init : function() {
		$( "#data", entradaNFETerceirosController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$(".pesqProdutosNotaGrid", entradaNFETerceirosController.workspace).flexigrid({
			preProcess: entradaNFETerceirosController.executarPreProcessamento,
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
					align : 'right'
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
				width : 810,
				height : 250
			});
		$(".notaRecebidaGrid", entradaNFETerceirosController.workspace).flexigrid({
			preProcess: entradaNFETerceirosController.executarPreProcessamento,
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
					width : 280,
					sortable : true,
					align : 'left'
				}, {
					display : 'NF-e',
					name : 'numeroNfe',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Série',
					name : 'serie',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Chave Acesso',
					name : 'chaveAcesso',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : '',
					name : 'acao',
					width : 25,
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
		
		
		$(".encalheNfeGrid", entradaNFETerceirosController.workspace).flexigrid({
			preProcess: entradaNFETerceirosController.executarPreProcessamento,
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
					name : 'tipoNota',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor Nota R$',
					name : 'vlrNotaFormatado',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Valor Real R$',
					name : 'vlrRealFormatado',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Diferença',
					name : 'diferencaFormatado',
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
				}, {
					display : ' ',
					name : 'sel',
					width : 20,
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
	
	confirmar : function(){
		$(".dados", entradaNFETerceirosController.workspace).show();
	},
	
	pesqEncalhe : function(){
		$(".dadosFiltro", entradaNFETerceirosController.workspace).show();		
		var status = $('#situacaoNfe', entradaNFETerceirosController.workspace).val();		
		if(status == 'RECEBIDA'){			
			entradaNFETerceirosController.pesquisarNotaRecebidas();		
		}else{			
			entradaNFETerceirosController.pesquisarNotasPendente();
		}
		
	},
	
	pesquisarNotaRecebidas : function(){
		
		$(".notaRecebidaGrid", entradaNFETerceirosController.workspace).flexOptions({
			url: contextPath + "/nfe/entradaNFETerceiros/pesquisarNotasRecebidas",
			dataType : 'json',
			params: [
						{name:'filtro.codigoCota', value:$('#codigoCota', entradaNFETerceirosController.workspace).val()},
						{name:'filtro.data', value:$('#data', entradaNFETerceirosController.workspace).val()},
						{name:'filtro.statusNotaFiscalEntrada', value:$('#situacaoNfe', entradaNFETerceirosController.workspace).val()}						
						]
		});

		$(".notaRecebidaGrid", entradaNFETerceirosController.workspace).flexReload();
	},
	
	pesquisarNotasPendente : function(){
		
		$(".encalheNfeGrid", entradaNFETerceirosController.workspace).flexOptions({
			url: contextPath + "/nfe/entradaNFETerceiros/pesquisarNotasPendentes",
			dataType : 'json',
			params: [
						{name:'filtro.codigoCota', value:$('#codigoCota', entradaNFETerceirosController.workspace).val()},
						{name:'filtro.data', value:$('#data', entradaNFETerceirosController.workspace).val()},
						{name:'filtro.statusNotaFiscalEntrada', value:$('#situacaoNfe', entradaNFETerceirosController.workspace).val()}						
						]
		});

		$(".encalheNfeGrid", entradaNFETerceirosController.workspace).flexReload();		
	},
	
	mostrar_nfes : function(){
		$(".nfes", entradaNFETerceirosController.workspace).show();
	},

	executarPreProcessamento : function(resultado) {
			
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", entradaNFETerceirosController.workspace).hide();

			return resultado;
		}
		var status = $('#situacaoNfe', entradaNFETerceirosController.workspace).val();					
		if(status == 'RECEBIDA'){
			$.each(resultado.rows, function(index, row) {
				
				var linkAviso = '<a href="javascript:;" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_alert.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAviso;
			});
			
		}else{
			
			$.each(resultado.rows, function(index, row) {					
				
				var linkLancamento = '<a href="javascript:;"  onclick="entradaNFETerceirosController.popup_nfe(\''+row.cell.numeroCota+'\',\''+row.cell.nome+'\');" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_lancamento.png" hspace="5" border="0px" />' +
								   '</a>';
			   var linkCadastro = '<a href="javascript:;" onclick="entradaNFETerceirosController.popup_dadosNotaFiscal('+row.cell.numeroNfe+','
					   																		+row.cell.dataEncalhe+','
					   																		+row.cell.chaveAcesso+','
					   																		+row.cell.serie+','
					   																		+row.cell.vlrNota+','
					   																		+row.cell.idNotaFiscalEntrada+');" style="cursor:pointer">' +
							   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_cadastros.png" hspace="5" border="0px" />' +
		                         '</a>';
               var checkBox = '<input type="checkbox" id="checkNota" name="checkNota" />';
				
				row.cell.acao = linkLancamento + linkCadastro;
				row.cell.sel = checkBox;
			});
		
		}
		
		$(".grids", entradaNFETerceirosController.workspace).show();
		
		return resultado;
	},
	
	pesquisarCota : function() {
 		
		numeroCota = $("#codigoCota", entradaNFETerceirosController.workspace).val();
 		
 		$.postJSON(
			contextPath + '/nfe/entradaNFETerceiros/buscarCotaPorNumero',
			{ "numeroCota": numeroCota },
			function(result) {

				$("#nomeCota", entradaNFETerceirosController.workspace).html(result);
				
			},
			null,
			true
		);
 	}
	
}, BaseController);