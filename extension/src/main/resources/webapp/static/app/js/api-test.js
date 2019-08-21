
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
    $("#request-parameter-div").show();
    $("#request-parameter-h3").show();
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
   var body = $("#request-message-body").val();

   $.post("/apitest/exec.json",{
        "url": url,
        "method": method,
        "parameter": parameterString,
        "body":body
    },function(data){
       if (data.resultCode != '0') {
           alert(data.resultMessage);
           return;
       }
       parseResultData(data);
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

var path = window.location.search.substr(window.location.search.indexOf("=")+1);

$.post("/apitest/init.json",{
    "path": path
    },function(data) {
    if (data.resultCode != '0') {
        alert(data.resultMessage);
        return;
    }
    parseResultData(data);
});

$("ul.todo-list li").live("click", function () {
    var id = $(this).attr("case-id");
    $.post("/apitest/select.json",{
        "id": id
    },function(data){
        if(data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }
        parseResultData(data);

    });
});

$("#open-new-btn").on("click", function (){
    var url = $("#url").val();
    var parameterString = getParameterString();
    window.open(url + "?" + parameterString)
});
$("#case-name-btn").on("click", function (){
    var id = $("#id").val();
    var url = $("#url").val();
    var name = prompt("设置测试CASE名称", $("#name").val());
    $.post("/apitest/name.json",{
        "id": id,
        "path" : url,
        "name": name
    },function(data){
        if(data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }
        parseResultData(data);

    });
});
$("#case-store-btn").on("click", function (){
    var id = $("#id").val();
    var url = $("#url").val();
    var store = $(this).attr("trigger-val");
    $.post("/apitest/store.json",{
        "id": id,
        "path" : url,
        "store" : store
    },function(data){
        if(data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }
        parseResultData(data);

    });
});
$("#case-share-btn").on("click", function (){
    var id = $("#id").val();
    var url = $("#url").val();
    var share = $(this).attr("trigger-val");

    $.post("/apitest/share.json",{
        "id": id,
        "path" : url,
        "share": share
    },function(data){
        if(data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }
        parseResultData(data);

    });
});

$("#case-save-btn").on("click", function (){
    var url = $("#url").val();
    var method = $("#method").val();
    var parameterString = getParameterString();
    var body = $("#request-message-body").val();
    var response = $("#response-message-body").val();
    var id = $("#id").val();

    $.post("/apitest/save.json",{
        "id": id,
        "url": url,
        "method": method,
        "parameter": parameterString,
        "body":body,
        "response":response
    },function(data){
        if(data.resultCode != '0') {
            alert(data.resultMessage);
            return;
        }
        parseResultData(data);

    });
});

function parseResultData(data) {
    for(var key in data.data){
        var value = data.data[key];
        if(key == 'url'){
            $("#url").val(value);
            $("#url").trigger("change");
            $("#url").attr("readonly", 'readonly');
        }else if(key == 'method'){
            setTimeout(function () {
                $("#method").val(data.data.method);
            },500);
        }else if(key == 'parameters'){

        }else if(key == 'requestBody'){
            $("#request-message-body").val(value);
            $("#request-message-body").trigger("autosize");
            $("#request-body-div").show();
            $("#request-body-h3").show();
        }else if(key == 'responseBody'){
            $("#response-message-body").val(value);
            $("#response-message-body").trigger("autosize");
        }else if(key == 'stores'){
            $("#store_ul").html("");
            for(var i in value) {
                $("#store_ul").append(getUlItem(value[i]));
            }
        }else if(key == 'histories'){
            $("#history_ul").html("");
            for(var i in value) {
                $("#history_ul").append(getUlItem(value[i]));
            }
        }else if(key == 'shares'){
            $("#share_ul").html("");
            for(var i in value) {
                $("#share_ul").append(getUlItem(value[i]));
            }
        }else if(key == 'myShares'){
            $("#my_share_ul").html("");
            for(var i in value) {
                $("#my_share_ul").append(getUlItem(value[i]));
            }
        }else if(key == 'caseId'){
            $("#id").val(value)
        }else if(key == 'caseName'){
            $("#name").val(value)
            $("#case-name-btn").show()
        }else if(key == 'caseShare'){
            $("#case-share-btn").show();
            if(value == 1) {
                $("#case-share-btn").text("取消分享");
                $("#case-share-btn").attr("trigger-val", 0);
            }else{
                $("#case-share-btn").text("添加分享");
                $("#case-share-btn").attr("trigger-val", 1);
            }
        }else if(key == 'caseStore'){
            $("#case-store-btn").show();
            if(value == 1) {
                $("#case-store-btn").text("取消收藏");
                $("#case-store-btn").attr("trigger-val", 0);
            }else{
                $("#case-store-btn").text("添加收藏");
                $("#case-store-btn").attr("trigger-val", 1);
            }
        }else if(key == 'isMine'){
            if(value){
                $("#case-save-btn").show();
            }else {
                $("#case-share-btn").hide();
                $("#case-store-btn").hide();
                $("#case-name-btn").hide();
                $("#case-save-btn").hide();
            }
        }
    }
}

function getUlItem(array) {

    var icon = "icon-star-empty";
    if(array[2]){
        icon = "icon-star";
    }

    var $item = $('<li case-id="' + array[0] + '"><span class="todo-actions" style="opacity: 1;"><a href="#"><i class="' + icon + '"></i></a></span>' +
        '                        <span class="desc" style="opacity: 1; text-decoration: none;">' + array[1] + '</span>' +
        '                        <span class="label label-important" style="opacity: 1;">' + array[4]  + '</span>' +
        '                    </li>')
    return $item;

}