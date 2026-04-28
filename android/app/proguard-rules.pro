# Capacitor
-keep class com.getcapacitor.** { *; }
-keep @com.getcapacitor.annotation.CapacitorPlugin public class * {
    @com.getcapacitor.annotation.PermissionCallback <methods>;
    @com.getcapacitor.annotation.ActivityCallback <methods>;
    @com.getcapacitor.annotation.Permission <fields>;
    @com.getcapacitor.PluginMethod public <methods>;
}

# App
-keep class com.puter.app.** { *; }

# WebView JavaScript interface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# General
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
