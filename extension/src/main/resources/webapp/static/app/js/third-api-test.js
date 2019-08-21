
function parseUrlParamToObject($paramStr){
    var result = {};
    if(!$paramStr) {
        return result;
    }
    var $params = $paramStr.split("&");
    for(var $index in $params) {
        var key = $params[$index].substring(0, $params[$index].indexOf("="));
        var value = $params[$index].substring($params[$index].indexOf("=") + 1);
        if(result[key]) {
            result[key] =result[key] + "," + value;
        }else {
            result[key] = value;
        }
    }
    return result;
}

function getParameterTypeString(){
    var dataString = "";
    $("table#requset_parameter_table tbody tr").each(function(){
        var $row = $(this);
        var paramName = $row.find("input[name=name]").val();
        var paramType = $row.find("select[name=type]").val();
        if(paramType != "string") {
            if(dataString) dataString  += "&";
            dataString += (paramName + "=" + paramType);
        }
    });

    return dataString;
}

function getParameterString(){
    var dataString = "";
    $("table#requset_parameter_table tbody tr").each(function(){
        var $row = $(this);
        var paramName = $row.find("input[name=name]").val();
        var paramValue = $row.find("input[name=value]").val();
        if(dataString) dataString  += "&";
        dataString += (paramName + "=" + paramValue);
    });

    return dataString;
}

function addNewParameter(name, value, desc, $paramTable, $headRow){
    var isNew = true;
    $paramTable.find("input[name='name']").each(function(){
       var tmpName = $(this).val();
       if(tmpName == name) {
           var $row = $(this).parent("td").parent("tr");
           $row.find("input[name='value']").val(value);
           $row.find("input[name='desc']").val(desc);
           isNew = false;
       }
    });
    if(isNew){

        var $newRow = $headRow.clone();
        $newRow.find("input[name='name']").val(name);
        $newRow.find("input[name='value']").val(value);
        $paramTable.append($newRow);
        $paramTable.parents("div.row-fluid").show();
    }
}

$("#url").on("change", function () {
    var urlString = $(this).val();
    var $paramTable = $("table#requset_parameter_table tbody");
    var $headRow = $paramTable.find("tr:first");
    if(urlString.indexOf("?") > -1){
        var para_string = urlString.substring(urlString.indexOf("?")+1);
        var para_map = parseUrlParamToObject(para_string);
        for(var para_key in para_map){
            if(para_key){
                addNewParameter(para_key, para_map[para_key], "", $paramTable, $headRow);
            }
        }
        $(this).val(urlString.substring(0, urlString.indexOf("?")));
    }
    if($paramTable.find("tr").length > 1 && !$paramTable.find("tr td input[name='name']:first").val()){
        $paramTable.children(":first").remove();
    }
});

$("#send-request-btn").on("click", function (){
   var url = $("#url").val();
   var method = $("#method").val();
   var parameterString = getParameterString();
   var parameterTypeString = getParameterTypeString();
   var body = $("#request-message-body").val();
    var domainId = $("#domainId").text();

   $.post("/extend/invokeThirdApi.json",{
       "domainId": domainId,
        "url": url,
        "method": method,
       "parameter": parameterString,
       "parameterType": parameterTypeString,
       "body":body
    },function(data){
        $("#response-message-body").val(data);
       $("#response-message-body").trigger("autosize");
    });
});



$("#add-pub-btn").on("click", function () {
    var domainId = $("#domainId").text();
    var parameterString = getParameterString();
    var body = $("#request-message-body").val();
    $.post("/extend/addPubParameter.json",{
        "domainId": domainId,
        "parameter": parameterString,
        "body":body
    },function(data){
        if(data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }
        var parameters = data.data.parameters;
        if(parameters){
            var $paramTable = $("table#requset_parameter_table tbody");
            var $headRow = $paramTable.find("tr:first");
            for(var i in parameters){
                addNewParameter(parameters[i].code, parameters[i].value, parameters[i].name, $paramTable, $headRow)
            }
        }
        var body = data.data.body;
        $("#request-message-body").val(body);
        $("#request-message-body").trigger("autosize");
    });
});

$("#copy-request-parameters").on("click", function (){
    var urlString = $("#url").val();
    var parameterString = getParameterString();

    $("body").append("<input id = 'copy-tmp-input' />");
    $("#copy-tmp-input").val(urlString + "?" + parameterString);
    var obj = document.getElementById("copy-tmp-input");
    obj.select();
    document.execCommand("Copy");
    $("#copy-tmp-input").remove();
});

