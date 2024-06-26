package com.georgen.letterwind.api;

import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.broker.ordering.MessageOrderManager;
import com.georgen.letterwind.model.broker.ControlsGetter;
import com.georgen.letterwind.model.broker.ControlsSetter;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.broker.storages.MessageHandlerStorage;
import com.georgen.letterwind.model.constants.MessageFlowEvent;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.transport.TransportLayer;
import com.georgen.letterwind.util.Validator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Letterwind main configuration class
 */
public class LetterwindControls {


    // ============================ Multithreading: Dispatch, Reception, Consuming, Events =============================


    /** Determines how many threads can simultaneously perform the dispatch process. */
    private int sendersLimit;

    /** Determines how many threads can simultaneously execute the reception process. */
    private int receiversLimit;

    /** Determines the total number of @LetterwindConsumer methods (topic listeners) allowed to operate simultaneously. */
    private int consumersLimit;

    /** Determines how many threads can simultaneously handle errors or success events. */
    private int eventHandlersLimit;

    /** This flag will scale the total number of threads to the limits of the system processor. */
    private boolean isScaledToSystemCPU = true;


    // ======================================= Remote access: Server and Client ========================================


    /**
     * Global settings for listening messages from other Letterwind instances.
     * */
    private boolean isServerActive;
    private int serverPort;

    /**
     * Global remote access config — when the host and port set, all messages will be sent to the remote Letterwind instance.
     * Only individual config within each LetterwindTopic can override this.
     * */
    private String remoteHost;
    private int remotePort;


    // =========================================== Topics and message types ============================================


    /** Registered topics. Unregistered ones will not participate in messaging. */
    private Map<String, LetterwindTopic> topics = new ConcurrentHashMap<>();

    /** Message types mapped to the names of all registered topics */
    private Map<Class, Set<String>> topicsByMessageType = new ConcurrentHashMap();

    /** To quickly find a message type by its simple class name. */
    private Map<String, Class> messageTypes = new ConcurrentHashMap<>();


    // ================================================ Event handlers =================================================


    /**
     * A global error handler.
     * It has the lowest priority and will only be called if the @LetterwindMessage and LetterwindTopic error handlers are not present.
     * */
    private ErrorHandler errorHandler;

    /**
     * A global success handler.
     * It has the lowest priority and will only be called if the @LetterwindMessage and LetterwindTopic success handlers are not present.
     * */
    private SuccessHandler successHandler;


    // ===================================== Private constructor and public access =====================================


    private LetterwindControls(){}

    /** Thread safety wrapper. For more info, see "Bill Pugh Singleton" or "Holder Pattern". */
    private static class InstanceHolder {
        private static final LetterwindControls INSTANCE = new LetterwindControls();
        private static final ControlsGetter GETTER = new ControlsGetter();
        private static final ControlsSetter SETTER = new ControlsSetter();
    }

    /** Common public access method. */
    public static LetterwindControls getInstance(){ return InstanceHolder.INSTANCE; }

    /**
     * <p>    Syntax sugar.    </p>
     * <p>    This allows you to access LetterwindControls in a manner similar to the Builder Pattern.    </p>
     * <br>
     * <p>    So, you can do this:    </p>
     * <p>    int sendersLimit = LetterwindControls.get().sendersLimit();    </p>
     * <br>
     * <p>    Instead of this:    </p>
     * <p>    int sendersLimit = LetterwindControls.getInstance().getSendersLimit();    </p>
     * */
    public static ControlsGetter get(){ return InstanceHolder.GETTER; }

    /**
     * <p>    Syntax sugar.    </p>
     * <p>    This allows you to set LetterwindControls in a manner similar to the Builder Pattern.    </p>
     * <br>
     * <p>    So, you can do this:    </p>
     * <p>    LetterwindControls.set().sendersLimit(20);    </p>
     * <br>
     * <p>    Instead of this:    </p>
     * <p>    LetterwindControls.getInstance().setSendersLimit(20);    </p>
     * <br>
     * <p>    All operations via the set() method can be chained as follows:    </p>
     * <p>    LetterwindControls.set().remoteHost("localhost").remotePort(8080);    </p>
     * */
    public static ControlsSetter set(){ return InstanceHolder.SETTER; }


    //=========================================== Common accessors and logic ===========================================


    public int getSendersLimit() {
        return sendersLimit;
    }

    public LetterwindControls setSendersLimit(int sendersLimit) {
        this.sendersLimit = sendersLimit;
        return this;
    }

    public int getReceiversLimit() {
        return receiversLimit;
    }

    public LetterwindControls setReceiversLimit(int receiversLimit) {
        this.receiversLimit = receiversLimit;
        return this;
    }

    public int getConsumersLimit() {
        return consumersLimit;
    }

    public LetterwindControls setConsumersLimit(int consumersLimit) {
        this.consumersLimit = consumersLimit;
        return this;
    }

    public int getEventHandlersLimit() {
        return eventHandlersLimit;
    }

