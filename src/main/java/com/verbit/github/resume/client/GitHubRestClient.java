package com.verbit.github.resume.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import com.verbit.github.resume.configuration.GitHubAccessProperties;
import com.verbit.github.resume.dao.GitHubAccountOwnerDao;
import com.verbit.github.resume.dao.GitHubRepositoryDao;
import com.verbit.github.resume.dto.GitHubAccountOwnerDto;
import com.verbit.github.resume.dto.GitHubRepositoryDto;

@Component
public class GitHubRestClient {

	public static final String API_USERS = "/users";
	public static final String API_REPOS = "/repos";
	static final String JSON_CONTENT_TYPE = "application/json";

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private GitHubAccessProperties gitHubAccessProperties;

	private RestTemplate restTemplate = new RestTemplate();

	public List<GitHubRepositoryDao> getGitHubRepositoriesForAccount(String accountOwner) {
		String gitHubUri = gitHubAccessProperties.getEndpoint() + API_USERS + "/" + accountOwner + API_REPOS;
		HttpEntity<String> headerEntity = initHeader();
		GitHubRepositoryDto[] dtoArray = restTemplate
				.exchange(gitHubUri, HttpMethod.GET, headerEntity, GitHubRepositoryDto[].class).getBody();
		GitHubRepositoryDao[] daoArray = modelMapper.map(dtoArray, GitHubRepositoryDao[].class);
		return Arrays.asList(daoArray);
	}

	public GitHubAccountOwnerDao getGitHubAccount(String accountOwner) {
		String gitHubUri = gitHubAccessProperties.getEndpoint() + API_USERS + "/" + accountOwner;
		HttpEntity<String> headerEntity = initHeader();
		GitHubAccountOwnerDto dto = restTemplate
				.exchange(gitHubUri, HttpMethod.GET, headerEntity, GitHubAccountOwnerDto.class).getBody();
		return modelMapper.map(dto, GitHubAccountOwnerDao.class);
	}

	private HttpEntity<String> initHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "profile-analyzer");
		if (!StringUtils.isEmpty(gitHubAccessProperties.getToken())) {
			headers.setBearerAuth(gitHubAccessProperties.getToken());
		}
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return new HttpEntity<>("parameters", headers);
	}
}
