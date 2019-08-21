$("<input id='select-load-mode' value='true'/>").appendTo("body");
function helperItemAddInitial(_$target){
    _$target.children("div[dc*='handler.parameter']").find('table:visible:first tbody tr td.center a').remove();
    _$target.children("div[dc*='handler.precheck']").find('table.table td.center a').not(":first").not(":last").remove();
    if(_$target.is("tr")){
        calcOptionsAndRefresh(_$target);
    }else {
        var _$flatContainer = _$target;
        _$flatContainer.find(".icon-trash").parent(".btn.btn-danger.hfhref").not(".extend-btn").after($('<a class="btn btn-danger hfhref extend-btn" href="javascript:void(0)" params="" action=\'{"component.row.delete":{"param":"{}"}}\'><i class="icon-trash"></i></a>'));
        _$flatContainer.find(".icon-trash").parent(".btn.btn-danger.hfhref").not(".extend-btn").remove();
        _$flatContainer.find("input[value=sql]").parent().parent().find("input#value").parent().next().append($("<a class='btn btn-danger dyn-script-btn'><i class='icon-refresh'></i></a>"));
        _$flatContainer.find("input[value=sql]").parent().parent().find("input#value").addClass("input-textarea-switch");
        var $sqlInput = _$flatContainer.find("input[value=sql]").parent().parent().find("input#value");
        if($sqlInput.length > 0) {
            var $sqlTextarea = $("<textarea class='input-mini span12' id='" + $sqlInput.attr("id") + "' name='" + $sqlInput.attr("name")+ "' rows='1'> "+ " </textarea>")
            $sqlTextarea.val($sqlInput[0].getAttribute("value"));
            $sqlInput.after($sqlTextarea);
            $sqlInput.remove();
        }

        _$flatContainer.find("input#value[value*=\\$\\{PARAMETERS\\.PAGE]").each(function(){
            var expr = $(this).val();
            if(expr.match("\\$\\{[a-zA-Z.]+:\\d+\\}")) {
                $(this).val(expr.match("\\d+")[0]);
            }
        });
        _$flatContainer.find("input#value[value*=\\$\\{PARAMETERS\\.VALUES],input#value[value*=\\$\\{HANDLER],input#value[value*=\\$\\{RUNTIME]").parent().parent().hide();

        _$flatContainer.find("input[id='#default'][value*=\\$\\{DATASOURCE], input[id='#default'][value*=\\$\\{THIRD]").not(":visible").each(function(){
            var defaultValue = $(this).val();
            var $this = $(this).next("td").children("#value:first");
            $this.attr("data-condition","1=1");
            $this.attr("data-value",$this.val());
            if(defaultValue == "${DATASOURCE.DBS}"){
                $this.attr("data-code","cfg_datasouce_mysql.remark.remark");
                $this.attr("db_object_type", "db");
            }else if(defaultValue == "${DATASOURCE.TABLES}"){
                $this.attr("data-code","URL:/extend/getDbTables.json");
                $this.attr("relat-element","mysql_key={dbId}");
                $this.parent().next().append($("<a class='btn btn-danger dyn-script-btn'><i class='icon-refresh'></i></a>"));
                $this.attr("db_object_type", "table");
            }else if(defaultValue == "${DATASOURCE.FIELDS}"){
                $this.attr("data-code","URL:/extend/getDbColumns.json");
                $this.attr("relat-element","table={tableId}&&mysql_key={dbId}");
                $this.attr("db_object_type", "field");
            }else if(defaultValue == "${THIRD.DOMAIN}"){
                $this.attr("data-code","URL:/extend/getThirdApiDomains.json");
                $this.attr("relat-element","1=1");
                $this.attr("db_object_type", "db");
            }else if(defaultValue == "${THIRD.API}"){
                $this.attr("data-code","URL:/extend/getThirdApis.json");
                $this.attr("relat-element","domain_id={dbId}");
                $this.attr("db_object_type", "table");
            }else if(defaultValue == "${THIRD.SCHEMA}"){
                $this.attr("data-code","URL:/extend/getThirdApiSchema.json");
                $this.attr("relat-element","domain_id={dbId}&&api_path={tableId}");
                $this.attr("db_object_type", "field");
                $this.parent().next().append($("<a class='btn btn-danger third-invoke-btn'><i class='icon-refresh'></i></a>"));
            }

            var html = $this.get(0).outerHTML;

            html = html.replace(/^<input/,"<select");
            html = html + "</select>";

            $this.replaceWith(html);
        });

        _$flatContainer.find("input[name='#parentPath']").trigger("change");

        calcMappingSelectOptionsAndRefresh(_$flatContainer);
        refreshRelationOptionsAndSetValue(_$flatContainer);

        add_result_batch_edit_href();

        _$target.find("input[name='#class'][value='']").parent().find("[name='#path']").prev().each(function () {
            var url = $(this).text();
            $(this).after($('<a class="goto-handler-href" href="javascript:void(0)" params="" action="" style="color: dodgerblue;">' + url + '</a>'));
            $(this).remove();
        })
        // _$target.find(".hfform form.form-horizontal fieldset").each(function(){
        //    var $form = $(this);
        //     $form.children(":last").css("margin-right", "20px");
        //     $form.children(".span3:last").removeClass("span3").addClass("span2")
        // });
    }
}

