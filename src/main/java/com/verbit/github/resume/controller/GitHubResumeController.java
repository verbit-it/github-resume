package com.verbit.github.resume.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.StringUtils;

import com.verbit.github.resume.dao.GitHubAccountOwnerDao;
import com.verbit.github.resume.dao.GitHubRepositoryDao;
import com.verbit.github.resume.dao.GitHubResumeDao;
import com.verbit.github.resume.dao.GitHubStatisticalDataDao;
import com.verbit.github.resume.dto.GitHubResumeDto;
import com.verbit.github.resume.service.GitHubResumeService;

@RestController
@RequestMapping(path = "/github")
public class GitHubResumeController {

	@Autowired
	private GitHubResumeService gitHubResumeService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(value = "resume/{account}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody GitHubResumeDto getAccount(@PathVariable("account") String accountOwner) {
		GitHubAccountOwnerDao gitHubAccountOwnerDao = gitHubResumeService.getGitHubAccount(accountOwner);
		List<GitHubRepositoryDao> gitHubRepositoryDaoList = gitHubResumeService
				.getGitHubRepositoriesForAccount(accountOwner);
		GitHubResumeDao dao = fillDao(gitHubAccountOwnerDao, gitHubRepositoryDaoList);

		return modelMapper.map(dao, GitHubResumeDto.class);
	}

	private GitHubResumeDao fillDao(GitHubAccountOwnerDao gitHubAccountOwnerDao,
			List<GitHubRepositoryDao> gitHubRepositoryDaoList) {
		GitHubResumeDao dao = new GitHubResumeDao();
		dao.setUsername(gitHubAccountOwnerDao.getLogin());
		dao.setWebsiteUrl(gitHubAccountOwnerDao.getBlog());
		if (!ListUtils.isEmpty(gitHubRepositoryDaoList)) {
			dao.setAmountOfCreatedRepositories(gitHubRepositoryDaoList.size());
			dao.setGitHubRepositories(gitHubRepositoryDaoList);
			dao.setGitHubStatisticalData(fillStatisticalDataDao(gitHubRepositoryDaoList));
		}
		return dao;
	}

	private List<GitHubStatisticalDataDao> fillStatisticalDataDao(List<GitHubRepositoryDao> gitHubRepositoryDaoList) {
		List<GitHubStatisticalDataDao> gitHubStatisticalDataDaoList = new ArrayList<>();

		if (!ListUtils.isEmpty(gitHubRepositoryDaoList)) {

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
		if (!ListUtils.isEmpty(repoList)) {
			gitHubStatisticalDataDao.setLanguageRatio((repoList.size() / (double) amountOfRepositories) * 100);
		}
		gitHubStatisticalDataDaoList.add(gitHubStatisticalDataDao);
	}
}
