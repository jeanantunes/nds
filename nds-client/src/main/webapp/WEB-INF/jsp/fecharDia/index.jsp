<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
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

	<form id="form-dividas-receber">
		<div id="dialog-dividas-receber" title="Dívidas a Receber" style="display:none;">
			<fieldset style="width:750px!important;">
		    	<legend>Dívidas a Receber</legend>
		    	
                <table class="dividasReceberGrid"></table>
		      
                <span class="bt_novos" title="Gerar Arquivo">
                  <a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarDividasReceber?fileType=XLS" title="Arquivo">
                    <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
                    Arquivo
                  </a>
                </span>
		        <span class="bt_novos" title="Imprimir">
                  <a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarDividasReceber?fileType=PDF" title="Imprimir">
                    <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
                    Imprimir
                  </a>
                 </span>
		    </fieldset>
		</div>
	</form>
  
    <form id="form-dividas-vencer">
      <div id="dialog-dividas-vencer" title="Dívidas a Vencer" style="display:none;">
      <fieldset style="width:750px!important;">
          <legend>Dívidas a Vencer</legend>
          <table class="dividasVencerGrid"></table>
            <span class="bt_novos" title="Gerar Arquivo">
              <a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarDividasVencer?fileType=XLS" title="Arquivo">
                <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
                Arquivo
              </a>
            </span>
            
            <span class="bt_novos" title="Imprimir">
              <a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarDividasVencer?fileType=PDF" title="Imprimir">
                <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
                Imprimir
              </a>
            </span>
            
        </fieldset>
    </div>
    </form>
	
	<form id="form-popup-validacao-recebimento-fisico">
		<div id="dialog-recebe-fisico" title="Recebimento Físico" style="display:none;">
		<fieldset style="width:350px;">
	    	<legend>Recebimento Físico</legend>
	        <table class="recebeFisicoGrid"></table>
	    </fieldset>
	    <br clear="all" />
	    <span class="bt_novos" title="Gerar Arquivo">
	    	<a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarRecebimentoFisico?fileType=XLS">
	    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	    		Arquivo
	    	</a>
	    </span> 
	    
		<span class="bt_novos" title="Imprimir">
			<a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarRecebimentoFisico?fileType=PDF">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				Imprimir
			</a>
		</span>
		
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
			    
				<table class="cotasGrid"></table>
			      
				<span class="bt_novos" title="Gerar Arquivo">
				  	<a id="lnkExportacaoCotaXLS" href="#" title="Arquivo">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
					</a>
				</span>
				<span class="bt_novos" title="Imprimir">
					<a id="lnkExportacaoCotaPDF" href="#" title="Imprimir">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir
				 	</a>
				</span>
			</fieldset>
		</div>
	</form>

	<form id="form-venda-total">
		<input type="hidden" name="tipoVenda" id="tipoVenda" />
		<div id="dialog-venda-total" title="Movimento" style="display:none;">
			<fieldset style="width:850px;">
		    	<legend>Vendas</legend>
		        <table class="vendasDialogGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo">  
			    <a href="javaScript:;" onclick="fecharDiaController.exportarVendaEncalheOuSuplementar('XLS');">
		    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		    		Arquivo
		    	</a>
	    	</span>
		
			<span class="bt_novos" title="Imprimir">		
			 	<a href="javaScript:;" onclick="fecharDiaController.exportarVendaEncalheOuSuplementar('PDF');">
		    		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
		    		Imprimir
			    </a>		
			</span>
		
		</div>
	</form>

	<form id="form-suplementares">
		<div id="dialog-suplementares" title="Movimento" style="display:none;">
			<fieldset style="width:850px;">
		    	<legend>Suplementar</legend>
		        <table class="suplementarDialogGrid"></table>
		    </fieldset>
		    <br clear="all" />
		     <span class="bt_novos" title="Gerar Arquivo">  
			    <a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarResumoSuplementar?fileType=XLS">
		    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		    		Arquivo
		    	</a>
	    	</span>
		
			<span class="bt_novos" title="Imprimir">		
			 	<a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarResumoSuplementar?fileType=PDF">
		    		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
		    		Imprimir
			    </a>		
			</span>
		
		</div>
	</form>

	<form id="form-recolhimentos">
		<div id="dialog-recolhimentos" title="Movimento" style="display:none;">
			<fieldset style="width:850px;">
		    	<legend>Recolhimento</legend>
		        <table class="recolhimentoDialogGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo">
			    <a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarResumoEncalhe?fileType=XLS">
			    	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
			    	Arquivo
			    </a>
		    </span>
		
			<span class="bt_novos" title="Imprimir">
			
			<a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarResumoEncalhe?fileType=PDF">
		    	<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
		    	Imprimir
			</a>
			
			</span>
	</form>

</div>

	<form id="form-repartes">
		<div id="dialog-repartes" title="Movimento" style="display:none;">
			<fieldset style="width:850px;">
		    	<legend>Lançamento</legend>
		        <table class="reparteDialogGrid"></table>
		    </fieldset>
		    <br clear="all" />
		    <span class="bt_novos" title="Gerar Arquivo">
		    	<a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarResumoReparte?fileType=XLS">
		    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
		    		Arquivo
		    	</a>		    
		    </span>
		
			<span class="bt_novos" title="Imprimir">
			
			<a href="${pageContext.request.contextPath}/administracao/fecharDia/exportarResumoReparte?fileType=PDF">
		    	<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
		    	Arquivo
		    </a>
			
			</span>
		
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
		      </table>
		    </fieldset>
		</div>
	</form>