function refreshObjectList(_$this, dbId, tableId){
    var dbObjectType = _$this.attr("db_object_type");

    if(dbObjectType == "table" && dbId) {
        _$this.attr("dbId",dbId);
        refreshObject(_$this);
    }else if(dbObjectType == "field") {
        if(dbId){
            _$this.attr("dbId",dbId);
        }
        if(tableId){
            _$this.attr("tableId",tableId);
        }
        refreshObject(_$this);
    }
}
function refreshObject(_$this){
    var $dbIdInput = _$this.next("input[name=dbId]");
    var $tableIdInput = _$this.next("input[name=tableId]");
    if(_$this.attr("dbId")){
        if($dbIdInput.length > 0){
            $dbIdInput.val(_$this.attr("dbId"));
        }else {
            $dbIdInput = $("<input type='hidden' name='dbId' value='" + _$this.attr("dbId") + "'/>");
            _$this.after($dbIdInput);
        }
    }
    if(_$this.attr("tableId")){
        if($tableIdInput.length > 0){
            $tableIdInput.val(_$this.attr("tableId"));
        }else {
            $tableIdInput = $("<input type='hidden' name='tableId' value='" + _$this.attr("tableId") + "'/>");
            _$this.after($tableIdInput);
        }
    }
    $.selectLoad(_$this, function(){
        if($dbIdInput) $dbIdInput.remove();
        if($tableIdInput) $tableIdInput.remove();
    });

}

function initRelationSelect(){
    $("input[name='#parentPath']").trigger("change");
    calcMappingSelectOptionHtml($("body"));
    refreshRelationOptionsAndSetValue($("body"));
};
/**
 * 如果元素发生变化找到上级容器，计算上级容器Option片段，同时刷新相关“依赖属性”下拉框
 * @param _$container
 */
function calcOptionsAndRefresh(_$element){
    var $container = _$element.parents("div[dc='peacock_api_xeditor#api.handlers.handler.result'], div[dc='peacock_api_xeditor#api.prehandler.result'], div[id='peacock_api_xeditor#api.parameters.parameter_list']").eq(0).parent();
    calcMappingSelectOptionsAndRefresh($container);
};

/**
 * 如果容器发生变化，计算该容器Option片段，同时刷新相关“依赖属性”下拉框
 * @param _$container
 */
function calcMappingSelectOptionsAndRefresh(_$container){
    calcMappingSelectOptionHtml(_$container);
    var $table = _$container.find("div[dc='peacock_api_xeditor#api.handlers.handler.result'], div[dc='peacock_api_xeditor#api.prehandler.result'], div[id='peacock_api_xeditor#api.parameters.parameter_list']").eq(0);
    if($table.is("div[id='peacock_api_xeditor#api.parameters.parameter_list']")){
        refreshRelationOptionsAndSetValue($("body"));//如果是请求参数变化，则刷新所有的“依赖属性”下拉框
    }else{
        if($table.is("div[dc='peacock_api_xeditor#api.prehandler.result']")){
            refreshRelationOptionsAndSetValue($("div[dc='peacock_api_xeditor#api.handlers.handler']:first"));//如果是前置处理器结果参数变化，则刷新所有的处理器的“依赖属性”下拉框
        }
        $table.parent().parent().nextAll().each(function(){
            refreshRelationOptionsAndSetValue($(this));
        });
    }
};

