package plugin.iamport.webview

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.annotation.NonNull
import com.iamport.sdk.data.sdk.IamPortCertification
import com.iamport.sdk.data.sdk.IamPortRequest
import com.iamport.sdk.data.sdk.IamPortResponse
import com.iamport.sdk.data.sdk.Platform
import com.iamport.sdk.domain.core.Iamport
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugins.webviewflutter.WebViewFactory
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IamPortSdkManager(private val factory: WebViewFactory) : MethodCallHandler {

    private val TAG = "IamportFlutter"

    private lateinit var methodChannel: MethodChannel

    private var application: Application? = null
    private var activity: Activity? = null

    private val json = Json { isLenient = true }
    private lateinit var result: MethodChannel.Result

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            FLUTTERCONST.PAYMENT -> {
                call.argument<String>(FLUTTERCONST.USER_CODE)?.let { userCode ->
                    call.argument<String>(FLUTTERCONST.REQUEST)?.let { request ->
                        payment(userCode, request)
                    }
                }
                this.result = result
            }
            FLUTTERCONST.CERTIFICATION -> {
                call.argument<String>(FLUTTERCONST.USER_CODE)?.let { userCode ->
                    call.argument<String>(FLUTTERCONST.REQUEST)?.let { request ->
                        certification(userCode, request)
                    }
                }
                this.result = result
            }
            else -> result.notImplemented()
        }
    }

    fun onInstanceAtAttachedToEngine(
            messenger: BinaryMessenger,
            _applicationContext: Context
    ) {
        Log.d(TAG, "[onInstanceAtAttachedToEngine]")
        methodChannel = MethodChannel(messenger, FLUTTERCONST.CHANNEL_NAME)
        methodChannel.setMethodCallHandler(this)
        application = _applicationContext.applicationContext as Application
        create(application)
    }


    fun onInstanceAtAttachedToActivity(_activity: Activity) {
        Log.d(TAG, "[onInstanceAtAttachedToActivity]")
        activity = _activity
        Iamport.init(activity as ComponentActivity)
    }

    fun onDetachedFromActivity() {
        Log.d(TAG, "[onDetachedFromActivity]")
        methodChannel.setMethodCallHandler(null)
        activity = null
    }

    fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        Log.d(TAG, "[onReattachedToActivityForConfigChange]")
        onInstanceAtAttachedToActivity(binding.activity)
    }

    fun onAttachedToActivity(binding: ActivityPluginBinding) {
        Log.d(TAG, "[onAttachedToActivity]")
        onInstanceAtAttachedToActivity(binding.activity)
    }

    fun onDetachedFromActivityForConfigChanges() {
        Log.d(TAG, "[onDetachedFromActivityForConfigChanges]")
        onDetachedFromActivity()
    }

    private fun create(app: Application?) {
        app?.run {
            Iamport.create(this)
        } ?: run {
            Log.e(TAG, "Application 을 찾을 수 없음!")
        }
    }

    private fun payment(userCode: String, requestStr: String) {
        Log.d(TAG, requestStr)

        Iamport.setWebView(factory.flutterWebView.view as WebView)
        json.decodeFromString<IamPortRequest>(requestStr).let { request ->
            request.platform = Platform.flutter.name
            Log.d(TAG, "payment => $request")
            Iamport.payment(userCode, request) {
                receivedResult(it)
            }
        }
    }

    private fun certification(userCode: String, requestStr: String) {
        Log.d(TAG, requestStr)

        Iamport.setWebView(factory.flutterWebView.view as WebView)
        json.decodeFromString<IamPortCertification>(requestStr).let { request ->
            request.platform = Platform.flutter.name
            Log.d(TAG, "certification => $request")
            Iamport.certification(userCode, request) {
                receivedResult(it)
            }
        }
    }

    private fun receivedResult(response: IamPortResponse?) {
        runCatching {
            json.encodeToString(response).let {
                Log.d(TAG, it)
                result.success(it)
            } ?: run {
                result.success(null)
            }
        }.onFailure {
            Log.d(TAG, "runCatching Failure")
            result.success(null)
        }
    }
}