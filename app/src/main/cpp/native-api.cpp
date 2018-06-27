#include <jni.h>
#include <string>
#include <fstream>
#include <google/protobuf/io/zero_copy_stream_impl_lite.h>
#include <google/protobuf/io/coded_stream.h>

#include <android/log.h>


#define  LOG_TAG    "native-api"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

void messsage_callback(char *buf, int len, jint i);


google::protobuf::Message* createMessage(const std::string& typeName)
{
    google::protobuf::Message* message = NULL;
    const google::protobuf::Descriptor* descriptor = google::protobuf::DescriptorPool::generated_pool()->FindMessageTypeByName(typeName);
    if (descriptor)
    {
        const google::protobuf::Message* prototype = google::protobuf::MessageFactory::generated_factory()->GetPrototype(descriptor);
        if (prototype)
        {
            message = prototype->New();
        }
    }
    return message;
}

const char *method_class_from_java = "com/xc/platform/protocol/support/PlatformProvider";
const char *method_name_from_java = "notifyMsgCallBack";
static JavaVM* javaVm = NULL;
static jobject mInstance = 0;
static jmethodID mMethodID = 0;

void static_messsage_callback(char *buf, int len, jint msgType)
{
    if (javaVm == NULL)
        return;

    int status;
    JNIEnv *env = NULL;
    bool isAttached = false;

    status = javaVm->GetEnv((void**) &env, JNI_VERSION_1_6);
    if(status < 0)
    {
        status = javaVm->AttachCurrentThread(&env, NULL);
        if(status < 0)
        {
            return;
        }
        isAttached = true;
    }

    jclass cls = env->GetObjectClass(mInstance);
    if (cls != 0)
    {
        jmethodID mid = env->GetStaticMethodID(cls, "notifyMsgCallBack1", "([BI)V");
        if (mid != 0)
        {
            jbyte* byte = (jbyte*)buf;
            jbyteArray jarray = env->NewByteArray(len);
            env->SetByteArrayRegion(jarray, 0, len, byte);

            env->CallStaticVoidMethod(cls, mid, jarray,msgType);
        }
    }

    if (isAttached)        // From native thread
    {
        javaVm->DetachCurrentThread();
    }
}
void messsage_callback(char *buf, int len, jint msgType)
{
    if (javaVm == NULL)
        return;

    int status;
    JNIEnv *env = NULL;
    bool isAttached = false;

    status = javaVm->GetEnv((void**) &env, JNI_VERSION_1_6);
    if(status < 0)
    {
        status = javaVm->AttachCurrentThread(&env, NULL);
        if(status < 0)
        {
            return;
        }
        isAttached = true;
    }

    jbyte* byte = (jbyte*)buf;
    jbyteArray jarray = env->NewByteArray(len);
    env->SetByteArrayRegion(jarray, 0, len, byte);

    env->CallVoidMethod(mInstance, mMethodID, jarray,msgType);

    if (isAttached)        // From native thread
    {
        javaVm->DetachCurrentThread();
    }
}
//通过类实例访问JAVA方法
static void GetInterfaceObject(JNIEnv *env, const char *path, jobject *objptr) {
    //找到类的对象
    jclass cls = env->FindClass(path);
    if(!cls) {
        return;
    }
    //获取类的默认构造方法ID
    jmethodID constr = env->GetMethodID(cls, "<init>", "()V");
    if(!constr) {
        return;
    }
    //查找实例方法的ID
    jmethodID mid = env->GetMethodID(cls, method_name_from_java, "([BI)V");
    if(!mid) {
        return;
    }
    mMethodID = mid;
    //创建类的实例
    jobject obj = env->NewObject(cls, constr);
    if(!obj) {
        return;
    }
    (*objptr) = env->NewGlobalRef(obj);
    env->DeleteLocalRef(cls);
}
int JniLoad(JavaVM* jvm, void* reserved)
{
    javaVm = jvm;

    JNIEnv *env;
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    GetInterfaceObject(env, method_class_from_java, &mInstance);
    return JNI_VERSION_1_6;
}

void JniUnLoad(JavaVM* jvm, void* reserved)
{
    JNIEnv *env;
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return;
    }

    env->DeleteGlobalRef(mInstance);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_xc_platform_protocol_support_PlatformProvider_platformInit(JNIEnv *env, jobject instance) {
    env->GetJavaVM(&javaVm);
    JniLoad(javaVm, 0);
    LOGD( "===========app_init ============\n");
    return 1;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_xc_platform_protocol_support_PlatformProvider_setPackageName(JNIEnv *env, jobject instance,
                                                             jbyteArray packageName_, jint pnLen,
                                                             jbyteArray metholdName_, jint mnLen) {
    jbyte *packageName = env->GetByteArrayElements(packageName_, NULL);
    jbyte *metholdName = env->GetByteArrayElements(metholdName_, NULL);

//    method_class_from_java = new char[pnLen];
//    method_name_from_java = new Byte[mnLen];

//    memset(method_class_from_java, 0, pnLen);
//    memset(method_name_from_java, 0, mnLen);

//    memcpy(method_class_from_java, packageName, pnLen);
//    memcpy(method_name_from_java, metholdName, mnLen);

    env->ReleaseByteArrayElements(packageName_, packageName, 0);
    env->ReleaseByteArrayElements(metholdName_, metholdName, 0);

    return 1;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_xc_platform_protocol_support_PlatformProvider_appSendMsg(JNIEnv *env, jobject instance,
                                                         jbyteArray pPara_, jint len, jint srcPid,
                                                         jint destPid, jint msgType,
                                                         jstring msgName_) {

    jbyte *pPara = env->GetByteArrayElements(pPara_, NULL);
    const char *msgName = env->GetStringUTFChars(msgName_, 0);

    google::protobuf::Message *pMessage = createMessage(msgName);
    if(pMessage == NULL) return -1;
    pMessage->ParseFromArray(pPara, len);

//    sendInnerMsg(destPid, srcPid, msgType, pMessage);

    messsage_callback((char *) pPara, len, msgType);

    env->ReleaseByteArrayElements(pPara_, pPara, 0);
    env->ReleaseStringUTFChars(msgName_, msgName);
    return 0;
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_xc_platform_protocol_support_PlatformProvider_getPlatformProviderInsance(JNIEnv *env, jobject instance) {
    return mInstance;
}

