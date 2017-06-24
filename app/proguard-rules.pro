#报错级别，容错率最高
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontoptimize
-verbose
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
#保留行号
-keepattributes SourceFile,LineNumberTable
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepclasseswithmembernames class * {
native <methods>;
}

-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

#保留libs
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *;}

-dontwarn com.google.**
-keep class com.google.** { *;}

-dontwarn org.apache.**
-keep class org.apache.** { *;}

-dontwarn com.umeng.socialize.**
-keep class com.umeng.socialize.** { *;}

-dontwarn com.tencent.**
-keep class com.tencent.** { *;}

#新浪分享
-dontwarn com.sina.**
-keep class com.sina.** { *;}

-dontwarn we.video.wevideo.bean.**
-keep class we.video.wevideo.bean.** { *;}

# imageLoader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *;}

# volley
-dontwarn com.android.volley.**
-keep class com.android.volley.** { *;}

#保留bean
-keep class com.bsk.sugar.bean.** { *;}
-keep class com.bsk.sugar.huanxin.** { *;}

# Gson
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}

#侧滑
-dontwarn we.video.wevideo.ui.slidingmenu.**
-keep class we.video.wevideo.ui.slidingmenu.** { *;}

-dontwarn com.android.support.**
-keep class com.android.support.** { *; }
-keep public class * extends android.support.**
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment