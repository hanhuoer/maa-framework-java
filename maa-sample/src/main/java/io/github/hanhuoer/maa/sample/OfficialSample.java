package io.github.hanhuoer.maa.sample;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.core.AdbController;
import io.github.hanhuoer.maa.core.base.Resource;
import io.github.hanhuoer.maa.core.base.Tasker;
import io.github.hanhuoer.maa.core.custom.Context;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognizer;
import io.github.hanhuoer.maa.core.util.Future;
import io.github.hanhuoer.maa.core.util.TaskFuture;
import io.github.hanhuoer.maa.model.AdbInfo;
import io.github.hanhuoer.maa.model.RecognitionDetail;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import io.github.hanhuoer.maa.util.CollectionUtils;
import io.github.hanhuoer.maa.util.FileUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author H
 */
@Slf4j
public class OfficialSample {

    @SneakyThrows
    public static void main(String[] args) {
        log.info("...");
        MaaOptions options = new MaaOptions();
        options.setKillAdbOnShutdown(false);
//        options.setStdoutLevel(MaaLoggingLevelEunm.INFO);
        Maa maa = Maa.create(options);

        List<AdbInfo> deviceList = AdbController.find();
        if (CollectionUtils.isEmpty(deviceList)) {
            System.out.println("No ADB device found.");
            return;
        }

        // for demo, we just use the first device
        AdbInfo device = deviceList.get(0);
        AdbController controller = new AdbController(device);
        Future waiting = controller.postConnection().waiting();
        boolean succeeded = waiting.succeeded();
        if (!succeeded) {
            System.out.println("Failed connect.");
        }

        Resource resource = new Resource();
        resource.postPath(FileUtils.joinUserDir("resources", "resource").getAbsolutePath())
                .waiting();

        Tasker tasker = new Tasker();
        tasker.bind(controller, resource);

        if (!tasker.inited()) {
            System.out.println("Failed to init MAA.");
            return;
        }

        System.out.println("Succeed to init MAA.");
        resource.registerRecognizer("MyRec", new MyRecognizer());
//        resource.registerAction("MyAct", new MyAction());

//        tasker.runTask("StartUpAndClickButton");
        TaskFuture<TaskDetail> myTask = tasker.postPipeline("MyTask").waiting();
        Thread.sleep(10 * 1000);
        TaskDetail taskDetail = myTask.get();
        System.out.println(taskDetail);

        System.out.println("End.");
    }

    public static class MyRecognizer extends CustomRecognizer {

        @Override
        public AnalyzeResult analyze(Context context, AnalyzeArg arg) {
            JSONObject overrideJson = new JSONObject();
            overrideJson.put("MyCustomOCR", new JSONObject(Map.of("roi", new int[]{100, 100, 200, 300})));
            RecognitionDetail detail = context.runRecognition("MyCustomOCR", arg.getImage(), overrideJson);
            System.out.println("context reco detail: " + detail);

            // context is a reference, will override the pipeline for whole task.
            JSONObject overrideJson2 = new JSONObject();
            overrideJson2.put("MyCustomOCR", new JSONObject(Map.of("roi", new int[]{1, 1, 114, 514})));
            boolean contextOverridePipelineResult = context.overridePipeline(overrideJson2);
            System.out.println("context override result: " + contextOverridePipelineResult);
            // context.runRecognition ...

            // make a new context to override the pipeline, only for itself.
            // copy is the same as the clone method for python binding.
            Context newContext = context.copy();
            JSONObject overrideJson3 = new JSONObject();
            overrideJson3.put("MyCustomOCR", new JSONObject(Map.of("roi", new int[]{100, 200, 300, 400})));
            newContext.overridePipeline(overrideJson3);
            RecognitionDetail recoDetail = newContext.runRecognition("MyCustomOCR", arg.getImage());
            System.out.println("new context reco detail: " + recoDetail);

            Future clickFuture = context.tasker().controller().postClick(10, 20);
            clickFuture.waiting();

            context.overrideNext(arg.getCurrentTaskName(), List.of("TaskA", "TaskB"));

            return new AnalyzeResult()
                    .setBox(new Rect().setX(0).setY(0).setW(100).setH(100))
                    .setDetail("Hello World!");
        }
    }

    public static class MyAction extends CustomAction {

        @Override
        public RunResult run(Context context, RunArg arg) {
            return RunResult.success();
        }
    }

}
