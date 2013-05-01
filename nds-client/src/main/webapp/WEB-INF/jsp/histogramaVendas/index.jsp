<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"]></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/NDS.js"></script>
<script  type="text/javascript" src="${pageContext.request.contextPath}/scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/autoCompleteController.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/histogramaVendas.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/analiseHistograma.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="scripts/flexGridService.js"></script>

<script language="javascript" type="text/javascript">

	var pesquisaProduto= new PesquisaProduto(histogramaVendasController.workspace);
	$(function(){
		histogramaVendasController.init();
	});

</script>

<style type="text/css">
.filtroPracaSede, .filtroPracaAtendida, .filtroComponentes{display:none;}
</style>
<style>
.gridScroll tr:hover{background:#FFC}

#outros{display:none;}

</style>


<div id="dialog-detalhes" title="Visualizando Produto" style="margin-right:0px!important; float:right!important;">
	
	<img src="images/loading.gif" id="loadingCapa"/>
	<!-- <img src="capas/revista-nautica-11.jpg" width="235" height="314" id="imagemCapaEdicao" style="display:none"/>  -->
	<img  width="235" height="314" id="imagemCapaEdicao" style="display:none"/>
</div>


<div class="corpo" id="analiseHistoricoVendasContent"/>

<div class="corpo" id="histogramaVendasContent">
 
    <br clear="all"/>
    <br />
   
      <div class="grids" style="display:noneA;">
      
      <form id="pesquisaHistogramaVendas" name="pesquisaHistogramaVendas" method="post">
      <fieldset class="classFieldset">
  <legend> Pesquisar Histograma
  </legend>
  <table width="950" border="0" cellpadding="2" cellspacing="1">
    <tr>
            <td width="20"><input name="filtroPor" type="radio" id="radio" value="" checked="checked" onclick="filtroTodas();" /></td>
            <td width="103"><label for="radio"><strong>Todas as Cotas</strong></label></td>
            <td width="20"><input type="radio" name="filtroPor" id="histogramaVenda_pracaSede" value="0" onclick="filtroSede();"  /></td>
            <td width="70"><label for="radio3"><strong>Praça Sede</strong></label></td>
            <td width="20"><input type="radio" name="filtroPor" id="histogramaVenda_pracaAtendida" value="2" onclick="filtroAtendida();"  /></td>
            <td width="98"><label for="radio4"><strong>Praça Atendida</strong></label></td>
            <td width="20"><input id="inserirComponentes" name="inserirComponentes" type="checkbox" value="checked" onclick="$('.filtroComponentes').toggle();" /></td>
            <td width="558"><table width="552" border="0" cellpadding="2" cellspacing="1">
              <tr>
                <td width="82"><label for="inserirComponentes"><strong>Componentes</strong></label></td>
                <td width="459" colspan="10"><table border="0" cellpadding="2" cellspacing="1" class="filtro filtroComponentes">
                  <tr>
                    <td width="171">
                    <select name="componente" id="componente" style="width:150px;">
                      <option selected="selected" value="-1">Selecione...</option>
                                            
					<c:forEach items="${componenteList}" var="componente" varStatus="idx">
						<option value="${idx.count-1}">${componente.descricao}</option>
					</c:forEach>
										
                    </select></td>
                    <td width="70">Elementos:</td>
                    <td width="150"><select name="elemento" id="elemento" style="width:150px;">
                      <option selected="selected" value="-1">Selecione...</option>
                    </select></td>
                    <td width="26">&nbsp;</td>
                  </tr>
                </table></td>
              </tr>
            </table></td>
          </tr>
        </table>
        <table width="440" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="42">Código:</td>
            <td width="60"><input type="text" name="codigo" id="codigo" style="width:60px;"  maxlength="8"/></td>
            <td width="47">Produto:</td>
            <td width="140"><input type="text" name="produto" id="produto" style="width:140px;" /></td>
            <td width="38">Edição:</td>
            <td width="60"><input type="text" name="edicao" id="edicao" style="width:60px;"/></td>
            <td width="16"><span class="classPesquisar"><a href="javascript:histogramaVendasController.pesquisarFiltro();">&nbsp;</a></span></td>
          </tr>
        </table>	
      </fieldset>
      
     <div class="linha_separa_fields">&nbsp;</div>
</form>
       
       <!--  href="javascript:histogramaVendasController.pesquisarFiltro(); href="/Lancamento/analise_histograma.htm"" -->
    <fieldset class="classFieldset">
    	<legend>Edições do Produto</legend>
        <table class="edicaoProdCadastradosGrid" id="edicaoProdCadastradosGrid"></table>
        <span class="bt_novos" title="Analisar"><a href="javascript:histogramaVendasController.realizarAnalise();"><img src="images/ico_copia_distrib.gif" hspace="5" border="0" />Analisar</a></span>
        
        <span class="bt_novos" title="Cancelar"><a href="javascript:;" onclick="$('.ui-tabs-selected').children('.ui-icon-close').click();"><img src="images/ico_excluir.gif" hspace="5" border="0" />Cancelar</a></span>
     </fieldset>
      

      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 

<div class="corpo" id="analiseHistogramaVendasContent"/>
