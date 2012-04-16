<head>
<style>
#dialog-pdv{display:none!important;}
.diasFunc label, .finceiro label, .materialPromocional label{ vertical-align:super;}
.complementar label{ vertical-align:super; margin-right:5px; margin-left:5px;}
</style>
<script>

	function popup_novoEntregador(isCadastroPF) {

		if (isCadastroPF) {

			$("#dadosCadastraisPF").show();
			$("#dadosCadastraisPJ").hide();

		} else {

			$("#dadosCadastraisPJ").show();
			$("#dadosCadastraisPF").hide();
		}

		$( "#dialog-novoEntregador" ).dialog({
			resizable: false,
			height:610,
			width:840,
			modal: true,
			buttons: {
				"Confirmar": function() {

					var formData;
					var url;

					if (isCadastroPF) {

						formData = $("#formDadosEntregadorPF").serializeArray();
						url = '<c:url value="/cadastro/entregador/cadastrarEntregadorPessoaFisica" />';

					} else {

						formData = $("#formDadosEntregadorPJ").serializeArray();
						url = '<c:url value="/cadastro/entregador/cadastrarEntregadorPessoaJuridica" />';
					}

					$.postJSON(
						url,
						formData,
						function(result) {

							$("#dialog-novoEntregador").dialog( "close" );

							$(".pessoasGrid").flexReload();
							
							exibirMensagem(
								result.tipoMensagem, 
								result.listaMensagens
							);
						},
						function(result) {

							exibirMensagemDialog(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens
							);
						},
						true
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}

	function editarEntregador(idEntregador) {

		$.postJSON(
			'<c:url value="/cadastro/entregador/editarEntregador" />',
			{'idEntregador' : idEntregador},
			function(result) {

				if (result.pessoaFisica) {

					popup_novoEntregador(true);

					$("#idEntregadorPF").val(result.entregador.id);
					$("#codigoEntregadorPF").val(result.entregador.codigo);
					$("#inicioAtividadePF").html(result.entregador.inicioAtividade.$);
					$("#nomeEntregador").val(result.pessoaFisica.nome);
					$("#apelido").val(result.pessoaFisica.apelido);
					$("#cpf").val(result.pessoaFisica.cpf);
					$("#rg").val(result.pessoaFisica.rg);
					$("#dataNascimento").val(result.pessoaFisica.dataNascimento ? result.pessoaFisica.dataNascimento.$ : "");
					$("#orgaoEmissor").val(result.pessoaFisica.orgaoEmissor);
					$("#ufOrgaoEmissor").val(result.pessoaFisica.ufOrgaoEmissor);
					$("#estadoCivil").val(result.pessoaFisica.estadoCivil);
					$("#sexo").val(result.pessoaFisica.sexo);
					$("#nacionalidade").val(result.pessoaFisica.nacionalidade);
					$("#emailPF").val(result.pessoaFisica.email);
					$("#percentualComissaoPF").val(result.entregador.percentualComissao);

					if (result.entregador.comissionado) {
						$("#comissionadoPF").click();
						$("#naoComissionadoPF").uncheck();	
					} else {
						$("#naoComissionadoPF").click();
						$("#comissionadoPF").uncheck();
					}

					if (result.entregador.procuracao) {
						$("#procuracaoPF").click();
						$("#naoProcuracaoPF").uncheck();	
					} else {
						$("#naoProcuracaoPF").click();
						$("#procuracaoPF").uncheck();
					}

				} else {

					popup_novoEntregador(false);

					$("#idEntregadorPJ").val(result.entregador.id);
					$("#codigoEntregadorPJ").val(result.entregador.codigo);
					$("#inicioAtividadePJ").html(result.entregador.inicioAtividade.$);
					$("#razaoSocial").val(result.pessoaJuridica.razaoSocial);
					$("#nomeFantasia").val(result.pessoaJuridica.nomeFantasia);
					$("#cnpj").val(result.pessoaJuridica.cnpj);
					$("#inscricaoEstadual").val(result.pessoaJuridica.inscricaoEstadual);
					$("#emailPJ").val(result.pessoaJuridica.email);
					$("#percentualComissaoPJ").val(result.entregador.percentualComissao);

					if (result.entregador.comissionado) {
						$("#comissionadoPJ").click();
						$("#naoComissionadoPJ").uncheck();	
					} else {
						$("#naoComissionadoPJ").click();
						$("#comissionadoPJ").uncheck();
					}

					if (result.entregador.procuracao) {
						$("#procuracaoPJ").click();
						$("#naoProcuracaoPJ").uncheck();	
					} else {
						$("#naoProcuracaoPJ").click();
						$("#procuracaoPJ").uncheck();
					}
				}
			},
			function(result) {
				
				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
				);
			},
			true
		);
	}

	function confirmarExclusaoEntregador(idEntregador) {

		$( "#dialog-excluir-entregador" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$.postJSON(
						'<c:url value="/cadastro/entregador/removerEntregador" />',
						{'idEntregador' : idEntregador},
						function(result) {

							$("#dialog-excluir-entregador").dialog( "close" );
							
							$(".pessoasGrid").flexReload();
							
							exibirMensagem(
								result.tipoMensagem, 
								result.listaMensagens
							);
						},
						function(result) {

							exibirMensagemDialog(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens
							);
						}
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};

	function processarResultadoEntregadores(data) {

		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem,
				data.mensagens.listaMensagens
			);

			$(".grids").hide();

			return;
		}

		var i;

		for (i = 0; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell[lastIndex] = getActionEntregador(data.rows[i].id);
		}

		if (data.rows.length < 0) {

			$(".grids").hide();

			return;
		} 

		$(".grids").show();

		return data;
	}

	function getActionEntregador(idEntregador) {

		return '<a href="javascript:;" onclick="editarEntregador('
				+ idEntregador
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar entregador">'
				+ '<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>'
				+ '</a>'
				+ '<a href="javascript:;" onclick="confirmarExclusaoEntregador('
				+ idEntregador
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir entregador">'
				+ '<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px"/>'
				+ '</a>';
	}

	$(function() {

		$(".pessoasGrid").flexigrid({
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
				display : 'Apelido/Nome Fantasia',
				name : 'apelidoNomeFantasia',
				width : 110,
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
				display : 'Ação',
				name : 'acao',
				width : 60,
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
			height : 255, 
			singleSelect: true
		});

		$( "#tabsNovoEntregador" ).tabs();
	});

	function pesquisarEntregadores() {

		var formData = $("#formularioPesquisaEntregadores").serializeArray();

		$(".pessoasGrid").flexOptions({
			url : '<c:url value="/cadastro/entregador/pesquisarEntregadores" />',
			preProcess : processarResultadoEntregadores,
			params : formData
		});

		$(".pessoasGrid").flexReload();

		$(".grids").show();
	}
</script>
</head>
<body>

	<div id="dialog-excluir-entregador" title="Excluir Entregador" style="display:none">
		<p>Confirma a exclusão deste Entregador?</p>
	</div>

	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info">
				</span> 
			</p>
		</div>
    
      <fieldset class="classFieldset" title="Capataz, Entregador, Mula, Carreteiro">
   	    <legend> Pesquisar Entregador</legend>
   	    
   	    <form id="formularioPesquisaEntregadores">
   	    
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="117">Nome/Razão Social:</td>
              <td colspan="3"><input type="text" name="filtroEntregador.nomeRazaoSocial" id="textfield2" style="width:160px;"/>
              </td>
                <td width="146">Apelido / Nome Fantasia:</td>
              <td width="145"><input type="text" name="filtroEntregador.apelidoNomeFantasia" id="textfield" style="width:130px;"/></td>
                <td width="79">CPF / CNPJ:</td>
              <td width="152"><input type="text" name="filtroEntregador.cpfCnpj" id="textfield" style="width:150px;"/></td>
              <td width="106">
              	<span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisarEntregadores();">Pesquisar</a></span>
              </td>
            </tr>
          </table>
         </form>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend title="Capataz, Entregador, Mula, Carreteiro">Entregadores Cadastrados</legend>
        <div class="grids" style="display:none;">
        	<table class="pessoasGrid"></table>
        </div>
        <span class="bt_novos" title="Novo">
           	<a href="javascript:;" onclick="popup_novoEntregador(true);">
           		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CPF
           	</a>
        </span>

        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_novoEntregador(false);">
        	<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CNPJ</a>
        </span>
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
	</div>
	
	<jsp:include page="novoEntregador.jsp" />
</body>