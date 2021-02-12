package com.verbit.github.resume.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GitHubAccountOwnerDto {

	@JsonProperty("login")
    private String login;
	
	@JsonProperty("id")
    private Integer id;
    
	@JsonProperty("node_id")
    private String nodeId;
    
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    @JsonProperty("gravatar_id")
    private String gravatarId;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("html_url")
    private String htmlUrl;
    
    @JsonProperty("followers_url")
    private String followersUrl;
    
    @JsonProperty("following_url")
    private String followingUrl;
    
    @JsonProperty("gists_url")
    private String gistsUrl;
    
    @JsonProperty("starred_url")
    private String starredUrl;
    
    @JsonProperty("subscriptions_url")
    private String subscriptionsUrl;
    
    @JsonProperty("organizations_url")
    private String organizationsUrl;
    
    @JsonProperty("repos_url")
    private String reposUrl;
    
    @JsonProperty("events_url")
    private String eventsUrl;
    
    @JsonProperty("received_events_url")
    private String receivedEventsUrl;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("site_admin")
    private Boolean siteAdmin;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("company")
    private String company;
    
    @JsonProperty("blog")
    private String blog; 
}