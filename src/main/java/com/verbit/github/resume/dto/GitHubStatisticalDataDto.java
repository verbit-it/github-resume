package com.verbit.github.resume.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GitHubStatisticalDataDto {

	@JsonProperty("language_name")
    private String languageName;
	@JsonProperty("language_ratio")
    private Double languageRatio; 
}