<div class="corpo" style="overflow-y: auto;">   
    <br clear="all"/>
    <br />
   
    <div class="container">
    	
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
              <td width="529">
              		<span class="bt_confirmar_novo grids" style="display:none;" title="Confirmar">
              			<a onclick="fecharDiaController.popup();" href="javascript:;">
              			<img width="16" border="0" hspace="5" height="16" alt="Confirmar" src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar</a>
              	</span>
             </td>
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
    <td align="center" bgcolor="#F4F4F4"><strong><a href="javascript:;" onclick="fecharDiaController.popup_encalhe();">ENCALHE</a></strong></td>
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
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalEncalheLogico"></div></td>
      </tr>
      <tr>
        <td width="130" align="left" style="border-bottom:1px solid #ccc;">Físico</td>
        <td width="62" align="right" style="border-bottom:1px solid #ccc;"><div id="totalEncalheFisico"></div></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Juramentado</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalEncalheJuramentada"></div></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Venda</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><a href="javascript:;" onclick="fecharDiaController.popup_vendasTot('encalhe');"><div id="vendaEncalhe"></div></a></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Sobras</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalSobraEncalhe"></div></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Faltas</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalFaltaEncalhe"></div></td>
      </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Saldo</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="saldoEncalhe"></div></td>
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
        <td width="103" align="right" style="border-bottom:1px solid #ccc;"><div id="totalSuplementarEstoqueLogico"></div></td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Transferências</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalSuplementarTransferencia"></div></td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Vendas</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><a href="javascript:;" onclick="fecharDiaController.popup_vendasTot('suplementar');"><div id="totalSuplementarVenda"></div></a></td>
        </tr>
      <tr>
        <td align="left" style="border-bottom:1px solid #ccc;">Saldo</td>
        <td align="right" style="border-bottom:1px solid #ccc;"><div id="totalSuplementarSaldo"></div></td>
        </tr>
      <tr>
        <td align="left">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
    </table></td>
    </tr>
</table>
          <span  style="margin-left:7px; margin-top:5px; margin-bottom:5px; float:left; clear:right;">&nbsp;</span><br clear="all"/>

          <table id="tabela_dividas_receber_vencer" width="950" border="0" align="center" cellpadding="0" cellspacing="0" style="border:1px solid #000;">
            <tr>
              <td height="26" align="center" bgcolor="#F4F4F4"><strong><a href="javascript:;" onclick="fecharDiaController.popup_dividas_receber();">DÍVIDAS A RECEBER</a></strong></td>
              <td align="center" bgcolor="#F4F4F4" style="width:10px; border-left:1px solid #ccc;">&nbsp;</td>
              <td align="center" bgcolor="#F4F4F4"><strong><a href="javascript:;" onclick="fecharDiaController.popup_dividas_vencer();">DÍVIDAS A VENCER</a></strong></td>
            </tr>
            <tr>
              <td valign="top">
              
              <table  id="tabela_dividas_receber" width="381" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
               

              </table></td>
              <td align="center" valign="top" style="width:10px; border-left:1px solid #ccc;">&nbsp;</td>
              <td align="center" valign="top">
              <table id="tabela_dividas_vencer" width="381" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
               
              </table></td>
            </tr>
        </table>
          <span  style="margin-left:7px; margin-top:5px; margin-bottom:5px; float:left; clear:right;">&nbsp;</span><br clear="all"/>

          <table width="950" border="0" align="center" cellpadding="0" cellspacing="0" style="border:1px solid #000;">
            <tr>
              <td width="950" height="26" align="center" bgcolor="#F4F4F4"><strong>COTAS</strong></td>
            </tr>
            <tr>
              <td valign="top">
	              <table id="tabela_cotas" width="910" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
	              </table>
              </td>
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
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;" id="produtolancamento"></td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;" id="produtoJuramentado"></td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;" id="produtoSuplenetar"></td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;" id="produtoRecolhimento"></td>
                  <td width="150" align="center" style="border-bottom:1px solid #ccc;" id="produtoDanificados"></td>
                </tr>
                <tr>
                  <td style="border-bottom:1px solid #ccc;">Exemplar</td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="exemplarlancamento"></td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="exemplarJuramentado"></td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="exemplarSuplenetar"></td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="exemplarRecolhimento"></td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="exemplarDanificados"></td>
                </tr>
                <tr>
                  <td style="border-bottom:1px solid #ccc;">Valor R$</td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="valorlancamento"></td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="valorJuramentado"></td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="valorSuplenetar"></td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="valorRecolhimento"></td>
                  <td align="center" style="border-bottom:1px solid #ccc;" id="valorDanificados"></td>
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
              <td valign="top">
	              <table id="tabela_consignado" width="910" border="0" cellpadding="2" cellspacing="1" style="margin-left:10px; margin-right:10px;">
	              </table>
              </td>
            </tr>
        </table>
<br />
          <br />



        <br clear="all" />
          <a href="relatorio_geral_financeiro_fisico_1.htm" target="_blank">rel_1</a> |  <a href="relatorio_geral_financeiro_fisico_2.htm" target="_blank">rel_2</a> |  <a href="relatorio_geral_financeiro_fisico_3.htm" target="_blank">rel_3</a><br />
          <br clear="all" />
        <br />
	
        <br clear="all" />
          
	
        
      </fieldset>
      	</div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script language="javascript" type="text/javascript">

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
