package framework;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import configuration.Config;
import event.Event;
import event.EventManager;
import spider.Spider;

public class Framework {
    List<Spider> spiders = new ArrayList<>();
    Config config;

    public Framework() {
        EventManager.RegistEvent(Event.GLOBAL_STARTED,this::onStart);
    }
    public static Framework newFramework(Spider spider,Config config) {
        Framework framework = new Framework();
        framework.spiders.add(spider);
        framework.config = config;
        return framework;
    }

    public void start() {
        new frameworkEngine(this).start();
    }

    public void onStart(Config config) {
        System.out.println("框架已启动！！");
    }
}
