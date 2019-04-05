/*
    全局js文件,拓展包
*/
layui.config({
    base: '../js/'      //自定义layui组件的目录
}).extend({ //设定组件别名
    common:'common',
    sliderVerify:'sliderVerify/sliderVerify'
});
/**

 @Name：wenda首页
 @Author：庄巍


 */
layui.define(['element', 'form','laypage','jquery','layer','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,layer = layui.layer
        ,common=layui.common;
    //statr 分页
    $(function () {
        initClickMore();
        initWordsLimit();
    })
    // initClickMore();
    function initWordsLimit(){
        var text=$("input[name='question_title']").val();
        var counter=text.length;
        $("#input_Statistics").text(String(counter)+"/50");
        $(document).keyup(function(){
            var text=$("input[name='question_title']").val();
            var counter=text.length;
            if(counter>=100){
                $("#input_Statistics").text("你输入的字符超出限制了！！！");
            }else
                $("#input_Statistics").text(String(counter)+"/50");

        });
    }
    function initClickMore() {
        /*弹窗事件*/
        $(".js-askq").on("click",function () {
            layer.open({
                type: 1,
                title: '提问',
                // maxmin: true,
                shadeClose: true, //点击遮罩关闭层
                area : ['500px' , '300px'],
                content: $(".addQuestionMod")
            });
        });
        $(".js-submitNewQues").on("click",function () {
            var title=$("input[name='question_title']").val();
            var content=$("textarea[name='question_content']").val();
            if(title==""||title==undefined){
                // layer.msg("请输入问题标题！");
                return
            }
            common.ajax("/question/add",{
                "content":content,
                "title":title
            },function (result) {
                console.log(result)
                if(result.code=='1'){
                    if(result.msg=="annoymous")
                    {
                        window.location="/reg"
                    }else {
                        layer.msg("发表问题出错，请联系管理员")
                    }
                    return
                }else {
                    layer.msg("发表问题成功");
                    window.location="/question/"+question_id;
                }

            })
        })
        /*回到顶部*/
        $("#to_top").on("click",function() {
            <!--$("html,body").animate({scrollTop:0}, 200);-->
            $(window).scrollTop(0);
        });
    }


    exports('global', {});
});

