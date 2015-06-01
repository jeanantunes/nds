var boxController = $.extend(true, {
	tipoBoxEnun : {
		ENCALHE : 'Encalhe',
		LANCAMENTO : 'Lan&ccedil;amento',
		ESPECIAL : 'Especial'
	},
	box : {},
	path : contextPath + '/cadastro/box/',
	intGridBox : function() {
		$(".boxGrid", this.workspace).flexigrid({
			preProcess : function(data) {
				if(typeof data.mensagens == "object") {

					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);

				} else {
					$.each(data.rows, function(index, value) {
						var idBox = value.cell.id;
						var acao = '<a href="javascript:;" onclick="boxController.editar(' + idBox + ');"><img src="' + contextPath + '/images/ico_editar.gif" border="0" style="margin-right:10px;" />';
						acao += '</a> <a href="javascript:;" onclick="boxController.excluir(' + idBox + ');""><img src="' + contextPath + '/images/ico_excluir.gif" border="0" /></a>';

						value.cell.acao = acao;						
						value.cell.tipoBox = boxController.tipoBoxEnun[value.cell.tipoBox];				
						
						value.cell.codigo = '<a href="javascript:;" onclick="boxController.detalhe(' + idBox + ');">'+value.cell.codigo+'</a>';
					});

					return data;
				}

			},
			dataType : 'json',
			colModel : [{
				display : 'Box',
				name : 'codigo',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 380,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Box',
				name : 'tipoBox',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
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
			width : 960,
			height : 'auto'
		});
		$(".boxGrid", this.workspace).flexOptions({
			"url" : this.path + 'busca.json',
			params : [{
				name : "codigoBox",
				value : $("#pesquisaCodigoBox", this.workspace).val()
			}, {
				name : "tipoBox",
				value : $("#pesquisaTipoBox", this.workspace).val()
			}]
		});

	},
	buscar : function(obj) { 
		var serializedObj = $("#pesquisar_box_form", this.workspace).serialize();
		
		var codigo = $("#pesquisaCodigoBox").val();
		var tipoBox = $("#pesquisaTipoBox").val();
		
		$(".boxGrid", this.workspace).flexOptions({
			"url" : this.path + 'busca.json', 
			params: [{name:'box.codigo', value: codigo },
			         {name:'box.tipoBox', value: tipoBox }],			
			newp:1
		});
		$(".boxGrid").flexReload();
		$("#fileExport").show();
	},
	bindButtons : function() {
		$("#btnPesquisar", this.workspace).click(function() {
			boxController.buscar(this);
			$(".grids").show();
			$("#fileExport").show();
		});
		$("#btnNovo", this.workspace).click(function() {
			boxController.novo();
		});
	},
	bindInputEvent : function() {
		$('#pesquisaCodigoBox, #boxCodigo').numeric();
		$('#pesquisaCodigoBox').bind('keypress', function(e) {
			if(e.keyCode == 13) {
				boxController.buscar();
				$(".grids", this.workspace).show();
			}
		});
	},
	init : function() {
		this.intGridBox();
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
					boxController.salvar(this);

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
		$.postJSON(this.path + 'buscaPorId.json', {
			'id' : id
		}, function(data) {
			if( typeof data.mensagens == "object") {
				exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			} else {
				$("#boxId").val(data.box.id);
				$("#boxCodigo").val(data.box.codigo);
				$("#boxNome").val(data.box.nome);
				$("#boxTipoBox").val(data.box.tipoBox);

				if (data.emUso) {
					$("#boxCodigo").attr("readOnly", "readOnly");
					$("#boxTipoBox").attr('disabled', true);
				} else {
					$("#boxCodigo").removeAttr("readOnly");
					$("#boxTipoBox").attr('disabled', false);
				}
				boxController.showPopupEditar('Editar Box');
			}
		});
	},
	getData : function() {		
		this.box.codigo = parseInt($("#boxCodigo").val());
		this.box.nome = $("#boxNome").val();
		this.box.tipoBox = $("#boxTipoBox").val();
	},	
	salvar : function(dialog) {

		$("#boxTipoBox").attr('disabled', false);
		var obj = $("#novo_box_form", this.workspace).serialize();
		$("#boxTipoBox").attr('disabled', true);
		
		$.postJSON(this.path + 'salvar.json', obj, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if(tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			}

			$(dialog).dialog("close");
			$("#effect").show("highlight", {}, 1000, callback);
			$(".grids", this.workspace).show();
			boxController.buscar(this);

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
					boxController.remove(id);
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
			$(".boxGrid", this.workspace).flexReload();
			
		});
	},
	novo : function() {
		
		if(!verificarPermissaoAcesso(this.workspace))
			return;
		
		var data = {
			box : {}
		};
		
		this.clearForm($("#novo_box_form", this.workspace));

		$("#boxCodigo").removeAttr("readOnly");
		$("#boxTipoBox").attr('disabled', false);		
		
		this.bindData(data, $("#novo_box_form", this.workspace));
		this.showPopupEditar('Incluir Novo Box');
	},	
	initGridDetalhe: function(){
		$(".boxCotaGrid").flexigrid({
			preProcess : function(data) {
				if( typeof data.mensagens == "object") {
					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
				} else {
					return data;
				}
			},
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'roteiro',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rota',
				width : 120,
				sortable : true,
				align : 'left'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			width : 570,
			height : 210
		});
	},
	detalheDialog:function(){
		$( "#dialog-box", this.workspace).dialog({
			resizable: false,
			height:410,
			width:630,
			modal: true,
			buttons: [{
				id:"btnDialogBoxFechar",
				text:"Fechar",
				click: function() {
					$( this ).dialog( "close" );
				}
			}],
			form: $("#dialog-box", this.workspace).parents("form")			
		});
	},
	detalhe:function(id){		
		$(".boxCotaGrid", this.workspace).flexOptions({
			"url" : this.path + 'detalhe.json',
			params : [{
				name : "id",
				value : id
			}],
			newp:1
		});
		$(".boxCotaGrid", this.workspace).flexReload();
		this.detalheDialog();
	}
}, BaseController);
//@ sourceURL=box.js