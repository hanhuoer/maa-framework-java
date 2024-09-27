package io.github.hanhuoer.maa.sample;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.core.AdbController;
import io.github.hanhuoer.maa.core.base.Resource;
import io.github.hanhuoer.maa.core.base.Tasker;
import io.github.hanhuoer.maa.core.custom.Context;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognizer;
import io.github.hanhuoer.maa.model.AdbInfo;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import io.github.hanhuoer.maa.util.CollectionUtils;
import io.github.hanhuoer.maa.util.FileUtils;

import java.util.List;

/**
 * @author H
 */
public class OfficialSample {

    public static void main(String[] args) {
        MaaOptions options = new MaaOptions();
        Maa maa = Maa.create(options);

        List<AdbInfo> deviceList = AdbController.find("/Users/yuanshaokang/Library/Android/sdk/platform-tools/adb");
        if (CollectionUtils.isEmpty(deviceList)) {
            System.out.println("No ADB device found.");
            return;
        }

        // for demo, we just use the first device
        AdbInfo device = deviceList.get(0);
        AdbController controller = new AdbController(device);
        controller.connect();

        Resource resource = new Resource();
        resource.load(FileUtils.joinUserDir("resources", "resource").getAbsolutePath());
        Tasker tasker = new Tasker();
        tasker.bind(controller, resource);

        if (!tasker.inited()) {
            System.out.println("Failed to init MAA.");
            return;
        }

        MyRecognizer myRec = new MyRecognizer();
        MyAction myAct = new MyAction();

        resource.registerRecognizer("MyRec", myRec);
        resource.registerAction("MyAct", myAct);

//        tasker.runTask("StartUpAndClickButton");
        TaskDetail taskDetail = tasker.postPipeline("MyTask").waiting().get();
        System.out.println(taskDetail);

        System.out.println("End.");
    }

    public static class MyRecognizer extends CustomRecognizer {

        @Override
        public AnalyzeResult analyze(Context context, AnalyzeArg arg) {
            return new AnalyzeResult(
                    new Rect().setX(0).setY(0).setW(100).setH(100),
                    "Hello World!"
            );
        }
    }

    public static class MyAction extends CustomAction {

        @Override
        public RunResult run(Context context, RunArg arg) {
            return new RunResult().setSuccess(true);
        }
    }

}
