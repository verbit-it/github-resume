package com.verbit.github.resume.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.verbit.github.resume.dao.GitHubAccountOwnerDao;
import com.verbit.github.resume.dao.GitHubRepositoryDao;
import com.verbit.github.resume.dao.GitHubResumeDao;
import com.verbit.github.resume.dao.GitHubStatisticalDataDao;
import com.verbit.github.resume.dto.GitHubResumeDto;
import com.verbit.github.resume.service.GitHubResumeService;

@RestController
@RequestMapping(path = "/github")
public class GitHubResumeController {

	public static final String SUPPORTED_MEDIA_TYPE_JSON = "json";
	public static final String SUPPORTED_MEDIA_TYPE_XML = "xml";

	@Autowired
	private GitHubResumeService gitHubResumeService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(value = "resume/{account}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, consumes = MediaType.ALL_VALUE)
	public ResponseEntity<GitHubResumeDto> getAccount(@PathVariable("account") String accountOwner,
			@RequestParam(name = "mediaType", required = false) String requestedMediaType,
			@RequestHeader HttpHeaders headers) {

		MediaType responseContentType = determineResponseContentType(headers, requestedMediaType);
		GitHubAccountOwnerDao gitHubAccountOwnerDao = gitHubResumeService.getGitHubAccount(accountOwner);
		List<GitHubRepositoryDao> gitHubRepositoryDaoList = gitHubResumeService
				.getGitHubRepositoriesForAccount(accountOwner);
		GitHubResumeDao dao = fillDao(gitHubAccountOwnerDao, gitHubRepositoryDaoList);
		GitHubResumeDto dto = modelMapper.map(dao, GitHubResumeDto.class);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, responseContentType.toString()).body(dto);
	}

	private MediaType determineResponseContentType(HttpHeaders headers, String requestedMediaType) {
		if (!CollectionUtils.isEmpty(headers.getAccept())) {
			// ignore requested media type value -> use accept header sent by the client
			List<MediaType> mediaTypeAcceptHeaderList = headers.getAccept();
			if (mediaTypeAcceptHeaderList.stream().anyMatch(item -> (!StringUtils.isEmpty(item.toString())
					&& item.toString().contains(MediaType.APPLICATION_JSON_VALUE)))) {
				return MediaType.APPLICATION_JSON;
			}
			if (mediaTypeAcceptHeaderList.stream().anyMatch(item -> (!StringUtils.isEmpty(item.toString())
					&& item.toString().contains(MediaType.APPLICATION_XML_VALUE)))) {
				return MediaType.APPLICATION_XML;
			}
		}
		if (!StringUtils.isEmpty(requestedMediaType)) {
			if (SUPPORTED_MEDIA_TYPE_JSON.equals(requestedMediaType)) {
				return MediaType.APPLICATION_JSON;
			}
			if (SUPPORTED_MEDIA_TYPE_XML.equals(requestedMediaType)) {
				return MediaType.APPLICATION_XML;
			}
		}

		// default
		return MediaType.APPLICATION_JSON;
	}

	private GitHubResumeDao fillDao(GitHubAccountOwnerDao gitHubAccountOwnerDao,
			List<GitHubRepositoryDao> gitHubRepositoryDaoList) {
		GitHubResumeDao dao = new GitHubResumeDao();
		dao.setUsername(gitHubAccountOwnerDao.getLogin());
		dao.setWebsiteUrl(gitHubAccountOwnerDao.getBlog());
		if (!CollectionUtils.isEmpty(gitHubRepositoryDaoList)) {
			dao.setAmountOfCreatedRepositories(gitHubRepositoryDaoList.size());
			dao.setGitHubRepositories(gitHubRepositoryDaoList);
			dao.setGitHubStatisticalData(fillStatisticalDataDao(gitHubRepositoryDaoList));
		}
		return dao;
	}

	private List<GitHubStatisticalDataDao> fillStatisticalDataDao(List<GitHubRepositoryDao> gitHubRepositoryDaoList) {
		List<GitHubStatisticalDataDao> gitHubStatisticalDataDaoList = new ArrayList<>();

		if (!CollectionUtils.isEmpty(gitHubRepositoryDaoList)) {
			Map<String, List<GitHubRepositoryDao>> map = gitHubRepositoryDaoList.stream()
					.filter(dao -> !StringUtils.isEmpty(dao.getLanguage()))
					.collect(Collectors.groupingBy(GitHubRepositoryDao::getLanguage));

			map.forEach((language, repoList) -> calculateStatisticalData(language, repoList,
					gitHubRepositoryDaoList.size(), gitHubStatisticalDataDaoList));
		}
		return gitHubStatisticalDataDaoList;
	}

	private void calculateStatisticalData(String language, List<GitHubRepositoryDao> repoList,
			Integer amountOfRepositories, List<GitHubStatisticalDataDao> gitHubStatisticalDataDaoList) {
		GitHubStatisticalDataDao gitHubStatisticalDataDao = new GitHubStatisticalDataDao();
		gitHubStatisticalDataDao.setLanguageeName(language);
		if (!CollectionUtils.isEmpty(repoList)) {
			gitHubStatisticalDataDao.setLanguageRatio((repoList.size() / (double) amountOfRepositories) * 100);
		}
		gitHubStatisticalDataDaoList.add(gitHubStatisticalDataDao);
	}
}
