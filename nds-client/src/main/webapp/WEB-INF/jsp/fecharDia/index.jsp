<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/fecharDia.js"></script>
<script language="javascript" type="text/javascript">

	/*
	/dialog-encalhe-sobras
	/dialog-encalhe-faltas
	/dialog-estoque-lancto
	/dialog-estoque-recolto
	*/
	
	$(function() {
		fecharDiaController.init();		
	});		
	
</script>
<style type="text/css">
.linha_separa_fields{width:400px!important;}
</style>
</head>

<body>
	<form id="form-popup">
		<div id="dialog-novo" title="Fechar o Dia">
			<p>Confirma os Valores?</p>
		</div>
	</form>

	<form id="form-boletos-baixados">
		<div id="dialog-boletos-baixados" title="Boletos Baixados" style="display:none;">
			<fieldset style="width:750px!important;">
		    	<legend>Boletos Baixados - Vencimento: 10/05/2012</legend>
		    	<table class="boletoBaixadoGrid"></table>
		        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		        <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
		        
		    </fieldset>
		</div>
	</form>
	
	<form id="form-popup-validacao-recebimento-fisico">
		<div id="dialog-recebe-fisico" title="Recebimento F�sico" style="display:none;">
		<fieldset style="width:350px;">
	    	<legend>Recebimento F�sico</legend>
	        <table class="recebeFisicoGrid"></table>
	    </fieldset>
	    <br clear="all" />
	    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
	
		<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		
		</div>
	</form>
	
	<form id="form-popup-validacao-confirmacao-expedicao">
		<div id="dialog-confirma-expedicao" title="Confirmação de Expedição" style="display:none;">
		<fieldset style="width:650px;">
	    	<legend>Confirmar Expedição</legend>
	        <table class="confirmaExpedicaoGrid"></table>
	        
	    </fieldset>
	    <br clear="all" />
	    
		</div>
	</form>
	
	<form id="form-popup-validacao-lancamento-faltas-sobras" ></form>
		<div id="dialog-lancto-faltas-sobras" title="Lançamento de Faltas e Sobras" style="display:none;">
		<fieldset style="width:500px;">
	    	<legend>Lançamento de Faltas e Sobras</legend>
		        <table class="lctoFaltasSobrasGrid"></table>
		        <br clear="all" />
		        <p>Diferenças não direcionadas, caso não seja direcionadas serão transferidas para o estoque Ganhos/Perdas!</p>
				<p>Deseja realizar o direcionamento?
		        <br clear="all" />
		        <span class="bt_novos"><a href="../Estoque/lancamento_faltas_sobras.htm"><img src="../images/ico_check.gif" hspace="5" border="0" />Sim</a></span>
		        
		        <span class="bt_novos"><a href="javascript:;" onclick="popup_transferencias();"><img src="../images/ico_excluir.gif" hspace="5" border="0" />Não</a></span>
		
				</p>
	    </fieldset>
	    <br clear="all" />
	
	
	</div>
	</form>

	<form id="form-cota-grid">
		<div id="dialog-cota-grid" title="Cotas" style="display:none;">
			<fieldset style="width:330px;">
		    	<legend>Cotas</legend>
		        <table class="popCotasGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		
		<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		
		</div>
	</form>

	<form id="form-venda-total">
		<div id="dialog-venda-total" title="Movimento" style="display:none;">
			<fieldset style="width:850px;">
		    	<legend>Vendas</legend>
		        <table class="vendasDialogGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		
		<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		
		</div>
	</form>

	<form id="form-suplementares">
		<div id="dialog-suplementares" title="Movimento" style="display:none;">
			<fieldset style="width:850px;">
		    	<legend>Suplementar</legend>
		        <table class="suplementarDialogGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		
		<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		
		</div>
	</form>

	<form id="form-recolhimentos">
		<div id="dialog-recolhimentos" title="Movimento" style="display:none;">
			<fieldset style="width:850px;">
		    	<legend>Recolhimento</legend>
		        <table class="recolhimentoDialogGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		
		<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
	</form>

