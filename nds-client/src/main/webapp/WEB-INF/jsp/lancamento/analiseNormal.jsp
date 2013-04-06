<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/NDS.js"></script>
<%-- <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/acessoRapido.js"></script> --%>
<script language="javascript" src="${pageContext.request.contextPath}/scripts/tooltip/jquery.tools.min.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/scripts/tooltip/jquery.tipsy.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/tooltip/tipsy.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/tooltip/tipsy-docs.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/analiseNormal.js"></script>
<script language="javascript" type="text/javascript">

function popup_cotas_estudo() {
		$( "#dialog-cotas-estudos" ).dialog({
			resizable: false,
			height:530,
			width:550,
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

function popup_cotas_detalhes() {
		$( "#dialog-cotas-detalhes" ).dialog({
			resizable: false,
			height:500,
			width:740,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			}
		});
	};
	
function popup_detalhes() {
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false,
		});
	};
function popup_detalhes_close() {
	  $( "#dialog-detalhes" ).dialog( "close" );
	  
	  
	  }
$(function() {   
    $('.legendas').tipsy({gravity: $.fn.tipsy.autoNS});
  });

function mostraDados(){
	$('.detalhesDados').show();
	}
function escondeDados(){
	$('.detalhesDados').hide();
	}
	
function definicaoReparte(){
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$( "#dialog-defineReparte" ).dialog({
		resizable: false,
		height:590,
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
</script>
   


<style>
.gridScroll tr:hover{background:#FFC}
.analiseRel tbody{height:100px; overflow:auto;}
.analiseRel tr:hover{background:#FFC;}
.class_tpdv{width:55px;}
.class_novaCota{width:32px;}
.class_cota{width:40px;}
.class_nome{width:90px;}
.class_npdv{width:30px;}
.class_media{width:35px; color:#F00; font-weight:bold;}
.class_vlrs{width:35px;}
.class_vda{width:35px; color:#F00; font-weight:bold;}
.detalhesDados{position:absolute; display:none; background:#fff; border:1px solid #ccc; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); }
</style>

</head>

<body>
<div id="dialog-defineReparte" title="Nova Cota Base" style="display:none;">
  <fieldset style="width:605px!important;">
   		<legend>Dados da Cota</legend>
    	<table width="500" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="42"><strong>Cota:</strong></td>
            <td width="92">1223</td>
            <td width="44"><strong>Nome:</strong></td>
            <td width="155">Antonio José da Silva</td>
            <td width="151">&nbsp;</td>
          </tr>
        </table>

	</fieldset>
    <br clear="all" />
    <fieldset style="width:605px!important; margin-top:10px;">
   		<legend>Dados do Produto</legend>
    	<table width="500" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="42"><strong>Código:</strong></td>
            <td width="92">0564</td>
            <td width="44"><strong>Produto:</strong></td>
            <td width="155">Tauros</td>
            <td width="44"><strong>Classificação:</strong></td>
            <td width="155">Relançamento</td>
          </tr>
        </table>

	</fieldset>
    <br clear="all" />
    <fieldset style="width:605px!important; margin-top:10px;">
   		<legend>PDV da Cota</legend>
    	<table class="pdvCotaGrid"></table>
        <table width="600" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="312">&nbsp;
</td>
    <td width="174">&nbsp;</td>
    <td width="71" align="center">999.999</td>
    <td width="43">&nbsp;</td>
  </tr>
    <tr>
    <td colspan="3" align="right">Manter Fixa&nbsp; </td>
    <td><input name="input2" type="checkbox" value="" /></td>
  </tr>
</table>
	</fieldset>

</div>
<div id="dialog-cotas-detalhes" title="Pontos de Vendas" style="display:none;">  
  
  	<fieldset style="width:690px!important; margin-top:5px;">
    	<legend>Cotas Cadastradas</legend>
    	<table class="cotasDetalhesGrid"></table>
    </fieldset>
    
  <fieldset style="width:690px!important; margin-top:5px;">
  <legend>Cota</legend>
        
    <table width="686" border="0" cellpadding="2" cellspacing="1">
        <tr>
          <td width="50"><strong>Cota:</strong></td>
          <td width="43">9623</td>
          <td width="78"><strong>Nome:</strong></td>
          <td width="289">Book News Liv e Revistaria Ltd EPP</td>
          <td width="58"><strong>Tipo:</strong></td>
          <td width="137">Alterantivo</td>
        </tr>
        <tr>
          <td><strong>Ranking:</strong></td>
          <td>10</td>
          <td><strong>Faturamento:</strong></td>
          <td>58.383,67</td>
          <td><strong>Mês/Ano:</strong></td>
          <td>09/2012</td>
        </tr>
    </table>

</fieldset>

<fieldset style="width:690px!important; margin-top:5px;">
  <legend>MIX</legend>
        
    <table width="686" border="0" cellpadding="2" cellspacing="1">
        <tr>
          <td width="48"><strong>Código:</strong></td>
          <td width="52">${estudo.produtoEdicao.produto.codigo}</td>
          <td width="54"><strong>Produto:</strong></td>
          <td width="511">${estudo.produtoEdicao.nomeComercial}</td>
        </tr>
      </table>
    <table width="686" border="0" cellpadding="2" cellspacing="1">
      <tr>
        <td width="55"><strong>Rep.Mín.:</strong></td>
        <td width="41">05</td>
        <td width="60"><strong>Rep. Máx.:</strong></td>
        <td width="79">9.999</td>
        <td width="49"><strong>Usuário:</strong></td>
        <td width="141">Rodrigue</td>
        <td width="105"><strong>Data Manutenção.:</strong></td>
        <td width="115">28/03/2012 10:56</td>
      </tr>
  </table>
</fieldset>
</div>


<div id="dialog-detalhes" title="Visualizando Produto">
	<img src="${pageContext.request.contextPath}/capas/revista-nautica-11.jpg" width="235" height="314" />
</div>
<div id="dialog-edicoes-produtos" title="Pesquisar Edições de Produto (Ainda n�o funcionando)" style="display:none;">  
    <fieldset style="width:500px!important; margin-top:10px;">
    	<legend>Edições do Produto</legend>
        <table class="edicaoProdCadastradosGrid"></table>
     </fieldset>
</div>

<div id="dialog-cotas-estudos" title="Cotas que não entraram no estudo" style="display:none;">  
	<fieldset style="width:500px!important;">
    <legend>Pesquisar Cota</legend>
        
      <table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="63">Cota:</td>
            <td width="161"><input type="text" name="textfield" id="textfield" style="width:60px;" /></td>
            <td width="46">Nome:</td>
            <td width="209"><input type="text" name="textfield2" id="textfield2" style="width:185px;" /></td>
          </tr>
          <tr>
            <td>Motivo:</td>
            <td colspan="3"><select name="select" id="select" style="width:408px">
              <option selected="selected">Todas as Cotas</option>
            </select></td>
          </tr>
        </table>

    </fieldset>
     <fieldset style="width:500px!important; margin-top:10px;">
    	<legend>Componentes</legend>
        <table width="503" border="0" cellspacing="1" cellpadding="1">
                   <tr>
                   	 <td width="67">Componente:</td>
                     <td width="185"><select name="select" id="select" style="width:170px;">
                     </select></td>
                     <td width="52">Elemento:</td>
                     <td width="186"><select name="select2" id="select2" style="width:170px;">
                     </select></td>
                   </tr>
                 </table>
        
        
     </fieldset>
    <fieldset style="width:500px!important; margin-top:10px;">
    	<legend>Cotas que não entraram no estudo</legend>
        <table class="cotasEstudoGrid"></table>
        <div style="float:right; margin-top:5px; margin-right:60px;"><strong>Saldo:</strong> 999</div>
     </fieldset>
</div>



<div id="dialog-mudar-base" title="Mudar Base de Visualização" style="display:none;">
	
  <fieldset style="width:500px!important;">
    <legend>Base Produto</legend>
        
      <table width="500" border="0" cellpadding="2" cellspacing="1">
          <tr>
            <td width="45"><strong>Estudo:</strong></td>
            <td width="76">${estudo.id}</td>
            <td width="43"><strong>Código:</strong></td>
            <td width="42">${estudo.produtoEdicao.produto.codigo}</td>
            <td width="47"><strong>Produto:</strong></td>
            <td width="117">${estudo.produtoEdicao.produto.nome}</td>
            <td width="40"><strong>Edição:</strong></td>
            <td width="49">${estudo.produtoEdicao.numeroEdicao}</td>
          </tr>
        </table>
        <table width="500" border="0" cellpadding="2" cellspacing="1" >
  <tr>
    <td width="104"><strong>Chamada de Capa:</strong></td>
    <td width="385">${estudo.produtoEdicao.chamadaCapa}</td>
  </tr>
</table>

    </fieldset>
    
    <fieldset style="width:500px!important; margin-top:10px;">
    	<legend>Produtos Cadastrados</legend>
        <table class="prodCadastradosGrid"></table>
     </fieldset>
</div>







<div class="corpo">
  <div class="header">
  	<div class="sub-header">
    	<div class="logo"><img src="${pageContext.request.contextPath}/images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></div>
        
        <div class="titAplicacao">
        	<h1>Treelog S/A. Logística e Distribuição - SP</h1>

			<h2>CNPJ: 00.000.000/00001-00</h2>
            <h3>Distrib vs.1</h3>
        </div>
        
        <div class="usuario"><a href="${pageContext.request.contextPath}/login.htm"><img src="${pageContext.request.contextPath}/images/bt_sair.jpg" alt="Sair do Sistema" title="Sair do Sistema" width="63" height="27" border="0" align="right" /></a>
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
    <li><a href="${pageContext.request.contextPath}/index.htm"><span class="classHome">&nbsp;</span>Home</a>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classCadastros">&nbsp;</span>Cadastro</a>
        <ul>
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_produtos.htm">Produto</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_edicao.htm">Edição</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_cotas.htm">Cota</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/jornaleiros_equivalentes.htm">Cotas Base</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_fiador.htm">Fiador</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_entregador.htm">Entregador</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_transportador.htm">Transportador</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_fornecedor.htm">Fornecedor</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/roteirizacao.htm">Roteirização</a></li>
            
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_box.htm">Box</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/cadastro_bancos.htm">Banco</a></li>
            <li><a href="${pageContext.request.contextPath}/Cadastro/alteracao_cotas.htm">Alteração / Cota</a></li>
            <!--<li><a href="${pageContext.request.contextPath}/Cadastro/consulta_geral_equivalentes.htm">Consulta Geral Equivalentes</a></li>-->
            <li><a href="${pageContext.request.contextPath}/Cadastro/help_cadastros.htm">Help</a></li>
</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classLancamento">&nbsp;</span>Lançamento</a>
        <ul>
	<!--<li class="criando"><a href="javascript:;"  onclick="alert('Serviço em construção.');">Conectividade</a></li>-->
    <li><a href="balanceamento_da_matriz.htm">Balanceamento da Matriz</a></li>
  <li><a href="furo_publicacao.htm">Furo de Lançamento</a></li>
  <li><a href="cadastro_parciais.htm">Parciais</a></li>
  <li><a href="relatorio_vendas.htm">Relatório de Vendas</a></li>
  <li><a href="venda_produto.htm">Venda por Produto</a></li>
  <li><a href="relatorio_tipos_produtos.htm">Relatório Tipos de Produtos</a></li>
    <li><a href="help_lancamento.htm">Help</a></li>
</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classDistribuicao">&nbsp;</span>Distribuição</a>
        <ul>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/matriz_distribuicao.htm">Matriz de Distribuição</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/analise_estudo.htm">Análise de Estudos</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/mix_produto.htm">Mix de Produto</a></li>
    <li ><a href="${pageContext.request.contextPath}/Distribuicao/fixacao.htm">Fixação de Reparte</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/classificacao_nao_recebida.htm">Classificação Não Recebida</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/segmento_nao_recebido.htm">Segmento Não Recebido</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/tratamento_excessao.htm">Exceções de Segmentos e Parciais</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/ajustes_reparte.htm">Ajustes Reparte</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/engloba_desengloba.htm">Desenglobação</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/histograma.htm">Histograma de Venda</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/historico_venda.htm">Histórico de Venda</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/regiao.htm">Região</a></li><li><a href="Distribuicao/area_influencia.htm">Área de Influência/Gerador de Fluxo</a></li><li><a href="${pageContext.request.contextPath}/Distribuicao/informacoes_produtos.htm">Informações do Produto</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/caracteristica_distribuicao.htm">Caracteristicas de Distribuição</a></li>
    <li><a href="${pageContext.request.contextPath}/Distribuicao/help_distribuicao.htm">Help</a></li>
</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classEstoque">&nbsp;</span>Estoque</a>
        <ul>
    <li><a href="${pageContext.request.contextPath}/Estoque/recebimento_fisico.htm">Recebimento Fisico</a></li>
    <li><a href="${pageContext.request.contextPath}/Estoque/lancamento_faltas_sobras.htm">Lançamento  Faltas e Sobras</a></li>
<!--
    <li><a href="${pageContext.request.contextPath}/Estoque/relatorio_faltas_sobras.htm">Relatório Faltas e Sobras</a></li>-->
    <!--<li><a href="${pageContext.request.contextPath}/Estoque/ajuste_estoque.htm">Ajuste Estoque - Inventário</a></li>-->
    <li><a href="${pageContext.request.contextPath}/Estoque/consulta_notas_sem_fisico.htm">Consulta Notas</a></li>
    <li><a href="${pageContext.request.contextPath}/Estoque/consulta_faltas_sobras.htm">Consulta Faltas e Sobras</a></li>
    <li><a href="${pageContext.request.contextPath}/Estoque/extrato_edicao.htm">Extrato de Edição</a></li>
    <li><a href="${pageContext.request.contextPath}/Estoque/visao_estoque.htm">Visão do Estoque</a></li>
    <li><a href="${pageContext.request.contextPath}/Estoque/edicoes_fechadas.htm">Edições Fechadas com Saldo</a></li>
    <li><a href="${pageContext.request.contextPath}/Estoque/help_estoque.htm">Help</a></li>
</ul>
    </li>
    
    <li><a href="javascript:;" class="trigger"><span class="classExpedicao">&nbsp;</span>Expedição</a>
        <ul>
    <li><a href="${pageContext.request.contextPath}/Expedicao/interface_picking.htm">Interface Picking</a></li>
  <li><a href="${pageContext.request.contextPath}/Expedicao/mapa_abastecimento.htm">Mapa Abastecimento</a></li>
  <li><a href="${pageContext.request.contextPath}/Expedicao/confirma_expedicao.htm">Confirma Expedição</a></li>
    <li><a href="${pageContext.request.contextPath}/Expedicao/jornaleiro_ausente.htm">Cota Ausente - Reparte</a></li>
    <li><a href="${pageContext.request.contextPath}/Expedicao/geracao_nfe.htm">Nota de Envio</a></li>
    <li><a href="${pageContext.request.contextPath}/Expedicao/resumo_expedicao_nota.htm">Resumo de Expedição</a></li>
    <li><a href="${pageContext.request.contextPath}/Expedicao/romaneios.htm">Romaneios</a></li>
    <li><a href="${pageContext.request.contextPath}/Expedicao/help_expedicao.htm">Help</a></li>
</ul>  
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classDevolucao">&nbsp;</span>Devolução</a>
    <ul>
	<li><a href="${pageContext.request.contextPath}/Devolucao/balanceamento_da_matriz_recolhimento.htm">Balanceamento da Matriz</a></li>
    <li><a href="${pageContext.request.contextPath}/Devolucao/consulta_informe_encalhe.htm">Informe Recolhimento</a></li>
    <li><a href="${pageContext.request.contextPath}/Devolucao/ce_antecipada.htm">CE Antecipada - Produto</a></li>
    <li><a href="${pageContext.request.contextPath}/Devolucao/emissao_ce.htm">Emissão CE</a></li>
    <li><a href="${pageContext.request.contextPath}/Devolucao/conferencia_encalhe_jornaleiro.htm">Conferência de Encalhe</a></li>
    <li><a href="${pageContext.request.contextPath}/Devolucao/venda_encalhe.htm">Venda de Encalhe / Suplementar</a></li>
  <li><a href="${pageContext.request.contextPath}/Devolucao/fechamento_fisico_logico.htm">Fechamento Encalhe</a></li>
    <li><a href="${pageContext.request.contextPath}/Devolucao/fechamento_ce_integracao.htm">Fechamento CE - Integração</a></li>
  <li><a href="${pageContext.request.contextPath}/Devolucao/devolucao_fornecedor.htm">Devolução ao Fornecedor</a></li>
   <!--<li><a href="${pageContext.request.contextPath}/Devolucao/digitacao_contagem_devolucao.htm">Devolução Fornecedor</a></li>-->
  <li><a href="${pageContext.request.contextPath}/Devolucao/emissao_bandeira.htm">Emissão das Bandeiras</a></li>
   <li><a href="${pageContext.request.contextPath}/Devolucao/chamadao.htm">Chamadão</a></li>
   <li><a href="${pageContext.request.contextPath}/Devolucao/edicoes_chamada.htm">Consulta Encalhe</a></li>
   <li><a href="${pageContext.request.contextPath}/Devolucao/help_devolucao.htm">Help</a></li>
</ul></li>
<li><a href="javascript:;" class="trigger"><span class="classNFe">&nbsp;</span>NF-e</a>
    <ul>
	<li><a href="${pageContext.request.contextPath}/NFE/tratamento_arquivo_retorno_nfe.htm">Retorno NF-e</a></li>
    <li><a href="${pageContext.request.contextPath}/NFE/consulta_nfe_encalhe_tratamento.htm">Entrada NF-e Terceiros</a></li>
    <li><a href="${pageContext.request.contextPath}/NFE/geracao_nfe_NFE.htm">Geração NF-e</a></li>
    <li><a href="${pageContext.request.contextPath}/NFE/impressao_nfe_NFE.htm">Impressão NF-e</a></li>
    <!--<li><a href="${pageContext.request.contextPath}/NFE/cancelamento_nfe.htm">Cancelamento NFE</a></li>-->
    <li><a href="${pageContext.request.contextPath}/NFE/painel_monitor_nfe.htm">Painel Monitor NF-e</a></li>
    <li><a href="${pageContext.request.contextPath}/NFE/help_nfe.htm">Help</a></li>    
</ul>
    
    
    </li>
    
    
<li><a href="javascript:;" class="trigger"><span class="classFinanceiro">&nbsp;</span>Financeiro</a>
    <ul>
    <li><a href="${pageContext.request.contextPath}/Financeiro/baixa_bancaria.htm">Baixa Financeira</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/negociar_divida.htm">Negociação de Divida</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/debitos_creditos.htm">Débitos / Créditos Cota</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/impressao_boletos.htm">Impressão de Boletos</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/cadastro_manutencao_status.htm">Manutenção de Status Cota</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/suspensao_jornaleiro.htm">Suspensão  Cota</a></li>
  <li><a href="${pageContext.request.contextPath}/Financeiro/consulta_boletos_jornaleiros.htm">Consulta Boletos por Cota</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/conta_corrente.htm">Conta Corrente</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/conta_pagar.htm">Contas a pagar</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/historico_inadimplencia.htm">Inadimplência</a></li>
  <li><a href="${pageContext.request.contextPath}/Financeiro/consignado_cota.htm">Consulta Consignado</a></li>
  <li><a href="${pageContext.request.contextPath}/Financeiro/cadastro_tipo_desconto.htm">Tipo de Desconto Cota</a></li>
  <li><a href="${pageContext.request.contextPath}/Financeiro/relatorio_garantias.htm">Relatório de Garantias</a></li>
  <li><a href="${pageContext.request.contextPath}/Financeiro/parametros_cobranca.htm">Parâmetros de Cobrança</a></li>
    <li><a href="${pageContext.request.contextPath}/Financeiro/help_financeiro.htm">Help</a></li>
</ul>
    
    
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classAdministracao">&nbsp;</span>Administração</a>
    <ul>
	<li><a href="${pageContext.request.contextPath}/Administracao/fechar_dia.htm">Fechamento Diário</a></li>
    <li><a href="${pageContext.request.contextPath}/Administracao/workflow_aprovacao.htm">Controle Aprovação</a></li>
   <!-- <li><a href="painel_operacional.htm">Painel Operacional</a></li>-->
    <li><a href="${pageContext.request.contextPath}/Administracao/painel_processamento.htm">Painel Processamento</a></li>
    <li><a href="${pageContext.request.contextPath}/Administracao/fallowup_sistema.htm">Follow Up do Sistema</a></li>
    <li><a href="${pageContext.request.contextPath}/Administracao/cadastro_usuario.htm">Grupos de Acesso</a></li>
  <li><a href="${pageContext.request.contextPath}/Administracao/cadastro_calendario.htm">Calendário</a></li>
  <!--<li><a href="${pageContext.request.contextPath}/Administracao/cadastro_tipos_movimento.htm">Tipo de Movimento</a></li>-->
  <li><a href="${pageContext.request.contextPath}/Administracao/faixa_reparte.htm">Faixa de Reparte</a></li>
 <li><a href="${pageContext.request.contextPath}/Administracao/gerar_arquivo_jornaleiro.htm">Gerar Arquivo Jornaleiro</a></li>
  <li><a href="${pageContext.request.contextPath}/Administracao/cadastro_tipo_nota.htm">Tipos de NF-e</a></li>
       
           <!--<li><a href="${pageContext.request.contextPath}/Administracao/cadastro_servico_entrega.htm">Serviço de Entrega</a></li>-->
           
           <li><a href="${pageContext.request.contextPath}/Administracao/relatorio_servico_entrega.htm">Relatório de Serviços de Entrega</a></li>
   
   <!--<li><a href="${pageContext.request.contextPath}/Administracao/tipos_produtos.htm">Tipos de Produtos</a></li>-->
  <!--<li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Plano de Contas</a></li>-->
  <li><a href="${pageContext.request.contextPath}/Administracao/parametros_sistema.htm">Parâmetros de Sistema</a></li>
  <li><a href="${pageContext.request.contextPath}/Administracao/parametros_distribuidor.htm">Parâmetros Distribuidor</a></li>
  <!--<li class="criando"><a href="javascript:;" onclick="alert('Serviço em construção.');">Histórico do PDV</a></li>-->
    <li><a href="${pageContext.request.contextPath}/Administracao/help_administracao.htm">Help</a></li>
</ul></li>
    <li><a href="${pageContext.request.contextPath}/help.htm" style="width:14px!important	;"><span class="classHelp">&nbsp;</span></a></li>
</ul>
  	<br class="clearit" />
	</div>
</div>
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
                <span class="ui-state-default ui-corner-all" style="float:right;">
                <a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a></span>
				<b>Base de Estudo < evento > com < status >.</b></p>
	</div>
    
    	<div class="detalhesDados">
    	  <table width="976" border="0" cellpadding="2" cellspacing="2" class="dadosTab">
    	    <tr>
    	      <td class="class_linha_1"><strong>Edição:</strong></td>
    	      <td class="class_linha_1">0209</td>
    	      <td class="class_linha_1">0208</td>
    	      <td class="class_linha_1">0207</td>
    	      <td class="class_linha_1">0206</td>
    	      <td class="class_linha_1">0205</td>
    	      <td class="class_linha_1">0204</td>
    	      <td class="class_linha_1">0203</td>
    	      <td class="class_linha_1">0202</td>
    	      <td class="class_linha_1">0202</td>
    	      <td><a href="javascript:;" onclick="escondeDados();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" width="15" height="15" border="0" /></a></td>
  	        </tr>
    	    <tr>
    	      <td width="165" class="class_linha_2"><strong>Data Lançamento:</strong></td>
    	      <td width="80" align="center" class="class_linha_2">09/08/2012</td>
    	      <td width="80" align="center" class="class_linha_2">12/07/2012</td>
    	      <td width="80" align="center" class="class_linha_2">18/06/2012</td>
    	      <td width="80" align="center" class="class_linha_2">14/05/2012</td>
    	      <td width="80" align="center" class="class_linha_2">13/04/2012</td>
    	      <td width="80" align="center" class="class_linha_2">12/03/2012</td>
    	      <td width="80" align="center" class="class_linha_2">07/02/2012</td>
    	      <td width="80" align="center" class="class_linha_2">04/01/2012</td>
    	      <td width="80" align="center" class="class_linha_2">04/01/2012</td>
    	      <td width="19" align="center">&nbsp;</td>
  	        </tr>
    	    <tr>
    	      <td class="class_linha_1"><strong>Reparte:</strong></td>
    	      <td align="right" class="class_linha_1">8.588</td>
    	      <td align="right" class="class_linha_1">8.590</td>
    	      <td align="right" class="class_linha_1">8.595</td>
    	      <td align="right" class="class_linha_1">8.590</td>
    	      <td align="right" class="class_linha_1">8.585</td>
    	      <td align="right" class="class_linha_1">7.797</td>
    	      <td align="right" class="class_linha_1">7.797</td>
    	      <td align="right" class="class_linha_1">7.237</td>
    	      <td align="right" class="class_linha_1">6.588</td>
    	      <td align="right">&nbsp;</td>
  	        </tr>
    	    <tr>
    	      <td class="class_linha_2"><strong>Venda:</strong></td>
    	      <td align="right" class="class_linha_2">0</td>
    	      <td align="right" class="class_linha_2">2.587</td>
    	      <td align="right" class="class_linha_2">2.713</td>
    	      <td align="right" class="class_linha_2">3.007</td>
    	      <td align="right" class="class_linha_2">2.691</td>
    	      <td align="right" class="class_linha_2">2.791</td>
    	      <td align="right" class="class_linha_2">2.409</td>
    	      <td align="right" class="class_linha_2">2.109</td>
    	      <td align="right" class="class_linha_2">1.109</td>
    	      <td align="right">&nbsp;</td>
  	        </tr>
  	    </table>
    	</div>
      <fieldset class="classFieldset">
   	    <legend> Pesquisar </legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td>Código:</td>
              <td id="codigoProduto">${estudo.produtoEdicao.produto.codigo}</td>
              <td>Produto:</td>
              <td width="173" id="nomeProduto">${estudo.produtoEdicao.produto.nome}
              </td>
              <td width="77">Edição:</td>
              <td id="numeroEdicao">${estudo.produtoEdicao.numeroEdicao}</td>
              <td>Estudo:</td>
              <td colspan="3" id="estudo_id">${estudo.id}</td>
            </tr>
            <tr>
              <td width="79">Classificação:</td>
                <td width="111">${estudo.produtoEdicao.produto.tipoClassificacaoProduto.descricao}</td>
                <td width="65">Segmento:</td>
                <td>${estudo.produtoEdicao.produto.tipoSegmentoProduto.descricao}</td>
                <td>Ordenar por:</td>
                <td width="169">
<!--                 nao mudar os values dessas options, pois tem coisa na tela e no banco que dependem desses nomes -->
	                <select 
	                name="filterOrder" 
	                id="filterOrder" style="width:150px;">
	                  <option value="0">Selecione:.</option>
	                  <option value="filtroReparte">Reparte</option>
	                  <option value="filtroRanking">Ranking</option>
	                  <option value="filtroPercVenda">% de Venda</option>
	                  <option value="filtroReducao">Redução de Reparte</option>
					</select>
				</td>
                <td width="55">Reparte:</td>
              <td width="53"><input type="text" name="textfield4" id="textfield4" style="width:40px;" /></td>
              <td width="81">Abrangência:</td>
              <td width="36">2%</td>
            </tr>
          </table>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="84">Componente:</td>
            <td width="188">
            	<select id="componentes" name="componentes" style="width:170px;" onchange="javascript:selecionarElementos(this.value)">
	              <option value="null" selected="selected">Selecione...</option>
	              <option value="tipo_ponto_venda">Tipo de Ponto de Venda</option>
	              <option value="gerador_de_fluxo">Gerador de Fluxo</option>
	              <option value="bairro">Bairro</option>
	              <option value="regiao">Região</option>
	              <option value="cotas_a_vista">Cotas A Vista</option>
	              <option value="cotas_novas">Cotas Novas</option>
	              <option value="area_influencia">Área de Influência</option>
	              <option value="distrito">Distrito</option>
	            </select>
	        </td>
            <td width="60">Elemento:</td>
            <td width="179">
            	<select id="elementos" name="elementos" style="width:170px;">
              		<option selected="selected">Selecione...</option>
            	</select>
            </td>
            <td width="271">
            	<span id="filterOrderFields" style="display:none;">
					<span id="filtroReparte" style="display:none;">Reparte</span>
					<span id="filtroReducao" style="display:none;"> % redução</span>
					<span id="filtroRanking" style="display:none;"> Ranking</span>
					<span id="filtroPercVenda" style="display:none;"> % Venda</span>
					de:<input name="filterSortFrom" type="text" style="width:60px;"/>
              		até:<input name="filterSortTo" type="text" style="width:60px;"/>
					<a href="#" id="newSearchByFilterOrder"><img src="${pageContext.request.contextPath}/images/ico_check.gif" alt="Confirmar" border="0" /></a>
				</span>
			</td>
            <td width="35" align="center"><a href="javascript:;" onclick="mostraDados();"><img src="${pageContext.request.contextPath}/images/ico_boletos.gif" title="Exibir Detalhes" width="19" height="15" border="0" /></a></td>
            <td width="97"><span class="bt_novos"><a href="javascript:;" onmouseover="popup_detalhes();" onmouseout="popup_detalhes_close();"><img src="${pageContext.request.contextPath}/images/ico_detalhes.png" alt="Ver Capa" hspace="5" border="0" />Ver Capa</a></span></td>
          </tr>
        </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Base de Estudo / Análise</legend>
        <div class="grids" style="display:block;">
        
        
        
        <table width="950" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="405" align="right"><strong>Edição:</strong></td>
                <td width="78" align="center" class="header_table" id="edicao6">-</td>
                <td width="78" align="center" class="header_table" id="edicao5">-</td>
                <td width="78" align="center" class="header_table" id="edicao4">-</td>
                <td width="78" align="center" class="header_table" id="edicao3">-</td>
                <td width="83" align="center" class="header_table" id="edicao2">-</td>
                <td width="84" align="center" class="header_table" id="edicao1">-</td>
                <td width="16" align="center">&nbsp;</td>
            </tr>
          </table>
        <table class="baseEstudo2Grid"></table>
        

        <table width="940" border="0" cellspacing="2" cellpadding="2">
          <tr class="class_linha_1">
            <td width="60">Qtde Cotas:</td>
            <td width="125" id="qtdDeCotas">0</td>
            <td width="26">&nbsp;</td>
            <td width="56" align="right" id="total_reparte_sugerido">0</td>
            <td width="26" align="right">&nbsp;</td>
            <td width="40" align="right" id="total_media_venda">0</td>
            
            <td width="36" align="right" class="vermelho" id="total_ultimo_reparte">0</td>
            
            <td width="35" align="right" id="total_reparte6">0</td>
            <td width="36" align="right" class="vermelho" id="total_vendas6">0</td>
            <td width="38" align="right" id="total_reparte5">0</td>
            <td width="34" align="right" class="vermelho" id="total_vendas5">0</td>
            <td width="36" align="right" id="total_reparte4">0</td>
            <td width="36" align="right" class="vermelho" id="total_vendas4">0</td>
            <td width="36" align="right" id="total_reparte3">0</td>
            <td width="37" align="right" class="vermelho" id="total_vendas3">0</td>
            <td width="40" align="right" id="total_reparte2">0</td>
            <td width="37"  align="right" class="vermelho" id="total_vendas2">0</td>
            <td width="40" align="right" id="total_reparte1">0</td>
            <td width="37" align="right" class="vermelho" id="total_vendas1">0</td>
             <td width="7">&nbsp;</td>
          </tr>
          </table>
          
		
            
          	<span class="bt_novos" title="Gerar Arquivo">
          		<a href="exportar?fileType=XLS&id=${estudo.id}">
          			<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
          			Arquivo
				</a>
			</span>
			<span class="bt_novos" title="Imprimir">
				<a href="exportar?fileType=PDF&id=${estudo.id}">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					Imprimir
				</a>
			</span>
			<span class="bt_novos">
				<a href="javascript:return false;" id="liberar">
					<img src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" alt="Liberar" hspace="5" border="0" />
					Liberar
				</a>
			</span>
    		<span class="bt_novos">
    			<a href="javascript:history.back(-1);;">
    				<img src="${pageContext.request.contextPath}/images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />
    				Voltar
    			</a>
    		</span>
           <span class="bt_novos"><a href="#" id="botao_mudar_base" ><img src="${pageContext.request.contextPath}/images/ico_atualizar.gif" alt="Mudar Base de Visualização" hspace="5" border="0" />Mudar Base de Visualização</a></span>
			<span class="bt_novos">
				<a href="javascript:;" onclick="popup_cotas_estudo();">
					<img src="${pageContext.request.contextPath}/images/ico_jornaleiro.gif" alt="Cotas que não entraram no Estudo" hspace="5" border="0" />
					Cotas que não entraram no Estudo
				</a>
			</span>
		</div>
      </fieldset>
       </div>
     </div>
     
     
 <script>
 $(function(){
	 analiseNormalController.init("${estudo.id}"); 
 });
 
 
 $(".cotasDetalhesGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/cotasDetalhes-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo',
				name : 'tipo',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : '% Fat.',
				name : 'percFaturamento',
				width : 30,
				sortable : true,
				align : 'right'
			}, {
				display : 'Princ.',
				name : 'principal',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 420,
				sortable : true,
				align : 'left'
			}],
			width : 690,
			height : 200
		});
 
 $(".cotasEstudoGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/cotasEstudo-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Motivo',
				name : 'motivo',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 490,
			height : 200
		});
		
 </script>
 <div style="display:none;" class="assets">
 <span class="acoes">
 <a href='#' class="editarEdicao"><img src='${pageContext.request.contextPath}/images/ico_editar.gif'/></a>
 <a href='#' class="excluirEdicao"><img src='${pageContext.request.contextPath}/images/ico_excluir.gif'/></a>
 </span>
 <span class="setas">
 <a href='#'><img class="arrowUp" src='${pageContext.request.contextPath}/images/seta_sobe.gif'/></a>
 <a href='#'><img class="arrowDown" src='${pageContext.request.contextPath}/images/seta_desce.gif'/></a>
 </span>
 </div>
  </body>
</html>