<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/NDS.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="scripts/informacoesProduto.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaProduto = new PesquisaProduto();

$(function(){
	informacoesProdutoController.init();
	//var pesquisaCota = new PesquisaCota();
});

</script>

<style type="text/css">
.ui-tabs .ui-tabs-panel {
    padding: 0.45em 0.4em!important;
}
</style>

<div class="areaBts">
  <div class="area">
    <div class='grids' style="display:none;">
       <span class="bt_arq">
              <a href="${pageContext.request.contextPath}/distribuicao/informacoesProduto/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
                <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
              </a>
            </span>
                     
         <span class="bt_arq">
            <a href="${pageContext.request.contextPath}/distribuicao/informacoesProduto/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
              <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
            </a>
         </span>
       </div>
  </div>
</div>
<br clear="all" />
  <br />
<div class="container">
    
      <fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
   	    <legend> Pesquisar Informação do Produto</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="45">Código:</td>
                <td width="123">
                <input type="text" name="idCodigo" id="idCodigo" style="width:100px;"
                       onchange="pesquisaProduto.pesquisarPorCodigoProduto('#idCodigo','#nomeProduto', false, undefined, undefined);"/>
                </td>
                <td width="52">Produto:</td>
                <td width="269">
                <input type="text" name="nomeProduto" id="nomeProduto" style="width:250px;" 
                	   onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#nomeProduto');" 
		 	   		   onblur="pesquisaProduto.pesquisarPorNomeProduto('#idCodigo', '#nomeProduto');"/>
                </td>
                <td width="43">Classificação:</td>
                <td width="276">
                  <select name="select" id="comboClassificacao" style="width:200px;">
                  <option selected="selected">Selecione...</option>
                  		<c:forEach items="${listaClassificacao}" var="classificacao">
								<option value="${classificacao.key}">${classificacao.value}</option>
				  		</c:forEach>
                </select></td>
              
              <td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="informacoesProdutoController.filtroPrincipal();">Pesquisar</a></span></td>
            
            </tr>
          </table>
      </fieldset>

      <div class="linha_separa_fields">&nbsp;</div>
      
      <div class="grids" style="display:none;">
      
      <fieldset class="classFieldset">
       	  <legend>Informações dos Produtos Cadastrados</legend>
        
        	<table class="produtosInfosGrid"></table>
            
           
   		   
      </fieldset>
      </div>
      

      <div class="linha_separa_fields">&nbsp;</div>
    
    </div>
</div> 

<div id="dialog-detalhes" title="Visualizando Produto" style="margin-right:0px!important; float:right!important;">
 <img id="imagemCapaEdicao" width="235" height="314" />
</div>