/**
 * 计算容器内所有供“依赖属性”下拉框的使用的Option HTML片段(按组件缓存)
 * @param _$container
 */
function calcMappingSelectOptionHtml(_$container){
    if(!_$container){
        _$container=$("body");
    }
    var $dependenceTables = _$container.find("div[dc='peacock_api_xeditor#api.handlers.handler.result'], div[dc='peacock_api_xeditor#api.prehandler.result'], div[id='peacock_api_xeditor#api.parameters.parameter_list']");
    $dependenceTables.each(function(){
        var $table=$(this);
        var $select = $("<select></select>");
        if($table.is("div[id='peacock_api_xeditor#api.parameters.parameter_list']")){
            $select.append($("<option value='' selected='selected'>-请选择-</option>"));
            $table.find("table tbody tr:visible").each(function(){
                if($(this).find("[name='#name']").val()){
                    var value = $(this).find("[name='#name']").val();
                    var title = $(this).find("[name='#description']").val();
                    var $option = $("<option value='" + value + "'>" + title + "</option>");
                    $select.append($option);
                }
            });
        }else {
            $table.find("table tbody tr:visible").each(function(){
                if($(this).find("[name='#name']").val()){
                    var value = $(this).find("[name='#name']").val();
                    var title = $(this).find("[name='#alias']").val();
                    if(!title) title = value;
                    var $option = $("<option value='" + value + "'>" + value + "</option>");
                    $select.append($option);
                }
            });
        }
        $table.attr("depend-select-options", $select.html());
    });
};

/**
 * 刷新容器内所有“依赖属性”下拉框的Options以及智能赋值
 * @param _$container
 */
function refreshRelationOptionsAndSetValue(_$container){
    if(!_$container){
        _$container=$("body");
    }
    var $parameterContainers = _$container.find("div[dc='peacock_api_xeditor#api.handlers.handler.parameter'], div[dc='peacock_api_xeditor#api.prehandler.parameter']");
    $parameterContainers.each(function(){
        var $this = $(this);
        var html = mergeSelectOptionsHtml($this);
        $this.find("select[name='#ref']").html(html);
        $this.find("select[name='#ref']").each(function(){
            if($(this).attr("data-value")){
                $(this).val($(this).attr("data-value"));
            }
            if(!$(this).val()){//自动匹配
                var target = $(this).parents("tr:first").find("input[name='#name']").val();
                if($(this).find("option[value='" + target + "']").size() > 0) {
                    $(this).val(target);
                }else {
                    $(this).find("option").each(function(){
                        if($(this).val().replace("_","").toLowerCase() == target.replace("_","").toLowerCase()){
                            $(this).attr("selected",true);
                        }
                    });
                }
            }
        });
    });
};
function mergeSelectOptionsHtml(_$parameterContainer){

    var html = $("div[id='peacock_api_xeditor#api.parameters.parameter_list'][depend-select-options]").attr("depend-select-options");

    if(_$parameterContainer.is("div[dc='peacock_api_xeditor#api.handlers.handler.parameter']")){
        $("div[dc='peacock_api_xeditor#api.prehandler.result'][depend-select-options]").each(function(){
            html = html + $(this).attr("depend-select-options");
        });
    }

    _$parameterContainer.parent().parent().prevAll().find("div[depend-select-options]").each(function(){
        html = html + $(this).attr("depend-select-options");
    });
    return html;
};



function showSelect(){
    $.reloadDisplay($("[id='peacock_api_xeditor#api.parameters.parameter_list']"));
    $.reloadDisplay($("[dc='peacock_api_xeditor#api.parameters.parameter.checker']"));
    $.reloadDisplay($("[dc='peacock_api_xeditor#api.parameters.parameter.option']"));
}



$("input.input-textarea-switch").live("click", function () {
    var $textarea = $("<textarea class='input-mini span12 input-textarea-switch'>" + $(this).val() + "</textarea>");
    $(this).after($textarea);
    $(this).hide();
    $textarea.trigger("click");
    $textarea.trigger("autosize");
});
$("textarea.input-textarea-switch").live("blur", function () {
    $(this).prev().val($(this).val());
    $(this).prev().show();
    $(this).remove();
});

