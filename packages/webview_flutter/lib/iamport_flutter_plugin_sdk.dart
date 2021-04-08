
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:webview_flutter/iamport_response.dart';

class IamportFlutterPluginSdk {
  static const MethodChannel _channel =
      const MethodChannel('iamport_flutter_plugin_sdk');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> showNativeWebView() async {
    await _channel.invokeMethod('ShowNativeWebView');
  }

  static Future<String?> payment(String userCode, String requestJsonString) async {
    return await _channel.invokeMethod("payment", <String, dynamic>{
      "userCode": userCode,
      "request": requestJsonString,
    });
  }

  static Future<String?> certification(String userCode, String requestJsonString) async {
    return await _channel.invokeMethod("certification", <String, dynamic>{
      "userCode": userCode,
      "request": requestJsonString,
    });
  }

}
