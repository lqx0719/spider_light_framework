package event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import configuration.Config;
public class EventManager {
    private static final Map<Event,List<Consumer<Config>>> eventConsumerMap = new HashMap<>();
    public static void RegistEvent(Event event, Consumer<Config> consumer) {
        List<Consumer<Config>> consumers = eventConsumerMap.get(event);
        if(null == consumers ) {
            consumers = new ArrayList<Consumer<Config>>();

        }
        consumers.add(consumer);
        eventConsumerMap.put(event, consumers);
    }

    public static void startEvent(Event event,Config config) {
        List<Consumer<Config>> consumers = eventConsumerMap.get(event);
        if(null != consumers) {
            consumers.forEach(consumer -> consumer.accept(config));
        }
    }
}
