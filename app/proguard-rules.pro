# Preserve reflection metadata used by Gson and useful source locations in
# Crashlytics stack traces. R8 can still optimize and shrink application code.
-keepattributes Signature,InnerClasses,EnclosingMethod,*Annotation*,SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Strip Android log calls from minified release builds. Debug builds are not
# minified, so their Log output remains available during development.
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
    public static int wtf(...);
}

# ForceUpdateConfig is read and written through Gson using field names as JSON
# keys (the remote config payload has no @SerializedName annotations).
-keepclassmembers class com.beta.soft.utils.ForceUpdateConfig {
    <fields>;
}

# Ktor uses ManagementFactory to detect debuggers, which isn't available on Android.
-dontwarn java.lang.management.**
-dontwarn io.ktor.util.debug.IntellijIdeaDebugDetector

# SLF4J is a logging facade; it looks for a binder at runtime that might not be there.
-dontwarn org.slf4j.impl.StaticLoggerBinder
