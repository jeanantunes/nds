<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="../css/NDS.css" />
<link rel="stylesheet" type="text/css" href="../scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" href="../css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/acessoRapido.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css" href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript">

function incluirSegmento() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$( "#dialog-novo" ).dialog({
		resizable: false,
		height:500,
		width:650,
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

function excluirSegmento() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$( "#dialog-excluir" ).dialog({
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
};

function porCota(){
	$('.porCota').show();
	$('.porArea').hide();
}
function porArea(){
	$('.porCota').hide();
	$('.porArea').show();
}
function filtroPorCota(){
	$('.filtroPorCota').show();
	$('.filtroPorArea').hide();
	$('.porArea').hide();
}
function filtroPorArea(){
	$('.filtroPorCota').hide();
	$('.filtroPorArea').show();
	$('.porCota').hide();
}

</script>

</head>

<body>

<div id="dialog-excluir" title="Excluir Segmento Não Recebida">
	<p>Confirma a exclusão desta Segmento?</p>
</div>

<div id="dialog-novo" title="Novo Segmento">
	<fieldset style="width:600px!important;">
    	<legend>Pesquisar Banca</legend>
        <table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="33">Cota:</td>
    <td width="85"><input type="text" name="textfield" id="textfield" style="width:80px;"/></td>
    <td width="41">Nome:</td>
    <td width="217"><input type="text" name="textfield2" id="textfield2" style="width:200px;"/></td>
    <td width="98"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
  </tr>
</table>
	</fieldset>
    <br clear="all" />

    <fieldset style="width:600px!important; margin-top:10px;">
    	<legend>Bancas Pesquisadas</legend>
        <table class="pesqBancasGrid"></table>
    </fieldset>
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
    	<div id="div_acessoRapido" class="box_acesso_rapido">
          <span class="titulo"><a href="javascript:;" onclick="acessoRapido();" style="float:left;">Acesso Rápido</a></span>
          <a href="javascript:;" onclick="acessoRapidoFechar();" style="float:right;" class="fechar"><span class="ui-icon ui-icon-close">&nbsp;</span></a>
				
          <div class="class_acessos">
             	<ul id="acessoRapido"></ul>
          </div>
       </div>

    </div>
  
  </div>
  <div class="bg_menu">
  <div id="menu_principal">
  <ul>
    <li><a href="../index.htm"><span class="classHome">&nbsp;</span>Home</a>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classCadastros">&nbsp;</span>Cadastro</a>
        <ul>
            <li><a href="../Cadastro/cadastro_produtos.htm">Produto</a></li>
            <li><a href="../Cadastro/cadastro_edicao.htm">Edição</a></li>
            <li><a href="../Cadastro/cadastro_cotas.htm">Cota</a></li>
            <li><a href="../Cadastro/jornaleiros_equivalentes.htm">Cotas Base</a></li>
            <li><a href="../Cadastro/cadastro_fiador.htm">Fiador</a></li>
            <li><a href="../Cadastro/cadastro_entregador.htm">Entregador</a></li>
            <li><a href="../Cadastro/cadastro_transportador.htm">Transportador</a></li>
            <li><a href="../Cadastro/cadastro_fornecedor.htm">Fornecedor</a></li>
            <li><a href="../Cadastro/roteirizacao.htm">Roteirização</a></li>
            
            <li><a href="../Cadastro/cadastro_box.htm">Box</a></li>
            <li><a href="../Cadastro/cadastro_bancos.htm">Banco</a></li>
            <li><a href="../Cadastro/alteracao_cotas.htm">Alteração / Cota</a></li>
            <!--<li><a href="../Cadastro/consulta_geral_equivalentes.htm">Consulta Geral Equivalentes</a></li>-->
            <li><a href="../Cadastro/help_cadastros.htm">Help</a></li>
</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classLancamento">&nbsp;</span>Lançamento</a>
        <ul>
	<!--<li class="criando"><a href="javascript:;"  onclick="alert('Serviço em construção.');">Conectividade</a></li>-->
    <li><a href="../Lancamento/balanceamento_da_matriz.htm">Balanceamento da Matriz</a></li>
  <li><a href="../Lancamento/furo_publicacao.htm">Furo de Lançamento</a></li>
  <li><a href="../Lancamento/cadastro_parciais.htm">Parciais</a></li>
  <li><a href="../Lancamento/relatorio_vendas.htm">Relatório de Vendas</a></li>
  <li><a href="../Lancamento/venda_produto.htm">Venda por Produto</a></li>
  <li><a href="../Lancamento/relatorio_tipos_produtos.htm">Relatório Tipos de Produtos</a></li>
    <li><a href="../Lancamento/help_lancamento.htm">Help</a></li>
</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classDistribuicao">&nbsp;</span>Distribuição</a>
        <ul>
    <li><a href="matriz_distribuicao.htm">Matriz de Distribuição</a></li>
    <li><a href="analise_estudo.htm">Análise de Estudos</a></li>
    <li><a href="mix_produto.htm">Mix de Produto</a></li>
    <li ><a href="fixacao.htm">Fixação de Reparte</a></li>
    <li><a href="classificacao_nao_recebida.htm">Classificação Não Recebida</a></li>
    <li><a href="segmento_nao_recebido.htm">Segmento Não Recebido</a></li>
    <li><a href="tratamento_excessao.htm">Exceções de Segmentos e Parciais</a></li>
    <li><a href="ajustes_reparte.htm">Ajustes Reparte</a></li>
    <li><a href="engloba_desengloba.htm">Desenglobação</a></li>
    <li><a href="histograma.htm">Histograma de Venda</a></li>
    <li><a href="historico_venda.htm">Histórico de Venda</a></li>
    <li><a href="regiao.htm">Região</a></li><li><a href="area_influencia.htm">Área de Influência/Gerador de Fluxo</a></li><li><a href="informacoes_produtos.htm">Informações do Produto</a></li>
    <li><a href="caracteristica_distribuicao.htm">Caracteristicas de Distribuição</a></li>
    <li><a href="help_distribuicao.htm">Help</a></li>
</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classEstoque">&nbsp;</span>Estoque</a>
        <ul>
    <li><a href="../Estoque/recebimento_fisico.htm">Recebimento Fisico</a></li>
    <li><a href="../Estoque/lancamento_faltas_sobras.htm">Lançamento  Faltas e Sobras</a></li>
<!--
    <li><a href="../Estoque/relatorio_faltas_sobras.htm">Relatório Faltas e Sobras</a></li>-->
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
    <li><a href="../Expedicao/interface_picking.htm">Interface Picking</a></li>
  <li><a href="../Expedicao/mapa_abastecimento.htm">Mapa Abastecimento</a></li>
  <li><a href="../Expedicao/confirma_expedicao.htm">Confirma Expedição</a></li>
    <li><a href="../Expedicao/jornaleiro_ausente.htm">Cota Ausente - Reparte</a></li>
    <li><a href="../Expedicao/geracao_nfe.htm">Nota de Envio</a></li>
    <li><a href="../Expedicao/resumo_expedicao_nota.htm">Resumo de Expedição</a></li>
    <li><a href="../Expedicao/romaneios.htm">Romaneios</a></li>
    <li><a href="../Expedicao/help_expedicao.htm">Help</a></li>
</ul>  
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classDevolucao">&nbsp;</span>Devolução</a>
    <ul>
	<li><a href="../Devolucao/balanceamento_da_matriz_recolhimento.htm">Balanceamento da Matriz</a></li>
    <li><a href="../Devolucao/consulta_informe_encalhe.htm">Informe Recolhimento</a></li>
    <li><a href="../Devolucao/ce_antecipada.htm">CE Antecipada - Produto</a></li>
    <li><a href="../Devolucao/emissao_ce.htm">Emissão CE</a></li>
    <li><a href="../Devolucao/conferencia_encalhe_jornaleiro.htm">Conferência de Encalhe</a></li>
    <li><a href="../Devolucao/venda_encalhe.htm">Venda de Encalhe / Suplementar</a></li>
  <li><a href="../Devolucao/fechamento_fisico_logico.htm">Fechamento Encalhe</a></li>
    <li><a href="../Devolucao/fechamento_ce_integracao.htm">Fechamento CE - Integração</a></li>
  <li><a href="../Devolucao/devolucao_fornecedor.htm">Devolução ao Fornecedor</a></li>
   <!--<li><a href="../Devolucao/digitacao_contagem_devolucao.htm">Devolução Fornecedor</a></li>-->
  <li><a href="../Devolucao/emissao_bandeira.htm">Emissão das Bandeiras</a></li>
   <li><a href="../Devolucao/chamadao.htm">Chamadão</a></li>
   <li><a href="../Devolucao/edicoes_chamada.htm">Consulta Encalhe</a></li>
   <li><a href="../Devolucao/help_devolucao.htm">Help</a></li>
</ul></li>
<li><a href="javascript:;" class="trigger"><span class="classNFe">&nbsp;</span>NF-e</a>
    <ul>
	<li><a href="../NFE/tratamento_arquivo_retorno_nfe.htm">Retorno NF-e</a></li>
    <li><a href="../NFE/consulta_nfe_encalhe_tratamento.htm">Entrada NF-e Terceiros</a></li>
    <li><a href="../NFE/geracao_nfe_NFE.htm">Geração NF-e</a></li>
    <li><a href="../NFE/impressao_nfe_NFE.htm">Impressão NF-e</a></li>
    <!--<li><a href="../NFE/cancelamento_nfe.htm">Cancelamento NFE</a></li>-->
    <li><a href="../NFE/painel_monitor_nfe.htm">Painel Monitor NF-e</a></li>
    <li><a href="../NFE/help_nfe.htm">Help</a></li>    
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
  <li><a href="../Financeiro/consulta_boletos_jornaleiros.htm">Consulta Boletos por Cota</a></li>
    <li><a href="../Financeiro/conta_corrente.htm">Conta Corrente</a></li>
    <li><a href="../Financeiro/conta_pagar.htm">Contas a pagar</a></li>
    <li><a href="../Financeiro/historico_inadimplencia.htm">Inadimplência</a></li>
  <li><a href="../Financeiro/consignado_cota.htm">Consulta Consignado</a></li>
  <li><a href="../Financeiro/cadastro_tipo_desconto.htm">Tipo de Desconto Cota</a></li>
  <li><a href="../Financeiro/relatorio_garantias.htm">Relatório de Garantias</a></li>
  <li><a href="../Financeiro/parametros_cobranca.htm">Parâmetros de Cobrança</a></li>
    <li><a href="../Financeiro/help_financeiro.htm">Help</a></li>
</ul>
    
    
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classAdministracao">&nbsp;</span>Administração</a>
    <ul>
	<li><a href="../Administracao/fechar_dia.htm">Fechamento Diário</a></li>
    <li><a href="../Administracao/workflow_aprovacao.htm">Controle Aprovação</a></li>
   <!-- <li><a href="painel_operacional.htm">Painel Operacional</a></li>-->
    <li><a href="../Administracao/painel_processamento.htm">Painel Processamento</a></li>
    <li><a href="../Administracao/fallowup_sistema.htm">Follow Up do Sistema</a></li>
    <li><a href="../Administracao/cadastro_usuario.htm">Grupos de Acesso</a></li>
  <li><a href="../Administracao/cadastro_calendario.htm">Calendário</a></li>
  <!--<li><a href="../Administracao/cadastro_tipos_movimento.htm">Tipo de Movimento</a></li>-->
  <li><a href="../Administracao/faixa_reparte.htm">Faixa de Reparte</a></li>
 <li><a href="../Administracao/gerar_arquivo_jornaleiro.htm">Gerar Arquivo Jornaleiro</a></li>
  <li><a href="../Administracao/cadastro_tipo_nota.htm">Tipos de NF-e</a></li>
       
           <!--<li><a href="../Administracao/cadastro_servico_entrega.htm">Serviço de Entrega</a></li>-->
           
           <li><a href="../Administracao/relatorio_servico_entrega.htm">Relatório de Serviços de Entrega</a></li>
   
   <!--<li><a href="../Administracao/tipos_produtos.htm">Tipos de Produtos</a></li>-->
  <!--<li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Plano de Contas</a></li>-->
  <li><a href="../Administracao/parametros_sistema.htm">Parâmetros de Sistema</a></li>
  <li><a href="../Administracao/parametros_distribuidor.htm">Parâmetros Distribuidor</a></li>
  <!--<li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Histórico do PDV</a></li>-->
    <li><a href="../Administracao/help_administracao.htm">Help</a></li>
</ul></li>
    <li><a href="../help.htm" style="width:14px!important	;"><span class="classHelp">&nbsp;</span></a></li>
</ul>
  	<br class="clearit" />
	</div>
</div>
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Segmentação Não Recebida < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Área de Influência</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="20"><input type="radio" name="pesqPor" id="radio" value="radio" onclick="filtroPorArea();" /></td>
            <td width="208">Área de Influência / Gerador de Fluxo</td>
            <td width="20"><input type="radio" name="pesqPor" id="radio2" value="radio" onclick="filtroPorCota();" /></td>
            <td width="33">Cota</td>
            <td width="643"><table width="642" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorCota" style="display:none;">
              <tr>
           	  <td width="35">Cota:</td>
                <td width="93"><input type="text" name="textfield" id="textfield" style="width:80px;"/></td>
                <td width="36">Nome:</td>
                <td width="348"><input type="text" name="textfield2" id="textfield2" style="width:200px;"/></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="porArea();">Pesquisar</a></span></td>
            </tr>
        </table>
          
          
        </td>
          </tr>
        </table>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorArea" style="display:none;">
          <tr>
            <td width="104">Área de Influência:</td>
            <td width="189"><select name="select2" id="select2" style="width:180px;">
              <option selected="selected">Selecione...</option>
            </select></td>
            <td width="162">Geradores de Fluxo Primário:</td>
            <td width="209"><select name="select" id="select" style="width:180px;">
              <option selected="selected">Selecione...</option>
            </select></td>
            <td width="61">Secundário:</td>
            <td width="194"><select name="select3" id="select3" style="width:180px;">
              <option selected="selected">Selecione...</option>
            </select></td>
          </tr>
          <tr>
            <td colspan="4"><table width="426" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="23"><input type="radio" name="radio" id="radio" value="radio" /></td>
                <td width="99">Todas as Cotas</td>
                <td width="20"><input type="radio" name="radio" id="radio2" value="radio" /></td>
                <td width="284">Cotas Ativas</td>
              </tr>
            </table></td>
            <td>&nbsp;</td>
            <td><span class="bt_pesquisar"><a href="javascript:;" onclick=" porArea();">Pesquisar</a></span></td>
  </tr>
        </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:block;">
      
      <div class="porArea" style="display:none;">
      <fieldset class="classFieldset">
       	  <legend>Área de Influência / Gerador de Fluxos</legend>
        
        	<table class="areaInfluenciaGrid"></table>
            
            
            <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
      
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
      </div>
      
      
      
      
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>

	$(".areaInfluenciaGrid").flexigrid({
			url : '../xml/areaInfluenciaGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo PDV',
				name : 'tipoPdv',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Bairro',
				name : 'bairro',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'cidade',
				width : 80,
				sortable : true,
				align : 'left'
			},  {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Área de Influência',
				name : 'areaInfluencia',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Gerador 1',
				name : 'gerador1',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Gerador 2',
				name : 'gerador2',
				width : 90,
				sortable : true,
				align : 'left'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
</script>
</body>
</html>
