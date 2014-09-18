<head>
<script type="text/javascript" src="scripts/cotaBase.js"></script>
<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
<script language="javascript" type="text/javascript">

	var pesquisaCota = new PesquisaCota();

$(function(){
	cotaBaseController.init();
});
	


</script>

</head>
 
<body>

<div id="cotasBaseHidden" style="display: none;" ></div>

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
            <td width="155">Relan�amento</td>
          </tr>
        </table>

	</fieldset>
    <br clear="all" />
    <fieldset style="width:605px!important; margin-top:10px;">
   		<legend>PDV da Cota</legend>
    	<table class="pdvCotaGrid"></table>
        <table width="600" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="312"><span class="bt_novos" title="Excluir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0" />Excluir</a></span>
</td>
    <td width="174">&nbsp;</td>
    <td width="71" align="center">999.999</td>
    <td width="43">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3" align="right">Selecionar Todos&nbsp;</td>
    <td><input name="input" type="checkbox" value="" /></td>
  </tr>
  <tr>
    <td colspan="3" align="right">Manter Fixa&nbsp; </td>
    <td><input name="input2" type="checkbox" value="" /></td>
  </tr>
</table>
	</fieldset>

</div>
<div id="dialog-novo-equivale" title="Nova Cota Base" style="display:none;">
  <fieldset style="width:605px!important;">
   		<legend>Dados da Cota</legend>
    	<table width="500" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="42"><strong>Cota:</strong></td>
            <td width="92"><input type="text" name="textfield8" id="textfield8" style="width:60px;"/></td>
            <td width="44"><strong>Nome:</strong></td>
            <td width="155"><input type="text" name="textfield9" id="textfield9" style="width:130px;"/></td>
            <td width="151"><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
          </tr>
        </table>

	</fieldset>
    <br clear="all" />
    <fieldset style="width:605px!important; margin-top:10px;">
   		<legend>Cota Base Cadastrada</legend>
    	<table class="novaEquivalenteGrid"></table>
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
	</fieldset>

</div>
<div id="dialog-detail" title="Detalhes" style="display:none;">
  <fieldset style="width:880px!important;">
   		<legend>Dados da Cota</legend>
    	<table width="500" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="43"><strong>Cota:</strong></td>
            <td width="93"><div id="numeroCotaDetalhe"></div> </td>
            <td width="45"><strong>Nome:</strong></td>
            <td width="306"><div id="nomeCotaDetalhe"></div> </td>
          </tr>
        </table>
	</fieldset>
    <br clear="all" />
    <fieldset style="width:880px!important; margin-top:10px;">
   		<legend>Nome: <div id="nomeCotaDetalhe"></div></legend>
    	<table class="consultaEquivalentesDetalheGrid" id="consultaEquivalentesDetalheGrid" ></table>
	</fieldset>
	<div id="botoesImprimirDoPopUpDetalhe">
		<span class="bt_novos" title="Gerar Arquivo">
				<a href="${pageContext.request.contextPath}/cadastro/cotaBase/exportar?fileType=XLS&tipoDeLista=pesquisaDetalhes">
				    <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
				</a>
		</span>
		<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/cadastro/cotaBase/exportar?fileType=PDF&tipoDeLista=pesquisaDetalhes">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir
				</a>
		</span>
	</div>
</div>

<div id="dialog-segmentos" title="Segmentos não recebidos" style="display:none;">
    <fieldset style="width:250px!important; margin-top:10px;">
   		<legend>Segmentos</legend>
    	<table class="consultaSegmentosGrid" id="consultaSegmentosGrid" ></table>
        
	</fieldset>

</div>


<div id="dialog-foto-pdv" title="Foto PDV" style="display:none;">
  <fieldset style="width:630px!important;">
   	<legend>Nome: <span id="idNomePdv"></span></legend>
    	<img src="${pageContext.request.contextPath}/images/pdv/no_image.jpeg" id="idImagem" name="idImagem" width="630" height="400" />
	</fieldset>
</div>

<div id="dialog-cancelar" title="Cancelar Peso" style="display:none;">
	<p>Confirma o Cancelamento deste Peso?</p>
</div>

<div id="dialog-confirm" title="Confirmar Informe de Peso">
	<p>Confirma o Peso Atribuído?</p>
</div>
<div id="dialog-excluir" title="Excluir Cota">
	<p>Confirma a exclusão desta Cota?</p>
</div>
<div id="dialog-peso" title="Novo Peso">

    <fieldset class="classSegmento" style="width:600px!important; margin-top:10px; display:block;">
    	<legend>Ajuste por Segmento</legend>
        <table class="rankSegmentoGrid"></table>
    </fieldset>
    
   
</div>



