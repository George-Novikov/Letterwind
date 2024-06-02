## Letterwind - Embedded Message Broker  

Letterwind is an embedded Java message broker/event bus library.  

* It is capable of handling multi-threaded tasks both locally and remotely.  

* The remote transport layer is built on the Netty server.  

* Message propagation uses serialization / deserialization through the OS file system. 
This ensures a more consistent and reliable sending and receiving mechanism.
Future releases will continue to expand fault tolerance features.

## Usage
### 1. Declare a message class
Your message class must be marked with the @LetterwindMessage annotation.
```java
@LetterwindMessage
public class SampleMessage {
    private String value;

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
}
```
You can also use String as the simplest form of the message.  

### 2. Declare a consumer class & method
For a method to receive a message, it must be marked with the @LetterwindConsumer annotation.  
It must also be public and have a single argument of your message type.  
A consumer class can have multiple @LetterwindConsumer methods.
```java
public class SampleConsumer {
    @LetterwindConsumer
    public void receive(SampleMessage message){
        ...your logic is here...
    }
}
```
Your consumer class can also have any other methods (private of public) if you need.

### 2. Configure topic
Messages are distributed among consumers via topics represented by the LetterwindTopic class.  
Each topic can have multiple consumer classes.  
```java
LetterwindTopic topic = LetterwindTopic.build()
        .setName("SampleTopic")
        .addConsumer(SampleConsumer.class);
```

### 3. Register the topic. Configure global settings

Global settings are made through the LetterwindControls class.  
In order for a topic to be available for sending messages, it must be registered there.
```java
LetterwindControls.set()
        .topic(topic)
        .consumersLimit(40);
```
Here you can also configure other global settings, such as the number of threads available for all consumer operations, etc.

### 4. Send  

Send your message via the static method Letterwind.send();
```java
SampleMessage message = new SampleMessage();
message.setValue("Any value relevant to your logic");
Letterwind.send(message);
```
This example shows a "fan-out" distribution, where your message is sent to every registered topic, whose consumers accept messages of this type.  
There are other versions of the send() method, that allow to send topic-specific messages:  
```java
Letterwind.send(message, "SampleTopic");
```
```java
Letterwind.send(message, topic);
```

## Remote usage

If your architecture requires more than one application deployed on multiple machines, you can configure remote messaging.

### 1. Configure server
Your consumer application must have server configuration enabled:
```java
LetterwindControls.set()
        .serverActive(true)
        .serverPort(17566);
```
### 2. Configure client
Your producer application must be configured to connect to the server.

This can be achieved by specifying the remote server address globally, in the LetterwindControls.  

```java
LetterwindControls.set()
        .remoteHost("127.0.0.1")
        .remotePort(17566);
```

Or you can configure it only for a specific LetterwindTopic.
```java
LetterwindTopic topic = LetterwindTopic.build()
        .setName("SampleTopic")
        .addConsumer(SampleConsumer.class)
        .setRemoteHost("localhost")
        .setRemotePort(17566);
```

The latter will allow you to send remote messages exclusively on this topic — all other sendings will work locally.  

The process of sending a message is similar to local.  
But of course, you should configure your LetterwindTopic, LetterwindConsumer and LetterwindMessage with the same names on both sides.