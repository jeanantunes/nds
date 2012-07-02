<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/emissaoCE.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">
	
	var ECE = new EmissaoCE('${pageContext.request.contextPath}', 'ECE');
		
	function popup_pesq_fornecedor() {
		
			$( "#dialog-pesq-fornecedor" ).dialog({
				resizable: false,
				height:300,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$( ".forncedoresSel" ).css('display','table-cell');
						$( ".linhaForncedoresSel" ).show();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
	};
		
	function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:210,
			width:650,
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
		      
	};
	
	function popup_excluir() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:230,
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
	};
	
  //callback function to bring a hidden box back
		function callback() {
			setTimeout(function() {
				$( "#effect:visible").removeAttr( "style" ).fadeOut();

			}, 1000 );
		};	

	function mostrar(){
		$(".grids").show();
	}	
	$(function() {
		$( "#dtRecolhimentoDe" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dtRecolhimentoAte" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
	
	function confirmar(){
		$(".dados").show();
	}
	
	function pesqEncalhe(){
		$(".dadosFiltro").show();
	}
	
	function removeFornecedor(){
		$( ".forncedoresSel" ).fadeOut('fast');
		$( ".linhaForncedoresSel" ).hide();
	}
	
</script>
<style type="text/css">
  .dados, .dadosFiltro{display:none;}

  #dialog-novo, #dialog-alterar, #dialog-excluir{display:none; font-size:12px;}
  .box_field{width:200px;}
  
  #dialog-pesq-fornecedor fieldset {width:450px!important;}
.forncedoresSel, .linhaForncedoresSel{display:none;}
#dialog-pesq-fornecedor{display:none;}

.fornecedores ul{margin:0px; padding:0px;}
.fornecedores li{display:inline; margin:0px; padding:0px;}
  </style>
</head>

<body>
<form action="" method="get" id="form1" name="form1">

<div id="dialog-pesq-fornecedor" title="Selecionar Fornecedor">
<fieldset>
	<legend>Selecione um ou mais Fornecedores</legend>
    <select name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
      <option>Dinap</option>
      <option>FC</option>
      <option>Treelog</option>
    </select>

</fieldset>
</div>






<div id="dialog-excluir" title="Suspensão de Cotas">
	<p>Confirma a Suspensão destas Cotas?</p>
    
   	  <b>Motivo</b><br clear="all" />

        <textarea cols="" rows="4" style="width:340px;"></textarea>
    </fieldset>
</div>





<div id="dialog-novo" title="CE Antecipada">
	<p>Data Antecipada:      <input name="datepickerDe" type="text" id="datepickerDe" style="width:80px;"/></p>
	<p>Confirma a gravação dessas informações? </p>     
</div>








<div class="corpo">
  <div class="header">
  	<div class="sub-header">
    	<div class="logo"><img src="../images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></div>
        
        <div class="titAplicacao">
        	<h1>Treelog S/A. Logística e Distribuição - SP</h1>

			<h2>CNPJ: 00.000.000/00001-00</h2>
            <h3>Distrib vs.1</h3>
        </div>
        
        <div class="usuario"><a href="../login.htm"><img src="../images/bt_sair.jpg" alt="Sair do Sistema" title="Sair do Sistema" width="63" height="27" border="0" align="right" /></a>
            <br clear="all" />
          <span>Usuário: Junior Fonseca</span>
          <span>
          <script type="text/javascript" language="JavaScript">
          diaSemana();
          </script>

          </span>
        </div>
    
    </div>
  
  </div>
  <div class="bg_menu">
  <div id="menu_principal">
  <ul>
    <li><a href="../index.htm"><span class="classHome">&nbsp;</span>Home</a></li>
    <li><a href="javascript:;" class="trigger"><span class="classCadastros">&nbsp;</span>Cadastro</a>
        <ul>
            <li><a href="../Cadastro/cadastro_produtos.htm">Produto</a></li>
            <li><a href="../Cadastro/cadastro_edicao.htm">Edição</a></li>
            <li><a href="../Cadastro/cadastro_cotas.htm">Cota</a></li>
            <li><a href="../Cadastro/cadastro_fiador.htm">Fiador</a></li>
            <li><a href="../Cadastro/cadastro_entregador.htm">Entregador</a></li>
            <li><a href="../Cadastro/cadastro_transportador.htm">Transportador</a></li>
            <li><a href="../Cadastro/cadastro_fornecedor.htm">Fornecedor</a></li>
            <li><a href="../Cadastro/roteirizacao.htm">Roteirização</a></li>
            
            <li><a href="../Cadastro/cadastro_box.htm">Box</a></li>
            <li><a href="../Cadastro/cadastro_bancos.htm">Banco</a></li>
            <li><a href="../Cadastro/help_cadastros.htm">Help</a></li>
		</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classLancamento">&nbsp;</span>Lançamento</a>
        <ul>
	<li class="criando"><a href="javascript:;"  onclick="alert('Serviço em construção.');">Conectividade</a></li>
    <li><a href="../Lancamento/balanceamento_da_matriz.htm">Balanceamento da Matriz</a></li>
  <li class="criando"><a href="javascript:;"  onclick="alert('Serviço em construção.');">Cálculo Distribuição</a></li>
  <li><a href="../Lancamento/furo_publicacao.htm">Furo de Lançamento</a></li>
  <li><a href="../Lancamento/cadastro_parciais.htm">Parciais</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Mix de Produto</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Fixação de Reparte</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Reserva Técnica</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Redistribuição</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Venda Suplementar</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Cotas Suspensas/Novas</a></li>
  <li><a href="../Lancamento/relatorio_vendas.htm">Relatório de Vendas</a></li>
  <li><a href="../Lancamento/venda_produto.htm">Venda por Produto</a></li>
    <li><a href="../Lancamento/help_lancamento.htm">Help</a></li>
</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classEstoque">&nbsp;</span>Estoque</a>
        <ul>
    <li><a href="../Estoque/recebimento_fisico.htm">Recebimento Fisico</a></li>
    <li><a href="../Estoque/lancamento_faltas_sobras.htm">Lançamento  Faltas e Sobras</a></li>
    <!--<li><a href="../Estoque/ajuste_estoque.htm">Ajuste Estoque - Inventário</a></li>-->
    <li><a href="../Estoque/consulta_notas_sem_fisico.htm">Consulta Notas</a></li>
    <li><a href="../Estoque/consulta_faltas_sobras.htm">Consulta Faltas e Sobras</a></li>
    <li><a href="../Estoque/extrato_edicao.htm">Extrato de Edição</a></li>
    <li><a href="../Estoque/visao_estoque.htm">Visão do Estoque</a></li>
    <li><a href="../Estoque/edicoes_fechadas.htm">Edições Fechadas com Saldo</a></li>
    <li><a href="../Estoque/help_estoque.htm">Help</a></li>
</ul>
    </li>
    
    
    
    <li><a href="javascript:;" class="trigger"><span class="classExpedicao">&nbsp;</span>Expedição</a>
        <ul>
    <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Interface Picking</a></li>
  <li><a href="../Expedicao/mapa_abastecimento.htm">Mapa Abastecimento</a></li>
    <li><a href="../Expedicao/jornaleiro_ausente.htm">Cota Ausente</a></li>
    <li><a href="../Expedicao/geracao_nfe.htm">Geração NE/NF-e</a></li>
    <li><a href="../Expedicao/resumo_expedicao_nota.htm">Resumo de Expedição</a></li>
    <li><a href="../Expedicao/romaneios.htm">Romaneios</a></li>
    <li><a href="../Expedicao/help_expedicao.htm">Help</a></li>
</ul>  
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classDevolucao">&nbsp;</span>Devolução</a>
    <ul>
	<li><a href="balanceamento_da_matriz_recolhimento.htm">Balanceamento da Matriz</a></li>
    <li><a href="consulta_informe_encalhe.htm">Informe Recolhimento</a></li>
    <li><a href="ce_antecipada.htm">CE Antecipada - Produto</a></li>
    <li><a href="emissao_ce.htm">Emissão CE</a></li>
    <li><a href="conferencia_encalhe_jornaleiro.htm">Conferência de Encalhe</a></li>
    <li><a href="venda_encalhe.htm">Venda de Encalhe / Suplementar</a></li>
  <li><a href="fechamento_fisico_logico.htm">Fechamento Encalhe</a></li>
  <li><a href="devolucao_fornecedor.htm">Devolução ao Fornecedor</a></li>
   <!--<li><a href="Devolucao/digitacao_contagem_devolucao.htm">Devolução Fornecedor</a></li>-->
  <li><a href="emissao_bandeira.htm">Emissão das Bandeiras</a></li>
   <li><a href="chamadao.htm">Chamadão</a></li>
   <li><a href="edicoes_chamada.htm">Consulta Encalhe</a></li>
   <li><a href="help_devolucao.htm">Help</a></li>
</ul></li>
<li><a href="javascript:;" class="trigger"><span class="classNFe">&nbsp;</span>NF-e</a>
    <ul>
	<li><a href="../Expedicao/tratamento_arquivo_retorno_nfe.htm">Retorno NF-e</a></li>
    <li><a href="../Expedicao/consulta_nfe_encalhe_tratamento.htm">Consulta NFE Encalhe Tratamento</a></li>
    <li><a href="../Expedicao/cancelamento_nfe.htm">Cancelamento NFE</a></li>
    <li><a href="../Expedicao/painel_monitor_nfe.htm">Painel Monitor NF-e</a></li>
    <li><a href="../Recolhimento/help_recolhimento.htm">Help</a></li>    
</ul>
    
    
    </li>
    
    
<li><a href="javascript:;" class="trigger"><span class="classFinanceiro">&nbsp;</span>Financeiro</a>
        <ul>
    <li><a href="../Financeiro/baixa_bancaria.htm">Baixa Financeira</a></li>
    <li><a href="../Financeiro/negociar_divida.htm">Negociação de Divida</a></li>
   <li><a href="../Financeiro/debitos_creditos.htm">Débitos / Créditos Cota</a></li>
    <li><a href="../Financeiro/impressao_boletos.htm">Impressão de Boletos</a></li>
    <li><a href="../Financeiro/cadastro_manutencao_status.htm">Manutenção de Status Cota</a></li>
    <li><a href="../Financeiro/suspensao_jornaleiro.htm">Suspensão  Cota</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Mapa Financeiro Diário</a></li>
    <li><a href="../Financeiro/consulta_boletos_jornaleiros.htm">Consulta Boletos por Cota</a></li>
    <li><a href="../Financeiro/conta_corrente.htm">Conta Corrente</a></li>
    <li><a href="../Financeiro/historico_inadimplencia.htm">Inadimplência</a></li>
  <li><a href="../Financeiro/consignado_cota.htm">Consignado Cota</a></li>
    <li><a href="../Financeiro/help_financeiro.htm">Help</a></li>
</ul>
    
    
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classAdministracao">&nbsp;</span>Administração</a>
    <ul>
	<li><a href="../Administracao/fechar_dia.htm">Fechamento Diário</a></li>
    <li><a href="../Administracao/workflow_aprovacao.htm">Controle Aprovação</a></li>
  <li><a href="../Administracao/painel_operacional.htm">Painel Operacional</a></li>
    <li><a href="../Administracao/painel_processamento.htm">Painel Processamento</a></li>
    <li><a href="../Administracao/fallowup_sistema.htm">Follow Up do Sistema</a></li>
    <li><a href="../Administracao/cadastro_usuario.htm">Usuários</a></li>
    <li class="criando"><a href="javascript:" onclick="alert('Serviço em construção.');">Grupos de Acesso</a></li>
    <li><a href="../Administracao/cadastro_regras.htm">Regras</a></li>
  <li><a href="../Administracao/cadastro_calendario.htm">Calendário</a></li>
  <li><a href="../Administracao/cadastro_tipos_movimento.htm">Tipo de Movimento</a></li>
  <li><a href="../Administracao/gerar_arquivo_jornaleiro.htm">Gerar Arquivo Jornaleiro</a></li>
  <li><a href="../Administracao/cadastro_tipo_nota.htm">Tipo de Nota</a></li>
       
           <li><a href="../Administracao/cadastro_servico_entrega.htm">Serviço de Entrega</a></li>
   <li><a href="../Administracao/cadastro_tipo_desconto.htm">Tipo de Desconto Cota</a></li>
   <li><a href="../Administracao/tipos_produtos.htm">Tipos de Produtos</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Plano de Contas</a></li>
  <li><a href="../Administracao/parametros_cobranca.htm">Parâmetros de Cobrança</a></li>
  <li><a href="../Administracao/parametros_sistema.htm">Parâmetros de Sistema</a></li>
  <li><a href="../Administracao/parametros_distribuidor.htm">Parâmetros Distribuidor</a></li>
  <li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Histórico do PDV</a></li>
    <li><a href="../Administracao/help_administracao.htm">Help</a></li>
</ul></li>
    <li><a href="../help.htm"><span class="classHelp">&nbsp;</span>Help</a></li>
</ul>
  <br class="clearit" />
</div>
</div>
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>CE Antecipada < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend>Pesquisar CE´s</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td nowrap="nowrap">Dt. Recolhimento:</td>
    <td width="113"><input name="dtRecolhimentoDe" type="text" id="dtRecolhimentoDe" style="width:80px;"/></td>
    <td width="28" colspan="-1" align="center">Até</td>
    <td width="130"><input name="dtRecolhimentoAte" type="text" id="dtRecolhimentoAte" style="width:80px;"/></td>
    <td>Intervalo Box:</td>
    <td width="91"><select name="jumpMenu" id="jumpMenu" style="width:80px;">
      <option selected="selected"> </option>
      <option>Box 1</option>
      <option>Box 2</option>
      <option>Box 3</option>
      <option>Box 4</option>
      <option>Box 5</option>
      <option>Box 6</option>
    </select></td>
    <td width="22" align="center">Até</td>
    <td width="91"><select name="jumpMenu2" id="jumpMenu2" style="width:80px;">
      <option selected="selected"> </option>
      <option>Box 1</option>
      <option>Box 2</option>
      <option>Box 3</option>
      <option>Box 4</option>
      <option>Box 5</option>
      <option>Box 6</option>
    </select></td>
    <td width="28">Cota:</td>
    <td width="68"><input type="text" style="width:60px;"/></td>
    <td width="30" align="center">Até</td>
    <td width="104"><input type="text" style="width:60px;"/></td>
  </tr>
  <tr>
    <td width="105">Roteiro:</td>
    <td><select  style="width:100px;">
      <option selected="selected"> </option>
      <option>Roteiro 1</option>
      <option>Roteiro 2</option>
      <option>Roteiro 3</option>
      <option>Roteiro 4</option>
      <option>Roteiro 5</option>
      <option>Roteiro 6</option>
      </select></td>
    <td>Rota:</td>
    <td><select name="select" style="width:130px;">
      <option selected="selected"> </option>
      <option>Rota 1</option>
      <option>Rota 2</option>
      <option>Rota 3</option>
      <option>Rota 4</option>
      <option>Rota 5</option>
      <option>Rota 6</option>
      </select></td>
    <td width="79" align="right"><input type="checkbox" name="checkbox" id="checkbox" onclick="imprimiPernosalizado();" /></td>
    <td> Capa </td>
    <td><input type="checkbox" name="checkbox2" id="checkbox2" class="imprimirPersonalizada" /></td>
    <td><span class="imprimirPersonalizada">Personalizada?</span></td>
    <td align="right">&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>Fornecedor:</td>
    <td colspan="2"><div style="float:left; line-height:35px; margin-right:5px;"><a href="javascript:;" onclick="popup_pesq_fornecedor();">clique para selecionar</a></div></td>
    <td colspan="8"><div class="fornecedores" style="float:left;">
      <ul>
        <li>
          <div class="forncedoresSel"><label>Dinap</label> <a href="javascript:;" onclick="removeFornecedor();"><img src="../images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" /></a></div>
          </li>
        <li>
          <div class="forncedoresSel"><label>FC </label><a href="javascript:;" onclick="removeFornecedor();"><img src="../images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" /></a></div>
          </li>
        <li>
          <div class="forncedoresSel"><label>Dinap</label> <a href="javascript:;" onclick="removeFornecedor();"><img src="../images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" /></a></div>
          </li>
        <li>
          <div class="forncedoresSel"><label>FC </label><a href="javascript:;" onclick="removeFornecedor();"><img src="../images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" /></a></div>
          </li>
        <li>
          <div class="forncedoresSel"><label>Dinap</label> <a href="javascript:;" onclick="removeFornecedor();"><img src="../images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" /></a></div>
          </li>
        <li>
          <div class="forncedoresSel"><label>FC </label><a href="javascript:;" onclick="removeFornecedor();"><img src="../images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" /></a></div>
          </li>
      </ul> </div></td>
    <td><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
    </tr>
  </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend> Emissão CE</legend>
        <div class="grids" style="display:none;">
		  <table class="ceEmissaoGrid"></table>
		  <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>


<span class="bt_novos" title="Imprimir"><a href="ce_modelo_1.htm" target="_blank"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir CE</a></span>
        </div>
		
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

        

    
    </div>
</div> 
<script>
	$(".ceEmissaoGrid").flexigrid({
			url : '../xml/emissaoCe-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 670,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde. Exemplares',
				name : 'Total Exemplares',
				width : 130,
				sortable : true,
				align : 'center'
			}],
			width : 960,
			height : 180
		});
</script>
</form>

</body>