package com.kumbirai.udemy.mutiny.vertx.db;

import com.github.javafaker.Faker;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class CreateUsers extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(CreateUsers.class);
	private final Pool db;

	public CreateUsers(final Pool db)
	{
		this.db = db;
	}

	@Override
	public Uni<Void> asyncStart()
	{
		vertx.periodicStream(20000L)
				.toMulti()
				.subscribe()
				.with(item -> createMoreUsers());
		return Uni.createFrom()
				.voidItem();
	}

	private void createMoreUsers()
	{
		LOG.info("Creating more users...");
		Faker faker = new Faker();
		Multi.createFrom()
				.items(IntStream.rangeClosed(1, ThreadLocalRandom.current()
								.nextInt(6))
							   .boxed()
							   .map(num -> faker.name()
									   .fullName()))
				.onItem()
				.transform(fullName -> db.preparedQuery("INSERT INTO users(name) VALUES ($1) RETURNING (id)")
						.execute(Tuple.of(fullName))
						.onItem()
						.transform(rs -> rs.iterator()
								.next()
								.getLong("id"))
						.subscribe()
						.with(id -> LOG.debug("New Id {}", id)))
				.subscribe()
				.with(item ->
					  {
						  return;
					  });
	}
}
