package com.github.tscz.jalp.vocable;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VocableTo( //
		@JsonProperty("id") UUID id, //
		@JsonProperty("value") String value, //
		@JsonProperty("translation") String translation) {

}