$("select[db_object_type]").live("change", function(){
    $this = $(this);
    var dbObjectType = $this.attr("db_object_type");
    var objectId = $this.val();
    var dbId = null;
    var tableId = null;
    if(dbObjectType == "db"){
        dbId = objectId;
    }else if(dbObjectType == "table"){
        tableId = objectId;
    }else {
        return;
    }


    $this.parents("table:first").find("select[db_object_type]").each(function(){
        var $childObject = $(this);
        if($this != $childObject){
            refreshObjectList($childObject, dbId, tableId);
        }
    });

});

function GetUrlParam(paraName) {
    var url = document.location.toString();
    var arrObj = url.split("?");

    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("&");
        var arr;

        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");

            if (arr != null && arr[0] == paraName) {
                return arr[1];
            }
        }
        return "";
    }
    else {
        return "";
    }
}

$(".goto-handler-href").live("click", function(){
   var path = $(this).text();
   var programId=GetUrlParam("programId");
   var version = $(this).parents("form:first").find("input[name='#version']").val();
    $.ajax({
        url: "/extend/getHandlerIdByPathInfo",
        data: {
            programId: programId,
            path:path,
            version: version
        },
        type: 'post',
        success: function(data){
            if(data.resultCode != '0') {
                alert(data.resultMessage);
                return;
            }

            window.open("/config/cfg_runtime_handler_xmleditor.html?" +
                "id=" + data.data.id + "&programId=" + programId  + "&pathEnding=" + data.data.pathEnding);
        }
    });
});

function ajaxRequest(url, data, _$this) {
    $.ajax({
        url: url,
        data: data,
        type: 'post',
        success: function(data){
            if(data.resultCode != '0') {
                alert(data.resultMessage);
                return;
            }

            if(data.data.in){
                var _struct = data.data.in;
                var $resultBody = $("[id='peacock_api_xeditor#api.parameters.parameter_list'] table:visible tbody");
                mergeBackendReturn($resultBody, _struct);
            }

            if(data.data.out){
                var _struct = data.data.out;
                var $resultBody = _$this.parents("[component=flatContainer]:first").children(":last").find("table.table tbody");
                mergeBackendReturn($resultBody, _struct);
            }
        }
    });
}

function mergeBackendReturn($resultBody, _struct) {
    var hasItem = {};
    $resultBody.find("input[name='#name']").each(function () {
        hasItem[$(this).val()] = $(this).parent().parent();
    });

    // $resultBody.find("tr").not(":first").remove();
    var addStatus = false;
    var $baseTr = $resultBody.children(":first");
    for(var key in _struct) {
        if(!(key in hasItem)) {
            var $newTr = $baseTr.clone();
            $newTr.find("input[name='#name']").val(key);
            // console.info(_struct[key]);
            $newTr.find("select[name='#type']").val(_struct[key][0]);
            if($newTr.find("input[name='#description']")) {
                $newTr.find("input[name='#description']").val(_struct[key][1]);
            }
            if($newTr.find("select[name='#required']")) {
                $newTr.find("select[name='#required']").val(_struct[key][2]);
            }
            if(!addStatus) $newTr.children(":first").css("border-top","1px solid #FF7F50"); //第一条新增内容增加红线表示

            $baseTr.parent().append($newTr);
            $.refreshOptionsShowAndHide($newTr.find("select[name='#formatter']"));
            $.refreshOptionsShowAndHide($newTr.find("select[name='#pattern']"));
            addStatus = true;
        }
    }

    var deleteTrs = [];
    for(var key in hasItem) {
        if(!(key in _struct)) {
            deleteTrs.push(hasItem[key]);
        }
    }

    if(deleteTrs.length == hasItem.length && addStatus == false) {
        deleteTrs.splice(0, 1);
    }
    for(var idx in deleteTrs) {
        deleteTrs[idx].remove();
    }
}

$(".third-invoke-btn").live("click",function(){
    if(!$(".third-invoke-btn:visible").parents("tr").find("#value").val()){
        alert("值不能为空!");
    }
    var $this = $(this);
    var allValues = {};
    var $names = $this.parents("table:first").find("[name='#name']");
    $this.parents("table:first").find("#value").each(function(i){
        if($(this).is("input") || $(this).is("select")) {
            allValues[$names.eq(i).val()] = $(this).val();
        }

    });

    var data = {"allValues" : JSON.stringify(allValues)};
    ajaxRequest("/extend/dynThirdApiResultParse.json", data, $this);
});




