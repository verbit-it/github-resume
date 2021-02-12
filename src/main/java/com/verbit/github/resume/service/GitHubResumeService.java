package com.verbit.github.resume.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.verbit.github.resume.client.GitHubRestClient;
import com.verbit.github.resume.dao.GitHubAccountOwnerDao;
import com.verbit.github.resume.dao.GitHubRepositoryDao;

@Service
public class GitHubResumeService {

	@Autowired
	private GitHubRestClient gitHubRestClient;

	public GitHubAccountOwnerDao getGitHubAccount(String accountOwner) {
		return gitHubRestClient.getGitHubAccount(accountOwner);
	}
	
	public List<GitHubRepositoryDao> getGitHubRepositoriesForAccount(String accountOwner) {
		return gitHubRestClient.getGitHubRepositoriesForAccount(accountOwner);
	}
	
}
