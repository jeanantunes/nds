<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>

<link href="css/menu_superior.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/novo_distrib.css" />
<link rel="stylesheet" type="text/css" href="css/NDS.css" />
<link rel="stylesheet" href="scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" href="scripts/flexigrid-1.1/css/flexigrid.pack.css" />

<script language="javascript" type="text/javascript" src="scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript" src="scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>

<script type="text/javascript">

(function($) {
	$.fn.extend($.ui.tabs.prototype,{
		_original_init : $.ui.tabs.prototype._init,
		_init: function() {
			var self = this.element;
			
			this._original_init();
			this.element.children('.ui-tabs-nav').after('<div class="ui-tabs-strip-spacer"></div>');

			var tabOpt = {
				tabTemplate: "<li><a href='#\{href\}'>#\{label\}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",	
				add: function(event, ui) {		    
					self.tabs('select', '#' + ui.panel.id);
				},
				ajaxOptions: {
					error: function( xhr, status, index, anchor ) {
						$( anchor.hash ).html(
							"pagina não encontrada :</br> Mensagem de Erro: </br>xhr[" + JSON.stringify( xhr )+ "] </br>status[" + xhr.status + "] </br>index [" + index + "] </br>anchor [" + anchor 
						);
					}
				},
				cache: true
			};
			
			$.fn.extend(this.options, tabOpt);
			this.addCloseTab();
		},
		addTab: function(title, url) {
			var self = this.element, o = this.options, add = true;
			$( "li", self ).each(function() {
				if ($("a", this).html() == title) {
					var index = $( "li", self ).index( this );
					self.tabs('select', index);
					add = false;
				}
			});	
			if (add) {
				self.tabs( 'add' , url, title);
			}
		},
		addCloseTab: function() {
			var self = this.element, o = this.options;
			$( "span.ui-icon-close", self ).live( 'click', function() {
				var index = $( "li", $( self ) ).index( $( this ).parent() );
				if (index > -1)
					$( self ).tabs( "remove", index );
			});
		}						
	});
})(jQuery);


$(function() {	
	
	$('#workspace').tabs();	
	
	// Dinamicaly add tabs from menu
	$("#menu_principal ul li ul li").click(function() {
		$( '#workspace' ).tabs('addTab', $("a",this).html(), $("a",this).prop("href") );		
		return false;
	});

});
</script>

<style>
#workspace { margin-top: 0px; }
</style>
</head>
<body>

<div class="corpo">
<div class="header">
  		<div class="sub-header">
    	<div class="logo">&nbsp;</div>
        
          <div class="titAplicacao">
            <h1>Treelog S/A. Logística e Distribuição - SP</h1>
                <h2>CNPJ: 00.000.000/00001-00</h2>
                <h3>Distrib vs.1</h3>
          </div>
        
        	<div class="usuario">
        		<div class="bt_novos">
            
            	<label title="Usuário Logado no Sistema">Usuário: Junior Fonseca</label>
                </div>
                <div class="bt_novos">
                <label>
				<script type="text/javascript" language="JavaScript">
                    diaSemana();
                </script>
                </label>
            </div>
            <div class="bt_novos">
            	<a href="javascript:;" title="Sair do Sistema" class="sair">Sair</a>
            </div>

      </div> 
	</div>
  </div>


<div id="menu_principal">
  <ul>
    <li><a href="index.htm"><span class="classHome">&nbsp;</span>Home</a></li>
    <li><a href="javascript:;" class="trigger"><span class="classCadastros">&nbsp;</span>Cadastro</a>
        <ul>
            <li><a href="produto">Produto</a></li>
            <li><a href="Cadastro/cadastro_edicao.htm">Edição</a></li>
            <li><a href="Cadastro/cadastro_cotas.htm">Cota</a></li>
            <li><a href="Cadastro/cadastro_fiador.htm">Fiador</a></li>
            <li><a href="Cadastro/cadastro_entregador.htm">Entregador</a></li>
            <li><a href="Cadastro/cadastro_transportador.htm">Transportador</a></li>
            <li><a href="Cadastro/cadastro_fornecedor.htm">Fornecedor</a></li>
            <li><a href="Cadastro/roteirizacao.htm">Roteirização</a></li>
            
            <li><a href="Cadastro/cadastro_box.htm">Box</a></li>
            <li><a href="Cadastro/cadastro_bancos.htm">Banco</a></li>
            <li><a href="Cadastro/help_cadastros.htm">Help</a></li>
		</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classLancamento">&nbsp;</span>Lançamento</a>
        <ul>
	<li class="criando"><a href="javascript:alert('Serviço em construção.');">Conectividade</a></li>
    <li><a href="Lancamento/balanceamento_da_matriz.htm">Balanceamento da Matriz</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Cálculo Distribuição</a></li>
  <li><a href="Lancamento/furo_publicacao.htm">Furo de Lançamento</a></li>
  <li><a href="Lancamento/cadastro_parciais.htm">Parciais</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Mix de Produto</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Fixação de Reparte</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Reserva Técnica</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Redistribuição</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Venda Suplementar</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Cotas Suspensas/Novas</a></li>
    <li><a href="Lancamento/help_lancamento.htm">Help</a></li>
</ul>
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classEstoque">&nbsp;</span>Estoque</a>
        <ul>
	<li><a href="Estoque/recebimento_fisico.htm">Recebimento Fisico</a></li>
    <li><a href="Estoque/lancamento_faltas_sobras.htm">Lançamento  Faltas e Sobras</a></li>
    <li class="criando"><a href="javascript:alert('Serviço em construção.');">Estoque</a></li>
 
    <li><a href="Estoque/consulta_notas_sem_fisico.htm">Consulta Notas</a></li>
    <li><a href="Estoque/consulta_faltas_sobras.htm">Consulta Faltas e Sobras</a></li>
    <li><a href="Estoque/extrato_edicao.htm">Extrato de Edição</a></li>
    <li><a href="Estoque/help_estoque.htm">Help</a></li>