</div>

	<form id="form-repartes">
		<div id="dialog-repartes" title="Movimento" style="display:none;">
			<fieldset style="width:850px;">
		    	<legend>Lan�amento</legend>
		        <table class="reparteDialogGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		
			<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		
		</div>
	</form>

<div id="dialog-estoque-recolto" title="Estoque" style="display:none;">
	<fieldset style="width:700px;">
    	<legend>Recolhimento</legend>
        <table class="estoque-recoltoGrid"></table>
    </fieldset>
    <br clear="all" />
    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

</div>


<div id="dialog-estoque-lancto" title="Estoque" style="display:none;">
	<fieldset style="width:700px;">
    	<legend>Lançamento</legend>
        <table class="estoque-lanctoGrid"></table>
    </fieldset>
    <br clear="all" />
    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

</div>

	<form id="form-fisico-faltas">
		<div id="dialog-fisico-faltas" title="Físico" style="display:none;">
			<fieldset style="width:700px;">
		    	<legend>Faltas</legend>
		        <table class="fisico-faltasGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
		
		<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		
		</div>
	</form>

<div id="dialog-encalhe-faltas" title="Encalhe" style="display:none;">
	<fieldset style="width:700px;">
    	<legend>Faltas</legend>
        <table class="encalhe-faltasGrid"></table>
    </fieldset>
    <br clear="all" />
    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

</div>

<div id="dialog-encalhe-sobras" title="Encalhe" style="display:none;">
	<fieldset style="width:700px;">
    	<legend>Sobras</legend>
        <table class="encalhe-sobrasGrid"></table>
    </fieldset>
    <br clear="all" />
    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

</div>

<div id="dialog-fisico-sobras" title="Físico" style="display:none;">
	<fieldset style="width:700px;">
    	<legend>Sobras</legend>
        <table class="fisico-sobrasGrid"></table>
    </fieldset>
    <br clear="all" />
    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

</div>

	<form id="form-processos">
		<div id="dialog-processos" title="Status Processos" style="display:none;">
		  <fieldset style="width:260px;">
		    	<legend>Status dos Processos</legend>
		      <table width="260" border="0" cellspacing="1" cellpadding="1" id="tabela-validacao">
		        <tr class="header_table">
		          <td width="205">Processo</td>
		          <td width="48" align="center">Status</td>
		        </tr>		        		        
		        <tr class="class_linha_1">
		          <td>Fechamento de Encalhe:</td>
		          <td align="center"><img src="../images/ico_bloquear.gif" width="16" height="16" alt="Com Diferença" /></td>
		        </tr>		       
		        <tr class="class_linha_1">
		          <td>Manutenção Status da Cota:</td>
		          <td align="center"><img src="../images/ico_check.gif" alt="Processo Efetuado" width="16" height="16" /></td>
		        </tr>
		        <tr class="class_linha_2">
		          <td>Ajuste de Comissão Jornaleiros:</td>
		          <td align="center"><img src="../images/ico_check.gif" alt="Processo Efetuado" width="16" height="16" /></td>
		        </tr>
		      </table>
		    </fieldset>
		</div>
	</form>