$(".dyn-script-btn").live("click",function(){
    if(!$(".dyn-script-btn:visible").parents("tr").find("#value").val()){
        alert("值不能为空!");
    }

    var $this = $(this);
    var allValues = {};
    var $names = $this.parents("table:first").find("[name='#name']");
    $this.parents("table:first").find("[name='value']").each(function(i){
        if($(this).is("input") || $(this).is("textarea") || $(this).is("select")) {
            allValues[$names.eq(i).val()] = $(this).val();
        }

    });
    var clazz = $this.parents("[component=flatContainer]:first").children(":first").find("[name='#class']").val();
    var path = $this.parents("[component=flatContainer]:first").children(":first").find("[name='#path']").val();

    var data = {"allValues" : JSON.stringify(allValues), "clazz" : clazz, "path" : path};
    ajaxRequest("/extend/dynSqlResultParse", data, $this);
});

function toggle_batch_edit_button(_$this, _targetClass) {
    var $list = _$this.parents(".hflist:first");
    $list.find(".batch-edit-fetch").toggle();
    $list.find(".batch-edit-remove").toggle();
    $list.find(".batch-edit-cancel").toggle();
    if(_targetClass){
        $list.find(_targetClass).toggle();
    }

}

function add_batch_edit_checkbox(_$table) {
    _$table.find("tr:first").prepend("<td width='2%'></td>");
    _$table.find("tr").not(":first").prepend("<td width='2%'><input type='checkbox' name='checkIds'></td>");
    _$table.find("tr input:checkbox").uniform();
}

function remove_batch_edit_checkbox(_$table) {
    _$table.find("tr").each(function () {
       $(this).find("td:first").remove();
    });
}

function mergeResultBody($resultBody, $newResultBody){
    var change = false;
    $newResultBody.children("tr").each(function () {
        var $tr = $(this);
        var respName = $tr.find("input[name='#name']").val();
        if($resultBody.find("input[name='#name'][value='" + respName + "']").length == 0) {//表明沒有
            var $newTr = $tr.clone();
            $resultBody.append($newTr);
            if(!change) {
                $newTr.children(":first").css("border-top","1px solid #FF7F50"); //第一条新增内容增加红线表示
            }
            change = true;
        }
    });
    return change;
}

$(".batch-edit-refresh").live("click", function () {
    var $item = $(this).parents("div.hfcontainer:first").parent("[id]");
    var $container = $item.parents("div.row-fluid[dc]:first");
    var $resultBody = $(this).parents("div.hflist[id]").find("form tbody");
    var resultId = $(this).parents("div.hflist[id]").attr("id");

    var helpName = $item.attr("id");
    var targetId = $container.attr("dc");
    $.post("/getFileEditorHelperData.html",{helperName:helpName, targetId:targetId},function(data){
        // if(data.resultCode != '0') {
        //     alert(data.resultMessage);
        //     return;
        // }
        var $newResultBody = $(data).find("[id='" + resultId + "'] form tbody");
        var change = mergeResultBody($resultBody, $newResultBody);
        if(change) {
            if(helperItemAddInitial) {
                helperItemAddInitial($item);
            }
            $.reloadDisplay($resultBody);
        }

    });
});


$(".batch-edit-copy").live("click", function () {
    var $resultBody = $(this).parents("div.hflist[id]").find("form tbody");
    if($(this).text() == '复制') {
        $(".runtime-copy-src").removeClass("runtime-copy-src");
        $resultBody.addClass("runtime-copy-src");
        $(".batch-edit-copy").text("粘贴");
        $(this).text("复制取消")
    }else if($(this).text() == '复制取消') {
        $(".runtime-copy-src").removeClass("runtime-copy-src");
        $(".batch-edit-copy").text("复制");
    }else if($(this).text() == '粘贴') {
        var $srcResultBody = $(".runtime-copy-src");
        $(".runtime-copy-src").removeClass("runtime-copy-src");
        $(".batch-edit-copy").text("复制");
        var change = mergeResultBody($resultBody, $srcResultBody);
        if(change) {
            if(helperItemAddInitial) {
                helperItemAddInitial($item);
            }
        }

    }

});

