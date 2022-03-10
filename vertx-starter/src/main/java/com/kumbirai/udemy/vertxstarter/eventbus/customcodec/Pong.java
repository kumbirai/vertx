package com.kumbirai.udemy.vertxstarter.eventbus.customcodec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Pong
{
	private Integer id;
	private Object payload;
}