<div class="corpo">   
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Confirmação de Valores < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Fechar o Dia</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="104">Data da Operação:</td>
              <td width="111">${dataOperacao}</td>
              <td width="185">
              	<span class="bt_novos" title="Iniciar Fechamento do Dia">
              		<a href="javascript:;" onclick="fecharDiaController.popup_processos();">
              		<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/bt_devolucao.png">Iniciar Fechamento do Dia</a>
              	</span>
              </td>
              <td width="529"><span class="bt_confirmar_novo grids" style="display:none;" title="Confirmar"><a onclick="fecharDiaController.popup();" href="javascript:;"><img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="../images/ico_check.gif">Confirmar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
      <fieldset class="classFieldset">
       	  <legend> Confirmação de Valores em: ${dataOperacao}</legend>
          
          <table width="950" border="0" align="center" cellpadding="0" cellspacing="0" style="border:1px solid #000;">
  <tr>
    <td height="26" align="center" bgcolor="#F4F4F4"><strong><a href="javascript:;" onclick="fecharDiaController.popup_repartes();">REPARTE</a></strong></td>
    <td align="center" bgcolor="#F4F4F4" style="width:10px; border-left:1px solid #ccc;">&nbsp;</td>
    <td align="center" bgcolor="#F4F4F4"><strong><a href="javascript:;" onclick="fecharDiaController.popup_recolhimento();">ENCALHE</a></strong></td>
    <td align="center" bgcolor="#F4F4F4" style="width:10px; border-left:1px solid #ccc;">&nbsp;</td>
    <td align="center" bgcolor="#F4F4F4"><strong><a href="javascript:;" onclick="fecharDiaController.popup_suplementar();">SUPLEMENTAR</a></strong></td>
    </tr>
  <tr>
    <td valign="top"><table border="0" cellspacing="1" cellpadding="2" style="margin-left:10px; margin-right:10px;">
      <tr>
        <td align="left">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
      <tr>
        <td width="109" align="left" style="border-bottom:1px solid #ccc;">(+) Reparte R$</td>
        <td width="103" align="right" style="border-bottom:1px solid #ccc;"><div id="totalReparte"></div> </td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">(+) Sobras</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalSobras"></div></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">(-) Faltas</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalFaltas"></div></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">(+) Transferência</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalTransferencia"></div></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">(=) A Distribuir</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalADistribuir"></div></td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Distribuido</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalDistribuido"></div></td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Sobra Dist.</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalSobraDistribuido"></div></td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Diferença</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalDiferenca"></div></td>
      </tr>
      <tr>
        <td align="left">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
    </table></td>
    <td align="center" valign="top" style="width:10px; border-left:1px solid #ccc;">&nbsp;</td>
    <td align="center" valign="top"><table border="0" cellspacing="1" cellpadding="2" style="margin-left:10px; margin-right:10px;">
      <tr>
        <td align="left">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Lógico</td>
        <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
      </tr>
      <tr>
        <td width="130" align="left" style="border-bottom:1px solid #ccc;">Físico</td>
        <td width="62" align="right" style="border-bottom:1px solid #ccc;">71.826,79</td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Juramentado</td>
        <td align="right" style="border-bottom:1px solid #ccc;">71.789,79</td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Venda</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><a href="javascript:;" onclick="fecharDiaController.popup_vendasTot();">10.000,00</a></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Sobras em</td>
        <td align="right" style="border-bottom:1px solid #ccc;">37,00</td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Faltas em</td>
        <td align="right" style="border-bottom:1px solid #ccc;">37,00</td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Saldo</td>
        <td align="right" style="border-bottom:1px solid #ccc;">482,84</td>
      </tr>
      <tr>
        <td align="left">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
    </table></td>
    <td align="center" valign="top" style="width:10px; border-left:1px solid #ccc;">&nbsp;</td>
    <td align="center" valign="top"><table border="0" cellspacing="1" cellpadding="2" style="margin-left:10px; margin-right:10px;">
      <tr>
        <td align="left">&nbsp;</td>
        <td align="right">&nbsp;</td>
        </tr>
      <tr>
        <td width="109" align="left" style="border-bottom:1px solid #ccc;">Estoque Lógico</td>
        <td width="103" align="right" style="border-bottom:1px solid #ccc;">505,47</td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Transferências</td>
        <td align="right" style="border-bottom:1px solid #ccc;">542,35</td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Vendas</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><a href="javascript:;" onclick="popup_vendasTot();">10.000,00</a></td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Saldo</td>
        <td align="right" style="border-bottom:1px solid #ccc;">83,90</td>
        </tr>
      <tr>
        <td align="left">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
    </table></td>
    </tr>
