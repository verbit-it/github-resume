package com.verbit.github.resume.dao;

import java.util.List;

public class GitHubResumeDao {

	private String username;
	private String websiteUrl;

	private Integer amountOfCreatedRepositories;
	private List<GitHubRepositoryDao> gitHubRepositories;
	private List<GitHubStatisticalDataDao> gitHubStatisticalData;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public Integer getAmountOfCreatedRepositories() {
		return amountOfCreatedRepositories;
	}

	public void setAmountOfCreatedRepositories(Integer amountOfCreatedRepositories) {
		this.amountOfCreatedRepositories = amountOfCreatedRepositories;
	}

	public List<GitHubRepositoryDao> getGitHubRepositories() {
		return gitHubRepositories;
	}

	public void setGitHubRepositories(List<GitHubRepositoryDao> gitHubRepositories) {
		this.gitHubRepositories = gitHubRepositories;
	}

	public List<GitHubStatisticalDataDao> getGitHubStatisticalData() {
		return gitHubStatisticalData;
	}

	public void setGitHubStatisticalData(List<GitHubStatisticalDataDao> gitHubStatisticalData) {
		this.gitHubStatisticalData = gitHubStatisticalData;
	}
}