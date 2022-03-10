package com.kumbirai.udemy.vertxstarter.eventbus.customcodec;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ping
{
	private String message;
	private boolean enabled;

	@Override
	public String toString()
	{
		return new Gson().toJson(this);
	}
}
