/*
 * Copyright 2009-2011 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <sys/ioctl.h>

//#include <mtd/mtd-user.h>

#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>

#include <linux/fb.h>
#include <linux/kd.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <sys/time.h>

#include "SerialPort.h"

#include "android/log.h"

static const char *TAG = "serial_port";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

static speed_t getBaudrate(jint baudrate) {
    switch (baudrate) {
        case 0:
            return B0;
        case 50:
            return B50;
        case 75:
            return B75;
        case 110:
            return B110;
        case 134:
            return B134;
        case 150:
            return B150;
        case 200:
            return B200;
        case 300:
            return B300;
        case 600:
            return B600;
        case 1200:
            return B1200;
        case 1800:
            return B1800;
        case 2400:
            return B2400;
        case 4800:
            return B4800;
        case 9600:
            return B9600;
        case 19200:
            return B19200;
        case 38400:
            return B38400;
        case 57600:
            return B57600;
        case 115200:
            return B115200;
        case 230400:
            return B230400;
        case 460800:
            return B460800;
        case 500000:
            return B500000;
        case 576000:
            return B576000;
        case 921600:
            return B921600;
        case 1000000:
            return B1000000;
        case 1152000:
            return B1152000;
        case 1500000:
            return B1500000;
        case 2000000:
            return B2000000;
        case 2500000:
            return B2500000;
        case 3000000:
            return B3000000;
        case 3500000:
            return B3500000;
        case 4000000:
            return B4000000;
        default:
            return -1;
    }
}

/*
 * Class:     android_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject
JNICALL Java_com_advantech_advfuntest_SerialPort_open
        (JNIEnv * env, jclass
thiz,
jstring path, jint
baudrate,
jint flags
)
{
int fd;
speed_t speed;
jobject mFileDescriptor;

/* Check arguments */
{
speed = getBaudrate(baudrate);
if (speed == -1) {
/* TODO: throw an exception */
//LOGE("Invalid baudrate");
return
NULL;
}
}

/* Opening device */
{
jboolean iscopy;
const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
//LOGD("Opening serial port %s with flags 0x%x", path_utf, O_RDWR | flags);
fd = open(path_utf, O_RDWR | flags);
//LOGD("open() fd = %d", fd);
(*env)->
ReleaseStringUTFChars(env, path, path_utf
);
if (fd == -1)
{
/* Throw an exception */
//LOGE("Cannot open port");
/* TODO: throw an exception */
return
NULL;
}
}

/* Configure device */
{
struct termios cfg;
//LOGD("Configuring serial port");
if (
tcgetattr(fd,
&cfg))
{
//LOGE("tcgetattr() failed");
close(fd);
/* TODO: throw an exception */
return
NULL;
}

cfmakeraw(&cfg);
cfsetispeed(&cfg, speed);
cfsetospeed(&cfg, speed);

if (
tcsetattr(fd, TCSANOW,
&cfg))
{
//LOGE("tcsetattr() failed");
close(fd);
/* TODO: throw an exception */
return
NULL;
}
}

/* Create a corresponding file descriptor */
{
jclass cFileDescriptor = (*env)->FindClass(env, "java/io/FileDescriptor");
jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor, "<init>", "()V");
jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor, "descriptor", "I");
mFileDescriptor = (*env)->NewObject(env, cFileDescriptor, iFileDescriptor);
(*env)->
SetIntField(env, mFileDescriptor, descriptorID, (jint)
fd);
}

return
mFileDescriptor;
}

