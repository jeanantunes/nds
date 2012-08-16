var ParciaisController = $.extend(true, {
	
	idProdutoEdicao : null,
	dataLancamento : null,
	dataRecolhimento : null,
	codigoProduto : null,
	nomeProduto : null,
	numEdicao : null,
	nomeFornecedor : null,
	precoCapa : null,
	statusParcial : null,
		
	idLancamento : null,
	
	init : function() {
		$( "#dataLancamentoEd", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dataRecolhimentoEd", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dataInicial", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dataFinal", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});

		$("#nomeProduto", this.workspace).autocomplete({source: ""});
		$("#edicaoProduto", this.workspace).autocomplete({source: ""});
		
		this.inicializarGrids();
	},
	
	/**
	 * Ação de clique do botão pesquisar
	 */
	cliquePesquisar : function() {
		
		if(this.get('codigoProduto').length!=0 && this.get('edicaoProduto').length!=0) {
			$('#painelLancamentos',this.workspace).hide();
			$('#painelPeriodos',this.workspace).show();	
			
			
			this.codigoProduto = this.get('codigoProduto');
			this.numEdicao = this.get('edicaoProduto');
			
			this.pesquisarPeriodosParciais();
		} else {
			$('#painelPeriodos',this.workspace).hide();
			$('#painelLancamentos',this.workspace).show();
			this.pesquisarLancamentosParciais();
		}		
	},
	
	pesquisarLancamentosParciais : function() {
		
		$(".parciaisGrid",this.workspace).flexOptions({			
			url : contextPath + "/parciais/pesquisarParciais",
			dataType : 'json',
			preProcess: this.processaRetornoPesquisaParciais,
			params:this.getDados()
		});
		
		$(".parciaisGrid", this.workspace).flexReload();
	},
	
	pesquisarPeriodosParciais : function() {
		
		$(".periodosGrid",this.workspace).flexOptions({			
			url : contextPath + "/parciais/pesquisarPeriodosParciais",
			dataType : 'json',
			preProcess: this.processaRetornoPeriodosParciais,
			params:this.getDados()
		});
		
		$(".periodosGrid",this.workspace).flexReload();
		
	},

	pesquisarPeriodosParciaisModal : function(codigoProduto,numEdicao) {
		
		var data = [];
		
		data.push({name: 'filtro.codigoProduto',	value: codigoProduto});
		data.push({name: 'filtro.edicaoProduto',	value: numEdicao});
		
		$(".parciaisPopGrid",this.workspace).flexOptions({			
			url : contextPath + "/parciais/pesquisarPeriodosParciais",
			dataType : 'json',
			preProcess: this.processaRetornoPeriodosParciaisModal,
			params:data
		});
		
		$(".parciaisPopGrid",this.workspace).flexReload();
		
	},

	inserirPeriodos : function(modal) {
		$.postJSON(contextPath + "/parciais/inserirPeriodos",
				this.getDadosNovosPeriodo(),
				function(result){
					if(modal)
						$(".parciaisPopGrid",this.workspace).flexReload();
					else
						$(".periodosGrid",this.workspace).flexReload();
				},
				null, 
				true,
				"dialog-detalhes");
		
		
	},	
	
	
	processaRetornoPesquisaParciais : function(result) {
		
		if(result.mensagens) 
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
		
		if(result.rows.length==0) {
			$("#exportacao",this.workspace).hide();
		} else {
			$("#exportacao",this.workspace).show();
		}
		
		$.each(result.rows, function(index,row){ParciaisController.gerarAcaoPrincipal(index,row);} );
				
		return result;
	},
	
	processaRetornoPeriodosParciais : function(result) {
		
		if(result.mensagens) 
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			
		
		if(result.rows.length==0) {
			$('#exportacaoPeriodos',this.workspace).hide();
		} else {
			$('#exportacaoPeriodos',this.workspace).show();
			this.idProdutoEdicao = result.rows[0].cell.idProdutoEdicao;
		}
		
		if(result.rows[0].cell.geradoPorInterface==true)
			$("#btnIncluirPeriodos",this.workspace).hide();
		else
			$("#btnIncluirPeriodos",this.workspace).show();		
		
		$.each(result.rows, function(index,row){ParciaisController.gerarAcaoDetalhes(index,row);} );
				
		return result;
	},
	
	processaRetornoPeriodosParciaisModal : function(result) {
		
		if(result.mensagens)
				exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens, "dialog-detalhes");
					
		if(result.rows.length==0) {
			$('#exportacaoPeriodosModal',this.workspace).hide();
		} else {
			$('#exportacaoPeriodosModal',this.workspace).show();
		}
		
		if(result.rows[0].cell.geradoPorInterface==true)
			$("#btnIncluirPeriodosModal",this.workspace).hide();
		else
			$("#btnIncluirPeriodosModal",this.workspace).show();		
		
		$.each(result.rows, function(index,row){ParciaisController.gerarAcaoDetalhes(index,row);} );
				
		return result;
	},
		
		
	
	/**
	 * Retorna todos os dados da tela principal no padrão utilizado pelo VRaptor
	 * @return Espelho de FiltroParciaisDTO (br.com.abril.nds.dto) 
	 */
	getDados : function() {
	
		var data = [];
		
		data.push({name:'filtro.codigoProduto',		value: this.get("codigoProduto")});
		data.push({name:'filtro.nomeProduto',		value: this.get("nomeProduto")});
		data.push({name:'filtro.edicaoProduto',		value: this.get("edicaoProduto")});
		data.push({name:'filtro.idFornecedor',		value: this.get("idFornecedor")});
		data.push({name:'filtro.dataInicial',		value: this.get("dataInicial")});
		data.push({name:'filtro.dataFinal',			value: this.get("dataFinal")});
		data.push({name:'filtro.status',			value: this.get("status")});
		
		data.push({name:'filtro.nomeFornecedor',	value: $('#idFornecedor option:selected').text()});
		
		return data;
	},
	
	getDadosNovosPeriodo : function() {
		
		var data = [];
		
		data.push({name:'peb',				value: this.get("peb")});
		data.push({name:'qtde',				value: this.get("qtde")});
		data.push({name:'idProdutoEdicao',	value: this.idProdutoEdicao});
		
		return data;
	},
	
	getDadosParaPeb : function() {
		
		var data = [];
		
		data.push({name:'codigoProduto',		value: this.codigoProduto});
		data.push({name:'edicaoProduto',		value: this.numEdicao});
		
		return data;
	},
	
	getDadosEdicaoPeriodo : function() {
		
		var data = [];
		
		data.push({name:'dataLancamento',		value: this.get('dataLancamentoEd')});
		data.push({name:'dataRecolhimento',		value: this.get('dataRecolhimentoEd')});
		data.push({name:'idLancamento',			value: this.idLancamento});	
		
		return data;
	},
	
	carregaPeb : function() {
		
		this.set('peb','');
		this.set('qtde','');
		
		$.postJSON(contextPath + "/parciais/obterPebDoProduto",
				this.getDadosParaPeb(),
				function(result){ParciaisController.set('peb',result);},
				null, 
				true,
				"dialog-novo");
		
	},
	
	gerarAcaoPrincipal : function(index,row) {
		row.cell.acao = 
			'<a href="javascript:;" onclick="ParciaisController.carregarDetalhes(\''+ 
				row.cell.idProdutoEdicao +'\', \''+
				row.cell.dataLancamento +'\', \''+
				row.cell.dataRecolhimento +'\', \''+
				row.cell.codigoProduto +'\', \''+
				row.cell.nomeProduto +'\', \''+
				row.cell.numEdicao +'\', \''+
				row.cell.nomeFornecedor +'\', \''+ 
				row.cell.precoCapa +'\', \''+ 
				row.cell.statusParcial + '\')">' +
				'<img src="'+contextPath+'/images/ico_detalhes.png" border="0" /></a>';
	},
	
	gerarAcaoDetalhes : function(index, row) {
		row.cell.vendas = '<a href="javascript:;" onclick="ParciaisController.detalheVendas(\'' +
		row.cell.dataLancamento +'\', \''+
		row.cell.dataRecolhimento +'\', \''+
		row.cell.idProdutoEdicao +'\', \''+
		'\');">' + row.cell.vendas + '</a>';
		
		row.cell.acao = 
			'<a href="javascript:;" ' +
			(row.cell.geradoPorInterface==true?'style="opacity: 0.5;"':'onclick="ParciaisController.carregarEdicaoDetalhes(\''+ 
					row.cell.idLancamento +'\', \''+
					row.cell.dataLancamento +'\', \''+
					row.cell.dataRecolhimento +
			        ' \')"')+
			        
			' ><img src="'+contextPath+'/images/ico_editar.gif" border="0" hspace="5" /></a>' +
			'<a href="javascript:;" '+
			(row.cell.geradoPorInterface==true?'style="opacity: 0.5;"':' onclick="ParciaisController.carregarExclusaoPeriodo(\'' + row.cell.idLancamento+ '\');" ')+
			'><img src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0" /></a>';
	},
	
	carregarDetalhes : function(idProdutoEdicao , dataLancamento, dataRecolhimento, codigoProduto, 
			nomeProduto, numEdicao, nomeFornecedor, precoCapa, statusParcial) {
				
		this.idProdutoEdicao = idProdutoEdicao;
		this.dataLancamento = dataLancamento;
		this.dataRecolhimento = dataRecolhimento;
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numEdicao = numEdicao;
		this.nomeFornecedor = nomeFornecedor;
		this.precoCapa = precoCapa;
		this.statusParcial = statusParcial;
		
		
		$('#codigoProdutoM',this.workspace).text(codigoProduto);
		$('#nomeProdutoM',this.workspace).text(nomeProduto);
		$('#numEdicaoM').text(numEdicao);
		$('#nomeFornecedorM',this.workspace).text(nomeFornecedor);
		$('#dataLancamentoM',this.workspace).text(dataLancamento);
		$('#dataRecolhimentoM',this.workspace).text(dataRecolhimento);
		
		this.pesquisarPeriodosParciaisModal(codigoProduto,numEdicao);
		
		this.popup_detalhes();
	},
	
	carregarEdicaoDetalhes : function(idLancamento, dataLancamento,dataRecolhimento) {
		
		this.idLancamento = idLancamento;
		
		$('#codigoProdutoEd',this.workspace).val(this.codigoProduto);
		$('#nomeProdutoEd',this.workspace).val(this.nomeProduto);
		$('#numEdicaoEd',this.workspace).val(this.numEdicao);
		$('#nomeFornecedorEd',this.workspace).val(this.nomeFornecedor);
		$('#dataLancamentoEd',this.workspace).val(dataLancamento);
		$('#dataRecolhimentoEd',this.workspace).val(dataRecolhimento);
		$('#precoCapaEd',this.workspace).val(this.precoCapa);
		
		this.popup_edit_produto();
	},
	
	carregarExclusaoPeriodo : function(idLancamento) {
		
		this.idLancamento = idLancamento;
		
		this.popup_excluir();
	},
	
	editarPeriodoParcial : function() {
		
		$.postJSON(contextPath + "/parciais/editarPeriodoParcial",
				this.getDadosEdicaoPeriodo(),
				function(result){

					$( "#dialog-edit-produto", this.workspace).dialog( "close" );
					
					if($('#painelPeriodos',this.workspace).css('display')=='none') {
						exibirMensagemDialog('SUCCESS', ['Período alterado com sucesso.'], "dialog-detalhes");			
						$(".parciaisPopGrid",this.workspace).flexReload();
					} else {
						$(".periodosGrid",this.workspace).flexReload();
						exibirMensagem('SUCCESS', ['Período alterado com sucesso.']);
					}
					
				},	
				null,
				true,
				'dialog-edit-produto');		
	},
	
	excluirPeriodoParcial : function() {
		
		var data = [];		
		data.push({name:'idLancamento',			value: this.idLancamento});	
		
		$.postJSON(contextPath + "/parciais/excluirPeriodoParcial",
				data,
				function(result){
			
					if($('#painelPeriodos',this.workspace).css('display')=='none') {
						exibirMensagemDialog('SUCCESS', ['Período excluido com sucesso.'], "dialog-detalhes");			
						$(".parciaisPopGrid",this.workspace).flexReload();
					} else {
						$(".periodosGrid",this.workspace).flexReload();
						exibirMensagem('SUCCESS', ['Período excluido com sucesso.']);
					}
					
					$( "#dialog-excluir", this.workspace).dialog( "close" );					
				},	
				function(result) {
					$( "#dialog-excluir", this.workspace).dialog( "close"); 
				},
				true,
				'dialog-detalhes');	
		
	},
	

	detalheVendas : function(dtLcto, dtRcto, idProdutoEdicao) {
		
		var data = [];
		
		data.push({name:'dtLcto',				value: dtLcto});
		data.push({name:'dtRcto',				value: dtRcto});
		data.push({name:'idProdutoEdicao',		value: idProdutoEdicao});
		
		
		$(".parciaisVendaGrid",this.workspace).flexOptions({			
			url : contextPath + "/parciais/pesquisarParciaisVenda",
			dataType : 'json',
			params: data
		});

		$(".parciaisVendaGrid",this.workspace).flexReload();

		pupup_detalheVendas();
	},
		
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	set : function(campo,value) {
				
		var elemento = $("#" + campo ,this.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	get : function(campo) {
		
		var elemento = $("#" + campo, this.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	

	popup : function(modal) {
		
			ParciaisController.carregaPeb();
		
			$( "#dialog-novo",this.workspace).dialog({
				resizable: false,
				height:200,
				width:400,
				modal: true,
				
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   	ParciaisController.inserirPeriodos(modal);								
									$( this ).dialog( "close" );
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this ).dialog( "close" );
					           }
				           }
		        ],
		        form: $("#dialog-novo", this.workspace).parents("form")
			
			});
		},
		
		pupup_detalheVendas : function() {
			
			$( "#dialog-detalhe-venda", this.workspace).dialog({
				resizable: false,
				height:450,
				width:660,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
						
					},
				},
				form: $("#dialog-detalhe-venda", this.workspace).parents("form")
			});
		},
		
		popup_alterar : function() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-novo", this.workspace).dialog({
				resizable: false,
				height:200,
				width:350,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-novo", this.workspace).parents("form")
			});	
			      
		},
		
		popup_excluir : function() {
		
			$( "#dialog-excluir", this.workspace).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						ParciaisController.excluirPeriodoParcial();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-excluir", this.workspace).parents("form")
			});
		},
		
		popup_detalhes : function() {
		
			$( "#dialog-detalhes", this.workspace).dialog({
				resizable: false,
				height:550,
				width:960,
				modal: true,
				buttons: {
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-detalhes", this.workspace).parents("form")
			});
		},
		
		popup_edit_produto : function() {
		
			$( "#dialog-edit-produto", this.workspace).dialog({
				resizable: false,
				height:360,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						ParciaisController.editarPeriodoParcial();
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-edit-produto", this.workspace).parents("form")
			});
		},
		
		inicializarGrids : function() {
			

			$(".parciaisGrid", this.workspace).flexigrid({
				colModel : [ {
					display : 'Data Lancto',
					name : 'dataLancamento',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Recolhimento',
					name : 'dataRecolhimento',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Código',
					name : 'codigoProduto',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numEdicao',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status',
					name : 'statusParcial',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 80,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigoProduto",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
		}); 	

		$(".grids", this.workspace).show();	


		$(".periodosGrid", this.workspace).flexigrid({
				colModel : [ {
					display : 'Lcto',
					name : 'dataLancamento',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Rcto',
					name : 'dataRecolhimento',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Reparte',
					name : 'reparte',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Suplementação',
					name : 'suplementacao',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Encalhe',
					name : 'encalhe',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Venda',
					name : 'vendas',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : '% Venda',
					name : 'percVenda',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Venda CE',
					name : 'vendaCE',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Reparte Acum.',
					name : 'reparteAcum',
					width : 75,
					sortable : true,
					align : 'center'
				}, {
					display : 'Venda Acum.',
					name : 'vendaAcumulada',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : '% Venda Acum.',
					name : 'percVendaAcumulada',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],

				sortname : "dataLancamento",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
			}); 

		$(".parciaisPopGrid", this.workspace).flexigrid({
				colModel : [ {
					display : 'Lcto',
					name : 'dataLancamento',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Rcto',
					name : 'dataRecolhimento',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Reparte',
					name : 'reparte',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Suplementação',
					name : 'suplementacao',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Encalhe',
					name : 'encalhe',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Venda',
					name : 'vendas',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : '% Venda',
					name : 'percVenda',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Venda CE',
					name : 'vendaCE',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Reparte Acum.',
					name : 'reparteAcum',
					width : 75,
					sortable : true,
					align : 'center'
				}, {
					display : 'Venda Acum.',
					name : 'vendaAcumulada',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : '% Venda Acum.',
					name : 'percVendaAcumulada',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "dataLancamento",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 900,
				height : 200
			}); 
			

		$(".parciaisVendaGrid", this.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Juramentada',
				name : 'vendaJuramentada',
				width : 110,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 595,
			height : 200
		});
	}
	
	
}, BaseController);

$(function() {
	
	ParciaisController.init();
				
});
