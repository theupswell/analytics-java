package analytics;

import analytics.client.Client;
import analytics.converter.Converter;
import analytics.model.Channel;
import analytics.model.payload.Payload;
import java.util.Queue;

public class Analytics {
  final Channel channel;
  final Client client;
  final Converter converter;
  final Queue<Payload> queue;

  Analytics(Channel channel, Client client, Converter converter, Queue<Payload> queue) {
    this.channel = channel;
    this.client = client;
    this.converter = converter;
    this.queue = queue;
  }
}
