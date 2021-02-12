package com.verbit.github.resume.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubResumeDto {

	@JsonProperty("username")
	private String username;

	@JsonProperty("website")
	private String website;

	@JsonProperty("amount_created_repositories")
	private Integer amountOfCreatedRepositories;
	@JsonProperty("repositories")
	private List<GitHubRepositoryDto> gitHubRepositories;
	@JsonProperty("statistical_data")
	private List<GitHubStatisticalDataDto> gitHubStatisticalData;
}