$(".batch-alias-set").live("click", function () {
    var $resultBody = $(this).parents("div.hflist[id]").find("form tbody");

    $resultBody.find("tr").each(function(){
        var $tr = $(this);
        var name = $tr.find("input#\\#name").val();
        var alias = $tr.find("input#\\#alias").val();
        if(!alias && name) {
            if(name.indexOf("_") > -1) {
                var nameParts = name.toLowerCase().split("_");
                for(var i in nameParts) {
                    if(i > 0 && nameParts[i]) {
                        nameParts[i] = nameParts[i].charAt(0).toUpperCase() + nameParts[i].substring(1)
                    }
                }
                $tr.find("input#\\#alias").val(nameParts.join(""));
            }else {
                $tr.find("input#\\#alias").val(name);
            }
        }
    })
});


    $(".batch-edit-fetch").live("click", function () {
    toggle_batch_edit_button($(this), ".batch-edit-fetch-confirm");
    var $table = $(this).parents(".hflist:first").find("form table");
    add_batch_edit_checkbox($table);
});


$(".batch-edit-remove").live("click", function () {
    toggle_batch_edit_button($(this), ".batch-edit-remove-confirm");
    var $table = $(this).parents(".hflist:first").find("form table");
    add_batch_edit_checkbox($table);
});

$(".batch-edit-fetch-confirm").live("click", function () {
    var $table = $(this).parents(".hflist:first").find("form table");
    if($table.find("input[name=checkIds]:checked").length == 0){
        alert("至少选择一个");
    }else {
        toggle_batch_edit_button($(this), ".batch-edit-fetch-confirm");
        $table.find("input[name=checkIds]").not(":checked").each(function () {
            $(this).parents("tr:first").remove();
        });
        remove_batch_edit_checkbox($table);
    }



});
$(".batch-edit-remove-confirm").live("click", function () {
    var $table = $(this).parents(".hflist:first").find("form table");
    if($table.find("input[name=checkIds]:checked").length == 0){
        alert("至少选择一个");
    }else {
        toggle_batch_edit_button($(this), ".batch-edit-remove-confirm");
        $table.find("input[name=checkIds]:checked").each(function () {
            $(this).parents("tr:first").remove();
        });
        remove_batch_edit_checkbox($table);
    }
});

$(".batch-edit-cancel").live("click", function () {
    toggle_batch_edit_button($(this));
    $(".batch-edit-remove-confirm").hide();
    $(".batch-edit-fetch-confirm").hide();
    var $table = $(this).parents(".hflist:first").find("form table");
    remove_batch_edit_checkbox($table);

});



function add_result_batch_edit_href() {
    $("[id='peacock_api_xeditor#api.handlers.handler.result_list'] div h2,[id='peacock_api_xeditor#api.prehandler.result_list'] div h2")
        .not(".href-added").each(function () {
        var $this = $(this);
        $this.append('[<a class="batch-edit batch-edit-fetch"  href="javascript:;" style="color: dodgerblue;">批量选取</a>' +
            '<a class="batch-edit batch-edit-fetch-confirm"  href="javascript:;" style="color: dodgerblue; display: none;">选取确认</a>' +
            '<a class="batch-edit batch-edit-remove-confirm" href="javascript:;" style="color: dodgerblue; display: none;">移出确认</a>,' +
            '<a class="batch-edit batch-edit-remove" href="javascript:;" style="color: dodgerblue;">批量移出</a>' +
            '<a class="batch-edit batch-edit-cancel" href="javascript:;" style="color: red; display: none;">取消</a>], ');
            $this.append('[<a class="batch-edit batch-edit-refresh" href="javascript:;" style="color: dodgerblue;">刷新</a>,' +
                '<a class="batch-edit batch-edit-copy" href="javascript:;" style="color: dodgerblue;">复制</a>,' +
                '<a class="batch-edit batch-alias-set" href="javascript:;" style="color: dodgerblue;">别名设置</a>]');
        $(this).addClass("href-added");
    });
}