</table>
          <span  style="margin-left:7px; margin-top:5px; margin-bottom:5px; float:left; clear:right;">&nbsp;</span><br clear="all"/>

          <table width="950" border="0" align="center" cellpadding="0" cellspacing="0" style="border:1px solid #000;">
            <tr>
              <td height="26" align="center" bgcolor="#F4F4F4"><strong><a href="javascript:;" onclick="fecharDiaController.popup_boletos_baixados();">DÍVIDAS A RECEBER</a></strong></td>
              <td align="center" bgcolor="#F4F4F4" style="width:10px; border-left:1px solid #ccc;">&nbsp;</td>
              <td align="center" bgcolor="#F4F4F4"><strong><a href="javascript:;" onclick="fecharDiaController.popup_boletos_baixados();">DÍVIDAS A VENCER</a></strong></td>
            </tr>
            <tr>
              <td valign="top"><table width="381" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
                <tr>
                  <td align="left">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                </tr>
                <tr class="header_table">
                  <td align="left">Forma de Pagamento</td>
                  <td align="right">Total R$</td>
                  <td align="right">Valor Pago</td>
                  <td align="right">Inadimplência</td>
                </tr>
                <tr>
                  <td width="120" align="left" style="border-bottom:1px solid #ccc;">Boleto</td>
                  <td width="80" align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td width="80" align="right" style="border-bottom:1px solid #ccc;">135.371,10</td>
                  <td width="80" align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Boleto em Branco</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">83,90</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Cheque</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">143,62</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Dinheiro</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">0,00</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Depósito</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">135.311,38</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Transferência</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">135.227,48</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Outros</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">83,90</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                </tr>
              </table></td>
              <td align="center" valign="top" style="width:10px; border-left:1px solid #ccc;">&nbsp;</td>
              <td align="center" valign="top"><table width="381" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
                <tr>
                  <td align="left">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                </tr>
                <tr class="header_table">
                  <td align="left">Forma de Pagamento</td>
                  <td align="right">Total R$</td>
                  <td align="right">Valor Pago</td>
                  <td align="right">Inadimplência</td>
                </tr>
                <tr>
                  <td width="120" align="left" style="border-bottom:1px solid #ccc;">Boleto</td>
                  <td width="80" align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td width="80" align="right" style="border-bottom:1px solid #ccc;">135.371,10</td>
                  <td width="80" align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Boleto em Branco</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">83,90</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Cheque</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">143,62</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Dinheiro</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">0,00</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Depósito</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">135.311,38</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Transferência</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">135.227,48</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left" style="border-bottom:1px solid #ccc;">Outros</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">83,90</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">&nbsp;</td>
                </tr>
                <tr>
                  <td align="left">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                </tr>
              </table></td>
            </tr>
        </table>
          <span  style="margin-left:7px; margin-top:5px; margin-bottom:5px; float:left; clear:right;">&nbsp;</span><br clear="all"/>

          <table width="950" border="0" align="center" cellpadding="0" cellspacing="0" style="border:1px solid #000;">
            <tr>
              <td width="950" height="26" align="center" bgcolor="#F4F4F4"><strong>COTAS</strong></td>
            </tr>
            <tr>
              <td valign="top"><table width="910" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
                <tr>
                  <td align="left">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                </tr>
                <tr class="header_table">
                  <td align="center">Total</td>
                  <td align="center">Ativas</td>
                  <td align="center">Ausentes - Reparte</td>
                  <td align="center">Ausentes - Encalhe</td>
                  <td align="center">Novos</td>
                  <td align="center">Inativas</td>
                </tr>
                <tr>
                  <td width="222" align="center" style="border-bottom:1px solid #ccc;">100</td>
                  <td width="153" align="center" style="border-bottom:1px solid #ccc;">90</td>
                  <td width="158" align="center" style="border-bottom:1px solid #ccc;"><a href="javascript:;" onclick="fecharDiaController.popup_cotasGrid();">05</a></td>
                  <td width="183" align="center" style="border-bottom:1px solid #ccc;"><a href="javascript:;" onclick="fecharDiaController.popup_cotasGrid();">05</a></td>
                  <td width="183" align="center" style="border-bottom:1px solid #ccc;"><a href="javascript:;" onclick="fecharDiaController.popup_cotasGrid();">05</a></td>
                  <td width="188" align="center" style="border-bottom:1px solid #ccc;"><a href="javascript:;" onclick="fecharDiaController.popup_cotasGrid();">00</a></td>
                </tr>
                <tr>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                </tr>
              </table></td>
            </tr>
        </table>

