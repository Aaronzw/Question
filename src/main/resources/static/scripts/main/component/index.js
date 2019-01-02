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
            window.location.reload()
        }).always(function () {
            bSubmit = false;
        });
    });

    $(".preview_table").on("click", "textarea", function () {

        var obj = $(this);
        var title = "评分理由";
        //var index = obj.parent(".childdiv").parent(".fudiv").index();
        //if (index >= 0) {
        //    var prev = obj.parents(".formcontent").prev();
        //    title = $.trim(prev.children("div").eq(index).html());
        //}
        var name = $.trim(obj.attr("name"));

        if (name == "EgoReason") {
            title = "自我评估理由";
        }
        if (name == "SuperviReason") {
            title = "主管评分理由";
        }
        if (name == "SuperviIdea") {
            title = "主管建议";
        }
        var width = obj.width();
        if (width <= 650) {
            layer.prompt({
                title: title, formType: 2, value: obj.val(), area: ['550px', '325px'],
                btn: ["确定"],
                closeBtn: 1,
                yes: function (index, layero) {
                    var text = layero.find(".layui-layer-input").val();
                    obj.val($.trim(text));
                    layer.close(index);
                }
            });
        }
    });
});


