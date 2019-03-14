//封装ajax
layui.define(['jquery','layer'], function(exports){
    var $ = layui.jquery;
    var layer=layui.layer;
    var obj = {
        ajax: function (url, data, callback) {
            $.ajax({
                url: url,
                type: "post",
                dataType:"json",
                data: data,
                success: callback,

            });
        }
    };
    //提问弹窗

    //输出接口
    exports('common', obj);
});