<span  style="margin-left:7px; margin-top:5px; margin-bottom:5px; float:left; clear:right;">&nbsp;</span><br clear="all"/>

          <table width="950" border="0" align="center" cellpadding="0" cellspacing="0" style="border:1px solid #000;">
            <tr>
              <td width="950" height="26" align="center" bgcolor="#F4F4F4"><strong>ESTOQUE</strong></td>
            </tr>
            <tr>
              <td valign="top"><table width="910" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
                <tr>
                  <td align="left">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                </tr>
                <tr class="header_table">
                  <td>&nbsp;</td>
                  <td align="center">Lançamento</td>
                  <td align="center">Juramentada</td>
                  <td align="center">Suplementar</td>
                  <td align="center">Recolhimento</td>
                  <td align="center">Danificados</td>
                </tr>
                <tr>
                  <td width="129" style="border-bottom:1px solid #ccc;">Produto</td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;">90</td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;">00</td>
                </tr>
                <tr>
                  <td style="border-bottom:1px solid #ccc;">Exemplar</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">90</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">00</td>
                </tr>
                <tr>
                  <td style="border-bottom:1px solid #ccc;">Valor R$</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">90</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">05</td>
                  <td align="center" style="border-bottom:1px solid #ccc;">00</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                </tr>
              </table></td>
            </tr>
        </table>
        
        
<span  style="margin-left:7px; margin-top:5px; margin-bottom:5px; float:left; clear:right;">&nbsp;</span><br clear="all"/>

          <table width="950" border="0" align="center" cellpadding="0" cellspacing="0" style="border:1px solid #000;">
            <tr>
              <td width="950" height="26" align="center" bgcolor="#F4F4F4"><strong>CONSIGNADO</strong></td>
            </tr>
            <tr>
              <td valign="top"><table width="910" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
                <tr>
                  <td align="left">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                </tr>
                <tr class="header_table">
                  <td>&nbsp;</td>
                  <td align="right">Saldo Anterior  R$</td>
                  <td align="right">Entradas  R$</td>
                  <td align="right">Saídas  R$</td>
                  <td align="right">Saldo Atual  R$</td>
                </tr>
                <tr>
                  <td width="164" style="border-bottom:1px solid #ccc;">Consignado</td>
                  <td width="180" align="right" style="border-bottom:1px solid #ccc;">2.386.172,32</td>
                  <td width="180" align="right" style="border-bottom:1px solid #ccc;">120.661,32</td>
                  <td width="180" align="right" style="border-bottom:1px solid #ccc;">84.249,48</td>
                  <td width="180" align="right" style="border-bottom:1px solid #ccc;">2.872.584,16</td>
                </tr>
                <tr>
                  <td style="border-bottom:1px solid #ccc;">A Vista</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">474.641,28</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">15.187,80</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">8.649,53</td>
                  <td align="right" style="border-bottom:1px solid #ccc;">481.179,55</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                  <td align="center">&nbsp;</td>
                </tr>
              </table></td>
            </tr>
        </table>
<br />
          <br />



        <br clear="all" />
          <a href="relatorio_geral_financeiro_fisico_1.htm" target="_blank">rel_1</a> |  <a href="relatorio_geral_financeiro_fisico_2.htm" target="_blank">rel_2</a> |  <a href="relatorio_geral_financeiro_fisico_3.htm" target="_blank">rel_3</a><br />
          <br clear="all" />
        <br />
	<span class="bt_confirmar_novo" title="Confirmar"><a onclick="fecharDiaController.popup();" href="javascript:;"><img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="../images/ico_check.gif">Confirmar</a></span>
          
          
          <br clear="all" />
          
	
        
      </fieldset>
      	</div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script language="javascript" type="text/javascript">
