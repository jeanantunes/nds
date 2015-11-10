var params = new Array();

var estornoNFEController  = $.extend(true, {

	path : contextPath +"/nfe/estornoNFE/",
	
	init : function() {
		
		this.initDatas();
		this.initFlexiGrids();
		this.bindButtons();
		this.createDialog();
		
	},

	createDialog : function() {
		$("#dialog-novo", this.workspace).dialog({
			resizable : false,
			height : 250,
			width : 650,
			modal : true,
			buttons : [{
				id:"btnDialogNovoConfirmar",
				text:"Confirmar",
				click: function() {
					estornoNFEController.salvar(this);
				}},{
					id:"btnDialogNovoCancelar",
					text:"Cancelar",
					click : function() {
				    
					$(this).dialog("close");
				}
			}],
			beforeClose : clearMessageDialogTimeout,
			form: $("#dialog-novo", this.workspace).parents("form")
		});
		$("#dialog-novo", this.workspace).dialog("close");
	},
	
	showPopupEditar : function(title) {
		$("#dialog-novo", this.workspace)
			.dialog( "option" ,  "title", title )
			.dialog( "open" );
		
	},
	
	initDatas : function() {
		$(".input-date").datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true
		});
		
		$(".input-date").mask("99/99/9999");
		
		$("#estorno-dataEmissao").val(formatDateToString(new Date()));
		
		$("#estorno-numero", this.workspace).numeric();
	},
	
	initButtons : function() {
		
		var _this = this;

		$("#estornoNFEConfirmar", this.workspace).click(function() {
			_this.btnConfirmar();
		});
	},
	
	btnConfirmar : function() {
		
		if(this.isEmptyOrNull()) {
			return;
		}
		
		var parametros = this.param();

		$.postJSON(this.path + 'confirmar', parametros, function(data) {
			
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			exibirMensagem("SUCCESS", ["Operação realizada com sucesso!"]);
			
		});		
		
	},
	
	limparTabela : function() {
		
	},

	isEmptyOrNull : function () {
		
		var mensagens = []; 
		
		if($("#estorno-numero").val() == '') {
			mensagens.push("O ['Número da Nota'] não pode ser nula");
		}
		
		if($("#estorno-serie").val() == '') {
			mensagens.push("A ['Serie da Nota'] não pode ser nula");
		}
		
		if($("#estorno-dataEmissao").val() == '') {
			mensagens.push("A ['Data Emissão'] não pode ser nula");
		}
		
		if(mensagens.length > 0) {
			exibirMensagem('WARNING', mensagens);
			return true;
		}
	},
	
	param : function () {
		
		params.push({name:"filtro.numeroNotaInicial" , value: $("#estorno-numero").val()});
    	params.push({name:"filtro.serie" , value: $("#estorno-serie").val()});
    	params.push({name:"filtro.dataInicial" , value: $("#estorno-dataEmissao").val()});
    	params.push({name:"filtro.dataFinal" , value: $("#estorno-dataEmissao").val()});
    	params.push({name:"filtro.chaveAcesso" , value: $("#estorno-chaveAcesso").val()});
    	
    	return params;
	},
	
	exibirMensagemSucesso : function (result){
		
		var tipoMensagem = result.tipoMensagem;
		var listaMensagens = result.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		}
	},
	
	initFlexiGrids : function() {
		$(".estornoGrid", this.workspace).flexigrid({
			preProcess : function(data) {
				if(typeof data.mensagens == "object") {

					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);

				} else {

					$.each(data.rows, function(index, value) {
						
						var idNota = value.cell.id;
						
						acao += '</a> <a href="javascript:;" onclick="estornoNFEController.estornoNotaFiscal(' + idNota + ');""><img src="' + contextPath + '/images/ico_excluir.gif" border="0" /></a>';
						
					});
					
					return data;
				}

			},
			
			dataType : 'json',
			colModel : [{
				display : 'Número da Nota',
				name : 'numeroNota',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Série',
				name : 'serieNota',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Chave de Acesso',
				name : 'chaveAcesso',
				width : 350,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Emissão',
				name : 'dataEmissao',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "numero",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
		});

	},
	
	buscar : function() {
		
		if(this.isEmptyOrNull()) {
			$(".grids", estornoNFEController.workspace).hide();
			return;
		} else {
			estornoNFEController.initFlexiGrids();
			
			var parametros = this.param();
			
			$(".estornoGrid", estornoNFEController.workspace).flexOptions({
				preProcess: estornoNFEController.executarPreProcessamento,
				url: contextPath + "/nfe/estornoNFE/pesquisar",
				dataType : 'json',
				params: parametros
				
			});
			
			$(".estornoGrid").flexReload();
			$(".grids", estornoNFEController.workspace).show();
			
		}
		
		
	},
	
	executarPreProcessamento : function(resultado) {
		
		if( typeof resultado.mensagens == "object") {
			$(".grids", estornoNFEController.workspace).hide();
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
		} else {
			
			$.each(resultado.rows, function(index, value) {
				var idNota = value.cell.id;
				var acao = '</a> <a href="javascript:;" onclick="estornoNFEController.estornoNotaFiscal(' + idNota + ');""><img src="' + contextPath + '/images/ico_excluir.gif" border="0" /></a>';
				
				value.cell.acao = acao;
			});
			
			return resultado;
		}
		
	},
	
	bindButtons : function() {
		
		var _this = this;

		$("#estornoNFEConfirmar", this.workspace).click(function() {
			_this.buscar();
		});
	},
	
	showPopupEditar : function(title) {
		$("#dialog-novo", this.workspace)
			.dialog( "option" ,  "title", title )
			.dialog( "open" );
		
	},
	
	salvar : function(dialog) {
		
		if(this.isEmptyOrNull()) {
			return;
		}
		
		var parametros = this.param();

		$.postJSON(this.path + 'pesquisar', parametros, function(data) {
			
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
				return;
			}
			
			exibirMensagem("SUCCESS", ["Operação realizada com sucesso!"]);
			$("#effect").show("highlight", {}, 1000, callback);
			$(".grids", this.workspace).show();
			estornoNFEController.buscar(this);
			
		});
		
	},
}, BaseController);
//@ sourceURL=estornoNFE.js