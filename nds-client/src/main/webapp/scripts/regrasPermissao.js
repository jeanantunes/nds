var regrasPermissaoController = $.extend(true, {
		path : "",
		init : function(contextPath) {
			this.path = contextPath + "/administracao/gruposAcesso";
			this.initRegrasGrid();
			this.mostrarRegra();
		},
		mostrarRegra : function() {
			// var serializedObj = $(obj).closest("form").serialize();
			var serializedObj = $("#pesquisar_regras_form", this.workspace).serialize();
			$(".regrasGrid", this.workspace).flexOptions({
				"url" : this.path + '/pesquisarRegras?' + serializedObj,
				method: 'GET',
				newp:1
			});
			$(".regrasGrid", this.workspace).flexReload();
		},
		initRegrasGrid : function() {
			$(".regrasGrid", this.workspace).flexigrid({
				preProcess: function(data) {
					if( typeof data.mensagens == "object") {
						exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
						$(".gridsRegra", this.workspace).hide();
						return data;
					} else {
						$(".gridsRegra", this.workspace).show();
						return data;
					}
				},
				dataType : 'json',
				colModel : [ {
					display : 'Nome',
					name : 'nome',
					width : 473,
					sortable : true,
					align : 'left'
				}, {
					display : 'Descrição',
					name : 'descricao',
					width : 400,
					sortable : true,
					align : 'left'
				}],
				sortname : "nome",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 900,
				height : 'auto'
			});
		}
}, BaseController);