$(".boletoBaixadoGrid").flexigrid({
			url : '../xml/boletos_baixadosB-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 40,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'banco',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Conta-Corrente',
				name : 'cCorrente',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nosso Número',
				name : 'nossoNumero',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'vlr',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt. Vencto',
				name : 'dtVencto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Forma Pgto',
				name : 'formaPgto',
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
			width : 750,
			height : 220
		});
$(".popCotasGrid").flexigrid({
			url : '../xml/popCotasGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 225,
				sortable : true,
				align : 'left'
			}],
			width : 330,
			height : 200
		});
$(".vendasDialogGrid").flexigrid({
			url : '../xml/vendasDialogGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt. Rclto',
				name : 'dtRecolto',
				width : 90,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 850,
			height : 255
		});

	
	
	$(".suplementarDialogGrid").flexigrid({
			url : '../xml/suplementarDialogGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Qtde Contabil',
				name : 'qtdeContabil',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde Fisico',
				name : 'qtdeFisico',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dif.',
				name : 'dif',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 850,
			height : 255
		});
		
	$(".recolhimentoDialogGrid").flexigrid({
			url : '../xml/recolhimentoDialogGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Encalhe',
				name : 'vendaEncalhe',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'dif',
				width : 70,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 850,
			height : 255
		});
	$(".reparteDialogGrid").flexigrid({
			url : '../xml/reparteDialogGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Sobra em',
				name : 'sobraEm',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Falta em',
				name : 'faltaEm',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Transf.',
				name : 'transf',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'A Distr',
				name : 'aDistr',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Distribuido',
				name : 'distribuido',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Sobra Distr',
				name : 'sobraDistri',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dif.',
				name : 'dif',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 850,
			height : 255
		});
	
	$(".estoque-recoltoGrid").flexigrid({
			url : '../xml/estoque_recolto-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Saldo Anterior',
				name : 'saldoAnterior',
				width : 155,
				sortable : true,
				align : 'center'
			}, {
				display : 'Entradas',
				name : 'entradas',
				width : 155,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saídas',
				name : 'saida',
				width : 155,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saldo Atual',
				name : 'saldoAtual',
				width : 155,
				sortable : true,
				align : 'center'
			}],
			sortname : "saldoAnterior",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 700,
			height : 255
		});
	
	$(".estoque-lanctoGrid").flexigrid({
			url : '../xml/estoque_lancto-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Saldo Anterior',
				name : 'saldoAnterior',
				width : 155,
				sortable : true,
				align : 'center'
			}, {
				display : 'Entradas',
				name : 'entradas',
				width : 155,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saídas',
				name : 'saida',
				width : 155,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saldo Atual',
				name : 'saldoAtual',
				width : 155,
				sortable : true,
				align : 'center'
			}],
			sortname : "saldoAnterior",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 700,
			height : 255
		});
	$(".encalhe-faltasGrid").flexigrid({
			url : '../xml/encalhe_sobras-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Sobra',
				name : 'sobra',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo',
				name : 'tipo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Destino',
				name : 'destino',
				width : 115,
				sortable : true,
				align : 'left'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 700,
			height : 255
		});
	
	$(".encalhe-sobrasGrid").flexigrid({
			url : '../xml/encalhe_sobras-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Sobra',
				name : 'sobra',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo',
				name : 'tipo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Destino',
				name : 'destino',
				width : 115,
				sortable : true,
				align : 'left'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 700,
			height : 255
		});
	
	$(".fisico-faltasGrid").flexigrid({
			url : '../xml/fisico_faltas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Falta',
				name : 'falta',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo',
				name : 'tipo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Destino',
				name : 'destino',
				width : 115,
				sortable : true,
				align : 'left'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 700,
			height : 255
		});
	
	$(".fisico-sobrasGrid").flexigrid({
			url : '../xml/fisico_sobras-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Sobra',
				name : 'sobra',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo',
				name : 'tipo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Destino',
				name : 'destino',
				width : 115,
				sortable : true,
				align : 'left'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 700,
			height : 255
		});
</script>
</body>
</html>
