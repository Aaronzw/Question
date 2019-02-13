$(document).ready(function () {

    $("#paginate").pagination({
        pageSize: 10,
        total: 1000,
        pageIndex:1,
    });
    $("#page").on("pageClicked", function (event, data) {
        //分页按钮点击事件
        console.log(data)
    });
    $(".js-submitAnswer").on("click",function () {
        var content=$(".js-addComment").val();
        var  qid=$('input[name="questionId"]').val();
        var userId=$('input[name="uid"]').val();
        if(console==""){
            alert("回答不能为空！");
            return
        }
        var flag=false;
        if(flag){
            return false;
        }
        flag=true;

        $.ajax({
            url: '/comment/add',
            type: 'post',
            data: {"questionId":qid,"content":content,"uid":userId},
            dataType: 'json'
        }).done(function (oResult) {
            // 未登陆，跳转到登陆页面
            console.log(oResult);
            if(oResult.code==0){
                alert("发表回答成功！");
                window.location.reload()
            }else {
                alert("发表回答失败！"+oResult.msg);
                window.location.re()
            }
            window.location.href = "http://www.baidu.com";
        }).fail(function () {
            alert('出现错误，请重试');
            window.location.reload();
        }).always(function () {
            bSubmit = false;
        });
    });
});


