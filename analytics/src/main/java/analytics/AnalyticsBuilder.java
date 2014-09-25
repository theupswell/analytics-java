package analytics;

import analytics.client.Client;
import analytics.converter.Converter;
import analytics.model.Channel;
import analytics.model.payload.Payload;
import analytics.queue.QueueProvider;
import analytics.util.Utils;
import java.util.Queue;

public class AnalyticsBuilder {
  final String writeKey;
  Channel channel;
  Client client;
  Converter converter;
  Queue<Payload> queue;

  public AnalyticsBuilder(String writeKey) {
    if (Utils.isNullOrEmpty(writeKey)) {
      throw new IllegalArgumentException("writeKey may not be null or empty.");
    }
    this.writeKey = writeKey;
  }

  public AnalyticsBuilder setChannel(Channel channel) {
    if (channel == null) {
      throw new IllegalArgumentException("channel may not be null");
    }
    if (this.channel != null) {
      throw new IllegalStateException("channel already set");
    }
    this.channel = channel;
    return this;
  }

  public AnalyticsBuilder setClient(Client client) {
    if (client == null) {
      throw new IllegalArgumentException("client may not be null");
    }
    if (this.client != null) {
      throw new IllegalStateException("client already set");
    }
    this.client = client;
    return this;
  }

  public AnalyticsBuilder setConverter(Converter converter) {
    if (converter == null) {
      throw new IllegalArgumentException("converter may not be null");
    }
    if (this.converter != null) {
      throw new IllegalStateException("converter already set");
    }
    this.converter = converter;
    return this;
  }

  public AnalyticsBuilder setQueue(QueueProvider queueProvider) {
    if (queueProvider == null) {
      throw new IllegalArgumentException("queueProvider may not be null");
    }
    Queue<Payload> providedQueue = queueProvider.get();
    if (providedQueue == null) {
      throw new IllegalArgumentException("queueProvider.get() should not be null");
    }
    if (this.queue != null) {
      throw new IllegalStateException("queue already set");
    }
    this.queue = providedQueue;
    return this;
  }
}
