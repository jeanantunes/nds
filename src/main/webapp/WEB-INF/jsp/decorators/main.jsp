<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="nds" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>NDS - Novo Distrib</title>

		<nds:css/>
		<nds:javascript/>

		<script language="javascript" type="text/javascript">
		$(function() {
					$( "#tabs-cadastros" ).tabs();
					$( "#tabs-pessoas" ).tabs();
				});
		</script>
		<style type="text/css">
		fieldset table{font-size:11px!important;}

		</style>
		<decorator:head />
	</head>

<body>
<div class="corpo">
  <nds:header/>
  <nds:menuSuperior/>
    <div class="containerHome">
      <fieldset>
        	<legend><img src="images/bt_cadastros.png" alt="Cadastros" /> Cadastro</legend>
            <ul>
            	<li><a href="cadastro_bancos.htm">de Bancos</a></li>
                <li><a href="cadastro_box.htm">de Box</a></li>
                <li><a href="cadastro_manutencao_status.htm">de Manutenção de Status</a></li>
                <li><a href="cadastro_parciais.htm">de Parciais</a></li>   
                <li><a href="cadastro_pessoas.htm">de Pessoas</a></li>
                <li><a href="cadastro_tipos_movimento.htm">de Tipos de Movimento</a></li>
                <li><a href="help_cadastros.htm">Help</a></li>
            </ul>
        </fieldset>
    
    
    
    	<fieldset>
        	<legend><img src="images/bt_financeiro.png" alt="Financeiro" /> Financeiro</legend>
            <ul>
                <li><a href="baixa_bancaria.htm">Baixa Bancária</a></li>
                <li><a href="baixa_bancaria_manual.htm">Baixa Bancária Manual</a></li>
                <li><a href="baixa_manual_divida.htm">Baixa Manual de Dívidas</a></li>
                <li><a href="consulta_boletos_jornaleiros.htm">Consulta Boletos por Jornaleiro</a></li>
                <li><a href="conta_corrente.htm">Conta Corrente</a></li>
                <li><a href="debitos_creditos.htm">Débitos / Créditos Jornaleiros</a></li>
                 <li><a href="fechar_dia.htm">Fechar o Dia</a></li>
                 <li><a href="geracao_cobranca.htm">Geração Cobrança</a></li>
                <li><a href="impressao_boletos.htm">Impressão de Boletos</a></li>
                <li><a href="iniciar_dia.htm">Iniciar o Dia</a></li>
                <li><a href="parametros_cobranca.htm">Parâmetros de Cobrança</a></li>
                <li><a href="suspensao_jornaleiro.htm">Suspensão de Jornaleiro</a></li>
                <li><a href="workflow_aprovacao.htm">Work Flow de Aprovação</a></li>
                <li><a href="help_financeiro.htm">Help</a></li>
            </ul>
        
        </fieldset>
        
        <fieldset>
        	<legend><img src="images/bt_estoque.png" alt="Estoque" /> Estoque</legend>
            <ul>  
                <li><a href="consulta_notas_sem_fisico.htm">Consulta de Notas</a></li>
                <li><a href="consulta_faltas_sobras.htm">Consulta Faltas e Sobras</a></li>
                <li><a href="extrato_edicao.htm">Extrato de Edição</a></li>
                <li><a href="lancamento_faltas_sobras.htm">Lançamento de Faltas e Sobras</a></li>
                <li><a href="recebimento_fisico.htm">Recebimento Fisico</a></li>
                <li><a href="help_estoque.htm">Help</a></li>
            </ul>
        
        </fieldset>
        
        <fieldset>
        	<legend><img src="images/bt_lancamento.png" alt="Lançamento" width="15" height="15" /> Lançamento</legend>
            <ul>
                <li><a href="balanceamento_da_matriz.htm">Balanceamento da Matriz</a></li>
                <li><a href="consulta_consignado_jornaleiro.htm">Consulta de Consignado do Jornaleiro</a></li>
                <li><a href="furo_publicacao.htm">Furo de Publicação</a></li>
                <li><a href="help_lancamento.htm">Help</a></li>
            </ul>
        
        </fieldset>
        
        
        
        
		<div class="linha_separa_fields">&nbsp;</div>
        <fieldset>
        	<legend><img src="images/bt_expedicao.png" alt="Expedição" /> Expedição</legend>
            <ul>
                <!--li><a href="alteracao_reparte_jornaleiro.htm">Alteração de Reparte Jornaleiros</a></li-->
                
                <li><a href="confirma_expedicao.htm">Confirma Expedição</a></li>
                <li><a href="consulta_resumos_nfe_geradas_retornadas.htm">Consulta Resumos das NF-e Geradas e Retornadas</a></li>
                <li><a href="geracao_arquivo_nfe.htm">Geração arquivos NF-e</a></li>
                <li><a href="jornaleiro_ausente.htm">Jornaleiro Ausente</a></li>
                <li><a href="painel_monitor_nfe.htm">Painel Monitor NF-e</a></li>
                <li><a href="resumo_expedicao_cd.htm">Resumo de Expedição por CD</a></li>
                <li><a href="resumo_expedicao_nota.htm">Resumo de Expedição por Nota</a></li>
                <li><a href="tratamento_arquivo_retorno_nfe.htm">Tratamento Arquivo Retorno NF-e</a></li>
                <li><a href="help_expedicao.htm">Help</a></li>
            </ul>
        
        </fieldset>
        
        <fieldset>
        	<legend><img src="images/bt_recolhimento.png" alt="Recolhimento" /> Recolhimento</legend>
            <ul>
                <li><a href="balanceamento_da_matriz_recolhimento.htm">Balanceamento da Matriz</a></li>
                <li><a href="cadastro_notas_encalhe_jornaleiro.htm">Cadastro de Notas de Encalhe do Jornaleiro</a></li>
                <li><a href="chamadao.htm">Chamadão</a></li>
                <li><a href="conferencia_encalhe_jornaleiro.htm">Conferência de Encalhe Jornaleiro</a></li>
                <li><a href="conferencia_encalhe_jornaleiro_contingencia.htm">Conferência de Encalhe Jornaleiro Contingência</a></li>
                  <li><a href="consulta_encalhe_jornaleiro.htm">Consulta Encalhe Jornaleiro</a></li>
                 
                 <li><a href="consulta_informe_encalhe.htm">Consulta de Informe de Encalhe</a></li>
                <li><a href="edicoes_chamada.htm">Consulta CE</a></li>
                 
                 <li><a href="liberacao_encalhe_conferido.htm">Liberação do Encalhe Conferido</a></li>
                  <li><a href="vale_brinde.htm">Vale Brinde</a></li>
                  <li><a href="venda_encalhe.htm">Venda de Encalhe</a></li>
                <li><a href="help_recolhimento.htm">Help</a></li>
            </ul>
        
        </fieldset>
        
        <fieldset>
        	<legend><img src="images/bt_devolucao.png" alt="Devolução" /> Devolução</legend>
            <ul>
                <li><a href="digitacao_contagem_devolucao.htm">Digitação de Contagem para Devolução</a></li>
                <li><a href="help_devolucao.htm">Help</a></li>
            </ul>
        
        </fieldset>
        
        <fieldset>
        	<legend><img src="images/bt_administracao.png" alt="Administração" /> Administração</legend>
            <ul>
                <li><a href="help_interfaces.htm">Help</a></li>
            </ul>
        
        </fieldset>
        
    
    <br clear="all" />

    </div>
    <br clear="all" />
	
	Versão 1.0.0			
			<div id="notify" style="display: none;">
			</div>
			<%@include file="../components/error.jsp"%>
			<decorator:body />
</div>

</body>
</html>
