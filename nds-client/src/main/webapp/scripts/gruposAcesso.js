var gruposAcessoController = $.extend(true, {
		path : "",
		init : function(contextPath) {
			this.initRegrasGrid();
			this.initUsuariosGrid();
			$( "#tabs-grupos" ).tabs();
			this.path = contextPath + "/administracao/gruposAcesso";
		},
		popup_usuario : function() {
			$( "#dialog-novo-usuario" ).dialog({
				resizable: false,
				height:620,
				width:750,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
						$(".grids").show();
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		popup_alterar_usuario : function() {
			$( "#dialog-novo-usuario" ).dialog({
				resizable: false,
				height:620,
				width:750,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						$( "#abaPdv" ).show( );
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});			
		},
		popup_excluir_usuario : function() {
			$( "#dialog-excluir-usuario" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		popup_nova_regra : function() {
			$( "#dialog-novo_regra" ).dialog({
				resizable: false,
				height:200,
				width:470,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
						$(".grids").show();
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		popup_alterar_regra : function() {
			$( "#dialog-novo_regra" ).dialog({
				resizable: false,
				height:200,
				width:470,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						$( "#abaPdv" ).show( );
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});	
		},
		popup_excluir_regra : function() {
			$( "#dialog-excluir_regra" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		mostrarUsuario : function() {
			$('.gridsUsuario').show();
		},
		mostrarRegra : function() {
			// var serializedObj = $(obj).closest("form").serialize();
			var serializedObj = $("#pesquisar_regras_form", this.workspace).serialize();
			$(".regrasGrid", this.workspace).flexOptions({
				"url" : this.path + '/pesquisarRegras?' + serializedObj,
				newp:1
			});
			$(".regrasGrid", this.workspace).flexReload();
		},
		initUsuariosGrid : function() {
			$(".usuariosGrid", this.workspace).flexigrid({
				dataType : 'json',
				colModel : [ {
					display : 'Username',
					name : 'user',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Lembrete',
					name : 'lembrete',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-mail',
					name : 'email',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ativa',
					name : 'ativa',
					width : 30,
					sortable : true,
					align : 'left'
				}, {
					display : 'Conta Expira',
					name : 'expiraEm',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Bloqueada',
					name : 'bloqueada',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Senha Expira',
					name : 'senhaExpira',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				sortname : "nome",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 900,
				height : 255
			});
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
					width : 370,
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
				height : 255
			});
		}
}, BaseController);
