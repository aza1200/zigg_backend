package soma.achoom.zigg.s3.entity

enum class S3DataType(val path:String) {
    HISTORY_THUMBNAIL("thumbnail/history/"),
    HISTORY_VIDEO("video/history/"),
    SPACE_IMAGE("image/space/"),
    USER_BANNER_IMAGE("banner/"),
    USER_PROFILE_IMAGE("profile/"),
    POST_IMAGE("image/post/"),
    POST_VIDEO("video/post/"),
    POST_THUMBNAIL("thumbnail/post/"),
    POST_REPORT("report/post/"),
    COMMENT_REPORT("report/comment/"),
    USER_REPORT("report/user/");
    fun getBucketName():String{
        return path
    }
}