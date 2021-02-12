package com.verbit.github.resume;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.verbit.github.resume.client.GitHubRestClient;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GitHubResumeApplicationTests {

	@Autowired
	GitHubRestClient restClient;

	public final List<String> testAccountList = Arrays.asList("qwertzalcoatl", "shrink0r");

	@Test
	void testGitHubResumeSuccessfull() {
		testAccountList.forEach(user -> sendRequest(user));
	}

	void sendRequest(String user) {
		assertThat(restClient.getGitHubAccount(user)).isNotNull();
	}

}
