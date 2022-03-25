package com.kumbirai.udemy.mutiny.vertx;

import io.smallrye.mutiny.Multi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloMulti
{
	private static final Logger LOG = LoggerFactory.getLogger(HelloMulti.class);

	public static void main(String[] args)
	{
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		// Multi represents a stream of data. A stream can emit 0, 1, n, or an infinite number of items.
		Multi.createFrom()
				.items(IntStream.rangeClosed(1,
								15)
						.boxed())
				.onItem()
				.transform(value -> IntStream.rangeClosed(1,
								20)
						.boxed()
						.map(num -> decimalFormat.format(Math.pow(value,
								num)))
						.collect(Collectors.toList()))
				.onFailure()
				.invoke(failure -> LOG.error("Transformation failed with: ",
						failure))
				.onItem()
				.transform(String::valueOf)
				.select()
				.first(10)
				.subscribe()
				.with(LOG::info,
						failure -> LOG.error("Failed with: ",
								failure));
	}
}