/*
 * Class:     cedric_serial_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_com_advantech_advfuntest_SerialPort_close
(JNIEnv
*env,
jobject thiz
)
{
jclass SerialPortClass = (*env)->GetObjectClass(env, thiz);
jclass FileDescriptorClass = (*env)->FindClass(env, "java/io/FileDescriptor");

jfieldID mFdID = (*env)->GetFieldID(env, SerialPortClass, "mFd", "Ljava/io/FileDescriptor;");
jfieldID descriptorID = (*env)->GetFieldID(env, FileDescriptorClass, "descriptor", "I");

jobject mFd = (*env)->GetObjectField(env, thiz, mFdID);
jint descriptor = (*env)->GetIntField(env, mFd, descriptorID);

//LOGD("close(fd = %d)", descriptor);
close(descriptor);
}

JNIEXPORT void JNICALL
Java_com_advantech_advfuntest_ExecCmd_flash_1to_1file
(JNIEnv
*env,
jobject thiz
)
{
int fd, open_flag;
off_t offset = strtoll("0xE0000", NULL, 0);
size_t len = strtoul("0x1000", NULL, 0);
//LOGE("in read fun");
const char *filename = "/data/data/com.advantech.adv/flag";
enum {
    OPT_INFO,
    OPT_READ,
    OPT_WRITE,
    OPT_ERASE
} option = OPT_INFO;

//LOGE("in read fun 2	");
open_flag = (option == OPT_INFO || option == OPT_READ) ? O_RDONLY : O_RDWR;
if ((
fd = open("/dev/mtd/mtd0", O_SYNC | open_flag)
) < 0)
printf("open()");

u_int8_t *buf = NULL;
int outfd, err;
int size = len * sizeof(u_int8_t);
int n = len;

//LOGE("in read fun 3	");
//LOGE("%d\n",offset);
if (offset !=
lseek(fd, offset, SEEK_SET
)) {
//LOGE("lseek()");
goto
err0;
}
//LOGE("in read fun 4	");
outfd = creat(filename, 0666);
if (outfd < 0) {
//LOGE("creat()");
goto
err1;
}
//LOGE("in read fun 5	");

retry:
if ((
buf = (u_int8_t *) malloc(size)
) == NULL) {
#define BUF_SIZE    (64 * 1024 * sizeof(u_int8_t))
fprintf(stderr,
"%s: malloc(%#x)\n", __func__, size);
if (size != BUF_SIZE) {
size = BUF_SIZE;
fprintf(stderr,
"%s: trying buffer size %#x\n", __func__, size);
goto
retry;
}
perror("malloc()");
goto
err0;
}
do {
if (n <= size)
size = n;
err = read(fd, buf, size);
if (err < 0) {
fprintf(stderr,
"%s: read, size %#x, n %#x\n", __func__, size, n);
//LOGE("read()");
goto
err2;
}
err = write(outfd, buf, size);
if (err < 0) {
fprintf(stderr,
"%s: write, size %#x, n %#x\n", __func__, size, n);
//LOGE("write()");
goto
err2;
}
if (err != size) {
fprintf(stderr,
"Couldn't copy entire buffer to %s. (%d/%d bytes copied)\n", filename, err, size);
goto
err2;
}
n -=
size;
} while (n > 0);

if (buf != NULL)
free(buf);
close(outfd);
//printf("Copied %zu bytes from address 0x%.8"PRIxoff_t" in flash to %s\n", len, offset, filename);
return;

err2:
        close(outfd);
err1:
if (buf != NULL)
free(buf);
err0:
return;
}


JNIEXPORT void JNICALL
Java_com_advantech_advfuntest_SerialPort_frabuffer_1rgb
(JNIEnv
*env,
jobject thiz
)
{
const char *devfile = "/dev/graphics/fb0";
long int screensize = 0;
int fbFd = 0;


/* Open the file for reading and writing */
fbFd = open(devfile, O_RDWR);
if (fbFd == -1)
{
perror ("Error: cannot open framebuffer device");
exit (1);
}

//获取finfo信息并显示
if (
ioctl (fbFd, FBIOGET_FSCREENINFO,
&finfo) == -1)
{
perror ("Error reading fixed information");
exit (2);
}

if (
ioctl (fbFd, FBIOGET_VSCREENINFO,
&vinfo) == -1)
{
perror ("Error reading variable information");
exit (3);
}

/* Figure out the size of the screen in bytes */
screensize = finfo.smem_len;

/* Map the device to memory */
frameBuffer =
(char *) mmap(0, screensize, PROT_READ | PROT_WRITE, MAP_SHARED,
              fbFd, 0);
if (frameBuffer == MAP_FAILED)
{
perror ("Error: Failed to map framebuffer device to memory");
exit (4);
}

printf ("Will draw 3 rectangles on the screen,\n"
"they should be colored red, green and blue (in that order).\n");
drawRect (0, 0, vinfo.xres, vinfo.yres, 0xffff0000);
sleep (2);
drawRect (0, 0, vinfo.xres, vinfo.yres, 0xff00ff00);
sleep (2);
drawRect (0, 0, vinfo.xres, vinfo.yres, 0xff0000ff);
sleep (2);


munmap (frameBuffer, screensize
);    //解除内存映射，与mmap对应

close (fbFd);
}

