# AfreecaTv Chat Crawler for Java

[![](https://jitpack.io/v/lede-dev/afreecatv-chat-crawler-java.svg)](https://jitpack.io/#lede-dev/afreecatv-chat-crawler-java)

This project is inspired by the [afreecatv-chat-crawler](https://github.com/cha2hyun/afreecatv-chat-crawler) project. </br>
I would like to express my gratitude to the original creator for their inspiration and contributions.

# Setting Up

### Gradle 
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```
```groovy
dependencies {
    implementation 'com.github.lede-dev:afreecatv-chat-crawler-java:{version}'
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.lede-dev</groupId>
    <artifactId>afreecatv-chat-crawler-java</artifactId>
    <version>Tag</version>
</dependency>
```

# Usage


First, we create an instance of AfreecaTvChatCrawler. There are two ways to create an instance.

- Use AfreecaTV live broadcast URL
```java
AfreecaTvChatCrawler crawler = new AfreecaTvChatCrawler("play.afreecatv.com/lilpa0309/263127556");
```

- Use AfreecaTV Broadcaster ID and Number
```java
AfreecaTvChatCrawler crawler = new AfreecaTvChatCrawler("lilpa0309", 263127556);
```
or
```java
AfreecaTvChatCrawler crawler = new AfreecaTvChatCrawler("lilpa0309", "263127556");
```

</br>
Next, register the AfreecaTvMessageReceiveEvent to handle message reception.

```java
crawler.registerMessageReceiveEvent(new AfreecaTvMessageReceiveEvent() {
    @Override
    public void onMessageReceive(@NotNull AccMessage message) {
        String.format("%s[%s] : %s", 
            message.getSenderNickname(), message.getSenderId(), message.getMessage());
    }
});
```

</br>
Once all configurations are complete, you can start the crawler.

```java
crawler.connect();
```

</br>
That's it! Here are a few points to keep in mind when using it.

- If the AfreecaTV broadcaster is not live, the crawler won't be able to connect to the broadcast.
- Message reception operates in separate threads for each crawler. Be sure to use thread synchronization carefully.


# Example
- [Example for Minecraft Plugin](https://github.com/Lede-dev/afreecatv-chat-crawler-java-example)