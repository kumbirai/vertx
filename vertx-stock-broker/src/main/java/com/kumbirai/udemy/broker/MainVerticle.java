package com.kumbirai.udemy.broker;

import com.kumbirai.udemy.broker.config.ConfigLoader;
import com.kumbirai.udemy.broker.db.migration.FlywayMigration;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
	private static final String DEPLOYED_WITH_ID = "Deployed {} with id {}";

	public static void main(String[] args)
	{
		var vertx = Vertx.vertx();
		vertx.exceptionHandler(error -> LOG.error("Unhandled:", error));
		vertx.deployVerticle(new MainVerticle())
				.onFailure(err -> LOG.error("Failed to deploy:", err))
				.onSuccess(id -> LOG.info(DEPLOYED_WITH_ID, MainVerticle.class.getSimpleName(), id));
	}

	@Override
	public void start(Promise<Void> startPromise) throws Exception
	{
		vertx.deployVerticle(VersionInfoVerticle.class.getName())
				.onFailure(startPromise::fail)
				.onSuccess(id -> LOG.info(DEPLOYED_WITH_ID, VersionInfoVerticle.class.getSimpleName(), id))
				.compose(next -> migrateDatabase())
				.onFailure(startPromise::fail)
				.onSuccess(id -> LOG.info("Migrated db schema to latest version!"))
				.compose(next -> deployRestApiVerticle(startPromise));
	}

	private Future<Void> migrateDatabase()
	{
		return ConfigLoader.load(vertx)
				.compose(config -> FlywayMigration.migrate(vertx, config.getDbConfig()));
	}

	private Future<String> deployRestApiVerticle(final Promise<Void> startPromise)
	{
		return vertx.deployVerticle(RestApiVerticle.class.getName(), new DeploymentOptions().setInstances(halfProcessors()))
				.onFailure(startPromise::fail)
				.onSuccess(id ->
						   {
							   LOG.info(DEPLOYED_WITH_ID, RestApiVerticle.class.getSimpleName(), id);
							   startPromise.complete();
						   });
	}

	private int halfProcessors()
	{
		return Math.max(1, Runtime.getRuntime()
				.availableProcessors() / 2);
	}
}
