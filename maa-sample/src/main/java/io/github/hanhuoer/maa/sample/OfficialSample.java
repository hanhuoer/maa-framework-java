package io.github.hanhuoer.maa.sample;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.core.AdbController;
import io.github.hanhuoer.maa.core.base.Instance;
import io.github.hanhuoer.maa.core.base.Resource;
import io.github.hanhuoer.maa.core.custom.CustomAction;
import io.github.hanhuoer.maa.core.custom.CustomRecognizer;
import io.github.hanhuoer.maa.core.custom.SyncContext;
import io.github.hanhuoer.maa.model.AdbInfo;
import io.github.hanhuoer.maa.model.CustomRecognizerAnalyzeResult;
import io.github.hanhuoer.maa.model.Rect;
import io.github.hanhuoer.maa.model.TaskDetail;
import io.github.hanhuoer.maa.util.CollectionUtils;
import io.github.hanhuoer.maa.util.FileUtils;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author H
 */
public class OfficialSample {

    public static void main(String[] args) {
        MaaOptions options = new MaaOptions();
        Maa maa = Maa.create(options);

        List<AdbInfo> deviceList = AdbController.find();
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
        Instance instance = new Instance();
        instance.bind(controller, resource);

        if (!instance.inited()) {
            System.out.println("Failed to init MAA.");
            return;
        }

        MyRecognizer myRec = new MyRecognizer();
        MyAction myAct = new MyAction();

        instance.registerRecognizer("MyRec", myRec);
        instance.registerAction("MyAct", myAct);

//        instance.runTask("StartUpAndClickButton");
        TaskDetail taskDetail = instance.runTask("MyTask");
        System.out.println(taskDetail);

        System.out.println("End.");
    }

    public static class MyRecognizer extends CustomRecognizer {

        @Override
        public CustomRecognizerAnalyzeResult analyze(SyncContext context, BufferedImage image, String taskName, String customParam) {
            return new CustomRecognizerAnalyzeResult(
                    true,
                    new Rect().setX(0).setY(0).setW(100).setH(100),
                    "Hello World!"
            );
        }

    }

    public static class MyAction extends CustomAction {

        @Override
        public boolean run(SyncContext context, String taskName, String customParam, Rect box, String recDetail) {
            return true;
        }

        @Override
        public void stop() {

        }
    }

}