$(function () {
    $(".hfcontainer").css("margin-left","0px");

    $("div.hfcontainer[component='flatContainer']").each(function(){
        helperItemAddInitial($(this));
    });

    // 将删除按钮统一调整为业绩级删除，不调用后台删除
    $(".icon-trash").parent(".btn.btn-danger.hfhref").not(".extend-btn").after($('<a class="btn btn-danger hfhref extend-btn" href="javascript:void(0)" params="" action=\'{"component.row.delete":{"param":"{}"}}\'><i class="icon-trash"></i></a>'));
    $(".icon-trash").parent(".btn.btn-danger.hfhref").not(".extend-btn").remove();


    $("[id='peacock_api_xeditor#api.parameters.parameter_list'] i.icon-trash").live("click",function(){
        calcOptionsAndRefresh($(this));
    });
    $("[id='peacock_api_xeditor#api.parameters.parameter_list'] input[name='#name'], [id='peacock_api_xeditor#api.parameters.parameter_list'] input[name='#description']").live("change",function(){
        calcOptionsAndRefresh($(this));
    });

    $("[id='peacock_api_xeditor#api.prehandler.result_list'] i.icon-trash").live("click",function(){
        calcOptionsAndRefresh($(this));
    });
    $("[id='peacock_api_xeditor#api.prehandler.result_list'] input[name='#name'], [id='peacock_api_xeditor#api.prehandler.result_list'] input[name='#alias']").live("change",function(){
        calcOptionsAndRefresh($(this));
    });

    $("[id='peacock_api_xeditor#api.handlers.handler.result_list'] i.icon-trash").live("click",function(){
        calcOptionsAndRefresh($(this));
    });
    $("[id='peacock_api_xeditor#api.handlers.handler.result_list'] input[name='#name'], [id='peacock_api_xeditor#api.handlers.handler.result_list'] input[name='#alias']").live("change",function(){
        calcOptionsAndRefresh($(this));
    });

    $("textarea").live("focus", function () {
        $(this).attr("rows", $(this).val().split("\n").length +1);
        var cur_width = $(this).css("width");
        $(this).css("width", (cur_width.substring(0, cur_width.length -2 ) * 3)  + "px");
    });

    $("textarea").live("blur", function () {
        $(this).attr("rows", 1);
        $(this).css("width", "");
    });

    $("input[name='#parentPath']").live("change",function(){
        var val = $(this).val();
        var refresh = false;
        if(val) {
            var optionString = "<option value='HANDLER:" + val + "'>处理器:" + val + "</option>";
            if($(this).parents("div.hfform:first").attr("depend-select-options") != optionString) {
                $(this).parents("div.hfform:first").attr("depend-select-options", optionString);
                refresh = true;
            }

        }else {
            if($(this).parents("div.hfform:first").attr("depend-select-options")) {
                $(this).parents("div.hfform:first").removeAttr("depend-select-options");
                refresh = true;
            }
        }

        if(refresh) {
            var $handlerFlatContainer = $(this).parents("div[component=flatContainer]:first").parent();
            $handlerFlatContainer.nextAll().each(function(){
                refreshRelationOptionsAndSetValue($(this));
            });

            var $schemeBody = $("[id='peacock_api_xeditor#api.schema_list'] form tbody");


            var newSchema = [];
            $("input[name='#parentPath'][value!='']").each(function() {
                if(!$(this).val().startsWith("/tmp/")) {
                    newSchema.push($(this).val());
                }

            });

            var rowCont = 0;
            var oldSchema = [];
            $schemeBody .find("input[name='#path']").each(function() {
               var schemaName = $(this).val();
                oldSchema.push(schemaName);
                rowCont+=1;
            });

            var $tr = $schemeBody.children(":first");
            for(var i in newSchema) {
                if(oldSchema.indexOf(newSchema[i]) < 0) {
                    rowCont+=1;
                    var $newTr = $tr.clone();
                    $newTr.find("input[name='#path']").val(newSchema[i]);
                    $newTr.find("input[name='#description']").val("");
                    $schemeBody.append($newTr);
                    oldSchema.push(newSchema[i]);
                }
            }

            var length = oldSchema.length;
            for(var i in oldSchema) {
                var index = length - i - 1;
                if(newSchema.indexOf(oldSchema[index]) < 0) {
                    if(rowCont <= 1) {
                        $schemeBody.children("tr").eq(index).find("input[name='#path']").val("");
                        $schemeBody.children("tr").eq(index).find("input[name='#description']").val("");
                    }else {
                        if(!$schemeBody.children("tr").eq(index).find("input[name='#description']").val()) {
                            $schemeBody.children("tr").eq(index).remove();
                            rowCont-=1;
                        }
                        // $schemeBody .find("input[name='#path'][value='" + oldSchema[i] + "']:first").parent().parent().remove();
                    }

                }
            }
        }

    });

    setTimeout("initRelationSelect()",100);
    setTimeout("showSelect()",500);



});