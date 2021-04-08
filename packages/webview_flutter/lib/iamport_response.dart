import 'dart:convert';

class IamPortResponse {
  bool? imp_success = false;
  bool? success = false;
  String? imp_uid;
  String? merchant_uid;
  String? error_msg = null;
  String? error_code;

  IamPortResponse(this.imp_success, this.success, this.imp_uid,
      this.merchant_uid, this.error_msg, this.error_code);

  IamPortResponse.fromJson(Map<String, dynamic> data)
      : imp_success = data['imp_success'],
        success = data['success'],
        imp_uid = data['imp_uid'],
        merchant_uid = data['merchant_uid'],
        error_msg = data['error_msg'],
        error_code = data['error_code'];

  String toJsonString() {
    Map<String, dynamic> jsonData = {
      'imp_success': imp_success,
      'success': success,
      'imp_uid': imp_uid,
      'merchant_uid': merchant_uid,
      'error_msg': error_msg,
      'error_code': error_code
    };

    if (imp_success != null) {
      jsonData['imp_success'] = imp_success;
    }

    if (success != null) {
      jsonData['success'] = success;
    }

    if (imp_uid != null) {
      jsonData['imp_uid'] = imp_uid;
    }

    if (merchant_uid != null) {
      jsonData['merchant_uid'] = merchant_uid;
    }

    if (error_msg != null) {
      jsonData['error_msg'] = error_msg;
    }

    if (error_code != null) {
      jsonData['error_code'] = error_code;
    }

    return jsonEncode(jsonData);
  }
}