$("#copy-request-body").on("click", function (){
    var obj = document.getElementById("request-message-body");
    obj.select();
    document.execCommand("Copy")
});

$("#copy-response-body").on("click", function (){
    var obj = document.getElementById("response-message-body");
    obj.select();
    document.execCommand("Copy")
});

$("#config-review-btn").on("click", function (){
    var kvContainers;
    // if(is_init) {
    //     is_init = false;
    //     kvContainers = $("#kv-container-cache").val();
    // }else {
        kvContainers = getKVContainerPath();
    // }
    var url = $("#url").val();
    var method = $("#method").val();
    var parameterString = getParameterString();
    var parameterTypeString = getParameterTypeString();
    var body = $("#request-message-body").val();
    var response = $("#response-message-body").val();
    var domainId = $("#domainId").text();

    $.post("/extend/configMergeReview.json",{
        "url": url,
        "method": method,
        "domainId": domainId,
        "parameter": parameterString,
        "parameterType": parameterTypeString,
        "body":body,
        "response":response,
        "kvContainers":kvContainers
    },function(data){
        if(data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }
        var api_info = data.data.api;
        $("input[name='request-path']").val(api_info['path']);
        $("input[name='request-method']").val(api_info['method']);
        $("input[name='request-data']").val(api_info['requestType']);
        $("input[name='response-data']").val(api_info['responseType']);
        $("input[name='api-name']").val(api_info['name']);
        $("select[name='api-type']").val(api_info['apiType']);
        $("input[name='api-id']").val(api_info['id']);
        $("input[name='api-domainId']").val(api_info['domainId']);

        var parameter_info = data.data.parameter;
        if(parameter_info && parameter_info.length > 0){
            var $tableBody = $("#request-parameter-table tbody.hflist-data");
            $tableBody.children().not(":first").remove();
            var $headRow = $tableBody.find("tr:first");
            for(var i in parameter_info){
                var $newRow = $headRow.clone();
                $newRow.find("input[name='oper']").val(parameter_info[i]["oper"]);
                $newRow.find("select[name='type']").val(parameter_info[i]["type"]);
                $newRow.find("select[name='helper']").val(parameter_info[i]["batchHelper"]);
                $newRow.find("input[name='path']").val(parameter_info[i]["code"]);
                $newRow.find("input[name='name']").val(parameter_info[i]["name"]);
                $newRow.find("input[name='value']").val(parameter_info[i]["value"]);
                $newRow.find("input[name='is_pub']").val(parameter_info[i]["public"]);
                $newRow.find("input[name='is_common']").val(parameter_info[i]["common"]);
                $tableBody.append($newRow);
            }
            $headRow.remove();
            $tableBody.parents("div.row-fluid").show();
        }
        var request_info = data.data.request;
        if(request_info && request_info.length > 0){
            var $tableBody = $("#request-body-table tbody.hflist-data");
            $tableBody.children().not(":first").remove();
            // $.reloadDisplay($tableBody, false);
            var $headRow = $tableBody.find("tr:first");
            for(var i in request_info){
                var $newRow = $headRow.clone();
                $newRow.find("input[name='oper']").val(request_info[i]["oper"]);
                $newRow.find("select[name='type']").val(request_info[i]["type"]);
                $newRow.find("input[name='path']").val(request_info[i]["path"]);
                $newRow.find("input[name='name']").val(request_info[i]["name"]);
                $newRow.find("input[name='value']").val(request_info[i]["value"]);
                $newRow.find("input[name='is_pub']").val(request_info[i]["public"]);
                $newRow.find("input[name='is_common']").val(request_info[i]["common"]);
                $tableBody.append($newRow);
            }
            $headRow.remove();

            $tableBody.parents("div.row-fluid").show();
        }

        var response_info = data.data.response;
        if(response_info && response_info.length > 0) {
            var $tableBody = $("#response-config-table tbody.hflist-data");
            $tableBody.children().not(":first").remove();
            // $.reloadDisplay($tableBody, false);
            var $headRow = $tableBody.find("tr:first");
            for(var i in response_info){
                var $newRow = $headRow.clone();
                $newRow.find("input[name='oper']").val(response_info[i]["oper"]);
                $newRow.find("select[name='type']").val(response_info[i]["type"]);
                $newRow.find("select[name='helper']").val(response_info[i]["batchHelper"]);
                $newRow.find("input[name='path']").val(response_info[i]["path"]);
                $newRow.find("input[name='name']").val(response_info[i]["name"]);
                $newRow.find("input[name='value']").val(response_info[i]["value"]);
                $newRow.find("input[name='is_pub']").val(response_info[i]["public"]);
                $newRow.find("input[name='is_common']").val(response_info[i]["common"]);
                $newRow.find("input[name='full_path']").val(response_info[i]["tempPath"]);
                $tableBody.append($newRow);
            }
            $headRow.remove();
            $tableBody.parents("div.row-fluid").show();

        }


        $("span.hidden-show-span").each(function(){
            $(this).text($(this).prev("input:hidden").val());
        });
        $("input[name=is_pub][value=false]:hidden").parent().parent().css("color", '#383e4b');
        $("input[name=is_pub][value=true]:hidden").parent().parent().css("color", '#777');
        $("input[name=is_pub][value=false]:hidden").parent().parent().find("select,input:visible").removeAttr("readonly");
        $("input[name=is_pub][value=true]:hidden").parent().parent().find("select,input:visible").attr("readonly", 'readonly');

        $("input[name=is_common][value=false]:hidden").prev().show();
        $("input[name=is_common][value=true]:hidden").prev().hide();
        $("#kv-container-cache").val(getKVContainerPath());


    });
});

