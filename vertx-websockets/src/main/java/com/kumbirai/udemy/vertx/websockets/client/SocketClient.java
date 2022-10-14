package com.kumbirai.udemy.vertx.websockets.client;

import com.kumbirai.udemy.vertx.websockets.WebSocketHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketClient extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(SocketClient.class);

	public static void main(String[] args)
	{
	}

	@Override
	public void start(Promise<Void> startPromise) throws Exception
	{
		startPromise.complete();
		var client = vertx.createHttpClient();

		client.webSocket(8900, "localhost", WebSocketHandler.PATH, webSocketAsyncResult ->
		{
			if (webSocketAsyncResult.succeeded())
			{
				WebSocket socket = webSocketAsyncResult.result();
				socket.textMessageHandler(msg -> LOG.info("Server Message:{}{}", System.lineSeparator(), msg))
						.exceptionHandler(e -> LOG.error("Websocket Error: ", e));
			}
			else
			{
				LOG.error("Error: ", webSocketAsyncResult.cause());
			}
		});
	}
}
