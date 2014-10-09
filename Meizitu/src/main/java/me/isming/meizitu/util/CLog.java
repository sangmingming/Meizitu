package me.isming.meizitu.util;

import android.util.Log;

/**
 * User: sam
 * Date: 11/23/13
 * Time: 11:37 AM
 */
public class CLog {
    public static final String TAG = "MEIZITU.MOBILE";
    private static String tag = TAG;
    public static int logLevel = Log.VERBOSE;
    public static boolean isDebug = true;
    private static CLog sInstance;

    private CLog()
    { }
    private CLog(String tag) {
        this.tag = tag;
    }

    public synchronized static  CLog getInstance() {
        if (sInstance == null) {
            sInstance = new CLog();
        }
        return sInstance;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return null;
        }


        for (StackTraceElement st:sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }

            return "["+Thread.currentThread().getId()+": "+st.getFileName()+":"+st.getLineNumber()+"]";
        }

        return null;
    }

    public static void info(Object str) {
        if (logLevel <= Log.INFO) {
            String name = CLog.getInstance().getFunctionName();
            String ls=(name==null?str.toString():(name+" - "+str));
            Log.i(tag, ls);
        }
    }

    public static void i(Object str) {
        if (isDebug) {
            info(str);
        }
    }

    public static void verbose(Object str) {
        if (logLevel <= Log.VERBOSE) {
            String name = CLog.getInstance().getFunctionName();
            String ls=(name==null?str.toString():(name+" - "+str));
            Log.v(tag, ls);
        }
    }

    public static void v(Object str) {
        if (isDebug) {
            verbose(str);
        }
    }

    public static void warn(Object str) {
        if (logLevel <= Log.WARN) {
            String name = CLog.getInstance().getFunctionName();
            String ls=(name==null?str.toString():(name+" - "+str));
            Log.w(tag, ls);
        }
    }

    public static void w(Object str) {
        if (isDebug) {
            warn(str);
        }
    }

    public static void error(Object str) {
        if (logLevel <= Log.ERROR) {
            String name = CLog.getInstance().getFunctionName();
            String ls=(name==null?str.toString():(name+" - "+str));
            Log.e(tag, ls);
        }
    }

    public static void error(Exception ex) {
        if (logLevel <= Log.ERROR) {
            StringBuffer sb = new StringBuffer();
            String name = CLog.getInstance().getFunctionName();
            StackTraceElement[] sts = ex.getStackTrace();

            if (name != null) {
                sb.append(name+" - "+ex+"\r\n");
            } else {
                sb.append(ex+"\r\n");
            }

            if (sts != null && sts.length > 0) {
                for (StackTraceElement st:sts) {
                    if (st != null) {
                        sb.append("[ "+st.getFileName()+":"+st.getLineNumber()+" ]\r\n");
                    }
                }
            }

            Log.e(tag, sb.toString());
        }
    }

    public static void e(Object str) {
        if (isDebug) {
            error(str);
        }
    }

    public static void e(Exception ex) {
        if (isDebug) {
            error(ex);
        }
    }

    public static void debug(Object str) {
        if (logLevel <= Log.DEBUG) {
            String name = CLog.getInstance().getFunctionName();
            String ls = (name == null?str.toString():(name+" - "+str));
            Log.d(tag, ls);
        }
    }

    public static void d(Object str) {
        if (isDebug) {
            debug(str);
        }
    }
}
