<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/NDS.js"></script>
<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="scripts/flexGridService.js"></script>
<script type="text/javascript" src="scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="scripts/historicoVenda.js" ></script>
<script type="text/javascript" src="scripts/autoCompleteCampos.js" ></script>
<script type="text/javascript" src="scripts/utils.js" ></script>
<script language="javascript" type="text/javascript">

$(function() {
  historicoVendaController.init();
});

</script>
<style type="text/css">
.filtroQtdeReparte, .filtroQtdeVenda, .filtroComponentes, .filtroCotas, .filtroPercVenda, .filtroTodasAsCotas{display:none;}
</style>

<div class="areaBts">
  <div class="area">
    <span class="bt_novos">
      <a href="javascript:;" id="analiseHistorico" rel="tipsy" title="Analisar">
        <img src="images/ico_copia_distrib.gif" hspace="5" border="0" />
      </a>
    </span>

  </div>
</div>
<br clear="all" />
<br />

<div id="dialog-detalhes" title="Visualizando Produto" style="margin-right:0px!important; float:right!important;">
  <img id="imagemCapaEdicao" width="235" height="314" />
</div>

<div id="analiseHistoricoContent"/>

<div id="baseAnalise">

  <div class="corpo">

    <div class="container">

      <!--<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all">
      <p>
        <span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span> <b>Segmentação Não Recebida
          < evento >
            com
            < status >.</b>
          </p>
        </div-->
        <div class="grids" >

          <div class="porCota" >
            <div style="float:left; width:510px;">
              <fieldset class="classFieldset fieldFiltroItensNaoBloqueados" style="width:480px!important;">
                <legend>Pesquisar Produto</legend>

                <form id="pesquisaPorProduto">
                  <table width="440" border="0" cellpadding="2" cellspacing="1" class="filtro">
                    <tr>
                      <td width="42">Código:</td>
                      <td width="60">
                        <input type="text" name="filtro.produtoDto.codigoProduto" id="filtroCodigoProduto" style="width:60px;" />
                      </td>
                      <td width="47">Produto:</td>
                      <td width="140">
                        <input type="text" name="filtro.produtoDto.nomeProduto" id="filtroNomeProduto" style="width:140px;" />
                      </td>
                    </tr>
                    <tr>
                      <td width="38">Edição:</td>
                      <td width="40">
                        <input type="text" name="filtro.numeroEdicao" id="filtroNumeroEdicao" style="width:60px;"/>
                      </td>
                      <td width="76">Classificação:</td>
                      <td width="50">
                        <select name="tipoClassificacaoProdutoId" id="comboClassificacao" style="width:200px;">
                          <option selected="selected">Selecione...</option>
                          <c:forEach items="${listaClassificacao}" var="classificacao">
                            <option value="${classificacao.key}">${classificacao.value}</option>
                          </c:forEach>
                        </select>
                      </td>
                      <td width="16">
                        <span class="classPesquisar">
                          <a href="javascript:;" id="pesquisaFiltroProduto"></a>
                        </span>
                      </td>
                    </tr>
                  </table>
                </form>
              </fieldset>

              <fieldset class="classFieldset" style="width:480px!important; margin-top:10px!important;">
                <legend>Edições do Produto</legend>
                <table class="edicaoProdCadastradosGrid"></table>
              </fieldset>

              <fieldset class="classFieldset" style="width:480px!important; margin-top:10px!important;">
                <legend>Produtos Selecionados</legend>
                <table class="edicaoSelecionadaGridHistoricoVenda"></table>
              </fieldset>
            </div>
            <fieldset class="classFieldset fieldFiltroItensNaoBloqueados" style="float:left; width:417px!important; margin-left:10px!important;">
              <legend>Pesquisar Histórico de Venda</legend>

              <form id="filtroHistoricoVenda">
                <table width="410" border="0" cellpadding="2" cellspacing="1">
                  <tr>
                    <td width="45"> <strong>Status:</strong>
                    </td>
                    <td width="24">
                      <input type="radio" name="status" id="todas" value="radio" checked />
                    </td>
                    <td width="101">
                      <label for="todas">Todos os Status</label>
                    </td>
                    <td width="20">
                      <input type="radio" name="status" id="hist-venda-cotasAtivas" value="radio" />
                    </td>
                    <td width="184">
                      <label for="cotasAtivas">Cotas Ativas</label>
                    </td>
                  </tr>
                </table>
                <table width="410" border="0" cellpadding="2" cellspacing="1">
                  <tr>
                    <td width="20">
                      <input type="radio" name="filtroPor" id="radio3" value="radio" onclick="historicoVendaController.filtroReparte();" />
                    </td>
                    <td width="87">
                      <label for="radio3"> <strong>Qtde Reparte:</strong>
                      </label>
                    </td>
                    <td width="48">
                      <span class="filtroQtdeReparte">Inicial:</span>
                    </td>
                    <td width="62">
                      <input type="text" onkeydown="onlyNumeric(event);" name="filtro.qtdReparteInicial" id="qtdReparteInicial" style="width:40px; text-align:center;" class="filtroQtdeReparte"  />
                    </td>
                    <td width="35">
                      <span class="filtroQtdeReparte">Final:</span>
                    </td>
                    <td width="46">
                      <input type="text" onkeydown="onlyNumeric(event);" name="filtro.qtdReparteFinal" id="qtdReparteFinal" style="width:40px; text-align:center;" class="filtroQtdeReparte"  />
                    </td>
                    <td width="15">
                      <span class="classPesquisar filtroQtdeReparte">
                        <a href="javascript:;" id="pesquisaPorQtdReparte"></a>
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <input type="radio" name="filtroPor" id="radio4" value="radio" onclick="historicoVendaController.filtroVenda();" />
                    </td>
                    <td>
                      <label for="radio4">
                        <strong>Qtde. Venda:</strong>
                      </label>
                    </td>
                    <td>
                      <span class="filtroQtdeVenda" >Inicial:</span>
                    </td>
                    <td>
                      <input type="text" onkeydown="onlyNumeric(event);" name="filtro.qtdVendaInicial" id="qtdVendaInicial" style="width:40px; text-align:center;" class="filtroQtdeVenda"  />
                    </td>
                    <td>
                      <span class="filtroQtdeVenda">Final:</span>
                    </td>
                    <td>
                      <input type="text" onkeydown="onlyNumeric(event);" name="filtro.qtdVendaFinal" id="qtdVendaFinal" style="width:40px; text-align:center;" class="filtroQtdeVenda" />
                    </td>
                    <td width="15">
                      <span class="classPesquisar filtroQtdeVenda">
                        <a href="javascript:;" id="pesquisaPorQtdVenda"></a>
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <input type="radio" name="filtroPor" id="radio5" value="radio" onclick="historicoVendaController.filtroPercVenda();" />
                    </td>
                    <td>
                      <label for="radio5">
                        <strong>% Venda:</strong>
                      </label>
                    </td>
                    <td>
                      <span class="filtroPercVenda">
                        <input onkeydown="onlyNumeric(event);"  name="filtro.percentualVenda" id="percentualVenda" type="text" style="width:30px;" />
                        %
                      </span>
                    </td>
                    <td width="15">
                      <span class="classPesquisar filtroPercVenda">
                        <a href="javascript:;" id="pesquisaPorPercentualVenda"></a>
                      </span>
                    </td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                  </tr>
                </table>
                <table width="410" border="0" cellpadding="2" cellspacing="1">
                  <tr>
                    <td width="20">
                      <input type="radio" name="filtroPor" id="radio9" value="radio" onclick="historicoVendaController.filtroComponentes();" />
                    </td>
                    <td width="69">
                      <label for="radio9">
                        <strong>Componentes:</strong>
                      </label>
                    </td>
                    <td width="523" colspan="10">
                      <table border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorSegmento" >
                        <tr>
                          <td width="110">
                            <select name="filtro.componentesPdv" id="componente"  style="width:110px;" class="filtroComponentes">
                              <option selected="selected">Selecione...</option>
                              <c:forEach items="${componenteList}" var="componente" varStatus="idx">
                                <option value="${idx.count-1}">${componente.descricao}</option>
                              </c:forEach>
                            </select>
                          </td>
                          <td width="36">
                            <span class="filtroComponentes">Elem.:</span>
                          </td>
                          <td width="110">
                            <select name="filtro.elemento" id="elemento" style="width:110px;" class="filtroComponentes">
                              <option selected="selected">Selecione...</option>
                            </select>
                          </td>
                          <td width="15">
                            <span class="classPesquisar filtroComponentes">
                              <a href="javascript:;" id="pesquisaPorComponenentes">&nbsp;</a>
                            </span>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
                <table width="410" border="0" cellpadding="2" cellspacing="1">
                  <tr>
                    <td width="20">
                      <input type="radio" name="filtroPor" id="radio6" value="radio" onclick="historicoVendaController.filtroCotas();" />
                    </td>
                    <td width="27">
                      <label for="radio6">
                        <strong>Cota:</strong>
                      </label>
                    </td>
                    <td colspan="2">
                      <input type="text" name="filtro.cotaDto.numeroCota" id="hist-venda-numeroCota" style="width:60px;" class="filtroCotas"  />
                    </td>
                    <td width="30">
                      <span class="filtroCotas">
                        <strong>Nome:</strong>
                      </span>
                    </td>
                    <td width="207">
                      <input type="text" name="filtro.cotaDto.nomePessoa" id="hist-venda-nomePessoa" style="width:200px;" class="filtroCotas"  />
                    </td>
                    <td width="23">
                      <span class="classPesquisar filtroCotas">
                        <a href="javascript:;" id="pesquisaCotaPorNumeroOuNome">&nbsp;</a>
                      </span>
                    </td>
                  </tr>
                </table>

                <table width="410" border="0" cellpadding="2" cellspacing="1">
                  <tr>
                    <td width="20">
                      <input type="radio" name="filtroPor" id="todasAsCotasFiltro" value="radio" onclick="historicoVendaController.filtroTodasAsCotas()" />
                    </td>
                    <td>
                      <label for="todasAsCotasFiltro">
                        <strong>Todas as cotas</strong>
                      </label>
                      <span class="classPesquisar filtroTodasAsCotas">
                        <a href="javascript:;" id="pesquisarTodasAsCotas">&nbsp;</a>
                      </span>
                    </td>
                  </tr>

                </table>

              </form>
            </fieldset>
            <fieldset class="classFieldset" style="width:417px!important; margin-left:10px!important; margin-top:10px!important; ">
              <legend>Resultado da Pesquisa</legend>

              <table class="pesqHistoricoGrid"></table>
            </fieldset>

          </div>
        </div>
        <div class="linha_separa_fields">&nbsp;</div>

      </div>
    </div>
  </div>