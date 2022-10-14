package com.kumbirai.udemy.vertx.websockets;

import com.kumbirai.udemy.vertx.websockets.client.SocketClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle
{

	private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

	@Override
	public void start(Promise<Void> startPromise) throws Exception
	{
		vertx.deployVerticle(new SocketClient(), new DeploymentOptions().setWorker(true)
				.setWorkerPoolSize(2)
				.setWorkerPoolName("1st-client-verticle"), result ->
							 {
								 if (result.succeeded())
								 {
									 LOG.debug("Client deployed.");
								 }
								 else
								 {
									 LOG.error("Client failed due to:", result.cause());
								 }
							 });
		vertx.deployVerticle(new SocketClient(), new DeploymentOptions().setWorker(true)
				.setWorkerPoolSize(2)
				.setWorkerPoolName("2nd-client-verticle"), result ->
							 {
								 if (result.succeeded())
								 {
									 LOG.debug("Client deployed.");
								 }
								 else
								 {
									 LOG.error("Client failed due to:", result.cause());
								 }
							 });

		vertx.createHttpServer()
				.webSocketHandler(new WebSocketHandler(vertx))
				.listen(8900, http ->
				{
					if (http.succeeded())
					{
						startPromise.complete();
						LOG.info("HTTP server started on port 8900");
					}
					else
					{
						startPromise.fail(http.cause());
					}
				});
	}
}