<div class="corpo">
  
  
    <br clear="all"/>
    <br />
   
    <div class="container">
    
    <form id="formDePesquisa" >	
	     <fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
	  	    <legend> Pesquisar Cotas</legend>
	       <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	           <tr>
	          	   <td width="28">Cota:</td>
	               <td width="46">
	               	<input type="text" name="idCota" id="idCota" style="width:40px;" onchange="cotaBaseController.pesquisarPorNumeroCota('#idCota', '#nomeCota');" />
	               </td>
	               <td width="36">Nome:</td>
	               <td>
	               	<input type="text" name="nomeCota" id="nomeCota" disabled="disabled" style="width:130px;"/>
	               </td>
	               <td>Tipo PDV:</td>
	               <td><input type="text" name="tipoPDV" id="tipoPDV" disabled="disabled" style="width:110px;"/></td>
	               <td width="46">Bairro:</td>
	               <td width="91"><input type="text" name="bairro" id="bairro" disabled="disabled" style="width:90px;"/></td>
	               <td width="41">Cidade:</td>
	               <td colspan="2"><input type="text" name="cidade" id="cidade" disabled="disabled" style="width:100px;"/></td>
	               <td colspan="2">Dias Restantes:</td>
	               <td width="105"><input name="diasRestantes" type="text" id="diasRestantes" style="width:40px; text-align:center;" disabled="disabled"/></td>
	           </tr>
	           <tr>
	             <td colspan="3">Gerador de Fluxo: </td>
	             <td width="139"><input type="text" name="geradorFluxo" id="geradorFluxo" disabled="disabled" style="width:130px;" /></td>
	             <td width="65">Área Influencia: </td>
	             <td width="117"><input type="text" name="areaInfluencia" id="areaInfluencia" disabled="disabled" style="width:110px;"/></td>
	             <td>Período:</td>
	             <td><input type="text" name="periodoDe" disabled="disabled" id="periodoDe" style="width:60px;"/></td>
	             <td>Até:
	             </td>
	             <td width="85"><input type="text" name="periodoAte" id="periodoAte" disabled="disabled" style="width:60px;"/></td>
	             <td width="20"><input type="checkbox" name="isGeral" id="isGeral"/></td>
	             <td width="63">Pesquisa Geral?</td>
	             <td colspan="2">
	             
	                 <span class="bt_pesquisar pesqNormal"><a href="javascript:;" onclick="cotaBaseController.clickPesquisar();">Pesquisar</a></span>
	             </td>
	           </tr>
	         </table>
	     </fieldset>
      </form>
      <div class="linha_separa_fields">&nbsp;</div>
      
        <div class="grids" style="display:block;">
        	<div class="pesqGeralGrid" style="display:none;">
	            <fieldset class="classFieldset">
	                <legend>Cotas Base Cadastradas</legend>
	                <table class="consultaEquivalentesGrid" id="consultaEquivalentesGrid"></table>
	                <!--<span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_novoEquivalente();"><img src="../images/ico_salvar.gif" hspace="5" border="0" />Novo</a></span>-->
	                
	                <span class="bt_novos" title="Gerar Arquivo">
	                	<a href="${pageContext.request.contextPath}/cadastro/cotaBase/exportar?fileType=XLS&tipoDeLista=pesquisaGeral">
	                		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
	                </span>
	                
	                <span class="bt_novos" title="Imprimir">
	                	<a href="${pageContext.request.contextPath}/cadastro/cotaBase/exportar?fileType=PDF&tipoDeLista=pesquisaGeral">
	                		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
	                </span>
	            
	            </fieldset> 	
        	</div>
        <div class="pesqCotasGrid" style="display:none;">
        	<fieldset class="classFieldset">
                <legend>Cotas Base Cadastradas</legend>
        		<table class="cotasEquivalentesGrid" id="cotasEquivalentesGrid"></table>
            
            <span style="float:left; margin-top:5px; margin-bottom:5px; margin-left:10px;">
            	<strong>Informe o Indíce de Ajuste do Histórico:</strong> 
            	<input name="indiceAjuste" id="indiceAjuste" type="text" style="width:40px; text-align:center;" />
            </span>
            <br clear="all" />

			<!--<span class="bt_novos" title="Informar Peso"><a href="javascript:;" onclick="informarPeso();"><img src="../images/bt_cadastros.png" hspace="5" border="0" />Peso por Segmento</a></span>-->
            
            <span class="bt_novos" title="Confirmar">
            	<a href="javascript:;" onclick="cotaBaseController.confirmarPeso();">
            		<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
            	Confirmar</a>
            </span>

			<span class="bt_novos" title="Cancelar">
				<a href="javascript:;" onclick="cotaBaseController.cancelarPeso();">
					<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0" />
					Cancelar
				</a>
			</span>
            
            <span class="bt_novos" title="Histórico">
            	<a href="javascript:;" onclick="cotaBaseController.mostrarHistorico();">
            		<img src="${pageContext.request.contextPath}/images/ico_boletos.gif" hspace="5" border="0" />
            		Histórico
            	</a>
            </span>
        
      </fieldset>
      </div>
     </div>     
     <div class="linha_separa_fields">&nbsp;</div>
	     <div class="historicoGrid" id="historicoGrid" style="display:none;">
		     <fieldset class="classFieldset">
		       	  <legend>Cotas Base Cadastradas</legend>
		        	<div class="grids" style="display:block;">
			        	<table class="cotasEquivalentesBGrid" id="cotasEquivalentesBGrid"></table>
			            
						<span class="bt_novos" title="Voltar">
							<a href="javascript:;" onclick="cotaBaseController.botaoVoltarHistoricio();">
								<img src="${pageContext.request.contextPath}/images/seta_voltar.gif" hspace="5" border="0" />Voltar</a>
						</span>
			            
			            <span class="bt_novos" title="Gerar Arquivo">
			            	<a href="${pageContext.request.contextPath}/cadastro/cotaBase/exportar?fileType=XLS&tipoDeLista=pesquisaHistorico">
			            		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
			            </span>
			
						<span class="bt_novos" title="Imprimir">
							<a href="${pageContext.request.contextPath}/cadastro/cotaBase/exportar?fileType=PDF&tipoDeLista=pesquisaHistorico">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
						</span>
		
		        	</div>
		        
		      </fieldset>
	      </div>
	      
	  <div class="linha_separa_fields">&nbsp;</div>      
    </div>
</div>
</body>

