//封装ajax
layui.define(['jquery'], function(exports){
    var $ = layui.jquery;
    var obj = {
        ajax: function (url, data, callback) {
            $.ajax({
                url: url,
                type: "post",
                dataType:"json",
                data: data,
                success: callback
            });
        }
    };
    //输出接口
    exports('common', obj);
});