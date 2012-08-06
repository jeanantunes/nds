
var boxController = $.extend(true, {
	tipoBoxEnun : {
		ENCALHE : 'Encalhe',
		LANCAMENTO : 'Lan&ccedil;amento',
		POSTO_AVANCADO : 'Posto Avan&ccedilado'
	},
	box : {},
	path : contextPath + '/cadastro/box/',
	intGridBox : function() {
		$(".boxGrid", this.workspace).flexigrid({
			preProcess : function(data) {
				if( typeof data.mensagens == "object") {

					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);

				} else {
					$.each(data.rows, function(index, value) {
						var idBox = value.cell.id;
						var acao = '<a href="javascript:;" onclick="boxController.editar(' + idBox + ');"><img src="' + contextPath + '/images/ico_editar.gif" border="0" hspace="5" />';
						acao += '</a> <a href="javascript:;" onclick="boxController.excluir(' + idBox + ');""><img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" /></a>';

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
				width : 350,
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
				align : 'left'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 280
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
	//buscar : function(codigoBox, tipoBox) {
	buscar : function(obj) { 
		
		// var serializedObj = $(obj).closest("form").serialize();
		var serializedObj = $("#pesquisar_box_form", this.workspace).serialize();
		
		var codigo = $("#pesquisaCodigoBox").val();
		var tipoBox = $("#pesquisaTipoBox").val();
		
		/*var codigoBox = ;
		var tipoBox = ;*/
		
		$(".boxGrid", this.workspace).flexOptions({
			"url" : this.path + 'busca.json', //'busca.json?' + serializedObj,
//			method: 'GET',
			params: [{name:'box.codigo', value: codigo },
			         {name:'box.tipoBox', value: tipoBox }],			
			newp:1
		});
		$(".boxGrid").flexReload();
	},
	bindButtons : function() {
		$("#btnPesquisar", this.workspace).click(function() {
			//boxController.buscar(parseInt($("#pesquisaCodigoBox").val()), $("#pesquisaTipoBox").val());
			boxController.buscar(this);
			$(".grids").show();
		});
		$("#btnNovo", this.workspace).click(function() {
			boxController.novo();
		});
	},
	bindInputEvent : function() {
		//$('#pesquisaCodigoBox, #boxCodigo').mask("9999");
		$('#pesquisaCodigoBox, #boxCodigo').numeric();
		$('#pesquisaCodigoBox').bind('keypress', function(e) {
			if(e.keyCode == 13) {
				//boxController.buscar(parseInt($("#pesquisaCodigoBox").val()), $("#pesquisaTipoBox").val());
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
		//$("#dialog-novo").dialog({
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
			//form: $("#dialog-novo").parents("form")
		});
		$("#dialog-novo", this.workspace).dialog("close");
		//$("#dialog-novo").dialog("close");
	},
	showPopupEditar : function(title) {
		//$("#dialog-novo")
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
				boxController.bindData(data, $("#novo_box_form", this.workspace));
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
		//this.getData();

		//var obj = $(dialog).dialog("option", "form").serialize();

		var obj = $("#novo_box_form", this.workspace).serialize();
		
		$.postJSON(this.path + 'salvar.json', obj, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if(tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			}

			$(dialog).dialog("close");
			$("#effect").show("highlight", {}, 1000, callback);
			$(".grids", this.workspace).show();
			$(".boxGrid", this.workspace).flexReload();

		}, null, true);
	},
	excluir : function(id) {
		//$("#dialog-excluir").dialog({
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
			}]
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
		var data = {
			box : {}
		};
		
		this.clearForm($("#novo_box_form", this.workspace));

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
				sortable : false,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 200,
				sortable : false,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rota',
				width : 120,
				sortable : false,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'roteiro',
				width : 120,
				sortable : false,
				align : 'left'
			}],
			width : 570,
			height : 255
		});
	},
	detalheDialog:function(){
		//$( "#dialog-box" ).dialog({
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
			}]
		});
	},
	detalhe:function(id){		
		//$(".boxCotaGrid").flexOptions({
		$(".boxCotaGrid", this.workspace).flexOptions({
			"url" : this.path + 'detalhe.json',
			params : [{
				name : "id",
				value : id
			}],
			newp:1
		});
		//$(".boxCotaGrid").flexReload();
		$(".boxCotaGrid", this.workspace).flexReload();
		this.detalheDialog();
	}
}, BaseController);