</ul>
    </li>
    
    
    
    <li><a href="javascript:;" class="trigger"><span class="classExpedicao">&nbsp;</span>Expedição</a>
        <ul>
    <li class="criando"><a href="javascript:alert('Serviço em construção.');">Interface Picking</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Mapa Abastecimento</a></li>
    <li><a href="Expedicao/jornaleiro_ausente.htm">Cota Ausente</a></li>
    <li><a href="Expedicao/geracao_nfe.htm">Geração NE/NF-e</a></li>
    <li><a href="Expedicao/resumo_expedicao_nota.htm">Resumo de Expedição</a></li>
    <li><a href="Expedicao/help_expedicao.htm">Help</a></li>
</ul>  
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classDevolucao">&nbsp;</span>Devolução</a>
    <ul>
	<li><a href="Devolucao/balanceamento_da_matriz_recolhimento.htm">Balanceamento da Matriz</a></li>
    <li><a href="Devolucao/consulta_informe_encalhe.htm">Informe Recolhimento</a></li>
    <li><a href="Devolucao/ce_antecipada.htm">CE Produto</a></li>
    <li class="criando"><a href="javascript:alert('Serviço em construção.');">Emissão CE</a></li>
    <li><a href="Devolucao/conferencia_encalhe_jornaleiro.htm">Conferência de Encalhe</a></li>
    <li><a href="Devolucao/venda_encalhe.htm">Venda de Encalhe</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Fechamento Encalhe</a></li>
   <li><a href="Devolucao/digitacao_contagem_devolucao.htm">Devolução Fornecedor</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Emissão das Bandeiras</a></li>
   <li><a href="Devolucao/chamadao.htm">Chamadão</a></li>
   <li><a href="Devolucao/edicoes_chamada.htm">Consulta Encalhe</a></li>
   <li><a href="Devolucao/help_devolucao.htm">Help</a></li>
</ul></li>
<li><a href="javascript:;" class="trigger"><span class="classNFe">&nbsp;</span>NF-e</a>
    <ul>
	<li><a href="Expedicao/tratamento_arquivo_retorno_nfe.htm">Retorno NF-e</a></li>
    <li class="criando"><a href="javascript:alert('Serviço em construção.');">Cancelamento NFE</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Cadastro de NCM/CFOP</a></li>
    <li><a href="Expedicao/painel_monitor_nfe.htm">Painel Monitor NF-e</a></li>
    <li><a href="Recolhimento/help_recolhimento.htm">Help</a></li>    
</ul>
    
    
    </li>
    
    
<li><a href="javascript:;" class="trigger"><span class="classFinanceiro">&nbsp;</span>Financeiro</a>
        <ul>
    <li><a href="Financeiro/baixa_bancaria.htm">Baixa Financeira</a></li>
    <li class="criando"><a href="javascript:alert('Serviço em construção.');">Negociação de Divida</a></li>
    <li><a href="Financeiro/debitos_creditos.htm">Débitos / Créditos Cota</a></li>
    <li><a href="Financeiro/impressao_boletos.htm">Impressão de Boletos</a></li>
    <li><a href="Financeiro/cadastro_manutencao_status.htm">Manutenção de Status Cota</a></li>
    <li><a href="Financeiro/suspensao_jornaleiro.htm">Suspensão  Cota</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Mapa Financeiro Diário</a></li>
    <li><a href="Financeiro/consulta_boletos_jornaleiros.htm">Consulta Boletos por Cota</a></li>
    <li><a href="Financeiro/conta_corrente.htm">Conta Corrente</a></li>
    <li><a href="Financeiro/historico_inadimplencia.htm">Inadimplência</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Consigado Cota</a></li>
    <li><a href="Financeiro/help_financeiro.htm">Help</a></li>
</ul>
    
    
    </li>
    <li><a href="javascript:;" class="trigger"><span class="classAdministracao">&nbsp;</span>Administração</a>
    <ul>
	<li class="criando"><a href="javascript:alert('Serviço em construção.');">Fechamento Diário</a></li>
    <li><a href="Administracao/workflow_aprovacao.htm">Controle Aprovação</a></li>
    <li><a href="Administracao/painel_operacional.htm">Painel Operacional</a></li>
    <li class="criando"><a href="javascript:alert('Serviço em construção.');">Usuários</a></li>
    <li class="criando"><a href="javascript:alert('Serviço em construção.');">Grupos de Acesso</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Calendário</a></li>
  <li><a href="Administracao/cadastro_tipos_movimento.htm">Tipo de Movimento</a></li>
   <li class="criando"><a href="javascript:alert('Serviço em construção.');">Tipos de Produtos</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Tipos de Serviços de Entregas</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Plano de Contas</a></li>
  <li class="criando"><a href="Administracao/parametros_cobranca.htm">Parâmetros de Cobrança</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Parâmetros Distribuidor</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Consuslta Cota sem Encalhe</a></li>
  <li class="criando"><a href="javascript:alert('Serviço em construção.');">Histórico do PDV</a></li>
    <li><a href="Administracao/help_administracao.htm">Help</a></li>
</ul></li>
    <li><a href="help.htm"><span class="classHelp">&nbsp;</span>Help</a></li>
</ul>
  <br class="clearit">
</div>


    <div id="workspace"> 
        <ul></ul>
    </div>

</div>

</body>
</html>