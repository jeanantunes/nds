var entregadorController = $.extend(true, {
	
	preencherComboUF : function (ufSelecionado) {

		var idComboUF = "#ufOrgaoEmissor";

		$.postJSON(
			contextPath + '/cadastro/endereco/obterDadosComboUF',
			null,
			function(result) {

				$(idComboUF, Endereco.workspace).html("");
				
				$(idComboUF, Endereco.workspace).append('<option selected="selected"></option>');

				$.each(result, function(index, value) {

					var option = "<option value='" + value + "'>" + value + "</option>";

					$(idComboUF, Endereco.workspace).append(option);	
				});
				
				if (ufSelecionado) {
					
					$(idComboUF, Endereco.workspace).val(ufSelecionado);
				}
			},
			null,
			true
		);
	},
	
	obterCota: function(numeroCota, isPF) {
		
		var sufixo = "PJ";
		
		if(isPF) {
			
			sufixo = "PF";
		}

		var numeroCotaProcuracao = "#numeroCotaProcuracao" + sufixo;
		var nomeJornaleiroProcuracao = "#nomeJornaleiroProcuracao" + sufixo;
		var boxProcuracao = "#boxProcuracao" + sufixo;
		var nacionalidadeProcuracao = "#nacionalidadeProcuracao" + sufixo;
		var estadoCivilProcuracao = "#estadoCivilProcuracao" + sufixo;
		var enderecoPDVPrincipalProcuracao = "#enderecoPDVPrincipalProcuracao" + sufixo;
		var cepProcuracao = "#cepProcuracao" + sufixo;
		var bairroProcuracao = "#bairroProcuracao" + sufixo;
		var cidadeProcuracao = "#cidadeProcuracao" + sufixo;
		var numeroPermissaoProcuracao = "#numeroPermissaoProcuracao" + sufixo;
		var rgProcuracao = "#rgProcuracao" + sufixo;
		var cpfProcuracao = "#cpfProcuracao" + sufixo;
		
		$.postJSON(
			contextPath + '/cadastro/entregador/obterCotaPorNumero',
			{'numeroCota': numeroCota},
			function(result) {
				$(numeroCotaProcuracao, this.workspace).val(result.numeroCota);
				$(nomeJornaleiroProcuracao, this.workspace).val(result.nomeJornaleiro);
				$(boxProcuracao, this.workspace).val(result.box);
				$(nacionalidadeProcuracao, this.workspace).val(result.nacionalidade);
				$(estadoCivilProcuracao, this.workspace).val(result.estadoCivil);
				$(enderecoPDVPrincipalProcuracao, this.workspace).val(result.enderecoPDVPrincipal);
				$(cepProcuracao, this.workspace).val(result.cep);
				$(bairroProcuracao, this.workspace).val(result.bairro);
				$(cidadeProcuracao, this.workspace).val(result.cidade);
				$(numeroPermissaoProcuracao, this.workspace).val(result.numeroPermissao);
				$(rgProcuracao, this.workspace).val(result.rg).mask("99.999.999-9");
				$(cpfProcuracao, this.workspace).val(result.cpf).mask("999.999.999-99");
			}, 
			function(result) {
				
				$(numeroCotaProcuracao, this.workspace).val("");
				$(nomeJornaleiroProcuracao, this.workspace).val("");
				$(boxProcuracao, this.workspace).val("");
				$(nacionalidadeProcuracao, this.workspace).val("");
				$(estadoCivilProcuracao, this.workspace).val("");
				$(enderecoPDVPrincipalProcuracao, this.workspace).val("");
				$(cepProcuracao, this.workspace).val("");
				$(bairroProcuracao, this.workspace).val("");
				$(cidadeProcuracao, this.workspace).val("");
				$(numeroPermissaoProcuracao, this.workspace).val("");
				$(rgProcuracao, this.workspace).val("");
				$(cpfProcuracao, this.workspace).val("");

				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens, ""
				);
			},
			true
		);
	},

	novoEntregador: function(isCadastroPF) {
		
		$("#idEntregadorPF", this.workspace).val("");
		$("#idEntregadorPJ", this.workspace).val("");
		
		var idCampoInicioAtividade = "#inicioAtividade";
		
		var idCampoCodigoEntregador = "#codigoEntregador";

		if (isCadastroPF) {
			
			idCampoInicioAtividade += "PF";
			
			idCampoCodigoEntregador += "PF";
			
			$("#formDadosEntregadorPF", this.workspace)[0].reset();
			$("#idEntregadorPF", this.workspace).val("");
			$("#naoComissionadoPF", this.workspace).click();
			$("#naoProcuracaoPF", this.workspace).click();
			$("#percentualComissaoPF", this.workspace).mask("999,99");

		} else {

			idCampoInicioAtividade += "PJ";

			idCampoCodigoEntregador += "PJ";
			
			$("#formDadosEntregadorPJ", this.workspace)[0].reset();
			$("#idEntregadorPJ", this.workspace).val("");
			$("#naoComissionadoPJ", this.workspace).click();
			$("#naoProcuracaoPJ", this.workspace).click();
			$("#percentualComissaoPJ", this.workspace).mask("999,99");
			
		}
		
		$.postJSON(
			contextPath + '/cadastro/entregador/novoCadastro',
			null,
			function(result) {

				$("#linkDadosCadastrais", this.workspace).click();
				
				$(idCampoInicioAtividade, this.workspace).html(result.data);

				$(idCampoCodigoEntregador, this.workspace).val(result.nextCodigo);

				entregadorController.popup_novoEntregador(isCadastroPF);		
			}
		);
	},

	popup_novoEntregador: function(isCadastroPF) {

		if (isCadastroPF) {

			$("#dadosCadastraisPF", this.workspace).show();
			$("#dadosCadastraisPJ", this.workspace).hide();
			
			entregadorController.preencherComboUF();

		} else {

			$("#dadosCadastraisPJ", this.workspace).show();
			$("#dadosCadastraisPF", this.workspace).hide();
		}

		$( "#dialog-novoEntregador", this.workspace ).dialog({
			resizable: false,
			height:570,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {

					entregadorController.cadastrarEntregador(isCadastroPF);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			
			form: $("#div-wrapper-dialog-novoEntregador", this.workspace)
			
			
		});
	},
	
	cadastrarEntregador: function(isCadastroPF, imprimir) {

		var data = new Array();

		var url;

		if (isCadastroPF) {

			data.push({name: 'cpfEntregador', value: $("#cpf", this.workspace).val()});
			data.push({name: 'idEntregador', value: $("#idEntregadorPF", this.workspace).val()});
			data.push({name: 'codigoEntregador', value: $("#codigoEntregadorPF", this.workspace).val()});
			data.push({name: 'isComissionado', value: $("#comissionadoPF", this.workspace).is(":checked")});
			data.push({name: 'percentualComissao', value: $("#percentualComissaoPF", this.workspace).val()});
			data.push({name: 'dataNascimento', value: $("#dataNascimento", this.workspace).val()});
			
			data.push({name: 'pessoaFisica.cpf', value: $("#cpf", this.workspace).val()});
			data.push({name: 'pessoaFisica.nome', value: $("#nomeEntregador", this.workspace).val()});
			data.push({name: 'pessoaFisica.apelido', value: $("#apelido", this.workspace).val()});
			data.push({name: 'pessoaFisica.rg', value: $("#rg", this.workspace).val()});
			data.push({name: 'pessoaFisica.orgaoEmissor', value: $("#dados-cadastral-orgaoEmissor", this.workspace).val()});
			data.push({name: 'pessoaFisica.ufOrgaoEmissor', value: $("#ufOrgaoEmissor", this.workspace).val()});
			data.push({name: 'pessoaFisica.estadoCivil', value: $("#estadoCivil", this.workspace).val()});
			data.push({name: 'pessoaFisica.sexo', value: $("#sexo", this.workspace).val()});
			data.push({name: 'pessoaFisica.nacionalidade', value: $("#nacionalidade", this.workspace).val()});
			data.push({name: 'pessoaFisica.natural', value: $("#natural", this.workspace).val()});
			data.push({name: 'pessoaFisica.email', value: $("#emailPF", this.workspace).val()});

			url = contextPath + '/cadastro/entregador/cadastrarEntregadorPessoaFisica';

		} else {

			data.push({name: 'cnpjEntregador', value: $("#cnpj", this.workspace).val()});
			data.push({name: 'idEntregador', value: $("#idEntregadorPJ", this.workspace).val()});
			data.push({name: 'codigoEntregador', value: $("#codigoEntregadorPJ", this.workspace).val()});
			data.push({name: 'isComissionado', value: $("#comissionadoPJ", this.workspace).is(":checked")});
			data.push({name: 'percentualComissao', value: $("#percentualComissaoPJ", this.workspace).val()});

			data.push({name: 'pessoaJuridica.razaoSocial', value: $("#razaoSocial", this.workspace).val()});
			data.push({name: 'pessoaJuridica.nomeFantasia', value: $("#nomeFantasia", this.workspace).val()});
			data.push({name: 'pessoaJuridica.cnpj', value: $("#cnpj", this.workspace).val()});
			data.push({name: 'pessoaJuridica.inscricaoEstadual', value: $("#inscricaoEstadual", this.workspace).val()});
			data.push({name: 'pessoaJuridica.email', value: $("#emailPJ", this.workspace).val()});

			url = contextPath + '/cadastro/entregador/cadastrarEntregadorPessoaJuridica';
		}

		$.postJSON(
			url,
			data,
			function(result) {
				
				if (imprimir) {

					entregadorController.realizarImpressao(isCadastroPF);

				} else {

					$("#dialog-novoEntregador", this.workspace).dialog( "close" );

					entregadorController.pesquisarEntregadores();
					
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
				}
			});
	},
	
	editarEntregador: function(idEntregador) {
		
		$("#_edicao", this.workspace).val(idEntregador);
		
		$.postJSON(
			contextPath + '/cadastro/entregador/editarEntregador',
			{'idEntregador' : idEntregador},
			function(result) {

				if (result.pessoaFisica) {

					$("#formDadosEntregadorPF", this.workspace)[0].reset();

					entregadorController.popup_novoEntregador(true);

					$("#idEntregadorPF", this.workspace).val(result.entregador.id);
					$("#codigoEntregadorPF", this.workspace).val(result.entregador.codigo);
					$("#inicioAtividadePF", this.workspace).html(result.inicioAtividadeFormatada);
					$("#nomeEntregador", this.workspace).val(result.pessoaFisica.nome);
					$("#apelido", this.workspace).val(result.pessoaFisica.apelido);
					$("#cpf", this.workspace).val(result.pessoaFisica.cpf).mask("999.999.999-99");
					$("#rg", this.workspace).val(result.pessoaFisica.rg);
					$("#dataNascimento", this.workspace).val(result.dataNascimentoEntregadorFormatada).mask("99/99/9999");
					$("#dados-cadastral-orgaoEmissor", this.workspace).val(result.pessoaFisica.orgaoEmissor);
					entregadorController.preencherComboUF(result.pessoaFisica.ufOrgaoEmissor);
					//$("#ufOrgaoEmissor", this.workspace).val(result.pessoaFisica.ufOrgaoEmissor);
					$("#estadoCivil", this.workspace).val(result.pessoaFisica.estadoCivil);
					$("#sexo", this.workspace).val(result.pessoaFisica.sexo);
					$("#nacionalidade", this.workspace).val(result.pessoaFisica.nacionalidade);
					$("#natural", this.workspace).val(result.pessoaFisica.natural);
					$("#emailPF", this.workspace).val(result.pessoaFisica.email);
					$("#percentualComissaoPF", this.workspace).mask("999,99");
					$("#percentualComissaoPF", this.workspace).val(result.entregador.percentualComissao);

					if (result.entregador.comissionado) {
						$("#comissionadoPF", this.workspace).click();
						$("#naoComissionadoPF", this.workspace).uncheck();	
					} else {
						$("#naoComissionadoPF", this.workspace).click();
						$("#comissionadoPF", this.workspace).uncheck();
					}

					if (result.entregador.procuracao) {

						$("#estadoCivilProcuradorProcuracaoPF", this.workspace).val(result.procuracaoEntregador.estadoCivil);
						$("#nacionalidadeProcuradorProcuracaoPF", this.workspace).val(result.procuracaoEntregador.nacionalidade);
						$("#numeroPermissaoPF", this.workspace).val(result.procuracaoEntregador.numeroPermissao);
						$("#nomeProcuradorProcuracaoPF", this.workspace).val(result.procuracaoEntregador.procurador);
						$("#profissaoProcuradorProcuracaoPF", this.workspace).val(result.procuracaoEntregador.profissao);
						$("#rgProcuradorProcuracaoPF", this.workspace).val(result.procuracaoEntregador.rg).mask("99.999.999-9");
						$("#enderecoProcuradorProcuracaoPF", this.workspace).val(result.procuracaoEntregador.endereco);
						
						entregadorController.obterCota(result.procuracaoEntregador.cota.numeroCota, true); 

						$("#procuracaoPF", this.workspace).click();
						$("#naoProcuracaoPF", this.workspace).uncheck();	
					} else {
						$("#naoProcuracaoPF", this.workspace).click();
						$("#procuracaoPF", this.workspace).uncheck();
					}

				} else {

					$("#formDadosEntregadorPJ", this.workspace)[0].reset();

					entregadorController.popup_novoEntregador(false);

					$("#idEntregadorPJ", this.workspace).val(result.entregador.id);
					$("#codigoEntregadorPJ", this.workspace).val(result.entregador.codigo);
					$("#inicioAtividadePJ", this.workspace).html(result.inicioAtividadeFormatada);
					$("#razaoSocial", this.workspace).val(result.pessoaJuridica.razaoSocial);
					$("#nomeFantasia", this.workspace).val(result.pessoaJuridica.nomeFantasia);
					$("#cnpj", this.workspace).val(result.pessoaJuridica.cnpj).mask("99.999.999/9999-99");
					//$("#inscricaoEstadual", this.workspace).val(result.pessoaJuridica.inscricaoEstadual).mask("999.999.999.999");
					$("#inscricaoEstadual", this.workspace).val(result.pessoaJuridica.inscricaoEstadual);
					$("#emailPJ", this.workspace).val(result.pessoaJuridica.email);
					$("#percentualComissaoPJ", this.workspace).mask("999,99");
					$("#percentualComissaoPJ", this.workspace).val(result.entregador.percentualComissao);

					if (result.entregador.comissionado) {
						$("#comissionadoPJ", this.workspace).click();
						$("#naoComissionadoPJ", this.workspace).uncheck();	
					} else {
						$("#naoComissionadoPJ", this.workspace).click();
						$("#comissionadoPJ", this.workspace).uncheck();
					}

					if (result.entregador.procuracao) {

						$("#estadoCivilProcuradorProcuracaoPJ", this.workspace).val(result.procuracaoEntregador.estadoCivil);
						$("#nacionalidadeProcuradorProcuracaoPJ", this.workspace).val(result.procuracaoEntregador.nacionalidade);
						$("#numeroPermissaoPJ", this.workspace).val(result.procuracaoEntregador.numeroPermissao);
						$("#nomeProcuradorProcuracaoPJ", this.workspace).val(result.procuracaoEntregador.procurador);
						$("#profissaoProcuradorProcuracaoPJ", this.workspace).val(result.procuracaoEntregador.profissao);
						$("#rgProcuradorProcuracaoPJ", this.workspace).val(result.procuracaoEntregador.rg).mask("99.999.999-9");
						$("#enderecoProcuradorProcuracaoPJ", this.workspace).val(result.procuracaoEntregador.endereco);
						
						entregadorController.obterCota(result.procuracaoEntregador.cota.numeroCota, false);

						$("#procuracaoPJ", this.workspace).click();
						$("#naoProcuracaoPJ", this.workspace).uncheck();

					} else {
						$("#naoProcuracaoPJ", this.workspace).click();
						$("#procuracaoPJ", this.workspace).uncheck();
					}
				}

				ENDERECO_ENTREGADOR.popularGridEnderecos();
				
				ENTREGADOR.carregarTelefones();
				
				$("#linkDadosCadastrais", this.workspace).click();
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

	confirmarExclusaoEntregador: function(idEntregador) {
		
		if(!verificarPermissaoAcesso(this.workspace )){
			return;
		}
		
		$( "#dialog-excluir-entregador").dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$.postJSON(
						contextPath + '/cadastro/entregador/removerEntregador',
						{'idEntregador' : idEntregador},
						function(result) {

							$("#dialog-excluir-entregador", this.workspace).dialog( "close" );
							
							$(".pessoasGrid", this.workspace).flexReload();
							
							exibirMensagem(
								result.tipoMensagem, 
								result.listaMensagens
							);
						},
						function(result) {

							exibirMensagemDialog(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens, ""
							);
							
						}
					);
					$( this, this.workspace ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this, this.workspace ).dialog( "close" );
				}
			}
		});
	},
	
	realizarImpressao: function(isPF) {

		var sufixo = "PJ";
		
		if(isPF) {
			
			sufixo = "PF";
		}

		var nomeJornaleiroProcuracao = "#nomeJornaleiroProcuracao" + sufixo;
		var boxProcuracao = "#boxProcuracao" + sufixo;
		var nacionalidadeProcuracao = "#nacionalidadeProcuracao" + sufixo;
		var estadoCivilProcuracao = "#estadoCivilProcuracao" + sufixo;
		var enderecoPDVPrincipalProcuracao = "#enderecoPDVPrincipalProcuracao" + sufixo;
		var bairroProcuracao = "#bairroProcuracao" + sufixo;
		var cidadeProcuracao = "#cidadeProcuracao" + sufixo;
		var numeroPermissaoProcuracao = "#numeroPermissao" + sufixo;
		var rgProcuracao = "#rgProcuracao" + sufixo;
		var cpfProcuracao = "#cpfProcuracao" + sufixo;

		var nomeProcurador = "#nomeProcuradorProcuracao" + sufixo;		
		var rgProcurador = "#rgProcuradorProcuracao" + sufixo;
		var estadoCivilProcurador = "#estadoCivilProcuradorProcuracao" + sufixo;
		var nacionalidadeProcurador = "#nacionalidadeProcuradorProcuracao" + sufixo;
		var enderecoDoProcurado = "#enderecoProcuradorProcuracao" + sufixo;
		
		var data = new Array();			
		
		data.push({ name:'procuracaoImpressao.nomeJornaleiro' , value: $(nomeJornaleiroProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.boxCota' , value: $(boxProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.nacionalidade' , value: $(nacionalidadeProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.estadoCivil' , value: $(estadoCivilProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.enderecoPDV' , value: $(enderecoPDVPrincipalProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.bairroPDV' , value: $(bairroProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.cidadePDV' , value: $(cidadeProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.numeroPermissao' , value: $(numeroPermissaoProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.rgJornaleiro' , value: $(rgProcuracao, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.cpfJornaleiro' , value: $(cpfProcuracao, this.workspace).val()});
		
		data.push({ name:'procuracaoImpressao.nomeProcurador' , value: $(nomeProcurador, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.rgProcurador' , value: $(rgProcurador, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.estadoCivilProcurador' , value: $(estadoCivilProcurador, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.nacionalidadeProcurador' , value: $(nacionalidadeProcurador, this.workspace).val()});
		data.push({ name:'procuracaoImpressao.enderecoDoProcurado' , value: $(enderecoDoProcurado, this.workspace).val()});

		var parameters = entregadorController.arrayToURLParameters(data);
		
		window.location = contextPath + '/cadastro/entregador/imprimirProcuracao' + parameters;
	},

	imprimirProcuracao: function(isPF) {

		entregadorController.cadastrarEntregador(isPF, true);
	},

	arrayToURLParameters: function(array) {

		var i;
		var params = "?";
		
		for (i = 0; i < array.length; i++) {
			
			params += i == 0 ? "" : "&";
			params += array[i].name + "=" + array[i].value; 
		}
		
		return params;
	},
	
	processarResultadoEntregadores: function(data) {

		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem,
				data.mensagens.listaMensagens
			);

			$(".grids", this.workspace).hide();

			return;
		}

		var i;

		for (i = 0; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell[lastIndex] = entregadorController.getActionEntregador(data.rows[i].id);
		}

		if (data.rows.length < 0) {

			$(".grids", this.workspace).hide();

			return;
		} 

		$(".grids", this.workspace).show();

		return data;
	},

	getActionEntregador: function(idEntregador) {

		return '<a href="javascript:;" onclick="entregadorController.editarEntregador('
				+ idEntregador
				+ ')" '
				+ ' style="cursor:pointer;border:0px;" title="Editar entregador">'
				+ '<img src="'  + contextPath + '/images/ico_editar.gif" border="0px" style="margin-right:10px;"/>'
				+ '</a>'
				+ '<a href="javascript:;" onclick="entregadorController.confirmarExclusaoEntregador('
				+ idEntregador
				+ ')" '
				+ ' style="cursor:pointer;border:0px;" title="Excluir entregador">'
				+ '<img src="' + contextPath + '/images/ico_excluir.gif" border="0px"/>'
				+ '</a>';
	},

	init: function() {

		this.initGridPrincipal();
		this.initTabs();
		this.bindButtonActions();
		this.setMascaras();
		
		$(document).ready(function(){
			
			focusSelectRefField($("#filtroEntregador-nomeRazaoSocial"));
			
			$(document.body).keydown(function(e) {
				
				if(keyEventEnterAux(e)){
					entregadorController.pesquisarEntregadores();
				}
				
				return true;
			});
		});		
	},
	
	initGridPrincipal: function() {
		$(".pessoasGrid", this.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome / Razão Social',
				name : 'nomeRazaoSocial',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'CPF / CNPJ',
				name : 'cpfCnpj',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Apelido / Nome Fantasia',
				name : 'apelidoNomeFantasia',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : 'Telefone',
				name : 'telefone',
				width : 100,
				sortable : false,
				align : 'left'
			}, {
				display : 'E-Mail',
				name : 'email',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 40,
				sortable : false,
				align : 'center'
			} ],
			sortname : "nomeRazaoSocial",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto', 
			singleSelect: true
		});
	},

	initTabs: function() {
		
		$( "#tabsNovoEntregador", this.workspace ).tabs();
	},
	
	bindButtonActions: function() {
		
		$("#linkBtnPesquisar", this.workspace).click(function() {

			entregadorController.pesquisarEntregadores();
		});
		
		$("#linkBtnNovoEntregadorPF", this.workspace).click(function() {
			
			entregadorController.novoEntregador(true);	
		});
		
		$("#linkBtnNovoEntregadorPJ", this.workspace).click(function() {
			
			entregadorController.novoEntregador(false);	
		}); 
		
		$("#linkCotas", this.workspace).click(function() {
			
			entregadorController.pesquisarCotas();
		});
		
		$("#linkEndereco", this.workspace).click(function() {
			
			ENDERECO_ENTREGADOR.popularGridEnderecos();
		});
		
		$("#linkTelefone", this.workspace).click(function() {
			
			ENTREGADOR.carregarTelefones();
		});
		
		$("#comissionadoPF", this.workspace).click(function() {

			entregadorController.showComissaoPF(true);
		});
		
		$("#naoComissionadoPF", this.workspace).click(function() {

			entregadorController.showComissaoPF();
		});

		$("#comissionadoPJ", this.workspace).click(function() {

			entregadorController.showComissaoPJ(true);
		});
		
		$("#naoComissionadoPJ", this.workspace).click(function() {

			entregadorController.showComissaoPJ();
		});
		
		$("#cpf", this.workspace).blur(function() {
			
			entregadorController.obterPessoaFisica($(this).val());
		});

		$("#cnpj", this.workspace).blur(function() {
			
			entregadorController.obterPessoaJuridica($(this).val());
		});
	},
	
	pesquisarEntregadores: function() {

		var formData = $("#formularioPesquisaEntregadores", this.workspace).serializeArray();

		$(".pessoasGrid", this.workspace).flexOptions({
			url : contextPath + '/cadastro/entregador/pesquisarEntregadores',
			preProcess : entregadorController.processarResultadoEntregadores,
			params : formData
		});

		$(".pessoasGrid", this.workspace).flexReload();

		$(".grids", this.workspace).show();
	},
	
	pesquisarCotas: function(){
		
		var idEntregador = $("#_edicao", this.workspace).val();
		
		$.postJSON(
			contextPath + '/cadastro/entregador/carregarAbaCota',
			{'idEntregador' : idEntregador},
			function(result) {
				
				if (result.tipoMensagem){
					
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
					
					return;
				}
				
				$("#nomeEntregadorAbaCota", this.workspace).text(result[0]);
				$("#nomeRoteiro", this.workspace).text(result[1]);
				$("#numeroBox", this.workspace).text(result[2]);
				$("#nomeRota", this.workspace).text(result[3]);
				
				$(".entregadoresCotaGrid", this.workspace).flexAddData({
					page: result[4].page, total: result[4].total, rows: result[4].rows
				});
				
				$(".entregadoresCotaGrid", this.workspace).flexOptions({
					url : contextPath + '/cadastro/entregador/atualizarGridCotas',
					params : [
					          {name: "idEntregador", value: idEntregador}
					         ]
				});
			}, null, true
		);
	},
	
	showComissaoPF: function(show) {

		if (show) {

			$(".comissionadoPf", this.workspace).show();

		} else {

			$(".comissionadoPf", this.workspace).hide();
		}
	},

	obterPessoaFisica: function(cpf) {

		if (!cpf || cpf == "") {
			return;
		}
		
		$.postJSON(
			contextPath + '/cadastro/entregador/obterPessoaFisica',
			{'cpf': cpf},
			function(result) {

				if (result.id) {

					$("#nomeEntregador", this.workspace).val(result.nome);
					$("#apelido", this.workspace).val(result.apelido);
					$("#cpf", this.workspace).val(result.cpf).mask("999.999.999-99");
					$("#rg", this.workspace).val(result.rg);
					$("#dataNascimento", this.workspace).val(result.dataNascimento.$).mask("99/99/9999");
					$("#dados-cadastral-orgaoEmissor", this.workspace).val(result.orgaoEmissor);
					$("#ufOrgaoEmissor", this.workspace).val(result.ufOrgaoEmissor);
					$("#estadoCivil", this.workspace).val(result.estadoCivil);
					$("#sexo", this.workspace).val(result.sexo);
					$("#nacionalidade", this.workspace).val(result.nacionalidade);
					$("#natural", this.workspace).val(result.natural);
					$("#emailPF", this.workspace).val(result.email);

				}
			},
			function(result) {

				$("#nomeEntregador", this.workspace).val("");
				$("#apelido", this.workspace).val("");
				$("#cpf", this.workspace).val("");
				$("#rg", this.workspace).val("");
				$("#dataNascimento", this.workspace).val("");
				$("#dados-cadastral-orgaoEmissor", this.workspace).val("");
				$("#ufOrgaoEmissor", this.workspace).val("");
				$("#estadoCivil", this.workspace).val("");
				$("#sexo", this.workspace).val("");
				$("#nacionalidade", this.workspace).val("");
				$("#natural", this.workspace).val("");
				$("#emailPF", this.workspace).val("");

				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			},
			true
		);
	},
	
	setMascaras: function() {

		$("#dataNascimento", this.workspace).mask("99/99/9999");
		$("#cpf", this.workspace).mask("999.999.999-99");
		$("#rgProcuradorProcuracaoPF", this.workspace).mask("99.999.999-9");
		$("#cnpj", this.workspace).mask("99.999.999/9999-99");
		$("#rgProcuradorProcuracaoPJ", this.workspace).mask("99.999.999-9");
		/*$("input[id^='percentualComissaoPF']", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		$("input[id^='percentualComissaoPJ']", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});*/
		
		$("#filtroEntregador.nomeRazaoSocial",this.workspace).autocomplete({source: ""});
		
		$("#filtroEntregador.apelidoNomeFantasia",this.workspace).autocomplete({source: ""});
	},
	
	showComissaoPJ: function(show) {

		if (show) {

			$(".comissionadoPJ", this.workspace).show();

		} else {

			$(".comissionadoPJ", this.workspace).hide();
		}
	},
	
	obterPessoaJuridica: function(cnpj) {

		if (!cnpj || cnpj == "") {
			return;
		}
		
		$.postJSON(
			contextPath + '/cadastro/entregador/obterPessoaJuridica',
			{'cnpj': cnpj},
			function(result) {

				if (result.id) {

					$("#razaoSocial", this.workspace).val(result.razaoSocial);
					$("#nomeFantasia", this.workspace).val(result.nomeFantasia);
					$("#cnpj", this.workspace).val(result.cnpj).mask("99.999.999/9999-99");
					//$("#inscricaoEstadual", this.workspace).val(result.inscricaoEstadual).mask("999.999.999.999");
					$("#emailPJ", this.workspace).val(result.email);	

				}
			},
			function(result) {

				$("#razaoSocial", this.workspace).val("");
				$("#nomeFantasia", this.workspace).val("");
				$("#cnpj", this.workspace).val("");
				$("#inscricaoEstadual", this.workspace).val("");
				$("#emailPJ", this.workspace).val("");

				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			},
			true
		);
	}
	
}, BaseController);

//@ sourceURL=entregador.js