    public LetterwindControls setEventHandlersLimit(int eventHandlersLimit) {
        this.eventHandlersLimit = eventHandlersLimit;
        return this;
    }

    public boolean isScaledToSystemCPU() {
        return isScaledToSystemCPU;
    }

    public void setScaledToSystemCPU(boolean scaledToSystemCPU) {
        isScaledToSystemCPU = scaledToSystemCPU;
    }

    public boolean isServerActive() {
        return isServerActive;
    }

    public LetterwindControls setServerActive(boolean serverActive) throws LetterwindException {
        this.isServerActive = serverActive;

        if (isServerActive){
            if (this.serverPort == 0) throw new LetterwindException("Server startup failed. Set a valid port.");
            TransportLayer.getInstance().initServer(this);
        }else {
            TransportLayer.getInstance().shutdownServer();
        }

        return this;
    }

    public int getServerPort() {
        return serverPort;
    }

    public LetterwindControls setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public LetterwindControls setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;

        if (this.hasRemoteListener()){
            TransportLayer.getInstance().initGlobalClient(this);
        }

        return this;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public LetterwindControls setRemotePort(int remotePort) {
        this.remotePort = remotePort;

        if (this.hasRemoteListener()){
            TransportLayer.getInstance().initGlobalClient(this);
        }

        return this;
    }

    public Map<String, LetterwindTopic> getTopics() {
        return topics;
    }

    public LetterwindControls setTopics(Map<String, LetterwindTopic> topics) {
        this.topics = topics;

        TransportLayer.getInstance().initAllTopicClients(this);

        return this;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public LetterwindControls setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public SuccessHandler getSuccessHandler() {
        return successHandler;
    }

    public LetterwindControls setSuccessHandler(SuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public LetterwindControls addTopic(LetterwindTopic topic) throws Exception {
        topics.put(topic.getName(), topic);
        addToMessageTypes(topic);

        if (topic.hasRemoteListener()){
            TransportLayer.getInstance().initTopicClient(topic);
        }

        return this;
    }

    private void addToMessageTypes(LetterwindTopic topic) throws IOException {
        Set<Class> messageTypes = topic.getConsumerMessageTypes();

        for (Class messageType : messageTypes){
            Set<String> topicNames = this.topicsByMessageType.get(messageType);
            if (topicNames == null) topicNames = new HashSet<>();

            topicNames.add(topic.getName());
            this.topicsByMessageType.put(messageType, topicNames);

            this.messageTypes.put(messageType.getSimpleName(), messageType);
            MessageHandlerStorage.getInstance().register(messageType);
            MessageOrderManager.initForTopics(messageType, topicNames);
        }
    }

    public boolean removeTopic(String topicName) throws Exception {
        LetterwindTopic topic = topics.get(topicName);
        if (topic != null){
            topics.remove(topicName);
            deleteFromMessageTypes(topic);
            return true;
        } else {
            return false;
        }
    }

    private void deleteFromMessageTypes(LetterwindTopic topic){
        Set<Class> messageTypes = topic.getConsumerMessageTypes();

        for (Class messageType : messageTypes){
            Set<String> topicNames = this.topicsByMessageType.get(messageType);
            if (topicNames != null) topicNames.remove(topic.getName());
            // For safety reasons nothing is removed from messageTypes map since @LetterwindMessage can be reused between consumers
        }
    }

    public LetterwindTopic getTopic(String topicName){
        return topics.get(topicName);
    }

    public Set<LetterwindTopic> getTopicsWithMessageType(Class messageType){
        Set<String> topicNames = topicsByMessageType.get(messageType);
        if (topicNames == null) return null;
        return topicNames
                .stream()
                .map(topicName -> topics.get(topicName))
                .collect(Collectors.toSet());
    }

    public Class getMessageTypeBySimpleName(String messageTypeSimpleName){
        if (!Validator.isValid(messageTypeSimpleName)) return null;
        return this.messageTypes.get(messageTypeSimpleName);
    }

    public boolean hasRemoteListener(){
        return Validator.isValid(this.remoteHost) && this.remotePort != 0;
    }

    public boolean hasFinalEventHandlers(){
        return this.errorHandler != null || this.successHandler != null;
    }

    /** This method is intended to prevent the unnecessary overhead of creating new threads with no event handling */
    public boolean isAllowedToProceed(Envelope envelope, MessageFlowEvent event){
        if (!event.isFinal()) return true;
        if (envelope == null || !envelope.hasMessageTypeName() || !envelope.hasTopicName()) return false;

        Class messageType = messageTypes.get(envelope.getMessageTypeName());
        if (messageType == null) return false;

        if (MessageHandlerStorage.getInstance().hasFinalEventHandlers(messageType, event)) return true;

        LetterwindTopic topic = envelope.hasTopic() ? envelope.getTopic() : this.topics.get(envelope.getTopicName());
        if (topic != null && topic.hasFinalEventHandlers()) return true;

        return this.hasFinalEventHandlers();
    }
}
