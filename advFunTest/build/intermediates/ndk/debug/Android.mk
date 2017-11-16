LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := libserial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	E:\WorkSpace\Android_Studio\Android\Donica\advFunTest\src\main\jni\Android.mk \
	E:\WorkSpace\Android_Studio\Android\Donica\advFunTest\src\main\jni\Application.mk \
	E:\WorkSpace\Android_Studio\Android\Donica\advFunTest\src\main\jni\SerialPort.c \

LOCAL_C_INCLUDES += E:\WorkSpace\Android_Studio\Android\Donica\advFunTest\src\debug\jni
LOCAL_C_INCLUDES += E:\WorkSpace\Android_Studio\Android\Donica\advFunTest\src\main\jni

include $(BUILD_SHARED_LIBRARY)
