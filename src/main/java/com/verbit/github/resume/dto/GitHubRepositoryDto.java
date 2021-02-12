package com.verbit.github.resume.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GitHubRepositoryDto {

	@JsonProperty("name")
    private String name;
	@JsonProperty("url")
    private String url; 
	@JsonProperty("description")
    private String description;
	@JsonProperty("language")
    private String language;
}