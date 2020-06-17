<!DOCTYPE html>
<html>
<#include "/header.ftl">
<body>
<div class="container">
    <h1>${title}</h1>
    <h2>${num}</h2>
    <hr>
    <div class="btn-group" role="group" aria-label="Basic example">
        <button type="button" class="btn btn-secondary" onclick="onClick(${pre});">上一題</button>
        <button type="button" class="btn btn-secondary" onclick="onClick(0);">隨機</button>
        <button type="button" class="btn btn-secondary" onclick="onClick(${next});">下一題</button>
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
    <button id="show_ans" type="button" class="btn btn-primary btn-lg btn-block" onclick="showAns();">顯示答案</button>
    <br>
   

</div>
</body>
<script>
function onClick(page){
    console.log(page);
    if(page==0)
        window.location = "/v0.1/exam/BigData_20200413/question";
    else
        window.location = "/v0.1/exam/BigData_20200413/question/"+page;
}

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
    }else{
        $("#result").html('<div class="alert alert-danger" role="alert">錯誤</div>');
    }

}

</script>
</html>