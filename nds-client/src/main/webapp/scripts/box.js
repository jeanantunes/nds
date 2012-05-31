var boxController = {
	tipoBoxEnun : {
		SUPLEMENTAR : 'Suplementar',
		RECOLHIMENTO : 'Recolhimento',
		LANCAMENTO : 'Lan&ccedil;amento',
		NUMEROS_ATRASADOS : 'NA'
	},
	box : {},
	path : contextPath + '/cadastro/box/',
	intGridBox : function() {
		$(".boxGrid").flexigrid({
			preProcess : function(data) {
				if( typeof data.mensagens == "object") {

					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);

				} else {
					$.each(data.rows, function(index, value) {
						var idBox = value.cell.id;
						var acao = '<a href="javascript:;" onclick="boxController.editar(' + idBox + ');"><img src="' + contextPath + '/images/ico_editar.gif" border="0" hspace="5" />';
						acao += '</a> <a href="javascript:;" onclick="boxController.excluir(' + idBox + ');""><img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" /></a>';

						value.cell.acao = acao;
						if(value.cell.tipoBox == 'LANCAMENTO' && value.cell.postoAvancado) {
							value.cell.tipoBox = 'Posto Avan&ccedil;ado';
						} else {
							value.cell.tipoBox = boxController.tipoBoxEnun[value.cell.tipoBox];
						}
						
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
			height : 255
		});
		$(".boxGrid").flexOptions({
			"url" : this.path + 'busca.json',
			params : [{
				name : "codigoBox",
				value : $("#pesquisaCodigoBox").val()
			}, {
				name : "tipoBox",
				value : $("#pesquisaTipoBox").val()
			}, {
				name : "postoAvancado",
				value : false
			}]
		});

	},
	buscar : function(codigoBox, tipoBox) {
		$(".boxGrid").flexOptions({
			"url" : this.path + 'busca.json',
			params : [{
				name : "codigoBox",
				value : codigoBox
			}, {
				name : "tipoBox",
				value : tipoBox
			}, {
				name : "postoAvancado",
				value : false
			}],
			newp:1
		});
		$(".boxGrid").flexReload();
	},
	bindButtons : function() {
		$("#btnPesquisar").click(function() {
			boxController.buscar($("#pesquisaCodigoBox").val(), $("#pesquisaTipoBox").val());
			$(".grids").show();
		});
		$("#btnNovo").click(function() {
			boxController.novo();
		});
	},
	bindInputEvent : function() {
		$('#pesquisaCodigoBox').bind('keypress', function(e) {
			if(e.keyCode == 13) {
				boxController.buscar($("#pesquisaCodigoBox").val(), $("#pesquisaTipoBox").val());
				$(".grids").show();
			}
		});
	},
	init : function() {
		this.intGridBox();
		this.initGridDetalhe();
		this.bindButtons();
		this.bindInputEvent();
	},
	showPopupEditar : function(title) {
		$("#dialog-novo").dialog({
			resizable : false,
			height : 230,
			width : 400,
			modal : true,
			title : title,
			buttons : {
				"Confirmar" : function() {
					boxController.salvar(this);

				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			beforeClose : clearMessageDialogTimeout
		});
	},
	editar : function(id) {
		$.postJSON(this.path + 'buscaPorId.json', {
			'id' : id
		}, function(data) {
			if( typeof data.mensagens == "object") {
				exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			} else {

				boxController.bindData(data);
				boxController.showPopupEditar('Editar Box');
			}
		});
	},
	bindData : function(data) {
		this.box = data.box;
		$("#boxCodigo,#boxTipoBox,#boxPostoAvancado").attr('disabled',data.associado);
		
		$("#boxCodigo").val(this.box.codigo);
		$("#boxNome").val(this.box.nome);
		$("#boxTipoBox").val(this.box.tipoBox);
		$("#boxPostoAvancado").attr("checked", this.box.postoAvancado);
	},
	getData : function() {		
		this.box.codigo = $("#boxCodigo").val();
		this.box.nome = $("#boxNome").val();
		this.box.tipoBox = $("#boxTipoBox").val();
		this.box.postoAvancado = ($("#boxPostoAvancado").attr("checked") == "checked");
	},
	salvar : function(dialog) {
		this.getData();
		var obj = {};
		for(var propriedade in this.box) {
			obj['box.' + propriedade] = this.box[propriedade];

		}

		$.postJSON(this.path + 'salvar.json', obj, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if(tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			}

			$(dialog).dialog("close");
			$("#effect").show("highlight", {}, 1000, callback);
			$(".grids").show();
			$(".boxGrid").flexReload();

		}, null, true);
	},
	excluir : function(id) {
		$("#dialog-excluir").dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
					boxController.remove(id);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	},
	remove : function(id) {
		$.postJSON(this.path + 'remove.json', {
			'id' : id
		}, function(data) {
			if( typeof data.mensagens == "object") {
				exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			}
			$(".boxGrid").flexReload();
			
		});
	},
	novo : function() {
		var data = {
			box : {},
			associado:false
		};

		this.bindData(data);
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
		$( "#dialog-box" ).dialog({
			resizable: false,
			height:410,
			width:630,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	detalhe:function(id){		
		$(".boxCotaGrid").flexOptions({
			"url" : this.path + 'detalhe.json',
			params : [{
				name : "id",
				value : id
			}],
			newp:1
		});
		$(".boxCotaGrid").flexReload();
		this.detalheDialog();
	}
};

