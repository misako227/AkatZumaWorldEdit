package com.z227.AkatZumaWorldEdit.ConfigFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AkatZumaLogger {
    private static volatile AkatZumaLogger instance;
    // 日志根目录
    public static final String logDirectory;
    private static File logFile;
    private static BufferedWriter writer;
    private static String currentDate;
    // 日志写入线程池
    private final ExecutorService singleThreadExecutor;
    // 日志队列
//    private final BlockingQueue<LogTask> logQueue;

    static {
        logDirectory = "config/AkatZumaWorldEdit/logs";
        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private AkatZumaLogger() {
        this.singleThreadExecutor = Executors.newSingleThreadExecutor();
//        this.logQueue = new LinkedBlockingQueue<>();
        initialize();
    }


    public static AkatZumaLogger getInstance() {
        if (instance == null) {
            synchronized (AkatZumaLogger.class) {
                if (instance == null) {
                    instance = new AkatZumaLogger();
                }
            }
        }
        return instance;
    }

    // 初始化日志目录和消费者线程
    private void initialize() {
        // 创建日志存储目录
        createLogDirectory(logDirectory);
        updateLogFile();

    }

    // 创建日志目录
    private void createLogDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("Failed to create log directory: " + path);
            }
        }
    }



    public static void  updateLogFile() {
        // 获取当前日期并创建月目录
        Date now = new Date();
        String monthDir = new SimpleDateFormat("yyyy-MM").format(now);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(now);
        currentDate = dateStr; // 更新当前日期

        // 创建日志月份目录
        File dir = new File(logDirectory + "/" + monthDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 创建日志文件
        logFile  = Paths.get(dir.toString(),dateStr + ".log").toFile();
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            writer = new BufferedWriter(new FileWriter(logFile, true));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 记录日志方法
    public void info(String message) {
//        logQueue.offer(new LogTask("INFO",  message));
//        executor.submit(() -> writeLog("INFO", message + Thread.currentThread().getName()));
        singleThreadExecutor.submit(() -> writeLog("INFO", message));
    }

    public ExecutorService getSingleThreadExecutor() {
        return singleThreadExecutor;
    }



    // 实际写入日志
    private synchronized void writeLog(String logLevel, String message) {


        try {
            // 获取当前日期并创建月目录
            Date now = new Date();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(now);
            String timeStr = new SimpleDateFormat("HH:mm:ss.SSS").format(now);

            //判断是否是新的一天
            if(!currentDate.equals(dateStr)){
                writer.close();//关闭当前文件,创建新的文件
                updateLogFile();
            }

            // 组织日志内容
            String logEntry = String.format("[%s] [%s] [%s]: %s\n",
                    dateStr, timeStr, logLevel, message);

            // 写入日志文件
            writer.write(logEntry);
            writer.flush();

        } catch (IOException e) {
            System.err.println("AkatZumaWorldEdit Failed to write log: " + e.getMessage());
        }
    }

    // 关闭日志系统
    public void shutdown() {
        System.out.println("AkatZuma Logger 关闭.");

        singleThreadExecutor.shutdown();
        try {
            writer.close();
            if (!singleThreadExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                singleThreadExecutor.shutdownNow();
            }
        } catch (Exception e) {
            singleThreadExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
