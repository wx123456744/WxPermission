package com.zfkj.gamecenter.permission;


/**
 * Created by win7 on 2019/4/10.
 */

public class PermissionRationaleTable {
    private static String rationaleSplit = "\\|"; // 多个权限分隔符
    private static String[] permissionsTables = {
            /**
             * 允许一个程序访问CellID或WiFi热点来获取粗略的位置
            */
            Permission.ACCESS_COARSE_LOCATION,
            /**
             *允许一个程序访问精良位置(如GPS)
            */
            Permission.ACCESS_FINE_LOCATION,
            /**
             * 允许一个应用程序添加语音邮件系统
            */
            Permission.ADD_VOICEMAIL,
            /**
             * 访问与您的生命体征相关的传感器数据
            */
            Permission.BODY_SENSORS,
            /**
             * 允许一个程序初始化一个电话拨号不需通过拨号用户界面需要用户确认
            */
            Permission.CALL_PHONE,
            /**
             * 请求访问使用照相设备
            */
            Permission.CAMERA,
            /**
             * 访问一个帐户列表在Accounts Service中
            */
            Permission.GET_ACCOUNTS,
            /**
             * 允许程序监视、修改有关播出电话
            */
            Permission.PROCESS_OUTGOING_CALLS,
            /**
             * 允许程序读取用户日历数据
            */
            Permission.READ_CALENDAR,
            /**
             * 允许程序读取系统日志
            */
            Permission.READ_CALL_LOG,
            /**
             * 允许程序读取用户联系人数据
            */
            Permission.READ_CONTACTS,
            /**
             * 允许程序读取外部存储设备数据
            */
            Permission.READ_EXTERNAL_STORAGE,
            /**
             * 读取电话状态
            */
            Permission.READ_PHONE_STATE,
            /**
             * 读取短信内容
            */
            Permission.READ_SMS,
            /**
             * 接收彩信
            */
            Permission.RECEIVE_MMS,
            /**
             * 接收短信
            */
            Permission.RECEIVE_SMS,
            /**
             * 接收Wap Push
            */
            Permission.RECEIVE_WAP_PUSH,
            /**
             * 录制声音通过手机或耳机的麦克
            */
            Permission.RECORD_AUDIO,
            /**
             * 发送短信
            */
            Permission.SEND_SMS,
            /**
             *允许程序使用SIP视频服务
            */
            Permission.USE_SIP,
            /**
             * 写入日程
            */
            Permission.WRITE_CALENDAR,
            /**
             * 写入日志
            */
            Permission.WRITE_CALL_LOG,
            /**
             * 写入联系人
            */
            Permission.WRITE_CONTACTS,
            /**
             * 写入外部存储
            */
            Permission.WRITE_EXTERNAL_STORAGE
    };

    public static String[] analysis(String permissions_str) {
        if (permissions_str != null && !"".equals(permissions_str)) {
            String[] permissions = permissions_str.split(rationaleSplit);
            String[] result = new String[permissions.length];
            for (int index = 0; index < permissions.length; index++) {
                int permissionCode = ConversionUtils.toInt(permissions[index]);
                if (permissionCode == 0) { // 传入权限标识号为0时指代需要授权权限授权
                    result = permissionsTables;
                    break;
                }
                if (permissionCode < permissionsTables.length)
                    result[index] = permissionsTables[permissionCode - 1];
            }
            return result;
        }
        return null;
    }

    public static void main(String[] args) {
        String[] array1 = analysis("1|2|19|80");
        System.out.println("************1************");
        for (String item : array1) {
            System.out.println(item);
        }
        System.out.println("************2************");
        String[] array2 = analysis("1|0|19|80");
        for (String item : array2) {
            System.out.println(item);
        }
        System.out.println("************3************");
        String[] array3 = analysis(null);
        if (array3 != null)
            for (String item : array3) {
                System.out.println(item);
            }
    }
}
