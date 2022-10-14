package com.kumbirai.udemy.vertx.websockets;

import com.github.javafaker.Faker;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PriceBroadcast
{

	private static final Logger LOG = LoggerFactory.getLogger(PriceBroadcast.class);
	private final Map<String, ServerWebSocket> connectedClients = new HashMap<>();

	public PriceBroadcast(final Vertx vertx)
	{
		periodicUpdate(vertx);
	}

	private void periodicUpdate(final Vertx vertx)
	{
		var faker = new Faker();
		vertx.setPeriodic(Duration.ofSeconds(1)
								  .toMillis(), id ->
						  {
							  LOG.debug("Push update to {} client(s)!", connectedClients.size());
							  final String priceUpdate = new JsonObject().put("date", LocalDateTime.now()
											  .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
									  .put("symbol", "AMZN")
									  .put("value", new Random().nextInt(1000))
									  .put("valid", faker.bool()
											  .bool())
									  .put("Chuck Norris", faker.chuckNorris()
											  .fact())
									  .encodePrettily();
							  connectedClients.values()
									  .forEach(ws -> ws.writeTextMessage(priceUpdate));
						  });
	}

	public void register(final ServerWebSocket ws)
	{
		connectedClients.put(ws.textHandlerID(), ws);
	}

	public void unregister(final ServerWebSocket ws)
	{
		connectedClients.remove(ws.textHandlerID());
	}
}
