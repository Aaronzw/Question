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