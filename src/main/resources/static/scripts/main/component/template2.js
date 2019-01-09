$(document).ready(function () {

    //添加问题功能
    $(".js-submitQuestion").on("click",function () {
        var title=$(".js-addTitle").val();
        var content=$(".js-addContent").val();
        if(title==""){
            alert("问题标题不能为空！");
        }
        var flag=false;
        if(flag){
            return false;
        }
        flag=true;

        $.ajax({
            url: '/question/add',
            type: 'post',
            data: {"title":title,"content":content},
            dataType: 'json'
        }).done(function (oResult) {
            // 未登陆，跳转到登陆页面
            console.log(oResult);
            if(oResult.code==0){
                alert("发表问题成功！");
                window.location.reload()
            }else {
                alert("发表问题失败！"+oResult.msg);
                window.location.reload()
            }
        }).fail(function () {
            alert('出现错误，请重试');
            window.location.reload();
        }).always(function () {
            bSubmit = false;
        });
    });
});


