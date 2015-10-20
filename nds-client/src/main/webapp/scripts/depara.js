var deparaController = $.extend(true, {
	
	depara : {},
	path : contextPath + '/cadastro/depara/',
	intGridDepara : function() {
		$(".deparaGrid", this.workspace).flexigrid({
			preProcess : function(data) {
				if(typeof data.mensagens == "object") {

					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);

				} else {
					$.each(data.rows, function(index, value) {
						var idDepara = value.cell.id;
						var acao = '<a href="javascript:;" onclick="deparaController.editar(' + idDepara + ');"><img src="' + contextPath + '/images/ico_editar.gif" border="0" style="margin-right:10px;" />';
						acao += '</a> <a href="javascript:;" onclick="deparaController.excluir(' + idDepara + ');""><img src="' + contextPath + '/images/ico_excluir.gif" border="0" /></a>';

						value.cell.acao = acao;						
									
						
						value.cell.id = '<a href="javascript:;" onclick="deparaController.detalhe(' + idDepara + ');">'+value.cell.id+'</a>';
					});

					return data;
				}

			},
			dataType : 'json',
			colModel : [{
				display : 'Depara',
				name : 'id',
				width : 120,
				
				sortable : true,
				align : 'left'
			},
			 {
				display : 'FC',
				name : 'fc',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Dinap',
				name : 'dinap',
				width : 220,
				sortable : true,
				align : 'left'
			} ,{
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "fc",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
		});
		$(".deparaGrid", this.workspace).flexOptions({
			"url" : this.path + 'busca.json',
			params : [{
				name : "fc",
				value : $("#pesquisaFc", this.workspace).val()
			}, {
				name : "dinap",
				value : $("#pesquisaDinap", this.workspace).val()
			}]
		});

	},
	buscar : function(obj) { 
		var serializedObj = $("#pesquisar_depara_form", this.workspace).serialize();
		
		var fc = $("#pesquisaFc").val();
		var dinap = $("#pesquisaDinap").val();
		
		$(".deparaGrid", this.workspace).flexOptions({
			"url" : this.path + 'busca.json', 
			params: [{name:'depara.fc', value: fc },
			         {name:'depara.dinap', value: dinap }],			
			newp:1
		});
		$(".deparaGrid").flexReload();
		$("#fileExport").show();
	},
	bindButtons : function() {
		$("#btnPesquisar", this.workspace).click(function() {
			deparaController.buscar(this);
		
			$(".grids").show();
			
			
		});
		$("#btnNovo", this.workspace).click(function() {
			deparaController.novo();
		});
	},
	bindInputEvent : function() {
		$('#pesquisaFc, #deparaFc').numeric();
		$('#pesquisaFc').bind('keypress', function(e) {
			if(e.keyCode == 13) {
				deparaController.buscar();
				$(".grids", this.workspace).show();
			}
		});
	},
	init : function() {
		this.intGridDepara();
		this.initGridDetalhe();
		this.bindButtons();
		this.bindInputEvent();
		this.createDialog();
	},
	createDialog : function() {
		$("#dialog-novo", this.workspace).dialog({
			resizable : false,
			height : 230,
			width : 400,
			modal : true,
			buttons : [{
				id:"btnDialogNovoConfirmar",
				text:"Confirmar",
				click: function() {
					deparaController.salvar(this);

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
	editar : function(id) {
		alert(id);
		$.postJSON(this.path + 'buscaPorId.json', {
			'id' : id
		}, function(data) {
			if( typeof data.mensagens == "object") {
				exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			} else {
				$("#deparaId").val(data.depara.id);
				$("#deparaFc").val(data.depara.codigo);
				$("#deparaDinap").val(data.depara.nome);
				

				if (data.emUso) {
					$("#deparaFc").attr("readOnly", "readOnly");
					$("#deparaDinap").attr('disabled', true);
				} else {
					$("#deparaFc").removeAttr("readOnly");
					$("#deparaDinap").attr('disabled', false);
				}
				deparaController.showPopupEditar('Editar Depara');
			}
		});
	},
	getData : function() {		
		this.depara.fc = parseInt($("#deparaFc").val());
		this.depara.dinap = $("#deparaDinap").val();
		
	},	
	salvar : function(dialog) {

		$("#deparaFc").attr('disabled', false);
		var obj = $("#novo_depara_form", this.workspace).serialize();
		$("#deparaFc").attr('disabled', true);
		
		$.postJSON(this.path + 'salvar.json', obj, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if(tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			}

			$(dialog).dialog("close");
			$("#effect").show("highlight", {}, 1000, callback);
			$(".grids", this.workspace).show();
			deparaController.buscar(this);

		}, null, true);
	},
	excluir : function(id) {
		
		if(!verificarPermissaoAcesso(this.workspace)){
			return;
		}
		
		$("#dialog-excluir", this.workspace).dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : [{
				id:"btnDialogExcluirConfirmar",
				text:"Confirmar", click : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
					deparaController.remove(id);
				}},{
					id:"btnDialogExcluirCancelar",
					text:"Cancelar",click : function() {
					$(this).dialog("close");
				}
			}],
			form: $("#dialog-excluir", this.workspace).parents("form")
		});
	},
	remove : function(id) {
		$.postJSON(this.path + 'remove.json', {
			'id' : id
		}, function(data) {
			if( typeof data.mensagens == "object") {
				exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			}
			$(".deparaGrid", this.workspace).flexReload();
			
		});
	},
	novo : function() {
		
		if(!verificarPermissaoAcesso(this.workspace))
			return;
		
		var data = {
			depara : {}
		};
		
		this.clearForm($("#novo_depara_form", this.workspace));

		$("#deparaFc").removeAttr("readOnly");
		$("#deparaDinap").attr('disabled', false);		
		
		this.bindData(data, $("#novo_depara_form", this.workspace));
		this.showPopupEditar('Incluir Novo Depara');
	},	
	initGridDetalhe: function(){
		$(".deparaCotaGrid").flexigrid({
			preProcess : function(data) {
				if( typeof data.mensagens == "object") {
					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
				} else {
					return data;
				}
			},
			dataType : 'json',
			colModel : [ {
				display : 'Cota Fc',
				name : 'fc',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota Dinap',
				name : 'dinap',
				width : 120,
				sortable : true,
				align : 'left'
			}],
			sortname : "fc",
			sortorder : "asc",
			width : 570,
			height : 210
		});
	},
	detalheDialog:function(){
		$( "#dialog-depara", this.workspace).dialog({
			resizable: false,
			height:410,
			width:630,
			modal: true,
			buttons: [{
				id:"btnDialogDeparaFechar",
				text:"Fechar",
				click: function() {
					$( this ).dialog( "close" );
				}
			}],
			form: $("#dialog-depara", this.workspace).parents("form")			
		});
	},
	detalhe:function(id){		
		$(".deparaCotaGrid", this.workspace).flexOptions({
			"url" : this.path + 'detalhe.json',
			params : [{
				name : "id",
				value : id
			}],
			newp:1
		});
		$(".deparaCotaGrid", this.workspace).flexReload();
		this.detalheDialog();
	}
}, BaseController);
//@ sourceURL=depara.js