//
// Created by liangmingjie on 2016/3/9.
//





#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

#define BUFER_SIZE 10240

JNIEXPORT jstring
Java_cn_donica_slcd_polling_NtscService_shellRun(JNIEnv
*env,
jobject thiz
) {
printf("Hello World!");
system("ls");
system("/system/bin/mxc-v4l2-tvin -x 0 -ct 27 -cl 0 -cw 720 -ch 480 -ot 0 -ol 0 -ow 1280 -oh 800 -m 2 -tb 1");
return (*env)->
NewStringUTF(env,
"Hello from JNI! ");
}


JNIEXPORT jstring
Java_cn_donica_slcd_polling_NtscService_command(JNIEnv
*env,
jobject thiz,
        jstring
cmd) {
const jbyte *p1 = (*env)->GetStringUTFChars(env, cmd, NULL);
char *str2 = (char *) p1;
FILE *fp;
char buffer[BUFER_SIZE] = {0};
memset(buffer,
0, BUFER_SIZE);
fp = popen(str2, "r");
fread(buffer,
BUFER_SIZE - 1, 1, fp);
buffer[BUFER_SIZE - 1] = 0;
pclose(fp);
return (*env)->
NewStringUTF(env, buffer
);
}

//JNIEXPORT jstring Java_cn_donica_slcd_polling_NtscService_sysCmd1(JNIEnv* env,jobject thiz, jstring str , jstring path)
//{
//    const jbyte* p1 = (*env)->GetStringUTFChars(env , str, NULL);
//    const jbyte* p2 = (*env)->GetStringUTFChars(env , path, NULL);
//    char* str2=(char *)p1;
//    char* path2=(char *)p2;
//    strcat(str2," > ");
//    strcat(str2,path2);
//    system(str2);
//    return(*env)->NewStringUTF(env,str2);
//}

JNIEXPORT jstring
Java_cn_donica_slcd_polling_NtscService_sysCmd1(JNIEnv
*env,
jobject thiz,
        jstring
str) {

const jbyte *p1 = (*env)->GetStringUTFChars(env, str, NULL);
char *str2 = (char *) p1;
system(str2);

return (*env)->
NewStringUTF(env, str2
);

}


JNIEXPORT jstring
Java_cn_donica_slcd_polling_NtscService_sysCmd2(JNIEnv
*env,
jobject thiz,
        jstring
str) {

const jbyte *p1 = (*env)->GetStringUTFChars(env, str, NULL);
char *str2 = (char *) p1;
//    system(str2);


//    int result = execv("/system"," -x 0 -ct 27 -cl 0 -cw 720 -ch 480 -ot 0 -ol 0 -ow 1280 -oh 800 -m 2 -tb 1") ;
//   // execv("",str2);
//    char *s tr3 = (char)result;



char *str3;
int result;
char *argv[] = {"pwd"};

if (

vfork()

== 0) {
printf("execv\n");
result = execv("/system", argv);
return (*env)->
NewStringUTF(env,
"Liang");

} else {

printf("This is the parent process\n");
return (*env)->
NewStringUTF(env,
"Ming");
}
str3 = (char) result;
return (*env)->
NewStringUTF(env,
"jie");
}

