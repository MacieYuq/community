function like(btn, entityType, entityId, entityUserId) {
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType":entityType, "entityId":entityId, "entityUserId":entityUserId},//controller层的like方法携带的参数
        function (data) {
            data = $.parseJSON(data);//解析controller里传来的json字符串
            if(data.code == 0) {
                $(btn).children("i").text(data.likeCount)
                $(btn).children("b").text(data.likeStatus==1?'已赞':"赞");
            }else {
                alert(data.msg);
            }
        }
    )
}