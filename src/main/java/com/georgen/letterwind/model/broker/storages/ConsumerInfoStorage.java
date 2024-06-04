package com.georgen.letterwind.model.broker.storages;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerInfoStorage {
    private ConcurrentHashMap<Class, Set<Method>> consumingMethods = new ConcurrentHashMap<>();

}
