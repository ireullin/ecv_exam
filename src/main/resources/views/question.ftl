<!DOCTYPE html>
<html>
<#include "/header.ftl">
<body>
<div class="container">
    <h1>${title}</h1>
    <h2>${num}</h2>
    <p id="hits"> ${correct} / ${total} </p>
    <hr>
    <div class="btn-group" role="group" aria-label="Basic example">
        <button id="btn_pre"  type="button" class="btn btn-secondary" >上一題</button>
        <button id="btn_ran"  type="button" class="btn btn-secondary" >隨機</button>
        <button id="btn_next" type="button" class="btn btn-secondary" >下一題</button>
    </div>
    <br>
    <br>  
    <p>${content}</p>
    <br>
    <#list opts as opt>
        <div class="form-check">
            <input class="form-check-input opt" type="checkbox" value="${opt?index}" id="defaultCheck${opt?index}">
            <label class="form-check-label" for="defaultCheck${opt?index}">
                ${opt}
            </label>
        </div>
        <br>
    </#list>  
    <br>
    <div id="result"></div>
    <br>
    <button id="show_ans" type="button" class="btn btn-primary btn-lg btn-block" >Submit</button>
    <br>
   

</div>

<script>

$(function() {
    console.log("onready");
    $("#btn_pre").bind("click",  function(e){ window.location = "/v0.1/exam/${path}/question/${pre}"; });
    $("#btn_ran").bind("click",  function(e){ window.location = "/v0.1/exam/${path}/question"; });
    $("#btn_next").bind("click", function(e){ window.location = "/v0.1/exam/${path}/question/${next}"; });
    $("#show_ans").bind("click", showAns);
});


function showAns(){
    const ans = "${ans}";
    console.log("Ans is " +ans);

    const chars = ['A','B','C','D','E','F','G','H'];
    let isCorrect = true;
    let i = 0;
    $(".opt").each(function(){
        if( !ans.includes(chars[i]) && $(this).prop("checked") ){
            isCorrect = false;
        }

        if( ans.includes(chars[i]) && !$(this).prop("checked") ){
            isCorrect = false;
        }

        i++;
    });
    

    console.log(isCorrect);
    if(isCorrect){
        $("#result").html('<div class="alert alert-success" role="alert">正確</div>');
        $.get("/v0.1/exam/${path}/correct/1", refreshHits);
        $('#show_ans').prop('disabled', true);
    }else{
        $("#result").html('<div class="alert alert-danger" role="alert">錯誤</div>');
        $.get("/v0.1/exam/${path}/correct/0", refreshHits);
    }

}

function refreshHits(e){
    console.log(e);
    $("#hits").html(e.correct + " / " + e.total);

}

</script>
</body>
</html>