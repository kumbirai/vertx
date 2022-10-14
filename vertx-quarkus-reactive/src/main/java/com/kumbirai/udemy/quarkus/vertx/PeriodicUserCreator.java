package com.kumbirai.udemy.quarkus.vertx;

import com.github.javafaker.Faker;
import com.kumbirai.udemy.quarkus.Users;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Optional;

@ApplicationScoped
public class PeriodicUserCreator extends AbstractVerticle
{
	static final String ADDRESS = PeriodicUserCreator.class.getName();
	private static final Logger LOG = LoggerFactory.getLogger(PeriodicUserCreator.class);

	@Override
	public Uni<Void> asyncStart()
	{
		var client = WebClient.create(vertx, new WebClientOptions().setDefaultHost("localhost")
				.setDefaultPort(8080));
		var faker = new Faker();
		vertx.periodicStream(Duration.ofSeconds(5)
									 .toMillis())
				.toMulti()
				.subscribe()
				.with(item ->
					  {
						  LOG.info("Create user!");
						  var user = new Users();
						  user.name = faker.name()
								  .firstName();
						  client.post("/users")
								  .sendJson(user)
								  .subscribe()
								  .with(result ->
										{
											Optional<String> location = Optional.of(result.headers()
																							.get("Location"));
											location.ifPresent(locate ->
															   {
																   LOG.info("Created User: {} - location: {}", user.name, locate);
																   vertx.eventBus()
																		   .publish(ADDRESS, locate);
															   });
										});
					  });
		return Uni.createFrom()
				.voidItem();
	}
}
