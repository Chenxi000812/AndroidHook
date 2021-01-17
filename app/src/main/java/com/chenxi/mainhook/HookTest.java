package com.chenxi.mainhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookTest implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.easytech.iron")){
            XposedBridge.log("拦截到进程："+lpparam.processName);
            Class ecPurchase = lpparam.classLoader.loadClass("com.easytech.iron.ecPurchase");
            XposedHelpers.findAndHookMethod(ecPurchase, "getOderNoFromResult", String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("参数是："+param.args[0]);
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("返回值是："+param.getResult());
                    super.afterHookedMethod(param);
                }
            });
            Class ironActivity = lpparam.classLoader.loadClass("com.easytech.iron.IronActivity");
            XposedHelpers.findAndHookMethod(ironActivity, "ResPurchase",int.class,String.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.args[0] = 0;
                    XposedBridge.log("第一个参数："+String.valueOf(param.args[0])+" 第二个参数是："+param.args[1]);
                    super.beforeHookedMethod(param);
                }
            });

            Class ecNative = lpparam.classLoader.loadClass("com.easytech.lib.ecNative");
            XposedHelpers.findAndHookMethod(ecNative, "GetIdentityStatus", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(1);
                    super.afterHookedMethod(param);
                }
            });
        }
    }
}