$("#test-to-left").on("click", function () {
    $("#test-to-left").toggle();
    $("#test-to-right").toggle();
    $("#test-div").addClass("span6").removeClass("span12");
    $("#conf-div").show();
});
$("#test-to-right").on("click", function () {
    $("#test-to-left").toggle();
    $("#test-to-right").toggle();
    $("#test-div").addClass("span12").removeClass("span6");
    $("#conf-div").hide();
});
$("#conf-to-left").on("click", function () {
    $("#conf-to-left").toggle();
    $("#conf-to-right").toggle();
    $("#conf-div").addClass("span12").removeClass("span6");
    $("#test-div").hide();
});
$("#conf-to-right").on("click", function () {
    $("#conf-to-left").toggle();
    $("#conf-to-right").toggle();
    $("#conf-div").addClass("span6").removeClass("span12");
    $("#test-div").show();

});

$(".area-h3").on("click", function () {
   var $target = $(this).next();
    $target.toggle();
});

$(".set-name-by-code").on("click", function () {
   $(this).parent().next().find("td input[name=name]").each(function () {
     if(!$(this).val()){
         $(this).val($(this).parent().parent().find("input[name=path]").val());
     }
   });
});

$(".save-common-parameter").live("click", function () {
    var $this = $(this);
    var $tr = $this.parent().parent();
    var type = $tr.find("select[name='type']").val();
    var path = $tr.find("input[name='path']").val();
    var name = $tr.find("input[name='name']").val();
    var domainId = $("#domainId").text();
    $.post("/extend/saveCommonParameter.json",{
        "type": type,
        "name": name,
        "path": path,
        "domainId":domainId
    },function(data) {
        if (data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }
        $this.hide();
    });
});

function getKVContainerPath() {
    var paths = [];
    $("select[name='type'] option[value='9']:checked").each(function(){
        paths.push($(this).parent().parent().next().find("input[name='full_path']").val())
    });
    return paths.join();
}

// $("select[name='type']").live("change", function () {
//     var old_val = $("#kv-container-cache").val();
//     var cur_val = getKVContainerPath();
//     if(old_val != cur_val) {
//         $("#config-review-btn").trigger("click");
//     }
// });


var is_api_test = window.location.pathname.endsWith("/third_api_testing.html");
var search_id = window.location.search.substr(window.location.search.indexOf("=")+1);

var is_init = true;
if(is_api_test) {
    var domainId = $("#domainId").text();
    $.post("/extend/initApiTesting.json",{
        "apiId": search_id,
        "domainId": domainId
    },function(data) {
        if (data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }

        $("#url").val(data.data.url);
        $("#url").trigger("change");

        if(data.data.request){
            $("#request-message-body").val(data.data.request);
            $("#request-message-body").parents("div.row-fluid").show();
        }

        $("#response-message-body").val(data.data.response);
        $("#url").attr("readonly", 'readonly');
        $("#request-message-body").trigger("autosize");
        $("#response-message-body").trigger("autosize");
        // $("#kv-container-cache").val(data.data.kvContainers);
        setTimeout(function () {
            $("#method").val(data.data.method);
            $("#config-review-btn").trigger("click");
        },500);
    });
}
