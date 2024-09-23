package io.github.hanhuoer.maa.sample;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.MaaOptions;
import io.github.hanhuoer.maa.core.AdbController;
import io.github.hanhuoer.maa.core.base.Resource;
import io.github.hanhuoer.maa.core.base.Tasker;
import io.github.hanhuoer.maa.model.AdbInfo;

import java.util.List;

/**
 * @author H
 */
public class QuickStart {
    public static void main(String[] args) {
        MaaOptions options = new MaaOptions();
        Maa maa = Maa.create(options);

        List<AdbInfo> adbInfoList = AdbController.find();
        AdbController controller = new AdbController(adbInfoList.get(0));
        controller.connect();
        Resource resource = new Resource();
        resource.load("./resource");
        Tasker tasker = new Tasker();
        boolean bind = tasker.bind(controller, resource);
        System.out.println("bind result: " + bind);
        System.out.println(tasker.inited());
        tasker.close();
    }
}
