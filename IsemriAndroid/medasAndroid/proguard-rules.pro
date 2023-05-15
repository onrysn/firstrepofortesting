# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-ignorewarnings

# Tüm sınıflar açık
-keep class * {
    public protected *;
}


-keep class mobit.elec.mbs.MbsServer
-keep class mobit.elec.mbs.medas.android.AndroidModule
-keep class com.mobit.BluetoothPrinter
-keep class mobit.printer.bixolon.Bixolon
-keep class mobit.printer.bixolon.BixolonAndroid
-keep class mobit.printer.bixolon.CPCL
-keep class mobit.printer.CPCL

#------------------------------------------------------------------------------
# Optik port

-keep class mobit.eemr.IMeterReader {
    public *;
}
-keep class mobit.eemr.IBluetooth {
    public *;
}
-keep class mobit.eemr.IProbeEvent {
    public *;
}
-keep class mobit.eemr.IReadEvent {
    public *;
}
-keep class mobit.eemr.ICallback {
    public *;
}

-keep class * implements mobit.eemr.IMeterReader {
    public protected *;
}
-keep class * extends mobit.eemr.IBluetooth {
    public protected *;
}
-keep class * implements mobit.eemr.IProbeEvent {
    public protected *;
}
-keep class * implements mobit.eemr.IReadEvent {
    public protected *;
}
-keep class * implements mobit.eemr.ICallback {
    public protected *;
}
-keep class mobit.eemr.MbtProbePowerStatus {
    public protected *;
}
-keep class mobit.eemr.MbtMeterInformation {
    public procted *;
}


#------------------------------------------------------------------------------

-keep class * implements com.mobit.IDb {
    public static *;
}

-keep class * implements com.mobit.IEnum {
    public static *;
}

-keep class mobit.elec.android.VeritabaniSifirla {
    public *;
}

-keep class * implements org.ksoap2.x.serialization.KvmSerializable {
    public *;
}

-keep class * extends mobit.android.ViewHolderEx {
    public *;
}

#------------------------------------------------------------------------------
# K�t�phaneler

-keep class org.ksoap2.* {
    public protected *;
}
-keep class org.sqldroid.* {
    public protected *;
}

-keep class com.google.* {
    public protected *;
}

-keep class com.google.zxing.client.android.* {
    public protected *;
}

#------------------------------------------------------------------------------