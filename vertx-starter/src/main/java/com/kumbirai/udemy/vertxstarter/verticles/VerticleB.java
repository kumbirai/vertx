package com.kumbirai.udemy.vertxstarter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleB extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(VerticleB.class);

	@Override
	public void start(final Promise<Void> startPromise) throws Exception
	{
		LOG.debug("Start {}", getClass().getName());
		startPromise.complete();
	}
}
