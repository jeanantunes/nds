var usuariosPermissaoController = $.extend(true, {
		path : "",
		init : function(contextPath) {
			this.path = contextPath + "/administracao/gruposAcesso";
			this.initUsuariosGrid();
			this.mostrarUsuario();
		},
		popup_novo_usuario : function() {
			$( "#dialog-novo-usuario" , this.workspace).dialog({
				resizable: false,
				height:620,
				width:750,
				modal: true,
				buttons: {
					"Confirmar": function() {
						var obj = $("#novo_usuario_form", this.workspace).serialize();
						
						var permissoes = "";
						$("#permissoesSelecionadasUsuario option", this.workspace).each(function() {
							if (permissoes!="") {
								permissoes += ",";
							}
							permissoes += $(this).val();
					    });

						var grupos = "";
						$("#gruposSelecionadosUsuario option", this.workspace).each(function() {
							if (grupos!="") {
								grupos += ",";
							}
							grupos += $(this).val();
					    });

						obj += "&usuarioDTO.permissoesSelecionadas=" + permissoes + "&usuarioDTO.gruposSelecionados=" + grupos;
						
						$.postJSON(usuariosPermissaoController.path + '/salvarUsuario', obj, function(data) {
							var tipoMensagem = data.tipoMensagem;
							var listaMensagens = data.listaMensagens;

							if(tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}

							$("#effect").show("highlight", {}, 1000, callback);
							$(".usuariosGrid", usuariosPermissaoController.workspace).flexReload();

						}, null, true);

						$( this ).dialog("close");
					},
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		popup_alterar_usuario : function() {
			$( "#dialog-novo-usuario", this.workspace ).dialog({
				resizable: false,
				height:620,
				width:750,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});			
		},
		popup_excluir_usuario : function() {
			$( "#dialog-excluir-usuario", this.workspace ).dialog({
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
		adicionaGruposSelecionados : function() {
			$('#permissoesGrupo', this.workspace).find(":selected").each(function() {
				$("#permissoesGrupoSelecionadas", this.workspace).append($("<option/>", {value: $(this).val(),
		 			   text: $(this).val() 
		 			  }));
			});
			$("#permissoesGrupo option:selected", this.workspace).remove();
		},
		removeGruposSelecionados : function() {
			$('#permissoesGrupoSelecionadas', this.workspace).find(":selected").each(function() {
				$("#permissoesGrupo", this.workspace).append($("<option/>", {value: $(this).val(),
		 			   text: $(this).val() 
		 			  }));
			});
			$("#permissoesGrupoSelecionadas option:selected", this.workspace).remove();
		},
		mostrarUsuario : function() {
			var serializedObj = $("#pesquisar_usuario_form", this.workspace).serialize();
			$(".usuariosGrid", this.workspace).flexOptions({
				"url" : this.path + '/pesquisarUsuarios?' + serializedObj,
				method: 'GET',
				newp:1
			});
			$('.usuariosGrid', this.workspace).flexReload();
		},
		initUsuariosGrid : function() {
			$(".usuariosGrid", this.workspace).flexigrid({
				preProcess: function(data) {
					if( typeof data.mensagens == "object") {
						exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
						$(".gridsGrupos", this.workspace).hide();
					} else {
						$.each(data.rows , function(index, value) {

							var linkEditarGrupo = '<a href="javascript:;" onclick="usuariosPermissaoController.editarUsuario(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Editar Usuário" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
				  		    '</a>';

							var linkExcluirGrupo = '<a href="javascript:;" onclick="usuariosPermissaoController.popup_excluir_usuario(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Excluir Usuário" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
				  		    '</a>';

							// lembreteSenha
							var lembreteSenha = "";
							if (value.cell.lembreteSenha != undefined) {
								lembreteSenha = value.cell.lembreteSenha;
							}
							value.cell[1] = lembreteSenha;

							// Nome
							var nome = "";
							if (value.cell.nome != undefined) {
								nome = value.cell.nome;
								if (value.cell.sobrenome != undefined) {
									nome += " " + value.cell.sobrenome;
								}
							}
							value.cell[2] = nome;

							// E-mail
							var email = "";
							if (value.cell.email != undefined) {
								email = value.cell.email;
							}
							value.cell[3] = email;
							
							// Telefone
							var telefone = "";
							if (value.cell.telefone != undefined) {
								if (value.cell.ddd != undefined) {
									telefone = "(" + value.cell.ddd + ") ";
								}
								telefone += value.cell.telefone;
							}
							value.cell[4] = telefone;

							// Ativa
							var ativa = "N";
							if (value.cell.ativa == "true") {
								ativa = "S";
							}
							value.cell[5] = ativa;
							
							// Ação
							value.cell[6] = linkEditarGrupo + linkExcluirGrupo;

						});
						$(".gridsUsuario", this.workspace).show();
					}
					return data;
				},
				dataType : 'json',
				colModel : [ {
					display : 'Username',
					name : 'login',
					width : 90,
					sortable : true,
					align : 'left'
				}, {
					display : 'Lembrete',
					name : 'lembreteSenha',
					width : 200,
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
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ativa',
					name : 'contaAtiva',
					width : 30,
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
		}
}, BaseController);
