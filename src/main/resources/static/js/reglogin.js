layui.config({

}).use(['sliderVerify', 'jquery', 'form','common'], function() {
    var sliderVerify = layui.sliderVerify,
        form = layui.form,
        $ = layui.jquery,
        common=layui.common;
    var slider_reg = sliderVerify.render({
        elem: "#slider_reg",
        onOk: function(){//当验证通过回调
            layer.msg("滑块验证通过");
        }
    })
    var slider_log = sliderVerify.render({
        elem: "#slider_log",
        onOk: function(){//当验证通过回调
            layer.msg("滑块验证通过");
        }
    })
    // $(".js-regBtn").on("click",function () {
    //     var username=$("input[name='username1']").val();
    //     var password=$("input[name='pswd1']").val();
    //     console.log(username+","+password);
    //     if(username==""||password==""){
    //         layui.msg("用户名或密码不能为空！");
    //         return false;
    //     }
    //     if(!slider_reg.isOk()){
    //         layer.msg("请先通过滑块验证");
    //         return false;
    //     }
    //     common.ajax("/reg/",{
    //         "username":username,
    //         "password":password
    //     },function (result) {
    //         console.log(result)
    //     })
    // })
    // $(".js-logBtn").on("click",function () {
    //     var username=$("input[name='username2']").val();
    //     var password=$("input[name='pswd2']").val();
    //     console.log(username+","+password);
    //     if(username==""||password==""){
    //         layui.msg("用户名或密码不能为空！");
    //         return false;
    //     }
    //     if(!slider_log.isOk()){
    //         layer.msg("请先通过滑块验证");
    //         return false;
    //     }
    //     common.ajax("/login/",{
    //         "username":username,
    //         "password":password
    //     },function (result) {
    //         console.log(result)
    //     });
    // });

    //监听提交
    form.on('submit(reg_form)', function(data) {
        if(!slider_reg.isOk()){//用于表单验证是否已经滑动成功
            layer.msg("请先通过滑块验证");
        }
        return true;
    });
    form.on('submit(log_form)', function(data) {
        if(!slider_log.isOk()){//用于表单验证是否已经滑动成功
            layer.msg("请先通过滑块验证");
        }
        return true;
    });
});