<div id="dialog-detalhe" title="Detalhes do Produto" style="display:none";>
      <table width="910" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td width="576" valign="top">
   	 
   	  <fieldset style="width:250px; margin-right:0px!important;margin-left:5px !important; float:left; ">
   	    <legend> Abrangência</legend>
                  <table width="240" border="0" cellpadding="2" cellspacing="1">
                      <tr>
                         <td width="52">Previsto:</td>
                        <td width="55"><input type="text" name="dataLcto5" id="sugeridoAbrang" style="width:35px; text-align:left;" /></td>
                       <td width="45">Real:</td>
                        <td width="35"><input type="text" name="dataLcto2" id="apuradaAbrang" style="width:55px; text-align:left;" /></td>
                      </tr>
                </table>
    	</fieldset>
        
      <fieldset style="width:250px; margin-right:0px!important;margin-left:10px !important; float:left; height:47px; ">
               	  <legend>Mínimo</legend>
                  <table width="230" border="0" cellpadding="2" cellspacing="1">
                      <tr>
                        <td width="52">Previsto:</td>
                        <td width="55"><input type="text" name="dataLcto5" id="sugeridoMinimo" style="width:35px; text-align:left;" /></td>
                        <td width="44">Real:</td>
                        <td width="59"><input type="text" name="dataLcto5" id="estudoMinimo" style="width:35px; text-align:left;" /></td>
                      </tr>
                </table>
    	</fieldset>
                
		<br clear="all" />
            
      <fieldset style="width:250px; margin-left:5px;margin-top:5px; margin-right:0px!important; float:left;">
			<legend>Base Sugerida</legend>
            <table class="editorBaseGrid"></table>               
      </fieldset>
                
	  <fieldset style="width:250px; margin-top:5px; margin-left:10px; float:left;  margin-right:0px!important;">
	  		<legend>Base Estudo</legend>
	        <table class="editorBaseApuradaGrid"></table>               
	  </fieldset>
	  <br />
	  <br />

      <fieldset style="width:528px; margin-right:0px!important; margin-top:5px;margin-left:5px !important; ">
       	    <legend>Reparte da edição</legend>
       	    <table width="520" border="0" cellpadding="2" cellspacing="1">
                <tr>
                  <td width="26">Total:</td>
                  <td width="41"><input type="text" name="dataLcto3" id="reparteTotal" style="width:35px; text-align:left;" /></td>
                  <td width="55">Promocional:</td>
                  <td width="41"><input type="text" name="dataLcto4" id="repartePromocional" style="width:35px; text-align:left;" /></td>
                  <td width="54">Distribuido:</td>
                  <td width="35"><input type="text" name="dataLcto6" id="reparteDistribuido" style="width:35px; text-align:left;" /></td>
                  <td width="32">Sobra:</td>
                  <td width="36"><input type="text" name="dataLcto5" id="sobra" style="width:35px; text-align:left;" /></td>
                  <td width="23">Vda:</td>
                  <td width="44"><input type="text" name="dataLcto5" id="venda" style="width:35px; text-align:left;" /></td>
                  <td width="48" align="right">% Vda:</td>
                  <td width="24"><input type="text" name="dataLcto5" id="porcentagemVenda" style="width:35px; text-align:left;" /></td>
              </tr>
            </table>
      </fieldset>


		<fieldset style="width:400px; margin-top:5px; margin-left:10px; float:left;  margin-right:0px!important;">
                    <legend>Bonificações</legend>
                    <table class="itensRegioesEspecificasGrid"></table>
        </fieldset>
    </td>
    
    <td width="327" valign="top">
    	<fieldset style="margin-left:5px !important; width:320px; ">
       	    <legend>Características</legend>
              <table width="320" border="0" cellpadding="2" cellspacing="1">
            <tr>
              <td width="99">Preço Capa R$:</td>
              <td width="210"><input type="text" name="inf-produto-precoCapa" id="inf-produto-precoCapa" style="width:70px; text-align:left;" /></td>
              </tr>
            <tr>
              <td>Pct. Padrão:</td>
              <td><input type="text" name="pctPadrao" id="pctPadrao" style="width:60px; text-align:left;" /></td>
              </tr>
            <tr>

              <td>Chamada Capa:</td>
              <td><input type="text" name="chamadaCapa" id="chamadaCapa" style="width:200px; text-align:left;" /></td>
              </tr>
            <tr>
              <td>Nome Comercial:</td>
              <td><input type="text" name="nomeComercial" id="nomeComercial" style="width:200px; text-align:left;" /></td>
              </tr>
            <tr>
              <td>Boletim Informativo:</td>
              <td><textarea name="boletimInfor" rows="2" id="boletimInfor" style="width:200px; text-align:left;"></textarea></td>
              </tr>
            </table>
   	      </fieldset>
            
          <fieldset style="width:320px; margin-right:0px!important; margin-top:5px; text-align:center; margin-left:5px; height:255px;">
              	<legend>Capa</legend>
              	
                <img id="imagemCapaDetalhe" width="167" height="215" />
                
          </fieldset>
    </td>
  </tr>
</table>
</div>
