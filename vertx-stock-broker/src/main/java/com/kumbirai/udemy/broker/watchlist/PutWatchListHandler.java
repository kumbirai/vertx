package com.kumbirai.udemy.broker.watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;
import java.util.UUID;

public class PutWatchListHandler implements Handler<RoutingContext>
{
	private final Map<UUID, WatchList> watchListPerAccount;

	public PutWatchListHandler(final Map<UUID, WatchList> watchListPerAccount)
	{
		this.watchListPerAccount = watchListPerAccount;
	}

	@Override
	public void handle(final RoutingContext context)
	{
		String accountId = WatchListRestApi.getAccountId(context);

		var json = context.getBodyAsJson();
		var watchList = json.mapTo(WatchList.class);
		watchListPerAccount.put(UUID.fromString(accountId),
				watchList);
		context.response()
				.end(json.toBuffer());
	}
}
