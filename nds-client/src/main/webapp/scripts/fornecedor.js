var fornecedorController = $.extend(true,{
		init:function(){
			$('#fornecedorController-filtroConsultaFornecedorRazaoSocial,#fornecedorController-filtroConsultaFornecedorCnpj,#fornecedorController-filtroConsultaFornecedorNomeFantasia', fornecedorController.workspace).bind('keypress', function(e) {
				if(e.keyCode == 13) {
					fornecedorController.pesquisarFornecedores();
				}
			});
			
			$('#fornecedorController-filtroConsultaFornecedorRazaoSocial', fornecedorController.workspace).focus();
			$("#fornecedorController-fornecedoresGrid", fornecedorController.workspace).flexigrid({
				dataType : 'json',
				colModel : [  {
					display : 'C&oacute;digo',
					name : 'codigoInterface',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Raz&atilde;o Social',
					name : 'razaoSocial',
					width : 160,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNPJ',
					name : 'cnpj',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Contato',
					name : 'responsavel',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-Mail',
					name : 'email',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : 'A&ccedil;&atilde;o',
					name : 'acao',
					width : 50,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				singleSelect: true,
				rp : 15,
				showTableToggleBtn : true,
				width : 950,
				height : 'auto'
			});
			
			$("#fornecedorController-validadeContrato", fornecedorController.workspace).datepicker({
				showOn : "button",
				buttonImage: contextPath + "/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy'
			});
			
			$( "#fornecedorController-validadeContrato", fornecedorController.workspace ).mask("99/99/9999");
			
			$( "#fornecedorController-cnpj", fornecedorController.workspace ).mask("99.999.999/9999-99",{completed:function(){
				fornecedorController.obterPessoaJuridica();
			}});
					
			$( "#fornecedorController-validadeContrato", fornecedorController.workspace ).mask("99/99/9999");
			
			$( "#fornecedorController-tabFornecedores", fornecedorController.workspace ).tabs();
		
			jQuery(function($){
			   $.mask.definitions['#']='[\-\.0-9]';
			   $("#fornecedorController-inscricaoEstadual", fornecedorController.workspace).mask("?##################",{placeholder:" "});
			});
			
			
			
		},

		novoFornecedor:	function (isEdicao) {
				
				if (!isEdicao) {
					
					fornecedorController.limparCamposModal();
					
					$.postJSON(
						 contextPath +"/cadastro/fornecedor/novoCadastro",
						null,
						function(result) {
							$("#fornecedorController-inicioAtividade", fornecedorController.workspace).html(result.data);
							$("#fornecedorController-codigoInterface", fornecedorController.workspace).val(result.nextCodigo);
							
							fornecedorController.showPopupFornecedor();
						},
						null,
						true
					);
				
				} else {
					
					fornecedorController.showPopupFornecedor();
				}

				$( "#fornecedorController-tabFornecedores", fornecedorController.workspace ).tabs( "select" , 0 );
			},

			showPopupFornecedor:	function () {

				$( "#fornecedorController-dialogNovoFornecedor", fornecedorController.workspace ).dialog({
					resizable: false,
					height:570,
					width:840,
					modal: true,
					buttons: {
						"Confirmar": function() {
							fornecedorController.cadastrarFornecedor();
						},
						"Cancelar": function() {
							$( this ).dialog( "close" );
						}
					},
					form: $("#fornecedorController-dialogNovoFornecedor", this.workspace).parents("form")
				});
			},
			
			limparCamposModal:	function () {
				$("#fornecedorController-idFornecedor", fornecedorController.workspace).val("");
				$("#fornecedorController-codigoInterface", fornecedorController.workspace).val("");
				$("#fornecedorController-razaoSocial", fornecedorController.workspace).val("");
				$("#fornecedorController-nomeFantasia", fornecedorController.workspace).val("");
				$("#fornecedorController-cnpj", fornecedorController.workspace).val("");
				$("#fornecedorController-inscricaoEstadual", fornecedorController.workspace).val("");
				$("#fornecedorController-responsavel", fornecedorController.workspace).val("");
				$("#fornecedorController-email", fornecedorController.workspace).val("");
				$("#fornecedorController-tipoFornecedor", fornecedorController.workspace).val("");
				$("#fornecedorController-validadeContrato", fornecedorController.workspace).val("");
				$("#fornecedorController-emailNfe", fornecedorController.workspace).val("");
				$("#fornecedorController-possuiContrato", fornecedorController.workspace).uncheck();
				$( '#fornecedorController-validade', fornecedorController.workspace ).hide();
				FORNECEDOR.limparCamposTelefone();
				ENDERECO_FORNECEDOR.limparFormEndereco();
			},
			
			cadastrarFornecedor:function () {
				
				var formData = $("#fornecedorController-formNovoFornecedor", fornecedorController.workspace).serializeArray();
				
				$.postJSON(
					 contextPath +"/cadastro/fornecedor/cadastrarFornecedor",
					formData,
					function(result) {
						
						$( "#fornecedorController-dialogNovoFornecedor", fornecedorController.workspace ).dialog( "close" );
						
						exibirMensagem(
							result.tipoMensagem, 
							result.listaMensagens
						);
						fornecedorController.pesquisarFornecedores();
					},
					function(result) {
						exibirMensagemDialog(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens, ""
						);
					},
					true
				);
			},
			
			pesquisarFornecedores:function () {
				
				var formData = $("#fornecedorController-formularioPesquisaFornecedores", fornecedorController.workspace).serializeArray();

				$("#fornecedorController-fornecedoresGrid", fornecedorController.workspace).flexOptions({
					url :  contextPath +"/cadastro/fornecedor/pesquisarFornecedores",
					preProcess : fornecedorController.processarResultadoFornecedores,
					params : formData
				});
				
				$("#fornecedorController-fornecedoresGrid", fornecedorController.workspace).flexReload();

				$(".fornecedorController-grids", fornecedorController.workspace).show();
			},

			processarResultadoFornecedores:function (data) {
				
				if (data.mensagens) {

					exibirMensagem(
						data.mensagens.tipoMensagem,
						data.mensagens.listaMensagens
					);

					$(".fornecedorController-grids", fornecedorController.workspace).hide();

					return;
				}

				data = data.tableModel;

				var i;

				for (i = 0; i < data.rows.length; i++) {

					if (!data.rows[i].cell.codigoInterface) {
						
						data.rows[i].cell.codigoInterface = "";
					}
					
					if (!data.rows[i].cell.razaoSocial) {
						
						data.rows[i].cell.razaoSocial = "";
					}
								
					if (!data.rows[i].cell.cnpj) {
						
						data.rows[i].cell.cnpj = "";
					}else{
						data.rows[i].cell.cnpj = Cnpj(data.rows[i].cell.cnpj);
					}
					
					if (!data.rows[i].cell.responsavel) {
						
						data.rows[i].cell.responsavel = "";
					}
					
					if (!data.rows[i].cell.telefone) {
						
						data.rows[i].cell.telefone = "";
					}

					if (!data.rows[i].cell.email) {
						
						data.rows[i].cell.email = "";
					}
					
					if (!data.rows[i].cell.emailNfe) {
						
						data.rows[i].cell.emailNfe = "";
					}
					
					var isHabilitado = data.rows[i].cell.origem != 'INTERFACE';
					
					data.rows[i].cell.acao = fornecedorController.getActionFornecedor(data.rows[i].id, isHabilitado);
				}

				if (data.rows.length < 0) {

					$(".fornecedorController-grids", fornecedorController.workspace).hide();

					return;
				} 

				$(".fornecedorController-grids", fornecedorController.workspace).show();

				return data;
			},

			editarFornecedor:function (idFornecedor) {

				$.postJSON(
					 contextPath +"/cadastro/fornecedor/editarFornecedor",
					{'idFornecedor': idFornecedor},
					function(result) {
						
						$("#fornecedorController-inicioAtividade", fornecedorController.workspace).html(result.inicioAtividade);
						$("#fornecedorController-idFornecedor", fornecedorController.workspace).val(result.idFornecedor);
						$("#fornecedorController-codigoInterface", fornecedorController.workspace).val(result.codigoInterface);
						$("#fornecedorController-razaoSocial", fornecedorController.workspace).val(result.razaoSocial);
						$("#fornecedorController-nomeFantasia", fornecedorController.workspace).val(result.nomeFantasia);
						$("#fornecedorController-cnpj", fornecedorController.workspace).val(Cnpj(result.cnpj));
						$("#fornecedorController-inscricaoEstadual", fornecedorController.workspace).val(result.inscricaoEstadual);
						$("#fornecedorController-responsavel", fornecedorController.workspace).val(result.responsavel);
						$("#fornecedorController-email", fornecedorController.workspace).val(result.email);
						$("#fornecedorController-emailNfe", fornecedorController.workspace).val(result.emailNfe);
						$("#fornecedorController-tipoFornecedor", fornecedorController.workspace).val(result.tipoFornecedor);
						
						if (result.possuiContrato) {

							$("#fornecedorController-possuiContrato", fornecedorController.workspace).check();

							$("#fornecedorController-validadeContrato", fornecedorController.workspace).val(result.validadeContrato);	
						
							$( '.fornecedorController-validade', fornecedorController.workspace ).show();
							
						} else {
							
							$("#fornecedorController-possuiContrato", fornecedorController.workspace).uncheck();
							
							$("#fornecedorController-validadeContrato", fornecedorController.workspace).val("");
							
							$( '.fornecedorController-validade', fornecedorController.workspace ).hide();
						}

						fornecedorController.novoFornecedor(true);
						
						ENDERECO_FORNECEDOR.popularGridEnderecos();
						
						FORNECEDOR.carregarTelefones();
					},
					function(result) {
						exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
						);
					}
				);
			},
			
			getActionFornecedor:function (idFornecedor, isHabilitado) {

				if (isHabilitado) {
				
					return '<a href="javascript:;" onclick="fornecedorController.editarFornecedor('
							+ idFornecedor
							+ ')" '
							+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar fornecedor">'
							+ '<img src="'+contextPath +'/images/ico_editar.gif" border="0px"/>'
							+ '</a>'
							+ '<a href="javascript:;" onclick="fornecedorController.confirmarExclusaoFornecedor('
							+ idFornecedor
							+ ')" '
							+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir fornecedor">'
							+ '<img src="'+contextPath + '/images/ico_excluir.gif" border="0px"/>'
							+ '</a>';
				
				} else {
					
					return '<img src="'+contextPath +'/images/ico_editar.gif" border="0px"'
							+ ' style="opacity:0.4;padding-right:10px"/>'
						 	+ '<img src="'+contextPath + '/images/ico_excluir.gif" border="0px"'
						 	+ ' style="opacity:0.4"/>';

				}
			},

			confirmarExclusaoFornecedor:function (idFornecedor) {
			
				$( "#fornecedorController-dialog-excluir", fornecedorController.workspace ).dialog({
					resizable: false,
					height:170,
					width:380,
					modal: true,
					buttons: {
						"Confirmar": function() {
							fornecedorController.excluirFornecedor(idFornecedor);
							$( this ).dialog( "close" );
						},
						"Cancelar": function() {
							$( this ).dialog( "close" );
						}
					},
					form: $("#fornecedorController-dialog-excluir", this.workspace).parents("form")
				});
			},
			
			excluirFornecedor:function (idFornecedor) {
			
				$.postJSON(
					 contextPath +"/cadastro/fornecedor/excluirFornecedor",
					{'idFornecedor': idFornecedor},
					function(result) {
						exibirMensagem(
							result.tipoMensagem, 
							result.listaMensagens
						);
						
						fornecedorController.pesquisarFornecedores();
					},
					function(result) {
						exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
						);
					}
				);
			},
			
			obterPessoaJuridica:function () {				
				$.postJSON(
						 contextPath +"/cadastro/fornecedor/obterPessoaJuridica",
						{'cnpj': $("#fornecedorController-cnpj", fornecedorController.workspace).val()},
						function(result) {
							
							if (result.razaoSocial) 
								$("#fornecedorController-razaoSocial", fornecedorController.workspace).val(result.razaoSocial);
							if (result.nomeFantasia)
								$("#fornecedorController-nomeFantasia", fornecedorController.workspace).val(result.nomeFantasia);
							if (result.inscricaoEstadual)
								$("#fornecedorController-inscricaoEstadual", fornecedorController.workspace).val(result.inscricaoEstadual);
							if (result.email)
								$("#fornecedorController-email", fornecedorController.workspace).val(result.email);
						},
						null,
						true
					);
			},
			

			mostraValidade:function () {
				
				if ($(".fornecedorController-validade", fornecedorController.workspace).css("display") == "none") {

					$( '.fornecedorController-validade', fornecedorController.workspace ).fadeIn( "slow" );
				
				} else {

					$( '.fornecedorController-validade', fornecedorController.workspace ).val("");

					$( '.fornecedorController-validade', fornecedorController.workspace ).fadeOut( "slow" );
				}

			}		
}, BaseController);