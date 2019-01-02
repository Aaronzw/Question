$(function () {
    var oExports = {
        initialize: fInitialize,
        encode: fEncode
    };
    oExports.initialize();

    function fInitialize() {
        var that = this;
        $(".js-submitQuestion").on('click',function () {
            var title=$(".js-addTitle").val();
            var content=$(".js-addContent").val();
            console.log(title+","+content);

            $.ajax({
                url: '/addcomment/',
                type: 'post',
                dataType: 'json',
                data: {image_id: sImageId, content: sCmt}
            }).done(function (oResult) {


            }).fail(function (oResult) {
                alert(oResult.msg || '提交失败，请重试');
            });
        });

    }

    function fEncode(sStr, bDecode) {
        var aReplace =["&#39;", "'", "&quot;", '"', "&nbsp;", " ", "&gt;", ">", "&lt;", "<", "&amp;", "&", "&yen;", "¥"];
        !bDecode && aReplace.reverse();
        for (var i = 0, l = aReplace.length; i < l; i += 2) {
            sStr = sStr.replace(new RegExp(aReplace[i],'g'), aReplace[i+1]);
        }
        return sStr;
    };

});



