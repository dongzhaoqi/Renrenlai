package com.siti.renrenlai.util;

/**
 * Created by Dong on 2016/4/5.
 */
public class ConstantValue {

    /*本机测试*/
//   public static final String urlRoot = "http://192.168.1.100:8080/RenrenLai";
//    public static final String urlRoot = "http://10.1.40.76:8080/RenrenLai";
 //   public static final String urlRoot = "http://10.1.1.165:8080/RenrenLai";

    /*外网测试*/
    public static final String urlRoot = "http://116.228.3.125/RenrenLai";

    /*缓存目录*/
    public static final String ENVIRONMENT_DIR_CACHE = "/activityCache";

    public static final String APPKEY = "105e175f4413e";
    public static final String APPSECRET = "c70863113ae6d83a2867234b5f9f25da";

    //用户登录
    public static final String USER_LOGIN = urlRoot + "/loginForApp";

    //更改用户昵称
    public static final String UPDATE_USER_NAME = urlRoot + "/updateUserName";

    //更改用户性别
    public static final String UPDATE_USER_GENDER = urlRoot + "/updateUserGender";

    //更改用户兴趣
    public static final String UPDATE_USER_HOBBY = urlRoot + "/updateUserHobby";

    //更改用户简介
    public static final String UPDATE_USER_INTRODUCTION = urlRoot + "/updateUserIntroduction";

    //获取活动列表
    public static final String GET_ACTIVITY_LIST = urlRoot + "/getActivityListForApp";

    //获取活动详情
    public static final String GET_ACTIVITY_INFO = urlRoot + "/getActivityDynamicInfoForApp";

    //上传图片
    public static final String UPLOAD_IMAGES = urlRoot + "/myupload";

    //报名活动
    public static final String PARTICIPATE_ACTIVITY = urlRoot + "/participateActivity";

    //评论活动
    public static final String COMMENT_ACTIVITY = urlRoot + "/insertCommentForApp";

    //喜欢该活动
    public static final String LOVE_THIS_ACTIVITY = urlRoot + "/loveThisActivityForApp";

    //发起活动
    public static final String LAUNCH_ACTIVITY = urlRoot + "/launchActivityForApp";

    //调用库图片
    public static final String GET_IMAGES_FROM_LIB = urlRoot + "/getImagePreForAppList";

    //获取我喜欢的活动
    public static final String GET_LOVED_ACTIVITY_LIST = urlRoot + "/getLovedActivityList";

    //获取我发起的活动
    public static final String GET_PUBLISH_ACTIVITY_LIST = urlRoot + "/getPublishActivityList";

    //获取我报名的活动
    public static final String GET_PARTICIPATE_ACTIVITY_LIST = urlRoot + "/getParticipateActivityList";

    //获取用户发起的项目
    public static final String GET_LAUNCHED_PROJECTS = urlRoot + "/getLaunchedProjects";

    //发起项目意愿
    public static final String LAUNCH_PROJECT_INTENTION = urlRoot + "/launchProjectIntention";

    //喜欢该项目
    public static final String LOVE_THIS_PROJECT = urlRoot + "/loveThisProjectForApp";

    //获取项目列表
    public static final String GET_PROJECT_LIST = urlRoot + "/getProjectListForApp";

    //获取项目详情
    public static final String GET_PROJECT_INFO = urlRoot + "/getProjectInfoForApp";


}
